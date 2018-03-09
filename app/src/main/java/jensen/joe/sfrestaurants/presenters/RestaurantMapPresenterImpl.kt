package jensen.joe.sfrestaurants.presenters

import android.location.Location
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import jensen.joe.sfrestaurants.common.Constants
import jensen.joe.sfrestaurants.models.placeDetail.Detail
import jensen.joe.sfrestaurants.models.placeSearch.Example
import jensen.joe.sfrestaurants.models.placeSearch.Result
import jensen.joe.sfrestaurants.services.GooglePlacesService
import jensen.joe.sfrestaurants.views.RestaurantMapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class RestaurantMapPresenterImpl(private val view: RestaurantMapView): RestaurantMapPresenter {

    private var markers: MutableMap<LatLng, String>? = HashMap()

    override fun onMapReady() {
        //view.addMarker(Constants.sanFranLatLng, "San Fran!")
        view.moveCamera(Constants.sanFranLatLng, 15.0f)
        getRestaurants(Constants.sanFranLatLng)
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        val id = markers?.get(marker?.position)
        if (id != null) {
            getPlaceDetail(id)
        }
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
        markers = HashMap()
        view.clearMarkers()
        GooglePlacesService.create().getPlaces(getLatLngParam(latLng), "restaurant", getRadius().toString(), view.getGoogleApiKey())
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

    private fun getRadius(): Double {
        val vr = view.getVisibleMapRegion()
        val results = FloatArray(3)
        Location.distanceBetween(vr.farLeft.latitude, vr.farLeft.longitude, vr.farRight.latitude, vr.farRight.longitude, results)
        val x = results[0].toDouble()
        Log.i("JOE", "radius: $x")
        return if (x <= 1) {
            500.0
        } else {
            x
        }
    }

    private fun getLatLngParam(latLng: LatLng): String {
        val builder = StringBuilder()
        builder.append(latLng.latitude).append(",").append(latLng.longitude)
        Log.i("JOE", builder.toString())
        return builder.toString()
    }

    private fun processResults(response: Example) {
        view.showSearchInAreaButton(false)
        for (result: Result in response.results) {
            val position = LatLng(result.geometry.location.lat,
                    result.geometry.location.lng)
            Log.i("JOE", "position: $position and id is " + result.place_id)
            markers?.put(position, result.place_id)
            view.addMarker(position, result.name, result.scope, result.place_id)
        }
    }

    private fun processDetailResults(response: Detail) {
        Log.i("JOE", "detail status: " + response.status)
    }

}