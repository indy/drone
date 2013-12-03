(ns drone-backend.io
  (:require [clojure.data.json :as json]
            [clojure.string :as str]))

(defn fetch-url [address]
  (with-open [stream (.openStream (java.net.URL. address))]
    (let  [buf (java.io.BufferedReader. 
                (java.io.InputStreamReader. stream))]
      (apply str (line-seq buf)))))

(defn file-exists? [filename]
  (.exists (clojure.java.io/as-file filename)))

(defn as-json [string]
  (json/read-str string :key-fn #(keyword (str/replace % "_" "-"))))

(defn fetch-json-url [address]
  (as-json (fetch-url address)))

(defn read-json-file [filename]
  (when (file-exists? filename)
   (as-json (slurp filename))))

(defn save-count [number filename]
  (spit filename number))

(defn save [data filename]
  (spit filename (json/write-str data)))

