package moonbox.catalyst.adapter.mongo.function

object UDFunctions {
  /** new in version 2.4 */
  def geoNear(x: Double,
              y: Double,
              spherical: Boolean = false,
              limit: Int = 100,
              num: Int = 100,
              maxDistance: Number = null,
              query: String = null,
              distanceMultiplier: Number = null,
              uniqueDocs: Boolean = false,
              distanceField: String,
              includeLocs: String,
              minDistance: Number
             ): Boolean = true

  /** new in version 3.2 */
  def indexStats(): Boolean = true

}
