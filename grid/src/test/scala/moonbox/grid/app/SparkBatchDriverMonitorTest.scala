package moonbox.grid.app

import moonbox.common.util.Utils
import org.apache.hadoop.fs.Path
import org.apache.hadoop.security.UserGroupInformation
import org.apache.hadoop.yarn.conf.YarnConfiguration
import org.scalatest.FunSuite


class SparkBatchDriverMonitorTest extends FunSuite {

  test("yarn-config") {
    val env = Map("YARN_CONF_DIR" -> "/Users/swallow/Documents/yarn_conf")
    val config = new YarnConfiguration()
    Utils.getDefaultYarnPropertyFiles(env).foreach(file => config.addResource(new Path(file)))
    println(config.get("yarn.resourcemanager.address"))
    val principal = "adxtest/user@ABDT.COM"
    val keytab = "/Users/swallow/Documents/kerberos/adxtest.keytab"

    UserGroupInformation.setConfiguration(config)
    UserGroupInformation.loginUserFromKeytab(principal, keytab)
  }

  test("kerberos") {
    val principal = "adxtest/user@ABDT.COM"
    val keytab = "/Users/swallow/Documents/kerberos/adxtest.keytab"

    UserGroupInformation.loginUserFromKeytab(principal, keytab)
  }

}
