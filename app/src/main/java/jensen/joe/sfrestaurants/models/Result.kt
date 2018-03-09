package jensen.joe.sfrestaurants.models

data class Result(val geometry: Geometry,
                  val icon: String,
                  val id: String,
                  val name: String,
                  val opening_hours: OpeningHours,
                  val photos: List<Photo>,
                  val placeId: String,
                  val rating: Double,
                  val reference: String,
                  val scope: String,
                  val types: List<String>,
                  val vicinity: String,
                  val price_level: Int)