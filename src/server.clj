(ns server
  (:require [io.pedestal.http :as http]
            [dbcfg]
            [routes]))

(def service-map
  {::http/routes routes/routes
   ::http/type :jetty
   ::http/port 8890})

(defn start []
  (http/start (http/create-server service-map)))

(defonce server (atom nil))

(defn start-dev []
  (dbcfg/build-and-load-db)
  (reset! server
    (http/start
      (http/create-server
        (merge service-map
          {::http/join? false
           ::http/allowed-origins ["http://localhost:8890"]
           ::http/resource-path "/static"
           ::http/file-path "/static"})))))

(defn stop-dev []
  (http/stop @server))

(defn restart []
  (stop-dev)
  (start-dev))
