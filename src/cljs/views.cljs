(ns reframe-todo-list.views
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [material-ui :as mui]
            [material-ui-icons :as icons]))

(defn render-item [{id :id text :text done :done :as item}]
  [:div.item {:key id
              :class (if done "done" "")}
   [:> mui/Checkbox {:checked done
                     :on-change #(rf/dispatch [:set-item-done id %2])}]
   [:span.text text]])

(defn render-input []
  (r/with-let [input-text (r/atom "")]
    [:div [:> mui/TextField {:margin :normal
                             :value @input-text
                             :on-change #(reset! input-text (-> % .-target .-value))}]
     [:> mui/IconButton {:on-click #(do (rf/dispatch [:add-item @input-text]
                                                     (reset! input-text "")))
                         :disabled (clojure.string/blank? @input-text)}
      [:> icons/Add]]]))

(defn home-page []
  [:div.container
   (render-input)
   (when-let [items @(rf/subscribe [:sorted-items])]
     [:div (map render-item items)])])
