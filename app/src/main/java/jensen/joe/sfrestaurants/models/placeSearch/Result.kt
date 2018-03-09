package jensen.joe.sfrestaurants.models.placeSearch

import jensen.joe.sfrestaurants.models.Geometry
import jensen.joe.sfrestaurants.models.OpeningHours
import jensen.joe.sfrestaurants.models.Photo

data class Result(val geometry: Geometry,
                  val icon: String,
                  val id: String,
                  val name: String,
                  val opening_hours: OpeningHours,
                  val photos: List<Photo>,
                  val place_id: String,
                  val rating: Double,
                  val reference: String,
                  val scope: String,
                  val types: List<String>,
                  val vicinity: String,
                  val price_level: Int)