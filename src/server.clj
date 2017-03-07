(ns server
  (:require [io.pedestal.http :as http]
            [dbcfg]
            [routes]))

(def service-map
  {::http/routes routes/routes
   ::http/type :jetty
   ::http/port 8090})

(defn start []
  (http/start (http/create-server service-map)))

(defonce server (atom nil))

(defn start-dev []
  (dbcfg/build-and-load-db)
  (reset! server
    (http/start
      (http/create-server
        (merge service-map
          {::http/join? false})))))

(defn stop-dev []
  (http/stop @server))

(defn restart []
  (stop-dev)
  (start-dev))
