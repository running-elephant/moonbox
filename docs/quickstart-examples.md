---
layout: global
title: Examples
---

#### 系统初始化
step 1: 使用ROOT账号登录
```shell
bin/moonbox-shell -u ROOT -p 123456 -r local
```
其中, -r local为可选, 表示是否连接到Spark Local的app上。如果不加则连接到 Spark Yarn的app上。如果没有配置分布式的Spark app,则必须指定-r local。

step 2: 创建organization
```sql
create org org_test;
```
step 3: 在organization中创建管理员
```sql
create sa sally in org org_test identified by 123456
```
step 4: 查看当前有哪些organization
```sql
show orgs
```
step 5: 查看有哪些管理员
```sql
show sas
```
step 6: 退出ROOT账号
```sql
exit
```
至此, 系统初始化完成。系统拥有一个名为org_test的命名空间, 该空间下有一个名为sally的管理员和一个名为default的逻辑数据库。


#### 用户使用
- 使用sally登录, 用户名形式为org@user
```shell
bin/moonbox-shell -u org_test@sally -p 123456
```
- 挂载数据源
```sql
mount database mb_mysql options(
    type 'mysql',
    url 'jdbc:mysql://host:port/database',
    user 'user',
    password 'password',
    driver 'com.mysql.jdbc.Driver'
)
```
- 列出所有数据库
```sql
show databases
```
- 列出以m开头的数据库
```sql
show databases like 'm%'
```
- 查看数据库信息
```sql
desc database mb_mysql
```
- 切换数据库
```sql
use mb_mysql
```
- 列出当前库下所有表
```sql
show tables
```
- 列出当前库下以m开头所有表
```sql
show tables like 'm%'
```
- 查看表结构
```sql
desc table_name
```
- 查看表信息
```sql
show create table table_name
```
- 对表进行查询
```sql
select * from table_name
```
- 查看SQL语句结果schema信息
```sql
show schema for select count(*) from table_name
```
- 创建视图
```sql
create view view_name as select count(*) from table_name
```
- 对视图进行查询
```sql
select * from view_name
```
- 查看表的创建信息
```sql
show create table view_name
```
- 查看表结构
```sql
desc view_name
```

目前,我们仅仅只挂载了一个数据源进行简单查询, 跨数据源混合计算以及更多其他操作请参阅User Guide章节。

