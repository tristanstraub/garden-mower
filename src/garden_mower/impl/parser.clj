(ns garden-mower.impl.parser
  (:require [clojure.core.protocols :as p])
  (:import [com.steadystate.css.dom CSSMediaRuleImpl CSSStyleDeclarationImpl CSSStyleRuleImpl Property]
           [com.steadystate.css.parser CSSOMParser SACParserCSS3]
           com.steadystate.css.parser.media.MediaQuery
           org.w3c.css.sac.InputSource))

(defn item-seq
  ([f coll]
   (mapv (partial f coll) (range (.getLength coll))))
  ([coll]
   (item-seq #(.item %1 %2) coll)))

(extend-type CSSMediaRuleImpl
  p/Datafiable
  (datafy [rule]
    {:type  :media-rule
     :media (p/datafy (.getMedia rule))
     :rules (mapv p/datafy (item-seq (.getCssRules rule)))}))

(extend-type CSSStyleRuleImpl
  p/Datafiable
  (datafy [rule]
    {:type     :style-rule
     :selector (.getSelectorText rule)
     :style    (p/datafy (.getStyle rule))}))

(extend-type CSSStyleDeclarationImpl
  p/Datafiable
  (datafy [decl]
    (let [properties (item-seq decl)]
      (mapv (fn [p]
              {:type     :style-property
               :name     p
               :value    (.getPropertyValue decl p)
               :priority (let [priority (.getPropertyPriority decl p)]
                           (when (seq priority)
                             priority))})
            properties))))

(extend-type com.steadystate.css.dom.MediaListImpl
  p/Datafiable
  (datafy [media-list]
    (map p/datafy (item-seq #(.mediaQuery %1 %2) media-list))))

(extend-type MediaQuery
  p/Datafiable
  (datafy [media-query]
    {:type       :media-query
     :media      (.getMedia media-query)
     :properties (mapv p/datafy (.getProperties media-query))
     :not?       (.isNot media-query)
     :only?      (.isOnly media-query)}))

(extend-type Property
  p/Datafiable
  (datafy [property]
    {:type       :property
     :name       (.getName property)
     :value      (p/datafy (.getValue property))
     :important? (.isImportant property)}))

(extend-type com.steadystate.css.dom.CSSValueImpl
  p/Datafiable
  (datafy [value]
    (.getCssText value)))

(defn parse
  [css]
  (if (string? css)
    (with-in-str css
      (let [source (InputSource. *in*)
            parser (CSSOMParser. (SACParserCSS3.))]
        (->> (.parseStyleSheet parser source nil nil)
             (.getCssRules)
             item-seq
             (map p/datafy))))
    css))
