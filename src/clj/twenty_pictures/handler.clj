(ns twenty-pictures.handler
  (:require
   [castra.middleware :as castra]
   [clojure.data.json :as json]
   (compojure [core :as c]
              [handler :as handler1]
              [route :refer [resources not-found]])
   (friend-oauth2 [util :refer [format-config-uri get-access-token-from-params]]
                  [workflow :as oauth2])
   [ring.adapter.jetty :refer [run-jetty]]
   [ring.middleware.defaults :as d]
   [ring.middleware.params :as p]
   [ring.middleware.session :refer [wrap-session]]
   [ring.util.codec :as codec]
   [ring.util.response :as response :refer [response]]
   [cemerick.friend :as friend]
   (cemerick.friend [workflows :as workflows]
                    [credentials :as creds])
   (twenty-pictures [api :as api]
                    [board-static :as board-static])))

(def server (atom nil))

(defn trace [in-fn]
  (fn [& args]
    (println "vvv")
    (println "The args were: " args)
    (let [result (apply in-fn args)]
      (println "The result was: " result)
      (println "^^^")
      result)))


(defn credential-fn [token]
  {:identity token
   :roles #{::user}})

(def client-config
  {:client-id "228406207564863"
   :client-secret "fbd46d9d62aa6ac17c27846fad4f5bea"
   :callback {:domain "http://srv2.elangley.org:9090"
              :path "/facebook.callback"}})

(def uri-config
  {:authentication-uri {:url "https://www.facebook.com/dialog/oauth"
                        :query {:client_id (:client-id client-config)
                                :redirect_uri (format-config-uri client-config)}}
   :access-token-uri   {:url "https://graph.facebook.com/oauth/access_token"
                        :query {:client_id (:client-id client-config)
                                :client_secret (:client-secret client-config)
                                :redirect_uri (format-config-uri client-config)}}})

(def friend-config
  {:allow-anon? true
   :workflows [(oauth2/workflow
                {:client-config client-config
                 :uri-config uri-config
                 :access-token-parsefn (trace #(-> %
                                                   :body
                                                   codec/form-decode
                                                   (get "access_token")))
                 :credential-fn (trace credential-fn)})]})

(defn make-json-responder [result]
  (fn [& args]
    {:body (json/write-str result)
     :headers {"Content-Type" "application/json"}
     :status 200}))

(defn make-api-response [hash]
  {:success true
   :result hash})

(def json-responder-partial
  (comp make-json-responder
        make-api-response))

(def get-boards (comp json-responder-partial
                      api/get-all-boards))

(def get-board-by-id (comp json-responder-partial
                           api/get-photos-in-board))

(c/defroutes app-routes
  (c/GET "/status" request
         (let [count (:count (:session request) 0)
               session (assoc (:session request) :count (inc count))]
           (-> (ring.util.response/response
                (str "<p>We've hit the session page " (:count session)
                     " times.</p><p>The current session: " session "</p>"))
               (assoc-in [:headers "Content-Type"] "text/html")
               (assoc :session session))))
  
  (c/GET "/bar" req
         "baz")
  (c/GET "/foo" req
         (friend/authorize #{::user} "bar"))

  (c/GET "/board/:id" [id]
         (board-static/view (api/get-photos-in-board id)
                            id))
  (resources "/" {:root ""})
  (p/wrap-params
   (c/GET "/" {{frag "_escaped_fragment_" :as qs} :query-params :as req}
          (println "\nreq was:" req "\nqs was:" qs "\nfrag was:" frag "\n")
          (response/content-type (if frag
                                   (response/redirect frag 301)
                                   (response/resource-response "index.html"))
                                 "text/html"))))


(def handler
  (-> app-routes
      ))

(c/defroutes foo-routes
  (c/GET "/bar" req
         "baz")
  (c/GET "/foo" req
         (friend/authorize #{::user} "bar")))

(def handler-new
  foo-routes)

(def app
  (-> handler
      (friend/authenticate {:allow-anon? true
                            :workflows [(oauth2/workflow
                                         {:client-config client-config
                                          :uri-config uri-config
                                          :access-token-parsefn get-access-token-from-params
                                          :credential-fn credential-fn})]})
      handler1/site
      (castra/wrap-castra 'twenty-pictures.api)
      (castra/wrap-castra-session "foo qwe rew ioo ")
      (d/wrap-defaults d/api-defaults)
      ))


;; (def app
;;   (friend/authenticate
;;    ring-app
;;    {:allow-anon? true
;;     :workflows [(oauth2/workflow
;;                  {:client-config client-config
;;                   :uri-config uri-config
;;                   :access-token-parsefn get-access-token-from-params
;;                   :credential-fn credential-fn})]}))

;; (defn handler [{session :session}]
;;   (let [count (:count session 0)
;;         session (assoc session :count (inc count))]
;;     (-> (response (str "You accessed this page " count " times."
;;                        "<br><script>document.write(document.cookie)</script>"))
;;         (assoc :session session)
;;         )))

(defn start-server
  "Start castra demo server"
  [port]
  (swap! server #(or % (app port))))
