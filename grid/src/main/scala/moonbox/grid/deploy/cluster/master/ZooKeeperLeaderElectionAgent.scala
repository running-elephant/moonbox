package moonbox.grid.deploy.cluster.master

import moonbox.common.{MbConf, MbLogging}
import moonbox.grid.config._
import org.apache.curator.framework.recipes.leader.{LeaderLatch, LeaderLatchListener}
import org.apache.curator.framework.CuratorFramework

class ZooKeeperLeaderElectionAgent(val candidate: LeaderElectable,
    conf: MbConf) extends LeaderLatchListener with LeaderElectionAgent with MbLogging  {

  private val WORKING_DIR = conf.get(RECOVERY_ZOOKEEPER_DIR) + "/leader_election"

  private var zk: CuratorFramework = _
  private var leaderLatch: LeaderLatch = _
  private var status = LeadershipStatus.NOT_LEADER

  start()

  private def start() {
	  // TODO
    logInfo("Starting ZooKeeper LeaderElection agent.")
    zk = ZooKeeperUtil.newClient(conf)
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
