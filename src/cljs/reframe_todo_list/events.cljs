(ns reframe-todo-list.events
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]))

;;dispatchers

(rf/reg-event-db
 :initialize-db
 (fn [_ _]
   (assoc {} :text "Hello world"
          :last-item-id 1
          :items '({:id 0 :text "test" :done false}))))

(defn add-to-items [db text]
  (cons (:items db) {:id (:last-item-id db) :text text :done false}))

(rf/reg-event-db
 :add-item
 (fn [db [_ text]]
   (assoc db :items (add-to-items db text)
          :last-item-id (inc (:last-item-id db)))))

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
 :items
 (fn [db _]
   (reverse (:items db))))

