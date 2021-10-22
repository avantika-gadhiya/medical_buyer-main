package bluesharklabs.com.medical.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.core.view.GravityCompat
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import android.view.Gravity
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.fragments.DashBoardFragment
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RatingBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.loginactivities.LoginActivity
import bluesharklabs.com.medical.model.OrderList
import bluesharklabs.com.medical.response.GetColorCodeResponse
import bluesharklabs.com.medical.response.OrderListResponse
import bluesharklabs.com.medical.utils.AppConstant
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.Target
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.drawer_layout.img_profile
import kotlinx.android.synthetic.main.drawer_layout.txt_logout
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var doubleBackToExitOnce:Boolean = false
    var listColorCode: List<GetColorCodeResponse.Datum> = arrayListOf()
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var geocoder: Geocoder? = null
    val PERMISSION_ID = 42
    private var current_latitude=""
    private var current_longitude=""
    private lateinit var mBroadcastManager: LocalBroadcastManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView()
            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//  set status text dark

        setContentView(R.layout.activity_main)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geocoder = Geocoder(this@MainActivity, Locale.US)

        getColorCodeListApi()

        img_menu.setOnClickListener(this)
        closeDrawer.setOnClickListener(this)
        txt_home.setOnClickListener(this)
        img_notification.setOnClickListener(this)
        txt_notfcation.setOnClickListener(this)
        txt_my_profile.setOnClickListener(this)
        txt_my_presc.setOnClickListener(this)
        txt_my_orders.setOnClickListener(this)
        txt_consult_my_dr.setOnClickListener(this)
        txt_check_avail_stor.setOnClickListener(this)
        txt_rate_us.setOnClickListener(this)
        txt_send_feedback.setOnClickListener(this)
        txt_terms_conditions.setOnClickListener(this)
        txt_search_product_info.setOnClickListener(this)
        txt_refr_to_frnds_n_contacs.setOnClickListener(this)
        txt_invite_store_to_reg.setOnClickListener(this)
        txt_contact_us.setOnClickListener(this)
        txt_logout.setOnClickListener(this)


        Glide.with(applicationContext)  //2
            .load(AppConstant.getPreferenceText(AppConstant.PREF_USER_PROFILE_IMAGE)) //3
            .centerCrop() //4
            .placeholder(R.drawable.ic_launcher_background) //5
            .error(R.drawable.ic_launcher_background) //6
            .fallback(R.drawable.ic_launcher_background) //7
            .into(img_profile) //8


        try {
            Glide.with(this@MainActivity)
                    .load(AppConstant.getPreferenceText(AppConstant.PREF_USER_PROFILE_IMAGE))
                    .listener(object : com.bumptech.glide.request.RequestListener<Drawable> {
                        override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                    }).placeholder(R.drawable.ic_launcher_background)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(img_profile)

//                                 Glide.with(this@MyProfileActivity)  //2
//                                .load(response.body()?.getData()?.profilePic) //3
//                                .error(R.drawable.ic_launcher_background) //6
//                                .into(imageView2) //8


        } catch (e: Exception) {
            Log.e("HElloPicError", "E   " + e)
        }


        user_name.setText(AppConstant.getPreferenceText(AppConstant.PREF_USER_NAME))
        user_number.setText("+91 "+AppConstant.getPreferenceText(AppConstant.PREF_USER_PHONE))


        mBroadcastManager = LocalBroadcastManager.getInstance(this)
        mBroadcastManager.registerReceiver(mReceiverLocationResult, IntentFilter(resources.getString(R.string.broadcastLocationResult)))



        getLastLocation()
        clickDashboard()


    }

    /*
   *  Receiver which get the data of Latitude and Longitude
   */
    private val mReceiverLocationResult: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {



            val latitude = intent.getDoubleExtra(resources.getString(R.string.bundleLocationLatitude),0.0)
            val longitude = intent.getDoubleExtra(resources.getString(R.string.bundleLocationLongitude),0.0)




            current_latitude= latitude.toString()
            current_longitude= longitude.toString()

            Log.e("HelloBroad","latitude   "+current_latitude+"   longitude   "+current_longitude)

        }
    }

    private fun clickDashboard() {

        val newFragment = DashBoardFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_frame, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }
    private fun getColorCodeListApi() {
       // progressBar.visibility = View.VISIBLE
        val apiService = AppConstant.getClient()!!.create(RetrofitInterface::class.java)

        val call = apiService.getcolorcode()

        call.enqueue(object : Callback<GetColorCodeResponse> {
            override fun onResponse(
                call: Call<GetColorCodeResponse>,
                response: Response<GetColorCodeResponse>
            ) {

              //  progressBar.visibility = View.GONE
                if (response.isSuccessful()) {
                    listColorCode = arrayListOf()

                    listColorCode = response.body()!!.data!!



                    for (i in 0 until listColorCode.size) {
                        //  var list = listPhoneCode.get(i).code
                        colorList?.add("#" + listColorCode.get(i).code.toString())
                    }


                }
            }

            override fun onFailure(call: Call<GetColorCodeResponse>, t: Throwable) {
               // progressBar.visibility = View.GONE
            }
        })
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_menu -> {
                drawerLayout!!.openDrawer(GravityCompat.START)
            }
            R.id.closeDrawer -> {
                drawerLayout!!.closeDrawer(GravityCompat.START)
            }
            R.id.txt_home -> {
                startActivity(
                    Intent(
                        this@MainActivity,
                        MainActivity::class.java
                    )
                )
            }
            R.id.txt_notfcation -> {
                startActivity(
                    Intent(
                        this@MainActivity,
                        NotificationsActivity::class.java
                    )
                )
            }
            R.id.img_notification -> {
                startActivity(
                    Intent(
                        this@MainActivity,
                        NotificationsActivity::class.java
                    )
                )
            }
            R.id.txt_my_profile -> {
                startActivity(
                    Intent(
                        this@MainActivity,
                        MyProfileActivity::class.java
                    )
                )
            }
            R.id.txt_my_presc -> {
                startActivity(
                    Intent(
                        this@MainActivity,
                        MyPrescriptionsActivity::class.java
                    )
                )
            }
            R.id.txt_my_orders -> {
                startActivity(
                    Intent(
                        this@MainActivity,
                        MyOrdersActivity::class.java
                    )
                )
            }
            R.id.txt_consult_my_dr -> {
                startActivity(
                    Intent(
                        this@MainActivity,
                        ConsultMyDoctorActivity::class.java
                    )
                )
            }
            R.id.txt_check_avail_stor -> {
                startActivity(
                    Intent(
                        this@MainActivity,
                        FindStoreActivity::class.java
                    )
                        .putExtra("availstore", "1")
                )
            }
            R.id.txt_rate_us -> {
                drawerLayout!!.closeDrawer(GravityCompat.START)
                showPopup()
            }
            R.id.txt_send_feedback -> {
                startActivity(
                    Intent(
                        this@MainActivity,
                        SliderSendFeedbackActivity::class.java
                    )
                )
            }
            R.id.txt_terms_conditions -> {
                startActivity(
                    Intent(
                        this@MainActivity,
                        TermsConditionsActivity::class.java
                    )
                )
            }
            R.id.txt_search_product_info -> {
                drawerLayout!!.closeDrawer(GravityCompat.START)
                val openURL = Intent(android.content.Intent.ACTION_VIEW)
                openURL.data = Uri.parse("https://www.mims.com/")
                startActivity(openURL)
            }
            R.id.txt_refr_to_frnds_n_contacs -> {
                drawerLayout!!.closeDrawer(GravityCompat.START)
                shareLink()
            }
            R.id.txt_invite_store_to_reg -> {
                drawerLayout!!.closeDrawer(GravityCompat.START)
                shareLinkForStore()
            }
            R.id.txt_contact_us -> {
                startActivity(
                    Intent(
                        this@MainActivity,
                        ContactUsActivity::class.java
                    )
                )
            }
            R.id.txt_logout -> {
                logOut()
            }
        }
    }


    fun logOut() {
        val builder = AlertDialog.Builder(this)

        // Set the alert dialog title
        //builder.setTitle("App background color")

        // Display a message on alert dialog
        builder.setMessage("Do you want to Logout from this app?")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("Yes") { dialog, which ->

            clearPref()
            // Do something when user press the positive button
            startActivity(
                Intent(
                    this@MainActivity,
                    LoginActivity::class.java
                )
            )
            finish()
        }
        builder.setNegativeButton("No") { dialog, which ->
            // Do something when user press the positive button
            dialog.dismiss()
        }

        // Display a negative button on alert dialog


        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()

    }

    fun  clearPref(){
        AppConstant.setPreferenceBoolean(AppConstant.IS_LOGIN, false)
        AppConstant.remove(AppConstant.PREF_USER_PROFILE_IMAGE)
        AppConstant.remove(AppConstant.PREF_USER_NAME)
        AppConstant.remove(AppConstant.PREF_USER_ID)
        AppConstant.remove(AppConstant.PREF_USER_PHONE)
    }
    fun shareLink(){


       /* val share = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "https://developer.android.com/training/sharing/")

            // (Optional) Here we're setting the title of the content
            putExtra(Intent.EXTRA_TITLE, "Introducing content previews")

            // (Optional) Here we're passing a content URI to an image to be displayed
          //  data = contentUri
       //     flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }, null)
        startActivity(share)*/




        val share = Intent(android.content.Intent.ACTION_SEND)
        share.type = "text/plain"

        share.putExtra(Intent.EXTRA_SUBJECT, "Link for Buyer App will be sent")
        share.putExtra(Intent.EXTRA_TEXT, "http://www.codeofaninja.com")
      //  share.putExtra(Intent.EXTRA_TITLE, "Introducing content previews")
        startActivity(Intent.createChooser(share, "Refer Friends & Contacts"))
    }
    fun shareLinkForStore(){
        val share = Intent(android.content.Intent.ACTION_SEND)
        share.type = "text/plain"

        share.putExtra(Intent.EXTRA_SUBJECT, "Link for Store App will be sent")
        share.putExtra(Intent.EXTRA_TEXT, "http://www.codeofaninja.com")

        startActivity(Intent.createChooser(share, "Invite Store to Register"))
    }

    fun showPopup() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.popup_rate_app)

        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.getWindow().getAttributes())
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.NO_GRAVITY
        //  lp.windowAnimations = R.style.DialogAnimation
        dialog.window!!.attributes = lp
        val img_close = dialog.findViewById(R.id.img_close) as AppCompatImageView?
        val btn = dialog.findViewById(R.id.btn) as AppCompatButton?
        val rBar = dialog.findViewById(R.id.rBar) as RatingBar?

        img_close!!.setOnClickListener {
            dialog.dismiss()
        }
        btn!!.setOnClickListener {

            dialog.dismiss()
            val msg = rBar!!.rating.toString()
            Toast.makeText(
                this@MainActivity,
                "Rating is: " + msg, Toast.LENGTH_SHORT
            ).show()

        }

        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    override fun onBackPressed() {
        if(doubleBackToExitOnce){
            // super.onBackPressed()
            moveTaskToBack(true)
        }

        this.doubleBackToExitOnce = true

        //displays a toast message when user clicks exit button
        //toast("please press again to exit ").show()

        Toast.makeText(this,"please press again to exit",Toast.LENGTH_SHORT).show()

        //displays the toast message for a while
        Handler().postDelayed({
            kotlin.run { doubleBackToExitOnce = false }
        }, 2000)

    }



    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return
                }

                mFusedLocationClient!!.lastLocation.addOnSuccessListener { location: Location? ->

                    if (location == null) {
                        requestNewLocationData()
                    } else {

                        current_latitude= location.latitude.toString()
                        current_longitude= location.longitude.toString()

                        Log.d("TAG","currentlat_mainnn"+current_latitude)
                        Log.d("TAG","currentlong_mainnnn"+current_longitude)


                        mBroadcastManager.sendBroadcast(
                                Intent(resources.getString(R.string.broadcastLocationResult))
                                        .putExtra(
                                                resources.getString(R.string.bundleLocationLatitude),
                                                location?.latitude
                                        )
                                        .putExtra(
                                                resources.getString(R.string.bundleLocationLongitude),
                                                location?.longitude
                                        )
                        )
                    }

                }

            } else {
//                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
//                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        mFusedLocationClient!!.requestLocationUpdates(
//                mLocationRequest, mLocationCallback,
//                Looper.myLooper()
//        )

        mFusedLocationClient!!.lastLocation.addOnSuccessListener { location: Location? ->

//            current_latitude= location?.latitude.toString()
//            current_longitude= location?.longitude.toString()

            mBroadcastManager.sendBroadcast(
                    Intent(resources.getString(R.string.broadcastLocationResult))
                            .putExtra(
                                    resources.getString(R.string.bundleLocationLatitude),
                                    location?.latitude
                            )
                            .putExtra(
                                    resources.getString(R.string.bundleLocationLongitude),
                                    location?.longitude
                            )
            )

            // Got last known location. In some rare situations this can be null.
        }
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation

            current_latitude= mLastLocation.latitude.toString()
            current_longitude= mLastLocation.longitude.toString()

            Log.d("TAG","currentlat_mainnn"+current_latitude)
            Log.d("TAG","currentlong_mainnnn"+current_longitude)
        }
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        )
    }
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    companion object{
        var colorList: ArrayList<String>? = arrayListOf()
        var current_latitude=""
        var current_longitude=""
    }
}