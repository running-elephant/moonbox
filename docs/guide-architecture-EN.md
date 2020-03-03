---
layout: global
title: Architecture
---

In this section, the work pattern of Moonbox is presented. 

### Work Pattern

Master-slave cluster mode is used in Moonbox and three roles - Master, Worker and App - exist in the cluster:
- Master

    Master is the main node of the whole Moonbox cluster, including Active Master and Standby Master and supporting Active/Standby switch through Zookeeper.  
- Worker

    When a Worker starts, it firstly registers with the Master. A Worker can start multiple different kinds of Apps and is responsible for the start/stop of these Apps.
- APP

    App is the node that is responsible for processing. There can be various kinds of APP nodes, such as Spark APP, Hive APP or other customized Apps.

![image text](<https://edp963.github.io/moonbox/img/guide-architecture1.png>)

Here we'd like to introduce the work pattern of Spark APP in detail.

Each Spark APP contains a long-running SparkContext, which is initialized as soon as the APP node is enabled. Once a client requests to create a new session, APP will create a Runner that contains a MbSession, which is connected to the above new session. MbSessions are kept so isolated that multi-tenant operations will not influence each other. MbSession is an encapsulation of SparkSession with some additional features, such as multi-tenant system, permission system, pushdown and optimization, etc..

![image text](<https://edp963.github.io/moonbox/img/guide-architecture2.png>)

Take Spark APP as an example: when Client sends connection request to Master,  Master returns a proper APP address back to Client automatically according to the task type of the request and redirects Client to the APP. 

Client sends command to Spark APP: if it is DDL commands, MbSession will manipulate Catalog into changing the metadata; If is QUERY commands, MbSession will control column permission according to the current user's permission and push down calculation for optimization. If all the operators or computational logic in the query could be pushed down, the data source client will be responsible for the query; if only part could be pushed down,  the data source client will be responsible for the push-down part, and Spark for the others.  

