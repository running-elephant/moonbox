package moonbox.core.command

trait System {
}

case object ShowSysInfo extends MbCommand with System

case object ShowJobInfo extends MbCommand with System

case object ShowRunningEventInfo extends MbCommand with System