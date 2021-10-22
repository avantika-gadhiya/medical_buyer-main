package bluesharklabs.com.medical.activities

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.response.GetAllCityResponse
import bluesharklabs.com.medical.response.GetAllDropdownResponse
import bluesharklabs.com.medical.utils.AppConstant
import bluesharklabs.com.medical.utils.InternalStorageContentProvider
import bluesharklabs.com.medical.utils.ScaleFile
import com.bumptech.glide.Glide
import com.google.android.gms.location.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_custom_order_preference.btn_save_n_continue
import kotlinx.android.synthetic.main.activity_custom_order_preference.constraint_store_pickup
import kotlinx.android.synthetic.main.activity_custom_order_preference.edt_area
import kotlinx.android.synthetic.main.activity_custom_order_preference.edt_landmark
import kotlinx.android.synthetic.main.activity_custom_order_preference.edt_office_flatno
import kotlinx.android.synthetic.main.activity_custom_order_preference.edt_office_flatno1
import kotlinx.android.synthetic.main.activity_custom_order_preference.edt_street_road
import kotlinx.android.synthetic.main.activity_custom_order_preference.edt_zip_code
import kotlinx.android.synthetic.main.activity_custom_order_preference.imageView3
import kotlinx.android.synthetic.main.activity_custom_order_preference.img_back
import kotlinx.android.synthetic.main.activity_custom_order_preference.img_upload_photo
import kotlinx.android.synthetic.main.activity_custom_order_preference.nested_location_delivery
import kotlinx.android.synthetic.main.activity_custom_order_preference.nested_zip_add_delivery
import kotlinx.android.synthetic.main.activity_custom_order_preference.spinner_store_pickup
import kotlinx.android.synthetic.main.activity_custom_order_preference.switch_location
import kotlinx.android.synthetic.main.activity_custom_order_preference.textView7
import kotlinx.android.synthetic.main.activity_custom_order_preference.txt_location
import kotlinx.android.synthetic.main.activity_find_platform.*
import kotlinx.android.synthetic.main.activity_order_preference.*
import kotlinx.android.synthetic.main.activity_order_preference.progressBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class OrderPreferenceActivity : AppCompatActivity(), View.OnClickListener,
        DatePickerDialog.OnDateSetListener {

    private var mFileTemp: File? = null
    private val SELECT_FILE = 1
    private var onLocationClicked = false

    var path = ""
    private val REQUEST_CAMERA = 0
    val REQUEST_CODE_TAKE_PICTURE = 2
    val REQUEST_CODE_GALLERY = 1

    val INITIAL_GALLERY = arrayOf<String>(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    )

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var geocoder: Geocoder? = null


    var drugPrefrncList: List<GetAllDropdownResponse.DrugPreference> = arrayListOf()
    var buyingPrefrncList: List<GetAllDropdownResponse.BuyingPreference> = arrayListOf()
    private var drug_list = ArrayList<String>()
    private var drugId_list = ArrayList<String>()

    var order_type = ""
    var isEditable = false
    var isCustom = false
    var isUserInteracting = false


    private var myCalendar = Calendar.getInstance()
    private var cYear = 0
    private var cMonth = 0
    private var cDay = 0
    private var from_date = ""
    private var to_date = ""
    private var from_date1 = ""
    private var to_date1 = ""
    private var from_date2 = ""
    private var to_date2 = ""
    private var from_time: Long? = null
    private var from_time1: Long? = null
    private var from_time2: Long? = null
    private var flag = 0
    private var timeFlag: Boolean? = null
    val PERMISSION_ID = 42
    private var store = 0
    private var location = 0
    private var zip = 0

    var city_name = ""

    var cityList: List<GetAllCityResponse.Datum> = arrayListOf()

    // city
    private var city_list = ArrayList<String>()

    var AUTOCOMPLETE_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR//  set status text dark

        setContentView(R.layout.activity_order_preference)


        // edt_office_flatno.setText("khkhjhjs kjjhh")
        // txt_location.setText("Your activity / container layout you wish status bar needs this property set:")
        // edt_delivery_date.setText("16 dec 2019")

        val orderTypes = arrayOf("Store Pickup", "Location Delivery", "Zip Address Delivery")
        val spinner = findViewById<Spinner>(R.id.spinner_store_pickup)

        getAllCity()
//        getAllZipCity()


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geocoder = Geocoder(this@OrderPreferenceActivity, Locale.US)

        if (intent != null) {
            if (intent.getStringExtra("edit") != null && intent.getStringExtra("edit").equals("1")) {


                isEditable = true

                order_type = delprerncetext

                setLayout()

            }
            if (intent.getBooleanExtra("custom", true)) {
                isCustom = intent.getBooleanExtra("custom", true)
            }
        } else {
            order_lat = "0"
            order_long = "0"
            turnOnLocation = "0"
        }

        edt_delivery_from_date.isFocusable = false  //location
        edt_delivery_from_date.isClickable = true   //location
        edt_delivery_from_time.isFocusable = false  //location
        edt_delivery_from_time.isClickable = true    //location

        edt_delivery_to_date.isFocusable = false  //location
        edt_delivery_to_date.isClickable = true  //location

        edt_delivery_to_time.isFocusable = false  //location
        edt_delivery_to_time.isClickable = true   //location

        edt_delivery_zip_date1.isFocusable = false  //zip add
        edt_delivery_zip_date1.isClickable = true   //zip add

        edt_delivery_zip_time1.isFocusable = false   //zip add
        edt_delivery_zip_time1.isClickable = true   //zip add

        edt_delivery_to_date1.isFocusable = false //zip add
        edt_delivery_to_date1.isClickable = true //zip add

        edt_delivery_to_time1.isFocusable = false  //zip add
        edt_delivery_to_time1.isClickable = true   // //zip add

        txt_location.isFocusable = false
        txt_location.isClickable = true

        edt_pref_to_date.isFocusable = false    //store
        edt_pref_to_date.isClickable = true     //store

        edt_picup_from_date.isFocusable = false     //store
        edt_picup_from_date.isClickable = true      //store

        edt_picup_from_time.isFocusable = false     //store
        edt_picup_from_time.isClickable = true      //store

        edt_pref_to_time.isFocusable = false      //store
        edt_pref_to_time.isClickable = true        //store

        edt_delivery_from_date.setOnClickListener(this) //location
        edt_delivery_to_date.setOnClickListener(this) //location
        edt_delivery_from_time.setOnClickListener(this) //location
        edt_delivery_to_time.setOnClickListener(this) //location

        edt_delivery_zip_date1.setOnClickListener(this)  //zip add
        edt_delivery_to_date1.setOnClickListener(this)    //zip add
        edt_delivery_zip_time1.setOnClickListener(this)    //zip add
        edt_delivery_to_time1.setOnClickListener(this)   //zip add

        edt_picup_from_date.setOnClickListener(this)     //store
        edt_pref_to_date.setOnClickListener(this)        //store
        edt_picup_from_time.setOnClickListener(this)     //store
        edt_pref_to_time.setOnClickListener(this)        //store

        img_upload_photo.setOnClickListener(this)
        img_back.setOnClickListener(this)
        btn_save_n_continue.setOnClickListener(this)
        txt_location.setOnClickListener(this)
        imageView3.setOnClickListener(this)

        getdropdownApi()

        switch_location.setOnCheckedChangeListener { buttonView, isChecked ->

            /* if (isUserInteracting) {
                 isEditable = false
             }*/

//            if (!isEditable) {
            if (isChecked) {
                onLocationClicked = true
                turnOnLocation = "1"
                getLastLocation()
            } else {
                onLocationClicked = false
                turnOnLocation = "0"
                order_lat = ""
                order_long = ""
            }
//            }
        }

        if (spinner != null) {
            val arrayAdapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, orderTypes)
            spinner.adapter = arrayAdapter

            if (isEditable) {
                var spinpos = 0
                for (i in 0 until orderTypes.size) {
                    if (order_type.equals(orderTypes.get(i))) {
                        spinpos = i
                    }
                    spinner.setSelection(spinpos)
                }
            }

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                ) {
                    /* if (isUserInteracting) {
                         isEditable = false
                     }
 */

                    if (!isEditable) {
                        order_type = orderTypes[position]
                    } else {
                        order_type = orderTypes[position]
                    }




                    setLayout()
                    /* Toast.makeText(
                         this@OrderPreferenceActivity,
                         "Slected Item" + " " + orderTypes[position],
                         Toast.LENGTH_SHORT
                     ).show()*/

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }


    }


    fun statusCheck() {
        /*val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
           Log.d("TAG","GPS ENABLE")

        }else{
            buildAlertMessageNoGps()
        }*/

        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gps_enabled: Boolean = false
        var network_enabled: Boolean = false


        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            buildAlertMessageNoGps()
        }

    }

    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.gps_disable_txt)
                .setCancelable(false)
                .setPositiveButton(R.string.yes,
                        DialogInterface.OnClickListener { dialog, id ->
                            startActivity(
                                    Intent(
                                            Settings.ACTION_LOCATION_SOURCE_SETTINGS
                                    )
                            )
                        })
                .setNegativeButton(R.string.no,
                        DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        val alert = builder.create()
        alert.show()
    }

    private fun getdropdownApi() {
        progressBar.visibility = View.VISIBLE
        val apiService = AppConstant.getClient()!!.create(RetrofitInterface::class.java)

        val call = apiService.getalldropdown()

        call.enqueue(object : Callback<GetAllDropdownResponse> {
            override fun onResponse(
                    call: Call<GetAllDropdownResponse>,
                    response: Response<GetAllDropdownResponse>
            ) {

                progressBar.visibility = View.GONE
                if (response.isSuccessful) {


                    drugPrefrncList = arrayListOf()
                    buyingPrefrncList = arrayListOf()

                    drug_list = arrayListOf()
                    drugId_list = arrayListOf()

                    Log.d("tag", "buyingPrefrncList==>" + buyingPrefrnc)


                    if (isCustom) {

                        textView7.setText(R.string.select_ur_buying_prfrnce)

                        buyingPrefrncList = response.body()!!.data!!.buyingPreference!!

                        for (i in 0 until buyingPrefrncList.size) {
                            //  var list = listPhoneCode.get(i).code
                            drug_list.add(buyingPrefrncList.get(i).name.toString())
                            drugId_list.add(buyingPrefrncList.get(i).id.toString())
                        }


                        val adapter = ArrayAdapter(
                                this@OrderPreferenceActivity,
                                android.R.layout.simple_spinner_item, drug_list
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item)
                        spinner_drug_prefrnc.adapter = adapter


                        if (isEditable) {

                            textView7.setText(R.string.select_ur_buying_prfrnce)

                            var pos = 0
                            for (i in 0 until drug_list.size) {
                                if (buyingPrefrnc.equals(drugId_list[i])) {
                                    pos = i
                                }
                                spinner_drug_prefrnc.setSelection(pos)
                            }

                        }


                        spinner_drug_prefrnc.onItemSelectedListener = object :
                                AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                    parent: AdapterView<*>,
                                    view: View, position: Int, id: Long
                            ) {

                                /*  if (isUserInteracting) {
                                      isEditable = false
                                  }
  */
                                if (!isEditable) {
                                    buyingPrefrnc = drugId_list[position]
                                    buyingPrefrncTxt = drug_list[position]
                                } else {
                                    buyingPrefrnc = drugId_list[position]
                                    buyingPrefrncTxt = drug_list[position]
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                                // write code to perform some action
                            }
                        }


                    } else {
                        isCustom = false
                        Log.d("tag", "buyingPrefrncList123==>" + buyingPrefrnc)
                        drugPrefrncList = response.body()!!.data!!.drugPreference!!


                        for (i in 0 until drugPrefrncList.size) {
                            //  var list = listPhoneCode.get(i).code
                            drug_list.add(drugPrefrncList.get(i).name.toString())
                            drugId_list.add(drugPrefrncList.get(i).id.toString())
                        }


                        val adapter = ArrayAdapter(
                                this@OrderPreferenceActivity,
                                android.R.layout.simple_spinner_item, drug_list
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item)
                        spinner_drug_prefrnc.adapter = adapter


                        if (isEditable) {
                            var pos = 0
                            for (i in 0 until drugId_list.size) {
                                if (drugPrefrnc.equals(drugId_list[i])) {
                                    pos = i
                                }
                                spinner_drug_prefrnc.setSelection(pos)
                            }

                        }

                        spinner_drug_prefrnc.onItemSelectedListener = object :
                                AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                    parent: AdapterView<*>,
                                    view: View, position: Int, id: Long
                            ) {

                                /* if (isUserInteracting) {
                                     isEditable = false
                                 }*/


                                if (!isEditable) {
                                    isCustom = false
                                    drugPrefrnc = drugId_list[position]
                                    drugPrefrncTxt = drug_list[position]
                                } else {
                                    isCustom = false
                                    drugPrefrnc = drugId_list[position]
                                    drugPrefrncTxt = drug_list[position]
                                }

                                Log.d("tag", "buyingPrefrncList456==>" + buyingPrefrnc)
                                // code= phonecode_list[position]
                                //  code = phonecode_list[position].removePrefix("+")


                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                                // write code to perform some action
                            }
                        }
                    }


                }
            }

            override fun onFailure(call: Call<GetAllDropdownResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
            }
        })
    }

    override fun onUserInteraction() {
        super.onUserInteraction()

        isUserInteracting = true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.btn_save_n_continue -> {
                Log.e("HELLOMAPS", "OrderPref")
                if (delType.equals("0")) {

                    //  order_lat = ""
                    //  order_long = ""
//                    del_lat = ""
//                    del_long = ""
                    //  turnOnLocation = ""
//                    photoId = ""
                    // startTime = ""
                    //  endTime = ""
                    //delDate = ""
//                    locaTion = ""
//                    buildName = ""
//                    blockNum = ""
//                    zipCode = ""
//                    landMark = ""
//                    area = ""
//                    city = ""
//                    streeT = ""

                    startTime = edt_picup_from_time.text.toString().trim()
                    endTime = edt_pref_to_time.text.toString().trim()
                    //  delDate = edt_pref_del_date.text.toString().trim()
                    from_date2 = edt_picup_from_date.text.toString().trim()
                    to_date2 = edt_pref_to_date.text.toString().trim()

                    todate_fromdate = (from_date2 + " " + startTime + " to " + to_date2 + " " + endTime)


                    if (from_date2.length < 1) {
                        Toast.makeText(this, "Enter From Pickup Date", Toast.LENGTH_SHORT).show()
                    } else if (startTime.length < 1) {
                        Toast.makeText(this, "Enter From Time", Toast.LENGTH_SHORT).show()
                    } else if (to_date2.length < 1) {
                        Toast.makeText(this, "Enter To Pickup Date ", Toast.LENGTH_SHORT).show()
                    } else if (endTime.length < 1) {
                        Toast.makeText(this, "Enter To Time", Toast.LENGTH_SHORT).show()
                    } else if (city.equals("")) {
                        Toast.makeText(this, "Please select Delivery City", Toast.LENGTH_SHORT).show()
                    }else {


                        Log.d("OnEditChange", "Order Preference   " + isEditable)

                        if (isEditable) {
                            startActivity(
                                    Intent(
                                            this@OrderPreferenceActivity,
                                            CustomFinalOrderActivity::class.java
                                    )
                                            .putExtra("edit", isEditable)
                                            .putExtra("custom", isCustom)
                                            .putExtra("ORDER_FOR", "")
                            )

                            //finish()
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        } else {
                            startActivity(
                                    Intent(
                                            this@OrderPreferenceActivity,
                                            OrderingForActivity::class.java
                                    )
                                            .putExtra("edit", isEditable)
                                            .putExtra("custom", isCustom)
                                            .putExtra("ORDER_FOR", "")
                            )
                            //finish()
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        }

                    }


                }
                if (delType.equals("1")) {

                    store_lat = ""
                    store_long = ""

//                    order_lat = ""
//                    order_long = ""
                    // del_lat = ""
                    //  del_long = ""
                    turnOnLocation = ""
                    //  photoId = ""
                    //   startTime = ""
                    //    endTime = ""
                    //  delDate = ""
                    //  locaTion=""
                    //  buildName=""
                    //   blockNum=""
                    // zipCode = ""
                    landMark = ""
                    //   area = ""
                    //  city = ""
                    streeT = ""

                    blockNum = edt_office_flatno.text.toString().trim()
                    buildName = edt_buiding_name.text.toString().trim()
                    locaTion = txt_location.text.toString().trim()
                    //   delDate = edt_delivery_date.text.toString().trim()
                    startTime = edt_delivery_from_time.text.toString().trim()
                    endTime = edt_delivery_to_time.text.toString().trim()
                    from_date = edt_delivery_from_date.text.toString().trim()
                    to_date = edt_delivery_to_date.text.toString().trim()

                    todate_fromdate = (from_date + " " + startTime + " to " + to_date + " " + endTime)

                    if (locaTion.isBlank()) {
                        Toast.makeText(this, getString(R.string.location_empty), Toast.LENGTH_SHORT).show()
                    } else if (blockNum.isBlank()) {
                        Toast.makeText(this, "Office/Flat Number is Empty", Toast.LENGTH_SHORT).show()
                    } else if (buildName.isBlank()) {
                        Toast.makeText(this, "Complex/Building Name is Empty", Toast.LENGTH_SHORT).show()
                    } else if (city.equals("")) {
                        Toast.makeText(this, "Please select Delivery City", Toast.LENGTH_SHORT).show()
                    } else if (from_date.isBlank()) {
                        Toast.makeText(this, "From Delivery Date is Empty", Toast.LENGTH_SHORT).show()
                    } else if (to_date.isBlank()) {
                        Toast.makeText(this, "To Delivery Date is Empty", Toast.LENGTH_SHORT).show()
                    } else if (startTime.isBlank()) {
                        Toast.makeText(this, "From Time is Empty", Toast.LENGTH_SHORT).show()
                    } else if (endTime.isBlank()) {
                        Toast.makeText(this, "To Time is Empty", Toast.LENGTH_SHORT).show()
                    } else {


                        if (isEditable) {
                            startActivity(
                                    Intent(
                                            this@OrderPreferenceActivity,
                                            CustomFinalOrderActivity::class.java
                                    )
                                            .putExtra("edit", isEditable)
                                            .putExtra("custom", isCustom)
                                            .putExtra("ORDER_FOR", "")
                                            .putExtra("ORDER_ID", "")
                            )

                            //finish()
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        } else {
                            startActivity(
                                    Intent(
                                            this@OrderPreferenceActivity,
                                            OrderingForActivity::class.java
                                    )
                                            .putExtra("edit", isEditable)
                                            .putExtra("custom", isCustom)
                                            .putExtra("ORDER_FOR", "")
                                            .putExtra("ORDER_ID", "")
                            )

                            //finish()
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        }

                    }


                }
                if (delType.equals("2")) {

//                    order_lat = ""
//                    order_long = ""
                    // del_lat = ""
                    //  del_long = ""
                    turnOnLocation = ""
                    photoId = ""
                    //   startTime = ""
                    //    endTime = ""
                    //  delDate = ""
//                    locaTion = ""
//                    buildName = ""
//                    blockNum = ""
//                    zipCode = ""
                    landMark = AppConstant.getPreferenceText(AppConstant.PREF_USER_LANDMARK)
//                    area = ""
//                    streeT = ""
//                    city = ""

                    blockNum = edt_office_flatno1.text.toString().trim()
                    buildName = edt_building_name1.text.toString().trim()
                    streeT = edt_street_road.text.toString().trim()
                    area = edt_area.text.toString().trim()
                    landMark = edt_landmark.text.toString().trim()
                    zipCode = edt_zip_code.text.toString().trim()
//                    city = AppConstant.getPreferenceText(AppConstant.PREF_USER_CITY)
                    startTime = edt_delivery_zip_time1.text.toString().trim()
                    endTime = edt_delivery_to_time1.text.toString().trim()
                    from_date1 = edt_delivery_zip_date1.text.toString().trim()
                    to_date1 = edt_delivery_to_date1.text.toString().trim()
                    //  delDate = edt_delivery_date1.text.toString().trim()


                    todate_fromdate = (from_date1 + " " + startTime + " to " + to_date1 + " " + endTime)

                    if (blockNum.isBlank()) {
                        Toast.makeText(this, "Enter Block Number", Toast.LENGTH_SHORT)
                                .show()
                    } else if (buildName.isBlank()) {
                        Toast.makeText(this, "Enter Building Name", Toast.LENGTH_SHORT)
                                .show()
                    } else if (streeT.isBlank()) {
                        Toast.makeText(this, "Enter Street/Road Name", Toast.LENGTH_SHORT).show()
                    } else if (area.isBlank()) {
                        Toast.makeText(this, "Enter Area Name", Toast.LENGTH_SHORT).show()
                    } else if (landMark.isBlank()) {
                        Toast.makeText(this, "Enter Landmark", Toast.LENGTH_SHORT).show()
                    } else if (zipCode.isBlank()) {
                        Toast.makeText(this, "Enter Zipcode", Toast.LENGTH_SHORT).show()
                    } else if (city.equals("")) {
                        Toast.makeText(this, "Please select Delivery City", Toast.LENGTH_SHORT).show()
                    } else if (from_date1.isBlank()) {
                        Toast.makeText(this, "Enter From Delivery Date", Toast.LENGTH_SHORT).show()
                    } else if (to_date1.isBlank()) {
                        Toast.makeText(this, "Enter TO Delivery Date", Toast.LENGTH_SHORT).show()
                    } else if (startTime.isBlank()) {
                        Toast.makeText(this, "Enter From Time", Toast.LENGTH_SHORT).show()
                    } else if (endTime.isBlank()) {
                        Toast.makeText(this, "Enter To Time", Toast.LENGTH_SHORT).show()
                    } else {

                        locaTion =
                                blockNum + ", " + buildName + ", " + streeT + ", " + area + ", " + landMark + ", " + zipCode

                        if (isEditable) {
                            startActivity(
                                    Intent(
                                            this@OrderPreferenceActivity,
                                            CustomFinalOrderActivity::class.java
                                    )
                                            .putExtra("edit", isEditable)
                                            .putExtra("custom", isCustom)
                                            .putExtra("ORDER_FOR", "")
                            )

                            //finish()
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        } else {
                            startActivity(
                                    Intent(
                                            this@OrderPreferenceActivity,
                                            OrderingForActivity::class.java
                                    )
                                            .putExtra("edit", isEditable)
                                            .putExtra("custom", isCustom)
                                            .putExtra("ORDER_FOR", "")
                            )

                            //finish()
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        }

                    }

                }


            }
            R.id.edt_delivery_from_date -> {     //location from date
                flag = 1
                location = 1
                showCalender()
            }
            R.id.edt_delivery_to_date -> {     //location to date
                flag = 1
                location = 0
                showCalender()
            }
            R.id.edt_delivery_from_time -> {       //location   edt_from_time
                timeFlag = false
                timepicker()
            }
            R.id.edt_delivery_to_time -> {         //location  edt_to_time
                timeFlag = false
                timepicker1()
            }
            R.id.img_upload_photo -> {
                choice()
                selectFromGallery()
            }
            R.id.edt_pref_to_time -> {    //store

                timeFlag = true
                timepicker1()
            }
            R.id.edt_picup_from_time -> {        //store
                timeFlag = true
                timepicker()
            }
            R.id.edt_picup_from_date -> {    //store from date
                flag = 0
                showCalender()
                store = 1
            }
            R.id.edt_pref_to_date -> { //store to date
                flag = 0
                store = 0
                showCalender()
            }
            R.id.edt_delivery_zip_date1 -> {  //zip from date
                flag = 2
                zip = 1
                showCalender()
            }

            R.id.edt_delivery_to_date1 -> {  //zip to date
                flag = 2
                zip = 0
                showCalender()
            }
            R.id.edt_delivery_zip_time1 -> {          //zip add
                timepicker2()
            }
            R.id.edt_delivery_to_time1 -> {          //zip add
                timepicker3()
            }
            R.id.txt_location -> {

                getLastLocation()

                /* startActivityForResult(
                     Intent(
                         this@OrderPreferenceActivity,
                         PlaceAutoCompleteActivity::class.java
                     ), 111
                 )
                 //finish()
                 overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)*/
            }
            R.id.imageView3 -> {
                getLastLocation()
                /* startActivityForResult(
                     Intent(
                         this@OrderPreferenceActivity,
                         PlaceAutoCompleteActivity::class.java
                     ), 111
                 )
                 //finish()
                 overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)*/
            }
        }
    }

    fun choice() {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = File(
                    Environment
                            .getExternalStorageDirectory(), "IMG_" + System.currentTimeMillis()
                    + ".jpg"
            )
        } else {
            mFileTemp = File(
                    filesDir, "IMG_" + System.currentTimeMillis()
                    + ".jpg"
            )
        }
    }

    private fun selectFromGallery() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!canAccessCamera()) {
                requestPermissions(INITIAL_GALLERY, 1340)
            } else {
                val photoPickerIntent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.type = "image/*"
                startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY)
            }
        } else {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY)
        }
    }

    private fun canAccessCamera(): Boolean {
        return hasPermission(Manifest.permission.CAMERA) && hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE) && hasPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun hasPermission(perm: String): Boolean {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1340) {

            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val photoPickerIntent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.type = "image/*"
                startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY)
            } else {
                Toast.makeText(this, "Premission Required", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }

        } else {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try {
                    var mImageCaptureUri: Uri? = null
                    val state = Environment.getExternalStorageState()
                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        mImageCaptureUri = Uri.fromFile(mFileTemp)
                    } else {
                        mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI
                    }
                    intent.putExtra(
                            MediaStore.EXTRA_OUTPUT,
                            mImageCaptureUri
                    )
                    intent.putExtra("return-data", true)
                    startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }

            } else {
                Toast.makeText(this, R.string.permission_required, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 111) {
            if (data!!.hasExtra("address")) {
                val returnString = data.extras.getString("address")
                txt_location.setText(returnString)
            }
        }
        if (resultCode == androidx.appcompat.app.AppCompatActivity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                val selectedImageUri = data?.data
                CropImage.activity(selectedImageUri)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setAspectRatio(1, 1)
                    .setInitialCropWindowPaddingRatio(0f)
                        .setGuidelines(CropImageView.Guidelines.OFF)
                        .setActivityTitle(resources.getString(R.string.my_crop))
                        .start(this)
            }
            if (requestCode == REQUEST_CAMERA) {
                val selectedImageUri = Uri.fromFile(mFileTemp)
                CropImage.activity(selectedImageUri)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setAspectRatio(1, 1)
                    .setInitialCropWindowPaddingRatio(0f)
                        .setGuidelines(CropImageView.Guidelines.OFF)
                        .setActivityTitle(resources.getString(R.string.my_crop))
                        .start(this)
            }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val result = CropImage.getActivityResult(data)
                val resultUri = result.uri
                //imgProfile.setImageURI(resultUri);
                path = resultUri.path
                if (resultUri.path == null) {
                    return
                } else {

//                    bitmap = BitmapFactory.decodeFile(path)
                    bitmap = ScaleFile.compressImage(path, this)
                    photoId = AppConstant.convert(bitmap!!)
                    img_upload_photo?.setImageBitmap(bitmap)
                }
            }
        }
    }

    private fun showCalender() {
        val today = Date()
        myCalendar!!.time = today

        val minDate = myCalendar.timeInMillis

        val datePickerDialog = DatePickerDialog(
                this@OrderPreferenceActivity, this,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = minDate
        datePickerDialog.setTitle("")
        datePickerDialog.show()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient?.lastLocation?.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {


                        val latitude = location.latitude
                        val longitude = location.longitude

                        order_lat = latitude.toString()
                        del_lat = latitude.toString()
                        order_long = longitude.toString()
                        del_long = longitude.toString()

                        try {

                            val addresses = geocoder!!.getFromLocation(latitude, longitude, 1)

                            val addressLine1 = addresses.get(0).getAddressLine(0)
                            //  Log.e("line1", addressLine1)

                            val citye = addresses.get(0).locality
                            val city3 = addresses.get(0).subLocality


//                        city = citye
                            if (city3 != null) {
                                area = city3

                            }
                            if (addresses.get(0).postalCode != null) {
                                zipCode = addresses.get(0).postalCode

                            }

                            Log.d("TAG", "address----" + addressLine1)

                            txt_location.setText(addressLine1)
                            locaTion = addressLine1

                        }catch (e:Exception){
                            Log.d("TAG", "Eroor----" + e.message)
                        }


                    }
                }
            } else {
//                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }


    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation


            val latitude = mLastLocation.latitude
            val longitude = mLastLocation.longitude

            order_lat = latitude.toString()
            del_lat = latitude.toString()
            order_long = longitude.toString()
            del_long = longitude.toString()


            val addresses = geocoder!!.getFromLocation(latitude, longitude, 1)
            val addressLine1 = addresses.get(0).getAddressLine(0)
            //  Log.e("line1", addressLine1)

            val citye = addresses.get(0).locality
            val city3 = addresses.get(0).subLocality
            val pinCode = addresses.get(0).postalCode

            city = citye
            if (city3 != null) {
                area = city3
            }
            if(pinCode != null){
                zipCode = pinCode
            }


            txt_location.setText(addressLine1)
            locaTion = addressLine1
            //  findViewById<TextView>(R.id.latTextView).text = mLastLocation.latitude.toString()
            // findViewById<TextView>(R.id.lonTextView).text = mLastLocation.longitude.toString()
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


    fun setLayout() {


        if (intent != null && intent.getStringExtra("address") != null) {

            txt_location.setText(intent.getStringExtra("address"))

            constraint_store_pickup.visibility = View.GONE
            nested_location_delivery.visibility = View.VISIBLE
            nested_zip_add_delivery.visibility = View.GONE

            spinner_store_pickup.setSelection(1)

        } else {

            if (order_type.equals("Store Pickup")) {
                delType = "0"
                delprerncetext = "Store Pickup"
                constraint_store_pickup.visibility = View.VISIBLE
                nested_location_delivery.visibility = View.GONE
                nested_zip_add_delivery.visibility = View.GONE

                if (isEditable) {
                    if (turnOnLocation.equals("1")) {
                        switch_location.isChecked = true
                    }


                    Log.e("HelloReOrde", "OnComes   delDateFormat " + delDateFormat + "   toDateFormat   " + toDateFormat)

                    edt_picup_from_date.setText(delDateFormat)
                    edt_pref_to_date.setText(toDateFormat)
                    edt_picup_from_time.setText(startTimeFormat.toLowerCase()) //store
                    edt_pref_to_time.setText(endTimeFormat.toLowerCase())  // store
                } else {
                    if (switch_location.isChecked)
                        switch_location.isChecked = false

                    edt_picup_from_date.setText("")
                    edt_pref_to_date.setText("")
                    edt_picup_from_time.setText("") //store
                    edt_pref_to_time.setText("")   // storef
                }

            } else if (order_type.equals("Location Delivery")) {
                delType = "1"
                delprerncetext = "Location Delivery"
                constraint_store_pickup.visibility = View.GONE
                nested_location_delivery.visibility = View.VISIBLE
                nested_zip_add_delivery.visibility = View.GONE
                Log.d("TAG", "ORDER Location")
                if (isEditable) {

                    edt_office_flatno.setText(blockNum)
                    edt_buiding_name.setText(buildName)
                    txt_location.setText(locaTion)
                    edt_delivery_from_date.setText(delDateFormat)
                    edt_delivery_to_date.setText(toDateFormat)
                    edt_delivery_from_time.setText(startTimeFormat.toLowerCase())     // location
                    edt_delivery_to_time.setText(endTimeFormat.toLowerCase())          //location


                    if (bitmap != null) {
                        img_upload_photo?.setImageBitmap(bitmap)
                    } else {
                        Glide.with(applicationContext)  //2
                                .load(photoId) //3
                                .placeholder(R.drawable.photo_id) //5
                                .centerCrop() //4
                                .into(img_upload_photo) //8
                    }
                } else {
                    edt_office_flatno.setText("")
                    edt_buiding_name.setText("")
                    txt_location.setText("")
                    edt_delivery_from_date.setText("")
                    edt_delivery_to_date.setText("")
                    edt_delivery_from_time.setText("")      //location
                    edt_delivery_to_time.setText("")       //location
                }
            } else if (order_type.equals("Zip Address Delivery")) {
                delType = "2"
                delprerncetext = "Zip Address Delivery"
                constraint_store_pickup.visibility = View.GONE
                nested_location_delivery.visibility = View.GONE
                nested_zip_add_delivery.visibility = View.VISIBLE

                if (isEditable) {
                    edt_office_flatno1.setText(blockNum)
                    edt_building_name1.setText(buildName)
                    edt_street_road.setText(streeT)
                    edt_area.setText(area)
                    edt_landmark.setText(landMark)
                    edt_zip_code.setText(zipCode)
                    edt_delivery_zip_date1.setText(delDateFormat)
                    edt_delivery_to_date1.setText(toDateFormat)
                    edt_delivery_zip_time1.setText(startTimeFormat.toLowerCase()) // zip add
                    edt_delivery_to_time1.setText(endTimeFormat.toLowerCase()) //zip add
                } else {
                    // edt_office_flatno1.setText("")
                    edt_building_name1.setText("")
                    // edt_street_road.setText("")
                    edt_area.setText("")
                    //edt_landmark.setText("")
                    //edt_zip_code.setText("")
                    edt_delivery_zip_date1.setText("")
                    edt_delivery_to_date1.setText("")
                    edt_delivery_zip_time1.setText("")  //zip add
                    edt_delivery_to_time1.setText("") //zip add

                    edt_street_road.setText(AppConstant.getPreferenceText(AppConstant.PREF_USER_ADDRESSLINE2))
                    edt_office_flatno1.setText(AppConstant.getPreferenceText(AppConstant.PREF_USER_ADDRESSLINE1))
                    edt_zip_code.setText(AppConstant.getPreferenceText(AppConstant.PREF_USER_ZIPCODE))
                    edt_landmark.setText((AppConstant.getPreferenceText(AppConstant.PREF_USER_LANDMARK)))
                }
                /* blockNum = edt_office_flatno1.text.toString().trim()
                    buildName = edt_building_name1.text.toString().trim()
                    streeT = edt_street_road.text.toString().trim()
                    area = edt_area.text.toString().trim()
                    landMark = edt_landmark.text.toString().trim()
                    zipCode = edt_zip_code.text.toString().trim()*/
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myCalendar!!.set(Calendar.YEAR, year)
        myCalendar!!.set(Calendar.MONTH, month)
        myCalendar!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)


        if (flag == 1) {
            updateLabel() //location
        } else if (flag == 2) {
            updateLabel1()  //zip
        } else {
            updateLabel2() //store
        }
    }

    private fun updateLabel() {   //location
        val myFormat = "dd/MM/yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat,Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val tz = myCalendar!!.timeZone

        //  from_date = sdf.format(myCalendar.getTime())
        //   to_date = sdf.format(myCalendar.getTime())


        if (location == 1) {
            delDateFormat = SimpleDateFormat("dd/MM/yyyy",Locale.US).format(myCalendar.time)
            from_date = SimpleDateFormat(" dd/MM/yyyy",Locale.US).format(myCalendar.time)
            delDate = SimpleDateFormat("yyyy-MM-dd",Locale.US).format(myCalendar.time)
            edt_delivery_from_date?.setText(SimpleDateFormat("dd/MM/yyyy",Locale.US).format(myCalendar.time))
            edt_delivery_to_date.setText("")

        } else {
            toDateFormat = SimpleDateFormat(" dd/MM/yyyy",Locale.US).format(myCalendar.time)
            to_date = SimpleDateFormat(" dd/MM/yyyy",Locale.US).format(myCalendar.time)
            toDeliveryDate = SimpleDateFormat("yyyy-MM-dd",Locale.US).format(myCalendar.time)


            if (!from_date.equals("") || from_date.isNotEmpty()) {

                val d1: Date = sdf.parse(from_date)
                val d2: Date = sdf.parse(to_date)

                if (d1.before(d2) || d1.equals(d2)) {

                    edt_delivery_to_date.setText(SimpleDateFormat("dd/MM/yyyy",Locale.US).format(myCalendar.time))
                } else {
                    Toast.makeText(this@OrderPreferenceActivity, "Invalid Date", Toast.LENGTH_LONG).show()
                    edt_delivery_to_date.setText("")
                }

            } else {

                Toast.makeText(this@OrderPreferenceActivity, "Enter From Delivery Date", Toast.LENGTH_LONG).show()
            }

        }

        //  edt_delivery_from_time.setText("")
        // edt_delivery_to_time.setText("")

    }

    private fun updateLabel1() {        //zip
        val myFormat = "dd/MM/yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat,Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val tz = myCalendar!!.timeZone


        if (zip == 1) {
            delDateFormat = SimpleDateFormat("dd/MM/yyyy",Locale.US).format(myCalendar.time)
            from_date1 = SimpleDateFormat(" dd/MM/yyyy",Locale.US).format(myCalendar.time)
            delDate = SimpleDateFormat("yyyy-MM-dd",Locale.US).format(myCalendar.time)
            edt_delivery_zip_date1?.setText(SimpleDateFormat("dd/MM/yyyy",Locale.US).format(myCalendar.time))
            edt_delivery_to_date1.setText("")

        } else {
            toDateFormat = SimpleDateFormat(" dd/MM/yyyy",Locale.US).format(myCalendar.time)
            to_date1 = SimpleDateFormat(" dd/MM/yyyy",Locale.US).format(myCalendar.time)
            toDeliveryDate = SimpleDateFormat("yyyy-MM-dd",Locale.US).format(myCalendar.time)


            if (!from_date1.equals("") || from_date1.isNotEmpty()) {

                val d1: Date = sdf.parse(from_date1)
                val d2: Date = sdf.parse(to_date1)

                if (d1.before(d2) || d1.equals(d2)) {

                    edt_delivery_to_date1.setText(SimpleDateFormat("dd/MM/yyyy",Locale.US).format(myCalendar.time))
                } else {
                    Toast.makeText(this@OrderPreferenceActivity, "Invalid Date", Toast.LENGTH_LONG).show()
                    edt_delivery_to_date1.setText("")
                }

            } else {

                Toast.makeText(this@OrderPreferenceActivity, "Enter From Delivery Date", Toast.LENGTH_LONG).show()
            }


        }
        //  edt_delivery_zip_time1.setText("")
        //   edt_delivery_to_time1.setText("")
    }

    private fun updateLabel2() {        //store
        val myFormat = "dd/MM/yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat,Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val tz = myCalendar!!.timeZone


        if (store == 1) {
            delDateFormat = SimpleDateFormat("dd/MM/yyyy",Locale.US).format(myCalendar.time)
            from_date2 = SimpleDateFormat("dd/MM/yyyy",Locale.US).format(myCalendar.time)
            delDate = SimpleDateFormat("yyyy-MM-dd",Locale.US).format(myCalendar.time)
            edt_picup_from_date?.setText(SimpleDateFormat("dd/MM/yyyy",Locale.US).format(myCalendar.time))
            edt_pref_to_date.setText("")

        } else {
            toDateFormat = SimpleDateFormat(" dd/MM/yyyy",Locale.US).format(myCalendar.time)
            to_date2 = SimpleDateFormat(" dd/MM/yyyy",Locale.US).format(myCalendar.time)
            toDeliveryDate = SimpleDateFormat("yyyy-MM-dd",Locale.US).format(myCalendar.time)

            if (!from_date2.equals("") || from_date2.isNotEmpty()) {

                val d1: Date = sdf.parse(from_date2)
                val d2: Date = sdf.parse(to_date2)

                if (d1.before(d2) || d1.equals(d2)) {

                    edt_pref_to_date.setText(SimpleDateFormat("dd/MM/yyyy",Locale.US).format(myCalendar.time))
                } else {
                    Toast.makeText(this@OrderPreferenceActivity, "Invalid Date", Toast.LENGTH_LONG).show()
                    edt_pref_to_date.setText("")
                }

            } else {

                Toast.makeText(this@OrderPreferenceActivity, "Enter From Delivery Date", Toast.LENGTH_LONG).show()
            }


        }

        //   edt_picup_from_time.setText("") //store
        //  edt_pref_to_time.setText("")  // store
    }

    @Throws(ParseException::class)
    fun isValidDate(pDateString: String?): Boolean {
        val date = SimpleDateFormat("MM/dd/yyyy",Locale.US).parse(pDateString)
        return Date().before(date)
    }


    fun get12hourTimeFormat(time24Hour: String?): String? {
        var time12Hour: String? = ""
        try {
            if (!TextUtils.isEmpty(time24Hour)) {
                val sdf = SimpleDateFormat("H:mm",Locale.US)
                val dateObj = sdf.parse(time24Hour)
                time12Hour = SimpleDateFormat("hh:mm aa",Locale.US).format(dateObj)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return time12Hour
    }


    private fun timepicker() {
        val cal = Calendar.getInstance()
        val hourr = cal.get(Calendar.HOUR_OF_DAY)
        val minutee = cal.get(Calendar.MINUTE)

        val myFormat = "dd MMM, yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat,Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("GMT")

        val currdate = sdf.format(cal.time)

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            if (!timeFlag!!) {

                if (edt_delivery_from_date.text.toString().trim().length < 1) {
                    Toast.makeText(this, "Delivery Date is Empty", Toast.LENGTH_SHORT).show()
                } else if (currdate.equals(from_date) && (hour <= hourr) &&
                        (minute <= minutee)) {
                    Toast.makeText(this, "Your start time must be more than present time",
                            Toast.LENGTH_SHORT).show()
                } else {
                    from_time = cal.timeInMillis
                    edt_delivery_from_time!!.setText(SimpleDateFormat("hh:mm a",Locale.US).format(cal.time).toLowerCase())  //location
                    startTimeFormat = SimpleDateFormat("hh:mm a",Locale.US).format(cal.time).toString().toLowerCase()
                    startTime = SimpleDateFormat("HH:mm:ss",Locale.US).format(cal.time).toString().toLowerCase()
                    endTime = ""
                    endTimeFormat = ""
                    edt_delivery_to_time!!.setText("")     //location
                }

            } else {

                if (edt_picup_from_date.text.toString().trim().length < 1) {
                    Toast.makeText(this, "Pickup Date is Empty", Toast.LENGTH_SHORT).show()
                } else if (currdate.equals(from_date2) && (hour <= hourr) &&
                        (minute <= minutee)) {
                    Toast.makeText(this, "Your start time must be more than present time",
                            Toast.LENGTH_SHORT).show()
                } else {
                    from_time = cal.timeInMillis
                    edt_picup_from_time!!.setText(SimpleDateFormat("hh:mm a",Locale.US).format(cal.time).toLowerCase()) //store
                    startTimeFormat = SimpleDateFormat("hh:mm a",Locale.US).format(cal.time).toString().toLowerCase()
                    startTime = SimpleDateFormat("HH:mm:ss",Locale.US).format(cal.time).toString().toLowerCase()
                    endTime = ""
                    endTimeFormat = ""

//yyyy-MM-dd HH:mm:ss // need to convert in this
                    edt_pref_to_time!!.setText("")   // store
                }
            }
        }

        TimePickerDialog(
                this,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                false
        ).show()
    }

    private fun timepicker1() {
        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            //  if (from_time != null && cal.getTimeInMillis() >= from_time!!) {
            if (!timeFlag!!) {
                //it's after current
                edt_delivery_to_time!!.setText(SimpleDateFormat("hh:mm a",Locale.US).format(cal.time).toLowerCase()) //edt_to_time
            } else {
                //it's after current
                edt_pref_to_time!!.setText(SimpleDateFormat("hh:mm a",Locale.US).format(cal.time).toLowerCase())   // store
            }

            endTime = SimpleDateFormat("HH:mm:ss",Locale.US).format(cal.time).toString().toLowerCase()
            endTimeFormat = SimpleDateFormat("hh:mm a",Locale.US).format(cal.time).toString().toLowerCase()
            /* } else {
                 //it's before current'
                 Toast.makeText(applicationContext, "Invalid Time", Toast.LENGTH_LONG).show()
             }*/

            //   txt_to_time!!.text=SimpleDateFormat("hh:mm a").format(cal.time)
        }
        TimePickerDialog(
                this,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                false
        ).show()
    }

    private fun timepicker2() {
        val cal = Calendar.getInstance()
        val hourr = cal.get(Calendar.HOUR_OF_DAY)
        val minutee = cal.get(Calendar.MINUTE)

        val myFormat = "dd MMM, yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat,Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("GMT")

        val currdate = sdf.format(cal.time)
        Log.d("TAG", "time_format-----" + SimpleDateFormat("hh:mm a",Locale.US).format(cal.time))
        Log.d("TAG", "currdate---1111111--" + currdate)

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            if (edt_delivery_zip_date1.text.toString().trim().length < 1) {
                Toast.makeText(this, "From Delivery Date is Empty", Toast.LENGTH_SHORT).show()
            } else if (currdate.equals(from_date1) && (hour <= hourr) &&
                    (minute <= minutee)) {
                Toast.makeText(this, "Your start time must be more than present time",
                        Toast.LENGTH_SHORT).show()
            } else {

                from_time1 = cal.timeInMillis
                edt_delivery_zip_time1!!.setText(SimpleDateFormat("hh:mm a",Locale.US).format(cal.time).toLowerCase()) //zip address
                startTime = SimpleDateFormat("HH:mm:ss",Locale.US).format(cal.time).toString().toLowerCase()
                startTimeFormat = SimpleDateFormat("hh:mm a",Locale.US).format(cal.time).toString().toLowerCase()
                endTime = ""
                endTimeFormat = ""
                edt_delivery_to_time1!!.setText("") //zip
            }
        }

        TimePickerDialog(
                this,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                false
        ).show()
    }

    private fun timepicker3() {
        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)


            //  if (from_time1 != null && cal.getTimeInMillis() >= from_time1!!) {
            //it's after current
            edt_delivery_to_time1!!.setText(SimpleDateFormat("hh:mm a",Locale.US).format(cal.time).toLowerCase()) //zip add
            endTimeFormat = SimpleDateFormat("hh:mm a",Locale.US).format(cal.time).toString().toLowerCase()
            endTime = SimpleDateFormat("HH:mm:ss",Locale.US).format(cal.time).toString().toLowerCase()
            /*} else {
                //it's before current'
                Toast.makeText(applicationContext, "Invalid Time", Toast.LENGTH_LONG).show()
            }*/

            //   txt_to_time!!.text=SimpleDateFormat("hh:mm a").format(cal.time)
        }
        TimePickerDialog(
                this,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                false
        ).show()
    }

    companion object {

        private const val LOCATION_PERMISSION_REQUEST_CODE = 100

        private var bitmap: Bitmap? = null

        var drugPrefrnc = ""
        var drugPrefrncTxt = ""
        var buyingPrefrnc = ""
        var buyingPrefrncTxt = ""
        var delprerncetext = ""
        var delType = ""
        var order_lat = ""
        var order_long = ""
        var store_lat = ""
        var store_long = ""
        var del_lat = ""
        var del_long = ""
        var turnOnLocation = "0"
        var photoId = ""
        var startTime = ""
        var endTime = ""
        var delDate = ""
        var toDeliveryDate = ""
        var todate_fromdate = ""
        var delDateFormat = ""
        var toDateFormat = ""
        var startTimeFormat = ""
        var endTimeFormat = ""
        var locaTion = ""
        var buildName = ""
        var blockNum = ""
        var zipCode = ""
        var landMark = ""
        var area = ""
        var city = ""
        var delivery_city = ""
        var streeT = ""


        /*
                    photoId = data?.photoId!!
                    final_md_id = data?.storeCriteriaStoreMdId!!
                    final_type_id= data?.storeCriteriaStoreTypeId!!
                    final_= data?.storeCriteriaStoreTypeName!!*/
    }

    private fun getAllCity() {
        progressBar.visibility = View.VISIBLE
        val apiService = AppConstant.getClient()!!.create(RetrofitInterface::class.java)

        val call = apiService.getAllStoreCity()

        call.enqueue(object : Callback<GetAllCityResponse> {
            override fun onResponse(call: Call<GetAllCityResponse>, response: Response<GetAllCityResponse>) {

                progressBar.visibility = View.GONE
                if (response.isSuccessful) {


                    city_list.add(0, "Select from available Cities")

                    cityList = response.body()?.data!!
                    for (i in 0 until cityList.size) {
                        city_list.add(cityList.get(i).city.toString())
                    }

                    val adapter = ArrayAdapter(this@OrderPreferenceActivity, android.R.layout.simple_spinner_item, city_list)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    val spinr_city = findViewById<Spinner>(R.id.spinr_city)
//                    spinr_city.setPrompt("Select City");
                    spinr_city.adapter = adapter

                    spinr_city.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                            if (position > 0) {
                                city = city_list[position]
                                delivery_city = city_list[position]
                            }else{
                                city = ""
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // write code to perform some action
                        }

                    }
//
//                    val spinr_zip_city = findViewById<Spinner>(R.id.spinr_zip_city)
//                    spinr_zip_city.adapter = adapter
//
//                    spinr_zip_city.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//
//                            if (position > 0) {
//                                city = city_list[position]
//                                delivery_city = city_list[position]
//                            }else{
//                                city = ""
//                            }
//                        }
//
//                        override fun onNothingSelected(parent: AdapterView<*>) {
//                            // write code to perform some action
//                        }
//
//                    }

                }
            }


            override fun onFailure(call: Call<GetAllCityResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
            }
        })
    }


    override fun onResume() {
        super.onResume()
        if (onLocationClicked && isLocationEnabled()) {
            switch_location.isChecked = true
            getLastLocation()
        } else {
            switch_location.isChecked = false
        }
    }
}