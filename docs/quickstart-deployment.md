---
layout: global
title: Deploying
---

#### 环境准备

- 已安装Apache Spark 2.2.0

  (此版本仅支持Apache Spark 2.2.0, 其他Spark 版本后续会兼容)
- 已安装MySQL并启动,且开启远程访问
- 各安装节点已经配置ssh免密登录

#### 下载
[moonbox-0.3.0-beta下载](https://github.com/edp963/moonbox/releases/tag/0.3.0-beta)

#### 解压

```
tar -zxvf moonbox-assembly_2.11-0.3.0-beta-dist.tar.gz
```

#### 修改配置文件
配置文件位于conf目录下
- step 1: 修改slaves

    ```
    mv slaves.example slaves
    vim slaves
    ```
    将会看到如下内容:
    ```
    localhost
    ```
    请根据实际情况修改为需要部署worker节点的地址, 每行一个地址

- step 2: 修改moonbox-env.sh
    ```
    mv moonbox-env.sh.example moonbox-env.sh
    chmod u+x moonbox-env.sh
    vim moonbox-env.sh
    ```
    将会看到如下内容:
    ```
    export JAVA_HOME=path/to/installed/dir
    export SPARK_HOME=path/to/installed/dir
    export YARN_CONF_DIR=path/to/yarn/conf/dir
    export MOONBOX_SSH_OPTS="-p 22"
    export MOONBOX_HOME=path/to/installed/dir
    # export MOONBOX_LOCAL_HOSTNAME=localhost
    export MOONBOX_MASTER_HOST=localhost
    export MOONBOX_MASTER_PORT=2551
    ```
    请根据实际情况修改

- step 3: 修改moonbox-defaults.conf
    ```
    mv moonbox-defaults.conf.example moonbox-defaults.conf
    vim moonbox-defaults.conf
    ```
    将会看到以下内容,其中:
    - catalog

      配置元数据存储位置, 必须修改, 请根据实际情况修改
    - rest

      配置rest服务, 按需修改
    - tcp

      配置tcp(jdbc)服务, 按需修改
    - local

      配置Spark Local模式作业, 值为数组, 有多少个元素表示每个Worker节点启动多少个Spark Local模式作业。如不需要可删除。
    - cluster

      配置Spark yarn模式作业, 值为数组, 有多少个元素表示每个Worker节点启动多少个Spark Yarn模式作业。如不需要可删除。
    ```
    moonbox {
        deploy {
            catalog {
                implementation = "mysql"
                url = "jdbc:mysql://host:3306/moonbox?createDatabaseIfNotExist=true"
                user = "root"
                password = "123456"
                driver = "com.mysql.jdbc.Driver"
            }
            rest {
                enable = true
                port = 9099
                request.timeout = "600s"
                idle.timeout= "600s"
            }
            tcp {
                enable = true
                port = 10010
            }
        }
        mixcal {
            pushdown.enable = true
            column.permission.enable = true
            spark.sql.cbo.enabled = true
            spark.sql.constraintPropagation.enabled = false
            spark.sql.catalogImplementation = "in-memory" # do not modify

            local = [{}]
            cluster = [{
              spark.hadoop.yarn.resourcemanager.hostname = "master"
              spark.hadoop.yarn.resourcemanager.address = "master:8032"
              spark.yarn.stagingDir = "hdfs://master:8020/tmp"
              spark.yarn.access.namenodes = "hdfs://master:8020"
              spark.loglevel = "ERROR"
              spark.cores.max = 2
              spark.yarn.am.memory = "512m"
              spark.yarn.am.cores = 1
              spark.executor.instances = 2
              spark.executor.cores = 1
              spark.executor.memory = "2g"
            }]
        }
    }
    ```
    - optional: 如果HDFS 配置了高可用(HA)、或者HDFS 配置了kerberos、或者YARN 配置了高可用(HA)、或者YARN 配置了kerberos

    将cluster元素中相关部分改为以下配置, 请根据实际情况修改。具体值可查阅hdfs配置文件和yarn配置文件。
    ```
    #### HDFS HA ####
    spark.hadoop.fs.defaultFS="hdfs://service_name"
    spark.hadoop.dfs.nameservices="service_name"
    spark.hadoop.dfs.ha.namenodes.service_name="xxx1,xxx2"
    spark.hadoop.dfs.namenode.rpc-address.abdt.xxx1="xxx1_host:8020"
    spark.hadoop.dfs.namenode.rpc-address.abdt.xxx2="xxx2_host:8020"
    spark.hadoop.dfs.client.failover.proxy.provider.abdt="org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider"
    spark.yarn.stagingDir = "hdfs://service_name/tmp"
    ```
    ```
    #### HDFS kerberos ####
    dfs.namenode.kerberos.principal = ""
    dfs.namenode.kerberos.keytab = ""
    ```
    ```
    #### YARN HA ####
    spark.hadoop.yarn.resourcemanager.ha.enabled=true
    spark.hadoop.yarn.resourcemanager.ha.rm-ids="yyy1,yyy2"
    spark.hadoop.yarn.resourcemanager.hostname.rm1="yyy1_host"
    spark.hadoop.yarn.resourcemanager.hostname.rm2="yyy2_host"
    ```
    ```
    #### YARN kerberos ####
    spark.yarn.principal = ""
    spark.yarn.keytab = ""
    ```

#### 分发安装包
   将MySQL Jdbc驱动包放置到libs和runtime目录下, 然后将整个moonbox安装目录拷贝到所有安装节点, 确保位置与主节点位置一致。

#### 启动集群
   在master节点执行
   ```
   sbin/start-all.sh
   ```
#### 停止集群
   在master节点执行
   ```
   sbin/stop-all.sh
   ```

#### 检查集群是否成功启动
   在master节点执行如下命令, 将会看到 MoonboxMaster 进程
   ```
   jps | grep Moonbox
   ````
   在worker节点执行如下命令, 将会看到 MoonboxWorker 进程
   ```
   jps | grep Moonbox
   ```
   在worker节点执行如下命令, 将会看到与配置文件对应个数的 SparkSubmit 进程
   ```
   jps -m | grep Spark
   ```
   使用moonbox-cluster命令查看集群信息
   ```
   bin/moonbox-cluster workers
   bin/moonbox-cluster apps
   ```
   如果检查通过, 则集群启动成功, 即可参阅examples部分开始体验啦。
   如果检查失败, 可通过查看master节点或者worker节点上logs目录下的日志进行问题排查。


