package jensen.joe.sfrestaurants.services

import jensen.joe.sfrestaurants.models.placeDetail.Detail
import jensen.joe.sfrestaurants.models.placeSearch.Example
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesService {

    @GET("maps/api/place/nearbysearch/json")
    fun getPlaces(@Query("location") location: String,
                  @Query("types") type: String,
                  @Query("radius") radius: String,
                  @Query("key") key: String):
            Call<Example>

    //https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJN1t_tDeuEmsRUsoyG83frY4&key=YOUR_API_KEY

    @GET("maps/api/place/details/json")
    fun getPlaceDetails(@Query("placeid") placeid: String,
                  @Query("key") key: String):
            Call<Detail>

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
