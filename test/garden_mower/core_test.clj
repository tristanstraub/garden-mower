(ns garden-mower.core-test
  (:require [garden-mower.core :as mower]
            [clojure.test :refer [deftest is]]
            [garden.core]))

(def css
  ".before { width: \"25%\"}
   @media (min-width: 640px) {
     .blue   { color: rgba(0,0,255,1); }
     .bg-red { background-color: rgba(255,0,0,1); }
   }
   .after { height: \"75%\" }")

(deftest attributes-test
  (is (= (mower/attributes css :.blue)
         [#garden.types.CSSAtRule{:identifier :media, :value {:media-queries {"all" true, "min-width" "640px"}, :rules (["&" {"color" "rgba(0, 0, 255, 1)"}])}}]))

  (is (= (mower/attributes css :.blue :.bg-red)
         [#garden.types.CSSAtRule{:identifier :media, :value {:media-queries {"all" true, "min-width" "640px"}, :rules (["&" {"color" "rgba(0, 0, 255, 1)"}])}} #garden.types.CSSAtRule{:identifier :media, :value {:media-queries {"all" true, "min-width" "640px"}, :rules (["&" {"background-color" "rgba(255, 0, 0, 1)"}])}}]))

  (is (= (mower/attributes css :.before :.after)
         [["&" {"width" "\"25%\""}] ["&" {"height" "\"75%\""}]]))

  (is (= (mower/attributes css :.blue :.after)
         [#garden.types.CSSAtRule{:identifier :media, :value {:media-queries {"all" true, "min-width" "640px"}, :rules (["&" {"color" "rgba(0, 0, 255, 1)"}])}} ["&" {"height" "\"75%\""}]]))

  (is (= (garden.core/css {:pretty-print? false} [:.btn (mower/attributes css :.blue :.after)])
         "@media all and (min-width:640px){.btn{color:rgba(0,0,255,1)}}.btn{height:\"75%\"}")))

(deftest media-query-output-test
  (is (= (->> "@media only screen and (max-width:100px){a{width:100%}}"
              mower/parse
              mower/to-garden
              (garden.core/css {:pretty-print? false}))
         "@media only screen and (max-width:100px){a{width:100%}}"))

  (is (= (->> "@media not print and (max-width:100px){a{width:100%}}"
              mower/parse
              mower/to-garden
              (garden.core/css {:pretty-print? false}))
         "@media not print and (max-width:100px){a{width:100%}}"))

  (is (= (->> "@media all and (max-width:100px){a{width:100%}}"
              mower/parse
              mower/to-garden
              (garden.core/css {:pretty-print? false}))
         "@media all and (max-width:100px){a{width:100%}}")))
