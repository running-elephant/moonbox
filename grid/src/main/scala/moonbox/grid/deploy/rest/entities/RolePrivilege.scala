package moonbox.grid.deploy.rest.entities

case class RolePrivilege(user: String,
                         isSa: Boolean,
                         privileges: String,
                         createBy: String,
                         createTime: String)