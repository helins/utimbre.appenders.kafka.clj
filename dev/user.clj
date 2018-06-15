(ns user

  "For daydreaming in the repl."

  (:require [clojure.spec.alpha              :as s]
            [clojure.spec.gen.alpha          :as gen]
            [clojure.spec.test.alpha         :as st]
            [clojure.test.check.clojure-test :as tt]
            [clojure.test.check.generators   :as tgen]
            [clojure.test.check.properties   :as tprop]
            [clojure.test                    :as t]
            [criterium.core                  :as ct]
            [milena.serialize                :as M.serialize]
            [milena.deserialize              :as M.deserialize]
            [milena.produce                  :as M.produce]
            [milena.consume                  :as M.consume]
            [taoensso.timbre                 :as log]
            [taoensso.nippy                  :as nippy]
            [dvlopt.void                     :as void]
            [dvlopt.ex                       :as ex]
            [dvlopt.utimbre                  :as utimbre]
            [dvlopt.utimbre.appenders.kafka  :as utimbre.appenders.kafka]))




;;;;;;;;;;


(set! *warn-on-reflection*
      true)




;;;;;;;;;;


(comment
  

  (def c 
       (M.consume/make {:deserializer-key   utimbre.appenders.kafka/deserializer-key
                        :deserializer-value utimbre.appenders.kafka/deserializer-value}))

  (M.consume/listen m
                    [["foo-log" 0]])

  (M.consume/rewind m)


  (def p
       (M.produce/make))


  (log/merge-config! {:appenders {::kafka (utimbre.appenders.kafka/make p
                                                                        "foo-log"
                                                                        "some-id")}})
  )
