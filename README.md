# Timbre appender for Apache Kafka

Some code to make dreams come true.

## Usage

Read the [API](https://dvlopt.github.io/doc/utimbre.appenders.kafka/index.html)

Everything is spec'ed and checked with clojure.spec.

In short :

```clj
(require 'dvlopt.utimbre.appenders.kafka)


(def appender
     (dvlopt.utimbre.appenders.kafka/make kafka-producer
                                          "my-topic"
                                          "program-id"))
```

## License

Copyright © 2018 Adam Helinski

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
