(ns twenty-pictures.board-list
  (:require-macros [javelin.core :refer [defc defc= dosync]]
                   [hoplon.core :refer [defelem]])
  (:require [clojure.string]
            [javelin.core :as j :refer [cell=]]
            [hoplon.core :as h :refer [main article div section iframe ul li a]]
            [twenty-pictures.frontend.rpc :as rpc]))

(rpc/get-all-boards)

(cell= (print :board-list rpc/photo-boards))
(defelem view [{board-list :board-list} _]
  (main
   (article
    (ul :class "board-list"
     (h/for-tpl [board rpc/photo-boards]
       (li (a :href (cell= (str "#board/" (:id board))) (cell= (:name board)))))))))
