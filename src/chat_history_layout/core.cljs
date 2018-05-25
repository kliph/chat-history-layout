(ns chat-history-layout.core
  (:require [reagent.core :as r]
            [chat-history-layout.lorem :as lorem]
            [soda-ash.core :as sa]
            [goog.dom]))

(def by-id goog.dom.getElement)
(def get-text goog.dom.getTextContent)

(defmulti history-item (fn [type props content] type))
(defmethod history-item :li.received [type props content]
  [sa/ListItem props
   [sa/Icon {:name "right triangle"}]
   content])
(defmethod history-item :li.sent [type props content]
  [sa/ListItem (merge props {:className "sent"})
   content])

(defn make-history []
  (let [logs (take 50 (cycle
                       [(fn [] (history-item :li.received {:key (gensym)} (lorem/sentence)))
                        (fn [] (history-item :li.sent {:key (gensym)} (lorem/sentence)))]))]
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
        new-message (history-item new-tag {:key (gensym)} text)
        new-next-message (next-message new-tag)
        history (@state :history)
        new-history (conj history new-message)
        updated-state {:history new-history
                       :next-message new-next-message
                       :choices (make-choices)}]

    (swap! state merge updated-state)))

(defn history []
  [sa/ListSA {:className "history"}
   (@state :history)])

(defn app-container []
  [sa/Container {}
   [sa/Menu {:fixed "top"
             :stackable true
             :widths 3
             :fluid true}
    [sa/MenuItem {} "Status 1"]
    [sa/MenuItem {} "Status 2"]
    [sa/MenuItem {} "Status 3"]]
   [:div.chat {}
    [history]]
   [sa/Menu {:fixed "bottom"
             :stackable true
             :vertical true
             :fluid true}
    [sa/MenuItem {:on-click choice-click
                  :link true}
     [sa/Header (get-in @state [:choices 0])]]
    [sa/MenuItem {:on-click choice-click
                  :link true}
     [sa/Header (get-in @state [:choices 1])]]
    [sa/MenuItem {:on-click choice-click
                  :link true}
     [sa/Header (get-in @state [:choices 2])]]]])

(r/render-component [app-container] (by-id "app"))

;;; Install the service worker
(when (exists? js/navigator.serviceWorker)
  (-> js/navigator
      .-serviceWorker
      (.register "/sw.js")
      (.then #(js/console.log "Server worker registered."))))
