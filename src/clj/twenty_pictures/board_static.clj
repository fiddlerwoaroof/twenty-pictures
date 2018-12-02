(ns twenty-pictures.board-static
  (:require [hiccup.core :as hiccup]))

(defn hiccup-template [images id]
  [:html
   [:head
    [:link {:rel "stylesheet"
            :href "/app.css"}]
    [:meta {:property "og:title" :content (str "album " id)}]
    [:meta {:property "og:url" :content (str "http://srv2.elangley.org:9090/#!/board/" id)}]
    (for [image (take 5 images)]
      [:meta {:property "og:image" :content (str "http:" (:url image))}])]

   [:body
    [:h1 "Twenty Pictures"]
    [:main
     [:article
      (for [{url :url :as image} images]
        [:a {:href "javascript:location.href=location.href.replace('/board', '/#board')"}
         [:div.card 
          [:section.image-pane {:style (str "background-image: url(" url ")")}]]])]
     [:h3 "Made with "
      [:a {:href "http://unsplash.com"} "Unsplash"]
      [:span {:class "instructions"} "Push ? for help"]]]]])

(defn view [images id]
  (hiccup/html (hiccup-template images id)))
