package moonbox.grid.deploy.master

import moonbox.grid.JobInfo
import moonbox.grid.deploy.worker.WorkerInfo

import scala.reflect.ClassTag

abstract class PersistenceEngine {
	protected def persist(name: String, obj: Object): Unit

	protected def unpersist(name: String): Unit

	protected def read[T: ClassTag](prefix: String): Seq[T]

	final def readJobs(): Seq[JobInfo] = {
		read[JobInfo]("jobs")
	}

	final def addJob(job: JobInfo): Unit = {
		persist("jobs/" + job.jobId, job)
	}

	final def removeJob(job: JobInfo): Unit = {
		unpersist("jobs/" + job.jobId)
	}

	final def addWorker(worker: WorkerInfo): Unit = {
		persist("workers/" + worker.id, worker)
	}

	final def removeWorker(worker: WorkerInfo): Unit = {
		unpersist("workers/" + worker.id)
	}

	def exist(path: String): Boolean

	def close(): Unit = {}
}
