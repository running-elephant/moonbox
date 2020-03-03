---
layout: global
title: User Guide
---

### 用户接口

Moonbox内置http server和tcp server,支持通过rest api、shell以及jdbc编程方式接入。其中:
- rest api
    用于异步提交batch批量作业、查询作业执行状态、取消作业
- shell
    用于进行adhoc交互式查询
- jdbc
    用于程序连接进行查询,因此可与zeppelin和davinci对接

#### 使用命令行
```
cd $MOONBOX_HOME/bin
./moonbox-shell -u username -p password [-h localhost -P 18090 -r local]
```
其中:

- -u 必填, 用户名
- -p 必填, 密码
- -h MoonboxMaster地址, 如客户端机器属于集群, 则不用填写
- -P Moonbox Tcp Server 服务端口, 如客户端机器属于集群, 则不用填写
- -r 可选, 如值为local则表示连接Spark Local APP

#### 使用rest api

提交批量作业
```
curl -XPOST http://host:port/batch/submit -d '{
    "username" : "",
    "password" : "",
    "config" : {},
    "lang" : "mql"
    "sqls" : ["use default", "insert into ..."]
}'
```

查询作业状态
```
curl -XPOST http://host:port/batch/progress -d '{
    "username" : "",
    "password" : "",
    "jobId" : ""
}'
```

取消作业状态
```
curl -XPOST http://host:port/batch/cancel -d '{
    "username" : "",
    "password" : "",
    "jobId" : ""
}'
```

#### 使用jdbc编程
Moonbox提供了jdbc驱动,请自行下载。以下为Scala示例:
```
Class.forName("moonbox.jdbc.MbDriver")
val url = s"jdbc:moonbox://host:port/database"
// if want to connect to local app
// val url = s"jdbc:moonbox://host:port/database?islocal=true"
val username = "username"
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

### 命令介绍

#### ROOT用户
ROOT用户作为系统管理员,推荐仅用作创建管理Organization和Sa(管理员)。ROOT可以执行的指令有:
```
# 修改自己的用户名
ALTER USER root IDENTIFIED BY newPassword

# 创建Organization
CREATE ORG orgname
# 重命名Organization
RENAME ORG orgname TO newname
ALTER ORG orgname RENAME TO newname
# 删除Organization
DROP ORG orgname

# 在Organization中创建Sa用户
CREATE SA saname IN ORG orgname
# 给Sa修改密码
RENAME SA saname IN ORG orgname TO newpassword
ALTER SA saname IN ORG orgname RENAME TO newpassword
# 删除SA
DROP SA saname IN ORG orgname
```

#### Sa用户

Basic Concept章节已经介绍过User的六大属性,只要拥有某个属性即有权限执行对应的一类指令。Sa是由ROOT创建,拥有全部权限。以下仅列出Sa的特殊命令,其余命令将合并到普通用户节讲解。
```
# 给用户授权, 类似于角色类授权。Moonbox为简单起见,目前以下指令限定只能Sa执行,也即角色类权限不能传递授权。
GRANT GRANT OPTION ACCOUNT, DDL, DCL TO USER username
# 取消用户授权
REVOKE GRANT OPTION ACCOUNT, DDL, DCL FROM USER username
```

#### Sa用户和普通用户
我们将根据用户拥有的权限来分类进行指令的介绍
- DML

    默认所有的用户都拥有DML权限,可以执行SHOW、DESC、SELECT、INSERT类指令。
    列出用户
    ```
    SHOW USERS
    SHOW USERS like '%'
    ```

    列出数据库
    ```
    SHOW DATABASES
    SHOW DATABASES like '%'
    ```

    列出表
    ```
    SHOW TABLES
    SHOW TABLES IN db
    SHOW TABLES IN db like '%'
    ```

    列出函数
    ```
    SHOW FUNTIONS
    SHOW SYSTEM FUNCTIONS like '%'
    SHOW USER FUNCTIONS like '%'
    ```

    列出过程
    ```
    SHOW PROCS
    SHOW PROCS like '%'
    ```

    列出定时任务
    ```
    SHOW EVENTS
    SHOW EVENTS like '%'
    ```

    展示表的创建方式
    ```
    SHOW CREATE TABLE tbname
    ```

    描述SQL语句结果的schema
    ```
    SHOW SCHEMA FOR SELECT ...
    ```

    描述数据库信息
    ```
    DESC DATABASE dbname
    ```

    描述表结构
    ```
    DESC TABLE tbname
    ```

    描述用户信息
    ```
    DESC USER username
    ```

    描述函数信息
    ```
    DESC FUNTION funcname
    ```

    描述定时任务信息
    ```
    DESC EVENT eventname
    ```

    切换数据库
    ```
    USER dbname
    ```

    查询返回结果
    ```
    SELECT ...
    ```

    创建视图
    ```
    CREATE TEMP VIEW viewname AS SELECT ...
    ```

    查询将结果写入到存储
    ```
    INSERT INTO/OVERWRITE tbname AS SELECT ...
    ```

- Account

    拥有Account权限的用户,可以执行账号相关指令。
    ```
    CREATE USER username IDENTIFIED BY password # 创建用户
    RENAME USER username TO newname # 修改用户名
    ALTER USER username RENAME TO newname # 修改用户名
    ALTER USER username IDENTIFIED BY newpassword
    DROP USER username
    ```
- DDL

    系统初始化之后,Organization中只有一个名为default的TYPE 1数据库。拥有DDL权限的用户可以进行以下一些操作:
    创建、删除TYPE 1 数据库
    ```
    CREATE DATABASE dbname
    DROP DATABASE dbname
    ```
    在TYPE 1 数据库中挂载、卸载虚拟表
    ```
    USE dbname
    MOUNT TABLE tbname OPTIONS(key 'value', key 'value')
    RENAME TABLE tbname TO newname
    ALTER TABLE tbname RENAME TO newname
    ALTER TABLE tbname SET OPTIONS(key 'newvalue')
    UNMOUT TABLE tbname
    ```
    挂载、卸载TYPE 2 数据库
    ```
    MOUNT DATABASE dbname OPTIONS(key 'value', key 'value')
    RENAME DATABASE dbname TO newname
    ALTER DATABASE dbname RENAME TO newname
    ALTER DATABASE dbname SET OPTIONS(key 'newvalue')
    UNMOUNT DATABASE dbname
    ```
    创建、修改、删除procedure,procedure为一系列SQL的封装,主要用于定时任务。
    ```
    CREATE PROC procname USING MQL AS (USE default; INSERT INTO oracle_external AS SELECT * FROM mysql_test)
    RENAME PROC procname TO newname
    ALTER PROC procname RENAME TO newname
    ALTER PROC procname AS (USE default; INSERT INTO oracle_external AS SELECT * FROM mysql_test LIMIT 100)
    DROP PROC procname
    ```
    创建、修改、开启、停止、删除定时任务,event需要和一个procedure进行关联,执行的即为procedure。调度表达式为crontab格式。
    ```
    CREATE EVENT eventname ON SCHEDULE AT '0/50 * * * * ?' DO CALL procname
    RENAME EVENT eventname TO newname
    ALTER EVENT eventname RENAME TO newname
    ALTER EVENT eventname ON SCHEDULE AT '0/40 * * * * ?'
    ALTER EVENT eventname ENABLE
    ALTER EVENT eventname DISABLE
    DROP EVENT eventname
    ```
    创建、删除function。Moonbox除了支持jar形式的UDF,还支持在线源代码的形式,包括Java和Scala。也可以将多个函数写在一个类中,但是在注册的时候需要指定函数名。
    ```
    # 使用Scala源代码创建function
    CREATE FUNCTION funcname AS 'PersonData' 'mutiply1' USING scala '(
        class PersonData {
            val field = 16
            def mutiply1(i: Int): Int = i * field
        }
    )'
    # 使用jar包创建function,多个函数写在一个类中
    CREATE FUNCTION funcname AS 'zoo.Panda' 'multiply' USING jar 'hdfs://host:8020/tmp/tortoise-1.0-SNAPSHOT.jar'
    # 使用jar包创建function,集成Scala FunctionN接口或者Java UDFN接口
    CREATE FUNCTION funcname AS 'zoo.Single' USING jar 'hdfs://host:8020/tmp/single-1.0-SNAPSHOT.jar'
    DROP FUNCITON funcname
    ```
- DCL

    拥有DCL权限的用户,可以执行将某些资源授权给用户访问的指令。
    ```
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
    ```
    GRANT ACCOUNT TO USER username
    REVOKE ACCOUNT FROM USER username
    ```

- GrantDDL

    拥有GrantDDL权限的用户,可以执行将DDL权限授予其他用户的指令。
    ```
    GRANT DDL TO USER username
    REVOKE DDL FROM USER username
    ```

- GrantDCL

    拥有GrantDCL权限的用户,可以执行将DCL权限授予其他用户的指令。
    ```
    GRANT DCL TO USER username
    REVOKE DCL FROM USER username
    ```
    以上属性看起来很复杂,可以把ACCOUNT、DDL、DCL理解为一阶权力,GrantAccount、GrantDDL、GrantDCL为二阶权力,二阶权力掌管一阶权力的授予和撤销。SA掌管二阶权力的授予和撤销。理论上通过属性的自由组合可以根据需求构建出"集权"和"三权分立"的用户体系。
