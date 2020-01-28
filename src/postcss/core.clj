(ns postcss.core
  (:require [postcss.impl.emitter]))

(defn merge-selectors
  [css mixins]
  (postcss.impl.emitter/merge-selectors css mixins))
