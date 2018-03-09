package jensen.joe.sfrestaurants.services

import android.graphics.Bitmap
import jensen.joe.sfrestaurants.models.place.detail.Detail
import jensen.joe.sfrestaurants.models.place.search.Example
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

    @GET("maps/api/place/details/json")
    fun getPlaceDetails(@Query("placeid") placeid: String,
                        @Query("key") key: String):
            Call<Detail>

    @GET("maps/api/place/photo")
    fun getPlacePhoto(@Query("photoreference") photoreference: String,
                      @Query("key") key: String,
                      @Query("maxheight") maxheight: String,
                      @Query("maxwidth") maxwidth: String):
            Call<Bitmap>

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
