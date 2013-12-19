(ns drone-backend.core-test
  (:use clojure.test
        drone-backend.core))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 1 1))))

(deftest b-test
  (testing "something"
    (is (= "(defproject drone-backend \"0.1.0-SNAPSHOT\"\n  :description \"generate the static json files required by the Drone app\"\n  :url \"http://indy.io/drone\"\n  :license {:name \"Apache License, Version 2.0\"\n            :url \"http://www.apache.org/licenses/LICENSE-2.0\"}\n  :dependencies [[org.clojure/clojure \"1.5.1\"]\n                 [org.clojure/data.json \"0.2.3\"]]\n  :main drone-backend.core)\n" (slurp "project.clj")))))
