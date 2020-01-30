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
         [#garden.types.CSSAtRule{:identifier :media, :value {:media-queries {"min-width" "640px"}, :rules (["&" {"color" "rgba(0, 0, 255, 1)"}])}}]))

  (is (= (mower/attributes css :.blue :.bg-red)
         [#garden.types.CSSAtRule{:identifier :media, :value {:media-queries {"min-width" "640px"}, :rules (["&" {"color" "rgba(0, 0, 255, 1)"}])}} #garden.types.CSSAtRule{:identifier :media, :value {:media-queries {"min-width" "640px"}, :rules (["&" {"background-color" "rgba(255, 0, 0, 1)"}])}}]))

  (is (= (mower/attributes css :.before :.after)
         [["&" {"width" "\"25%\""}] ["&" {"height" "\"75%\""}]]))

  (is (= (mower/attributes css :.blue :.after)
         [#garden.types.CSSAtRule{:identifier :media, :value {:media-queries {"min-width" "640px"}, :rules (["&" {"color" "rgba(0, 0, 255, 1)"}])}} ["&" {"height" "\"75%\""}]]))

  (is (= (garden.core/css {:pretty-print? false} [:.btn (mower/attributes css :.blue :.after)])
         "@media(min-width:640px){.btn{color:rgba(0,0,255,1)}}.btn{height:\"75%\"}")))
