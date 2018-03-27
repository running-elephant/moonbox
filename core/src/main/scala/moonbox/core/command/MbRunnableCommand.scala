package moonbox.core.command

import moonbox.core.MbSession
import moonbox.core.catalog.CatalogSession
import org.apache.spark.sql.Row

trait MbRunnableCommand extends MbCommand {
	def run(mbSession: MbSession)(implicit ctx: CatalogSession): Seq[Row]
}
