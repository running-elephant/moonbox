---
layout: global
title: Deployment
---

* This will become a table of contents (this text will be scraped).
{:toc}
#### 环境准备

- JDK 1.8
- Spark 2.2.0
- Redis 3.x.x
- MySQL
- Zookeeper (非必须，若无则无法进行运行时数据持久化，备master恢复待运行作业丢失)
- 配置执行启动脚本的机器到其他所有机器SSH免密码登录

#### 部署配置

下载 moonbox-0.2.0-dist.tar.gz 包，或者使用如下命令自行编译

```
git clone -b 0.2.0 https://github.com/edp963/moonbox.git
cd moonbox
sh dev/build.sh
```

解压moonbox-0.2.0-dist.tar.gz

```
tar -zxvf moonbox-0.2.0-dist.tar.gz
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
      	implementation = "mysql"
      	url = "jdbc:mysql://host:port/moonbox?createDatabaseIfNotExist=true"
      	user = "root"
      	password = "123456"
      	driver = "com.mysql.jdbc.Driver"
    	}
    	cache {
      	implementation = "redis"
      	servers = "host"
      	port = 6379
      	fetchSize = 200
    	}
    	mixcal {
          implementation = "spark"
      	spark.master = "local[*]"
      	spark.loglevel = "INFO"
      	spark.app.name = "test1"
      	pushdown.enable = true
      	column.permission.enable = false
    	}
  }
  ```

  ​