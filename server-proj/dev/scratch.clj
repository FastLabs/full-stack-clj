(ns scratch
  (:require [manifold.deferred :as md]
            [manifold.stream :as ms]))

(defn periodic-stream [scale]
  (ms/periodically 1000 (fn []
                          (rand-int scale))))



(defn start-simulation []
  (let [aggregated-stream (ms/stream)]
    (ms/consume prn aggregated-stream)
    (doseq [name [100 50 40 500 100]]
      (ms/connect  (periodic-stream name) aggregated-stream))))