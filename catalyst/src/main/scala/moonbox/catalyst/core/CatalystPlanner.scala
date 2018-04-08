package moonbox.catalyst.core

class CatalystPlanner(rules: Seq[Strategy]) extends CatalystStrategies {
	override def strategies: Seq[Strategy] = rules

}
