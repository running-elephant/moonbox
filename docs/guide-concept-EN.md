---
layout: global
title: Basic Concept
---

This section describes some basic concepts involved in Moonbox, which are conducive to understanding the design philosophy of Moonbox and better utilizing it.


#### Data Virtualization

Data virtualization is a technology that offers unified interactive mode and query language to user or user program. With data virtualization, you can query data across many systems without having to know the physical databases that the data is really in, just like operating a single database. Actually, it is a virtual database, which doesn't store data.

![](https://raw.githubusercontent.com/edp963/moonbox/master/docs/img/data-virtualization.jpg)

From "Designing a Modern Data Warehouse + Data Lake" - Melissa Coates, Solution Architect, BlueGranite



Generally speaking, data virtualization tools possess the following capabilities:  
- Transparent Access

    It can access data from different data sources with unified mode and language regardless of location and features of data sources.  
- Various Sources Suppported

    It abstracts the access mode and query language of data sources, supporting various kinds of data sources and extending easily.  
- Distributed Computing 

    It supports the adoption of distributed computing for a quick hybrid computation across different data sources.
- Metadata Acquisition  

    It can acquire metadata from underlying data sources with unified mode.  
- Data Security

    Data security includes login authentication, authorized access, data encryption, audit, etc..   

You are expected to regard Moonbox as the Data Virtualization Layer in the above picture, which can provide the mentioned capabilities. Now let's take a look at some related concepts to Moonbox.  


#### Data Source

Storage systems like HDFS, storage systems with computing capabilities like MySQL and Elasticsearch and calculation engine like Presto, all of them can be data sources of Moonbox.  


#### Data Mapping  

   You can create mapping relationships between virtual tables and data sources in a data mapping layer to ensure that  data virtualization platform delivers accurate data to consumers. Meanwhile, data mapping layer masks the complexities of the underlying data sources.
   
   ![](https://raw.githubusercontent.com/edp963/moonbox/master/docs/img/guide-concept-datamapping-en.png)

Just like a database, Moonbox also provides two level namespaces, namely database and table. For Moonbox database, two organization modes exist, TYPE 1 and TYPE 2.

- TYPE 1

    Moonbox tables in Moonbox database are part source tables from different source databases. Similar to the organization mode of Spark SQL database, it's appropriate for TYPE 1 to put source tables scattered in different source databases into a Moonbox database according to business needs. 
- TYPE 2

    Moonbox tables in Moonbox database are all the source tables from one source database. Similar to the organization mode of Presto schema, TYPE 2 could map source database to Moonbox database.  

Moonbox view, subject to Moonbox database, is similar to the "logical view" in relational database. Moonbox view is created through SQL based on Moonbox tables, and nested view can be created based on Moonbox view. 


#### Multi-tenant System

The concept of "Organization" is introduced into Moonbox for user space division. Three roles exist in Moonbox, namely ROOT (System Adminiatrator), SA (Super Admin) and User.  

- ROOT

    As a built-in user, ROOT doesn't belong to any Organizations. It is used to create Organization or SA in an Organization.  
- SA  

    SA is created by ROOT and subject to Organization, in which one or more SAs exist. SA is responsible for managing all the resources in the Organization it belongs to, creating Users and granting appropriate permissions to Users.  
- User

    User is created by SA or other User with creating permission and belongs to the same Organization with the one that creates it. Moonbox abstracts 6 functionalities for User: SA owns all the functionalities; different User roles could be created through free collocation of those functionalities.  

![](https://raw.githubusercontent.com/edp963/moonbox/master/docs/img/guide-concept-users.png)

  - Account

    Whether it can execute Account statement, such as creating/deleting User.  
  - DDL

    Whether it can execute DDL statement, such as creating/deleting database, mount/unmount Moonbox tables.  
  - DCL

    Whether it can execute DCL statement, such as the authorization of a database, a table or some fields in a table to some Users.  

  - GrantAccount

    Whether it can authorize other users to execute Account statement.  
  - GrantDDL

    Whether it can authorize other users to execute DDL statement.  
  - GrantDCL

    Whether it can authorize other users to execute DCL statement.  


#### Permission System  

Moonbox can also authorize User to access data in Moonbox tables.

- insert

    Write permission control at table level. Authorizing data access at database level means authorizing data access to all tables in this database.  

- select

    Read permission control at column level. Authorizing data access at database level means authorizing data access to all columns in all the tables in this database; authorizing data access at table level means authorizing data access to all columns in this table.  

- update (doing)

    Update permission control at column level. Authorizing data access at database level means authorizing data access to all columns in all the tables in this database; authorizing data access at table level means authorizing data access to all columns in this table.  

- truncate (doing)

    Truncate table permission control at table level. Authorizing data access at database level means authorizing data access to all tables in this database.  
- delete (doing)

    Deleting permission control at table level. Authorizing data access at database level means authorizing data access to all tables in this database.

In addition, for Moonbox, database-level, table-level and column-level authorization relations are stored in three authorization tables. When the authorization relations are queried, database-level query is prior to table-level query, and table-level query is prior to column-level query. The authorization relation of each level is independent of others.


#### Distributed Computing

Spark is adopted here as distributed computing engine of Moonbox. You can refer to the Spark Website for more information.


#### Pushdown and Optimization

SQL pushdown is a common query optimization method used in data virtualization tools. It supports pushdown of operators to data source for execution, so as to reduce the data volume in data virtualization tools. Spark SQL supports the pushdown of Project and Filter to data source, but it neglects the features of some data sources, for instance, data source with computing ability can support more operators. According to different features of data sources, Moonbox can parse SQL and push down more appropriate operators to data sources.  
