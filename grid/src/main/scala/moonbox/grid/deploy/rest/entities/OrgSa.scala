package moonbox.grid.deploy.rest.entities

case class OrgSa(org: String, name: String, password: Option[String])

case class OrgSaRename(org: String, user: String, newUser: String)

case class OrgSaDetail(org: String,
                       name: String,
                       createTime: String,
                       updateTime: String)

case class BatchOpSaSeq(sas: Seq[OrgSa])