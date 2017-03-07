(ns frontend.core
  (:require [reagent.core :as r]
            [ajax.core :as ajax]))

(enable-console-print!)

(defn load-app []
  (println "Hey there")
  (ajax/ajax-request
    {:uri "/api/todo"  ;; Would be better to use whatever `url-for` is
     :method :get
     :response-format (ajax/json-response-format {:keywords? true})
     :handler (fn [[ok res]]
                (if ok (println (str res))))}))

(defn ^:export run [] (load-app))
