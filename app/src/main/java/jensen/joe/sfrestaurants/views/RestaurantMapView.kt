package jensen.joe.sfrestaurants.views

import com.google.android.gms.maps.model.LatLng

interface RestaurantMapView {

    fun addMarker(position: LatLng, title: String)

    // fun clearMarker

    fun moveCamera(position: LatLng, zoom: Float)

    fun getGoogleApiKey(): String

    fun showSearchInAreaButton(show: Boolean)

}