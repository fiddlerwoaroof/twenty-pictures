(ns twenty-pictures.handler
  (:require
   [castra.middleware :as castra]
   [compojure.core :as c]
   [compojure.route :refer [resources not-found]]
   [ring.adapter.jetty :refer [run-jetty]]
   [ring.middleware.defaults :as d]
   [ring.util.response :as response]))

(def server (atom nil))


(c/defroutes app-routes
  (c/GET "/foo" req
         "bar")
  (resources "/" {:root ""})
  (c/GET "/" req
         (response/content-type (response/resource-response "index.html")
                                "text/html")))

(def handler
  (-> app-routes
      (castra/wrap-castra 'twenty-pictures.api)
      (castra/wrap-castra-session "foo qwe rew ioo ")
      (d/wrap-defaults d/api-defaults)))

(defn app [port]
  (-> handler
      (run-jetty {:join? false
                  :port port})))

(defn start-server
  "Start castra demo server"
  [port]
  (swap! server #(or % (app port))))
