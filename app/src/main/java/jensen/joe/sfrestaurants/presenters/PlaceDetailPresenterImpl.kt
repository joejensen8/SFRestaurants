package jensen.joe.sfrestaurants.presenters

import android.util.Log
import jensen.joe.sfrestaurants.models.place.detail.Detail
import jensen.joe.sfrestaurants.services.GooglePlacesService
import jensen.joe.sfrestaurants.views.PlaceDetailView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceDetailPresenterImpl(private val view: PlaceDetailView): PlaceDetailPresenter {

    private fun getPlaceDetail(placeID: String) {
        GooglePlacesService.create().getPlaceDetails(placeID, view.getGoogleApiKey())
                .enqueue(object : Callback<Detail> {

            override fun onResponse(call: Call<Detail>, response: Response<Detail>) {
                val detail = response.body()
                processDetailResults(detail as Detail)
            }

            override fun onFailure(call: Call<Detail>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    private fun processDetailResults(response: Detail) {
        Log.i("JOE", "detail status: " + response.status)
        val title = response.result.name
        val rating = response.result.rating.toString() + " stars"
        val reviews = "(" + response.result.reviews.count().toString() + " reviews)"
        val address = response.result.formatted_address
        val description = "$rating - $reviews"
    }

}