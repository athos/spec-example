(ns spec-example.todo-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [spec-example.todo :as todo]))

(s/def ::task-list
  (s/with-gen
    ::todo/task-list
    (fn []
      (gen/fmap #(reduce todo/add-task todo/empty-task-list %)
                (s/gen (s/coll-of ::todo/description))))))

(defspec prop-add-task-to-empty
  (prop/for-all [description (s/gen ::todo/description)]
    (let [tasks (todo/add-task todo/empty-task-list description)
          task (todo/fetch-task tasks 0)]
      (and (= (:description task) description)
           (= (:status task) :pending)))))

(defspec prop-add-task
  (prop/for-all [tasks (s/gen ::task-list)
                 description (s/gen ::todo/description)]
    (let [tasks' (todo/add-task tasks description)
          task (todo/fetch-task tasks' (todo/count-tasks tasks))]
      (and (= (:description task) description)
           (= (:status task) :pending)))))

(defspec prop-update-status
  (prop/for-all [tasks (s/gen ::task-list)]
    (prop/for-all [id (gen/fmap :id (gen/elements tasks))
                   status (s/gen ::todo/status)]
      (let [tasks' (todo/update-status tasks id status)
            task (todo/fetch-task tasks' id)]
        (= (:status task) status)))))

(defspec prop-idempotent-update-status
  (prop/for-all [tasks (s/gen ::task-list)]
    (prop/for-all [id (gen/fmap :id (gen/elements tasks))]
      (let [tasks' (todo/update-status tasks id :done)]
        (= tasks'
           (todo/update-status tasks id :done))))))
