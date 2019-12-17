package moonbox.grid.deploy.rest.entities

import java.sql.Timestamp

case class OrgSa(org: String, name: String, password: Option[String])

case class OrgSaDetail(org: String,
                       name: String,
                       createTime: Timestamp,
                       updateTime: Timestamp)

case class BatchOpSaSeq(sas: Seq[OrgSa])