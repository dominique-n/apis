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
      status400 (json/parse-string (slurp (str path "status400.json")) true)
      error {:status 400 :body ""}
      response-fn (fn [& _] response) 
      status400-fn (fn [& _] status400) 
      items (json/parse-string (slurp (str path "items.json")) true)
      [item0 item1 & _] items]


  (facts "About `http-iterate"
         (let [query-params (assoc (apply hash-map *query-params) :maxResults 10)
               *http-iterate0 (http-iterate response-fn 3 query-params)
               *http-iterate1 (http-iterate status400-fn 3 query-params)]
           *http-iterate0 => #(= (count %) 30)
           *http-iterate1 => empty?

           )
         )

  (facts "About 'activities-search"
         ((partial http-get 0) 1) => response
         (activities-search 2 :query ["#lol"] :maxResults 2) => #(= 20 (count %))
         (activities-search 2 :query ["#lol"] :maxResults 2) => #(-> % first :id)
         (background
           (http-get & anything) => response)
         )

  )
