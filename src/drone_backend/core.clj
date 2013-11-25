(ns drone-backend.core
  (:require [drone-backend.augment :as augment]
            [clojure.data.json :as json]
            [clojure.string :as str])
  (:gen-class))

(def data005 "resources/test-data/data-005.json")

(defn fetch-url [address]
  (with-open [stream (.openStream (java.net.URL. address))]
    (let  [buf (java.io.BufferedReader. 
                (java.io.InputStreamReader. stream))]
      (apply str (line-seq buf)))))

(defn as-json [string]
  (json/read-str string :key-fn #(keyword (str/replace % "_" "-"))))

(defn process [string]
  (let [strikes (:strike (as-json string))]
    (augment/process strikes)))

(defn process-file [filename]
  (process (slurp filename)))

(defn process-url [url]
  (process (fetch-url url)))

(defn -main [& terms]
  2)
