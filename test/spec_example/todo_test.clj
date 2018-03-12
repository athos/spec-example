(ns spec-example.todo-test
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [clojure.test :as t :refer [is]]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [com.gfredericks.test.chuck.clojure-test :refer [for-all]]
            [spec-example.todo :as todo]))

(t/use-fixtures :once
  (fn [f]
    (stest/instrument (stest/enumerate-namespace 'spec-example.todo))
    (f)
    (stest/unstrument)))

(s/def ::task-list
  (s/with-gen
    ::todo/task-list
    (fn []
      (gen/fmap #(reduce todo/add-task todo/empty-task-list %)
                (s/gen (s/coll-of ::todo/description))))))

(defspec prop-add-task-to-empty
  (for-all [description (s/gen ::todo/description)]
    (let [tasks (todo/add-task todo/empty-task-list description)
          task (todo/fetch-task tasks 0)]
      (is (= (:description task) description))
      (is (= (:status task) :pending)))))

(defspec prop-add-task
  (for-all [tasks (s/gen ::task-list)
            description (s/gen ::todo/description)]
    (let [tasks' (todo/add-task tasks description)
          task (todo/fetch-task tasks' (todo/count-tasks tasks))]
      (is (= (:description task) description))
      (is (= (:status task) :pending)))))

(defspec prop-update-status
  (for-all [tasks (s/gen (s/and ::task-list
                                #(pos? (todo/count-tasks %))))
            id (gen/fmap :id (gen/elements (todo/all-tasks tasks)))
            status (s/gen ::todo/status)]
    (let [tasks' (todo/update-status tasks id status)
          task (todo/fetch-task tasks' id)]
      (is (= (:status task) status)))))

(defspec prop-idempotent-update-status
  (for-all [tasks (s/gen (s/and ::task-list
                                #(pos? (todo/count-tasks %))))
            id (gen/fmap :id (gen/elements (todo/all-tasks tasks)))
            status (s/gen ::todo/status)]
    (let [tasks' (todo/update-status tasks id status)]
      (is (= tasks' (todo/update-status tasks' id status))))))
