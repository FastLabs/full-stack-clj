(ns ^:figwheel-hooks client-app.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [goog.dom :as gdom]
            [reagent.core :as ra]
            [reagent.dom :as rd]
            [haslett.client :as ws]
            [haslett.format :as fmt]
            [clojure.core.async :as ay]
            [re-frame.core :as rf]))

;TODO: think about storing in the db
(defonce ev-stream-ws (atom nil))

(rf/reg-event-db
  :initialise
  (fn [_ [_ app-config]]
    {:watched    #{}
     :app-config app-config}))

(rf/reg-event-db
  :request-stream
  (fn [db {:keys [subscription-id]}]
    (let [{:keys [sink]} @ev-stream-ws
          watched (:watched db)]
      (when (not (contains? watched subscription-id))
        (ay/put! sink {:subscription subscription-id :params []}))
      (update db :watched conj subscription-id))))          ;todo: look into subscription id and params

(rf/reg-event-db
  :sub-chunk-received
  (fn [db [_ chunk]]
    (prn "Chunk received: " chunk)
    db))


(def my-json
  "Haslett payload parsing"
  (reify fmt/Format
    (read [_ s]
      (prn s)
      (js->clj (js/JSON.parse s) :keywordize-keys true))
    (write [_ v]
      (js/JSON.stringify (clj->js v)))))

(defn consume-stream [{:keys [host-name port]}]
  (prn "WS connection to: " host-name ":" port)
  (go (let [{:keys [sink source] :as stream} (ay/<! (ws/connect (str "ws://" host-name ":" port "/listen") {:format my-json}))]
        (reset! ev-stream-ws stream)
        (loop [data (ay/<! source)]
          (when data
            (rf/dispatch [:sub-chunk-received data]))
          (when (ws/connected? stream)
            (recur (ay/<! source)))))))

(defn host-details []
  {:host-name (-> js/location .-hostname)
   :port      (-> js/location .-port)})

(defn mount-app-element []
  (when-let [el (gdom/getElement "app")]
    (rd/render [:div "Hello World!"] el)
    (rf/dispatch-sync [:initialise (host-details)])
    (consume-stream (host-details))))                       ;TODO: check how to initialize the websocket

(defn ^:export init []
  (mount-app-element))

(defn ^:after-load on-reload []
  (when @ev-stream-ws
    (do
      (prn "Close web-socket")
      (ws/close @ev-stream-ws)))
  (mount-app-element))