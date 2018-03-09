package jensen.joe.sfrestaurants.utils

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import java.util.*

fun getRadius(p1: LatLng, p2: LatLng): Double {
    val results = FloatArray(3)
    Location.distanceBetween(p1.latitude, p1.longitude, p2.latitude, p2.longitude, results)
    return results[0].toDouble()
}