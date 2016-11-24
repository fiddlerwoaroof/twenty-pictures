(ns twenty-pictures.frontend.rpc
  (:require-macros
   [javelin.core :refer [defc defc=]])
  (:require
   [javelin.core :refer [cell cell=]]
   [castra.core :refer [mkremote]]))

(defc url-value nil)
(defc url-loading nil)
(defc url-error nil)

(defc images nil)
(defc images-loading nil)
(defc images-error nil)

(defn get-board [name]
  (let* [board (cell nil)
         board-error (cell nil)
         board-loading (cell nil)
         rpc-fn (mkremote 'twenty-pictures.api/get-board
                          board board-error board-loading)]
    (print "name is " name)
    (cell= (rpc-fn name))
    (cell= (print :board board))
    board))

(def save-board
  (let [board (cell nil)
        board-error (cell nil)
        board-loading (cell nil)]
    (mkremote 'twenty-pictures.api/save-board
              board board-error board-loading)))

(def get-images
  (mkremote 'twenty-pictures.api/get-images
            images url-error url-value))

(defn save-image [n url]
  (let [image-cell (cell= (get-in images [n])
                          (partial swap! images assoc-in [n]))
        rpc-fn (mkremote 'twenty-pictures.api/save-image
                         image-cell url-error url-value)]
    (rpc-fn n url)
    image-cell))


(defc photo-boards nil)
(defc photo-boards-loading nil)
(defc photo-boards-error nil)

(defn print-error [err]
  (.forEach (.split (.-serverStack err)
                    (.stringify js/JSON
                                "\n"))
            #(print :photo-boards-error %)))

(cell= (when photo-boards-error
         (print-error photo-boards-error)))

; TODO: figger out error and loading cells
(defn get-board-by-uuid [uuid]
  (let* [value-cell (cell nil)
         error-cell (cell nil)
         loading-cell (cell nil)
         api-fn (mkremote 'twenty-pictures.api/get-photos-in-board
                          value-cell error-cell loading-cell)]
    (cell= (api-fn uuid))
    (cell= (when error-cell
             (print-error error-cell)))
    value-cell))

(def get-all-boards
  (mkremote 'twenty-pictures.api/get-all-boards
            photo-boards photo-boards-error photo-boards-loading))
