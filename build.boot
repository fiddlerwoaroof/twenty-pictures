(in-ns 'boot.user)
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
 :dependencies
 '[[adzerk/boot-cljs "2.1.5"]
   [adzerk/boot-cljs-repl "0.4.0"]
   [weasel "0.7.0"]
   [adzerk/boot-reload "0.6.0"]
   [hoplon/hoplon "7.2.0"]
   [org.clojure/clojure "1.9.0"]
   [org.clojure/clojurescript "1.9.293"]
   [org.clojure/core.match "0.3.0-alpha4"]
   [secretary "1.2.3"]
   [cider/piggieback "0.3.10"]
   [org.postgresql/postgresql "42.2.5"]
   [com.layerware/hugsql "0.4.9"]
   [com.cemerick/url "0.1.1"]
   [funcool/cuerdas "2.0.6"]
   [pandeiro/boot-http "0.8.3"]
   [ring "1.7.1"]
   [tailrecursion/clojure-adapter-servlet "0.2.1"]
   [ring/ring-defaults        "0.3.2"]
   [compojure "1.6.1"]
   [org.clojure/data.json "0.2.6"]
   [org.clojure/tools.nrepl "0.2.13"]
   [hoplon/castra "3.0.0-alpha5"]
   [hiccup "1.0.5"]
   [clj-http "3.9.1"]
   [com.cemerick/friend "0.2.3"
    :exclusions
    [org.clojure/core.cache]]
   [clojusc/friend-oauth2 "0.2.0"]]

 :resource-paths #{"assets"}
 :source-paths #{"src/clj" "src/cljs"}
 :asset-paths  #{"assets"})

(require
 '[adzerk.boot-cljs         :refer [cljs]]
 '[adzerk.boot-cljs-repl    :refer [cljs-repl]]
 '[adzerk.boot-reload       :refer [reload]]
 '[hoplon.boot-hoplon       :refer [hoplon prerender]]
 '[pandeiro.boot-http       :refer [serve]])

(deftask dev
  "Build twenty-pictures for local development."
  []
  (comp
   #(do (println "Starting app " %)
        %)
   (serve :handler 'twenty-pictures.handler/app
          :reload true
          :port 9090)
   (watch)
   (speak)
   (cljs-repl)
   (hoplon)
   (reload :port 34533)
   (cljs :source-map true :optimizations :none)
   ))

(deftask prod
  "Build twenty-pictures for production deployment."
  [ ]
  (comp
   (hoplon)
   (cljs :optimizations :advanced)
   (prerender)
   (target :dir #{"target"})))

(deftask make-war
  "Build a war for deployment"
  []
  (comp (hoplon)
        (cljs :optimizations :advanced)
        (prerender)
        (uber :as-jars true)
        (web :serve 'twenty-pictures.handler/app)
        (war)
        (target :dir #{"target"})))

