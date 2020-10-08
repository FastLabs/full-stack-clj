(ns server.core
  (:require [aleph.http :as http]
            [manifold.deferred :as md]
            [manifold.stream :as ms]
            [cheshire.core :as json]
            [ring.util.response :refer [response]]
            [compojure.route :as route]
            [compojure.core :as comp :refer [GET]]
            [ring.util.response :as resp]))

(defn listen-handler [req]
  (-> (http/websocket-connection req)
      (md/chain (fn [socket]
                  (ms/connect
                    (->> (ms/periodically 3000
                                          (fn [] {:data "some - data"}))
                        (ms/map json/encode))
                    socket)))

      (md/catch (fn [_]
                  {:status  400
                   :headers {"content-type" "application/text"}
                   :body    "Unable to create websocket connection"}))))




(defn app-routes []
  (comp/routes
    (comp/context "/api" []
      (GET "/health" [] (->> (response "ok"))))
    (GET "/listen" [:as req] (listen-handler req))
    (GET "/" [] (resp/redirect "/index.html"))
    (route/resources "/")))

(defn start-server []
  (http/start-server (app-routes) {:port 8080}))
