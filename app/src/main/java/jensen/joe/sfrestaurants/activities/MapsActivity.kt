package jensen.joe.sfrestaurants.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.Places
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
        RestaurantMapView, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private val presenter: RestaurantMapPresenter = RestaurantMapPresenterImpl(this)
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var mGeoDataClient: GeoDataClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mGeoDataClient = Places.getGeoDataClient(this, null)
        checkGooglePlayServices()
        buildGoogleApiClient()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        presenter.onMapReady()

        /*
        mMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }
        */

        mMap.setOnCameraIdleListener {
            presenter.onCameraIdle()
        }

        findViewById<Button>(R.id.search_this_area_button).setOnClickListener {
            presenter.onSearchThisAreaClicked(mMap.cameraPosition.target)
        }
    }

    override fun showSearchInAreaButton(show: Boolean) {
        findViewById<Button>(R.id.search_this_area_button).visibility = if (show) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun addMarker(position: LatLng, title: String) {
        mMap.addMarker(MarkerOptions().position(position).title(title))
    }

    override fun moveCamera(position: LatLng, zoom: Float) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom))
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        return presenter.onMarkerClick(p0)
    }

    @Synchronized
    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        mGoogleApiClient.connect()
    }

    // todo in presenter
    private fun checkGooglePlayServices(): Boolean {
        val googleAPI = GoogleApiAvailability.getInstance()
        val result = googleAPI.isGooglePlayServicesAvailable(this)
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show()
            }
            return false
        }
        return true
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.i("JOE", "connection failed")
    }

    override fun onConnected(p0: Bundle?) {
        Log.i("JOE", "connected!")
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.i("JOE", "connection suspended")
    }

    override fun getGoogleApiKey(): String {
        return getString(R.string.google_maps_key)
    }

}
