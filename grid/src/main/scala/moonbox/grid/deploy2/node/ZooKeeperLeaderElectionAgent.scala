package moonbox.grid.deploy2.node

import moonbox.common.{MbConf, MbLogging}
import org.apache.curator.framework.{CuratorFramework, CuratorFrameworkFactory}
import org.apache.curator.framework.recipes.leader.{LeaderLatch, LeaderLatchListener}
import org.apache.curator.retry.ExponentialBackoffRetry

class ZooKeeperLeaderElectionAgent(val candidate: LeaderElectable,
    conf: MbConf) extends LeaderLatchListener with LeaderElectionAgent with MbLogging  {


  val WORKING_DIR = conf.get("moonbox.deploy.zookeeper.dir", "/moonbox") + "/leader_election"

  private var zk: CuratorFramework = _
  private var leaderLatch: LeaderLatch = _
  private var status = LeadershipStatus.NOT_LEADER

  start()

  private def start() {
	  // TODO
    logInfo("Starting ZooKeeper LeaderElection agent")
    zk = {
		val servers = "localhost:2181"
		val retryTimes = 3
		val interval = 3000
		val client = CuratorFrameworkFactory.newClient(servers,
			1000, 1000,
			new ExponentialBackoffRetry(interval, retryTimes))
		client.start()
		client
	}
    leaderLatch = new LeaderLatch(zk, WORKING_DIR)
    leaderLatch.addListener(this)
    leaderLatch.start()
  }

  override def stop() {
    leaderLatch.close()
    zk.close()
  }

  override def isLeader() {
    synchronized {
      // could have lost leadership by now.
      if (!leaderLatch.hasLeadership) {
        return
      }
      logInfo("We have gained leadership")
      updateLeadershipStatus(true)
    }
  }

  override def notLeader() {
    synchronized {
      // could have gained leadership by now.
      if (leaderLatch.hasLeadership) {
        return
      }
      logInfo("We have lost leadership")
      updateLeadershipStatus(false)
    }
  }

  private def updateLeadershipStatus(isLeader: Boolean) {
    if (isLeader && status == LeadershipStatus.NOT_LEADER) {
      status = LeadershipStatus.LEADER
		candidate.electedLeader()
    } else if (!isLeader && status == LeadershipStatus.LEADER) {
      status = LeadershipStatus.NOT_LEADER
		candidate.revokedLeadership()
    }
  }

  private object LeadershipStatus extends Enumeration {
    type LeadershipStatus = Value
    val LEADER, NOT_LEADER = Value
  }
}
