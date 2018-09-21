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

(defn render-item [{id :id text :text done :done}]
  [:div {:key id} text])

(defn render-input []
  (let [input-text (r/atom "")]
    [:div [:input {:type "text"
                   ; :value @input-text
                   :on-change #(reset! input-text (-> % .-target .-value))}]
     [:button {:on-click #((do (rf/dispatch [:add-item @input-text])
                               (reset! input-text "")))}
      "Add"]]))

(defn home-page []
  [:div.container
   (render-input)
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
  (mount-components))
