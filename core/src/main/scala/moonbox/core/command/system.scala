package moonbox.core.command

trait System {
}

case object ShowSysInfo extends MbCommand with System

case object ShowJobInfo extends MbCommand with System

case object ShowEventInfo extends MbCommand with System