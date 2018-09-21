(ns reframe-todo-list.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [reframe-todo-list.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[reframe-todo-list started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[reframe-todo-list has shut down successfully]=-"))
   :middleware wrap-dev})
