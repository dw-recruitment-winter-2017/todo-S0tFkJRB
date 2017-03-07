(ns dbcfg
  (:require [datomic.api :only [q db squuid] :as d]))

(def uri "datomic:mem://todo")

(def schema-path "resources/db/schema.edn")

(def hardcoded-uuid "58bd2b73-4349-4726-83bc-9804fe74b125")

(defn build-todo [tm]
  {:todo/id (if (nil? (:id tm)) (d/squuid) (:id tm))
   :todo/description (:description tm)
   :todo/done (if (nil? (:done tm)) false (:done tm))
   :todo/removed (if (nil? (:removed tm)) false (:removed tm))})

(defn get-conn []
  (d/connect uri))

(defn get-schema []
  (read-string (slurp schema-path)))

;; This is a little verbose so I can just reuse `build-todo`
(defn get-data []
  [(build-todo {:description "Buy candles"})
   (build-todo {:description "Hang art"})
   (build-todo {:description "Wash slipcover"})
   (build-todo {:description "Unpack dishes"})
   (build-todo {:description "Buy CFLs"})
   (build-todo {:description "Find cat"})
   ;; Use the hardcoded UUID here to aid with testing; it's a pain to copy
   ;; and paste UUIDs all the time when you're mutating and deleting a todo.
   {:todo/id (java.util.UUID/fromString hardcoded-uuid)
    :todo/description "Buy tea"
    :todo/done true
    :todo/removed false}])

(defn load-schema []
  (let [conn (get-conn)]
    @(d/transact conn (get-schema))))

(defn load-data []
  (let [conn (get-conn)]
    @(d/transact conn (get-data))))

(defn load-schema-and-data []
  (let [conn (get-conn)]
    @(d/transact conn (get-schema))
    @(d/transact conn (get-data))))

(defn create-db []
  (d/create-database uri))

(defn delete-db []
  (d/delete-database uri))

(defn recreate-db []
  (if-not (d/create-database uri)
    (d/delete-database uri)
    (d/create-database uri)))

(defn build-db []
  (create-db)
  (load-schema))

(defn rebuild-db []
  (recreate-db)
  (load-schema))

(defn build-and-load-db []
  (create-db)
  (load-schema-and-data))

(defn rebuild-and-load-db []
  (recreate-db)
  (load-schema-and-data))
