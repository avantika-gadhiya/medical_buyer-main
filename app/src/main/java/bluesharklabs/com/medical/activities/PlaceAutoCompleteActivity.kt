package bluesharklabs.com.medical.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import bluesharklabs.com.medical.R
import com.google.android.gms.maps.SupportMapFragment
import android.location.Geocoder
import androidx.core.app.ActivityCompat
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.activity_place_auto_complete.*
import java.io.IOException
import java.util.*
import android.location.LocationManager
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

class PlaceAutoCompleteActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener,
    GoogleMap.OnMyLocationClickListener {


    private lateinit var mMap: GoogleMap
    var onCameraIdleListener: GoogleMap.OnCameraIdleListener? = null
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView()
            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//  set status text dark

        setContentView(R.layout.activity_place_auto_complete)


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        btn_add.setOnClickListener(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    fun statusCheck() {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()

        }
    }

    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, id ->
                    startActivityForResult(
                        Intent(
                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
                        ), 1
                    )
                })
            .setNegativeButton("No",
                DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        val alert = builder.create()
        alert.show()
    }

    private fun configureCameraIdle() {

        Log.d("tag", "mapReady")
        onCameraIdleListener = GoogleMap.OnCameraIdleListener {


            val latLng = mMap.cameraPosition.target
            val geocoder = Geocoder(this@PlaceAutoCompleteActivity, Locale.US)

            try {
                val addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

                if (addressList != null && addressList.size > 0) {
                    val locality = addressList[0].getAddressLine(0)
                    val country = addressList[0].countryName
                    Log.d("tag", "mapReady-->" + locality)

                    if (!country.equals(null)) {
                        if (!locality.isEmpty() && !country.isEmpty()) {
                            txt?.setText("$locality")
                        }
                    }
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {

        mMap = googleMap!!

        // mMap.uiSettings.isZoomControlsEnabled = true
        //   mMap.uiSettings.isMyLocationButtonEnabled = true

        setUpMap()
        configureCameraIdle()
        mMap.setOnCameraIdleListener(onCameraIdleListener)
        mMap.setOnMyLocationClickListener(this)


    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private fun setUpMap() {

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        statusCheck()
        mMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 18f))
            }
        }
    }

    override fun onMyLocationClick(p0: Location) {
        setUpMap()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.btn_add -> {
                //111

                val data = Intent()

                data.putExtra("address", txt.text.toString().trim())

                setResult(111, data)
                super.finish()

                /* startActivity(
                     Intent(
                         this@PlaceAutoCompleteActivity,
                         OrderingForActivity::class.java
                     )
                 )
                 //finish()
                 overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)*/
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            setUpMap()
            // configureCameraIdle()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //  super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (permissions.size == 1 &&
                permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                mMap.setMyLocationEnabled(true);


            } else {

                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show()
                // Permission was denied. Display an error message.
            }

        }
    }
}
