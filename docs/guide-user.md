---
layout: global
title: User Guide
---

## 使用方式

Moonbox内置http server和tcp server,支持通过rest api、shell以及jdbc编程方式接入。其中:
- rest api
    用于异步提交batch批量作业、查询作业执行状态、取消作业。每次submit调用都会运行一个新的spark application。
- shell
    用于进行adhoc交互式查询
- jdbc
    用于程序连接进行查询,因此可与zeppelin和davinci对接

#### 1 使用命令行进行交互式查询
```shell
cd $MOONBOX_HOME/bin
./moonbox-shell -u org@user -p password [-h localhost -P 10010 -r local]
```
```sql
-- 查看可用指令
cmds
-- 查看当前连接状态
state
-- 清空屏幕
clear
-- 设置返回数据条数
%set max_rows = 100
-- 其他更多命令请参阅后续内容
```
其中:

- -u 必填, 用户名,格式为org@user形式
- -p 必填, 密码
- -h MoonboxMaster地址, 如客户端机器属于集群, 则不用填写
- -P Moonbox Tcp Server 服务端口，为`moonbox-defaults.conf`中tcp部分配置的端口号，如没有修改则默认为10010。 如客户端机器属于集群, 则不用填写
- -r 可选, 如值为local则表示连接Spark Local APP，如没有配置分布式的app，即`moonbox-defaults.conf`中cluster部分，则必须加上-r local


#### 2 使用rest api运行批量作业
moonbox master节点启动了http server，提供rest调用服务。端口号为`moonbox-defaults.conf`中rest部分配置的端口号，如果没有修改则默认为9090。
- 提交批量作业
    ```shell
    curl -XPOST http://host:port/batch/submit -d '{
        "username" : "",
        "password" : "",
        "config" : {},
        "lang" : "mql"
        "sqls" : ["use default", "insert into ..."]
    }'
    ```
    **备注**：
        1. username格式为org@user形式
        2. config中可以配置spark相关参数
        3. sqls中最后一条SQL必须为insert语句

- 查询作业状态
    ```
    curl -XPOST http://host:port/batch/progress -d '{
        "username" : "",
        "password" : "",
        "jobId" : ""
    }'
    ```
    **备注**：
        1. username格式为org@user形式
        2. jobId为上面submit接口调用后返回的jobId

- 取消作业
    ```
    curl -XPOST http://host:port/batch/cancel -d '{
        "username" : "",
        "password" : "",
        "jobId" : ""
    }'
    ```
    **备注**：
        1. username为org@user形式
        2. jobId为submit接口调用后返回的jobId

#### 3 使用jdbc编程连接moonbox进行计算
Moonbox提供了jdbc驱动，下载地址：[moonbox jdbc驱动下载](https://github.com/edp963/moonbox/releases/tag/0.3.0-beta-SNAPSHOT)。将驱动加入代码依赖，编写jdbc代码。其中url中的端口为moonbox-defaults.conf中tcp部分配置的端口，如果没有修改默认为10010。
Scala示例:
```scala
Class.forName("moonbox.jdbc.MbDriver")
val url = s"jdbc:moonbox://host:port/database"
// if want to connect to local app
// val url = s"jdbc:moonbox://host:port/database?islocal=true"
val username = "org@user"
val password = "password"
val connection = DriverManager.getConnection(url, username, password)
val statement = connection.createStatement()
statement.setQueryTimeout(60*10)
statement.setFetchSize(200)
val rs = statement.executeQuery("select * from mysql_test")
while (rs.next) {
    println(rs.getString(1))
}
```
Java示例：
```java
Class.forName("moonbox.jdbc.MbDriver");
String url = s"jdbc:moonbox://host:port/database";
// if want to connect to local app
// String url = s"jdbc:moonbox://host:port/database?islocal=true";
String username = "org@user";
String password = "password";
Connection connection = DriverManager.getConnection(url, username, password);
Statement statement = connection.createStatement();
statement.setQueryTimeout(60*10);
statement.setFetchSize(200);
ResultSet rs = statement.executeQuery("select * from mysql_test");
while (rs.next) {
    System.out.println(rs.getString(1));
}
```
#### 4 使用Python连接moonbox进行计算
1. 安装jdk
2. 安装python依赖库
```shell
pip install JayDeBeApi
pip uninstall JPype1
pip install JPype1==0.6.3
```
3. 编写程序
```python
import jaydebeapi 

url = 'jdbc:moonbox://hsot:port/database' 
driver = 'moonbox.jdbc.MbDriver' 
# 将version替换为实际使用的版本
jarFile = '/usr/local/moonbox-jdbc_2.11-version.jar' 

conn = jaydebeapi.connect(dirver, url, {'user':"org@user",'password':"password",'maxrows':"10000",'read_timeout':"600"}, jarFile) 
curs = conn.cursor() 
curs.execute("select  1") 
result = curs.fetchall() 
print(result) 
curs.close() 
conn.close()
```
使用pandas：
```python
import jaydebeapi 
import pandas as pd
url = 'jdbc:moonbox://hsot:port/database' 
driver = 'moonbox.jdbc.MbDriver' 
# 将version替换为实际使用的版本
jarFile = '/usr/local/moonbox-jdbc_2.11-version.jar' 

conn = jaydebeapi.connect(dirver, url, {'user':"org@user",'password':"password",'maxrows':"10000",'read_timeout':"600"}, jarFile) 

result = pd.read_sql("select 1", conn)
print(result) 
conn.close()
```
## 注册数据源
以MySQL为例，更多数据源注册请参阅Integration章节DataSource部分。
- 注册单张表
  ```sql
  -- 创建一个逻辑库
  CREATE DATABASE test;
  -- 切换到逻辑库
  USE test;
  MOUNT TABLE mysql_table_test OPTIONS(
      -- mysql数据库连接地址
      url 'jdbc:mysql://host:port/database'
      -- mysql数据库用户
      user 'user',
      -- mysql数据库用户密码
      password 'password',
      -- mysql jdbc 驱动类
      driver 'com.jdbc.mysql.Driver',
      -- 要挂载的mysql 表名
      dbtable 'table'
  )
  ```
  **备注**：
  1. 可选分片参数，手动指定分片信息
    ```sql
    -- 用于分片的列名，为整数类型的列，且分布均匀
    partitionColumn 'id',
    -- 用于分片的列的最小值
    lowerBound '1',
    -- 用于分片的列的最大值
    upperBound '100000',
    -- 数字，表示要分多少片
    numPartitions '10'
    ```
  2. 可选分片参数，自动计算分片上下界信息
   ```sql
    -- 用于分片的列名，为整数类型的列，且分布均匀
    partitionColumn 'id',
    -- 配置自动计算分片上下界信息
    autoComputePartitionBound ‘true’
    -- 数字，表示要分多少片
    numPartitions '10'
    ```
    
- 注册整个库
    ```sql
    MOUNT DATABASE mysql_database_test OPTIONS(
        -- mysql数据库连接地址
      url 'jdbc:mysql://host:port/database'
      -- mysql数据库用户
      user 'user',
      -- mysql数据库用户密码
      password 'password',
      -- mysql jdbc 驱动类
      driver 'com.jdbc.mysql.Driver',
    )
    ```
    **备注**:
    1. 为每张表配置分片信息
        ```sql
        USE mysql_database_test
        ALTER TABLE mysql_table SET OPTIONS(
            -- 参考注册单张表的分片配置
        )
        ```
    2. 与数据源同步
        当数据源新增或者删除了表，可使用以下命令进行同步
        ```sql
        REFRESH DATABASE mysql_database_test
        ```   
## 查询
#### 1. 设置查询参数
```sql
-- 关闭下推优化
set spark.sql.optimize.pushdown = false
-- 设置spark sql运行时shuffle partition参数
set spark.sql.shuffle.partitions = 100
-- 可设置更多其他spark sql运行时参数
```
#### 2. 跨库查询
假如moonbox中有如下数据库和表：
```
database1
    table1
    table2
database2
    table1
    table2    
```
```sql
-- 同库Join, database1库下的table1和table2
use database1;
select * from table1 join table2 on table1.id = table2.id

-- 跨库Join, database1库下的table1和database2库下的table1
select * from database1.table1 t1 join database2.table1 t2 on t1.id = t2.id

-- 切换到database1, 则database1库名可省略
use database1;
select * from table1 t1 join database2.table1 t2 on t1.id = t2.id

-- 切换到database2, 则database2库名可省略
use database2;
select * from database1.table1 t1 join table1 t2 on t1.id = t2.id

```
## 写出数据
```sql
-- 将select子句结果追加到table中
INSERT INTO tablename select ...

-- 先清空table，然后将select子句结果写入到table中
INSERT OVERWRITE TABLE tablename select ...

-- 将字面量数据写入table中
INSERT INTO tablename VALUES (...)
```
## 数据权限控制
moonbox提供了列级别的数据访问权限控制，可以精确控制哪些用户可以查看哪些表的哪些列字段。默认权限控制没有开启。
#### 1. 开启、关闭数据权限控制
使用root账号登录，执行以下指令：
```sql
-- 创建organization的时候开启权限控制
CREATE ORG test_org OPTIONS(spark.sql.permission 'true');

-- 为已存在的organization开启权限控制，即时生效无需重启
ALTER ORG test_org SET OPTIONS(spark.sql.permission 'true');

-- 为已存在的organization关闭权限控制，即时生效无需重启
-- 方式一
ALTER ORG test_org SET OPTIONS(spark.sql.permission 'false');
-- 方式二
ALTER ORG test_org REMOVE OPTIONS(spark.sql.permission);
```
#### 2. 授予、撤销权限
使用sa账号登录，执行以下指令：
```sql
-- 将database1下所有表的读权限授予user1
GRANT SELECT ON database1.* TO user1
-- 将授予给user1的database1下所有表的读权限撤销
REVOKE SELECT ON database1.* FROM user1

-- 将database1.table1的所有字段的读权限授予user1
GRANT SELECT ON database1.table1 TO user1
-- 将授予user1的database1.table1的所有字段的读权限撤销
REVOKE SELECT ON database1.table1 FROM user1

-- 将database1.table1的id, name, age字段的读权限授予user1
GRANT SELECt(id, name, age) ON database1.table1 TO user1
-- 将授予user1的database1.table1的id, name字段的读权限撤销
REVOKE SELECt(id, name) ON database1.table1 TO user1
```

## 定时任务
Moonbox提供了定时作业的功能，用户使用DDL语句定义定时任务，以quartz cron表达式的形式定义调度策略，后台内嵌quartz进行任务定时调度。默认moonbox是没有开启定时调度功能的。
#### 1. 配置开启定时调度功能
- 在`moonbox-defaults.conf`中catalog部分所配置的数据库中，创建一个名字为`moonbox_quartz`的数据库。然后使用数据库客户端运行位于$MOONBOX_HOME/scripts目录下的quartz_tables_mysql.sql文件中的SQL。在刚才创建的`moonbox_quartz`库中创建出所有表。
- 修改`moonbox-defaults.conf`配置文件，在deploy下增加以下配置，然后重启moonbox。
    ```properties
    timer {
        enable = true
        org.quartz.scheduler.instanceName = "TimedEventScheduler"
        org.quartz.threadPool.threadCount = 3
        org.quartz.scheduler.skipUpdateCheck = true
        org.quartz.jobStore.misfireThreshold = 3000
        org.quartz.jobStore.class = "org.quartz.simpl.RAMJobStore"

        org.quartz.scheduler.instanceName = "EventScheduler"
        org.quartz.threadPool.threadCount = 3
        org.quartz.scheduler.skipUpdateCheck = true
        org.quartz.jobStore.misfireThreshold = 3000
        org.quartz.jobStore.class = "org.quartz.impl.jdbcjobstore.JobStoreTX"
        org.quartz.jobStore.driverDelegateClass = "org.quartz.impl.jdbcjobstore.StdJDBCDelegate"
        org.quartz.jobStore.useProperties = false
        org.quartz.jobStore.tablePrefix = "QRTZ_"
        org.quartz.jobStore.dataSource = "quartzDataSource"
        org.quartz.dataSource.quartzDataSource.driver = "com.mysql.jdbc.Driver"
        org.quartz.dataSource.quartzDataSource.URL = "jdbc:mysql://host:port/moonbox_quartz?useUnicode=true&characterEncoding=utf8&connectionCollation=utf8_bin&autoReconnect=true"
        org.quartz.dataSource.quartzDataSource.user = "user"
        org.quartz.dataSource.quartzDataSource.password = "password"
        org.quartz.dataSource.quartzDataSource.maxConnections = 4
    }
    ```
    其中以下部分请按照实际情况修改，为刚才创建的`moonbox_quartz`数据库的连接信息。
    ```
    org.quartz.dataSource.quartzDataSource.driver
    org.quartz.dataSource.quartzDataSource.URL
    org.quartz.dataSource.quartzDataSource.user
    org.quartz.dataSource.quartzDataSource.password
    ```
#### 2. 创建、删除定时任务
- procedure
procedure是一系列SQL语句的封装，类似数据库中的存储过程，表示了作业的计算逻辑，最后一条执行逻辑必须为insert语句。
    ```sql
    -- 创建procedure
    CREATE PROC procname USING MQL AS (USE default; INSERT INTO oracle_external SELECT * FROM mysql_test)
    -- 修改procedure执行逻辑
    ALTER PROC procname AS (USE default; INSERT INTO oracle_external SELECT * FROM mysql_test LIMIT 100)
    -- 删除procedure
    DROP PROC procname
    ```
- event
 event关联了procedure和调度策略，表示什么时间做什么事情。调度表达式为`quartz`表达式，请参阅`quartz`表达式的写法。
    ```sql
    -- 创建event，以什么策略执行哪个procedure
    CREATE EVENT eventname ON SCHEDULE AT '0/50 * * * * ?' DO CALL procname
    -- 修改event执行策略
    ALTER EVENT eventname ON SCHEDULE AT '0/40 * * * * ?'
    -- 删除event
    DROP EVENT eventname
    ``` 
#### 3、开启、关闭定时任务
```sql
-- 开启event
ALTER EVENT eventname ENABLE
-- 关闭event
ALTER EVENT eventname DISABLE
```

## 资源隔离
用户使用moonbox-shell或者jdbc等方式进行交互，当用户登录认证成功时，系统会将用户的session随机绑定到一个正在运行的常驻spark app。这样一个spark app里面就会同时运行着多个organization用户的SQL查询，这样各个Organization的用户的查询就会相互干扰。moonbox提供使用配置标签的方式以Organization为粒度进行资源的隔离。
- 修改`moonbox-defaults.conf`中的cluster或者local部分，为app增加一个配置项
    ```properties
    cluster = [{
        ... # 其他配置
        spark.app.label = "business1"
        },
        {
        ... # 其他配置
        spark.app.label = "business2"
        }
    ]
    ```
- 使用ROOT用户登录，修改Organization参数配置
    ```sql
    -- 设置org1使用标签为business1的app
    ALTER ORG org1 SET OPTIONS(spark.app.label 'business1')
    -- 设置org2使用标签为business2的app
    ALTER ORG org2 SET OPTIONS(spark.app.label 'business2')
    ```

## 命令介绍
#### 1. ROOT用户
ROOT用户作为系统管理员, **仅用作创建管理Organization和Sa(管理员)**。ROOT可以执行的指令有:
```sql
-- 修改自己的用户名
ALTER USER root IDENTIFIED BY newPassword

-- 创建Organization
CREATE ORG orgname
-- 重命名Organization
RENAME ORG orgname TO newname
ALTER ORG orgname RENAME TO newname
-- 删除Organization
DROP ORG orgname

-- 在Organization中创建Sa用户
CREATE SA saname IN ORG orgname
-- 给Sa修改密码
RENAME SA saname IN ORG orgname TO newpassword
ALTER SA saname IN ORG orgname RENAME TO newpassword
-- 删除SA
DROP SA saname IN ORG orgname

-- 列出所有Organization
SHOW ORGS
-- 列出所有Sa
SHOW SAS
```

####2. Sa用户

Basic Concept章节已经介绍过User的六大属性,只要拥有某个属性即有权限执行对应的一类指令。Sa是由ROOT创建,拥有全部权限。以下仅列出Sa的特殊命令,其余命令将合并到普通用户节讲解。
```sql
# 给用户授权, 类似于角色类授权。Moonbox为简单起见,目前以下指令限定只能Sa执行,也即角色类权限不能传递授权。
GRANT GRANT OPTION ACCOUNT, DDL, DCL TO USER username
# 取消用户授权
REVOKE GRANT OPTION ACCOUNT, DDL, DCL FROM USER username
```

####3. Sa用户和普通用户
我们将根据用户拥有的权限来分类进行指令的介绍
- DML

    默认所有的用户都拥有DML权限,可以执行SHOW、DESC、SELECT、INSERT类指令。
    列出用户
    ```sql
    SHOW USERS
    SHOW USERS like '%'
    ```

    列出数据库
    ```sql
    SHOW DATABASES
    SHOW DATABASES like '%'
    ```

    列出表
    ```sql
    SHOW TABLES
    SHOW TABLES IN db
    SHOW TABLES IN db like '%'
    ```

    列出函数
    ```sql
    SHOW FUNTIONS
    SHOW SYSTEM FUNCTIONS like '%'
    SHOW USER FUNCTIONS like '%'
    ```

    列出过程
    ```sql
    SHOW PROCS
    SHOW PROCS like '%'
    ```

    列出定时任务
    ```sql
    SHOW EVENTS
    SHOW EVENTS like '%'
    ```

    展示表的创建方式
    ```sql
    SHOW CREATE TABLE tbname
    ```

    描述SQL语句结果的schema
    ```sql
    SHOW SCHEMA FOR SELECT ...
    ```

    描述数据库信息
    ```sql
    DESC DATABASE dbname
    ```

    描述表结构
    ```sql
    DESC TABLE tbname
    ```

    描述用户信息
    ```sql
    DESC USER username
    ```

    描述函数信息
    ```sql
    DESC FUNTION funcname
    DESC FUNCTION EXTENDED funcname
    ```

    描述定时任务信息
    ```sql
    DESC EVENT eventname
    ```

    切换数据库
    ```sql
    USER dbname
    ```

    查询返回结果
    ```sql
    SELECT ...
    ```

    创建视图
    ```sql
    CREATE TEMP VIEW viewname AS SELECT ...
    CREATE OR REPLACE TEMP VIEW viewname AS SELECT ...
    CREATE CACHE TEMP VIEW viewname AS SELECT ...
    CREATE OR REPLACE CACHE TEMP VIEW viewname AS SELECT ...
    ```

    查询将结果写入到存储
    ```sql
    INSERT INTO/OVERWRITE tbname AS SELECT ...
    ```

- Account

    拥有Account权限的用户,可以执行账号相关指令。
    ```sql
    CREATE USER username IDENTIFIED BY password # 创建用户
    RENAME USER username TO newname # 修改用户名
    ALTER USER username RENAME TO newname # 修改用户名
    ALTER USER username IDENTIFIED BY newpassword
    DROP USER username
    ```
- DDL

    系统初始化之后,Organization中只有一个名为default的TYPE 1数据库。拥有DDL权限的用户可以进行以下一些操作:
    创建、删除TYPE 1 数据库
    ```sql
    CREATE DATABASE dbname
    DROP DATABASE dbname
    ```
    在TYPE 1 数据库中挂载、卸载虚拟表
    ```sql
    USE dbname
    MOUNT TABLE tbname OPTIONS(key 'value', key 'value')
    RENAME TABLE tbname TO newname
    ALTER TABLE tbname RENAME TO newname
    ALTER TABLE tbname SET OPTIONS(key 'newvalue')
    UNMOUT TABLE tbname
    ```
    挂载、卸载TYPE 2 数据库
    ```sql
    MOUNT DATABASE dbname OPTIONS(key 'value', key 'value')
    RENAME DATABASE dbname TO newname
    ALTER DATABASE dbname RENAME TO newname
    ALTER DATABASE dbname SET OPTIONS(key 'newvalue')
    UNMOUNT DATABASE dbname
    ```
    创建、修改、删除procedure,procedure为一系列SQL的封装,主要用于定时任务。
    ```sql
    CREATE PROC procname USING MQL AS (USE default; INSERT INTO oracle_external AS SELECT * FROM mysql_test)
    RENAME PROC procname TO newname
    ALTER PROC procname RENAME TO newname
    ALTER PROC procname AS (USE default; INSERT INTO oracle_external AS SELECT * FROM mysql_test LIMIT 100)
    DROP PROC procname
    ```
    创建、修改、开启、停止、删除定时任务,event需要和一个procedure进行关联,执行的即为procedure。调度表达式为**quartz cron**格式。
    ```sql
    CREATE EVENT eventname ON SCHEDULE AT '0/50 * * * * ?' DO CALL procname
    RENAME EVENT eventname TO newname
    ALTER EVENT eventname RENAME TO newname
    ALTER EVENT eventname ON SCHEDULE AT '0/40 * * * * ?'
    ALTER EVENT eventname ENABLE
    ALTER EVENT eventname DISABLE
    DROP EVENT eventname
    ```
    创建、删除function。Moonbox除了支持jar形式的UDF,还支持在线源代码的形式,包括Java和Scala。也可以将多个函数写在一个类中,但是在注册的时候需要指定函数名。
    ```sql
    # 使用Scala源代码创建function
    CREATE FUNCTION myMutiply AS 'PersonData' 'mutiply' USING scala '(
        class PersonData {
            val field = 16
            def mutiply(i: Int): Int = i * field
        }
    )'
    # 使用jar包创建function,多个函数写在一个类中
    CREATE FUNCTION myMutiply AS 'zoo.Panda' 'multiply' USING jar 'hdfs://host:8020/tmp/tortoise-1.0-SNAPSHOT.jar'
    # 使用jar包创建function,集成Scala FunctionN接口或者Java UDFN接口
    CREATE FUNCTION myMutiply AS 'zoo.Single' USING jar 'hdfs://host:8020/tmp/single-1.0-SNAPSHOT.jar'
    DROP FUNCITON funcname

    # 使用自定义的函数
    SELECT myMutiply(1)
    ```
- DCL

    拥有DCL权限的用户,可以执行将某些资源授权给用户访问的指令。
    ```sql
    GRANT SELECT ON dbname.* TO USER username
    REVOKE SELECT ON dbname.* FROM USER username
    GRANT SELECT ON dbname.tbname TO USER username
    REVOKE SELECT ON dbname.tbname FROM USER username
    GRANT SELECT(col1,col2...) ON dbname.tbname TO USER username
    REVOKE SELECT(col1) ON dbname.tbname FROM USER username
    GRANT UPDATE ON dbname.* TO USER username
    REVOKE UPDATE ON dbname.* FROM USER username
    GRANT UPDATE ON dbname.tbname TO USER username
    REVOKE UPDATE ON dbname.tbname FROM USER username
    GRANT UPDATE(col1,col2...) ON dbname.tbname TO USER username
    REVOKE UPDATE(col1) ON dbname.tbname FROM USER username
    ```

- GrantAccount

    拥有GrantAccount权限的用户,可以执行将Account权限授予其他用户的指令。
    ```sql
    GRANT ACCOUNT TO USER username
    REVOKE ACCOUNT FROM USER username
    ```

- GrantDDL

    拥有GrantDDL权限的用户,可以执行将DDL权限授予其他用户的指令。
    ```sql
    GRANT DDL TO USER username
    REVOKE DDL FROM USER username
    ```

- GrantDCL

    拥有GrantDCL权限的用户,可以执行将DCL权限授予其他用户的指令。
    ```sql
    GRANT DCL TO USER username
    REVOKE DCL FROM USER username
    ```
    以上属性看起来很复杂,可以把ACCOUNT、DDL、DCL理解为一阶权力,GrantAccount、GrantDDL、GrantDCL为二阶权力,二阶权力掌管一阶权力的授予和撤销。SA掌管二阶权力的授予和撤销。理论上通过属性的自由组合可以根据需求构建出"集权"和"三权分立"的用户体系。
