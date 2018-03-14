(ns spec-example.todo
  (:require [clojure.spec.alpha :as s]))

(s/def ::id nat-int?)
(s/def ::description string?)
(s/def ::status #{:pending :done})
(s/def ::task (s/keys :req-un [::id ::description ::status]))

(s/def ::items (s/map-of ::id ::task))
(s/def ::task-list (s/keys :req-un [::items]))

(def empty-task-list
  {:items {}})

(s/fdef add-task
  :args (s/cat :tasks ::task-list :description ::description)
  :ret ::task-list)

(defn add-task [tasks description]
  (let [id (count (:items tasks))
        task {:id id
              :description description
              :status :pending}]
    (assoc-in tasks [:items id] task)))

#_(s/fdef count-tasks
  :args (s/cat :tasks ???)
  :ret ???)

(defn count-tasks [tasks]
  (count (:items tasks)))

#_(s/fdef all-tasks
  :args (s/cat :tasks ???)
  :ret ???)

(defn all-tasks [tasks]
  (sequence (vals (:items tasks))))

(s/fdef fetch-task
  :args (s/cat :tasks ::task-list :id ::id)
  :ret (s/nilable ::task))

(defn fetch-task [tasks id]
  (get-in tasks [:items id]))

(s/fdef update-status
  :args (s/cat :tasks ::task-list :id ::id :status ::status)
  :ret ::task-list)

(defn update-status [tasks id status]
  (assoc-in tasks [:items id :status] status))
