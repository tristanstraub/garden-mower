(ns tailwind.client
  (:require-macros [tailwind.style :as style])
  (:require [goog.dom :as dom]
            [rum.core :as rum]
            [cljs.pprint :refer [cl-format]]))

(def lock-icon
  [:svg.icon {:xmlns   "http://www.w3.org/2000/svg"
              :viewBox "0 0 20 20"}
   [:path {:d "M4 8V6a6 6 0 1 1 12 0v2h1a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2v-8c0-1.1.9-2 2-2h1zm5 6.73V17h2v-2.27a2 2 0 1 0-2 0zM7 6v2h6V6a3 3 0 0 0-6 0z"}]])

(rum/defc card
  [{:keys [category title body avatar author date image image-title]}]
  [:div.card-outer
   [:div.card-inner-image {:style {:background-image (cl-format nil "url('~a')" image)}
                           :title image-title}]
   [:div.card-inner-body
    [:div.card-body-outer
     [:p.card-body-top lock-icon category]
     [:div.card-title title]
     [:p.card-body-text body]]
    [:div.card-footer
     [:img.card-footer-image {:src avatar :alt "Random coder"}]
     [:div.card-footer-text
      [:p.card-footer-author author]
      [:p.card-footer-date date]]]]])

(def card-data
  {:category    "Secret sauce"
   :image       "/espresso.jpg"
   :image-title "Espresso cup and coffee beans on a napkin, on a table"
   :title       "Coffee or tea? Spaces or tabs? Emacs or vi?"
   :body        "Why choose just one..."
   :avatar      "dobbs.jpg"
   :author      "Random coder"
   :date        "Jan 28, 2020"})

(rum/defc root
  []
  [:span
   [:style (style/css)]
   [:div.container.card-container
    (card card-data)
    (card card-data)]])

(rum/mount (root) (dom/getElement "app"))
