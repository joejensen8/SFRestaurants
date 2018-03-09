package jensen.joe.sfrestaurants.presenters

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

interface RestaurantMapPresenter {

    fun onMapReady()

    fun onMarkerClick(marker: Marker?): Boolean

    fun onCameraIdle()

    fun onSearchThisAreaClicked(latLng: LatLng)

    fun moreInfoClicked(placeID: String)

}