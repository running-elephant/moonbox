package moonbox.grid.deploy.rest.entities

sealed trait ResourceStat

case class OrgResourceStatSummary(
  org: String,
  usedMemory: String,
  usedCores: Int,
  totalMemory: String,
  totalCores: Int) extends ResourceStat


case class OrgResourceStatInfo(
  summary: OrgResourceStatSummary,
  detail: Seq[ApplicationResourceStat]) extends ResourceStat

case class ApplicationResourceStat(
  org: String,
  appName: String,
  appType: String,
  appCluster: String,
  usedMemory: String,
  usedCores: Int)


