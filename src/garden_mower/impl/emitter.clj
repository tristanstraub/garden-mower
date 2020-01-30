(ns garden-mower.impl.emitter
  (:require [garden.stylesheet]
            [clojure.string :as str]))

(defmulti emit :type)

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
  [{:keys [not? only? media properties]}]
  (->> (concat (when (seq media)
                 [{:name  media
                    :value (if only? "only" (not not?))}])
               properties)
       (map (fn [{:keys [name value important?]}]
              [name (if important?
                      (str value " !important")
                      value)]))
       (into {})))

(defmethod emit :media-rule
  [{:keys [media rules]}]
  (garden.stylesheet/at-media (apply merge (map emit media))
                              (vec (mapcat emit rules))))
