package jensen.joe.sfrestaurants.models

data class Example(val html_attributions: List<Any>,
                   val next_page_token: String,
                   val results: List<Result>,
                   val status: String)