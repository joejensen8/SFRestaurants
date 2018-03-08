package jensen.joe.sfrestaurants.presenters

import jensen.joe.sfrestaurants.common.Constants
import jensen.joe.sfrestaurants.views.RestaurantMapView

class RestaurantMapPresenterImpl(private val view: RestaurantMapView): RestaurantMapPresenter {

    fun getRestaurants() {
        // just getting in san fran right now
    }

    override fun onMapReady() {
        view.addMarker(Constants.sanFranLatLng, "San Fran!")
        view.moveCamera(Constants.sanFranLatLng, 12.0f)
    }

}