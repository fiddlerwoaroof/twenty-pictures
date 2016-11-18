;; This file is part of Twenty Pictures.
;;
;; Twenty Pictures is free software: you can redistribute it and/or
;; modify it under the terms of the GNU Affero General Public License
;; as published by the Free Software Foundation, either version 3 of
;; the License, or (at your option) any later version.
;;
;; Twenty Pictures is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
;; Affero General Public License for more details.
;;
;; You should have received a copy of the GNU Affero General Public
;; License along with Twenty Pictures.  If not, see
;; <http://www.gnu.org/licenses/>.

(set-env!
  :dependencies '[[adzerk/boot-cljs "1.7.228-2"]
                  [adzerk/boot-reload "0.4.13"]
                  [hoplon/hoplon "6.0.0-alpha17"]
                  [org.clojure/clojure "1.8.0"]
                  [org.clojure/clojurescript "1.9.293"]
                  [com.cemerick/piggieback "0.2.0"]
                  [pandeiro/boot-http "0.7.6"]
                  [ring "1.5.0"]
                  [ring/ring-defaults        "0.2.1"]
                  [compojure "1.5.1"]
                  [org.clojure/tools.nrepl "0.2.12"]
                  [hoplon/castra "3.0.0-alpha5"]]
  :resource-paths #{"src" "assets"}
  :source-paths #{"src"}
  :asset-paths  #{"assets"})

(require
  '[adzerk.boot-cljs         :refer [cljs]]
  '[adzerk.boot-reload       :refer [reload]]
  '[hoplon.boot-hoplon       :refer [hoplon prerender]]
  '[pandeiro.boot-http       :refer [serve]])

(deftask dev
  "Build twenty-pictures for local development."
  []
  (comp
   (serve :handler 'twenty-pictures.handler/handler
          :reload true
          :port 9090)
    (watch)
    (speak)
    (hoplon)
    (reload :port 34533)
    (cljs)))

(deftask prod
  "Build twenty-pictures for production deployment."
  []
  (comp
    (hoplon)
    (cljs :optimizations :advanced)
    (prerender)
    (target :dir #{"target"})))
