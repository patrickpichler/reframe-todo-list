(ns reframe-todo-list.views
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [material-ui :as mui]
            [material-ui-icons :as icons]))

(defn item-view [props item]
  (let [show-delete (r/atom false)]
    (fn [props {id :id text :text done :done :as item}]
      [:div.item {:class (if done "done" "")
                  :on-mouse-over #(reset! show-delete true)
                  :on-mouse-out #(reset! show-delete false)}
       [:> mui/Checkbox {:checked done
                         :on-change #((props :on-check) id %2) }]
       [:span.text text]
       [:> mui/IconButton {:class (if @show-delete "" "hidden")
                           :on-click #((props :on-delete) id)}
        [:> icons/Delete]]])))

(defn item-input-component
  ([props] (item-input-component props ""))

  ([props initial-text]
   (let [text (r/atom initial-text)]
     (fn []
       [:div [:> mui/TextField {:margin :normal
                                :value @text
                                :on-change #(reset! text (-> % .-target .-value))}]
              [:> mui/IconButton {:on-click #(do ((props :on-add) @text)
                                                 (reset! text initial-text))
                                  :disabled (clojure.string/blank? @text)}
               [:> icons/Add]]]))))

(defn counter-component [initial-value]
  (let [counter (r/atom initial-value)]
    (fn []
      [:div
       [:button {:on-click #(swap! counter inc)} @counter]])))

(defn home-page []
  [:div.container
   [item-input-component {:on-add #(rf/dispatch [:add-item %])}]
   (when-let [items @(rf/subscribe [:sorted-items])]
     [:div (for [item items]
             ^{:key (item :id)}
             [item-view {:on-check #(rf/dispatch [:set-item-done %1 %2])
                         :on-delete #(rf/dispatch [:delete-item %1])} item])])])
