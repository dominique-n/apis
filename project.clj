(defproject apis "0.0.1-SNAPSHOT"
  :description "Cool new project to do things and stuff"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clojure-csv/clojure-csv  "2.0.1"]
                 [cheshire  "5.6.3"]
                 [enlive  "1.1.6"]
                 [clj-http  "2.3.0"] ;based on Apache HttpCompo
                 ]
  :profiles {:dev {:dependencies [[midje "1.7.0"]]}
             ;; You can add dependencies that apply to `lein midje` below.
             ;; An example would be changing the logging destination for test runs.
             :midje {}})
             ;; Note that Midje itself is in the `dev` profile to support
             ;; running autotest in the repl.

  
