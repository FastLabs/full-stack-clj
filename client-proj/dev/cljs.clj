(ns cljs
  (:require [cljs-dev.dev :as dev]
            [cljs-dev.dist-build :as dist]))

(def dev-compiler-opts (merge dev/base-options {:main 'client-app.core}))

;TODO: review css dir if required here as it is inherited from the default
(def fig-config (-> dev/main-config (assoc :watch-dirs ["src"]
                                           :css-dirs ["../web-root/public/css"])))
(defn start-figwheel! []
  (dev/start-figwheel! fig-config dev-compiler-opts))

(defn dist-build! []
  (dist/dist-compile 'ss {}))


(defn -main [& args]
  (let [build (keyword (first args))]
    (case build
      :dist (dist-build!))))