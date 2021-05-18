(ns alexeberts.dv-todo-app.server.server-entry
  (:require
    [mount.core :as mount]
    alexeberts.dv-todo-app.server.server)
  (:gen-class))

(defn -main [& args]
  (println "args: " args)
  (mount/start-with-args {:config "config/prod.edn"}))