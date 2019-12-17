package moonbox.grid.deploy.rest.entities

import java.sql.Date

case class Organization(name: String, config: Map[String, String], description: Option[String])

case class DateTest(date: Date = new Date(System.currentTimeMillis()))