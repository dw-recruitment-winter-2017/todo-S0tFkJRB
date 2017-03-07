(ns frontend.core
  (:require [reagent.core :as r]
            [ajax.core :as ajax]))

(enable-console-print!)

(def key-enter 13)

(def key-escape 27)

;; General app state managers

(defonce app-state (r/atom {:todos {}}))

(defn get-todo [id]
  (get (:todos @app-state) id))

(defn add-todo! [new-todo]
  (reset! app-state
    {:todos (assoc (:todos @app-state) (:id new-todo) new-todo)}))

(defn reset-todo! [new-todo]
  (reset! app-state
    {:todos (assoc (:todos @app-state) (:id new-todo) new-todo)}))

(defn remove-todo! [id]
  (reset! app-state
    {:todos (dissoc (:todos @app-state) id)}))

(defn load-todos! [new-todos]
  (reset! app-state
    {:todos (zipmap (map (fn [todo] (:id todo)) new-todos) new-todos)}))

;; Wrappers that send state update requests to the backend, and then call
;; the appropriate local (frontend) state manager on success

(defn todo-add [description]
  (println "Hey there")
  (ajax/ajax-request
    {:uri "/api/todo"
     :method :post
     :format (ajax/json-request-format)
     :response-format (ajax/json-response-format {:keywords? true})
     :params {:description description}
     :handler (fn [[ok res]] (if ok (add-todo! (js->clj res))
                                    (println (str res))))}))

(defn todo-toggle [id]
  (let [todo (get-todo id)]
    (if-not (nil? todo)
      (ajax/ajax-request
        {:uri (str "/api/todo/" id)
         :method :patch
         :format (ajax/json-request-format)
         :response-format (ajax/json-response-format {:keywords? true})
         :params {:done (not (:done todo))}
         :handler (fn [[ok res]] (if ok (reset-todo! (js->clj res))
                                        (println (str res))))}))))

(defn todo-remove [id]
  (let [todo (get-todo id)]
    (if-not (nil? todo)
      (ajax/ajax-request
        {:uri (str "/api/todo/" id)
         :method :delete
         :format :raw
         :response-format :raw
         :handler (fn [[ok res]] (if ok (remove-todo! id)
                                        (println (str res))))}))))

(defn todo-remove-all []
  (doseq [id (keys (:todos @app-state))] (todo-remove id)))

;; Components

(defn todo-input-component [{:keys [description on-save on-stop]}]
  (let [desc (r/atom description)
        stop (fn [] (reset! desc "") (if on-stop (on-stop)))
        save #(let [v (-> @desc str clojure.string/trim)]
                (if-not (empty? v) (on-save v))
                (stop))]
    (fn [{:keys [id class placeholder]}]
      [:input {:type "text"
               :value @desc
               :id "new-todo"
               :class "input is-large is-fullwidth"
               :placeholder "(Add an entry...)"
               :on-blur stop
               :on-change (fn [ev] (reset! desc (-> ev .-target .-value)))
               :on-key-down (fn [ev]
                              (let [keycode (.-which ev)]
                                (cond
                                  (= keycode key-enter) (save)
                                  (= keycode key-escape) (stop))))
               }])))

(defn todo-item-component []
  (fn [{:keys [id description done]}]
    [:div {:class "columns"}
      [:div {:class "column is-10 notification is-info is-offset-1"}
        [:div {:class "columns"}
          [:div {:class "column is-1"}
            [:a {:class (str "button " (if done "is-success" "is-warning"))
                 :on-click (fn [ev] (todo-toggle id))}
              (if done "Undo" "Do")]]
          [:div {:class "column is-10"}
            [:button {:class "delete" :on-click (fn [ev] (todo-remove id))}]
            [:p {:class "title is-3"} description]]]]
    [:div [:p]]]))

(defn app [props]
  (fn []
    (let [todos (vals (:todos @app-state))]
      [:div
        [:h3 {:class "title is-1 has-text-centered"} "List of To Dos!"]
        [:div#todoapp
          [:div {:class "columns"}
            [:div {:class "column is-8 is-offset-2"}
              [todo-input-component {:on-save todo-add}]]]
        (when (-> todos count pos?)
          [:div
            [:div#main
              [:div#todo-list
                (for [todo todos]
                  ^{:key (:id todo)} [todo-item-component todo])]]
              [:div {:class "columns"}
                [:div {:class "column is-8 is-offset-5"}
                  [:a {:class "button is-danger is-large"
                       :on-click (fn [ev] (todo-remove-all))}
                    "Remove all"]]]])]])))

(defn load-app []
  (ajax/ajax-request
    {:uri "/api/todo"  ;; Would be better to use whatever `url-for` is
     :method :get
     :response-format (ajax/json-response-format {:keywords? true})
     :handler (fn [[ok res]]
                (if ok ((load-todos! (js->clj res))
                        (r/render [app] (js/document.getElementById "app")))
                  (println (str res))))}))

;; Entry point

(defn ^:export run [] (load-app))
