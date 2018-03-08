package jensen.joe.sfrestaurants.activities

import android.content.Intent
import android.os.Bundle


class SplashScreenActivity: AbstractActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // do any needed logic before landing on login screen
        moveToMapActivity()
    }

    private fun moveToMapActivity() {
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
        finish()
    }

}