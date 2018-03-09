package jensen.joe.sfrestaurants.models.place

data class OpeningHours(val open_now: Boolean,
                        val weekday_text: List<Any>)