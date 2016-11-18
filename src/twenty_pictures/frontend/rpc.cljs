(ns twenty-pictures.frontend.rpc
  (:require-macros
   [javelin.core :refer [defc defc=]])
  (:require
   [javelin.core :refer [cell]]
   [castra.core :refer [mkremote]]))

(defc url-value nil)
(defc url-loading nil)
(defc url-error nil)

(defc images nil)
(defc images-loading nil)
(defc images-error nil)

(defc= logger (.log js/console (clj->js (map #(% :background) images))))

(def get-images
  (mkremote 'twenty-pictures.api/get-images
            images url-error url-value))

(defn save-image [n url]
  (let [rpc-fn (mkremote 'twenty-pictures.api/save-image
                         url-value url-error url-value)]
    (swap! images assoc-in [n :background] url)
    (rpc-fn n url)
    (get-images)))
