(ns dev
  (:require [juxt.clip.core :as clip]
            [server.core :as srv]
            [juxt.clip.repl :refer [start stop reset set-init! system]]))

(def system-config
  {:components {
                :sys/server {:start srv/start-server}}})

(set-init! #(identity system-config))