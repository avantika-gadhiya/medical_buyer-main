package bluesharklabs.com.medical.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        fun GettingMiliSeconds(Date: String?): Long? {
            var timeInMilliseconds: Long = 0
            val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US)
            try {
                val mDate = sdf.parse(Date)
                timeInMilliseconds = mDate.time
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return timeInMilliseconds
        }
    }
}