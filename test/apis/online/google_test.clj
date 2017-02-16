(ns apis.online.google-test
  (:require [midje.sweet :refer :all]
            [apis.google :refer :all]
            [cheshire.core :as json]
            [net.cgrand.enlive-html :as html]
            [clj-http.client :as http]
            ))

(def api-key "**insert your GoogleAps api-key with Google+Api enabled**")

(future-facts "About `http-get"
              (let [query-params (assoc *query-params 
                                        :query ["marketing"] 
                                        :key api-key 
                                        :maxResults 3)
                    *http-get (http-get [:activities] query-params)]
                (-> *http-get :status) => 200
                (-> *http-get :body (json/parse-string true) :items) => #(= (count %) 3) 
                ))
