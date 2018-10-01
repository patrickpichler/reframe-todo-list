(ns reframe-todo-list.views
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [material-ui :as mui]
            [material-ui-icons :as icons]))

(defn item-edit-view [{on-done :on-done :as props} initial-text]
  (let [text (r/atom initial-text)]
    (fn []
      [:div
       [:> mui/TextField {:value @text
                          :auto-focus true
                          :on-blur #(on-done @text)
                          :on-change #(reset! text (-> % .-target .-value))
                          :on-key-down #(case (.-which %)
                                          13 (on-done @text)
                                          27 (on-done initial-text)
                                          nil)}]
       [:> mui/IconButton {:on-click #(on-done @text)}
        [:> icons/Done]]])))

(defn item-view [props item]
  (let [show-delete (r/atom false)]
    (fn [props {id :id text :text done :done :as item}]
      [:div.item {:class (if done "done" "")
                  :on-focus #(reset! show-delete true)
                  :on-blur #(reset! show-delete false)
                  :on-mouse-over #(reset! show-delete true)
                  :on-mouse-out #(reset! show-delete false)}
       [:> mui/Checkbox {:checked done
                         :on-change #((props :on-check) id %2)}]

       [:span.text {:tabindex "0"
                    :on-key-down #(case (.-which %)
                                    13 ((props :on-start-editing)))
                    :on-click (props :on-start-editing)} text]

       [:> mui/IconButton {:tabindex "1"
                           :class (if @show-delete "" "hidden")
                           :on-click #((props :on-delete) id)}
        [:> icons/Delete]]])))

(defn item-component [{on-update :on-update
                       on-delete :on-delete :as props} item]
  (let [editing? (r/atom false)]
    (fn [props {id :id text :text :as item}]
      [:div (if @editing?
              [item-edit-view {:on-done #(do (reset! editing? false)
                                             (println %1)
                                             (if (clojure.string/blank? %1)
                                               (on-delete id)
                                               (on-update %1)))} text]
              [item-view (assoc props :on-start-editing #(reset! editing? true)) item])])))

(defn item-input-component
  ([props] (item-input-component props ""))

  ([{on-add :on-add :as :props} initial-text]
   (let [text (r/atom initial-text)
         add-action #(if (not (clojure.string/blank? @text))
                           (do (on-add @text)
                            (reset! text initial-text)))]
     (fn []
       [:div [:> mui/TextField {:margin :normal
                                :value @text
                                :on-key-down #(case (.-which %)
                                                13 (add-action)
                                                nil)
                                :on-change #(reset! text (-> % .-target .-value))}]

        [:> mui/IconButton {:on-click add-action
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
