(ns clojure-console-todo.core
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(def cli-options
  ;; An option with a required argument
  [["-p" "--port PORT" "Port number"
    :default 80
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
   ;; A non-idempotent option
   ["-v" nil "Verbosity level"
    :id :verbosity
    :default 0
    :assoc-fn (fn [m k _] (update-in m [k] inc))]
   ;; A boolean option defaulting to nil
   ["-h" "--help"]])

(def todos (atom []))

(defn create [title text]
  {:id (hash title) :title title, :text text, :done false})

(defn add [todo todos]
  (swap! todos conj todo))

(defn remove_ [id todos]
  (swap! todos #(remove (fn [item] (= (:id item) id)) %)))
  
(defn -main [& args]
  (println "Hello, Simple TODO list."))
  ;((parse-opts args cli-options) :errors)

(def cache-todo
  (atom {}))

(defn gen-todo-index [m]
  (inc (count m)))
  
(defn mk-todo [title text]
  {:title title, :text text, :done false})

(defn save-todo [m index todo]
  (swap! m assoc-in [index] todo))

(defn rm-todo [m index]
  (swap! m dissoc index))

(defn get-todo [m index]
  (m index))

(defn toggle-done-todo [m index]
  (swap! m assoc-in [index :done] (not ((@m index) :done))))
  
(defn str-todo [m]
  (pr-str @m))

(defn read-todo [str-todos]
  (read-string str-todos))

(defn store-file-todos [m]
  (let [data (str-todo m)]
    (spit "todos.clj" data)))

; http://tnoda-clojure.tumblr.com/post/28499910150/collection-literals-instead-of-json
(defn load-file-todos [m]
  (swap! m merge (read-string (slurp "todos.clj"))))

