package moonbox.catalog

class StillUsedException(msg: String) extends Exception(msg)

class ProcedureStillUsedException(proc: String)
  extends StillUsedException(s"procedure $proc is still used by some events")

