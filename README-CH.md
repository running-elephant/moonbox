Moonbox
============

[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

Moonbox是一个DVtaaS（Data Virtualization as a Service）平台解决方案。Moonbox基于数据虚拟化设计思想，致力于提供批量计算服务解决方案。Moonbox负责屏蔽底层数据源的物理和使用细节，为用户带来虚拟数据库般使用体验，用户只需通过统一SQL语言，即可透明实现跨异构数据系统混算和写出。此外Moonbox还提供数据服务、数据管理、数据工具、数据开发等基础支持，可支撑更加敏捷和灵活的数据应用架构和逻辑数仓实践。

Feature
============

- **多租户支持**

  Moonbox建立了一套完整的用户体系，引入了Organization的概念，用于划分用户空间。系统管理员ROOT账号可以创建多个Organization，
  并在Organization中指定该Organization的管理者（SA），可以是一个或者多个。
  SA负责创建管理普通用户。Moonbox将普通用户的能力抽象出六大属性，分别是是否可以执行Account管理语句，是否可以执行DDL语句，是否可以执行DCL语句, 是否拥有可以授权其他用户执行Account类语句的能力，是否拥有可以授权其他用户执行DDL语句的能力，是否拥有可以授权其他用户执行DCL语句的能力。通过属性的自由组合，可以构建出满足多种角色，多种需求的用户体系模型，并可借此实现多租户。

- **跨数据源混算**

  Moonbox以Spark为计算引擎,可以支持多种数据源进行混合计算,比如MySQL、Oracle、Hive、Kudu、HDFS、MongoDB,且支持自定义扩展。

- **统一SQL**

  Moonbox将查询语言统一为Spark SQL，使用Spark SQL语法进行查询操作，同时扩展了一套DDL、DCL语句。包括对用户的创建删除和授权，数据表或者数据列的访问授权，挂载卸载物理数据源或者数据表，创建删除逻辑数据库，创建删除udf/udaf，创建删除定时任务等。

- **优化策略**

  Moonbox基于Spark进行混算，Spark SQL是支持多数据源的，但是Spark SQL在从数据源中进行数据拉取的时候只进行了project和filter算子的下推，并没有考虑数据源的算力特性。Moonbox对Spark Optimizer优化后的LogicalPlan作进一步的优化，根据规则拆分出可以进行下推的子树，将子树mapping成数据源查询语言，将下推结果拉回Spark参与进一步的计算。如果LogicalPlan可以整体下推计算，那么Moonbox将不采用Spark进行计算，直接使用数据源客户端运行LogicalPlan mapping出来的查询语句，以减少启动分布式作业的开销，并节省分布式计算资源。

- **列权限控制**

  Moonbox定义了DCL语句来实现数据列级别权限控制。Moonbox管理员通过DCL语句将数据表或者数据列授权给用户，Moonbox会将用户和表以及列的权限关系保存到catalog中。当用户在使用SQL查询时会被拦截，分析出SQL被解析后的LogicalPlan中是否引用了未被授权的表或者列，如果有就报错返回给用户。

- **多种形式的UDF/UDAF**

  Moonbox除了支持以jar包的形式创建UDF/UDAF外，还支持以源代码的形式创建，包括Java语言和Scala语言，这给UDF开发验证带来了便捷性。

- **定时任务**

  Moonbox提供了定时作业的功能，用户使用DDL语句定义定时任务，以crontab表达式的形式定义调度策略，后台内嵌quartz进行任务定时调度。


For more details, see the [WEBSITE](https://edp963.github.io/moonbox).

Documentation
=============
Please refer [WEBSITE](https://edp963.github.io/moonbox).

Latest Release
=============
[LATEST RELEASE](https://github.com/edp963/moonbox/releases/tag/0.3.0-beta)

Get Help
============
The fastest way to get response from our developers is to send email to our mail list <edp_support@groups.163.com>,
and you are also welcome to join our WeChat group for online discussion.

![img-w150](https://github.com/edp963/edp-resource/raw/master/WeChat.jpg)


License
============
Please refer to [LICENSE](https://github.com/edp963/moonbox/blob/master/LICENSE) file.
