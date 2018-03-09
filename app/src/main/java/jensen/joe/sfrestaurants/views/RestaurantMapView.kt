package jensen.joe.sfrestaurants.views

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.VisibleRegion

interface RestaurantMapView {

    fun addMarker(position: LatLng, title: String)

    fun clearMarkers()

    fun moveCamera(position: LatLng, zoom: Float)

    fun getGoogleApiKey(): String

    fun showSearchInAreaButton(show: Boolean)

    fun getZoomLevel(): Float

    fun getVisibleMapRegion(): VisibleRegion

    fun getCameraLocation(): LatLng

    fun showMarkerDetail(title: String, description: String, address: String, placeID: String)

    fun showDetailView(place: String)

    fun dismissBottomDetail()

    fun setDetailTitle(title: String)

    fun setDetailHours(hours: String)

    fun setOpenNow(openNow: String)

    fun setDetailAddress(address: String)

    fun setDetailImage(url: String)

}