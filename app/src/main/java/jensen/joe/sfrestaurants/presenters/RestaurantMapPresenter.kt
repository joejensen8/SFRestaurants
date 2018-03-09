package jensen.joe.sfrestaurants.presenters

import com.google.android.gms.maps.model.Marker

interface RestaurantMapPresenter {

    fun onMapReady()

    fun onMarkerClick(marker: Marker?): Boolean

}