(ns example.style
  (:require [postcss.core :as postcss]
            [clojure.string :as str]))

(defonce tailwind-css
  (delay (slurp "https://cdn.tailwindcdn.com/1.1.4/tailwind.min.css")))

(comment
  (defn kw
    "Transform space separated classes into vector of keywords for use with `alias-selectors`.
     Use with cider-eval-last-sexp-and-replace to convert class string"
    [classes-string]
    (mapv #(keyword (str "." (name %))) (str/split classes-string #" ")))
  )

(def classes
  {:body                [:.bg-gray-600]
   :.card-container     [:.flex :.mx-auto :.flex-col :.sm:flex-row]
   :.card-outer         [:.max-w-sm :.w-full :.sm:max-w-full :.lg:flex :.mb-1 :.mx-auto :.sm:mx-1]
   :.card-inner-image   [:.h-48 :.lg:h-auto :.lg:w-48 :.flex-none :.bg-cover :.rounded-t :.lg:rounded-t-none :.lg:rounded-l :.text-center :.overflow-hidden]
   :.card-inner-body    [:.border-r :.border-b :.border-l :.border-gray-400 :.lg:border-l-0 :.lg:border-t :.lg:border-gray-400 :.bg-white :.rounded-b :.lg:rounded-b-none :.lg:rounded-r :.p-4 :.flex :.flex-col :.justify-between :.leading-normal]
   :.card-body-top      [:.text-sm :.text-gray-600 :.flex :.items-center]
   :.icon               [:.fill-current :.text-gray-500 :.w-3 :.h-3 :.mr-2]
   :.card-title         [:.text-gray-900 :.font-bold :.text-xl :.mb-2]
   :.card-body-text     [:.text-gray-700 :.text-base]
   :.card-footer        [:.flex :.items-center]
   :.card-footer-image  [:.w-10 :.h-10 :.rounded-full :.mr-4]
   :.card-footer-text   [:.text-sm]
   :.card-footer-author [:.text-gray-900 :.leading-none]
   :.card-footer-date   [:.text-gray-600]})

(defmacro css
  []
  (postcss/alias-selectors @tailwind-css classes))
