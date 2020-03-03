package moonbox.grid.deploy.rest.entities

case class RolePrivilege(user: String,
                         isSa: Boolean,
                         privileges: String,
                         createBy: String,
                         createTime: String)


case class ColumnPrivilege(user: String,
                           database: String,
                           table: String,
                           columns: String,
                           privilege: String)