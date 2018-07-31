---
layout: global
title: User Guide
---

### Quick Start
Moonbox服务启动之后,系统管理人员使用ROOT用户连接Moonbox,输入SQL语句,以分号结束。
```
cd $MOONBOX_HOME/bin
./cli.sh -m rest -u ROOT -p 123456 -h localhost -P 18090
```
其中:
- -m 连接Moonbox的方式, 选项为rest和jdbc
- -u 用户名
- -p 密码,ROOT初始密码为123456
- -h Moonbox rest server服务地址
- -P Moonbox rest server 服务端口

创建Organization,假设名为testOrg
```
CREATE ORG/ORGANIZATION testOrg;
```
在该Organization中创建Sa,假设名为sally
```
CREATE SA sally IN ORG testOrg IDENTIFIED BY 123456;
```
至此,用户空间已经创建完毕,更多命令请参考Indexing部分。

应用管理人员使用Sa用户登录
```
cd $MOONBOX_HOME/bin
./cli.sh -m rest -u sally -p 123456 -h localhost -P 18090
```
当Sa初始登录之后,Sa所属的Organization中只有一个默认的名为default的数据库,可以在该数据库下挂载虚拟表。例如挂载MySQL类型数据库test中的booklist表。
```
MOUNT TABLE mysql_test OPTIONS(
    type 'mysql',
    url 'jdbc:mysql://localhost:3306/test',
    user 'root',
    password 'password',
    driver 'com.mysql.jdbc.Driver',
    dbtable 'booklist'
);
```
当执行挂载表指令的时候,系统会检查连接参数的正确性,所以要确保参数正确。需要注意,在发行包中并没有包含MySQL的jdbc驱动,所以需要自行将对应的驱动包拷贝到$MOONBOX_HOME/libs和$MOONBOX_HOME/runtime中。
列出所有表
```
SHOW TABLES;
```
查看表详情
```
DESCRIBE TABLE mysql_test;
```
对表内容做查询
```
SELECT * FROM mysql_test;
```
Sa还可以创建新用户
```
    CREATE USER username IDENTIFIED BY password; # 创建用户
    GRANT ACCOUNT, DDL, DCL TO USER username; # 给用户授权, ACCOUNT、DDL、DCL按需选择
```
如果不给新创建的用户进行授权,那么该用户就只有DML权限,即只能执行SELECT, SHOW ,DESCRIBE等操作。
至此,Sa即可将新创建的账号分配给用户使用了。以上只是一个简短快速的体验,更多内容请阅读接下来的部分。

### 客户端使用

Moonbox内置http server和tcp server,支持通过rest和jdbc方式接入。

#### 使用命令行
```
cd $MOONBOX_HOME/bin
./cli.sh -m rest -u username -p password -h localhost -P 18090
```
其中:

- -m 连接Moonbox的方式, 选项为rest和jdbc
- -u 用户名
- -p 密码,ROOT初始密码为123456
- -h Moonbox rest server服务地址
- -P Moonbox rest server 服务端口

#### 使用rest
rest方式支持batch和adhoc两种方式。batch是两批作业之间无上下文关联,可能对调度到不同的机器上的runner执行,adhoc是上下文相关的,有session的概念,会调度到同一个runner中执行。其中batch又分为异步和同步模式,adhoc只支持同步。

##### 系统登录
```
# request 示例
curl -XPOST http://host:port/login -d '{
    "username": "sally",
    "password": "123456"
}'
# response 示例,该token内容作为该用户今后登录凭证
{
    "token" : "eyJ0eXAiOiJKV1QiLCJhbGciOiJITUQ1In0.eyJ1c2VybmFtZSI6InNhbGx5Iiwic2VlZCI6IjVhMjNiMDczLWE4NzctNGRmZC1hYjkyLWY2ZGI0YjNmNzVlYyJ9._jIe1cgbc9d9JMW6g6D6KA"
}
```
##### 查询提交
adhoc方式
```
# 开启session,request示例
curl -XPOST http://host:port/openSession -d '{
    "token" : "eyJ0eXAiOiJKV1QiLCJhbGciOiJITUQ1In0.eyJ1c2VybmFtZSI6InNhbGx5Iiwic2VlZCI6IjVhMjNiMDczLWE4NzctNGRmZC1hYjkyLWY2ZGI0YjNmNzVlYyJ9._jIe1cgbc9d9JMW6g6D6KA"
}'
# 开启session,response示例,该sessionId用于session标识
{
  "sessionId" : "3ddec591-16db-4a7c-93a7-c6df54763ad0"
}

# 提交查询,request示例
curl -XPOST http://host:port/query -d '{
    "token" : "eyJ0eXAiOiJKV1QiLCJhbGciOiJITUQ1In0.eyJ1c2VybmFtZSI6InNhbGx5Iiwic2VlZCI6IjI1MzYyNmU0LWNkMzMtNDc1ZC04NTFhLWEzYTMzZTY1NGVhMSJ9.L6t_WRLTuyXuub_i46ZAhA",
    "sessionId" : "3ddec591-16db-4a7c-93a7-c6df54763ad0",
    "sqls":["use default", "show tables"]
}'
# 提交查询,response示例
{
"jobId" : "job-20180730150004-00001",
"data" : [["mysql_test"], ["oracle_test"]]
}

# 关闭session,request示例
curl -XPOST http://host:port/closeSession -d '{
{
    "token" : "eyJ0eXAiOiJKV1QiLCJhbGciOiJITUQ1In0.eyJ1c2VybmFtZSI6InNhbGx5Iiwic2VlZCI6IjI1MzYyNmU0LWNkMzMtNDc1ZC04NTFhLWEzYTMzZTY1NGVhMSJ9.L6t_WRLTuyXuub_i46ZAhA",
    "sessionId" : "3ddec591-16db-4a7c-93a7-c6df54763ad0",
}'
# 关闭session,response示例
{}
```

batch同步方式
```
# 同步提交查询,request示例
curl -XPOST http://host:port/submit -d '{
    "token" : "eyJ0eXAiOiJKV1QiLCJhbGciOiJITUQ1In0.eyJ1c2VybmFtZSI6InNhbGx5Iiwic2VlZCI6IjI1MzYyNmU0LWNkMzMtNDc1ZC04NTFhLWEzYTMzZTY1NGVhMSJ9.L6t_WRLTuyXuub_i46ZAhA",
    "mode" : "sync",
    "sqls":["use default", "show tables"]
}'
# 同步提交查询,response示例
{
"jobId" : "job-20180730150004-00001",
"data" : [["mysql_test"], ["oracle_test"]]
}
```
batch异步方式

异步提交作业,会将select类型作业结果保存到缓存,等待用户查询结果。对于insert类型作业会将结果保存到对应的外部存储。所以使用异步方式提交查询时,请注意create、alter、drop、show或者desc等指令,结果会丢失,因为这类指令的返回结果为direct类型,不会存储到缓存。
```
# 异步提交查询,request示例
curl -XPOST http://host:port/submit -d '{
    "token" : "eyJ0eXAiOiJKV1QiLCJhbGciOiJITUQ1In0.eyJ1c2VybmFtZSI6InNhbGx5Iiwic2VlZCI6IjI1MzYyNmU0LWNkMzMtNDc1ZC04NTFhLWEzYTMzZTY1NGVhMSJ9.L6t_WRLTuyXuub_i46ZAhA",
    "mode" : "async",
    "sqls":["use default", "select * from mysql_test"]
}'
# 异步提交查询,response示例
{
  "jobId" : "job-20180730192553-00000"
}

# 查询异步作业状态, request示例
curl -XPOST http://host:port/progress -d '{
    "token" : "eyJ0eXAiOiJKV1QiLCJhbGciOiJITUQ1In0.eyJ1c2VybmFtZSI6InNhbGx5Iiwic2VlZCI6IjI1MzYyNmU0LWNkMzMtNDc1ZC04NTFhLWEzYTMzZTY1NGVhMSJ9.L6t_WRLTuyXuub_i46ZAhA",
    "jobId" : "job-20180730192553-00000"
}'
# 查询异步作业状态,response示例
{
    "jobId" : "job-20180730192553-00000",
    "status" : "SUCCESS"
}

# 获取异步作业结果,request示例
curl -XPOST http://host:port/result -d '{
    "token" : "eyJ0eXAiOiJKV1QiLCJhbGciOiJITUQ1In0.eyJ1c2VybmFtZSI6InNhbGx5Iiwic2VlZCI6IjI1MzYyNmU0LWNkMzMtNDc1ZC04NTFhLWEzYTMzZTY1NGVhMSJ9.L6t_WRLTuyXuub_i46ZAhA",
    "jobId" : "job-20180730192553-00000",
    "offset" : 0,
    "size" : 2
}'
# 获取异步作业结果,response示例
{
  "jobId" : "job-20180730192553-00000",
  "schema" : "{\"type\":\"struct\",\"fields\":[{\"name\":\"id\",\"type\":\"long\",\"nullable\":true,\"metadata\":{\"name\":\"id\",\"scale\":0}},{\"name\":\"bname\",\"type\":\"string\",\"nullable\":true,\"metadata\":{\"name\":\"bname\",\"scale\":0}},{\"name\":\"male\",\"type\":\"boolean\",\"nullable\":true,\"metadata\":{\"name\":\"male\",\"scale\":0}},{\"name\":\"outer_key\",\"type\":\"long\",\"nullable\":true,\"metadata\":{\"name\":\"outer_key\",\"scale\":0}},{\"name\":\"china_key\",\"type\":\"string\",\"nullable\":true,\"metadata\":{\"name\":\"china_key\",\"scale\":0}},{\"name\":\"china_name\",\"type\":\"string\",\"nullable\":true,\"metadata\":{\"name\":\"china_name\",\"scale\":0}}]}",
  "data" : [ [ 1, "name1", false, 1, "xx科技", "刘明" ], [ 8, "bbbb", true, 2, "xx科技", "李军" ]]
}

# 取消正在运行的作业, request示例
curl -XPOST http://host:port/cancel -d '{
    "token" : "eyJ0eXAiOiJKV1QiLCJhbGciOiJITUQ1In0.eyJ1c2VybmFtZSI6InNhbGx5Iiwic2VlZCI6IjI1MzYyNmU0LWNkMzMtNDc1ZC04NTFhLWEzYTMzZTY1NGVhMSJ9.L6t_WRLTuyXuub_i46ZAhA",
    "jobId" : "job-20180730192553-00000"
}'

# 取消正在运行的作业, response示例
{
    "jobId" : "job-20180730203509-00010"
}
```

##### 系统登出

```
# 系统登出,request示例
curl -XPOST http://host:port/logout -d '{
    "token" : "eyJ0eXAiOiJKV1QiLCJhbGciOiJITUQ1In0.eyJ1c2VybmFtZSI6InNhbGx5Iiwic2VlZCI6IjI1MzYyNmU0LWNkMzMtNDc1ZC04NTFhLWEzYTMzZTY1NGVhMSJ9.L6t_WRLTuyXuub_i46ZAhA",
}'
# 系统登出,response示例
{
    "message" : "Logout successfully."
}
```

#### 使用jdbc编程
Moonbox提供了jdbc驱动,请自行下载。以下为Scala示例:
```
Class.forName("moonbox.jdbc.MbDriver")
val url = s"jdbc:moonbox://host:port/database"
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

### 使用命令

#### ROOT用户
ROOT用户作为系统管理员,推荐仅用作创建管理Organization和Sa。ROOT可以执行的指令有:
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
    ```
    SHOW DATABASES
    SHOW TABLES
    SHOW USERS
    SHOW FUNTIONS
    SHOW APPLICATIONS
    SHOW EVENTS
    DESC DATABASE dbname
    DESC TABLE tbname
    DESC USER username
    DESC FUNTION funcname
    DESC APPLICATION appname
    DESC EVENT eventname
    USER dbname
    SELECT ...
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
    创建Application
    创建定时器
    创建function
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



