---
layout: global
title: Examples
---

#### System Initialization
Step 1: Log in with "ROOT" account.
```
bin/moonbox-shell -u ROOT -p 123456 -r local
```
In the above command, if you add `-r local`, moonbox-shell will be connected to Spark Local App; if not, moonbox-shell will be connected to Spark Yarn App.

Step 2: Create organization.
```
create org org_test;
```
Step 3: Create SA (super admin) in the organization.
```
create sa sally in org org_test identified by 123456
```
Step 4: Log out "ROOT" account.
```
exit
```
System initialization is finished. The system contains a namespace called org_test, in which there is an SA named "sally" and a logical database named "default".


#### Using Procedures
- Log in with "sally" account.
```
bin/moonbox-shell -u sally -p 123456
```
- Mount data sources.
```
mount database mb_mysql options(
    type 'mysql',
    url 'jdbc:mysql://host:port/database',
    user 'user',
    password 'password',
    driver 'com.mysql.jdbc.Driver'
)
```
- Show all the databases.
```
show databases
```
- Show databases of which the name begins with "m".
```
show databases like 'm%'
```
- View database information.
```
desc database mb_mysql
```
- Switch database.
```
use mb_mysql
```
- Show all tables in this database.
```
show tables
```
- Show all tables of which the name begins with "m" in this database.
```
show tables like 'm%'
```
- View table structure.
```
desc table_name
```
- View table (information).
```
show create table table_name
```
- Query data in the table.
```
select * from table_name
```
- View schema of the result of SQL statement.
```
show schema for select count(*) from table_name
```
- Create view.
```
create view view_name as select count(*) from table_name
```
- Query data in the view.
```
select * from view_name
```
- View table (information).
```
show create table view_name
```
- View table structure.
```
desc view_name
```

We just mount one data source here as an example. Please refer to User Guide for more other operations.
