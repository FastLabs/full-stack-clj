(ns cljs-dev.dist-build
  (:require [cljs.build.api]))

(defn dist-compile [main-ns additional-opts]
  (cljs.build.api/build
    (merge {:main          main-ns
            :infer-externs true
            :output-dir    "out"
            :output-to     "public/cljs-out/main.js"
            :optimizations :advanced}
           additional-opts)))