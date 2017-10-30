(defproject clojure_console_todo "0.0.1-SNAPSHOT"
  :description "simple console todo-list application by Clojure"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.cli "0.3.5"]]
  :aot [clojure-console-todo.core]
  :main clojure-console-todo.core)
