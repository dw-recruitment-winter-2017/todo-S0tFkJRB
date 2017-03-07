(ns response)

(defn ok [body] {:status 200 :body body :headers {}})
(defn created [body] {:status 201 :body body :headers {}})
(defn nocontent [] {:status 204 :body "" :headers {}})
(defn badrequest [] {:status 400 :body "Bad Request" :headers {}})
(defn notfound [] {:status 404 :body "Not Found" :headers {}})


