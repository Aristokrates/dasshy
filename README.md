# Dasshy

[![Build Status](https://travis-ci.org/KromatikSolutions/dasshy.svg?branch=master)](https://travis-ci.org/KromatikSolutions/dasshy)    [![Coverage Status](https://coveralls.io/repos/github/KromatikSolutions/dasshy/badge.svg?branch=master)](https://coveralls.io/github/KromatikSolutions/dasshy?branch=master) [![License (GPL version 3)](https://img.shields.io/badge/license-AGPL%20v3-blue.svg)](COPYING)

Dasshy enables creating policies for real-time stream processing as well as creating on-demand analytic queries with miliseconds execution time.
It tries to unify the streaming and batch processing.

## Key technologies
- [Spark & Spark Streaming & SparkSQL] (http://spark.apache.org/) - We use the latest spark 2.0.0 snapshot release to keep up with the new features & improvements
- [Apache Zookeeper] (https://zookeeper.apache.org/)
- [Apache Kafka] (http://kafka.apache.org/)
- [Netflix Archaius] (https://github.com/Netflix/archaius)
- [Jetty] (http://www.eclipse.org/jetty/)
- [Jersey] (https://jersey.java.net/)
- [Apache Thrift] (https://thrift.apache.org/)
- [Docker] (https://www.docker.com/)

## Documentation

See the [Javadoc](http://kromatiksolutions.github.com/dasshy/javadoc)

See the [DasshyModel](http://kromatiksolutions.github.com/dasshy/model) for the Rest API policy model

See the [Wiki](https://github.com/KromatikSolutions/dasshy/wiki) for full documentation, examples and other information. (Work In Progress)

## Modules

| Name | Description |
| ---- | ----------- |
| [Dasshy Server](#dasshy-server) | A Spark 2.0 application providing a REST API for creating policies |
| [Dasshy Model](#dasshy-model) | Thrift model for the REST API  |
| [Dasshy Web](#dasshy-web) | Angular 2.0 Web application for managing policies and creating custom dashboards. Uses the server API (Work In Progress) |
| [Dasshy SDK](#dasshy-sdk) | A library of interfaces for building custom stages (extractors, transformers, loaders) of a policy |

## Dasshy Server
The server is an Spark application containing the Spark Driver, that can be run on any spark cluster.
Additionally, it exposes an REST api for managing policies both for streaming and batch processing.

## Dasshy Model
Contains the model that is used in the REST api. The model is based on the [Apache Thrift] (https://thrift.apache.org/) framework.
This framework is used as a serialization mechanism between any client and the Dasshy server and it provides scalable cross-language development.

The central part of the dasshy model is the policy: [TPolicy]()

The policy consist of:
- batchClock: TBatchClock - determines the execution cycle of the policy (streaming or batch)
- extractor: TStage - defines how to extract the data from a given source
- transformer: TStage - defines how to transform the data that was previously extracted
- loader: TStage - defines how to load the data into a given sink and return the result of the policy execution(job).
- state: TJobState - state of the policy execution(job)

## Dasshy Web
Web application for managing policies, built using the latest [Angular 2.0](https://angular.io/) framework. The web application allows for creating policies as well as creating custom dashboards for displaying the result of the policy execution(job)

This module is WIP (Work In Progress) and still not open-sourced.

## Dasshy SDK


## Build

To build:

```
$ git clone git@github.com:KromatikSolutions/dasshy.git
$ ./gradlew build
```

## Run

Dasshy server depends on [Apache Zookeeper] (https://zookeeper.apache.org/) for storing the policies.
If you don't have zookeeper already installed we do provide you with a dockerized zookeeper.

Assuming you've already installed docker locally, starting the dockerized zookeeper can be done as:
```
$ docker-compose up
```
Once zookeeper is available, Dasshy server can be started as well:

```
$ ./gradlew runServer
```

Please note that Dasshy server assumes default zookeeper connection string to be: localhost:2181.

If you need to pass different zookeeper connection string use the system property: zookeeper.connect

```
$ ./gradlew -Dzookeeper.connect=[zookeeper host]:[zookeeper [port] runServer
```
For full list of supported configuration properties for Dasshy server refer to: [DasshyProperties](dasshy-server/src/main/java/com/kromatik/dasshy/server/config/DasshyProperties.java)

## Distribution

Dasshy server is distributed as a Spark application built as a single uber jar.
This jar can be deployed on any spark cluster or run locally.

The uber jar can be found at:

```
$ ls dasshy-server/build/libs/dasshy-server-0.0.1-SNAPSHOT.jar
```

To run this uber jar locally:

```
$ java -cp dasshy-server-0.0.1-SNAPSHOT.jar com.kromatik.dasshy.server.DasshyServer
```

## Feedback and Contribution

For bugs, questions and discussions please use the [Github Issues](https://github.com/KromatikSolutions/dasshy/issues).

For contribution, please read [CONTRIBUTING.md](CONTRIBUTING.md) on the process of submitting pull requests.
