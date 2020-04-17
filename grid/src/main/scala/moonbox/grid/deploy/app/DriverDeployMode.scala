package moonbox.grid.deploy.app

object DriverDeployMode {

  def apply(deployMode: String): DriverDeployMode = {
    if (deployMode.equalsIgnoreCase("CLIENT")) {
      CLIENT
    } else if (deployMode.equalsIgnoreCase("CLUSTER")) {
      CLUSTER
    } else {
      NONE
    }
  }

  case object NONE extends DriverDeployMode

  case object CLIENT extends DriverDeployMode

  case object CLUSTER extends DriverDeployMode

}

trait DriverDeployMode