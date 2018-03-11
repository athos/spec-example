(ns spec-example.todo
  (:require [clojure.spec.alpha :as s]))

(s/def ::description string?)
(s/def ::due-date inst?)
(s/def ::task-contents
  (s/keys :req-un [::description]
          :opt-un [::due-date]))
(s/def ::id nat-int?)
(s/def ::status #{:pending :done})
(s/def ::task
  (s/merge (s/keys :req-un [::id ::status])
           ::task-contents))

(s/def ::items (s/map-of ::id ::task))
(s/def ::task-list (s/keys :req-un [::items]))

(def empty-task-list
  {:items (sorted-map)})

(s/fdef add-task
  :args (s/cat :tasks ::task-list :task ::task-contents)
  :ret ::task-list)

(defn add-task [tasks task]
  (let [id (count (:items tasks))
        task (assoc task :id id :status :pending)]
    (assoc-in tasks [:items id] task)))

(s/fdef count-tasks
  :args (s/cat :tasks ::task-list)
  :ret int?)

(defn count-tasks [tasks]
  (count (:items tasks)))

(s/fdef fetch-tasks
  :args (s/cat :tasks ::task-list)
  :ret (s/coll-of ::task))

(defn fetch-tasks [tasks]
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

(s/fdef update-due-date
  :args (s/cat :tasks ::task-list :id ::id :due-date ::due-date)
  :ret ::task-list)

(defn update-due-date [tasks id due-date]
  (assoc-in tasks [:items id :due-date] due-date))
