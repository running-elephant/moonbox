---
layout: global
title: Configuration
---

* This will become a table of contents (this text will be scraped).
{:toc}

## System Properties

<table class="table">
<tr><th>Property Name</th><th>Default</th><th>Meaning</th></tr>
<tr>
  <td><code>moonbox.port.maxReties</code></td>
  <td>16</td>
  <td>
    Maximum number of retries when binding to a port before giving up.   
  </td>
</tr>
<tr>
  <td><code>moonbox.worker.timeout</code></td>
  <td>5m</td>
  <td>
    How long to wait to connect to a worker before marking it as DEAD.  
  </td>
</tr>
</table>

## Service Properties

<table class="table">
<tr><th>Property Name</th><th>Default</th><th>Meaning</th></tr>
<tr>
  <td><code>moonbox.deploy.rest.enable</code></td>
  <td>true</td>
  <td>
    Whether to enable REST server.  
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.rest.port</code></td>
  <td>9090</td>
  <td>
    The port of REST server.  
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.rest.request-timeout</code></td>
  <td>60s</td>
  <td>
    Duration for a REST request operation to wait before timing out.    
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.rest.idle-timeout</code></td>
  <td>60s</td>
  <td>
    Maximum amount of time a REST server can stay idle before timing out.  
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.rest.client.idle-timeout</code></td>
  <td>60s</td>
  <td>
    Maximum amount of time a REST client can stay idle before timing out.   
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.tcp..enable</code></td>
  <td>true</td>
  <td>
    Whether to enable TCP server.  
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.tcp.port</code></td>
  <td>10010</td>
  <td>
    The port of TCP server.  
  </td>
</tr>
</table>

## Rpc Properties

<table class="table">
<tr><th>Property Name</th><th>Default</th><th>Meaning</th></tr>
<tr>
  <td><code>moonbox.rpc.implementation</code></td>
  <td>akka</td>
  <td>
    Communication mode of Moonbox RPC; only akka is supported at present.  
  </td>
</tr>
<tr>
  <td><code>moonbox.rpc.akka.loglevel</code></td>
  <td>ERROR</td>
  <td>
    Log level of akka.  
  </td>
</tr>
<tr>
  <td><code>moonbox.rpc.akka.actor.provider</code></td>
  <td>akka.cluster.ClusterActorRefProvider</td>
  <td>
    Please refer to akka website for details.  
  </td>
</tr>
<tr>
  <td><code>moonbox.rpc.akka.actor.debug.autoreceive</code></td>
  <td>off</td>
  <td>
    Please refer to akka website for details.  
  </td>
</tr>
<tr>
  <td><code>moonbox.rpc.akka.actor.remote.transport</code></td>
  <td>akka.remote.netty.NettyRemoteTransport</td>
  <td>
    Please refer to akka website for details.  
  </td>
</tr>
<tr>
  <td><code>moonbox.rpc.akka.actor.remote.log-remote-lifecycle-events</code></td>
  <td>off</td>
  <td>
    Please refer to akka website for details.  
  </td>
</tr>
<tr>
  <td><code>moonbox.rpc.akka.actor.cluster.auto-down-unreachable-after</code></td>
  <td>60s</td>
  <td>
    Please refer to akka website for details.  
  </td>
</tr>
<tr>
  <td><code>moonbox.rpc.akka.cluster.failure-detector.acceptable-heartbeat-pause</code></td>
  <td>10s</td>
  <td>
    Please refer to akka website for details.  
  </td>
</tr>
<tr>
  <td><code>moonbox.rpc.akka.cluster.retry-unsuccessful-join-after</code></td>
  <td>3s</td>
  <td>
    Please refer to akka website for details.  
  </td>
</tr>
<tr>
  <td><code>moonbox.rpc.akka.extensions.0</code></td>
  <td>akka.cluster.client.ClusterClientReceptionist</td>
  <td>
    Please refer to akka website for details.  
  </td>
</tr>
</table>

## Recovery Properties

<table class="table">
<tr><th>Property Name</th><th>Default</th><th>Meaning</th></tr>
<tr>
  <td><code>moonbox.deploy.recovery.implementation</code></td>
  <td>NONE</td>
  <td>
    If you choose "NONE", it's a unpersisted worker; if you choose "Zookeeper", it's a persisted worker.  
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.recovery.zookeeper.url</code></td>
  <td>localhost:2181</td>
  <td>
    Zookeeper cluster URL; if there are multiple URLs, they should be separated by comma.  
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.recovery.zookeeper.dir</code></td>
  <td>/moonbox</td>
  <td>
    Persisted data storage directory.  
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.recovery.zookeeper.retry.times</code></td>
  <td>3</td>
  <td>
    Number of retries when connecting to Zookeeper before giving up.  
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.recovery.zookeeper.retry.wait</code></td>
  <td>1s</td>
  <td>
    Duration for an Zookeeper connection operation to wait before retrying.  
  </td>
</tr>
</table>

## Catalog Properties

Catalog supports only relational databases at present, such as MySQL, Oracle, SQL Server, DB2 and Postgres. Following are the configuration options:  
<table class="table">
<tr><th>Property Name</th><th>Default</th><th>Meaning</th></tr>
<tr>
  <td><code>moonbox.deploy.catalog.implementation</code></td>
  <td>Not set</td>
  <td>
    Saving mode of catalog metadata  
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.catalog.url</code></td>
  <td>Not set</td>
  <td>
    Database URL, `?createDatabaseIfNotExist=true` should be added.
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.catalog.user</code></td>
  <td>Not set</td>
  <td>
    User name used to create database connection
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.catalog.password</code></td>
  <td>Not set</td>
  <td>
    Password used to create database connection
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.catalog.driver</code></td>
  <td>Not set</td>
  <td>
    Driver class name used to create database connection
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.catalog.await-timeout</code></td>
  <td>20s</td>
  <td>
    Duration for creating database connection to wait before timing out
  </td>
</tr>
</table>

We have listed a general format above. You can use any relational database the catalog supports and make sure to copy corresponding JDBC driver jar to $MOONBOX_HOME/libs directory in each client. Here we'd like to take MySQL as an example.  

<table class="table">
<tr><th>Property Name</th><th>Example</th><th>Meaning</th></tr>
<tr>
  <td><code>moonbox.deploy.catalog.implementation</code></td>
  <td>mysql</td>
  <td>
    Saving mode of catalog metadata is MySQL  
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.catalog.url</code></td>
  <td>jdbc:mysql://host:port/moonbox?createDatabaseIfNotExist=true"</td>
  <td>
    Database URL  
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.catalog.user</code></td>
  <td>user</td>
  <td>
    User name used to create database connection  
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.catalog.password</code></td>
  <td>password</td>
  <td>
    Password used to create database connection  
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.catalog.driver</code></td>
  <td>com.mysql.jdbc.Driver</td>
  <td>
    Driver class used to create database connection  
  </td>
</tr>
</table>

## Timer Properties

Quartz is integrated in Moonbox to provide time-scheduling event service. Please set moonbox.deploy.timer.enable to "TURE" if you need time-scheduling event function.
<table class="table">
<tr><th>Property Name</th><th>Default</th><th>Meaning</th></tr>
<tr>
  <td><code>moonbox.deploy.timer.enable</code></td>
  <td>false</td>
  <td>
    Whether to enable time-scheduling event function.  
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.timer.org.quartz.scheduler.instanceName</code></td>
  <td>TimedEventScheduler</td>
  <td>
    Quartz instance name, please refer to Quartz website for details.  
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.timer.org.quartz.threadPool.threadCount</code></td>
  <td>3</td>
  <td>
    The number of threads in Quartz thread pool, please refer to Quartz website for details.  
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.timer.org.quartz.scheduler.skipUpdateCheck</code></td>
  <td>true</td>
  <td>
    Please refer to Quartz website for details.  
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.timer.org.quartz.jobStore.misfireThreshold</code></td>
  <td>3000</td>
  <td>
    Please refer to Quartz website for details.  
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.timer.org.quartz.jobStore.class</code></td>
  <td>org.quartz.simpl.RAMJobStore</td>
  <td>
    Saving mode for quartz job, please refer to Quartz website for details.  
  </td>
</tr>
</table>

Quartz job is not set to be persisted by fault; if you want set quartz job to be persisted, you can persist quartz job into MySQL according to the following configurations. Please refer to Quartz website for more information.

Note: It is necessary to create some databases and tables by hand in MySQL for persisting quartz metadata. For instance, create a database called moonbox_quartz firstly, use MySQL client to run the SQL statements in `quartz_tables_mysql.sql` file in `$MOONBOX_HOME/bin` directory, and create all tables in the database named moonbox_quartz.

<table class="table">
<tr><th>Property Name</th><th>Example</th><th>Meaning</th></tr>
<tr>
  <td><code>moonbox.deploy.timer.enable</code></td>
  <td>true</td>
  <td>
    Whether to enable time-scheduling event function.  
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.timer.org.quartz.scheduler.instanceName</code></td>
  <td>TimedEventScheduler</td>
  <td>
    Quartz instance name, please refer to Quartz website for details.  
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.timer.org.quartz.threadPool.threadCount</code></td>
  <td>3</td>
  <td>
    The number of threads in Quartz thread pool, please refer to Quartz website for details.  
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.timer.org.quartz.scheduler.skipUpdateCheck</code></td>
  <td>true</td>
  <td>
    Please refer to Quartz website for details.  
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.timer.org.quartz.jobStore.misfireThreshold</code></td>
  <td>3000</td>
  <td>
    Please refer to Quartz website for details.  
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.timer.org.quartz.jobStore.class</code></td>
  <td>org.quartz.impl.jdbcjobstore.JobStoreTX</td>
  <td>
    Saving mode for quartz job, please refer to Quartz website for details.  
  </td>
</tr>
<tr>
	<td><code>moonbox.deploy.timer.org.quartz.jobStore.driverDelegateClass</code></td>
	<td>org.quartz.impl.jdbcjobstore.StdJDBCDelegate</td>
	<td>Please refer to Quartz website for details.</td>
</tr>
<tr>
	<td><code>moonbox.deploy.timer.org.quartz.jobStore.useProperties</code></td>
	<td>false</td>
	<td>Please refer to Quartz website for details.</td>
</tr>
<tr>
	<td><code>moonbox.deploy.timer.org.quartz.jobStore.tablePrefix</code></td>
	<td>QRTZ_</td>
	<td>It stands for table prefix, which should keep consistent with the SQL statements with which the tables are created. Please refer to Quartz website for details.</td>
</tr>
<tr>
	<td><code>moonbox.deploy.timer.org.quartz.jobStore.dataSource</code></td>
	<td>quartzDataSource</td>
	<td>Please refer to Quartz website for details.</td>
</tr>
<tr>
	<td><code>moonbox.deploy.timer.org.quartz.dataSource.quartzDataSource.driver</code></td>
	<td>com.mysql.jdbc.Driver</td>
	<td>Please refer to Quartz website for details.</td>
</tr>
<tr>
	<td><code>moonbox.deploy.timer.org.quartz.dataSource.quartzDataSource.URL</code></td>
	<td>jdbc:mysql://host:port/moonbox_quartz</td>
	<td>Please refer to Quartz website for details.</td>
</tr>
<tr>
	<td><code>moonbox.deploy.timer.org.quartz.dataSource.quartzDataSource.user</code></td>
	<td>user</td>
	<td>Please refer to Quartz website for details.</td>
</tr>
<tr>
	<td><code>moonbox.deploy.timer.org.quartz.dataSource.quartzDataSource.password</code></td>
	<td>password</td>
	<td>Please refer to Quartz website for details.</td>
</tr>
<tr>
	<td><code>moonbox.deploy.timer.org.quartz.dataSource.quartzDataSource.maxConnections</code></td>
	<td>10</td>
	<td>Please refer to Quartz website for details.</td>
</tr>
</table>

## Mixcal Common Properties

<table class="table">
<tr><th>Property Name</th><th>Default</th><th>Meaning</th></tr>
<tr>
  <td><code>moonbox.mixcal.implementation</code></td>
  <td>spark</td>
  <td>
    Hybrid calculation engine, only Spark is supported at present.  
  </td>
</tr>
<tr>
  <td><code>moonbox.mixcal.pushdown.enable</code></td>
  <td>true</td>
  <td>
    Whether to enable pushdown function.  
  </td>
</tr>
<tr>
  <td><code>moonbox.mixcal.column.permission.enable</code></td>
  <td>false</td>
  <td>
    Whether to enable column permission control.  
  </td>
</tr>
<tr>
  <td><code>moonbox.mixcal.spark.sql.cbo.enable</code></td>
  <td>true</td>
  <td>
    Whether to enable spark SQL cost base optimize. Please refer to Spark website for details.  
  </td>
</tr>
<tr>
  <td><code>moonbox.mixcal.spark.sql.constraintPropagation.enabled</code></td>
  <td>false</td>
  <td>
    Please refer to Spark website for details.  
  </td>
</tr>
<tr>
  <td><code>moonbox.mixcal.local</code></td>
  <td>[{}]</td>
  <td>
    The value is an array and the element type "object". The number of array elements stands for how many Spark Local long-running obligations each worker node has enabled. Parameters related to Spark can be set in {}.
  </td>
</tr>
<tr>
  <td><code>moonbox.mixcal.cluster</code></td>
  <td>Not set</td>
  <td>
    The value is an array and the element type "object". The number of array elements stands for how many Spark Yarn long-running obligations each worker node has enabled. Parameters related to Spark can be set in {}.
  </td>
</tr>
</table>

Please refer to Spark website for more information about Spark configuration.
