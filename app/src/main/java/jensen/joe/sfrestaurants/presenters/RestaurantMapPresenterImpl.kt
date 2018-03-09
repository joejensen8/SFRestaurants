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

    private var markers: MutableMap<LatLng, Result>? = HashMap()
    private var lastCameraLocation: LatLng? = null

    override fun onMapReady() {
        //view.addMarker(Constants.sanFranLatLng, "San Fran!")
        view.moveCamera(Constants.sanFranLatLng, 15.0f)
        getRestaurants(Constants.sanFranLatLng)
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        val result = markers?.get(marker?.position)
        if (result != null) {
            val title = result.name
            val rating = result.rating.toString() + " stars"
            val price = getPriceSymbol(result.price_level)
            val address = result.vicinity
            val description = "$rating  -  $price"
            view.showMarkerDetail(title, description, address, result.place_id)
        }
        return false
    }

    private fun getPriceSymbol(price: Int): String {
        val builder = StringBuilder(4)
        for (i in 0..price) {
            builder.append('$')
        }
        return builder.toString()
    }

    override fun onCameraIdle() {
        val currCameraLocation = view.getCameraLocation()
        val lastCameraLoc = lastCameraLocation
        if (lastCameraLoc != null) {
            val radius = getRadius(lastCameraLoc, currCameraLocation)
            if (radius > 5000.0) {
                view.showSearchInAreaButton(true)
            }
        } else {
            lastCameraLocation = view.getCameraLocation()
        }
    }

    override fun onSearchThisAreaClicked(latLng: LatLng) {
        getRestaurants(latLng)
        view.showSearchInAreaButton(false)
    }

    override fun moreInfoClicked(placeID: String) {
        getPlaceDetail(placeID)
    }

    private fun getRestaurants(latLng: LatLng) {
        markers = HashMap()
        view.clearMarkers()
        val vr = view.getVisibleMapRegion()
        val calcRadius = getRadius(vr.farLeft, vr.farRight)
        val radius = if (calcRadius <= 1) { 500.0 } else { calcRadius }
        GooglePlacesService.create().getPlaces(getLatLngParam(latLng), "restaurant", radius.toString(), view.getGoogleApiKey())
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

    private fun getRadius(p1: LatLng, p2: LatLng): Double {
        val results = FloatArray(3)
        Location.distanceBetween(p1.latitude, p1.longitude, p2.latitude, p2.longitude, results)
        return results[0].toDouble()
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
            markers?.put(position, result)
            view.addMarker(position, result.name)
        }
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