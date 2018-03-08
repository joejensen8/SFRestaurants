package jensen.joe.sfrestaurants.activities

import android.os.Bundle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import jensen.joe.sfrestaurants.R
import jensen.joe.sfrestaurants.presenters.RestaurantMapPresenter
import jensen.joe.sfrestaurants.presenters.RestaurantMapPresenterImpl
import jensen.joe.sfrestaurants.views.RestaurantMapView

class MapsActivity : AbstractActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        RestaurantMapView {

    private val presenter: RestaurantMapPresenter = RestaurantMapPresenterImpl(this)
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        presenter.onMapReady()
    }

    override fun addMarker(position: LatLng, title: String) {
        mMap.addMarker(MarkerOptions().position(position).title(title))
    }

    override fun moveCamera(position: LatLng, zoom: Float) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom))
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        return false
    }

}
