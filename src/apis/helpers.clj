(ns apis.helpers)

(defn flatten1L [coll]
  (for [l1 coll, x l1] x))

