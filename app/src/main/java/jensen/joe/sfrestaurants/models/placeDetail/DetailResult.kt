package jensen.joe.sfrestaurants.models.placeDetail

import jensen.joe.sfrestaurants.models.Geometry
import jensen.joe.sfrestaurants.models.OpeningHours
import jensen.joe.sfrestaurants.models.Photo

data class DetailResult(val address_components: List<AddressComponent>,
                        val adr_address: String,
                        val formatted_address: String,
                        val formatted_phone_number: String,
                        val geometry: Geometry,
                        val icon: String,
                        val id: String,
                        val international_phone_number: String,
                        val name: String,
                        val opening_hours: OpeningHours,
                        val photos: List<Photo>,
                        val place_id: String,
                        val rating: Double,
                        val reference: String,
                        val reviews: List<Review>,
                        val scope: String,
                        val types: List<String>,
                        val url: String,
                        val utc_offset: Int,
                        val vicinity: String,
                        val website: String)