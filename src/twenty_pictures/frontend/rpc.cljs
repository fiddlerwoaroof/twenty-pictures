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
