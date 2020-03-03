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
    端口绑定失败重试次数
  </td>
</tr>
<tr>
  <td><code>moonbox.worker.timeout</code></td>
  <td>5m</td>
  <td>
    worker失联多久时间将worker标记为DEAD
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
    是否启动rest server
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.rest.port</code></td>
  <td>9090</td>
  <td>
    rest server服务端口
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.rest.request-timeout</code></td>
  <td>60s</td>
  <td>
    rest server请求超时时间
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.rest.idle-timeout</code></td>
  <td>60s</td>
  <td>
    rest server空闲超时时间
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.rest.client.idle-timeout</code></td>
  <td>60s</td>
  <td>
    rest client空闲超时时间
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.tcp..enable</code></td>
  <td>true</td>
  <td>
    是否启动tcp server
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.tcp.port</code></td>
  <td>10010</td>
  <td>
    tcp server服务端口
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
    moonbox rpc 通信方式,目前仅支持akka
  </td>
</tr>
<tr>
  <td><code>moonbox.rpc.akka.loglevel</code></td>
  <td>ERROR</td>
  <td>
    akka 日志级别
  </td>
</tr>
<tr>
  <td><code>moonbox.rpc.akka.actor.provider</code></td>
  <td>akka.cluster.ClusterActorRefProvider</td>
  <td>
    参阅akka 官方文档
  </td>
</tr>
<tr>
  <td><code>moonbox.rpc.akka.actor.debug.autoreceive</code></td>
  <td>off</td>
  <td>
    参阅akka 官方文档
  </td>
</tr>
<tr>
  <td><code>moonbox.rpc.akka.actor.remote.transport</code></td>
  <td>akka.remote.netty.NettyRemoteTransport</td>
  <td>
    参阅akka 官方文档
  </td>
</tr>
<tr>
  <td><code>moonbox.rpc.akka.actor.remote.log-remote-lifecycle-events</code></td>
  <td>off</td>
  <td>
    参阅akka 官方文档
  </td>
</tr>
<tr>
  <td><code>moonbox.rpc.akka.actor.cluster.auto-down-unreachable-after</code></td>
  <td>60s</td>
  <td>
    参阅akka 官方文档
  </td>
</tr>
<tr>
  <td><code>moonbox.rpc.akka.cluster.failure-detector.acceptable-heartbeat-pause</code></td>
  <td>10s</td>
  <td>
    参阅akka 官方文档
  </td>
</tr>
<tr>
  <td><code>moonbox.rpc.akka.cluster.retry-unsuccessful-join-after</code></td>
  <td>3s</td>
  <td>
    参阅akka 官方文档
  </td>
</tr>
<tr>
  <td><code>moonbox.rpc.akka.extensions.0</code></td>
  <td>akka.cluster.client.ClusterClientReceptionist</td>
  <td>
    参阅akka 官方文档
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
    持久化方式,目前支持NONE、zookeeper
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.recovery.zookeeper.url</code></td>
  <td>localhost:2181</td>
  <td>
    zookeeper集群连接地址,多个使用逗号分隔
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.recovery.zookeeper.dir</code></td>
  <td>/moonbox</td>
  <td>
    持久化数据存储目录
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.recovery.zookeeper.retry.times</code></td>
  <td>3</td>
  <td>
    连接zookeeper重试次数
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.recovery.zookeeper.retry.wait</code></td>
  <td>1s</td>
  <td>
    连接zookeeper重试等待时间
  </td>
</tr>
</table>

## Catalog Properties

Catalog目前仅支持关系型数据库,支持mysql、oracle、sqlserver、db2、postgres。配置项如下面所示。
<table class="table">
<tr><th>Property Name</th><th>Default</th><th>Meaning</th></tr>
<tr>
  <td><code>moonbox.deploy.catalog.implementation</code></td>
  <td>无</td>
  <td>
    catalog元数据存储方式
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.catalog.url</code></td>
  <td>无</td>
  <td>
    数据库连接地址, 需要加上?createDatabaseIfNotExist=true参数
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.catalog.user</code></td>
  <td>无</td>
  <td>
    建立数据库连接使用的用户名
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.catalog.password</code></td>
  <td>无</td>
  <td>
    建立数据库连接使用的密码
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.catalog.driver</code></td>
  <td>无</td>
  <td>
    建立数据库连接使用的驱动类名
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.catalog.await-timeout</code></td>
  <td>20s</td>
  <td>
    建立数据库连接超时时间
  </td>
</tr>
</table>

如需修改为其他数据库请根据实际情况进行修改,并将对应的jdbc驱动jar包拷贝到每台机器$MOONBOX_HOME/libs目录,以下给出mysql示例。

<table class="table">
<tr><th>Property Name</th><th>Example</th><th>Meaning</th></tr>
<tr>
  <td><code>moonbox.deploy.catalog.implementation</code></td>
  <td>mysql</td>
  <td>
    catalog元数据存储方式为MySQL
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.catalog.url</code></td>
  <td>jdbc:mysql://host:port/moonbox?createDatabaseIfNotExist=true"</td>
  <td>
    数据库连接地址
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.catalog.user</code></td>
  <td>user</td>
  <td>
    建立数据库连接使用的用户名
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.catalog.password</code></td>
  <td>password</td>
  <td>
    建立数据库连接使用的密码
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.catalog.driver</code></td>
  <td>com.mysql.jdbc.Driver</td>
  <td>
    建立数据库连接使用的驱动类
  </td>
</tr>
</table>

## Timer Properties

Moonbox内部集成了quartz提供定时任务服务,如需使用定时任务功能,请将moonbox.deploy.timer.enable设置为true。
<table class="table">
<tr><th>Property Name</th><th>Default</th><th>Meaning</th></tr>
<tr>
  <td><code>moonbox.deploy.timer.enable</code></td>
  <td>false</td>
  <td>
    是否开启定时任务功能
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.timer.org.quartz.scheduler.instanceName</code></td>
  <td>TimedEventScheduler</td>
  <td>
    quartz实例名字,参阅quartz官方文档
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.timer.org.quartz.threadPool.threadCount</code></td>
  <td>3</td>
  <td>
    quartz线程池线程个数,参阅quartz官方文档
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.timer.org.quartz.scheduler.skipUpdateCheck</code></td>
  <td>true</td>
  <td>
    参阅quartz官方文档
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.timer.org.quartz.jobStore.misfireThreshold</code></td>
  <td>3000</td>
  <td>
    参阅quartz官方文档
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.timer.org.quartz.jobStore.class</code></td>
  <td>org.quartz.simpl.RAMJobStore</td>
  <td>
    quartz job存储方式,参阅quartz官方文档
  </td>
</tr>
</table>

默认配置quartz Job没有进行持久化,如需要配置quartz job进行持久化,请参考以下配置将quartz job持久化到mysql,更多用法请参考quartz官方文档。
需要注意的是,我们需要先手动在mysql中创建一些用于保存quartz元数据的库和表。例如我们先创建一个名为moonbox_quartz的数据库,然后使用mysql客户端运行位于$MOONBOX_HOME/bin目录下的quartz_tables_mysql.sql文件中的sql,在刚才创建的库中创建出所有表。

<table class="table">
<tr><th>Property Name</th><th>Example</th><th>Meaning</th></tr>
<tr>
  <td><code>moonbox.deploy.timer.enable</code></td>
  <td>true</td>
  <td>
    是否开启定时任务功能
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.timer.org.quartz.scheduler.instanceName</code></td>
  <td>TimedEventScheduler</td>
  <td>
    quartz实例名字,参阅quartz官方文档
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.timer.org.quartz.threadPool.threadCount</code></td>
  <td>3</td>
  <td>
    quartz线程池线程个数,参阅quartz官方文档
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.timer.org.quartz.scheduler.skipUpdateCheck</code></td>
  <td>true</td>
  <td>
    参阅quartz官方文档
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.timer.org.quartz.jobStore.misfireThreshold</code></td>
  <td>3000</td>
  <td>
    参阅quartz官方文档
  </td>
</tr>
<tr>
  <td><code>moonbox.deploy.timer.org.quartz.jobStore.class</code></td>
  <td>org.quartz.impl.jdbcjobstore.JobStoreTX</td>
  <td>
    quartz job存储方式,参阅quartz官方文档
  </td>
</tr>
<tr>
	<td><code>moonbox.deploy.timer.org.quartz.jobStore.driverDelegateClass</code></td>
	<td>org.quartz.impl.jdbcjobstore.StdJDBCDelegate</td>
	<td>参阅quartz官方文档</td>
</tr>
<tr>
	<td><code>moonbox.deploy.timer.org.quartz.jobStore.useProperties</code></td>
	<td>false</td>
	<td>参阅quartz官方文档</td>
</tr>
<tr>
	<td><code>moonbox.deploy.timer.org.quartz.jobStore.tablePrefix</code></td>
	<td>QRTZ_</td>
	<td>表名前缀,需要与创建表的sql语句保持一致,参阅quartz官方文档</td>
</tr>
<tr>
	<td><code>moonbox.deploy.timer.org.quartz.jobStore.dataSource</code></td>
	<td>quartzDataSource</td>
	<td>参阅quartz官方文档</td>
</tr>
<tr>
	<td><code>moonbox.deploy.timer.org.quartz.dataSource.quartzDataSource.driver</code></td>
	<td>com.mysql.jdbc.Driver</td>
	<td>参阅quartz官方文档</td>
</tr>
<tr>
	<td><code>moonbox.deploy.timer.org.quartz.dataSource.quartzDataSource.URL</code></td>
	<td>jdbc:mysql://host:port/moonbox_quartz</td>
	<td>参阅quartz官方文档</td>
</tr>
<tr>
	<td><code>moonbox.deploy.timer.org.quartz.dataSource.quartzDataSource.user</code></td>
	<td>user</td>
	<td>参阅quartz官方文档</td>
</tr>
<tr>
	<td><code>moonbox.deploy.timer.org.quartz.dataSource.quartzDataSource.password</code></td>
	<td>password</td>
	<td>参阅quartz官方文档</td>
</tr>
<tr>
	<td><code>moonbox.deploy.timer.org.quartz.dataSource.quartzDataSource.maxConnections</code></td>
	<td>10</td>
	<td>参阅quartz官方文档</td>
</tr>
</table>

## Mixcal Common Properties

<table class="table">
<tr><th>Property Name</th><th>Default</th><th>Meaning</th></tr>
<tr>
  <td><code>moonbox.mixcal.implementation</code></td>
  <td>spark</td>
  <td>
    混算引擎方式,目前仅支持spark
  </td>
</tr>
<tr>
  <td><code>moonbox.mixcal.pushdown.enable</code></td>
  <td>true</td>
  <td>
    是否开启下推功能
  </td>
</tr>
<tr>
  <td><code>moonbox.mixcal.column.permission.enable</code></td>
  <td>false</td>
  <td>
    是否开启列级别权限控制
  </td>
</tr>
<tr>
  <td><code>moonbox.mixcal.spark.sql.cbo.enable</code></td>
  <td>true</td>
  <td>
    是否开启spark sql cost base optimize,参阅spark 官方文档
  </td>
</tr>
<tr>
  <td><code>moonbox.mixcal.spark.sql.constraintPropagation.enabled</code></td>
  <td>false</td>
  <td>
    参阅spark 官方文档
  </td>
</tr>
<tr>
  <td><code>moonbox.mixcal.local</code></td>
  <td>[{}]</td>
  <td>
    值为数组, 元素类型为object。数组元素个数代表每个Worker节点启动多少个Spark Local的常驻作业。{}对象中可设置Spark相关参数
  </td>
</tr>
<tr>
  <td><code>moonbox.mixcal.cluster</code></td>
  <td>无</td>
  <td>
    同上值为数组, 元素类型为object。数组元素个数代表每个Worker节点启动多少个Spark Yarn的常驻作业。{}对象中可设置Spark相关参数
  </td>
</tr>
</table>

更多关于spark的配置请参阅spark官方文档。
