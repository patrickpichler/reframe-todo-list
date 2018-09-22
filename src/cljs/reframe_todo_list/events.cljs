(ns reframe-todo-list.events
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]))

;;dispatchers

(rf/reg-event-db
 :initialize-db
 (fn [_ _]
   (assoc {} :text "Hello world"
          :last-item-id 1
          :items {})))

(rf/reg-event-db
 :add-item
 (fn [{last-id :last-item-id items :items :as db} [_ text]]
   (assoc db
          :items (assoc items last-id {:id last-id :text text :done false})
          :last-item-id (inc last-id))))

(rf/reg-event-db
 :set-item-done
 (fn [db [_ id done]]
   (update-in db [:items id :done] (constantly done))))

(rf/reg-event-db
 :set-docs
 (fn [db [_ docs]]
   (assoc db :docs docs)))

(rf/reg-event-fx
 :fetch-docs
 (fn [_ _]
   {:http {:url "/docs"
           :method :get
           :success-event [:set-docs]}}))

;;subscriptions

(rf/reg-sub
 :text
 (fn [db _]
   (:text db)))

(rf/reg-sub
 :sorted-items
 (fn [db _]
   (map #(second %) (sort-by #((second %) :id) (:items db)))))
