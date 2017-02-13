(ns apis.google-test
  (:require [midje.sweet :refer :all]
            [apis.google :refer :all]
            [cheshire.core :as json]
            ))


(let [path "dev-resources/google/"
      response (json/parse-string (slurp (str path "activities_response.json")) true)
      items (json/parse-string (slurp (str path "items.json")) true)
      [item0 item1 & _] items]


  (facts "About `extract-items"
         (extract-items response) => #(= 10 (count %))
         (extract-items response) => #(-> % first :id)
         )

  (facts "About `http-iterate"
         (http-iterate http nil nil) => 
         (provided (http & anything) => {:status "400"}))



  )
