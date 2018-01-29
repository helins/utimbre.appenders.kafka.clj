(ns dvlopt.utimbre.appenders.kafka

  "Timbre appender for Kafka."

  {:author "Adam Helinski"}

  (:require [clojure.spec.alpha     :as s]
            [clojure.spec.gen.alpha :as gen]
            [milena.serialize       :as M.serialize]
            [milena.deserialize     :as M.deserialize]
            [milena.produce         :as M.produce]
            [dvlopt.void            :as void]
            [dvlopt.ex              :as ex]
            [dvlopt.utimbre         :as utimbre]
            [taoensso.nippy         :as nippy]))




;;;;;;;;;; Specs - Misc


(s/def ::string

  (s/and string?
         not-empty))


(s/def ::id

  ::string)


(s/def ::key

  ::id)


(s/def ::value

  ::utimbre/entry)




;;;;;;;;;; Specs - Opts


(s/def ::serialize-key?

  (s/nilable boolean?))


(s/def ::serialize-value?

  (s/nilable boolean?))


(s/def ::partition

  (s/nilable (s/int-in 0
                       128001)))




;;;;;;;;;; API


(def defaults

  "Defaults values for options from this namespaces."

  {::serialize-key?   true
   ::serialize-value? true
   ::partition        nil})




(def serializer-key

  "Kafka serializer for the ::key."

  M.serialize/string)




(def serializer-value

  "Kafka serializer for the ::value."

  (M.serialize/make (fn serialize [_topic data]
                      (some-> data
                              nippy/freeze))))




(def deserializer-key

  "Kafka deserializer for the ::key."

  M.deserialize/string)




(def deserializer-value

  "Kafka deserializer for the ::value, no validation."

  (M.deserialize/make (fn deserialize [_topic data]
                        (some-> data
                                nippy/thaw))))




(s/fdef make

  :args (s/cat :producer M.produce/producer?
               :topic    ::string
               :id       ::id
               :opts     (s/? (s/keys :opt [::serialize-key?
                                            ::serialize-value?
                                            ::partition]))))

(defn make

  "Makes a Timbre appender for sending log entries to Kafka."

  ([producer topic id]

   (make producer
         topic
         id
         nil))


  ([producer topic id opts]

   (let [id'              (if (void/obtain ::serialize-key?
                                           opts
                                           defaults)
                            (M.serialize/serialize serializer-key
                                                   nil
                                                   id)
                            id)
         serialize-value? (void/obtain ::serialize-value?
                                       opts
                                       defaults)
         pre-msg          (void/assoc-some {:topic topic
                                            :key   id'}
                                           :partition (::partition opts))]
     {:enabled?    true
      :async?      false
      :min-level   nil
      :rate-limit  nil
      :output-fn  :inherit
      :fn         (fn commit-to-kafka [data]
                    (let [value (utimbre/entry data)]
                      (M.produce/commit producer
                                        (merge pre-msg
                                               {:timestamp (::utimbre/timestamp value)
                                                :value     (if serialize-value?
                                                             (M.serialize/serialize serializer-value
                                                                                    nil
                                                                                    value)
                                                             value)})))
                    nil)})))
