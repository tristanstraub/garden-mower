(ns postcss.impl.emitter
  (:require [garden.stylesheet]
            [garden.core]
            [clojure.string :as str]
            [postcss.impl.parser :as parser]))

(defmulti emit :type)

(defn alias-selector
  [alias selector node]
  (if (map? node)
    (case (:type node)
      :style-rule (when (= (name selector)
                           (name (:selector node)))
                    (assoc node :selector alias))
      :media-rule (let [media-rule (update node :rules #(alias-selector alias selector %))]
                    (when (seq (:rules media-rule))
                      media-rule))
      (throw (ex-info "Unrecognized node type" {:node node})))
    (filter identity (map #(alias-selector alias selector %) node))))

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

(defn aliased-rules
  [style-data mixins]
  (->> (for [[alias selectors] mixins
             selector         selectors]
         (alias-selector alias selector style-data))
       (apply concat)))

(defn alias-selectors*
  [parsed-css mixins]
  (->> (cond->> mixins
         (vector? mixins)
         (partition 2))
       (aliased-rules parsed-css)
       (mapv emit)))

(defn alias-selectors
  [css mixins]
  (garden.core/css (alias-selectors* (parser/parse-css css) mixins)))
