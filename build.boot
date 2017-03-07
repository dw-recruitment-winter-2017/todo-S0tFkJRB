(set-env!
  :resource-paths #{"src", "resources"}
  :dependencies '[[com.datomic/datomic-free "0.9.5561"]
                  [org.clojure/data.json "0.2.6"]
                  [org.clojure/clojurescript "1.9.495"]
                  [cljs-ajax "0.5.8"]
                  [reagent "0.6.0"]
                  [org.slf4j/slf4j-simple "1.7.22"]
                  [ring-cors "0.1.9"]
                  [io.pedestal/pedestal.service "0.5.2"]
                  [io.pedestal/pedestal.route "0.5.2"]
                  [io.pedestal/pedestal.jetty "0.5.2"]])
