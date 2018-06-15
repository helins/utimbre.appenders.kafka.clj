(defproject dvlopt/utimbre.appenders.kafka
            "0.0.0"

  :description  "Timbre appender for Apache Kafka"
  :url          "https://github.com/dvlopt/timbre.appenders.kafka"
  :license      {:name "Eclipse Public License"
                 :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[com.taoensso/nippy "2.13.0"]
                 [dvlopt/ex          "1.0.0"]
                 [dvlopt/milena      "0.4.0-alpha3"]
                 [dvlopt/utimbre     "0.0.1"]
                 [dvlopt/void        "0.0.0"]]
  :profiles     {:dev {:source-paths ["dev"]
                       :main         user
                       :dependencies [[com.taoensso/timbre    "4.10.0"]
                                      [criterium              "0.4.4"]
                                      [org.clojure/clojure    "1.9.0"]
                                      [org.clojure/test.check "0.10.0-alpha2"]]
                       :plugins      [[lein-codox      "0.10.3"]
                                      [venantius/ultra "0.5.2"]]
                       :codox        {:output-path  "doc/auto"
                                      :source-paths ["src"]}}})
