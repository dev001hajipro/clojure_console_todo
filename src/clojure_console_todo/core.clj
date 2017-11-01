(ns clojure-console-todo.core
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(def cli-options
  [["-h" "--help"]
   ["-a" "--add" "Add todo"]
   ["-l" "--ls" "Print todo list"]
   ["-d" "--delete ID" "Delete todo by id"
    :parse-fn #(Integer/parseInt %)]])   

(defn -main [& args]
  ; todo create a data file unless it exist. 
  ; load-data
  (do
    (load-file-todos cache-todo))
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (and (:add options) (= 2 (count arguments))) (do
                                                     (let [title (nth arguments 0), text (nth arguments 1)]
                                                       (save-todo cache-todo (gen-todo-index cache-todo) (mk-todo title text)))
                                                     (store-file-todos cache-todo))
      (:delete options) (do 
                          (rm-todo cache-todo (:delete options))
                          (store-file-todos cache-todo))
                                                     
      (:ls options) (do (print @cache-todo))
      (:help options) (do (print summary))
      errors (do (print errors))
      :else (print summary))))
        

(def cache-todo
  (atom {}))

(defn gen-todo-index [m]
  (inc (count @m)))
  
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

(defn load-file-todos [m]
  (swap! m merge 
    (binding [*read-eval* false]
      (read-string (slurp "todos.clj")))))

