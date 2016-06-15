# Dasshy

[![Build Status](https://travis-ci.org/KromatikSolutions/dasshy.svg?branch=master)](https://travis-ci.org/KromatikSolutions/dasshy)    [![Coverage Status](https://coveralls.io/repos/github/KromatikSolutions/dasshy/badge.svg?branch=master)](https://coveralls.io/github/KromatikSolutions/dasshy?branch=master) [![License (GPL version 3)](https://img.shields.io/badge/license-AGPL%20v3-blue.svg)](COPYING)

Dasshy enables creating policies for real-time stream processing as well as creating on-demand analytic queries with miliseconds execution time.
It tries to unify the streaming and batch processing.

## Key technologies
- [Spark & Spark Streaming & SparkSQL] (http://spark.apache.org/) - We use the latest spark 2.0.0 snapshot and its java API
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
| [Dasshy Server](#dasshy-server) | A Spark 2.0 application providing a REST API for creating policies. |
| [Dasshy Model](#dasshy-model) | Thrift model for the REST API  |
| [Dasshy Web](#dasshy-web) | Angular 2.0 Web application for managing policies and creating custom dashboards. |
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
- transformer: TStage - defines how to transform the data that was previously extracted.
- loader: TStage - defines how to load the data into a given sink and return the result of the policy execution(job).
- state: TJobState - state of the policy execution(job)

Each stage of the policy execution (extractor, transformer, loader) defines a stage implementation and passed its configuration parameters.

## Dasshy Web
Web application for managing policies, built using the latest [Angular 2.0](https://angular.io/) framework. The web application allows for creating policies as well as creating custom dashboards for displaying the result of the policy execution(job)

This module is WIP (Work In Progress) and still not open-sourced.

## Dasshy SDK

The Dasshy SDK allows defining custom stage plugins for the policy execution (extractor, transformer, loaders).

#### Extractor:

```java
public interface Extractor extends IStage
{
	/**
	 * Calculates the extracted data set and returns data sets grouped by names.
	 * The name of the data set can be used for registering sql tables per dataset
	 *
	 * @param context runtime context
	 * @param time    time, valid in case of a streaming job
	 * @return extracted data frames by name
	 */
	Map<String, Dataset<Row>> extract(final RuntimeContext context, final Time time);

	/**
	 * Commits the extracted data in case of a streaming job
	 */
	void commit();

	/**
	 * Rollback the extracted data in case of a streaming job
	 */
	void rollback();
}
```

#### Transformer:

```java
public interface Transformer extends IStage
{

	/**
	 * Transforms the input data frame
	 *
	 * @param context runtime context
	 * @param input   input data
	 * @return transformed data
	 */
	Map<String, Dataset<Row>> transform(final RuntimeContext context, final Map<String, Dataset<Row>> input);
}
```

#### Loader

```java
public interface Loader extends IStage
{

	/**
	 * Load the input data and return a result.
	 * This includes querying the input data frame and returning final result.
	 * Optionally, the output data can be loaded into a persistent storage
	 *
	 * @param context runtime context
	 * @param input   input data frames
	 * @return the result data
	 */
	Dataset<Row> load(final RuntimeContext context, final Map<String, Dataset<Row>> input);
}
```
#### Attribute Definitions

The stage plugins can accept some attributes. The attribute are defined when constructing the plugin.

```java

public class KafkaExtractor extends AbstractExtractor
{
	public KafkaExtractor()
	{

		// set the attribute definitions for this extractor (name, type, mandatory)

		setAttributeDefinitions(
		    			new StageAttribute(HOST, StageAttribute.Type.STRING, true),
						new StageAttribute(PORT, StageAttribute.Type.INTEGER, true),
						new StageAttribute(TOPIC, StageAttribute.Type.STRING, true),
						new StageAttribute(OFFSET, StageAttribute.Type.STRING, false));
	}
}
```

#### Stage Plugins

Steps for creating your own stage plugin:
- Implement a given stage interface (Exctractor, Transformer or Loader)
- Define the attributes for your stage plugin
- Register your stage plugin in [DefaultStagePlugin](dasshy-server/src/main/java/com/kromatik/dasshy/server/service/DefaultStagePlugin.java) with a unique identifier. Later on we will provide a REST api for registering plugins dynamically.
- Use your stage plugin in a policy. That means specifying the stage plugin identifier and providing values for the attributes defined in that plugin.

Refer to our [Integration Tests](dasshy-server/src/integration-test/java/com/kromatik/dasshy/server/PolicyIT.java) for example.

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
Once zookeeper is available, Dasshy server can be started using:

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
