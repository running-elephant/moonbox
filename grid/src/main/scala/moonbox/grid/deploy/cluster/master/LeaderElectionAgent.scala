package moonbox.grid.deploy.cluster.master

trait LeaderElectionAgent {
  	val candidate: LeaderElectable
  	def stop() {} // to avoid noops in implementations.
}

trait LeaderElectable {
  	def electedLeader(): Unit
  	def revokedLeadership(): Unit
}

class MonarchyLeaderAgent(val candidate: LeaderElectable)
  	extends LeaderElectionAgent {
		candidate.electedLeader()
}
