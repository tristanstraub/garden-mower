(ns tailwind.style
  (:require [postcss.core]
            [garden.core]
            [clojure.string :as str]))

(defonce tailwind-css
  (delay (postcss.core/parse (slurp "https://cdn.tailwindcdn.com/1.1.4/tailwind.min.css"))))

(defn attributes
  [& selectors]
  (apply postcss.core/attributes @tailwind-css selectors))

(defmacro css
  []
  (garden.core/css
   {:pretty-print? false}
   [[:.card-footer-date (attributes :.text-gray-600)]
    [:.card-footer-text (attributes :.text-sm)]
    [:.card-outer (attributes :.max-w-sm :.w-full :.sm:max-w-full :.lg:flex :.mb-1 :.mx-auto :.sm:mx-1)]
    [:.card-footer (attributes :.flex :.items-center)]
    [:.card-inner-image (attributes :.h-48 :.lg:h-auto :.lg:w-48 :.flex-none :.bg-cover :.rounded-t :.lg:rounded-t-none :.lg:rounded-l :.text-center :.overflow-hidden)]
    [:.card-inner-body (attributes :.border-r :.border-b :.border-l :.border-gray-400 :.lg:border-l-0 :.lg:border-t :.lg:border-gray-400 :.bg-white :.rounded-b :.lg:rounded-b-none :.lg:rounded-r :.p-4 :.flex :.flex-col :.justify-between :.leading-normal)]
    [:.card-container (attributes :.flex :.mx-auto :.flex-col :.sm:flex-row)]
    [:.card-footer-image (attributes :.w-10 :.h-10 :.rounded-full :.mr-4)]
    [:.card-body-text (attributes :.text-gray-700 :.text-base)]
    [:.card-footer-author (attributes :.text-gray-900 :.leading-none)]
    [:.card-title (attributes :.text-gray-900 :.font-bold :.text-xl :.mb-2)]
    [:body (attributes :.bg-gray-600)]
    [:.card-body-top (attributes :.text-sm :.text-gray-600 :.flex :.items-center)]
    [:.icon (attributes :.fill-current :.text-gray-500 :.w-3 :.h-3 :.mr-2)]]))

(comment
  (defn kw
    "Transform space separated html class into a vector of keywords. Used for converting html
     examples to clojure."
    [classes-string]
    (mapv #(keyword (str "." (name %))) (str/split classes-string #" ")))
  )
