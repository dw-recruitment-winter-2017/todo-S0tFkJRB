(ns repl
  (:require [dbcfg]
            [fecfg]
            [response]
            [controllers]
            [routes]
            [server]))

(defn restart []
  (dbcfg/delete-db)
  (require :reload '[dbcfg :as dbcfg]
                   '[fecfg :as fecfg]
                   '[response :as response]
                   '[controllers :as controllers]
                   '[routes :as routes]
                   '[server :as server])
  (server/restart))
