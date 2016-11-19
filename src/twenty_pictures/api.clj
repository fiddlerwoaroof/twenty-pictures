(ns twenty-pictures.api
  (:require [castra.core :refer [defrpc *session*]]))


(defonce image-list (atom (vec (repeat 20 {:text "foo"
                                           :initialized nil
                                           :background "http://placehold.it/1000x1000"}))))

(defrpc get-images []
  @image-list)

(defrpc get-image [n]
  (@image-list n))

;; We save the image and post complete state back
(defrpc save-image [n url]
  (println "The line was: " (type n) n url)
  (swap! image-list assoc-in [n :background] url)
  (println (@image-list n))
  (get-in @image-list [n]))
