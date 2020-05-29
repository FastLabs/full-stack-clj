(ns ^:figwheel-hooks client-app.core
  (:require [goog.dom :as gdom]
            [reagent.core :as ra]
            [reagent.dom :as rd]
            [re-frame.core :as rf]))


(defn mount-app-element []
  (when-let [el (gdom/getElement "app")]
    (rd/render [:div "Hello 11"] el)))

(defn ^:export init []
  (mount-app-element))

(defn ^:after-load on-reload []
  (mount-app-element))