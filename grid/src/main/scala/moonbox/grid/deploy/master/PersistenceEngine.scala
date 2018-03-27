package moonbox.grid.deploy.master

import moonbox.grid.JobInfo
import moonbox.grid.deploy.worker.WorkerInfo

import scala.reflect.ClassTag

abstract class PersistenceEngine {
	protected def persist(name: String, obj: Object): Unit

	protected def unpersist(name: String): Unit

	protected def read[T: ClassTag](prefix: String): Seq[T]

	final def addJob(job: JobInfo): Unit = {
		persist(job.jobId, job)
	}

	final def removeJob(job: JobInfo): Unit = {
		unpersist(job.jobId)
	}

	final def addWorker(worker: WorkerInfo): Unit = {
		persist(worker.id, worker)
	}

	final def removeWorker(worker: WorkerInfo): Unit = {
		unpersist(worker.id)
	}

	def close(): Unit = {}
}
