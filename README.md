# Moonbox - DVtaaS (Data Virtualization as a Service) Solution
[![](https://camo.githubusercontent.com/8cb994f6c4a156c623fe057fccd7fb7d7d2e8c9b/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f6c6963656e73652d417061636865253230322d3445423142412e737667)](https://www.apache.org/licenses/LICENSE-2.0.html)

## Document
[![](https://img.shields.io/badge/README-%E4%B8%AD%E6%96%87%E7%89%88-blue.svg)](https://github.com/edp963/moonbox/blob/master/README-CH.md)
[![](https://img.shields.io/badge/%E7%94%A8%E6%88%B7%E6%89%8B%E5%86%8C-%E4%B8%AD%E6%96%87%E7%89%88-blue.svg)](https://edp963.github.io/moonbox/)

## Introduction
Moonbox, designed based on the concept "Data Virtualization", is aimed at offering batch and interactive computing services. Moonbox hides the details and complexities of accessing data from the underlying data sources. Users can implement hybrid computation across disparate data systems and write out with SQL. In addition, Moonbox provides basic services like data service, data management, data tools, data development, etc., and it can make data application architecture and practice of logical data warehouse much more agile and flexible.

## Features
* Multi-tenant Supported  
Moonbox establishes a complete user architecture and introduces the concept of Organization for user space division. System Administrator can use ROOT account to create more than one Organizations and assign SA (super admin) (one or more) to these Organizations. SA creates and manages User. Moonbox abstracts 6 functionalities for User: whether it can execute Account statement, whether it can execute DDL statement, whether it can execute DCL statement, whether it can authorize other users to execute Account statement, whether it can authorize other users to execute DDL statement, and whether it can authorize other users to execute DCL statement. Free combinations of those functionalities build various user architecture models meeting multiple demands and implement multi-tenant.

* Hybrid Calculation across Multiple Data Sources  
Taking Apache Spark as calculation engine, Moonbox supports hybrid calculation across multiple data sources, such as MySQL, Oracle, Hive, Kudu, HDFS, MongoDB, etc., and it also supports custom extension for more data sources.

* Unified SQL Supported  
Spark SQL is the standard query language of Moonbox. With Spark SQL, specific DDL and DCL are expanded, including creating, deleting and authorizing users, access authorization for data table and data column, mount/umount of physical data source/table, creating or deleting logical database/time-scheduling event and udf/udaf, etc..

* Optimization Strategy Supported  
Moonbox supports hybrid calculation based on Apache Spark, and Spark SQL supports multiple data sources. However, Spark SQL fails to utilize the calculation feature of data sources while pulling data, only focusing on the pushdown of project and filter (operators). 
Moonbox optimizes LogicalPlan that has been optimized by Spark Optimizer, splits subtree which can be pushed to data source, figures out the Data Source Query Language as the mapping of the subtree, and pulls the results back to Spark for further calculation. 
If the whole LogicalPlan can be pushed to data source, Moonbox will directly run the query statement (mapping of LogicalPlan) with data source, so as to reduce the cost of distributed obligation and save computing resource.

* Column Permissions Control  
Moonbox defines DCL to implement column permission control. System Administrator authorizes data tables or columns to user with DCL, and Moonbox saves the permission relationship between user and tables/columns into catalog. While user executes SQL query,  Moonbox will intercept the SQL and analyze whether it contains unauthorized tables/columns. If it does, Moonbox will report errors to users.

* Diversified UDF/UDAF  
Moonbox supports creating UDF/UDAF not only with JAR files, but also with Source Code, including Java and Scala, making the development and verification of UDF more convenient.

* Time-Scheduling Event Supported  
Moonbox provides time-scheduling event function. User defines time-scheduling event with DDL, defines scheduling strategy with crontab expression, and embeds quartz in the backend for time-scheduling event.    

# Latest Release
Please download the latest [RELEASE](https://github.com/edp963/moonbox/releases/tag/0.3.0-beta)

## Get Help
The fastest way to get response from our developers is to send email to our mail list edp_support@groups.163.com, and welcome to join our WeChat group for online discussion.

[![](https://github.com/edp963/edp-resource/raw/master/WeChat.jpg)](https://github.com/edp963/edp-resource/raw/master/WeChat.jpg)

## License
Please refer to [LICENSE](https://github.com/edp963/moonbox/blob/master/LICENSE) file.


