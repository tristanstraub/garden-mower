(ns postcss.core
  (:require [postcss.impl.emitter]))

(defn alias-selectors
  [css mixins]
  (postcss.impl.emitter/alias-selectors css mixins))
