package moonbox.grid.deploy.timer

object EventStatus extends Enumeration{
    type EventStatus = Value
    val NONE, NORMAL, PAUSED, COMPLETE, ERROR, BLOCKED = Value
}