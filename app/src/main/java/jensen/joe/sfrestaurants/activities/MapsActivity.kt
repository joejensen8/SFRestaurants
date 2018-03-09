package jensen.joe.sfrestaurants.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
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
import com.google.android.gms.maps.model.VisibleRegion
import jensen.joe.sfrestaurants.R
import jensen.joe.sfrestaurants.common.Constants
import jensen.joe.sfrestaurants.fragments.PlaceDetailFragment
import jensen.joe.sfrestaurants.presenters.MapPresenter
import jensen.joe.sfrestaurants.presenters.MapPresenterImpl
import jensen.joe.sfrestaurants.views.RestaurantMapView


class MapsActivity : AbstractActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        RestaurantMapView, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private val presenter: MapPresenter = MapPresenterImpl(this)
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

        //requestLocationPermission()

        mGeoDataClient = Places.getGeoDataClient(this, null)
        checkGooglePlayServices()
        buildGoogleApiClient()
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // todo show rationale for requesting permission
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    Constants.LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            // Permission has already been granted
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        presenter.onMapReady()

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
        mMap.addMarker(MarkerOptions().position(position)
                .title(title))
    }

    override fun moveCamera(position: LatLng, zoom: Float) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom))
    }

    // todo some sort of caching system?
    override fun clearMarkers() {
        mMap.clear()
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        return presenter.onMarkerClick(p0)
    }

    override fun getZoomLevel(): Float {
        return mMap.cameraPosition.zoom
    }

    // todo in presenter
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

    }

    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun getGoogleApiKey(): String {
        return getString(R.string.google_maps_key)
    }

    override fun getVisibleMapRegion(): VisibleRegion {
        return mMap.projection.visibleRegion
    }

    override fun getCameraLocation(): LatLng {
        return mMap.cameraPosition.target
    }

    override fun showMarkerDetail(title: String, description: String, address: String, placeID: String) {
        val desc = findViewById<View>(R.id.place_information)
        desc.findViewById<TextView>(R.id.marker_title).text = title
        desc.findViewById<TextView>(R.id.marker_details).text = description
        desc.findViewById<TextView>(R.id.marker_address).text = address

        // todo animate

        if (desc.visibility == View.GONE) {
            val animation = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_up)
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}

                override fun onAnimationEnd(animation: Animation) {
                    desc.visibility = View.VISIBLE
                    desc.bringToFront()
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
            desc.startAnimation(animation)
        }

        desc.findViewById<Button>(R.id.marker_dismiss).setOnClickListener {
            // todo animation not working
            val anim = AnimationUtils.loadAnimation(this, R.anim.slide_top_down)
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}

                override fun onAnimationEnd(animation: Animation) {
                    desc.visibility = View.GONE
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
            desc.startAnimation(anim)
        }
        desc.findViewById<Button>(R.id.marker_more_info).setOnClickListener {
            presenter.moreInfoClicked(placeID)
        }
    }

    override fun moveToDetailView(place: String) {
        val fragToAdd = PlaceDetailFragment()
        val args = Bundle()
        args.putString("placeID", place)
        fragToAdd.arguments = args
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(fragToAdd, Constants.PLACE_DETAIL_FRAGMENT_TAG)
        transaction.addToBackStack(Constants.PLACE_DETAIL_FRAGMENT_TAG)
        transaction.commit()
    }

}
