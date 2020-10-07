(ns cljs-dev.dev
  (:require [figwheel.main.api :as figwheel]))

(def ^:private web-root "resources")

(defn figwheel-config
  ([]
   (figwheel-config web-root))
  ([web-root]
   {:watch-dirs          ["test" "src"]
    :target-dir          web-root
    :build-inputs        [:watch-dirs "dev"]
    :css-dirs            [(str web-root "/public/css")]
    :auto-testing        true
    :ring-server-options {:port 9501}
    :connect-url         "ws://[[client-hostname]]:[[server-port]]/figwheel-connect"
    :open-url            false}))


(defn with-devcards [conf main-ns])

(defn figwheel-opts
  ([opts]
   (figwheel-opts web-root opts))
  ([web-root opts]
   (merge {:output-to       (str web-root "/public/cljs-out/main.js")
           :closure-defines {"goog.DEBUG"                          true
                             "re_frame.trace.trace_enabled_QMARK_" true}
           :optimizations   :none
           :preloads        ['devtools.preload 'day8.re-frame-10x.preload]
           :source-map      true
           :devcards        true
           :external-config #:devtools{:config {:features-to-install   [:formatters :hints]
                                                :fn-symbol             "F"
                                                :print-config-override true}}
           :pretty-print    true}
          opts)))

(defn start-figwheel! [main-config options]
  (figwheel/start {:mode :serve}
                  {:id      "dev"
                   :options options
                   :config  main-config}))

(defn stop-figwheel! []
  (figwheel/stop-all))

(defn repl []
  (figwheel/cljs-repl "dev"))