(ns alexeberts.dv-todo-app.data-model.todo
  (:require
    [clojure.spec.alpha :as s]
    [clojure.string :as str]
    [com.fulcrologic.guardrails.core :refer [>defn >def | => ?]]
    [dv.fulcro-util-common :as fu]
    #?(:clj [dv.crux-util :as cu])
    [taoensso.timbre :as log]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Sample Todo Model
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(s/def :todo/id fu/id?)
(s/def :todo/label string?)
;; TODO: We should check for the specific :status enums :done :not-started :completed
(s/def :todo/status keyword?)

(def required-todo-keys [:todo/id :todo/label :todo/status])
(def optional-todo-keys [])
(def all-todo-keys (into required-todo-keys optional-todo-keys))
(def global-keys [:db/created-at :db/updated-at :crux.db/id])
(def db-todo-keys (into all-todo-keys global-keys))

(s/def ::todo (s/keys :req [:todo/id :todo/label :todo/status]
                :opt [:db/created-at :db/updated-at :crux.db/id]))

(>defn make-todo
  [{:todo/keys [id label status]
    :or        {id (fu/uuid)}}]
  [map? => ::todo]
  {:todo/id     id
   :todo/label  label
   :todo/status status})

(comment (make-todo {:todo/label "TEST"}))

(defn fresh-todo [props]
  (make-todo (merge props {:todo/id (fu/uuid)})))

#?(:clj (cu/gen-make-db-entity make-db-task ::todo))

(comment
  (make-todo {:todo/id    :TESTING
              :todo/label "Test Label from data-model.todo"
              :todo/status :not-started}))

