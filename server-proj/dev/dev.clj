(ns dev
  (:require [juxt.clip.core :as clip]
            [juxt.clip.repl :refer [start stop reset set-init! system]]))


(defn start-server []
  (prn "Server started")
  {:server-config {}})


(def system-config
  {:components {
                :sys/server {:start start-server}}})

(set-init! #(identity system-config))