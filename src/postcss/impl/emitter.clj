(ns postcss.impl.emitter
  (:require [garden.stylesheet]
            [garden.core]
            [clojure.string :as str]
            [postcss.impl.parser :as parser]))

(defmulti emit :type)

(defn selector-rules
  [css selector]
  (->> css
       (mapcat (fn [{:keys [type] :as rule}]
                 (if (= type :media-rule)
                   (:rules rule)
                   [rule])))
       (filter (comp #{(name selector)} name :selector))))

(defmethod emit :style-rule
  [{:keys [selector style]}]
  [(str/replace (name selector) "/" "\\/")
   (->> style
        (map (fn [{:keys [name value priority]}]
               [name (if priority
                       (str value " !" priority)
                       value)]))
        (into {}))])

(defmethod emit :media-query
  [{:keys [not? only? properties]}]
  {:pre [(not not?) (not only?)]}
  (->> properties
       (map (fn [{:keys [name value important?]}]
              [name (if important?
                      (str value " !important")
                      value)]))
       (into {})))

(defmethod emit :media-rule
  [{:keys [media rules]}]
  (garden.stylesheet/at-media (apply merge (map emit media))
                              (mapv emit rules)))

(defn mixins-data
  [style-data mixins]
  (map (fn [[name selectors]]
         {:type     :style-rule
          :selector name
          :style    (vec (mapcat (fn [selector]
                                   (mapcat :style (selector-rules style-data selector)))
                                 selectors))})
       mixins))

(defn merge-selectors*
  [parsed-css mixins]
  (->> (partition 2 mixins)
       (mixins-data parsed-css)
       (mapv emit)))

(defn merge-selectors
  [css mixins]
  (garden.core/css (merge-selectors* (parser/parse-css css) mixins)))
