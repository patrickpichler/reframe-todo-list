(ns reframe-todo-list.app
  (:require [reframe-todo-list.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
