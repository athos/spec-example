(ns user
  (:require [clojure.spec.alpha :as s]))

(defn goto [ns-name]
  (require ns-name)
  (in-ns ns-name)
  (require '[clojure.spec.alpha :as s]
           '[clojure.spec.test.alpha :as st]
           '[clojure.test.check.generators :as gen]
           '[clojure.pprint :refer [pp]]))

(comment

  Try

  (require '[expound.alpha :as ex])
  (set! s/*explain-out* ex/printer)

  or

  (require '[pinpointer.core :as p])
  (set! s/*explain-out* #(p/pinpoint-out % {:colorize :ansi}))

  to replace the explain printer with your favorite one

  )
