(defproject drone-backend "0.1.0-SNAPSHOT"
  :description "generate the static json files required by the Drone app"
  :url "http://indy.io/drone"
  :license {:name "Apache License, Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [[enlive "1.1.5"]
                 [org.clojure/clojure "1.5.1"]
                 [org.clojure/data.json "0.2.3"]]
  :main drone-backend.core
  :aot [drone-backend.core])
