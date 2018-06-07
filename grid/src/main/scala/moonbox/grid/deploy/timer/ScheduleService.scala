package moonbox.grid.deploy.timer

import java.util.Date
import moonbox.grid.deploy.timer.EventStatus.EventStatus


case class Application(name: String,
                       group: String,
                       sql: Seq[String])

case class Event(name: String,
                 group: String,
                 cronExpression: String,
                 startAt: Option[Date] = None,
                 endAt: Option[Date] = None)

case class ScheduledEntity(eventName: String,
                           eventGroup: String,
                           appName: String,
                           appGroup: String,
                           sql: Seq[String],
                           cronExpression: String,
                           startAt: Option[Date] = None,
                           endAt: Option[Date] = None)

case class ScheduledStatistics(firstFireTime: Option[Date],
                               prevFireTime: Option[Date],
                               nextFireTime: Option[Date],
                               endFireTime: Option[Date],
                               status: EventStatus)

case class ScheduledSummary(eventEntity: ScheduledEntity,
                            eventStatistics: ScheduledStatistics)


trait ScheduleService {

    def getAllApplications: Seq[(String, String)]

    def getApplicationsByGroup(groupName: String): Seq[(String, String)]

    def updateApplication(appName: String, appGroup: String, sql: Seq[String], function: (String, String, String, String, Seq[String]) => Unit ): Unit

    def deleteApplication(appName: String, appGroup: String): Boolean

    def pauseApplication(appName: String, appGroup: String): Unit

    def resumeApplication(appName: String, appGroup: String): Unit

    def checkApplicationExists(appName: String, appGroup: String): Boolean

    def getAllEvents: Seq[(String, String)]

    def getPendingEvents: Seq[ScheduledSummary]

    def getEventsByApplication(appName: String, appGroup: String): Seq[ScheduledSummary]

    def checkEventExists(eventGroup: String, eventName: String): Boolean

    def getEventsByGroup(eventGroup: String): Seq[(String, String)]

    def getEventByName(eventGroup: String, eventName: String): Option[ScheduledSummary]

    def startEvent(entity: ScheduledEntity, fun: (String, String, String, String, Seq[String]) => Unit ): Unit

    def updateEvent(entity: ScheduledEntity): Unit

    def deleteEvent(eventName: String, eventGroup: String): Boolean

    def pauseEvent(eventName: String, eventGroup: String): Unit

    def resumeEvent(eventName: String, eventGroup: String): Unit

    def shutdown(wait: Boolean = true): Unit

    def clear(): Unit
}
