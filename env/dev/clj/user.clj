(ns user
  (:require [reframe-todo-list.config :refer [env]]
            [clojure.spec.alpha :as s]
            [expound.alpha :as expound]
            [mount.core :as mount]
            [reframe-todo-list.figwheel :refer [start-fw stop-fw cljs]]
            [reframe-todo-list.core :refer [start-app]]))

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(defn start []
  (mount/start-without #'reframe-todo-list.core/repl-server))

(defn stop []
  (mount/stop-except #'reframe-todo-list.core/repl-server))

(defn restart []
  (stop)
  (start))


