(ns caves.ui.drawing
  (:require [lanterna.screen :as s]))

(defmulti draw-ui
  (fn [ui game screen]
    (:kind ui)))

(defmethod draw-ui :start [ui game screen]
  (s/put-sheet screen 0 0
               ["Welcome to the Caves of Clojure!"
                ""
                "Press any key to continue."
                "Once in the game, you can use Enter to win, and Backspace to lose."]))
  
(defmethod draw-ui :win [ui game screen]
  (s/put-string screen 0 0 "Congratulations, you win!")
  (s/put-string screen 0 1 "Press Escape to exit, anything else to restart."))

(defmethod draw-ui :lose [ui game screen]
  (s/put-string screen 0 0 "Sorry, better luck next time.")
  (s/put-string screen 0 1 "Press Escape to exit, anything else to restart."))

(defn draw-world [screen vrows vcols start-x start-y end-x end-y tiles]
      (doseq [[vrow-idx mrow-idx] (map vector
                                     (range 0 vrows)
                                     (range start-y end-y))
            :let [row-tiles (subvec (tiles mrow-idx) start-x end-x)]]
      (doseq [vcol-idx (range vcols)
              :let [{:keys [glyph color]} (row-tiles vcol-idx)]]
        (s/put-string screen vcol-idx vrow-idx glyph {:fg color}))))

(defn draw-crosshairs [screen vcols vrows]
  (let [crosshair-x (int (/ vcols 2))
        crosshair-y (int (/ vrows 2))]
    (s/put-string screen crosshair-x crosshair-y "X" {:fg :red})
    (s/move-cursor screen crosshair-x crosshair-y)))

(defn get-viewport-coords [game vcols vrows]
  (let [location (:location game)
        [center-x center-y] location
        tiles (:tiles (:world game))
        map-rows (count tiles)
        map-cols (count (first tiles))        
        start-x (max 0 (- center-x (int (/ vcols 2))))
        start-y (max 0 (- center-y (int (/ vrows 2))))
        end-x (+ start-x vcols)
        end-x (min end-x map-cols)
        end-y (+ start-y vrows)
        end-y (min end-y map-rows)
        start-x (- end-x vcols)
        start-y (- end-y vrows)]
    [start-x start-y end-x end-y]))

(defmethod draw-ui :play [ui game screen]
  (let [world (:world game)
        tiles (:tiles world)
        [cols rows] (s/get-size screen)
        vcols cols
        vrows (dec rows)
        [start-x start-y end-x end-y] (get-viewport-coords game vcols vrows)]
    (draw-world screen vrows vcols start-x start-y end-x end-y tiles)
    (draw-crosshairs screen vcols vrows)))

(defn draw-game [game screen]
  (s/clear screen)
  (doseq [ui (:uis game)]
    (draw-ui ui game screen))
  (s/redraw screen))
