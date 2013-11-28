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
      (io/save (filter #(> (:number %) id) strikes) 
               (str save-folder "/" "strikes-id-" id ".json"))
      (recur (inc id)))))

(defn save [strikes save-folder]
  (do 
    (io/save-count (apply max (map :number strikes)) 
                   (str save-folder "/" "strikes-count.json"))
    (io/save strikes 
             (str save-folder "/" "strikes-complete.json"))
    (save-diffed strikes save-folder)))

(defn process [json]
  (-> json
      :strike                         ; get strike list from json
      augment/process))               ; augment the strikes 

(defn process-file [filename save-folder]
  (let [strikes (process (io/read-json-file filename))]
    (save strikes save-folder)))

(defn process-url [url save-folder]
  (let [strikes (process (io/fetch-json-url url))]
    (save strikes save-folder)))

(defn -main [& terms]
  (process-url dronestream-url (first terms)))
