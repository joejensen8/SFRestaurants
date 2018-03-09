package jensen.joe.sfrestaurants.services

import jensen.joe.sfrestaurants.models.Example
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// https://maps.googleapis.com/maps/api/place/nearbysearch/output?parameters

interface GooglePlacesService {

    //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670,151.1957&radius=500&types=food&name=cruise&key=YOUR_API_KEY

    @GET("maps/api/place/nearbysearch/json")
    fun getPlaces(@Query("location") location: String,
                  @Query("types") type: String,
                  @Query("radius") radius: String,
                  @Query("key") key: String):
            Call<Example>

    /*
    @GET("maps/api/place/details/json")
    fun getPlaceDetails(@Query("location") location: String,
                  @Query("types") type: String,
                  @Query("radius") radius: String,
                  @Query("key") key: String):
            Call<Example>*/

    companion object {
        fun create(): GooglePlacesService {
            val retrofit = Retrofit.Builder()
                    .addConverterFactory(
                            GsonConverterFactory.create())
                    .baseUrl("https://maps.googleapis.com/")
                    .build()

            return retrofit.create(GooglePlacesService::class.java)
        }
    }

}
