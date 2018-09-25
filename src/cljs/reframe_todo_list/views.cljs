(ns reframe-todo-list.views
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [material-ui :as mui]
            [material-ui-icons :as icons]))

(defn item-edit-view [props text]
  (let [text (r/atom text)]
    (fn []
      [:div
       [:> mui/TextField {:value @text
                          :on-change #(reset! text (-> % .-target .-value))}]
       [:> mui/IconButton {:on-click #((props :on-done) @text)}
        [:> icons/Done]]])))

(defn item-view [props item]
  (let [show-delete (r/atom false)]
    (fn [props {id :id text :text done :done :as item}]
      [:div.item {:class (if done "done" "")
                  :on-mouse-over #(reset! show-delete true)
                  :on-mouse-out #(reset! show-delete false)}
       [:> mui/Checkbox {:checked done
                         :on-change #((props :on-check) id %2)}]

       [:span.text {:on-click (props :on-start-editing)} text]

       [:> mui/IconButton {:class (if @show-delete "" "hidden")
                           :on-click #((props :on-delete) id)}
        [:> icons/Delete]]])))

(defn item-component [props item]
  (let [editing? (r/atom false)]
    (fn [props {text :text :as item}]
      [:div (if @editing?
              [item-edit-view {:on-done #(do (reset! editing? false)
                                             ((props :on-update) %1))} text]
              [item-view (assoc props :on-start-editing #(reset! editing? true)) item])])))

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


(defn home-page []
  [:div.container
   [item-input-component {:on-add #(rf/dispatch [:add-item %])}]
   (when-let [items @(rf/subscribe [:sorted-items])]
     [:div (for [item items]
             ^{:key (item :id)}
             [item-component {:on-check #(rf/dispatch [:set-item-done %1 %2])
                              :on-delete #(rf/dispatch [:delete-item %1])
                              :on-update #(rf/dispatch [:update-item (item :id) %1])} item])])])
