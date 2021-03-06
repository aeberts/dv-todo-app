(ns alexeberts.dv-todo-app.server.db-layer
  (:require
    [com.fulcrologic.guardrails.core :refer [>defn >def | => ?]]
    [alexeberts.dv-todo-app.data-model.task :as dm]
    [dv.crux-util :as cu]
    [taoensso.timbre :as log]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; DB API
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-task [crux-node id] (select-keys (cu/domain-entity crux-node id) dm/db-task-keys))
(defn get-all-tasks [crux-node]
  (cu/crux-select crux-node dm/all-task-keys))

(defn insert-task [crux-node t] (cu/insert-entity crux-node :task/id t))
(defn read-merge-task
  [crux-node task-data]
  (cu/read-merge-entity crux-node :task/id task-data))

(defn read-merge-user
  [crux-node user-data]
  (cu/read-merge-entity crux-node :user/id user-data))

;; does db update
(defn merge-user!
  "(merge-user! {:new user data})"
  [crux-node user-data]
  (cu/merge-domain-entity crux-node :user/id user-data))
