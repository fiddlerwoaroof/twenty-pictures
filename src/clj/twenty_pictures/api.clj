(ns twenty-pictures.api
  (:require [castra.core :refer [defrpc *session*]]
            [twenty-pictures.db :refer [db]]
            [twenty-pictures.db-access :as db-access]))


(defonce image-list (atom (vec (repeat 20 {:text "foo"
                                           :initialized nil
                                           :background "http://placehold.it/1000x1000"}))))

(defn board-constructor []
  {:text "" :background (str "https://source.unsplash.com/category/nature/"
                                     (+ 2000 (rand-int 500))
                                     "x"
                                     (+ 2000 (rand-int 500)))})

(def boards
  (atom {"foo" (vec (map #(do %
                              (board-constructor))
                         (repeat 20 nil)))}))

(defrpc get-board [name]
  (println "The name is: " name)
  (println "Foo's board is: " (get-in @boards [name]))
  (println "Available boards: " (map type (keys @boards)))
  (println boards)
  (println (get boards (first (keys @boards))))

  (@boards name))

(defrpc save-board [name board]
  (swap! boards assoc-in [name] board))

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

(defrpc get-all-boards []
  (twenty-pictures.db-access/get-all-photo-boards db))


(defrpc get-photos-in-board [id]
  (twenty-pictures.db-access/get-photos-in-board-by-uuid db {:id id}))
