(ns fakere.core
  (:require [immutant.web :as server]
            [taoensso.timbre :as timbre :refer [log]]
            [cprop.core :refer [load-config]]
            [ring.logger :as logger]
            [fakere.handler :refer [app]])
  (:gen-class))


(def entry-point (logger/wrap-with-logger app))

(defn start-server
  [state prod?]
  (timbre/log :info "Starting server. Environment: " (if prod? "Prod" "Devel"))
  (if prod?
    (swap! state (server/run #'entry-point))
    (swap! state (server/run-dmc #'entry-point))))

(defn schedule-shutdown
  [state]
  (letfn [(stop-server []
            (log :info "Stopping server")
            (swap! state server/stop)
            (log :info "Server stopped"))]
    (Thread. stop-server)))


(defn -main 
  [& args]
  (let [the-server (atom nil)
        config (load-config)]
    (.addShutdownHook (Runtime/getRuntime) (schedule-shutdown the-server))
    (start-server the-server (:prod config))))
