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
      body (make-body response)
      status400 (json/parse-string (slurp (str path "status400.json")) true)
      body400 (make-body status400)
      response-fn (fn [& _] response) 
      status400-fn (fn [& _] status400) 
      items (json/parse-string (slurp (str path "items.json")) true)
      [item0 item1 & _] items]


  (facts "About `http-iterate"
         (let [query-params (assoc (apply hash-map *query-params) :maxResults 10)
               *http-iterate0 (http-iterate response-fn 3 query-params)
               *http-iterate1 (http-iterate status400-fn 3 query-params)]
           (count *http-iterate0) => 3
           (map :status *http-iterate0) => (three-of 200)
           (->> *http-iterate0 (map #(-> % :body :items)) flatten count) => 30

           (:status (first *http-iterate1)) => 400
           (count *http-iterate1) => 1
           )
         )

  (facts "About `extract-items"
                (let [it-ok (repeat 3 {:status 200 :body body})
                      it-error (reverse (cons {:status 400 :body body400} it-ok))]

                  (count (extract-items it-ok)) => 30

                  (count (extract-items it-error)) => 31
                  (last (extract-items it-error)) => #(has :error %)
                  ))

  (facts "About `time-stopper"
                (count (time-stopper 2000 items)) => (count items)
                (time-stopper 2020 items) => empty?
                ) 

  (facts "About 'activities-search"
         ((partial http-get 0) 1) => response
         (activities-search 2 :query ["#lol"] :maxResults 2) => #(= 20 (count %))
         (activities-search 2 :query ["#lol"] :maxResults 2) => #(-> % first :id)
         (background
           (http-get & anything) => response)
         )

  )
