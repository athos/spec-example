(defproject spec-example "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]]
  :profiles {:dev {:source-paths ["dev"]
                   :dependencies
                   [[com.gfredericks/test.chuck "0.2.8"]
                    [expound "0.5.0"]
                    [org.clojure/test.check "0.9.0"]
                    [pinpointer "0.1.0"]]}})
