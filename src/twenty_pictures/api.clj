(ns twenty-pictures.api
  (:require [castra.core :refer [defrpc *session*]]))


(defonce image-list (atom (vec (repeat 20 {:text "foo"
                                           :background "http://placehold.it/1000x1000"}))))

(defrpc get-images []
  @image-list)

(defrpc get-image [n]
  (@image-list n))

(defrpc save-image [n url]
  (println "The line was: " (type n) n url)
  (swap! image-list assoc-in [n :background] url)
  (println (@image-list n))
  url)
