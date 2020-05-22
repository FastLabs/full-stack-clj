(ns server.core
  (:require [aleph.http :as http]
            [ring.util.response :refer [response]]
            [compojure.route :as route]
            [compojure.core :as comp :refer [GET]]
            [ring.util.response :as resp]))


(defn app-routes []
  (comp/routes
    (comp/context "/api" []
      (GET "/health" [] (->> (response "ok"))))
    (GET "/" [] (resp/redirect "/index.html"))
    (route/resources "/")))

(defn start-server []
  (http/start-server (app-routes) {:port 8080}))
