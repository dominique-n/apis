(ns apis.google-test
  (:require [midje.sweet :refer :all]
            [apis.google :refer :all]
            [cheshire.core :as json]
            [net.cgrand.enlive-html :as html]
            [clj-http.client :as http]
            ))


(let [path "dev-resources/google/" 
      *query-params [:key "api-key" :query ["data" "business"] :orderBy "recent" :maxResults 20]
      response (json/parse-string (slurp (str path "activities_response.json")) true)
      items (json/parse-string (slurp (str path "items.json")) true)
      [item0 item1 & _] items]


  (facts "About 'activities-search"
         ((partial http-get 0) 1) => response
         (activities-search 2 :query "#lol" :maxResults 2) => #(= 20 (count %))
         (activities-search 2 :query "#lol" :maxResults 2) => #(-> % first :id)
         (background
           (http-get & anything) => response)
         )

  )
