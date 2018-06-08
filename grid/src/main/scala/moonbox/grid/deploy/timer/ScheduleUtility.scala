package moonbox.grid.deploy.timer

import java.util.Locale

import com.cronutils.descriptor.CronDescriptor
import com.cronutils.model.CronType
import com.cronutils.model.definition.{CronDefinition, CronDefinitionBuilder}
import com.cronutils.parser.CronParser

object ScheduleUtility {

    def describe(cronExpression: String): String = {
        val descriptor: CronDescriptor = CronDescriptor.instance(Locale.US); //Locale.CHINA
        val cronDefinition: CronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ)
        val parser: CronParser = new CronParser(cronDefinition)

        descriptor.describe(parser.parse(cronExpression))
    }


}
