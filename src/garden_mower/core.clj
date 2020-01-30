(ns garden-mower.core
  (:require [garden-mower.impl.parser :as parser]
            [garden-mower.impl.transformer :as transformer]
            [garden-mower.impl.emitter :as emitter]))

(defn parse
  [css]
  (parser/parse css))

(defn to-garden
  [nodes]
  (mapv emitter/emit nodes))

(defn attributes
  [css & selectors]
  (let [css (parse css)]
    (to-garden (mapcat #(transformer/alias-selector css :& %) selectors))))
