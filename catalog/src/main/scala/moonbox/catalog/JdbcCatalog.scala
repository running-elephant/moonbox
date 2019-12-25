/*-
 * <<
 * Moonbox
 * ==
 * Copyright (C) 2016 - 2019 EDP
 * ==
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * >>
 */

package moonbox.catalog

import java.util.{Date, Locale}

import moonbox.catalog.AbstractCatalog.User
import moonbox.catalog.config._
import moonbox.catalog.jdbc._
import moonbox.common.util.Utils
import moonbox.common.{MbConf, MbLogging}

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.implicitConversions


object JdbcCatalog {

  val DEFAULT_DATABASE = "default"

  implicit def timestampToDate(time: Long): Date = new Date(time)

}

class JdbcCatalog(conf: MbConf) extends AbstractCatalog with MbLogging {

  import JdbcCatalog._

  private val jdbcDao = new JdbcDao(conf)

  private def formatDatabaseName(name: String): String = {
    name.toLowerCase(Locale.ROOT)
  }

  val defaultDb = DEFAULT_DATABASE


  /**
    * org name to org id
    *
    * @param org
    * @return
    */
  def organizationId(org: String): Long = await {
    jdbcDao.action(jdbcDao.getOrganization(org)).map {
      case Some(orgEntity) => orgEntity.id.get
      case None => throw new NoSuchOrganizationException(org)
    }
  }

  def organizationName(orgId: Long): String = await {
    jdbcDao.action(jdbcDao.getOrganization(orgId)).map {
      case Some(orgEntity) => orgEntity.name
      case None => throw new IllegalStateException("organization not exists referenced by user")
    }
  }

  def clusterId(cluster: String): Long = await {
    jdbcDao.action(jdbcDao.getCluster(cluster)).map {
      case Some(clusterEntity) => clusterEntity.id.get
      case None => throw new NoSuchClusterException(cluster)
    }
  }

  def clusterName(clusterId: Long): String = await {
    jdbcDao.action(jdbcDao.getCluster(clusterId)).map {
      case Some(clusterEntity) => clusterEntity.name
      case None => throw new IllegalStateException(s"cluster is deleted.")
    }
  }

  /**
    * user name to user id
    *
    * @param user
    * @return
    */
  def userId(orgId: Long, user: String): Long = await {
    jdbcDao.action(jdbcDao.getUser(orgId, user)).map {
      case Some(userEntity) => userEntity.id.get
      case None => throw new NoSuchUserException(user)
    }
  }

  /**
    * user id to user name
    *
    * @param userId
    * @return
    */
  private def userName(userId: Long): String = {
    if (userId == -1) {
      "SYSTEM"
    } else await {
      jdbcDao.action(jdbcDao.getUser(userId)).map {
        case Some(userEntity) => userEntity.name
        case None => throw new IllegalStateException(s"user is deleted.")
      }
    }
  }


  /**
    * procedure id to procedure name
    *
    * @param procId
    * @return
    */
  private def procedureName(procId: Long): String = await {
    jdbcDao.action(jdbcDao.getProcedure(procId)).map {
      case Some(procIdEntity) => procIdEntity.name
      case None => throw new IllegalStateException(s"procedure is deleted.")
    }
  }

  // ----------------------------------------------------------------------------
  // Query -- belong to organization
  // ----------------------------------------------------------------------------

  protected override def doCreateQuery(queryDefinition: CatalogQuery,
                                       ignoreIfExists: Boolean)(implicit by: User): Unit = await {

    jdbcDao.action(jdbcDao.queryExists(by.orgId, queryDefinition.name)).flatMap {
      case true =>
        ignoreIfExists match {
          case true => Future(Unit)
          case false => throw new ProcedureExistsException(queryDefinition.name)
        }
      case false =>
        jdbcDao.action(jdbcDao.createQuery(QueryEntity(
          name = queryDefinition.name,
          text = queryDefinition.text,
          organizationId = by.orgId,
          description = queryDefinition.description,
          createBy = by.userId,
          updateBy = by.userId
        )))
    }
  }

  protected override def doDropQuery(query: String, ignoreIfNotExists: Boolean)(implicit by: User): Unit = await {

    jdbcDao.action(jdbcDao.getQuery(by.orgId, query)).flatMap {
      case Some(queryEntity) =>
        jdbcDao.action(jdbcDao.deleteQuery(queryEntity.id.get))
      case None =>
        ignoreIfNotExists match {
          case true => Future(Unit)
          case false => throw new NoSuchQueryException(query)
        }
    }
  }

  override def alterQuery(queryDefinition: CatalogQuery)(implicit by: User): Unit = await {

    jdbcDao.action(jdbcDao.getQuery(by.orgId, queryDefinition.name)).flatMap {
      case Some(queryEntity) =>
        jdbcDao.action(jdbcDao.updateQuery(QueryEntity(
          id = queryEntity.id,
          name = queryDefinition.name,
          text = queryDefinition.text,
          organizationId = by.orgId,
          description = queryDefinition.description,
          createBy = queryEntity.createBy,
          createTime = queryEntity.createTime,
          updateBy = by.userId,
          updateTime = Utils.now
        )))
      case None =>
        throw new NoSuchQueryException(queryDefinition.name)
    }

  }

  override def getQuery(query: String)(implicit by: User): CatalogQuery = await {
    jdbcDao.action(
      jdbcDao.getQuery(by.orgId, query)).map {
      case Some(queryEntity) =>
        CatalogQuery(
          name = query,
          text = queryEntity.text,
          description = queryEntity.description,
          owner = Some(userName(queryEntity.createBy))
        )
      case None => throw new NoSuchQueryException(query)
    }
  }

  override def getQueryOption(query: String)(implicit by: User): Option[CatalogQuery] = await {
    jdbcDao.action(
      jdbcDao.getQuery(by.orgId, query)
    ).map(_.map { queryEntity =>
      CatalogQuery(
        name = query,
        text = queryEntity.text,
        description = queryEntity.description,
        owner = Some(userName(queryEntity.createBy))
      )
    })
  }

  override def queryExists(query: String)(implicit by: User): Boolean = await {
    jdbcDao.action(jdbcDao.queryExists(by.orgId, query))
  }

  override def listQueries()(implicit by: User): Seq[CatalogQuery] = await {
    jdbcDao.action(
      jdbcDao.listQueries(by.orgId)
    ).map(_.sortBy(_.updateTime).reverse
      .map { queryEntity =>
        CatalogQuery(
          name = queryEntity.name,
          text = queryEntity.text,
          description = queryEntity.description,
          owner = Some(userName(queryEntity.createBy))
        )
      })
  }

  override def listQueries(pattern: String)(implicit by: User): Seq[CatalogQuery] = await {
    jdbcDao.action(
      jdbcDao.listQueries(by.orgId, pattern)
    ).map(_.map { queryEntity =>
      CatalogQuery(
        name = queryEntity.name,
        text = queryEntity.text,
        description = queryEntity.description,
        owner = Some(userName(queryEntity.createBy))
      )
    })
  }

  // ----------------------------------------------------------------------------
  // Cluster
  // ----------------------------------------------------------------------------

  override protected def doCreateCluster(clusterDefinition: CatalogCluster)(implicit by: User): Unit = await {
    jdbcDao.action(jdbcDao.createCluster(
      ClusterEntity(
        name = clusterDefinition.name,
        clusterType = clusterDefinition.`type`,
        environment = clusterDefinition.environment,
        config = clusterDefinition.config,
        createBy = by.userId,
        updateBy = by.userId
      )
    ))
  }

  override protected def doDropCluster(cluster: String, ignoreIfNotExists: Boolean)(implicit by: User): Unit = await {
    jdbcDao.action(jdbcDao.getCluster(cluster)).flatMap {
      case Some(clusterEntity) =>
        jdbcDao.action(jdbcDao.deleteCluster(clusterEntity.id.get))
      case None =>
        if (ignoreIfNotExists) Future(Unit)
        else throw new NoSuchClusterException(cluster)
    }
  }

  override def alterCluster(clusterDefinition: CatalogCluster)(implicit by: User): Unit = await {
    jdbcDao.action(jdbcDao.getCluster(clusterDefinition.name)).flatMap {
      case Some(clusterEntity) =>
        jdbcDao.action(
          jdbcDao.updateCluster(
            ClusterEntity(
              id = clusterEntity.id,
              name = clusterDefinition.name,
              clusterType = clusterDefinition.`type`,
              environment = clusterDefinition.environment,
              config = clusterDefinition.config,
              createBy = clusterEntity.createBy,
              createTime = clusterEntity.createTime,
              updateBy = by.userId,
              updateTime = Utils.now
            )
          )
        )
      case None =>
        throw new NoSuchClusterException(clusterDefinition.name)
    }
  }

  override def getCluster(cluster: String): CatalogCluster = await {
    jdbcDao.action(jdbcDao.getCluster(cluster)).map {
      case Some(clusterEntity) =>
        CatalogCluster(
          name = clusterEntity.name,
          `type` = clusterEntity.clusterType,
          environment = clusterEntity.environment,
          config = clusterEntity.config
        )
      case None =>
        throw new NoSuchClusterException(cluster)
    }
  }

  override def getClusterOption(cluster: String): Option[CatalogCluster] = await {
    jdbcDao.action(jdbcDao.getCluster(cluster)).map {
      _.map { clusterEntity =>
        CatalogCluster(
          name = clusterEntity.name,
          `type` = clusterEntity.clusterType,
          environment = clusterEntity.environment,
          config = clusterEntity.config
        )
      }
    }
  }

  override def clusterExists(cluster: String): Boolean = await {
    jdbcDao.action(jdbcDao.clusterExists(cluster))
  }

  override def listClusters(): Seq[CatalogCluster] = await {
    jdbcDao.action(jdbcDao.listClusters()).map(_.map { clusterEntity =>
      CatalogCluster(
        name = clusterEntity.name,
        `type` = clusterEntity.clusterType,
        environment = clusterEntity.environment,
        config = clusterEntity.config
      )
    })
  }

  override def listClusters(pattern: String): Seq[CatalogCluster] = await {
    jdbcDao.action(jdbcDao.listClusters(pattern)).map(_.map { clusterEntity =>
      CatalogCluster(
        name = clusterEntity.name,
        `type` = clusterEntity.clusterType,
        environment = clusterEntity.environment,
        config = clusterEntity.config
      )
    })
  }

  // ----------------------------------------------------------------------------
  // Application
  // ----------------------------------------------------------------------------

  override protected def doCreateApplication(appDefinition: CatalogApplication, ignoreIfExists: Boolean)(implicit by: User): Unit = await {
    jdbcDao.action(jdbcDao.applicationExists(appDefinition.name)).flatMap {
      case true =>
        ignoreIfExists match {
          case true => Future(Unit)
          case false => throw new ApplicationExistsException(appDefinition.name)
        }
      case false =>
        appDefinition.cluster match {
          case Some(cluster) =>
            jdbcDao.action(jdbcDao.getCluster(cluster)).flatMap {
              case Some(clusterEntity) =>
                jdbcDao.action(jdbcDao.createApplication(
                  ApplicationEntity(
                    name = appDefinition.name,
                    address = None,
                    organizationId = organizationId(appDefinition.org),
                    appType = appDefinition.appType,
                    clusterId = clusterEntity.id,
                    config = appDefinition.config,
                    startOnBoot = appDefinition.startOnBoot,
                    createBy = by.userId,
                    updateBy = by.userId
                  )
                ))
              case None =>
                throw new NoSuchClusterException(cluster)
            }
          case None =>
            jdbcDao.action(jdbcDao.createApplication(
              ApplicationEntity(
                name = appDefinition.name,
                address = None,
                organizationId = organizationId(appDefinition.org),
                appType = appDefinition.appType,
                clusterId = None,
                config = appDefinition.config,
                startOnBoot = appDefinition.startOnBoot,
                createBy = by.userId,
                updateBy = by.userId
              )
            ))
        }
    }
  }

  override def alterApplication(appDefinition: CatalogApplication)(implicit by: User): Unit = await {
    jdbcDao.action(jdbcDao.getApplication(appDefinition.name)).flatMap {
      case Some(appEntity) =>
        appDefinition.cluster match {
          case Some(cluster) =>
            jdbcDao.action(jdbcDao.getCluster(cluster)).flatMap {
              case Some(clusterEntity) =>
                jdbcDao.action(
                  jdbcDao.updateApplication(
                    ApplicationEntity(
                      id = appEntity.id,
                      name = appDefinition.name,
                      address = appEntity.address,
                      organizationId = organizationId(appDefinition.org),
                      appType = appDefinition.appType,
                      config = appDefinition.config,
                      startOnBoot = appDefinition.startOnBoot,
                      clusterId = clusterEntity.id,
                      createBy = appEntity.createBy,
                      createTime = appEntity.createTime,
                      updateBy = by.userId,
                      updateTime = Utils.now
                    )
                  )
                )
              case None =>
                throw new NoSuchClusterException(cluster)
            }
          case None =>
            jdbcDao.action(
              jdbcDao.updateApplication(
                ApplicationEntity(
                  id = appEntity.id,
                  name = appDefinition.name,
                  address = appEntity.address,
                  organizationId = organizationId(appDefinition.org),
                  appType = appDefinition.appType,
                  config = appDefinition.config,
                  startOnBoot = appDefinition.startOnBoot,
                  clusterId = None,
                  createBy = appEntity.createBy,
                  createTime = appEntity.createTime,
                  updateBy = by.userId,
                  updateTime = Utils.now
                )
              )
            )
        }
      case None =>
        throw new NoSuchApplicationException(appDefinition.name)
    }
  }

  override def listApplications()(implicit by: User): Seq[CatalogApplication] = await {
    jdbcDao.action(jdbcDao.listApplications(by.orgId)).map(_.map { case ((appEntity, orgEntity), clusterEntity) =>
      CatalogApplication(
        name = appEntity.name,
        org = orgEntity.name,
        cluster = clusterEntity.map(_.name),
        appType = appEntity.appType,
        config = appEntity.config,
        createTime = Some(appEntity.createTime),
        updateTime = Some(appEntity.updateTime),
        startOnBoot = appEntity.startOnBoot
      )
    })
  }

  override def listApplications(pattern: String)(implicit by: User): Seq[CatalogApplication] = await {
    jdbcDao.action(jdbcDao.listApplications(by.orgId, pattern)).map(_.map { case ((appEntity, orgEntity), clusterEntity) =>
      CatalogApplication(
        name = appEntity.name,
        org = orgEntity.name,
        cluster = clusterEntity.map(_.name),
        appType = appEntity.appType,
        config = appEntity.config,
        createTime = Some(appEntity.createTime),
        updateTime = Some(appEntity.updateTime),
        startOnBoot = appEntity.startOnBoot
      )
    })
  }

  override def applicationExists(app: String)(implicit by: User): Boolean = await {
    jdbcDao.action(jdbcDao.applicationExists(app))
  }

	override def applicationUsingClusterExists(cluster: String): Boolean = await {
		jdbcDao.action(jdbcDao.applicationUsingClusterExists(clusterId(cluster)))
	}

  override protected def doDropApplication(app: String, ignoreIfNotExists: Boolean)(implicit by: User): Unit = await {
    jdbcDao.action(jdbcDao.getApplication(app)).flatMap {
      case Some(appEntity) =>
        jdbcDao.action(jdbcDao.deleteApplication(appEntity.id.get))
      case None =>
        if (ignoreIfNotExists) Future(Unit)
        else throw new NoSuchApplicationException(app)
    }
  }

  override def getApplicationOption(app: String): Option[CatalogApplication] = await {
    jdbcDao.action(jdbcDao.getApplication(app)).map(_.map(appEntity =>
      CatalogApplication(
        name = appEntity.name,
        org = organizationName(appEntity.organizationId),
        appType = appEntity.appType,
        config = appEntity.config,
        cluster = appEntity.clusterId.map(clusterName),
        createTime = Some(appEntity.createTime),
        updateTime = Some(appEntity.updateTime),
        startOnBoot = appEntity.startOnBoot
      )
    ))
  }

  override def getApplication(app: String): CatalogApplication = await {
    jdbcDao.action(jdbcDao.getApplication(app)).map {
      case Some(appEntity) =>
        CatalogApplication(
          name = appEntity.name,
          org = organizationName(appEntity.organizationId),
          appType = appEntity.appType,
          config = appEntity.config,
          cluster = appEntity.clusterId.map(clusterName),
          createTime = Some(appEntity.createTime),
          updateTime = Some(appEntity.updateTime),
          startOnBoot = appEntity.startOnBoot
        )
      case None =>
        throw new NoSuchApplicationException(app)
    }
  }

  override def listAllApplications(): Seq[CatalogApplication] = await {
    jdbcDao.action(jdbcDao.listAllApplications()).map(_.map { case ((appEntity, orgEntity), clusterEntity) =>
      CatalogApplication(
        name = appEntity.name,
        org = orgEntity.name,
        cluster = clusterEntity.map(_.name),
        appType = appEntity.appType,
        config = appEntity.config,
        createTime = Some(appEntity.createTime),
        updateTime = Some(appEntity.updateTime),
        startOnBoot = appEntity.startOnBoot
      )
    })
  }

  override def listAllApplications(startOnBoot: Boolean): Seq[CatalogApplication] = await {
    jdbcDao.action(jdbcDao.listAllApplications(startOnBoot)).map(_.map { case ((appEntity, orgEntity), clusterEntity) =>
      CatalogApplication(
        name = appEntity.name,
        org = orgEntity.name,
        cluster = clusterEntity.map(_.name),
        appType = appEntity.appType,
        config = appEntity.config,
        createTime = Some(appEntity.createTime),
        updateTime = Some(appEntity.updateTime),
        startOnBoot = appEntity.startOnBoot
      )
    })
  }

	override def listApplicationsByCluster(cluster: String): Seq[CatalogApplication] = await {
		jdbcDao.action(jdbcDao.listApplicationsByCluster(clusterId(cluster))).map(_.map { case ((appEntity, orgEntity), clusterEntity) =>
			CatalogApplication(
				name = appEntity.name,
				org = orgEntity.name,
				cluster = clusterEntity.map(_.name),
				appType = appEntity.appType,
				config = appEntity.config,
				createTime = Some(appEntity.createTime),
				updateTime = Some(appEntity.updateTime),
				startOnBoot = appEntity.startOnBoot
			)
		})
	}

  // ----------------------------------------------------------------------------
  // Organization
  // ----------------------------------------------------------------------------

  protected override def doCreateOrganization(orgDefinition: CatalogOrganization, ignoreIfExists: Boolean)
                                             (implicit by: User): Unit = await {
    jdbcDao.action(jdbcDao.organizationExists(orgDefinition.name)).flatMap {
      case true =>
        ignoreIfExists match {
          case true => Future(Unit)
          case false => throw new OrganizationExistsException(orgDefinition.name)
        }
      case false =>
        jdbcDao.actionTransactionally(
          jdbcDao.createOrganization(
            OrganizationEntity(
              name = orgDefinition.name,
              config = orgDefinition.config,
              description = orgDefinition.description,
              createBy = by.userId,
              updateBy = by.userId)
          ).flatMap { id =>
            jdbcDao.createDatabase(
              DatabaseEntity(
                name = DEFAULT_DATABASE,
                organizationId = id,
                properties = Map(),
                isLogical = true,
                createBy = by.userId,
                updateBy = by.userId
              )
            )
          }
        )
    }
  }

  protected override def doDropOrganization(org: String, ignoreIfNotExists: Boolean, cascade: Boolean)
                                           (implicit by: User): Unit = await {
    jdbcDao.action(jdbcDao.getOrganization(org)).flatMap {
      case Some(catalogOrganization) =>
        if (cascade) {
          jdbcDao.actionTransactionally(
            for (
            // delete groups and group user rels
              _ <- jdbcDao.listGroups(catalogOrganization.id.get).map {
                _.map { groupEntity =>
                  jdbcDao.deleteGroupUserRelsByGroup(groupEntity.id.get)
                  jdbcDao.deleteGroup(groupEntity.id.get)
                }
              };
              // delete tables and functions in database
              _ <- jdbcDao.listDatabaseIds(catalogOrganization.id.get).map {
                _.map { dbId =>
                  jdbcDao.deleteTables(dbId)
                  jdbcDao.deleteFunctions(dbId)
                }
              };
              // delete databases in organization
              _ <- jdbcDao.deleteDatabases(catalogOrganization.id.get);
              // delete users  in organization
              _ <- jdbcDao.deleteUsers(catalogOrganization.id.get);
              // delete timed events  in organization
              _ <- jdbcDao.deleteTimedEvents(catalogOrganization.id.get);
              // delete procedures in organization
              _ <- jdbcDao.deleteProcedures(catalogOrganization.id.get);
              // delete queries in organization
              _ <- jdbcDao.deleteQueries(catalogOrganization.id.get);
              // delete organization
              _ <- jdbcDao.deleteOrganization(org)
            ) yield ()
          )
        } else {
          jdbcDao.action(
            for (
              databases <- jdbcDao.listDatabases(catalogOrganization.id.get);
              users <- jdbcDao.listUsers(catalogOrganization.id.get);
              events <- jdbcDao.listTimedEvents(catalogOrganization.id.get);
              procedures <- jdbcDao.listProcedures(catalogOrganization.id.get);
              groups <- jdbcDao.listGroups(catalogOrganization.id.get)
            ) yield (databases, users, events, procedures, groups)
          ).map { case (databases, users, events, procedures, groups) =>
            if (databases.isEmpty && users.isEmpty && events.isEmpty && procedures.isEmpty && groups.isEmpty) {
              jdbcDao.action(jdbcDao.deleteOrganization(org))
            } else {
              throw new NonEmptyException(s"organization $org")
            }
          }
        }
      case None =>
        ignoreIfNotExists match {
          case true => Future(Unit)
          case false => throw new NoSuchOrganizationException(org)
        }
    }
  }

  protected override def doRenameOrganization(org: String, newOrg: String)(implicit by: User): Unit = await {
    jdbcDao.action(jdbcDao.organizationExists(org)).flatMap {
      case true =>
        jdbcDao.action(jdbcDao.organizationExists(newOrg)).flatMap {
          case false =>
            jdbcDao.action(jdbcDao.renameOrganization(org, newOrg)(by.userId))
          case true => throw new OrganizationExistsException(newOrg)
        }
      case false =>
        throw new NoSuchOrganizationException(org)
    }
  }

  override def alterOrganization(orgDefinition: CatalogOrganization)(implicit by: User): Unit = await {
    jdbcDao.action(jdbcDao.getOrganization(orgDefinition.name)).flatMap {
      case Some(orgEntity) =>
        jdbcDao.action(jdbcDao.updateOrganization(
          OrganizationEntity(
            id = orgEntity.id,
            name = orgDefinition.name,
            config = orgDefinition.config,
            description = orgDefinition.description,
            createBy = orgEntity.createBy,
            createTime = orgEntity.createTime,
            updateBy = by.userId,
            updateTime = Utils.now))
        )
      case None =>
        throw new NoSuchOrganizationException(orgDefinition.name)
    }

  }

  override def getOrganization(org: String): CatalogOrganization = await {
    jdbcDao.action(jdbcDao.getOrganization(org)).map {
      case Some(catalogEntity) =>
        CatalogOrganization(
          name = catalogEntity.name,
          config = catalogEntity.config,
          description = catalogEntity.description
        )
      case None => throw new NoSuchOrganizationException(org)
    }
  }


  override def getOrganizationOption(org: String): Option[CatalogOrganization] = await {
    jdbcDao.action(jdbcDao.getOrganization(org)).map {
      _.map { catalogEntity =>
        CatalogOrganization(
          name = catalogEntity.name,
          config = catalogEntity.config,
          description = catalogEntity.description
        )
      }
    }
  }

  override def organizationExists(org: String): Boolean = await {
    jdbcDao.action(jdbcDao.organizationExists(org))
  }

  override def listOrganizations(): Seq[CatalogOrganization] = await {
    jdbcDao.action(jdbcDao.listOrganizations()).map(_.map { catalogEntity =>
      CatalogOrganization(
        name = catalogEntity.name,
        config = catalogEntity.config,
        description = catalogEntity.description
      )
    })
  }

  override def listOrganizations(pattern: String): Seq[CatalogOrganization] = await {
    jdbcDao.action(jdbcDao.listOrganizations(pattern)).map(_.map { catalogEntity =>
      CatalogOrganization(
        name = catalogEntity.name,
        config = catalogEntity.config,
        description = catalogEntity.description
      )
    })
  }


  // ----------------------------------------------------------------------------
  // User -- belong to organization
  // ----------------------------------------------------------------------------

  protected override def doCreateUser(userDefinition: CatalogUser, ignoreIfExists: Boolean)(implicit by: User): Unit = await {

    val orgId = organizationId(userDefinition.org)
    jdbcDao.action(jdbcDao.userExists(orgId, userDefinition.name)).flatMap {
      case true =>
        ignoreIfExists match {
          case true => Future(Unit)
          case false => throw new UserExistsException(userDefinition.name)
        }
      case false =>
        jdbcDao.action(jdbcDao.createUser(UserEntity(
          name = userDefinition.name,
          password = userDefinition.password,
          account = userDefinition.account,
          ddl = userDefinition.ddl,
          dcl = userDefinition.dcl,
          grantAccount = userDefinition.grantAccount,
          grantDdl = userDefinition.grantDdl,
          grantDcl = userDefinition.grantDcl,
          isSA = userDefinition.isSA,
          organizationId = orgId,
          configuration = userDefinition.configuration,
          createBy = by.userId,
          updateBy = by.userId
        )))
    }
  }

  protected override def doDropUser(org: String, user: String, ignoreIfNotExists: Boolean)(implicit by: User): Unit = await {
    val orgId = organizationId(org)
    jdbcDao.action(jdbcDao.getUser(orgId, user)).flatMap {
      case Some(userEntity) =>
        jdbcDao.actionTransactionally(
          for (
            _ <- jdbcDao.deleteGroupUserRelsByUser(userEntity.id.get);
            _ <- jdbcDao.deleteDatabasePrivilegeByUser(userEntity.id.get);
            _ <- jdbcDao.deleteTablePrivilegeByUser(userEntity.id.get);
            _ <- jdbcDao.deleteColumnPrivilegeByUser(userEntity.id.get);
            _ <- jdbcDao.deleteUser(userEntity.id.get)
          ) yield ()
        )
      case None =>
        ignoreIfNotExists match {
          case true => Future(Unit)
          case false => throw new NoSuchUserException(s"$user in your organization.")
        }
    }
  }

  protected override def doRenameUser(org: String, user: String, newUser: String)(implicit by: User): Unit = await {
    val orgId = organizationId(org)
    jdbcDao.action(jdbcDao.userExists(orgId, user)).flatMap {
      case true =>
        jdbcDao.action(jdbcDao.userExists(orgId, newUser)).flatMap {
          case false =>
            jdbcDao.action(jdbcDao.renameUser(user, newUser)(by.userId))
          case true =>
            throw new UserExistsException(newUser)
        }
      case false =>
        throw new NoSuchUserException(user)
    }
  }

  override def alterUser(userDefinition: CatalogUser)(implicit by: User): Unit = await {

    jdbcDao.action(
      jdbcDao.getUser(organizationId(userDefinition.org), userDefinition.name)
    ).flatMap {
      case Some(userEntity) =>
        jdbcDao.action(jdbcDao.updateUser(UserEntity(
          id = userEntity.id,
          name = userDefinition.name,
          password = userDefinition.password,
          account = userDefinition.account,
          ddl = userDefinition.ddl,
          dcl = userDefinition.dcl,
          grantAccount = userDefinition.grantAccount,
          grantDdl = userDefinition.grantDdl,
          grantDcl = userDefinition.grantDcl,
          isSA = userDefinition.isSA,
          organizationId = userEntity.organizationId,
          configuration = userDefinition.configuration,
          createBy = userEntity.createBy,
          createTime = userEntity.createTime,
          updateBy = by.userId,
          updateTime = Utils.now
        )))
      case None =>
        throw new NoSuchUserException(userDefinition.name)
    }

  }

  override def getUser(org: String, user: String): CatalogUser = await {
    jdbcDao.action(jdbcDao.getUser(organizationId(org), user)).map {
      case Some(userEntity) => CatalogUser(
        org = org,
        name = userEntity.name,
        password = userEntity.password,
        account = userEntity.account,
        ddl = userEntity.ddl,
        dcl = userEntity.dcl,
        grantAccount = userEntity.grantAccount,
        grantDdl = userEntity.grantDdl,
        grantDcl = userEntity.grantDcl,
        isSA = userEntity.isSA,
        configuration = userEntity.configuration,
        createBy = Some(userName(userEntity.createBy)),
        createTime = Some(userEntity.createTime),
        updateTime = Some(userEntity.updateTime)
      )
      case None => throw new NoSuchUserException(s"$user in organization $org")
    }
  }


  override def getUserOption(org: String, user: String): Option[CatalogUser] = await {
    jdbcDao.action(jdbcDao.getUser(organizationId(org), user)).map(_.map { userEntity =>
      CatalogUser(
        org = org,
        name = userEntity.name,
        password = userEntity.password,
        account = userEntity.account,
        ddl = userEntity.ddl,
        dcl = userEntity.dcl,
        grantAccount = userEntity.grantAccount,
        grantDdl = userEntity.grantDdl,
        grantDcl = userEntity.grantDcl,
        isSA = userEntity.isSA,
        configuration = userEntity.configuration,
        createBy = Some(userName(userEntity.createBy)),
        createTime = Some(userEntity.createTime),
        updateTime = Some(userEntity.updateTime)
      )
    })
  }

  override def userExists(org: String, user: String): Boolean = await {
    jdbcDao.action(jdbcDao.userExists(organizationId(org), user))
  }

  override def listUsers(org: String): Seq[CatalogUser] = await {
    jdbcDao.action(jdbcDao.listUsers(organizationId(org))).map(_.map { case (userEntity, createUserEntity) =>
      CatalogUser(
        org = org,
        name = userEntity.name,
        password = userEntity.password,
        account = userEntity.account,
        ddl = userEntity.ddl,
        dcl = userEntity.dcl,
        grantAccount = userEntity.grantAccount,
        grantDdl = userEntity.grantDdl,
        grantDcl = userEntity.grantDcl,
        isSA = userEntity.isSA,
        configuration = userEntity.configuration,
        createBy = Some(createUserEntity.name),
        createTime = Some(userEntity.createTime),
        updateTime = Some(userEntity.updateTime)
      )
    })
  }

  override def listUsers(org: String, pattern: String): Seq[CatalogUser] = await {
    jdbcDao.action(jdbcDao.listUsers(organizationId(org), pattern)).map(_.map { case (userEntity, createUserEntity) =>
      CatalogUser(
        org = org,
        name = userEntity.name,
        password = userEntity.password,
        account = userEntity.account,
        ddl = userEntity.ddl,
        dcl = userEntity.dcl,
        grantAccount = userEntity.grantAccount,
        grantDdl = userEntity.grantDdl,
        grantDcl = userEntity.grantDcl,
        isSA = userEntity.isSA,
        configuration = userEntity.configuration,
        createBy = Some(createUserEntity.name),
        createTime = Some(userEntity.createTime),
        updateTime = Some(userEntity.updateTime)
      )
    })
  }

  override def listSas(): Seq[CatalogUser] = await {
    jdbcDao.action(jdbcDao.listSas()).map(_.map { userEntity =>
      CatalogUser(
        org = organizationName(userEntity.organizationId),
        name = userEntity.name,
        password = userEntity.password,
        account = userEntity.account,
        ddl = userEntity.ddl,
        dcl = userEntity.dcl,
        grantAccount = userEntity.grantAccount,
        grantDdl = userEntity.grantDdl,
        grantDcl = userEntity.grantDcl,
        isSA = userEntity.isSA,
        configuration = userEntity.configuration,
        createBy = Some(userName(userEntity.createBy)),
        createTime = Some(userEntity.createTime),
        updateTime = Some(userEntity.updateTime)
      )
    })
  }

  override def listSas(pattern: String): Seq[CatalogUser] = await {
    jdbcDao.action(jdbcDao.listSas(pattern)).map(_.map { userEntity =>
      CatalogUser(
        org = organizationName(userEntity.organizationId),
        name = userEntity.name,
        password = userEntity.password,
        account = userEntity.account,
        ddl = userEntity.ddl,
        dcl = userEntity.dcl,
        grantAccount = userEntity.grantAccount,
        grantDdl = userEntity.grantDdl,
        grantDcl = userEntity.grantDcl,
        isSA = userEntity.isSA,
        configuration = userEntity.configuration,
        createBy = Some(userName(userEntity.createBy)),
        createTime = Some(userEntity.createTime),
        updateTime = Some(userEntity.updateTime)
      )
    })
  }

  // ----------------------------------------------------------------------------
  // Procedure -- belong to organization
  // ----------------------------------------------------------------------------

  protected override def doCreateProcedure(procDefinition: CatalogProcedure,
                                           ignoreIfExists: Boolean)(implicit by: User): Unit = await {

    jdbcDao.action(jdbcDao.procedureExists(by.orgId, procDefinition.name)).flatMap {
      case true =>
        ignoreIfExists match {
          case true => Future(Unit)
          case false => throw new ProcedureExistsException(procDefinition.name)
        }
      case false =>
        jdbcDao.action(jdbcDao.createProcedure(ProcedureEntity(
          name = procDefinition.name,
          cmds = procDefinition.sqls,
          lang = procDefinition.lang,
          organizationId = by.orgId,
          description = procDefinition.description,
          createBy = by.userId,
          updateBy = by.userId
        )))
    }
  }

  protected override def doDropProcedure(proc: String, ignoreIfNotExists: Boolean)(implicit by: User): Unit = await {

    jdbcDao.action(jdbcDao.getProcedure(by.orgId, proc)).flatMap {
      case Some(procedureEntity) =>
        jdbcDao.action(jdbcDao.timedEventExists(by.orgId, procedureEntity.id.get)).flatMap {
          case true =>
            throw new ProcedureStillUsedException(proc)
          case false =>
            jdbcDao.action(jdbcDao.deleteProcedure(procedureEntity.id.get))
        }
      case None =>
        ignoreIfNotExists match {
          case true => Future(Unit)
          case false => throw new NoSuchProcedureException(proc)
        }
    }
  }

  protected override def doRenameProcedure(proc: String, newProc: String)(implicit by: User): Unit = await {

    jdbcDao.action(jdbcDao.procedureExists(by.orgId, proc)).flatMap {
      case true =>
        jdbcDao.action(jdbcDao.procedureExists(by.orgId, newProc)).flatMap {
          case false =>
            jdbcDao.action(jdbcDao.renameProcedure(by.orgId, proc, newProc)(by.userId))
          case true =>
            throw new ProcedureExistsException(newProc)
        }
      case false =>
        throw new NoSuchProcedureException(proc)
    }
  }

  override def alterProcedure(procDefinition: CatalogProcedure)(implicit by: User): Unit = await {

    jdbcDao.action(jdbcDao.getProcedure(by.orgId, procDefinition.name)).flatMap {
      case Some(procEntity) =>
        jdbcDao.action(jdbcDao.updateProcedure(ProcedureEntity(
          id = procEntity.id,
          name = procDefinition.name,
          cmds = procDefinition.sqls,
          lang = procDefinition.lang,
          organizationId = by.orgId,
          description = procDefinition.description,
          createBy = procEntity.createBy,
          createTime = procEntity.createTime,
          updateBy = by.userId,
          updateTime = Utils.now
        )))
      case None =>
        throw new NoSuchProcedureException(procDefinition.name)
    }

  }

  override def getProcedure(proc: String)(implicit by: User): CatalogProcedure = await {
    jdbcDao.action(
      jdbcDao.getProcedure(by.orgId, proc)).map {
      case Some(procEntity) =>
        CatalogProcedure(
          name = proc,
          sqls = procEntity.cmds,
          lang = procEntity.lang,
          description = procEntity.description,
          owner = Some(userName(procEntity.createBy))
        )
      case None => throw new NoSuchProcedureException(proc)
    }
  }

  override def getProcedureOption(proc: String)(implicit by: User): Option[CatalogProcedure] = await {
    jdbcDao.action(
      jdbcDao.getProcedure(by.orgId, proc)
    ).map(_.map { procEntity =>
      CatalogProcedure(
        name = proc,
        sqls = procEntity.cmds,
        lang = procEntity.lang,
        description = procEntity.description,
        owner = Some(userName(procEntity.createBy))
      )
    })
  }

  override def procedureExists(proc: String)(implicit by: User): Boolean = await {
    jdbcDao.action(jdbcDao.procedureExists(by.orgId, proc))
  }

  override def listProcedures()(implicit by: User): Seq[CatalogProcedure] = await {
    jdbcDao.action(
      jdbcDao.listProcedures(by.orgId)
    ).map(_.map { case (procEntity, userEntity) =>
      CatalogProcedure(
        name = procEntity.name,
        sqls = procEntity.cmds,
        lang = procEntity.lang,
        description = procEntity.description,
        owner = Some(userEntity.name)
      )
    })
  }

  override def listProcedures(pattern: String)(implicit by: User): Seq[CatalogProcedure] = await {
    jdbcDao.action(
      jdbcDao.listProcedures(by.orgId, pattern)
    ).map(_.map { case (procEntity, userEntity) =>
      CatalogProcedure(
        name = procEntity.name,
        sqls = procEntity.cmds,
        lang = procEntity.lang,
        description = procEntity.description,
        owner = Some(userEntity.name)
      )
    })
  }

  // ----------------------------------------------------------------------------
  // timedevent -- belong to organization
  // ----------------------------------------------------------------------------

  protected override def doCreateTimedEvent(eventDefinition: CatalogTimedEvent, ignoreIfExists: Boolean)
                                           (implicit by: User): Unit = await {

    jdbcDao.action(jdbcDao.timedEventExists(by.orgId, eventDefinition.name)).flatMap {
      case true =>
        ignoreIfExists match {
          case true => Future(Unit)
          case false => throw new TimedEventExistsException(eventDefinition.name)
        }
      case false =>
        jdbcDao.action(
          jdbcDao.getProcedure(by.orgId, eventDefinition.procedure)
        ).flatMap {
          case Some(procedureEntity) =>
            jdbcDao.action(jdbcDao.createTimedEvent(TimedEventEntity(
              name = eventDefinition.name,
              organizationId = by.orgId,
              definer = userId(by.orgId, eventDefinition.definer),
              schedule = eventDefinition.schedule,
              enable = eventDefinition.enable,
              description = eventDefinition.description,
              procedure = procedureEntity.id.get,
              createBy = by.userId,
              updateBy = by.userId
            )))
          case None =>
            throw new NoSuchProcedureException(eventDefinition.procedure)
        }
    }
  }

  protected override def doRenameTimedEvent(event: String, newEvent: String)(implicit by: User): Unit = await {

    jdbcDao.action(jdbcDao.timedEventExists(by.orgId, event)).flatMap {
      case true =>
        jdbcDao.action(jdbcDao.timedEventExists(by.orgId, newEvent)).flatMap {
          case false =>
            jdbcDao.action(jdbcDao.renameTimedEvent(by.orgId, event, newEvent)(by.userId))
          case true =>
            throw new TimedEventExistsException(newEvent)
        }
      case false =>
        throw new NoSuchTimedEventException(event)
    }
  }

  override def alterTimedEvent(eventDefinition: CatalogTimedEvent)(implicit by: User): Unit = await {

    jdbcDao.action(jdbcDao.getTimedEvent(by.orgId, eventDefinition.name)).flatMap {
      case Some(eventEntity) =>
        jdbcDao.action(
          jdbcDao.getProcedure(by.orgId, eventDefinition.procedure)
        ).flatMap {
          case Some(procedureEntity) =>
            jdbcDao.action(jdbcDao.updateTimedEvent(TimedEventEntity(
              id = eventEntity.id,
              name = eventDefinition.name,
              organizationId = by.orgId,
              definer = userId(by.orgId, eventDefinition.definer),
              schedule = eventDefinition.schedule,
              enable = eventDefinition.enable,
              description = eventDefinition.description,
              procedure = procedureEntity.id.get,
              createBy = eventEntity.createBy,
              createTime = eventEntity.createTime,
              updateBy = by.userId,
              updateTime = Utils.now
            )))
          case None =>
            throw new NoSuchProcedureException(eventDefinition.procedure)
        }
      case None =>
        throw new NoSuchTimedEventException(eventDefinition.name)
    }
  }

  protected override def doDropTimedEvent(event: String, ignoreIfNotExists: Boolean)(implicit by: User): Unit = await {

    jdbcDao.action(jdbcDao.timedEventExists(by.orgId, event)).flatMap {
      case true =>
        jdbcDao.action(jdbcDao.deleteTimedEvent(by.orgId, event))
      case false =>
        ignoreIfNotExists match {
          case true => Future(Unit)
          case false => throw new NoSuchTimedEventException(event)
        }
    }
  }

  override def getTimedEvent(event: String)(implicit by: User): CatalogTimedEvent = await {
    jdbcDao.action(jdbcDao.getTimedEvent(by.orgId, event)).map {
      case Some(timedEventEntity) => CatalogTimedEvent(
        name = timedEventEntity.name,
        definer = userName(timedEventEntity.definer),
        schedule = timedEventEntity.schedule,
        enable = timedEventEntity.enable,
        description = timedEventEntity.description,
        procedure = procedureName(timedEventEntity.procedure),
        owner = Some(userName(timedEventEntity.createBy))
      )
      case None => throw new NoSuchTimedEventException(event)
    }
  }

  override def getTimedEventOption(event: String)(implicit by: User): Option[CatalogTimedEvent] = await {

    jdbcDao.action(jdbcDao.getTimedEvent(by.orgId, event)).map {
      _.map { timedEventEntity =>
        CatalogTimedEvent(
          name = timedEventEntity.name,
          definer = userName(timedEventEntity.definer),
          schedule = timedEventEntity.schedule,
          enable = timedEventEntity.enable,
          description = timedEventEntity.description,
          procedure = procedureName(timedEventEntity.procedure),
          owner = Some(userName(timedEventEntity.createBy))
        )
      }
    }
  }

  override def timedEventExists(event: String)(implicit by: User): Boolean = await {
    jdbcDao.action(jdbcDao.timedEventExists(by.orgId, event))
  }

  override def timedEventExists(procId: Long)(implicit by: User): Boolean = await {
    jdbcDao.action(jdbcDao.timedEventExists(by.orgId, procId))
  }

  override def listTimedEvents()(implicit by: User): Seq[CatalogTimedEvent] = await {
    jdbcDao.action(
      jdbcDao.listTimedEvents(by.orgId)
    ).map(_.map { timedEventEntity =>
      CatalogTimedEvent(
        name = timedEventEntity.name,
        definer = userName(timedEventEntity.definer),
        schedule = timedEventEntity.schedule,
        enable = timedEventEntity.enable,
        description = timedEventEntity.description,
        procedure = procedureName(timedEventEntity.procedure),
        owner = Some(userName(timedEventEntity.createBy))
      )
    })
  }

  override def listTimedEvents(pattern: String)(implicit by: User): Seq[CatalogTimedEvent] = await {
    jdbcDao.action(
      jdbcDao.listTimedEvents(by.orgId, pattern)
    ).map(_.map { timedEventEntity =>
      CatalogTimedEvent(
        name = timedEventEntity.name,
        definer = userName(timedEventEntity.definer),
        schedule = timedEventEntity.schedule,
        enable = timedEventEntity.enable,
        description = timedEventEntity.description,
        procedure = procedureName(timedEventEntity.procedure),
        owner = Some(userName(timedEventEntity.createBy))
      )
    })
  }

  // ----------------------------------------------------------------------------
  // Database -- belong to organization
  // ----------------------------------------------------------------------------

  protected override def doCreateDatabase(dbDefinition: CatalogDatabase, ignoreIfExists: Boolean)(implicit by: User): Unit = await {
    val dbName = formatDatabaseName(dbDefinition.name)

    jdbcDao.action(jdbcDao.databaseExists(by.orgId, dbName)).flatMap {
      case true =>
        ignoreIfExists match {
          case true => Future(Unit)
          case false => throw new DatabaseExistsException(dbDefinition.name)
        }
      case false =>
        jdbcDao.action(jdbcDao.createDatabase(DatabaseEntity(
          name = dbName,
          description = dbDefinition.description,
          organizationId = by.orgId,
          properties = dbDefinition.properties,
          isLogical = dbDefinition.isLogical,
          createBy = by.userId,
          updateBy = by.userId
        )))
    }
  }

  protected override def doDropDatabase(database: String, ignoreIfNotExists: Boolean, cascade: Boolean)(implicit by: User): Unit = await {
    val dbName = formatDatabaseName(database)

    jdbcDao.action(jdbcDao.getDatabase(by.orgId, dbName)).flatMap {
      case Some(db) =>
        if (cascade) {
          jdbcDao.actionTransactionally(
            for (
              y <- jdbcDao.deleteFunctions(db.id.get);
              z <- jdbcDao.deleteTables(db.id.get);
              _ <- jdbcDao.deleteDatabase(by.orgId, dbName)
            ) yield ()
          )
        } else {
          jdbcDao.actionTransactionally(
            for (
              tables <- jdbcDao.listTables(db.id.get);
              functions <- jdbcDao.listFunctions(db.id.get);
              _ <- if (tables.isEmpty && functions.isEmpty) {
                jdbcDao.deleteDatabase(by.orgId, dbName)
              } else throw new NonEmptyException(s"Database $database")
            ) yield ()
          )
        }
      case None =>
        ignoreIfNotExists match {
          case true => Future(Unit)
          case false => throw new NoSuchDatabaseException(database)
        }

    }
  }

  protected override def doRenameDatabase(db: String, newDb: String)(implicit by: User): Unit = await {
    val dbName = formatDatabaseName(db)
    val newDbName = formatDatabaseName(newDb)

    jdbcDao.action(jdbcDao.databaseExists(by.orgId, dbName)).flatMap {
      case true =>
        jdbcDao.action(jdbcDao.databaseExists(by.orgId, newDbName)).flatMap {
          case false =>
            jdbcDao.action(jdbcDao.renameDatabase(by.orgId, dbName, newDbName)(by.userId))
          case true =>
            throw new DatabaseExistsException(newDb)
        }
      case false =>
        throw new NoSuchDatabaseException(db)
    }
  }

  override def alterDatabase(dbDefinition: CatalogDatabase)(implicit by: User): Unit = await {
    val dbName = formatDatabaseName(dbDefinition.name)

    jdbcDao.action(jdbcDao.getDatabase(by.orgId, dbName)).flatMap {
      case Some(dbEntity) =>
        jdbcDao.action(jdbcDao.updateDatabase(DatabaseEntity(
          id = dbEntity.id,
          name = dbName,
          description = dbDefinition.description,
          organizationId = by.orgId,
          properties = dbDefinition.properties,
          isLogical = dbDefinition.isLogical,
          createBy = dbEntity.createBy,
          createTime = dbEntity.createTime,
          updateBy = by.userId,
          updateTime = Utils.now
        )))
      case None =>
        throw new NoSuchDatabaseException(dbDefinition.name)
    }

  }

  override def getDatabase(database: String)(implicit by: User): CatalogDatabase = await {
    val dbName = formatDatabaseName(database)

    jdbcDao.action(jdbcDao.getDatabase(by.orgId, dbName)).map {
      case Some(dbEntity) => CatalogDatabase(
        name = dbName,
        description = dbEntity.description,
        properties = dbEntity.properties,
        isLogical = dbEntity.isLogical,
        owner = Some(userName(dbEntity.createBy))
      )
      case None => throw new NoSuchDatabaseException(database)
    }
  }

  override def getDatabaseOption(database: String)(implicit by: User): Option[CatalogDatabase] = await {
    val dbName = formatDatabaseName(database)

    jdbcDao.action(
      jdbcDao.getDatabase(by.orgId, dbName)
    ).map(_.map { dbEntity =>
      CatalogDatabase(
        name = dbEntity.name,
        description = dbEntity.description,
        properties = dbEntity.properties,
        isLogical = dbEntity.isLogical,
        owner = Some(userName(dbEntity.createBy))
      )
    })
  }

  override def databaseExists(database: String)(implicit by: User): Boolean = await {
    val dbName = formatDatabaseName(database)
    jdbcDao.action(jdbcDao.databaseExists(by.orgId, dbName))
  }

  override def listDatabases()(implicit by: User): Seq[CatalogDatabase] = await {
    jdbcDao.action(jdbcDao.listDatabases(by.orgId)).map(_.map { case (dbEntity, userEntity) =>
      CatalogDatabase(
        name = dbEntity.name,
        description = dbEntity.description,
        properties = dbEntity.properties,
        isLogical = dbEntity.isLogical,
        owner = Some(userEntity.name)
      )
    })
  }

  override def listDatabases(pattern: String)(implicit by: User): Seq[CatalogDatabase] = await {
    jdbcDao.action(jdbcDao.listDatabases(by.orgId, pattern)).map(_.map { case (dbEntity, userEntity) =>
      CatalogDatabase(
        name = dbEntity.name,
        description = dbEntity.description,
        properties = dbEntity.properties,
        isLogical = dbEntity.isLogical,
        owner = Some(userEntity.name)
      )
    })
  }

  // ----------------------------------------------------------------------------
  // Table -- belong to database
  // ----------------------------------------------------------------------------
  protected override def doCreateTable(tableDefinition: CatalogTable, ignoreIfExists: Boolean)(implicit by: User): Unit = await {
    assert(tableDefinition.db.isDefined)
    val db = tableDefinition.db.get
    jdbcDao.action(
      jdbcDao.getDatabase(by.orgId, db)
    ).flatMap {
      case Some(dbEntity) => // require db exists
        jdbcDao.action(
          jdbcDao.getTable(dbEntity.id.get, tableDefinition.name)
        ).flatMap {
          case Some(_) => // table exists
            ignoreIfExists match {
              case true => Future(Unit)
              case false => throw new TableExistsException(db, tableDefinition.name)
            }
          case None => // do create
            jdbcDao.action(jdbcDao.createTable(TableEntity(
              name = tableDefinition.name,
              tableType = tableDefinition.tableType.name,
              description = tableDefinition.description,
              databaseId = dbEntity.id.get,
              properties = tableDefinition.properties,
              viewText = tableDefinition.viewText,
              isStream = tableDefinition.isStream,
              tableSize = tableDefinition.tableSize,
              createBy = by.userId,
              updateBy = by.userId
            )))
        }
      case None =>
        throw new NoSuchDatabaseException(db)
    }
  }

  protected override def doDropTable(database: String, table: String, ignoreIfNotExists: Boolean)(implicit by: User): Unit = await {
    jdbcDao.action(
      jdbcDao.getDatabase(by.orgId, database)
    ).flatMap {
      case Some(dbEntity) =>
        jdbcDao.action(
          jdbcDao.getTable(dbEntity.id.get, table)
        ).flatMap {
          case Some(tableEntity) =>
            val dbId = tableEntity.databaseId
            val tId = tableEntity.id.get
            jdbcDao.actionTransactionally(
              for (
                _ <- jdbcDao.deleteTablePrivilege(dbId, tId);
                _ <- jdbcDao.deleteColumnPrivilege(dbId, tId);
                _ <- jdbcDao.deleteTable(dbId, table)
              ) yield ()
            )
          case None =>
            if (!ignoreIfNotExists) {
              throw new NoSuchTableException(database, table)
            } else {
              Future(Unit)
            }
        }
      case None =>
        throw new NoSuchDatabaseException(database)
    }

  }

  protected override def doRenameTable(database: String, table: String, newTable: String)(implicit by: User): Unit = await {
    jdbcDao.action(
      jdbcDao.getDatabase(by.orgId, database)
    ).flatMap {
      case Some(dbEntity) =>
        val dbId = dbEntity.id.get
        jdbcDao.action(
          jdbcDao.tableExists(dbId, table)
        ).flatMap {
          case true =>
            jdbcDao.action(
              jdbcDao.tableExists(dbId, newTable)
            ).flatMap {
              case false =>
                jdbcDao.action(jdbcDao.renameTable(dbId, table, newTable)(by.userId))
              case true =>
                throw new TableExistsException(database, newTable)
            }
          case false =>
            throw new NoSuchTableException(database, table)
        }
      case None =>
        throw new NoSuchDatabaseException(database)
    }
  }

  override def alterTable(tableDefinition: CatalogTable)(implicit by: User): Unit = await {
    assert(tableDefinition.db.isDefined)

    val db = tableDefinition.db.get

    jdbcDao.action(
      jdbcDao.getDatabase(by.orgId, db)
    ).flatMap {
      case Some(dbEntity) =>
        jdbcDao.action(
          jdbcDao.getTable(dbEntity.id.get, tableDefinition.name)
        ).flatMap {
          case Some(tableEntity) =>
            jdbcDao.action(jdbcDao.updateTable(TableEntity(
              id = tableEntity.id,
              name = tableDefinition.name,
              tableType = tableDefinition.tableType.name,
              description = tableDefinition.description,
              databaseId = dbEntity.id.get,
              properties = tableDefinition.properties,
              viewText = tableDefinition.viewText,
              isStream = tableDefinition.isStream,
              tableSize = tableDefinition.tableSize,
              createBy = tableEntity.createBy,
              createTime = tableEntity.createTime,
              updateBy = by.userId,
              updateTime = Utils.now
            )))
          case None =>
            throw new NoSuchTableException(db, tableDefinition.name)
        }
      case None =>
        throw new NoSuchDatabaseException(db)
    }
  }

  override def getTable(database: String, table: String)(implicit by: User): CatalogTable = await {
    jdbcDao.action(
      jdbcDao.getDatabase(by.orgId, database)
    ).flatMap {
      case Some(dbEntity) =>
        jdbcDao.action(
          jdbcDao.getTable(dbEntity.id.get, table)
        ).flatMap {
          case Some(tableEntity) =>
            Future(CatalogTable(
              db = Some(database),
              name = tableEntity.name,
              tableType = CatalogTableType(tableEntity.tableType),
              description = tableEntity.description,
              properties = tableEntity.properties,
              viewText = tableEntity.viewText,
              isStream = tableEntity.isStream,
              tableSize = tableEntity.tableSize,
              owner = Some(userName(tableEntity.createBy))
            ))
          case None =>
            throw new NoSuchTableException(database, table)
        }
      case None =>
        throw new NoSuchDatabaseException(database)
    }
  }

  override def getTableOption(database: String, table: String)(implicit by: User): Option[CatalogTable] = await {
    jdbcDao.action(
      jdbcDao.getDatabase(by.orgId, database)
    ).flatMap {
      case Some(dbEntity) =>
        jdbcDao.action(
          jdbcDao.getTable(dbEntity.id.get, table)
        ).map(_.map { tableEntity =>
          CatalogTable(
            db = Some(database),
            name = tableEntity.name,
            tableType = CatalogTableType(tableEntity.tableType),
            description = tableEntity.description,
            properties = tableEntity.properties,
            viewText = tableEntity.viewText,
            isStream = tableEntity.isStream,
            tableSize = tableEntity.tableSize,
            owner = Some(userName(tableEntity.createBy))
          )
        })
      case None =>
        throw new NoSuchDatabaseException(database)
    }
  }


  override def tableExists(database: String, table: String)(implicit by: User): Boolean = await {
    jdbcDao.action(
      jdbcDao.getDatabase(by.orgId, database)
    ).flatMap {
      case Some(dbEntity) =>
        jdbcDao.action(jdbcDao.tableExists(dbEntity.id.get, table))
      case None =>
        throw new NoSuchDatabaseException(database)
    }
  }

  override def listTables(database: String)(implicit by: User): Seq[CatalogTable] = await {
    jdbcDao.action(
      jdbcDao.getDatabase(by.orgId, database)
    ).flatMap {
      case Some(dbEntity) =>
        jdbcDao.action(jdbcDao.listTables(dbEntity.id.get)).map(_.map { case (tableEntity, userEntity) =>
          CatalogTable(
            db = Some(database),
            name = tableEntity.name,
            tableType = CatalogTableType(tableEntity.tableType),
            description = tableEntity.description,
            properties = tableEntity.properties,
            viewText = tableEntity.viewText,
            isStream = tableEntity.isStream,
            tableSize = tableEntity.tableSize,
            owner = Some(userEntity.name)
          )
        })
      case None =>
        throw new NoSuchDatabaseException(database)
    }
  }

  override def listTables(database: String, pattern: String)(implicit by: User): Seq[CatalogTable] = await {
    jdbcDao.action(
      jdbcDao.getDatabase(by.orgId, database)
    ).flatMap {
      case Some(dbEntity) =>
        jdbcDao.action(jdbcDao.listTables(dbEntity.id.get, pattern)).map(_.map { case (tableEntity, userEntity) =>
          CatalogTable(
            db = Some(database),
            name = tableEntity.name,
            tableType = CatalogTableType(tableEntity.tableType),
            description = tableEntity.description,
            properties = tableEntity.properties,
            viewText = tableEntity.viewText,
            isStream = tableEntity.isStream,
            tableSize = tableEntity.tableSize,
            owner = Some(userEntity.name)
          )
        })
      case None =>
        throw new NoSuchDatabaseException(database)
    }
  }

  // ----------------------------------------------------------------------------
  // Function -- belong to database
  // ----------------------------------------------------------------------------

  protected override def doCreateFunction(funcDefinition: CatalogFunction, ignoreIfExists: Boolean)(implicit by: User): Unit = {
    assert(funcDefinition.db.isDefined)
    val db = funcDefinition.db.get
    jdbcDao.action(
      jdbcDao.getDatabase(by.orgId, db)
    ).flatMap {
      case Some(dbEntity) =>
        val dbId = dbEntity.id.get
        jdbcDao.action(jdbcDao.functionExists(dbId, funcDefinition.name)).flatMap {
          case true =>
            ignoreIfExists match {
              case true => Future(Unit)
              case false => throw new FunctionExistsException(
                funcDefinition.database, funcDefinition.name)
            }
          case false =>
            jdbcDao.actionTransactionally(
              jdbcDao.createFunction(FunctionEntity(
                name = funcDefinition.name,
                databaseId = dbId,
                description = funcDefinition.description,
                className = funcDefinition.className,
                methodName = funcDefinition.methodName,
                createBy = by.userId,
                updateBy = by.userId
              )).flatMap { funcId =>
                val resources = funcDefinition.resources.map { resource =>
                  FunctionResourceEntity(
                    funcId = funcId,
                    resourceType = resource.resourceType.`type`,
                    resource = resource.uri,
                    createBy = by.userId,
                    updateBy = by.userId
                  )
                }
                jdbcDao.createFunctionResources(resources: _*)
              }
            )
        }
      case None =>
        throw new NoSuchDatabaseException(db)
    }
  }

  protected override def doDropFunction(database: String, func: String, ignoreIfNotExists: Boolean)(implicit by: User): Unit = await {
    jdbcDao.action(
      jdbcDao.getDatabase(by.orgId, database)
    ).flatMap {
      case Some(dbEntity) =>
        val dbId = dbEntity.id.get
        jdbcDao.action(jdbcDao.getFunction(dbId, func)).flatMap {
          case Some(funcEntity) =>
            jdbcDao.actionTransactionally(
              jdbcDao.deleteFunctionResources(funcEntity.id.get).flatMap { _ =>
                jdbcDao.deleteFunction(dbId, func)
              }
            )
          case None =>
            ignoreIfNotExists match {
              case true => Future(Unit)
              case false => throw new NoSuchFunctionException(database, func)
            }
        }
      case None =>
        throw new NoSuchDatabaseException(database)
    }
  }

  protected override def doRenameFunction(database: String, func: String, newFunc: String)(implicit by: User): Unit = await {
    jdbcDao.action(
      jdbcDao.getDatabase(by.orgId, database)
    ).flatMap {
      case Some(dbEntity) =>
        val dbId = dbEntity.id.get
        jdbcDao.action(
          jdbcDao.functionExists(dbId, func)
        ).flatMap {
          case true =>
            jdbcDao.action(
              jdbcDao.functionExists(dbId, newFunc)
            ).flatMap {
              case false =>
                jdbcDao.action(jdbcDao.renameFunction(dbId, func, newFunc)(by.userId))
              case true =>
                throw new FunctionExistsException(database, func)
            }
          case false =>
            throw new NoSuchFunctionException(database, func)
        }
      case None =>
        throw new NoSuchDatabaseException(database)
    }
  }

  override def getFunction(database: String, func: String)(implicit by: User): CatalogFunction = await {
    jdbcDao.action(
      jdbcDao.getDatabase(by.orgId, database)
    ).flatMap {
      case Some(dbEntity) =>
        jdbcDao.action(jdbcDao.getFunction(dbEntity.id.get, func)).flatMap {
          case Some(funcEntity) =>
            jdbcDao.action(jdbcDao.listFunctionResources(funcEntity.id.get)).map { resourceEntities =>
              val functionResources = resourceEntities.map { resourceEntity =>
                FunctionResource(resourceEntity.resourceType, resourceEntity.resource)
              }
              CatalogFunction(
                db = Some(database),
                name = func,
                description = funcEntity.description,
                className = funcEntity.className,
                methodName = funcEntity.methodName,
                resources = functionResources,
                owner = Some(userName(funcEntity.createBy))
              )
            }
          case None => throw new NoSuchFunctionException(database, func)
        }
      case None =>
        throw new NoSuchDatabaseException(database)
    }

  }

  override def getFunctionOption(database: String, func: String)(implicit by: User): Option[CatalogFunction] = await {
    jdbcDao.action(
      jdbcDao.getDatabase(by.orgId, database)
    ).flatMap {
      case Some(dbEntity) =>
        jdbcDao.action(jdbcDao.getFunction(dbEntity.id.get, func)).flatMap {
          case Some(funcEntity) =>
            jdbcDao.action(jdbcDao.listFunctionResources(funcEntity.id.get)).map { resourceEntities =>
              val functionResources = resourceEntities.map { resourceEntity =>
                FunctionResource(resourceEntity.resourceType, resourceEntity.resource)
              }
              Some(CatalogFunction(
                db = Some(database),
                name = func,
                description = funcEntity.description,
                className = funcEntity.className,
                methodName = funcEntity.methodName,
                resources = functionResources,
                owner = Some(userName(funcEntity.createBy))
              ))
            }
          case None => Future(None)
        }
      case None =>
        throw new NoSuchDatabaseException(database)
    }

  }

  override def functionExists(database: String, func: String)(implicit by: User): Boolean = await {
    jdbcDao.action(
      jdbcDao.getDatabase(by.orgId, database)
    ).flatMap {
      case Some(dbEntity) =>
        jdbcDao.action(jdbcDao.functionExists(dbEntity.id.get, func))
      case None =>
        throw new NoSuchDatabaseException(database)
    }

  }

  override def listFunctions(database: String)(implicit by: User): Seq[CatalogFunction] = await {
    jdbcDao.action(
      jdbcDao.getDatabase(by.orgId, database)
    ).flatMap {
      case Some(dbEntity) =>
        jdbcDao.action(jdbcDao.listFunctions(dbEntity.id.get)).map { functionEntities =>
          functionEntities.groupBy(_._1).toSeq.map { case (func, tuples) =>
            val resources = tuples.map { case (_, resource) =>
              FunctionResource(resource.resourceType, resource.resource)
            }
            CatalogFunction(
              db = Some(database),
              name = func.name,
              description = func.description,
              className = func.className,
              methodName = func.methodName,
              resources = resources,
              owner = Some(userName(func.createBy))
            )
          }
        }
      case None =>
        throw new NoSuchDatabaseException(database)
    }

  }

  override def listFunctions(database: String, pattern: String)(implicit by: User): Seq[CatalogFunction] = await {
    jdbcDao.action(
      jdbcDao.getDatabase(by.orgId, database)
    ).flatMap {
      case Some(dbEntity) =>
        jdbcDao.action(jdbcDao.listFunctions(dbEntity.id.get, pattern)).map { functionEntities =>
          functionEntities.groupBy(_._1).toSeq.map { case (func, tuples) =>
            val resources = tuples.map { case (_, resource) =>
              FunctionResource(resource.resourceType, resource.resource)
            }
            CatalogFunction(
              db = Some(database),
              name = func.name,
              description = func.description,
              className = func.className,
              methodName = func.methodName,
              resources = resources,
              owner = Some(userName(func.createBy))
            )
          }
        }
      case None =>
        throw new NoSuchDatabaseException(database)
    }
  }

  // ----------------------------------------------------------------------------
  // database privilege --   the privilege relation of user - database
  // ----------------------------------------------------------------------------

  protected override def doCreateDatabasePrivilege(dbPrivilege: CatalogDatabasePrivilege)(implicit by: User): Unit = await {
    val uId = userId(by.orgId, dbPrivilege.user)
    jdbcDao.action(
      jdbcDao.getDatabase(by.orgId, dbPrivilege.database)
    ).flatMap {
      case Some(dbEntity) =>
        val dbId = dbEntity.id.get
        jdbcDao.action(
          jdbcDao.getDatabasePrivilege(uId, dbId)
        ).flatMap { exists =>
          val needCreate = dbPrivilege.privileges.filterNot(exists.contains).map { privilegeType =>
            DatabasePrivilegeEntity(
              userId = uId,
              databaseId = dbId,
              privilegeType = privilegeType,
              createBy = by.userId,
              updateBy = by.userId
            )
          }
          jdbcDao.action(jdbcDao.createDatabasePrivilege(needCreate: _*))
        }
      case None =>
        throw new NoSuchDatabaseException(dbPrivilege.database)
    }
  }

  protected override def doDropDatabasePrivilege(user: String, database: String, privileges: Seq[String])(implicit by: User): Unit = await {
    val uId = userId(by.orgId, user)
    jdbcDao.action(
      jdbcDao.getDatabase(by.orgId, database)
    ).flatMap {
      case Some(dbEntity) =>
        jdbcDao.action(jdbcDao.deleteDatabasePrivilege(uId, dbEntity.id.get, privileges: _*))
      case None =>
        throw new NoSuchDatabaseException(database)
    }
  }

  override def getDatabasePrivilege(user: String, database: String)(implicit by: User): CatalogDatabasePrivilege = await {
    val uId = userId(by.orgId, user)
    jdbcDao.action(
      jdbcDao.getDatabase(by.orgId, database)
    ).flatMap {
      case Some(dbEntity) =>
        jdbcDao.action(
          jdbcDao.getDatabasePrivilege(uId, dbEntity.id.get)).map(_.map(_.privilegeType)
        ).map { dbPrivilege =>
          CatalogDatabasePrivilege(
            user = user,
            database = database,
            privileges = dbPrivilege
          )
        }
      case None =>
        throw new NoSuchDatabaseException(database)
    }

  }


  // ----------------------------------------------------------------------------
  // table privilege --   the privilege relation of user - table
  // ----------------------------------------------------------------------------
  protected override def doCreateTablePrivilege(tablePrivilege: CatalogTablePrivilege)(implicit by: User): Unit = await {
    val uId = userId(by.orgId, tablePrivilege.user)

    jdbcDao.action(
      jdbcDao.getDatabase(by.orgId, tablePrivilege.database)
    ).flatMap {
      case Some(dbEntity) =>
        val dbId = dbEntity.id.get
        jdbcDao.action(
          jdbcDao.getTable(dbId, tablePrivilege.table)
        ).flatMap {
          case Some(tableEntity) =>
            jdbcDao.action(
              jdbcDao.getTablePrivilege(uId, dbId, tableEntity.id.get)).map(_.map(_.privilegeType)
            ).flatMap { exists =>
              val needCreate = tablePrivilege.privileges.filterNot(exists.contains).map { privilegeType =>
                TablePrivilegeEntity(
                  userId = uId,
                  databaseId = dbId,
                  tableId = tableEntity.id.get,
                  privilegeType = privilegeType,
                  createBy = by.userId,
                  updateBy = by.userId
                )
              }
              jdbcDao.action(jdbcDao.createTablePrivilege(needCreate: _*))
            }

          case None =>
            throw new NoSuchTableException(tablePrivilege.database, tablePrivilege.table)
        }

      case None =>
        throw new NoSuchDatabaseException(tablePrivilege.database)
    }

  }

  protected override def doDropTablePrivilege(user: String, database: String, table: String, privileges: Seq[String])(implicit by: User): Unit = await {

    val uId = userId(by.orgId, user)
    jdbcDao.action(
      jdbcDao.getDatabase(by.orgId, database)
    ).flatMap {
      case Some(dbEntity) =>
        val dbId = dbEntity.id.get
        jdbcDao.action(
          jdbcDao.getTable(dbId, table)
        ).flatMap {
          case Some(tableEntity) =>
            jdbcDao.action(
              jdbcDao.deleteTablePrivilege(uId, dbId, tableEntity.id.get, privileges)
            )
          case None =>
            throw new NoSuchTableException(database, table)
        }

      case None =>
        throw new NoSuchDatabaseException(database)
    }

  }

  override def getTablePrivilege(user: String, database: String, table: String)(implicit by: User): CatalogTablePrivilege = await {
    val uId = userId(by.orgId, user)
    jdbcDao.action(
      jdbcDao.getDatabase(by.orgId, database)
    ).flatMap {
      case Some(dbEntity) =>
        val dbId = dbEntity.id.get
        jdbcDao.action(
          jdbcDao.getTable(dbId, table)
        ).flatMap {
          case Some(tableEntity) =>
            jdbcDao.action(
              jdbcDao.getTablePrivilege(uId, dbId, tableEntity.id.get)
            ).map(_.map(_.privilegeType)).map { tbPrivilege =>
              CatalogTablePrivilege(
                user = user,
                database = database,
                table = table,
                privileges = tbPrivilege
              )
            }
          case None =>
            throw new NoSuchTableException(database, table)
        }

      case None =>
        throw new NoSuchDatabaseException(database)
    }

  }


  // ----------------------------------------------------------------------------
  // column privilege --   the privilege relation of user - table - column
  // ----------------------------------------------------------------------------
  protected override def doCreateColumnPrivilege(columnPrivilege: CatalogColumnPrivilege)(implicit by: User): Unit = await {

    val uId = userId(by.orgId, columnPrivilege.user)
    jdbcDao.action(
      jdbcDao.getDatabase(by.orgId, columnPrivilege.database)
    ).flatMap {
      case Some(dbEntity) =>
        val dbId = dbEntity.id.get
        jdbcDao.action(
          jdbcDao.getTable(dbId, columnPrivilege.table)
        ).flatMap {
          case Some(tableEntity) =>
            val tbId = tableEntity.id.get
            jdbcDao.action(
              jdbcDao.getColumnPrivilege(uId, dbId, tbId)
            ).map(entities => entities.map(en => (en.column, en.privilegeType))).flatMap { exists =>
              val needCreate = columnPrivilege.privilege.toSeq.flatMap { case (column, t) =>
                t.map((column, _))
              }.filterNot(exists.contains).map { case (c, t) =>
                ColumnPrivilegeEntity(
                  userId = uId,
                  databaseId = dbId,
                  tableId = tbId,
                  column = c,
                  privilegeType = t,
                  createBy = by.userId,
                  updateBy = by.userId
                )
              }
              jdbcDao.action(jdbcDao.createColumnPrivilege(needCreate: _*))
            }
          case None =>
            throw new NoSuchTableException(columnPrivilege.database, columnPrivilege.table)
        }

      case None =>
        throw new NoSuchDatabaseException(columnPrivilege.database)
    }
  }

  protected override def doDropColumnPrivilege(
                                                user: String,
                                                database: String,
                                                table: String,
                                                privileges: Seq[(String, Seq[String])])(implicit by: User): Unit = await {

    // because slick does not support where (c1, c2) in (('a', 'b'), ('c', 'd')),
    // so we use privilegeType as the main filter key and the column as inSet key
    // to reduce the times of database request.
    val typeToColumns = privileges.flatMap { case (column, privilegeTypes) =>
      privilegeTypes.map((column, _)) // column => type
    }.groupBy(_._2).map {
      case (k, v) => (k, v.map(_._1))
    }

    val uId = userId(by.orgId, user)

    jdbcDao.action(
      jdbcDao.getDatabase(by.orgId, database)
    ).flatMap {
      case Some(dbEntity) =>
        val dbId = dbEntity.id.get
        jdbcDao.action(
          jdbcDao.getTable(dbId, table)
        ).flatMap {
          case Some(tableEntity) =>
            val dropActions = typeToColumns.toSeq.map { case (privilegeType, columns) =>
              jdbcDao.deleteColumnPrivilege(uId, dbId, tableEntity.id.get, columns, privilegeType)
            }
            jdbcDao.actionTransactionally(dropActions: _*)
          case None =>
            throw new NoSuchTableException(database, table)
        }

      case None =>
        throw new NoSuchDatabaseException(database)
    }
  }

  override def getColumnPrivilege(user: String, database: String, table: String)(implicit by: User): CatalogColumnPrivilege = await {
    val uId = userId(by.orgId, user)
    jdbcDao.action(
      jdbcDao.getDatabase(by.orgId, database)
    ).flatMap {
      case Some(dbEntity) =>
        val dbId = dbEntity.id.get
        jdbcDao.action(
          jdbcDao.getTable(dbId, table)
        ).flatMap {
          case Some(tableEntity) =>
            jdbcDao.action(
              jdbcDao.getColumnPrivilege(uId, dbId, tableEntity.id.get)
            ).map { entities =>
              val privileges = entities.groupBy(_.column).map {
                case (k, v) => (k, v.map(_.privilegeType))
              }
              CatalogColumnPrivilege(
                user = user,
                database = database,
                table = table,
                privilege = privileges
              )
            }
          case None =>
            throw new NoSuchTableException(database, table)
        }

      case None =>
        throw new NoSuchDatabaseException(database)
    }
  }

  // ----------------------------------------------------------------------------
  // Group
  // ----------------------------------------------------------------------------

  override protected def doCreateGroup(groupDefinition: CatalogGroup, ignoreIfExists: Boolean)(implicit by: User): Unit = await {
    jdbcDao.action(jdbcDao.groupExists(by.orgId, groupDefinition.name)).flatMap {
      case true =>
        if (ignoreIfExists) {
          Future(Unit)
        } else {
          throw new GroupExistsException(groupDefinition.name)
        }
      case false =>
        jdbcDao.action(
          jdbcDao.createGroup(GroupEntity(
            name = groupDefinition.name,
            organizationId = by.orgId,
            description = groupDefinition.desc,
            createBy = by.userId,
            updateBy = by.userId
          ))
        )
    }
  }

  override protected def doRenameGroup(group: String, newGroup: String)(implicit by: User): Unit = await {
    jdbcDao.action(jdbcDao.groupExists(by.orgId, group)).flatMap {
      case false => throw new NoSuchGroupException(group)
      case true =>
        jdbcDao.action(jdbcDao.groupExists(by.orgId, newGroup)).flatMap {
          case true => throw new GroupExistsException(newGroup)
          case false =>
            jdbcDao.action(jdbcDao.renameGroup(by.orgId, group, newGroup)(by.userId))
        }
    }
  }

  override def listGroups()(implicit by: User): Seq[CatalogGroup] = await {
    jdbcDao.action(jdbcDao.listGroups(by.orgId)).map(_.map(groupEntity =>
      CatalogGroup(groupEntity.name, groupEntity.description)))
  }

  override def listGroups(pattern: String)(implicit by: User): Seq[CatalogGroup] = await {
    jdbcDao.action(jdbcDao.listGroups(by.orgId, pattern)).map(_.map(groupEntity =>
      CatalogGroup(groupEntity.name, groupEntity.description)))
  }

  override def alterGroup(groupDefinition: CatalogGroup)(implicit by: User): Unit = await {
    jdbcDao.action(jdbcDao.getGroup(by.orgId, groupDefinition.name)).flatMap {
      case None => throw new NoSuchGroupException(groupDefinition.name)
      case Some(groupEntity) =>
        jdbcDao.action(jdbcDao.updateGroup(
          groupEntity.copy(updateBy = by.userId, updateTime = Utils.now)
        ))
    }
  }

  override def getGroup(group: String)(implicit by: User): CatalogGroup = await {
    jdbcDao.action(jdbcDao.getGroup(by.orgId, group)).map {
      case None => throw new NoSuchGroupException(group)
      case Some(groupEntity) =>
        CatalogGroup(
          groupEntity.name,
          groupEntity.description
        )
    }
  }

  override def getGroupOption(group: String)(implicit by: User): Option[CatalogGroup] = await {
    jdbcDao.action(jdbcDao.getGroup(by.orgId, group)).map(_.map(groupEntity =>
      CatalogGroup(
        groupEntity.name,
        groupEntity.description
      )
    ))
  }

  override protected def doDropGroup(group: String, ignoreIfNotExists: Boolean, cascade: Boolean)(implicit by: User): Unit = await {
    jdbcDao.action(jdbcDao.getGroup(by.orgId, group)).flatMap {
      case None =>
        if (ignoreIfNotExists) Future(Unit)
        else throw new NoSuchGroupException(group)
      case Some(groupEntity) =>
        jdbcDao.action(jdbcDao.getGroupUserRelsByGroup(groupEntity.id.get)).flatMap { rels =>
          if (rels.nonEmpty) {
            if (!cascade) throw new NonEmptyException(group)
            else {
              jdbcDao.actionTransactionally(
                jdbcDao.deleteGroupUserRelsByGroup(groupEntity.id.get).flatMap(_ => jdbcDao.deleteGroup(groupEntity.id.get))
              )
            }
          } else {
            jdbcDao.action(jdbcDao.deleteGroup(groupEntity.id.get))
          }
        }
    }
  }

  override def groupExists(group: String)(implicit by: User): Boolean = await {
    jdbcDao.action(jdbcDao.groupExists(by.orgId, group))
  }

  override protected def doCreateGroupUserRel(groupUserRels: CatalogGroupUserRel)(implicit by: User): Unit = await {
    jdbcDao.action(jdbcDao.getGroup(by.orgId, groupUserRels.group)).flatMap {
      case None => throw new NoSuchGroupException(groupUserRels.group)
      case Some(groupEntity) =>
        jdbcDao.action(jdbcDao.getGroupUserRelsByGroup(groupEntity.id.get)).flatMap { rels =>
          jdbcDao.action(jdbcDao.createGroupUserRel(
            groupUserRels.users.filterNot(rels.contains).map(user => userId(by.orgId, user)).map(id =>
              GroupUserRelEntity(
                groupId = groupEntity.id.get,
                userId = id,
                createBy = by.userId,
                updateBy = by.userId
              )
            ): _*
          ))
        }
    }
  }

  override protected def doDropGroupUserRel(groupUserRel: CatalogGroupUserRel)(implicit by: User): Unit = await {
    jdbcDao.action(jdbcDao.getGroup(by.orgId, groupUserRel.group)).flatMap {
      case None => throw new NoSuchGroupException(groupUserRel.group)
      case Some(groupEntity) =>
        jdbcDao.action(
          jdbcDao.deleteGroupUserRels(groupEntity.id.get, groupUserRel.users.map(user => userId(by.orgId, user)))
        )
    }
  }

  override def listGroupUser(group: String)(implicit by: User): CatalogGroupUserRel = await {
    jdbcDao.action(jdbcDao.getGroup(by.orgId, group)).flatMap {
      case None => throw new NoSuchGroupException(group)
      case Some(groupEntity) =>
        jdbcDao.action(jdbcDao.getGroupUserRelsByGroup(groupEntity.id.get)).map(users =>
          CatalogGroupUserRel(
            group = group,
            users = users
          )
        )
    }
  }

  override def listGroupUser(group: String, pattern: String)(implicit by: User): CatalogGroupUserRel = await {
    jdbcDao.action(jdbcDao.getGroup(by.orgId, group)).flatMap {
      case None => throw new NoSuchGroupException(group)
      case Some(groupEntity) =>
        jdbcDao.action(jdbcDao.getGroupUserRelsByGroup(groupEntity.id.get, pattern)).map(users =>
          CatalogGroupUserRel(
            group = group,
            users = users
          )
        )
    }
  }


  override def listDatabasePrivileges()(implicit by: User): Seq[CatalogDatabasePrivilege] = await {
    jdbcDao.action(jdbcDao.listDatabasePrivileges(by.orgId)).map(
      privilegeEntities => {
        val privileges = privilegeEntities.map {
          case ((userEntity, databasePrivilegeEntity), databaseEntity) =>
            CatalogDatabasePrivilege(
              user = userEntity.name,
              database = databaseEntity.name,
              privileges = Seq(databasePrivilegeEntity.privilegeType)
            )
        }.sortBy(privilege => (privilege.user, privilege.database))

        val catalogDatabasePrivilegeSeq = new ListBuffer[CatalogDatabasePrivilege]
        if (privileges.nonEmpty) {
          var currentUser: String = privileges.head.user
          var currentDb: String = privileges.head.database
          val privilegeTypeSeq = new ListBuffer[String]
          for (privilege <- privileges) {
            if (privilege.user == currentUser && privilege.database == currentDb) {
              privilegeTypeSeq.append(privilege.privileges.head)
            } else {
              catalogDatabasePrivilegeSeq.append(
                CatalogDatabasePrivilege(
                  user = currentUser,
                  database = currentDb,
                  privileges = privilegeTypeSeq
                )
              )

              currentUser = privilege.user
              currentDb = privilege.database
              privilegeTypeSeq.clear()
              privilegeTypeSeq.append(privilege.privileges.head)
            }
          }

          catalogDatabasePrivilegeSeq.append(
            CatalogDatabasePrivilege(
              user = currentUser,
              database = currentDb,
              privileges = privilegeTypeSeq
            )
          )
        }
        catalogDatabasePrivilegeSeq
      }
    )
  }

  override def listTablePrivileges()(implicit by: User): Seq[CatalogTablePrivilege] = await {
    jdbcDao.action(jdbcDao.listTablePrivileges(by.orgId)).map(
      privilegeEntities => {
        val privileges = privilegeEntities.map {
          case (((userEntity, tablePrivilegeEntity), tableEntity), databaseEntity) =>
            CatalogTablePrivilege(
              user = userEntity.name,
              database = databaseEntity.name,
              table = tableEntity.name,
              privileges = Seq(tablePrivilegeEntity.privilegeType)
            )
        }.sortBy(privilege => (privilege.user, privilege.database, privilege.table))

        val catalogTablePrivilegeSeq = new ListBuffer[CatalogTablePrivilege]

        if (privileges.nonEmpty) {
          var currentUser: String = privileges.head.user
          var currentDb: String = privileges.head.database
          var currentTable: String = privileges.head.table
          val privilegeTypeSeq = new ListBuffer[String]
          for (privilege <- privileges) {
            if (privilege.user == currentUser && privilege.database == currentDb && privilege.table == currentTable) {
              privilegeTypeSeq.append(privilege.privileges.head)
            } else {
              catalogTablePrivilegeSeq.append(
                CatalogTablePrivilege(
                  user = currentUser,
                  database = currentDb,
                  table = currentTable,
                  privileges = privilegeTypeSeq
                )
              )

              currentUser = privilege.user
              currentDb = privilege.database
              currentTable = privilege.table
              privilegeTypeSeq.clear()
              privilegeTypeSeq.append(privilege.privileges.head)
            }
          }

          catalogTablePrivilegeSeq.append(
            CatalogTablePrivilege(
              user = currentUser,
              database = currentDb,
              table = currentTable,
              privileges = privilegeTypeSeq
            )
          )
        }
        catalogTablePrivilegeSeq
      }
    )
  }


  override def listColumnPrivileges()(implicit by: User): Seq[CatalogColumnPrivilegeEntity] = await {
    jdbcDao.action(jdbcDao.listColumnPrivileges(by.orgId)).map(
      privilegeEntities => {
        privilegeEntities.map {
          case (((userEntity, columnPrivilegeEntity), tableEntity), databaseEntity) =>
            CatalogColumnPrivilegeEntity(
              user = userEntity.name,
              database = databaseEntity.name,
              table = tableEntity.name,
              column = columnPrivilegeEntity.column,
              privilegeType = columnPrivilegeEntity.privilegeType
            )
        }.sortBy(privilege => (privilege.user, privilege.database, privilege.table, privilege.privilegeType))

        //        privileges.foreach(privilege => println(privilege.user, privilege.column, privilege.privilegeType))
        //
        //        val catalogColumnPrivilegeSeq = new ListBuffer[CatalogColumnPrivilege]
        //
        //        if (privileges.nonEmpty) {
        //          var currentUser: String = privileges.head.user
        //          var currentDb: String = privileges.head.database
        //          var currentTable: String = privileges.head.table
        //          var currentColumn: String = privileges.head.column
        //          val privilegeTypeSeq = new ListBuffer[String]
        //          for (privilege <- privileges) {
        //            if (privilege.user == currentUser && privilege.database == currentDb && privilege.table == currentTable && privilege.column == currentColumn) {
        //              privilegeTypeSeq.append(privilege.privilegeType)
        //            } else {
        //              catalogColumnPrivilegeSeq.append(
        //                CatalogColumnPrivilege(
        //                  user = currentUser,
        //                  database = currentDb,
        //                  table = currentTable,
        //                  privilege = Map(currentColumn -> privilegeTypeSeq)
        //                )
        //              )
        //
        //              currentUser = privilege.user
        //              currentDb = privilege.database
        //              currentTable = privilege.table
        //              currentColumn = privilege.column
        //              privilegeTypeSeq.clear()
        //              privilegeTypeSeq.append(privilege.privilegeType)
        //            }
        //          }
        //
        //          catalogColumnPrivilegeSeq.append(
        //            CatalogColumnPrivilege(
        //              user = currentUser,
        //              database = currentDb,
        //              table = currentTable,
        //              privilege = Map(currentColumn -> privilegeTypeSeq)
        //            )
        //          )
        //        }
        //        catalogColumnPrivilegeSeq
      }
    )
  }

  /**
    * asynchronous to synchronous
    *
    * @param f asynchronous method
    * @tparam T result type
    * @return
    */
  private def await[T](f: Future[T]): T = {
    Await.result(f, new FiniteDuration(conf.get(JDBC_CATALOG_AWAIT_TIMEOUT), MILLISECONDS))
  }

  def close(): Unit = {
    jdbcDao.close()
  }

}
