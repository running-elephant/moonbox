package moonbox.grid

import moonbox.common.config.ConfigBuilder

package object config {

	val CLUSTER_NAME = ConfigBuilder("moonbox.cluster.name")
	    .stringConf
	    .createWithDefaultString("moonbox")

	val JWT_ALGORITHM = ConfigBuilder("moonbox.jwt.algorithm")
		.stringConf
		.createWithDefaultString("HS256")
//	val JWT_TIMEOUT = ConfigBuilder("moonbox.jwt.timeout")
//		.timeConf
//		.createWithDefaultString("300m")
	val JWT_SECRET = ConfigBuilder("moonbox.jwt.secret")
		.stringConf
		.createWithDefaultString("moonbox_secret")

	val LOGIN_IMPLEMENTATION = ConfigBuilder("moonbox.login.implementation")
		.stringConf
		.createWithDefault("built-in")

	val LOGIN_TIMEOUT = ConfigBuilder("moonbox.login.timeout")
	    .timeConf
	    .createWithDefaultString("60m")
	val LOGIN_SINGLE_CONTROL_ENABLE = ConfigBuilder("moonbox.login.single.enable")
	    .booleanConf
	    .createWithDefault(false)

	val LOGIN_LDAP_USER = ConfigBuilder("moonbox.login.ldap.user")
	val LOGIN_LDAP_PASSWORD = ConfigBuilder("moonbox.login.ldap.password")
	val LOGIN_LDAP_SERVER = ConfigBuilder("moonbox.login.ldap.server")
	val LOGIN_LDAP_DC = ConfigBuilder("moonbox.login.ldap.dc")


	val LOGIN_LDAP_READ_TIMEOUT = ConfigBuilder("moonbox.login.ldap.read.timeout")
		.timeConf
		.createWithDefaultString("5s")
	val LOGIN_LDAP_CONNECT_TIMEOUT = ConfigBuilder("moonbox.login.ldap.connect.timeout")
		.timeConf
		.createWithDefaultString("1s")
	val LOGIN_LDAP_CONNECT_POOL = ConfigBuilder("moonbox.login.ldap.connect.pool")
		.booleanConf
		.createWithDefault(false)

	val SCHEDULER_INITIAL_WAIT = ConfigBuilder("moonbox.scheduler.initial.wait")
		.timeConf
		.createWithDefaultString("5s")
	val SCHEDULER_INTERVAL = ConfigBuilder("moonbox.scheduler.interval")
		.timeConf
		.createWithDefaultString("1s")

	val PORT_MAX_RETRIES = ConfigBuilder("moonbox.port.maxRetries")
	    .intConf
	    .createWithDefault(16)

	val REST_SERVER_ENABLE = ConfigBuilder("moonbox.rest.server.enable")
		.booleanConf
		.createWithDefault(true)
	val REST_SERVER_PORT = ConfigBuilder("moonbox.rest.server.port")
	    .intConf
		.createWithDefault(9090)
	val REST_SERVER_REQUEST_TIME = ConfigBuilder("moonbox.rest.server.request-timeout")
	    .timeConf
		.createWithDefaultString("1800s")
	val REST_SERVER_IDLE_TIMEOUT = ConfigBuilder("moonbox.rest.server.idle-timeout")
	    .timeConf
	    .createWithDefaultString("1800s")
	val REST_CLIENT_IDLE_TIME = ConfigBuilder("moonbox.rest.client.idle-timeout")
	    .timeConf
	    .createWithDefaultString("1800s")

	val TCP_SERVER_ENABLE = ConfigBuilder("moonbox.tcp.server.enable")
	    .booleanConf
	    .createWithDefault(true)

	val TCP_SERVER_PORT = ConfigBuilder("moonbox.tcp.server.port")
	    .intConf
	    .createWithDefault(10010)

	val PERSIST_ENABLE = ConfigBuilder("moonbox.persist.enable")
	    .booleanConf.createWithDefault(true)
	val PERSIST_IMPLEMENTATION = ConfigBuilder("moonbox.persist.implementation")
	    .stringConf
	    .createWithDefaultString("NONE")
	val PERSIST_SERVERS = ConfigBuilder("moonbox.persist.servers")
	    .stringConf
	    .createWithDefaultString("localhost:2181")
	val PERSIST_WORKING_DIR = ConfigBuilder("moonbox.persist.dir")
	    .stringConf
	    .createWithDefaultString("/moonbox")
	val PERSIST_RETRY_TIMES = ConfigBuilder("moonbox.persist.retry.times")
	    .intConf
	    .createWithDefault(3)
	val PERSIST_RETRY_WAIT = ConfigBuilder("moonbox.persist.retry.wait")
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
	    .createWithDefaultString("akka.cluster.ClusterActorRefProvider")
	val RPC_AKKA_ACTOR_DEBUG_AUTORECEIVE = ConfigBuilder("moonbox.rpc.akka.actor.debug.autoreceive")
	    .stringConf
	    .createWithDefaultString("off")
	val RPC_REMOTE_TRANSPORT = ConfigBuilder("moonbox.rpc.akka.remote.transport")
	    .stringConf
	    .createWithDefaultString("akka.remote.netty.NettyRemoteTransport")
	val RPC_AKKA_REMOTE_LOG_REMOTE_LIFECYCLE_EVENTS = ConfigBuilder("moonbox.rpc.akka.remote.log-remote-lifecycle-events")
	    .stringConf
	    .createWithDefaultString("off")
	val RPC_AKKA_CLUSTER_AUTODOWN = ConfigBuilder("moonbox.rpc.akka.cluster.auto-down")
	    .stringConf
	    .createWithDefaultString("off")
	val RPC_AKKA_CLUSTER_AUTODOWN_UNREACHABLE_AFTER = ConfigBuilder("moonbox.rpc.akka.cluster.auto-down-unreachable-after")
	    .stringConf
	    .createWithDefaultString("off")
    val RPC_AKKA_CLUSTER_FAILURE_DETECTOR_HEARTBEAT_PAUSE = ConfigBuilder("moonbox.rpc.akka.cluster.failure-detector.acceptable-heartbeat-pause")
        .timeConf
        .createWithDefaultString("10s")
	val RPC_AKKA_CLUSTER_RETRY_UNSUCCESSFUL_JOIN_AFTER = ConfigBuilder("moonbox.rpc.akka.cluster.retry-unsuccessful-join-after")
            .timeConf
            .createWithDefaultString("3s")
    val RPC_AKKA_EXTENSIONS_0 = ConfigBuilder("moonbox.rpc.akka.extensions.0")
	    .stringConf
	    .createWithDefaultString("akka.cluster.client.ClusterClientReceptionist")
    //https://doc.akka.io/docs/akka/2.4.3/java/cluster-usage.html

	val AKKA_DEFAULT_CONFIG = Map(
		RPC_AKKA_LOGLEVEL.key -> RPC_AKKA_LOGLEVEL.defaultValueString,
		RPC_AKKA_ACTOR_PROVIDER.key -> RPC_AKKA_ACTOR_PROVIDER.defaultValueString,
		RPC_AKKA_ACTOR_DEBUG_AUTORECEIVE.key -> RPC_AKKA_ACTOR_DEBUG_AUTORECEIVE.defaultValueString,
		RPC_REMOTE_TRANSPORT.key -> RPC_REMOTE_TRANSPORT.defaultValueString,
		RPC_AKKA_REMOTE_LOG_REMOTE_LIFECYCLE_EVENTS.key -> RPC_AKKA_REMOTE_LOG_REMOTE_LIFECYCLE_EVENTS.defaultValueString,
		RPC_AKKA_CLUSTER_AUTODOWN.key -> RPC_AKKA_CLUSTER_AUTODOWN.defaultValueString,
		RPC_AKKA_CLUSTER_AUTODOWN_UNREACHABLE_AFTER.key -> RPC_AKKA_CLUSTER_AUTODOWN_UNREACHABLE_AFTER.defaultValueString,
        RPC_AKKA_CLUSTER_FAILURE_DETECTOR_HEARTBEAT_PAUSE.key -> RPC_AKKA_CLUSTER_FAILURE_DETECTOR_HEARTBEAT_PAUSE.defaultValueString,
        RPC_AKKA_CLUSTER_RETRY_UNSUCCESSFUL_JOIN_AFTER.key -> RPC_AKKA_CLUSTER_RETRY_UNSUCCESSFUL_JOIN_AFTER.defaultValueString,
		RPC_AKKA_EXTENSIONS_0.key -> RPC_AKKA_EXTENSIONS_0.defaultValueString
	)

	val AKKA_HTTP_DEFAULT_CONFIG = Map(
		"moonbox.rest.akka.http.server.request-timeout" -> REST_SERVER_REQUEST_TIME.defaultValueString,
		"moonbox.rest.akka.http.server.idle-timeout" -> REST_SERVER_IDLE_TIMEOUT.defaultValueString,
		"moonbox.rest.akka.http.client.idle-timeout" -> REST_CLIENT_IDLE_TIME.defaultValueString
	)


	val LISTENER_BUS_EVENT_QUEUE_SIZE = ConfigBuilder("moonbox.scheduler.listenerbus.eventqueue.size")
	    .intConf
	    .createWithDefault(10000)

	val WORKER_TIMEOUT = ConfigBuilder("moonbox.worker.timeout")
	    .longConf
	    .createWithDefault(60)

	val WORKER_STATEREPORT_INTERVAL = ConfigBuilder("moonbox.worker.stateReport.interval")
	    .timeConf
	    .createWithDefaultString("3min")
}
