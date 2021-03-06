(ns alexeberts.dv-todo-app.client.ui.root
  (:require
    [com.fulcrologic.fulcro.application :as app]
    [com.fulcrologic.fulcro.components :as c :refer [defsc]]
    [com.fulcrologic.fulcro.dom :as dom :refer [div]]
    [com.fulcrologic.fulcro.routing.dynamic-routing :as dr]
    [com.fulcrologic.fulcro.ui-state-machines :as sm]
    [dv.fulcro-reitit :as fr]
    [dv.cljs-emotion-reagent :refer [global-style theme-provider]]
    [alexeberts.dv-todo-app.client.ui.task-item :refer [ui-task-list TaskList TaskForm ui-task-form]]
    [alexeberts.dv-todo-app.client.application :refer [SPA]]
    [alexeberts.dv-todo-app.client.ui.task-page :refer [TaskPage]]
    [alexeberts.dv-todo-app.client.ui.styles.app-styles :as styles]
    [alexeberts.dv-todo-app.client.ui.styles.global-styles :refer [global-styles]]
    [alexeberts.dv-todo-app.client.ui.styles.style-themes :as themes]
    [alexeberts.dv-todo-app.auth.login :refer [ui-login Login Session session-join valid-session?]]
    [alexeberts.dv-todo-app.auth.signup :refer [Signup]]
    [taoensso.timbre :as log]))

 (dr/defrouter TopRouter
   [this {:keys [current-state route-factory route-props]}]
   {:router-targets [TaskPage Signup]})

 (def ui-top-router (c/factory TopRouter))

 (defn menu [{:keys [session? login]}]
   [:div.ui.secondary.pointing.menu
    (concat
      (map (fn [p] ^{:key (name p)} [:a.item {:href (fr/route-href p)} (name p)])
        (if session? [:default :tasks] [:default]))
      [(ui-login login)])])

  (defsc PageContainer [this {:root/keys [router login] :as props}]
    {:query         [{:root/router (c/get-query TopRouter)}
                     [::sm/asm-id ::TopRouter]
                     session-join
                     {:root/login (c/get-query Login)}]
     :ident         (fn [] [:component/id :page-container])
     :initial-state (fn [_] {:root/router             (c/get-initial-state TopRouter {})
                             :root/login              (c/get-initial-state Login {})
                             :root/signup             (c/get-initial-state Signup {})
                             [:component/id :session] (c/get-initial-state Session {})})}
    (let [current-tab (fr/current-route this)
          session? (valid-session? props)]
      [:div.ui.container
        (menu {:session? session? :login login})
        (ui-top-router router)]))

(fr/register-fulcro-router! SPA TopRouter)

(def ui-page-container (c/factory PageContainer))

(defsc Root [this {:root/keys [page-container style-theme]}]
  {:query         [{:root/page-container (c/get-query PageContainer)}
                   :root/style-theme]
   :initial-state (fn [_] {:root/page-container (c/get-initial-state PageContainer {})
                           :root/style-theme themes/light-theme})}
  (theme-provider
    {:theme style-theme}
    (global-style (global-styles style-theme))
    [:button {:on-click #(styles/toggle-app-styles! this style-theme)} "Switch Theme"]
    (ui-page-container page-container)))
