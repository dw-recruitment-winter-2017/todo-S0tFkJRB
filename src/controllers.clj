(ns controllers
  (:require [datomic.api :only [q db] :as d]
            [response]
            [dbcfg]))

;; A few helpers

(defn to-todo [[id description done]]
  {:id id :description description :done done})

(defn to-str-todo [[id description done]]
  {:id (str id) :description description :done done})

(defn build-todo [tm]
  {:id (d/squuid)
   :description (:description tm)
   :done (if (nil? (:done tm)) false (:done tm))})

(defn build-updated-todo [tm]
  (merge {}
    (if (contains? tm :description) {:description (:description tm)})
    (if (contains? tm :done) {:done (:done tm)})))

;; Controllers

(defn show-about-page [context] "About page, WIP")

(defn list-todos [context]
  (let [conn (dbcfg/get-conn)
        db (d/db conn)]
    (response/ok
      (map to-str-todo
        (d/q '[:find ?id ?description ?done
               :where [?e :todo/id ?id]
                      [?e :todo/description ?description]
                      [?e :todo/done ?done]
                      [?e :todo/removed false]] db)))))

(defn create-todo [context]
  (let [payload (:json-params context)]
    (if-not (nil? (:description payload))
      (let [conn (dbcfg/get-conn)
            db (d/db conn)
            todo (build-todo payload)]
        @(d/transact conn [(dbcfg/build-todo todo)])
        (response/created (assoc todo :id (str (:id todo)))))
      (response/badrequest))))

(defn update-todo [context]
  (let [payload (:json-params context)
        given-id (:todo-id (:path-params context))]
    (if-not (nil? given-id)
      (let [conn (dbcfg/get-conn)
            db (d/db conn)
            uuid (java.util.UUID/fromString given-id)
            results (d/q '[:find ?id ?description ?done
                           :in $ ?id
                           :where [?e :todo/id ?id]
                                  [?e :todo/description ?description]
                                  [?e :todo/done ?done]
                                  [?e :todo/removed false]] db uuid)
            result (first results)]
        (if-not (nil? result)
          (let [todo (assoc
                       (merge
                         (to-todo result)
                         (build-updated-todo payload))
                       :id (first result))
                dbtodo (dbcfg/build-todo todo)]
            @(d/transact conn [dbtodo])
            (response/ok (assoc todo :id (str (:id todo)))))
          (response/notfound)))
    (response/badrequest))))

(defn delete-todo [context])
