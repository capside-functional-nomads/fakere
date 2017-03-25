(ns fakere.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]))

;; This is a complete application handler

(defn app
  [req]
  {:status 200
   :headers {"Content-type" "text/plain"}
   :body "ok"})
