package jensen.joe.sfrestaurants.views

import com.google.android.gms.maps.model.LatLng

interface RestaurantMapView {

    fun addMarker(position: LatLng, title: String)

    fun moveCamera(position: LatLng, zoom: Float)

}