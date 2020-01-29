(ns postcss.impl.emitter-test
  (:require [postcss.impl.emitter :as emitter]
            [postcss.impl.parser :as parser]
            [clojure.test :refer [deftest testing is]]))

(deftest merge-selectors
  (testing "Selectors in vector"
    (is (= (emitter/merge-selectors ".blue { color: rgba(0,0,255,1); } .bg-red { background-color: rgba(255,0,0,1);" [:.btn [".blue" ".bg-red"]])
           ".btn {\n  color: rgba(0, 0, 255, 1);\n  background-color: rgba(255, 0, 0, 1);\n}"))
    (is (= (emitter/merge-selectors ".blue { color: rgba(0,0,255,1); } .bg-red { background-color: rgba(255,0,0,1);" [:.btn-blue [".blue"] :.btn-red [".bg-red"]])
           ".btn-blue {\n  color: rgba(0, 0, 255, 1);\n}\n\n.btn-red {\n  background-color: rgba(255, 0, 0, 1);\n}"))
    (is (= (emitter/merge-selectors ".blue { color: rgba(0,0,255,1); } .bg-red { background-color: rgba(255,0,0,1);" [:.btn-blue-red [".blue" ".bg-red"] :.btn-red [".bg-red"]])
           ".btn-blue-red {\n  color: rgba(0, 0, 255, 1);\n  background-color: rgba(255, 0, 0, 1);\n}\n\n.btn-red {\n  background-color: rgba(255, 0, 0, 1);\n}")))

  (testing "Selectors in map"
    (is (= (emitter/merge-selectors ".blue { color: rgba(0,0,255,1); } .bg-red { background-color: rgba(255,0,0,1);" {:.btn-blue [".blue"] :btn-red [".bg-red"]})
           ".btn-blue {\n  color: rgba(0, 0, 255, 1);\n}\n\nbtn-red {\n  background-color: rgba(255, 0, 0, 1);\n}"))))

(deftest merge-selectors*
  (testing "merging from media query child selector"
    (is (= (emitter/merge-selectors* (parser/parse-css (slurp "/home/tristan/me/projects/ui-template/tailwind.min.css")) {:.btn [:.lg:flex]})
           [[".btn" {"display" "flex"}]]))))
