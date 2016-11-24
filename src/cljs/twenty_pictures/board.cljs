(ns twenty-pictures.board
  (:require-macros [javelin.core :refer [defc defc= dosync]]
                   [hoplon.core :refer [defelem]])
  (:require [clojure.string]
            [javelin.core :as j :refer [cell=]]
            [hoplon.core :as h :refer [main article div section iframe]]))

(defelem image-pane [{:keys [toggled image click]} children]
  (div :class (cell= {"card" true
                      "toggled" toggled})
       (apply section
              :class "image-pane"
              :click click
              {:style (cell= (str "background-image: url(" (:url image) ")"))}
              children)))

(defelem view [{board :board} _]
  (let* [the-board (j/cell= (or board (repeat 20 {:text "" :url "http://placehold.it/1000x1000"})))
         sections (j/cell= (vec (map-indexed vector the-board)))
         toggled (j/cell nil)
         current (j/cell= (get-in sections [toggled]))
         tiled (j/cell= (if toggled
                          (vec (concat [current]
                                       (subvec sections (inc toggled))
                                       (subvec sections 0 toggled)))
                          sections))]

    (j/cell= (dosync (print :current current)
                     (print :toggled toggled)
                     (print :tiled tiled)))
    (main
     (article
      (h/for-tpl [[idx card] tiled]
        (image-pane :image card
                    :toggled (cell= (= idx toggled))
                    :click #(if (= @idx @toggled)
                              (reset! toggled nil)
                              (do (.scrollIntoView (.querySelector js/document ".card:first-child"))
                                  (reset! toggled @idx)))))))))
