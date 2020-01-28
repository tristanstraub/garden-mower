(ns postcss.emitter-test
  (:require [postcss.impl.emitter :as emitter]
            [clojure.test :refer [deftest is]]))

(deftest merge-selectors
  (is (= (emitter/merge-selectors ".blue { color: rgba(0,0,255,1); } .bg-red { background-color: rgba(255,0,0,1);" [:.btn [".blue" ".bg-red"]])
         ".btn {\n  color: rgba(0, 0, 255, 1);\n  background-color: rgba(255, 0, 0, 1);\n}"))
  (is (= (emitter/merge-selectors ".blue { color: rgba(0,0,255,1); } .bg-red { background-color: rgba(255,0,0,1);" [:.btn-blue [".blue"] :btn-red [".bg-red"]])
         ".btn-blue {\n  color: rgba(0, 0, 255, 1);\n}\n\nbtn-red {\n  background-color: rgba(255, 0, 0, 1);\n}")))
