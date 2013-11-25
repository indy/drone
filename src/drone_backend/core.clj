(ns drone-backend.core
  (:require [drone-backend.augment :as augment]
            [drone-backend.io :as io]
            [clojure.string :as str])
  (:gen-class))

(def *latest-installed-strike* 2)
(def data005 "resources/test-data/data-005.json")

(defn save-diffed [strikes]
  (loop [id *latest-installed-strike*]
    (when (< id (count strikes))
      (io/save (filter #(> (:number %) id) strikes) 
               (str "strikes-id-" id ".json"))
      (recur (inc id)))))

(defn save [strikes]
  (do 
    (io/save-count (apply max (map :number strikes)) "strikes-count.json")
    (io/save strikes "strikes-complete.json")
    (save-diffed strikes)))

(defn process [json]
  (-> json
      :strike                         ; get strike list from json
      augment/process                 ; augment the strikes 
      save))

(defn process-file [filename]
  (process (io/read-json-file filename)))

(defn process-url [url]
  (process (io/fetch-json-url url)))

(defn -main [& terms]
  2)
