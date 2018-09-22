(ns reframe-todo-list.core
  (:require [reframe-todo-list.ajax :as ajax]
            [reframe-todo-list.views :as views]
            [reframe-todo-list.events]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(defn page []
  [:div
   [views/home-page]])

;; -------------------------
;; Initialize app
(defn mount-components []
  (rf/clear-subscription-cache!)
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch-sync [:initialize-db])
  (mount-components))
