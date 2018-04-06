(ns chat-history-layout.lorem
  (:require [clojure.string :as s]))

(def words
  (->
    "nulla at felis elementum porttitor sem quis condimentum felis vivamus augue
    magna sagittis nec vestibulum sed dictum eu eros donec facilisis est at mi
    bibendum finibus praesent cursus eros et risus dignissim tincidunt pellentesque
    quam vehicula vivamus eleifend magna ultrices tortor facilisis viverra nunc
    tincidunt tortor sit amet justo volutpat sit amet venenatis nisi mollis donec
    vel pellentesque libero maecenas tincidunt sem id nunc pretium posuere
    suspendisse accumsan justo tellus quis semper erat maximus vel etiam nec
    ipsum sem  aliquam nisl magna vulputate a commodo in pellentesque ac lorem
    sed sodales bibendum nisi fermentum laoreet praesent quis metus interdum mattis"
    (s/split #"\W+")
    set))

(defn sentence []
  (let [word-count (max 5 (inc (rand-int 29)))]
    (s/join " " (take word-count (shuffle words)))))
