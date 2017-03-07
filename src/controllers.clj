(ns controllers
  (:require [datomic.api :only [q db] :as d]
            [response]
            [dbcfg]))

(defn show-about-page [context] "About page, WIP")

(defn to-todo [[id description done]]
  {:id (str id) :description description :done done})

(defn list-todos [context]
  (let [conn (dbcfg/get-conn)
        db (d/db conn)]
    (response/ok
      (map to-todo
        (d/q '[:find ?id ?description ?done
               :where [?e :todo/id ?id]
                      [?e :todo/description ?description]
                      [?e :todo/done ?done]
                      [?e :todo/removed false]] db)))))

(defn create-todo [context])
(defn update-todo [context])
(defn delete-todo [context])
