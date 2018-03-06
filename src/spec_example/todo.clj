(ns spec-example.todo
  (:require [clojure.spec.alpha :as s]))

(s/def ::description string?)
(s/def ::status #{:pending :done})
(s/def ::due-date inst?)
(s/def ::task
  (s/keys :req-un [::description ::status]
          :opt-un [::due-date]))

(s/def ::items (s/coll-of ::task :kind vector?))
(s/def ::task-list
  (s/keys :req-un [::items]))

(def empty-task-list
  {:items []})

(s/fdef add-task
  :args (s/cat :tasks ::task-list :task ::task)
  :ret ::task-list)

(defn add-task [tasks task]
  (update tasks :items conj task))

(s/fdef update-status
  :args (s/cat :tasks ::task-list :id int? :status ::status)
  :ret ::task-list)

(defn update-status [tasks id status]
  (update-in tasks [:items id :status] status))

(s/fdef update-due-date
  :args (s/cat :tasks ::task-list :id int? :due-date ::due-date)
  :ret ::task-list)

(defn update-due-date [tasks id due-date]
  (update-in tasks [:items id :due-date] due-date))
