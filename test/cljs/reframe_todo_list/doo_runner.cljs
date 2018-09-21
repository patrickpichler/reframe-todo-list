(ns reframe-todo-list.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [reframe-todo-list.core-test]))

(doo-tests 'reframe-todo-list.core-test)

