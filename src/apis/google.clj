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
                   :maxResults 20
                   })

(defn http-get [actions query-params]
  (let [action (clojure.string/join "/" (mapv name actions))] 
    (http/get (str "https://www.googleapis.com/plus/v1/" action)
            {:insecure? true
             :throw-exceptions false
             :query-params (merge *query-params query-params)})))

(defn make-body [response] 
  (-> response :body (json/parse-string true)))

(defn http-iterate [http size query-params]
  (let [response0 (http query-params)]
    (->> {:status (:status response0) :body (make-body response0)}
       (iterate (fn [{status :status body :body}]
                  (if (= 200 status)
                    (let [*response (http (assoc query-params :pageToken (:nextPageToken body)))]
                      {:status (:status *response) :body (make-body *response)}))))
       (take-while identity) (take size))))

(defn extract-items [iterator]
  (flatten1L 
    (map (fn [{status :status body :body}]
           (if (= status 200) (:items body)
             [body]))
         iterator)))

(defn time-stopper [year-bound items]
  (let [extract-year #(->> % :published (re-seq #"^\d{4}" ) first Integer.)] 
    (take-while #(>= (extract-year %) year-bound) 
              items)))

(defn activities-search 
  ([size & query-params]
   (let [http (partial http-get ["activities"])
         *query-params (apply hash-map query-params)]
     (extract-items (http-iterate http size *query-params)))))
