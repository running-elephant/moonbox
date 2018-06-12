package moonbox.grid.timer

object EventState extends Enumeration {
    type EventState = Value
    val NONE, NORMAL, PAUSED, COMPLETE, ERROR, BLOCKED = Value
}