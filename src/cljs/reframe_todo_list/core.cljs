(ns reframe-todo-list.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [ajax.core :refer [GET POST]]
            [reframe-todo-list.ajax :as ajax]
            [reframe-todo-list.events]
            [secretary.core :as secretary])
  (:import goog.History))

; the navbar components are implemented via baking-soda [1]
; library that provides a ClojureScript interface for Reactstrap [2]
; Bootstrap 4 components.
; [1] https://github.com/gadfly361/baking-soda
; [2] http://reactstrap.github.io/

(defn render-item [item]
  (let [{name :name
         done :done} item]
  [:div name]))

(defn home-page []
  [:div.container
   [:div.row>div.col-sm-12
    [:h2.alert.alert-info "Tip: try pressing CTRL+H to open re-frame tracing menu"]]
   [:div [:input]]
   (when-let [items @(rf/subscribe [:items])]
     [:div (map render-item items)])])

(defn page []
  [:div
   [home-page]])

;; -------------------------
;; Initialize app
(defn mount-components []
  (rf/clear-subscription-cache!)
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (rf/dispatch [:initialize-db])
  (ajax/load-interceptors!))
