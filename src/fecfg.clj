(ns fecfg
  (:require [cljs.build.api]))

(defn build []
  (cljs.build.api/build "src/frontend"
    {:main 'frontend.core
     :output-dir "resources/static/js/out"
     :output-to "resources/static/js/main.js"
     :asset-path "js/out"}))

(defn watch []
  (cljs.build.api/watch "src/frontend"
    {:main 'frontend.core
     :output-dir "resources/static/js/out"
     :output-to "resources/static/js/main.js"
     :asset-path "js/out"}))
