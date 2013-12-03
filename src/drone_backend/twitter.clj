(ns drone-backend.twitter
  (:require [drone-backend.io :as io]
            [net.cgrand.enlive-html :as html]
            [clojure.data.json :as json]
            [clojure.string :as str]))

(def cache-filename "tweet-url-cache.json")

(defn- fetch-html-url [url]
  (try
    (html/html-resource (java.net.URL. url))
    (catch Exception e nil)))

; "278544689483890688"

(defn- tweet-id-to-twitter-url [tweet-id]
  (str "https://twitter.com/dronestream/statuses/" tweet-id))

(defn fetch-information-url [tweet-id]
  (let [twitter-url (tweet-id-to-twitter-url tweet-id)]
    (when-let [content (fetch-html-url twitter-url)]
      (-> content
          (html/select [:p.tweet-text :a.twitter-timeline-link])
          first
          :attrs
          :title))))

(defn load-local-cache [save-folder]
  "returns a map of key=tweet_id value=information_url"
  (if-let [tweet-info (io/read-json-file (str save-folder "/" cache-filename))]
    tweet-info
    {}))

; rebuild the cache from strikes data
(defn save-local-cache [strikes save-folder]
  (let [data (reduce (fn [a b] (assoc a (:tweet-id b) (:information-url b))) {} strikes)]
    (io/save data (str save-folder "/" cache-filename))))


(defn get-info [tweet-id tweet-info]
  (let [tweet-kw (keyword tweet-id)]
   (if (contains? tweet-info tweet-kw)
     (tweet-info tweet-kw)
     (fetch-information-url tweet-id))))
