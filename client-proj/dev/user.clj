(ns user)

(defn cljs []
  (require 'cljs)
  (in-ns 'cljs)
  (eval '(start-figwheel!)))