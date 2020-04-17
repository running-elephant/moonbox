/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 */

package moonbox.grid

import moonbox.common.config.ConfigBuilder

package object config {

  val CLUSTER_NAME = ConfigBuilder("moonbox.clusterName")
    .stringConf
    .createWithDefaultString("moonbox")
  val HOSTNAME = ConfigBuilder("moonbox.host")
    .stringConf
    .createWithDefaultString("localhost")

  val JWT_ALGORITHM = ConfigBuilder("moonbox.jwt.algorithm")
    .stringConf
    .createWithDefaultString("HS256")

  val JWT_SECRET = ConfigBuilder("moonbox.jwt.secret")
    .stringConf
    .createWithDefaultString("moonbox_secret")

  val LOGIN_IMPLEMENTATION = ConfigBuilder("moonbox.deploy.login.implementation")
    .stringConf
    .createWithDefault("built-in")

  val LOGIN_TIMEOUT = ConfigBuilder("moonbox.deploy.login.timeout")
    .timeConf
    .createWithDefaultString("60m")
  val LOGIN_SINGLE_CONTROL_ENABLE = ConfigBuilder("moonbox.deploy.login.single.enable")
    .booleanConf
    .createWithDefault(false)

  val LOGIN_LDAP_USER = ConfigBuilder("moonbox.deploy.login.ldap.user")
  val LOGIN_LDAP_PASSWORD = ConfigBuilder("moonbox.deploy.login.ldap.password")
  val LOGIN_LDAP_SERVER = ConfigBuilder("moonbox.deploy.login.ldap.server")
  val LOGIN_LDAP_DC = ConfigBuilder("moonbox.deploy.login.ldap.dc")


  val LOGIN_LDAP_READ_TIMEOUT = ConfigBuilder("moonbox.deploy.login.ldap.read.timeout")
    .timeConf
    .createWithDefaultString("5s")
  val LOGIN_LDAP_CONNECT_TIMEOUT = ConfigBuilder("moonbox.deploy.login.ldap.connect.timeout")
    .timeConf
    .createWithDefaultString("1s")
  val LOGIN_LDAP_CONNECT_POOL = ConfigBuilder("moonbox.deploy.login.ldap.connect.pool")
    .booleanConf
    .createWithDefault(false)

  val SCHEDULER_INITIAL_WAIT = ConfigBuilder("moonbox.scheduler.initial.wait")
    .timeConf
    .createWithDefaultString("5s")
  val SCHEDULER_INTERVAL = ConfigBuilder("moonbox.scheduler.interval")
    .timeConf
    .createWithDefaultString("1s")
  val JOBS_SUCCESS_RETAINED = ConfigBuilder("moonbox.jobs.retained.success")
    .intConf
    .createWithDefault(2000)
  val JOBS_FAILED_RETAINED = ConfigBuilder("moonbox.jobs.retained.failed")
    .intConf
    .createWithDefault(2000)
  val PORT_MAX_RETRIES = ConfigBuilder("moonbox.port.maxRetries")
    .intConf
    .createWithDefault(16)

  val TIMER_SERVICE_ENABLE = ConfigBuilder("moonbox.deploy.timer.enable")
    .booleanConf
    .createWithDefault(false)
  val TIMER_SERVICE_QUARTZ_INSTANCE_NAME = ConfigBuilder("moonbox.deploy.timer.org.quartz.scheduler.instanceName")
    .stringConf
    .createWithDefault("TimedEventScheduler")
  val TIMER_SERVICE_QUARTZ_THREAD_COUNT = ConfigBuilder("moonbox.deploy.timer.org.quartz.threadPool.threadCount")
    .intConf
    .createWithDefault(3)
  val TIMER_SERVICE_QUARTZ_SKIP_UPDATE_CHECK = ConfigBuilder("moonbox.deploy.timer.org.quartz.scheduler.skipUpdateCheck")
    .booleanConf
    .createWithDefault(true)
  val TIMER_SERVICE_QUARTZ_MISFIRE_THRESHOLD = ConfigBuilder("moonbox.deploy.timer.org.quartz.jobStore.misfireThreshold")
    .intConf
    .createWithDefault(3000)
  val TIMER_SERVICE_QUARTZ_JOBSTORE_CLASS = ConfigBuilder("moonbox.deploy.timer.org.quartz.jobStore.class")
    .stringConf
    .createWithDefault("org.quartz.simpl.RAMJobStore")

  val TIMER_SERVICE_QUARTZ_DEFAULT_CONFIG = Map(
    TIMER_SERVICE_QUARTZ_INSTANCE_NAME.key -> TIMER_SERVICE_QUARTZ_INSTANCE_NAME.defaultValueString,
    TIMER_SERVICE_QUARTZ_THREAD_COUNT.key -> TIMER_SERVICE_QUARTZ_THREAD_COUNT.defaultValueString,
    TIMER_SERVICE_QUARTZ_SKIP_UPDATE_CHECK.key -> TIMER_SERVICE_QUARTZ_SKIP_UPDATE_CHECK.defaultValueString,
    TIMER_SERVICE_QUARTZ_JOBSTORE_CLASS.key -> TIMER_SERVICE_QUARTZ_JOBSTORE_CLASS.defaultValueString,
    TIMER_SERVICE_QUARTZ_MISFIRE_THRESHOLD.key -> TIMER_SERVICE_QUARTZ_MISFIRE_THRESHOLD.defaultValueString
  )

  val REST_SERVER_ENABLE = ConfigBuilder("moonbox.deploy.rest.enable")
    .booleanConf
    .createWithDefault(true)
  val REST_SERVER_PORT = ConfigBuilder("moonbox.deploy.rest.port")
    .intConf
    .createWithDefault(9090)
  val REST_SERVER_REQUEST_TIME = ConfigBuilder("moonbox.deploy.rest.request-timeout")
    .timeConf
    .createWithDefaultString("60s")
  val REST_SERVER_IDLE_TIMEOUT = ConfigBuilder("moonbox.deploy.rest.idle-timeout")
    .timeConf
    .createWithDefaultString("60s")
  val REST_CLIENT_IDLE_TIME = ConfigBuilder("moonbox.deploy.rest.client.idle-timeout")
    .timeConf
    .createWithDefaultString("60s")

  val TCP_SERVER_ENABLE = ConfigBuilder("moonbox.deploy.tcp.enable")
    .booleanConf
    .createWithDefault(true)

  val TCP_SERVER_PORT = ConfigBuilder("moonbox.deploy.tcp.port")
    .intConf
    .createWithDefault(10010)

  val ODBC_SERVER_ENABLE = ConfigBuilder("moonbox.deploy.thrift.enable")
    .booleanConf
    .createWithDefault(false)
  val ODBC_SERVER_PORT = ConfigBuilder("moonbox.deploy.thrift.port")
    .intConf
    .createWithDefault(10020)
  val ODBC_SERVER_CLASS = ConfigBuilder("moonbox.deploy.thrift.className")
    .stringConf
    .createWithDefaultString("moonbox.odbc.server.MoonboxODBCServer")

  val RECOVERY_MODE = ConfigBuilder("moonbox.deploy.recovery.implementation")
    .stringConf
    .createWithDefault("NONE")
  val RECOVERY_ZOOKEEPER_URL = ConfigBuilder("moonbox.deploy.recovery.zookeeper.url")
    .stringConf
    .createWithDefault("localhost:2181")
  val RECOVERY_ZOOKEEPER_DIR = ConfigBuilder("moonbox.deploy.recovery.zookeeper.dir")
    .stringConf
    .createWithDefault("/moonbox")
  val RECOVERY_ZOOKEEPER_RETRY_TIMES = ConfigBuilder("moonbox.deploy.recovery.zookeeper.retry.times")
    .intConf
    .createWithDefault(3)
  val RECOVERY_ZOOKEEPER_RETRY_WAIT = ConfigBuilder("moonbox.deploy.recovery.zookeeper.retry.wait")
    .timeConf
    .createWithDefaultString("1s")

  val RPC_IMPLEMENTATION = ConfigBuilder("moonbox.rpc.implementation")
    .stringConf
    .createWithDefaultString("akka")
  val RPC_AKKA_LOGLEVEL = ConfigBuilder("moonbox.rpc.akka.loglevel")
    .stringConf
    .createWithDefaultString("ERROR")
  val RPC_AKKA_ACTOR_PROVIDER = ConfigBuilder("moonbox.rpc.akka.actor.provider")
    .stringConf
    .createWithDefaultString("akka.remote.RemoteActorRefProvider")
  val RPC_AKKA_ACTOR_DEBUG_AUTORECEIVE = ConfigBuilder("moonbox.rpc.akka.actor.debug.autoreceive")
    .stringConf
    .createWithDefaultString("off")
  val RPC_AKKA_REMOTE_TRANSPORT = ConfigBuilder("moonbox.rpc.akka.remote.transport")
    .stringConf
    .createWithDefaultString("akka.remote.netty.NettyRemoteTransport")
  val RPC_AKKA_REMOTE_PORT = ConfigBuilder("moonbox.rpc.akka.port")
    .intConf
    .createWithDefault(2551)
  val RPC_AKKA_REMOTE_LOG_REMOTE_LIFECYCLE_EVENTS = ConfigBuilder("moonbox.rpc.akka.remote.log-remote-lifecycle-events")
    .stringConf
    .createWithDefaultString("off")

  val AKKA_DEFAULT_CONFIG = Map(
    RPC_AKKA_LOGLEVEL.key -> RPC_AKKA_LOGLEVEL.defaultValueString,
    RPC_AKKA_ACTOR_PROVIDER.key -> RPC_AKKA_ACTOR_PROVIDER.defaultValueString,
    RPC_AKKA_ACTOR_DEBUG_AUTORECEIVE.key -> RPC_AKKA_ACTOR_DEBUG_AUTORECEIVE.defaultValueString,
    RPC_AKKA_REMOTE_TRANSPORT.key -> RPC_AKKA_REMOTE_TRANSPORT.defaultValueString,
    RPC_AKKA_REMOTE_LOG_REMOTE_LIFECYCLE_EVENTS.key -> RPC_AKKA_REMOTE_LOG_REMOTE_LIFECYCLE_EVENTS.defaultValueString
  )

  val AKKA_HTTP_DEFAULT_CONFIG = Map(
    "moonbox.rest.akka.http.server.request-timeout" -> REST_SERVER_REQUEST_TIME.defaultValueString,
    "moonbox.rest.akka.http.server.idle-timeout" -> REST_SERVER_IDLE_TIMEOUT.defaultValueString,
    "moonbox.rest.akka.http.client.idle-timeout" -> REST_CLIENT_IDLE_TIME.defaultValueString,
    "moonbox.rest.akka.http.server.remote-address-header" -> "on"
  )


  val LISTENER_BUS_EVENT_QUEUE_SIZE = ConfigBuilder("moonbox.master.listenerbus.eventqueue.size")
    .intConf
    .createWithDefault(10000)

  val WORKER_TIMEOUT = ConfigBuilder("moonbox.worker.timeout")
    .timeConf
    .createWithDefaultString("5m")

  val WORKER_STATEREPORT_INTERVAL = ConfigBuilder("moonbox.worker.stateReport.interval")
    .timeConf
    .createWithDefaultString("30s")

  val DRIVER_STATEREPORT_INTERVAL = ConfigBuilder("moonbox.mixcal.driver.stateReport.interval")
    .timeConf
    .createWithDefaultString("30s")
}
