package jensen.joe.sfrestaurants.models.place

data class Photo(val height: Int,
                 val html_attributions: List<String>,
                 val photo_reference: String,
                 val width: Int)

// todo use picasso to download photos