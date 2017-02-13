(ns apis.helpers-test
  (:require [midje.sweet :refer :all]
            [apis.helpers :refer :all]))


(facts "About `flatten1L"
       (flatten1L [[1 2] [3 4]]) => (just [1 2 3 4])
       )
