---
layout: global
title: Deploying Yarn Client
---

* This will become a table of contents (this text will be scraped).
{:toc}
#### 环境准备

- JDK 1.8
- Redis 3.x.x
- Yarn集群服务
- 配置执行启动脚本的机器到其他所有机器SSH免密码登录

#### 部署配置

下载 moonbox-0.2.0-dist.tar.gz 包，或者使用如下命令自行编译

```
git clone -b 0.2.0 https://github.com/edp963/moonbox.git
cd moonbox
sh dev/build.sh
```
使用dev/build.sh脚本编译,默认会添加所有数据源支持,具体有哪些数据源请参考Integration DataSource章节。如果想按需添加数据源支持,请使用如下指令编译,其中[]内为可选项。
```
git clone -b 0.2.0 https://github.com/edp963/moonbox.git
cd moonbox
mvn package -DskipTests -Pdist [-Pmysql -Poracle -Pes -Phive]
```

解压moonbox-0.2.0-dist.tar.gz

```
tar -zxvf moonbox-0.2.0-dist.tar.gz
```

解压完成之后目录结构应当如下:
```
moonbox
  - bin # 应用脚本目录
  - conf # 配置文件目录
  - libs # moonbox依赖jar存放目录
  - log  # 日志目录
  - runtime # Spark运行时依赖jar存放目录
  - sbin # 启动停止集群管理脚本目录
```

moonbox的配置分为环境变量、集群拓扑、运行参数三个部分，下面分别解释每个部分各表示什么含义以及如何配置。

- 环境变量

  配置文件为conf/moonbox-env.sh

  ```
  export MOONBOX_SSH_OPTIONS="-p 22" # 用于配置ssh端口，如默认无需修改
  export MOONBOX_HOME=/path/to/installed/dir # 配置moonbox安装目录
  ```

- 集群拓扑

  用于描述集群拓扑，配置文件为$MOONBOX_HOME/conf/nodes。moonbox以master-slave集群模式运行，支持配置多个master用于主备。示例如下，请按照实际情况配置。

  ```
  moonbox.gird.master.1   grid://host1:2551
  moonbox.gird.master.2   grid://host2:2551
  moonbox.gird.worker.1   host3
  moonbox.gird.worker.2   host4
  ```

- 运行参数

  用于配置运行时参数，配置文件为$MOONBOX_HOME/conf/moonbox-defaults.conf。以下为moonbox最简配置，请根据实际情况修改，更多配置请参考Configuration章节。

  ```
  moonbox {
      rest.server {
          port = 8080
      }
      tcp.server {
          port = 10010
      }
      catalog {
      	  implementation = "h2"
      	  url = "jdbc:h2:mem:testdb0;DB_CLOSE_DELAY=-1"
      	  user = "testUser"
      	  password = "testPass"
      	  driver = "org.h2.Driver"
      }
      cache {
      	  implementation = "redis"
      	  servers = "host:port"
      }
      mixcal {
          implementation = "spark"
          spark.loglevel = "INFO"
          spark.app.name = "test1"
          spark.master = "yarn"
          spark.submit.deployMode = "client"
          spark.hadoop.yarn.resourcemanager.hostname = "host"
          spark.hadoop.yarn.resourcemanager.address = "host:8032"
          spark.dfs.permissions.enabled = "false"
          spark.dfs.namenode.acls.enabled = "false"
          spark.yarn.stagingDir = "hdfs://host:8020/tmp"
          spark.yarn.access.namenodes = "hdfs://host:8020"
          spark.yarn.am.memory = "512m"
          spark.yarn.am.cores = 1
          spark.executor.instances = 4
          spark.executor.cores = 4
          spark.executor.memory = "4g"
          pushdown.enable = true
          column.permission.enable = false
      }
  }
  ```

将moonbox文件夹分发到conf/nodes中所配置的所有机器上,其所处目录应当与当前机器$MOONBOX_HOME一致。

#### 启动与停止

我们为用户提供了一键启动与停止集群的脚本,位于$MOONBOX_HOME/sbin目录下。
  - 启动集群
  在任意节点执行
  ```
  cd $MOONBOX_HOME
  sbin/start-all.sh
  ```
  - 停止集群
  在任意节点执行
  ```
  cd $MOONBOX_HOME
  sbin/stop-all.sh
  ```
备注:
- 执行启动和停止脚本的机器需要配置到其他机器的ssh免密码登录。
- 如catalog配置修改为其他数据库,请将对应的驱动jar包拷贝到每台机器的$MOONBOX_HOME/libs目录下。

  ​