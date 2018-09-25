(ns reframe-todo-list.views
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [material-ui :as mui]
            [material-ui-icons :as icons]))

(defn item-view [item]
  (let [show-delete (r/atom false)]
    (fn [{id :id text :text done :done :as item}]
      [:div.item {:class (if done "done" "")
                  :on-mouse-over #(reset! show-delete true)
                  :on-mouse-out #(reset! show-delete false)}
       [:> mui/Checkbox {:checked done
                         :on-change #(rf/dispatch [:set-item-done id %2])}]
       [:span.text text]
       [:> mui/IconButton {:class (if @show-delete "" "hidden")
                           :on-click #(rf/dispatch [:delete-item id])}
        [:> icons/Delete]]])))

(defn render-input []
  (r/with-let [input-text (r/atom "")]
    [:div [:> mui/TextField {:margin :normal
                             :value @input-text
                             :on-change #(reset! input-text (-> % .-target .-value))}]
     [:> mui/IconButton {:on-click #(do (rf/dispatch [:add-item @input-text]
                                                     (reset! input-text "")))
                         :disabled (clojure.string/blank? @input-text)}
      [:> icons/Add]]]))

(defn counter-component [initial-value]
  (let [counter (r/atom initial-value)]
    (fn []
      [:div
       [:button {:on-click #(swap! counter inc)} @counter]])))

(defn home-page []
  [:div.container
   (render-input)
   (when-let [items @(rf/subscribe [:sorted-items])]
     [:div (for [item items]
             ^{:key (item :id)}
             [item-view item])])])
