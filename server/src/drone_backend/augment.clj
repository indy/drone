(ns drone-backend.augment
  (:require [drone-backend.twitter :as twitter]
            [clojure.string :as str]))

(def SINGLE-NUMBER #"^\s*(\d+)\s*$")
(def RANGE #"^\s*(\d+)\s*-\s*(\d+)\s*$")

(defn has-valid-min-max? [s]
  (or (re-matches SINGLE-NUMBER s) (re-matches RANGE s)))

(defn parse-min-max [s]
  (if-let [m (re-matches SINGLE-NUMBER s)]
    [(Integer/parseInt (nth m 1)) (Integer/parseInt (nth m 1))]
    (if-let [m (re-matches RANGE s)]
      [(Integer/parseInt (nth m 1)) (Integer/parseInt (nth m 2))])))

(defn rangify-field [strike field-kw]
  (let [field (name field-kw)
        has-kw (keyword (str "has-valid-" field "-range"))
        min-kw (keyword (str field "-min"))
        max-kw (keyword (str field "-max"))]
    (if (has-valid-min-max? (field-kw strike))
      (let [[min max] (parse-min-max (field-kw strike))]
        (conj strike
              {has-kw true
               min-kw min
               max-kw max}))
      (conj strike 
            {has-kw false}))))

(defn rangify [strike]
  (reduce rangify-field strike [:deaths :civilians :children :injuries]))

(defn summary [strike field-kw singular]
  (let [field (name field-kw)
        has-kw (keyword (str "has-valid-" field "-range"))
        min-kw (keyword (str field "-min"))
        max-kw (keyword (str field "-max"))]
    (if (has-kw strike)
      (if (not= (min-kw strike) (max-kw strike))
        (str (min-kw strike) "-" (max-kw strike) " " field)
        (cond (= 0 (min-kw strike)) ""
              (= 1 (min-kw strike)) (str (min-kw strike) " " singular)
              :else (str (min-kw strike) " " field)))
      "")))

(defn summarise [strike]
  (let [summary-text (str/join ", " (remove empty?
                                     (map #(summary strike %1 %2) 
                                          [:deaths :civilians :children :injuries]
                                          ["death" "civilian" "child" "injury"])))]
    (conj strike {:drone-app-summary summary-text})))

(defn add-information-url3 [strikes tweet-info]
  (map (fn [s]
         (assoc s :information-url "foobar"))
       strikes))

(defn add-information-url [strikes tweet-info]
  (map (fn [s]
         (assoc s :information-url (twitter/get-info (:tweet-id s) tweet-info)))
       strikes))

(defn process [strikes tweet-info]
  (let [s (add-information-url strikes tweet-info)]
    (map #(-> % rangify summarise)
         s)))
