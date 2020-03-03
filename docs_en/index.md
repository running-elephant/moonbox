---
layout: global
displayTitle: Moonbox Overview
title: Overview
description: Moonbox MOONBOX_VERSION_SHORT documentation homepage
---

**Moonbox is a DVtaaS (Data Virtualization as a Service) Solution**

Moonbox, designed based on the concept "Data Virtualization", is aimed at offering batch computing services. Moonbox hides the details and complexities of accessing data from the underlying data sources. Users can implement hybrid computation across disparate data systems and write out with SQL. In addition, Moonbox provides basic services like data service, data management, data tools, data development, etc., and it can make data application architecture and practice of logical data warehouse much more agile and flexible.



## Philosophy

With the increasing use of big data technology in enterprises and the emergence of much more data systems to solve various scenarios, quick query calculation based on panoramic data has become an urgent need as well as a big challenge. At present, as a mainstream method, establishing data warehouse/data lake with Hadoop as the core solves the problems of hybrid computation and data dispersion to some extent, but maintenance cost and efficiency loss due to data imputation still exist. In addition, developers are faced with challenge of frequently-changing business and need for rapid delivery of results. 

Taking all aspects into consideration, data virtualization and compution servitization can provide good solutions. Therefore, Moonbox, a practice of data virtualization and compution servitization based on Big Data scenario, is brought into being. Following are several aspects of its design philosophy:

- Compution Servitization  
  Moonbox offers multiple query interfaces and timing task, turning computing resource into a service. You can register data sources by yourself and implement requirements with SQL. Business logic is the only thing you need to focus on.  
- Data Virtualization  
  Moonbox supports mapping of virtual table to physical table. You can process data without knowing physical locations of data and features of underlying data sources, just like using a virtual database.  
- Unified Portal  
  - Unified Query Language  
    Hiding the heterogeneity of data sources, Moonbox provides unified SQL grammar to query or compute across disparate data systems.  
  - Unified Metadata Service  
    Moonbox does't store schema; it can be connected to various data systems and acquire schema of data tables in real time, so it's free from metadata updating problems. Moonbox offers unified metadata service interface, so you don't need to invoke multiple interfaces for metadata from different sources.  
  - Unified Permission Control  
    Moonbox provides a unified secure access mechanism in the logical layer, which means you needn't learn different secure mechanisms of each data source: when access data sources, you just need to log in to your Moonbox account; when you query data, Moonbox will intercept and analyze SQL to implement column permission control. In other words, source query through Moonbox makes it much more secure and simple.



## Architecture

<p style="text-align: center;">
  <img src="img/overview-architecture.png" style="width:80%;" title="Moonbox Architecture" alt="Architecture" />
</p>

Generally speaking, Moonbox consists of four parts, namely Client, Access Layer, Grid and Storage/Computation Layer.

- Client  
  Following are several Moonbox Clients:  
  - Rest API  
    You can submit a batch obligation, check for a obligation state or cancel a obligation with Restful API.
  - JDBC  
    Moonbox provides JDBC driver. You can use JDBC to programme and  access data.
  - ODBC  
    Moonbox supports ODBC. You can connect Moonbox to data sources with SAS for data analysis.
  - CLI  
    CLI (command-line interface) is based on Jline. You can implement DDL (Data Definition Language), DML (Data Manipulation Language) and DCL (Data Control Language) commands and Query operations with CLI.  
  - Zeppelin  
    Zeppelin Moonbox Interpreter is available. You can use Zeppelin for quick prototype verification and SQL development.  
  - Davinci  
    Moonbox supports the access of Davinci (Data Visualization as a Service Solution) through JDBC for data query and display.  
- Access Layer  
  Access layer contains HTTP server, TCP server and Thrift server for Client access and login authentication.  
- Grid  
  Master-slave cluster mode is used in Grid and it supports master/slave switch. Three roles - Master, Worker and App - exist in Grid:  
  - Master receives all the user requests and and dispatches the requests to right Apps according to the request mode (adhoc/batch).  
  - When a Worker starts, it firstly registers with the Master and executes tasks. A Worker can start multiple different kinds of Apps and is responsible for the start/stop of these Apps.  
  - App also registers with Master. App is the one that is responsible for processing and computing, which can be a Spark APP or other customized App.    

- Storage/Computation Layer  
  For Moonbox, Spark is the default calculation engine, and a long-running Spark obligation is called App, supporting Standalone mode and Yarn mode. Spark App processes user requests, including user system management, permission management, SQL parsing, pushdown optimization, execution engine selection, etc., and submits computation tasks.  When computational logic can be pushed to data sources, Moonbox will figure out the data source query language as the mapping of the computation task and push the task down, so as to reduce cost for distributed obligation. Storage systems like HDFS, storage systems with computing capabilities like MySQL and Elasticsearch and calculation engine like Presto, all of them can be data sources of Moonbox.
  

## Features

- Multi-tenant Supported  
  Moonbox establishes a complete user architecture and introduces the concept of Organization for user space partition. System Administrator can use ROOT to create more than one Organizations and assign SA (super admin) (one or more) to these Organizations. SA creates and manages User. Moonbox abstracts 6 functionalities for User: whether it can execute Account statement, whether it can execute DDL statement, whether it can execute DCL statement, whether it can authorize other users to execute Account statement, whether it can authorize other users to execute DDL statement, and whether it can authorize other users to execute DCL statement. Free combinations of those functionalities build various user architecture models meeting multiple demands and implement multi-tenant.

- Hybrid Calculation across Multiple Data Sources  
  Taking Apache Spark as calculation engine, Moonbox supports hybrid calculation across multiple data sources, such as MySQL, Oracle, Hive, Kudu, HDFS, MongoDB, etc., and it also supports custom extension for more data sources.

- Unified SQL Supported  
  Spark SQL is the standard query language of Moonbox. With Spark SQL, specific DDL and DCL are expanded, including creating, deleting and authorizing users, access authorization for data table and data column, mount/unmount of physical data source/table, creating or deleting logical database/timing task and udf/udaf, etc..
 
- Optimization Strategy Supported  
  Moonbox supports hybrid calculation based on Apache Spark, and Spark SQL supports multiple data sources. However, Spark SQL fails to utilize the calculation feature of data sources while pulling data, only focusing on the pushdown of project and filter (operators). Moonbox optimizes LogicalPlan that has been optimized by Spark Optimizer, splits subtree which can be pushed to data source, figures out the Data Source Query Language as the mapping of the subtree, and pulls the results back to Spark for further calculation. If the whole LogicalPlan can be pushed to data source, Moonbox will directly run the query statement (mapping of LogicalPlan) with data source, so as to reduce the cost of distributed obligation and save computing resource.

- Column Permissions Control  
  Moonbox defines DCL to implement column permission control. System Administrator authorizes data tables or columns to user with DCL, and Moonbox saves the permission relationship between user and tables/columns into catalog. While user executes SQL query,  Moonbox will intercept the SQL and analyze whether it contains unauthorized tables/columns. If it does, Moonbox will report errors to users.

- Diversified UDF/UDAF  
  Moonbox supports creating UDF/UDAF not only with JAR files, but also with Source Code, including Java and Scala, making the development and verification of UDF more convenient.

- Timing Task Supported  
  Moonbox provides timing task function. User defines timing task with DDL, defines scheduling strategy with crontab expression, and embeds quartz in the backend for task timing scheduling.   


