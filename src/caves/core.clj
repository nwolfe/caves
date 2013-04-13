(ns caves.core
  (:gen-class)
  (:require [lanterna.screen :as s]))

(defn main
  [screen-type]
  (let [screen (s/get-screen screen-type)]
    (s/in-screen screen
                 (s/put-string screen 0 0 "Welcome to Caves of Clojure!")
                 (s/put-string screen 0 1 "Press any key to exit...")
                 (s/redraw screen)
                 (s/get-key-blocking screen))))

(defn -main
  [& args]
  (let [args (set args)
        screen-type :auto]
    (main screen-type)))
