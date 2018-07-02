package moonbox.common.util

import fastparse.all._
import fastparse.core.{Parsed, Parser}

import scala.collection.mutable.ArrayBuffer


object ParseUtils {

	lazy val hostnameParser = P((CharIn('a' to 'z') | CharIn('A' to 'Z') | CharIn('0' to '9')).rep.!)
	lazy val ipParser = P(CharIn('0' to '9').rep(min=1, max=3).rep(min=4, max=4, sep=".").!)
	lazy val portParser = P(CharIn('0' to '9').rep.!.map(_.toInt))
	lazy val socketParser = P((ipParser | hostnameParser) ~ (":" ~ portParser).?)
	lazy val serversParser = P(socketParser.rep(sep = ",") ~ End)
	lazy val timeParser = P(CharIn('0' to '9').rep.!.map(_.toLong) ~ (CharIn('a' to 'z') | CharIn('A' to 'Z')).rep.!)
	lazy val keyParser = P((!CharIn(" ", "=") ~ AnyChar).rep.!)
	lazy val valueParser = P(("'" ~ (!"'" ~ AnyChar).rep.! ~ "'") | ("\"" ~ (!"\"" ~ AnyChar).rep.! ~ "\""))
	lazy val delimiterParser = P((CharIn(" ").rep ~ CharIn("=").rep(min = 1,max = 2) ~ CharIn(" ").rep) | CharIn(" ").rep(min = 1))
	lazy val keyValueParser = P(keyParser ~ delimiterParser ~ valueParser)
	lazy val propertiesParser = P(keyValueParser.rep(sep = "," ~ " ".rep) ~ End)

	lazy val variableParser = "(\\$[a-zA-Z_][a-zA-Z0-9_]*)".r

	/*lazy val headCharInVariableParser = P(CharIn('a' to 'z') | CharIn('A' to 'Z') | CharIn("_"))
	lazy val tailCharInVariableParser = P((CharIn('a' to 'z') | CharIn('A' to 'Z') | CharIn('0' to '9') | CharIn("_")).rep)
	lazy val variableParser = P(Start ~ (!CharIn("$")).rep ~ CharIn("$") ~ (headCharInVariableParser ~ tailCharInVariableParser).!)
	lazy val variablesParser = P(((!"$").rep ~ (variableParser ~ Index)).rep)*/

	def parseVariable(text: String): Seq[String] = {
		val builder = new ArrayBuffer[String]()
		for(str <- variableParser.findAllIn(text)) {
			builder.+=:(str)
		}
		builder.distinct
	}

	def parseAddresses(servers: String): Seq[(String, Option[Int])] = {
		val parse: Parsed[Seq[(String, Option[Int])], Char, String] = serversParser.parse(servers)
		parse.fold(
			onFailure =
				(parser: Parser[_, Char, String], index: Int, reason: Parsed.Failure.Extra[Char, String])
				=> throw new Exception(s"parse addresses error: $servers"),
			onSuccess =
				(value: Seq[(String, Option[Int])], index: Int) => value
		)
	}

	def parseTime(time: String): Long = {
		val parse: Parsed[(Long, String), Char, String] = timeParser.parse(time)
		parse.fold(
			onFailure =
				(parser: Parser[_, Char, String], index: Int, reason: Parsed.Failure.Extra[Char, String])
				=> throw new Exception(s"parse time error: $time"),
			onSuccess =
				(value: (Long, String), index: Int) => value match {
					case (num, unit) => unit match {
						case "ms" | "MS" => num
						case "s" | "S" => num * 1000
						case "m" | "M" | "min" => num * 60 * 1000
						case "h" | "H" => num * 60 * 60 * 1000
						case _ => throw new Exception(s"unknown time unit: $unit")
					}
				}
		)

	}

	def parseProperties(properties: String): Seq[(String, String)] = {
		val parse: Parsed[Seq[(String, String)], Char, String] = propertiesParser.parse(properties)
		parse.fold(
			onFailure =
				(parser: Parser[_, Char, String], index: Int, reason: Parsed.Failure.Extra[Char, String])
				=> throw new Exception(s"parse table properties error: $properties"),
			onSuccess =
				(value: Seq[(String, String)], index: Int) => value
		)
	}

}
