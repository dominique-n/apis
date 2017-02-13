(ns apis.google
  (:require [clojure.java.io :as io]
            [clojure-csv.core :as csv]
            [cheshire.core :as json]
            [net.cgrand.enlive-html :as html]
            [clj-http.client :as http]
            [apis.helpers :refer :all]))


(def api-key "AIzaSyCKGpCxvPvgbeLhvJX12P56jooqL9YH8tQ")

(defn extract-items [response]
  (-> response :body (json/parse-string true) :items))

(defn get-next-page-token [response]
  (-> response :body (json/parse-string true) :nextPageToken))

(defn http-activities [query-params]
  (http/get "https://www.googleapis.com/plus/v1/activities"
                           {:insecure? true
                            :query-params query-params}))

(defn http-iterate [http size query-params]
  (->> (http query-params)
       (iterate #(http (assoc query-params :pageToken (get-next-page-token %))))
       (take size)
       (map extract-items) flatten1L))

(defn get-activities [size query-params]
  (let [request #(http/get "https://www.googleapis.com/plus/v1/activities"
                           {:insecure? true
                            :query-params %})]
    (->> (request query-params)
         (iterate #(request (assoc query-params :pageToken (get-next-page-token %))))
         (take size)
         (map extract-items) flatten1L)))

(def query-params {:key api-key
                   :query ["#opendata" "#datajournalism"]
                   :orderBy "recent"
                   :maxResults 20})

