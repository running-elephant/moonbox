package moonbox.catalyst.core

import org.apache.spark.sql.catalyst.catalog.SessionCatalog
import org.apache.spark.sql.catalyst.optimizer.Optimizer
import org.apache.spark.sql.internal.SQLConf

class CatalystOptimizer(catalog: SessionCatalog, conf: SQLConf)
	extends Optimizer(catalog, conf)
