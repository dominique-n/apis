(ns apis.google
  (:require [clojure.java.io :as io]
            [clojure-csv.core :as csv]
            [cheshire.core :as json]
            [net.cgrand.enlive-html :as html]
            [clj-http.client :as http]
            [apis.helpers :refer :all]))


(def *query-params {:key "api-key"
                   :query ["#term0" "#term1"]
                   :orderBy "recent"
                   :maxResults 20})

(defn http-get [actions query-params]
  (let [action (clojure.string/join "/" actions)] 
    (http/get (str "https://www.googleapis.com/plus/v1/" action)
            {:insecure? true
             :query-params (merge *query-params query-params)})))

(def activities-search (partial http-get ["activities"]))

(defn http-iterate [http size query-params]
  (->> [nil  (http query-params)]
       (iterate (fn [[_ response]]
                  (if (= 200 (:status response))
                    (let [body (-> response :body (json/parse-string true))]
                      [(:items body) (http (assoc query-params :pageToken (:nextPageToken body)))]
                      ))))
       next (map first)
       (take-while identity) (take size)
       flatten1L))

(defn activities-search [size & query-params]
  (let [http (partial http-get ["activities"])
        *query-params (apply hash-map query-params)]
    (http-iterate http size *query-params)))


