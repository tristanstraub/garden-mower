(ns garden-mower.impl.transformer)

(defn walk
  [node {:keys [style-rule-fn]
         :or   {style-rule-fn identity}
         :as   options}]
  (if (map? node)
    (case (:type node)
      :style-rule (style-rule-fn node)
      :media-rule (let [media-rule (update node :rules #(walk % options))]
                    (when (seq (:rules media-rule))
                      media-rule))
      (throw (ex-info "Unrecognized node type" {:node node})))
    (filter identity (map #(walk % options) node))))

(defn alias-selector
  "Drop non-matching selectors and rename remaining to alias"
  [node alias selector]
  (walk node {:style-rule-fn (fn [node]
                               (when (= (name selector)
                                        (name (:selector node)))
                                 (assoc node :selector alias)))}))
