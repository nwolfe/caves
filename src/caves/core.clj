(ns caves.core
  (:gen-class)
  (:require [lanterna.screen :as s]))

(defn -main
  [& args]
  (let [screen (s/get-screen :auto)]
    (s/in-screen screen
                 (s/put-string screen 0 0 "Welcome to Caves of Clojure!")
                 (s/put-string screen 0 1 "Press any key to exit...")
                 (s/redraw screen)
                 (s/get-key-blocking screen))))
