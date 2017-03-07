(ns frontend.core
  (:require [reagent.core :as r]
            [ajax.core :as ajax]))

(enable-console-print!)

(defonce app-state (r/atom {:todos {}}))

(defn get-todo [id]
  (get (:todos @app-state) id))

(defn load-todos! [new-todos]
  (reset! app-state
    {:todos (zipmap (map (fn [todo] (:id todo)) new-todos) new-todos)}))

(defn app [props]
  (println (:todos @app-state)))

(defn load-app []
  (ajax/ajax-request
    {:uri "/api/todo"  ;; Would be better to use whatever `url-for` is
     :method :get
     :response-format (ajax/json-response-format {:keywords? true})
     :handler (fn [[ok res]]
                (if ok ((load-todos! (js->clj res))
                        (r/render [app] (js/document.getElementById "app")))
                  (println (str res))))}))

(defn ^:export run [] (load-app))
