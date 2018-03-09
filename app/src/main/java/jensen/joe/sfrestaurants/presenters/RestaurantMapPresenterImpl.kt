package jensen.joe.sfrestaurants.presenters

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import jensen.joe.sfrestaurants.common.Constants
import jensen.joe.sfrestaurants.models.Example
import jensen.joe.sfrestaurants.models.Result
import jensen.joe.sfrestaurants.services.GooglePlacesService
import jensen.joe.sfrestaurants.views.RestaurantMapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantMapPresenterImpl(private val view: RestaurantMapView): RestaurantMapPresenter {

    override fun onMapReady() {
        view.addMarker(Constants.sanFranLatLng, "San Fran!")
        view.moveCamera(Constants.sanFranLatLng, 12.0f)
        getRestaurants(Constants.sanFranLatLng)
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        return false
    }

    override fun onCameraIdle() {
        view.showSearchInAreaButton(true)
    }

    override fun onSearchThisAreaClicked(latLng: LatLng) {
        getRestaurants(latLng)
        view.showSearchInAreaButton(false)
    }

    private fun getRestaurants(latLng: LatLng) {
        // just getting in san fran right now
        //val location: String = Constants.sanFranLatLng.toString() + "," + Constants.sanFranciscoLongitude.toString()

        GooglePlacesService.create().getPlaces(getLatLngParam(latLng), "restaurant", "5000", view.getGoogleApiKey())
                .enqueue(object : Callback<Example> {

            override fun onResponse(call: Call<Example>, response: Response<Example>) {
                val places = response.body()
                processResults(places as Example)
            }

            override fun onFailure(call: Call<Example>, t: Throwable) {
                t.printStackTrace()
            }

        })

    }

    private fun getLatLngParam(latLng: LatLng): String {
        val builder = StringBuilder()
        builder.append(latLng.latitude).append(",").append(latLng.longitude)
        Log.i("JOE", builder.toString())
        return builder.toString()
    }

    private fun processResults(response: Example) {
        for (result: Result in response.results) {
            val position = LatLng(result.geometry.location.lat,
                    result.geometry.location.lng)
            view.addMarker(position, result.name)
        }
    }

}