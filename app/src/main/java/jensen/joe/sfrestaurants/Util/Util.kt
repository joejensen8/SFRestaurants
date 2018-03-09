package jensen.joe.sfrestaurants.Util

import java.util.*

object Util {

    fun getDayOfWeek(): String {
        val calendar: Calendar = Calendar.getInstance()
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
    }

}