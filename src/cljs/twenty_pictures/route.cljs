(ns twenty-pictures.route
  (:require-macros [javelin.core :refer [defc defc= dosync]]
                   [secretary.core :refer [defroute]])
  (:require [clojure.string]
            [javelin.core :as j]
            [hoplon.core :as h]
            [cuerdas.core]
            [cemerick.url]
            [secretary.core]))

(def r (h/route-cell))

(def path
  (j/cell= (-> r
               (cuerdas.core/strip-prefix "#")
               (cuerdas.core/strip-prefix "/")
               (#(str "/" %))
               (#(do (print "The url is: " %)
                     %)))))

(j/cell= (print "the path is " path))

(defn generate-route
  ([path] (generate-route path nil))
  ([path query]
   {:pre [(sequential? path) (or (nil? query) (map? query))]}
   (str "#/"
        (clojure.string/join "/" path)
        (if query
          (str "?"
               (cemerick.url/map->query query))))))

(defn set-route!
  "Set the URL hash to work fo the given route."
  ([route]
   (if (string? route)
     (set! (.-hash (.-location js/window))
           route)
     (set-route! (generate-route route))))
  ([path query]
   (set-route! (generate-route path query))))

(defc page-info {:route :home
                 :page 1})

(defc= route
  (page-info :route)
  (partial swap! page-info assoc-in [:route]))

(defc= board
  (get page-info :board)
  (partial swap! page-info assoc-in [:board]))

(defroute "/" []
  (reset! route :home))

(defroute "/board" []
  (reset! route :board-list))

(defroute "/board/:board" [board]
  (dosync (reset! route :board)
          (swap! page-info assoc-in [:board] board)))

(j/cell= (dosync (print "page-info" page-info)))

(j/cell= (secretary.core/dispatch! path))
