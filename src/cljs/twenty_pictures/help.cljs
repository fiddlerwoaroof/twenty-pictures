(ns twenty-pictures.help
  (:require-macros [javelin.core :refer [defc defc= dosync]]
                   [hoplon.core :refer [defelem]])
  (:require [clojure.string]
            [javelin.core :as j :refer [cell=]]
            [hoplon.core :as h :refer [article h2 dl dt dd]]))

(defelem view [_ _]
  (article :class (cell= {"help" true
                          "visible" true})
           (h2 "Key Help")
           (dl (dt "j") (dd "Next picture")
               (dt "k") (dd "Previous picture")
               (dt "r") (dd "Reload picture")
               (dt "?") (dd "Show help")
               (dt "Escape") (dd "Dismiss help and/or go back to overview"))))

