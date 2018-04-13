(ns chat-history-layout.core
  (:require [reagent.core :as r]
            [chat-history-layout.lorem :as lorem]
            [goog.dom]))

(def by-id goog.dom.getElement)
(def get-text goog.dom.getTextContent)

(defn make-history []
  (let [logs (take 50 (cycle
                       [(fn [] [:li.received {:key (gensym)} (lorem/sentence)])
                        (fn [] [:li.sent {:key (gensym)} (lorem/sentence)])]))]
    [:ul {}
     (map (fn [f] (f)) logs)]))

(defn make-choices []
  (mapv lorem/sentence (range 3)))

(def state (r/atom {:history (make-history)
                    :next-message :li.received
                    :choices (make-choices)}))

(def next-message {:li.received :li.sent
                   :li.sent :li.received})

(defn choice-click [e]
  (let [elt (.-target e)
        text (get-text elt)
        new-tag (@state :next-message)
        new-message [new-tag {:key (gensym)} text]
        new-next-message (next-message new-tag)
        history (@state :history)
        new-history (conj history new-message)
        updated-state {:history new-history
                       :next-message new-next-message
                       :choices (make-choices)}]

    (swap! state merge updated-state)))

(defn app-container []
  [:div.container {}
   [:div.header {}
    [:ul.stats {}
     [:li.status {} "Status 1"]
     [:li.status {} "Status 2"]
     [:li.status {} "Status 3"]]]
   [:div.chat {}
    [:div.history {}
     (@state :history)]
    [:div.choices {}
     [:ul {}
      [:li.choice {:on-click choice-click} (get-in @state [:choices 0])]
      [:li.choice {:on-click choice-click} (get-in @state [:choices 1])]
      [:li.choice {:on-click choice-click} (get-in @state [:choices 2])]]]]])

(r/render-component [app-container] (by-id "app"))

;;; Install the service worker
(when (exists? js/navigator.serviceWorker)
  (-> js/navigator
      .-serviceWorker
      (.register "/sw.js")
      (.then #(js/console.log "Server worker registered."))))
