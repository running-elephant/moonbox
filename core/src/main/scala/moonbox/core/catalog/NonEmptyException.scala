package moonbox.core.catalog

class NonEmptyException(message: String)
	extends Exception(s"$message is not empty. Please clear it first or use CASCADE keyword.")
