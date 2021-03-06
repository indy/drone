(ns drone-backend.core
  (:require [drone-backend.augment :as augment]
            [drone-backend.io :as io]
            [clojure.string :as str])
  (:gen-class))

(def dronestream-url "http://api.dronestre.am/data")
(def latest-installed-strike 488)

(def data005 "resources/test-data/data-005.json")

(defn save-diffed [strikes save-folder]
  "for each 'latest id' that a client may have, save only the strikes required to get that client upto date"
  (loop [id latest-installed-strike]
    (when (< id (count strikes))
      (io/save-strikes (filter #(> (:number %) id) strikes) 
                       (str save-folder "/" "strikes-id-" id ".json"))
      (recur (inc id)))))

(defn save-all [strikes save-folder]
  (do 
    (io/save-count (apply max (map :number strikes)) 
                   (str save-folder "/" "strikes-count.json"))
    (io/save-strikes strikes 
                     (str save-folder "/" "strikes-complete.json"))
    (save-diffed strikes save-folder)))

(defn updated-strike-data? [strikes save-folder]
  "are there any new strikes compared to whats already in the cache?"
  (if-let [cached-strike-count (io/read-json-file (str save-folder "/" "strikes-count.json"))]
    (> (apply max (map :number strikes)) cached-strike-count)
    true))                              ; couldn't find strikes-count file so update strike data 

(defn process-json [json save-folder]
  (let [strike-data (:strike json)]
    (when (updated-strike-data? strike-data save-folder)
      (save-all (augment/process (:strike json)) 
                save-folder))))

(defn process-file [filename save-folder]
  (process-json (io/read-json-file filename) save-folder))

; (defn -main [& terms] (process-url dronestream-url (first terms)))

(defn -main [& terms]
  "invoke with $filename $save-folder"
  (process-file (first terms) (second terms)))
