(ns routes
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]
            [controllers]))

(def routes #{
  ["/about"             :get    [controllers/show-about-page]
                                :route-name :about-page]
  ["/api/todo"          :get    [http/json-body
                                 controllers/list-todos]
                                :route-name :list-todos]
  ["/api/todo"          :post   [(body-params/body-params)
                                 http/json-body
                                 controllers/create-todo]
                                :route-name :create-todo]
  ["/api/todo/:todo-id" :patch  [(body-params/body-params)
                                 http/json-body
                                 controllers/update-todo]
                                :route-name :update-todo]
  ["/api/todo/:todo-id" :delete [controllers/delete-todo]
                                :route-name :delete-todo]
})
