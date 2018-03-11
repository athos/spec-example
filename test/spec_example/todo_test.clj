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
                (s/gen (s/coll-of ::todo/task-contents))))))

(defspec new-task-is-added-as-pending
  (prop/for-all [tasks (s/gen ::task-list)
                 contents (s/gen ::todo/task-contents)]
    (let [tasks' (todo/add-task tasks contents)
          task (todo/fetch-task tasks' (todo/count-tasks tasks))]
      (= (:status task) :pending))))

(defspec update-status-is-idempotent
  (prop/for-all [tasks (s/gen ::task-list)]
    (prop/for-all [id (gen/fmap :id (gen/elements tasks))]
      (let [tasks' (todo/update-status tasks id :done)]
        (= tasks'
           (todo/update-status tasks id :done))))))
