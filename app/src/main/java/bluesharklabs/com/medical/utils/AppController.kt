
package bluesharklabs.com.medical.utils

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.google.firebase.crashlytics.FirebaseCrashlytics


class AppController : MultiDexApplication() {

    internal var myContext: Context? = null

    val appContext: Context
        get() {

            if (myContext != null) {
                return myContext as Context
            } else {
                myContext = this
                return myContext as AppController
            }
        }

    override fun onTerminate() {
        super.onTerminate()
    }

    override fun onCreate() {
        super.onCreate()
      //  Fabric.with(this, Crashlytics())
        val crashlytics = FirebaseCrashlytics.getInstance()
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
// To log a message to a crash report, use the following syntax:
        crashlytics.log("E/TAG: my message")
        instance = this
        myContext=this
        MultiDex.install(this)
      //  PhonePe.init(this)
    }



    /*@Synchronized
    fun getInstance(): AppController? {
        return mInstance
    }*/
    companion object {

        @get:Synchronized
        var instance: AppController? = null
            private set
    }
}