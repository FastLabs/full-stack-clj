(ns user)

(defn dev []
  (binding [*warn-on-reflection* false]
    (require 'dev)
    (in-ns 'dev)))