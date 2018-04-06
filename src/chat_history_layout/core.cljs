(ns chat-history-layout.core
  (:require [reagent.core :as r]
            [chat-history-layout.lorem :as lorem]
            [goog.dom]))

(def by-id goog.dom.getElement)

(defn make-history []
  (let [logs (take 50 (cycle
                       [(fn [] [:li.received {} (lorem/sentence)])
                        (fn [] [:li.sent {} (lorem/sentence)])]))]
    [:ul {}
     (map (fn [f] (f)) logs)]))

(defn app-container []
  [:div.container {}
   [:div.header {}]
   [:div.chat {}
    [:div.history {}
     (make-history)]
    [:div.choices {}
     [:ul {}
      [:li.choice {} (lorem/sentence)]
      [:li.choice {} (lorem/sentence)]
      [:li.choice {} (lorem/sentence)]]]]])

(r/render-component [app-container] (by-id "app"))

;;; Install the service worker
(when (exists? js/navigator.serviceWorker)
  (-> js/navigator
      .-serviceWorker
      (.register "/sw.js")
      (.then #(js/console.log "Server worker registered."))))
