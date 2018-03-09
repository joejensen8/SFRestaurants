package jensen.joe.sfrestaurants.presenters

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

class RestaurantMapPresenterImpl(private val view: RestaurantMapView,
                                 private val googleApiKey: String): RestaurantMapPresenter {

    override fun onMapReady() {
        view.addMarker(Constants.sanFranLatLng, "San Fran!")
        view.moveCamera(Constants.sanFranLatLng, 12.0f)
        getRestaurants()
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        return false
    }

    private fun getRestaurants() {
        // just getting in san fran right now
        val location = "37.7749,-122.4194"
        //val location: String = Constants.sanFranLatLng.toString() + "," + Constants.sanFranciscoLongitude.toString()

        GooglePlacesService.create().getPlaces(location, "restaurant", "5000", googleApiKey)
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

    private fun processResults(response: Example) {
        for (result: Result in response.results) {
            val position = LatLng(result.geometry.location.lat,
                    result.geometry.location.lng)
            view.addMarker(position, result.name)
        }
    }

}