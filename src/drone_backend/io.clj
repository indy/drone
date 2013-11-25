(ns drone-backend.io
  (:require [clojure.data.json :as json]
            [clojure.string :as str])
  (:gen-class))

(defn fetch-url [address]
  (with-open [stream (.openStream (java.net.URL. address))]
    (let  [buf (java.io.BufferedReader. 
                (java.io.InputStreamReader. stream))]
      (apply str (line-seq buf)))))


(defn as-json [string]
  (json/read-str string :key-fn #(keyword (str/replace % "_" "-"))))

(defn fetch-json-url [address]
  (as-json (fetch-url address)))

(defn read-json-file [filename]
  (as-json (slurp filename)))


(defn inform [strikes]
  (reduce (fn [a strike] (str a " " (:number strike))) "" strikes))

(defn save-count [number filename]
  (println (str filename " " number)))

(defn save [strikes filename]
  (println (str "saving " (count strikes) " strikes into " filename " " (inform strikes))))


