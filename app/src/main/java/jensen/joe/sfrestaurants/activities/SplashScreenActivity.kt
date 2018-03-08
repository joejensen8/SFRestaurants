package jensen.joe.sfrestaurants.activities

import android.content.Intent
import android.os.Bundle


class SplashScreenActivity: AbstractActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moveToMapActivity()
    }

    private fun moveToMapActivity() {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
        finish()
    }

}