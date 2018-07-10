package org.apache.spark.sql.util

import org.apache.spark.util.Utils

object UtilsWrapper {
    def getContextOrSparkClassLoader = Utils.getContextOrSparkClassLoader
}
