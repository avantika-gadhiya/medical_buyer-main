package bluesharklabs.com.medical.activities

import android.Manifest
import android.annotation.TargetApi
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
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
import bluesharklabs.com.medical.utils.AppConstant
import bluesharklabs.com.medical.utils.InternalStorageContentProvider
import bluesharklabs.com.medical.utils.ScaleFile
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_custom_order_preference.*
import kotlinx.android.synthetic.main.activity_custom_order_preference.btn_save_n_continue
import kotlinx.android.synthetic.main.activity_custom_order_preference.constraint_store_pickup
import kotlinx.android.synthetic.main.activity_custom_order_preference.img_back
import kotlinx.android.synthetic.main.activity_custom_order_preference.img_upload_photo
import kotlinx.android.synthetic.main.activity_custom_order_preference.nested_location_delivery
import kotlinx.android.synthetic.main.activity_custom_order_preference.nested_zip_add_delivery
import kotlinx.android.synthetic.main.activity_custom_order_preference.switch_location
import kotlinx.android.synthetic.main.activity_find_platform.*
import kotlinx.android.synthetic.main.activity_order_preference.*
import kotlinx.android.synthetic.main.activity_order_preference.progressBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CustomOrderPreferenceActivity : AppCompatActivity(), View.OnClickListener,
        DatePickerDialog.OnDateSetListener {

    private var mFileTemp: File? = null
    private val SELECT_FILE = 1
    var bitmap: Bitmap? = null
    var path = ""
    private val REQUEST_CAMERA = 0
    val REQUEST_CODE_TAKE_PICTURE = 2
    val REQUEST_CODE_GALLERY = 1

    val INITIAL_GALLERY = arrayOf<String>(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    )

    var order_type = ""
    private var myCalendar = Calendar.getInstance()
    private var cYear = 0
    private var cMonth = 0
    private var cDay = 0
    private var from_date = ""
    private var from_date1 = ""
    private var from_time: Long? = null
    private var from_time1: Long? = null
    private var flag: Boolean? = null

    var city_name = ""

    var cityList: List<GetAllCityResponse.Datum> = arrayListOf()

    // city
    private var city_list = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//  set status text dark

        setContentView(R.layout.activity_custom_order_preference)


        // edt_office_flatno.setText("khkhjhjs kjjkk")
        // txt_location.setText("Your activity / container layout you wish status bar needs this property set:")
        // edt_delivery_date.setText("16 dec 2019")

        /*   val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(this, permissions, 0)*/

        switch_location.setOnClickListener(View.OnClickListener {
            checkOrAskLocationPermission {
                switch_location.isChecked = true
            }
        })


        val orderTypes = arrayOf("Store Pickup", "Location Delivery", "Zip Address Delivery")
        val drugPreferences = arrayOf("Only My Brands/Make", "Any Brands/Make")
        val spinner = findViewById<Spinner>(R.id.spinner_store_pickup)
        val spinner1 = findViewById<Spinner>(R.id.spinner_prefrnc)
        val spinner2 = findViewById<Spinner>(R.id.spinr_city)

        getAllCity()


        edt_from_date.setFocusable(false)      //location
        edt_from_date.setClickable(true)       //location

        edt_to_date.setFocusable(false)         //location
        edt_to_date.setClickable(true)         //location

        edt_from_time.setFocusable(false)      //location
        edt_from_time.setClickable(true)       //location

        edt_to_time.setFocusable(false)        //location
        edt_to_time.setClickable(true)         //location

        edt_delivery_from_date1.setFocusable(false)      //zip add
        edt_delivery_from_date1.setClickable(true)       //zip add

        edt_to_date1.setFocusable(false)      //zip add
        edt_to_date1.setClickable(true)       //zip add

        edt_delivery_from_time1.setFocusable(false)       //zip add
        edt_delivery_from_time1.setClickable(true)          //zip add

        edt_to_time1.setFocusable(false)       //zip add
        edt_to_time1.setClickable(true)          //zip add

        edt_from_date.setOnClickListener(this)          //location
        edt_from_time.setOnClickListener(this)          //location
        edt_to_time.setOnClickListener(this)            //location
        edt_to_date.setOnClickListener(this)            //location
        img_upload_photo.setOnClickListener(this)

        edt_delivery_from_date1.setOnClickListener(this)     //zip add
        edt_delivery_from_time1.setOnClickListener(this)     //zip add
        edt_to_date1.setOnClickListener(this)                 //zip add
        edt_to_time1.setOnClickListener(this)                 //zip add

        img_back.setOnClickListener(this)
        btn_save_n_continue.setOnClickListener(this)

        if (spinner1 != null) {
            val arrayAdapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, drugPreferences)
            spinner1.adapter = arrayAdapter

            spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                ) {

                    order_type = drugPreferences[position]

                    setLayout()
                    /* Toast.makeText(
                         this@OrderPreferenceActivity,
                         "Slected Item" + " " + drugPreferences[position],
                         Toast.LENGTH_SHORT
                     ).show()*/

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }

        if (spinner != null) {
            val arrayAdapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, orderTypes)
            spinner.adapter = arrayAdapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                ) {
                    order_type = orderTypes[position]

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


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.btn_save_n_continue -> {
                Log.e("HELLOMAPS", "CustomOrderPref")
                startActivity(
                        Intent(this@CustomOrderPreferenceActivity, OrderingForActivity::class.java)
                                .putExtra("custom", "1")
                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.edt_from_date -> {
                flag = false
                showCalender()
            }
            R.id.edt_to_date -> {
                flag = false
                showCalender()
            }
            R.id.edt_from_time -> {
                timepicker()
            }
            R.id.edt_to_time -> {
                timepicker1()
            }
            R.id.img_upload_photo -> {
                choice()
                selectFromGallery()
            }
            R.id.edt_delivery_from_date1 -> {
                flag = true
                showCalender()
            }
            R.id.edt_to_date1 -> {
                flag = true
                showCalender()
            }
            R.id.edt_delivery_from_time1 -> {
                timepicker2()
            }
            R.id.edt_to_time1 -> {
                timepicker3()
            }
        }
    }


    /*    edt_from_date.setOnClickListener(this)          //location
        edt_from_time.setOnClickListener(this)          //location
        edt_to_time.setOnClickListener(this)            //location
        edt_to_date.setOnClickListener(this)            //location
        img_upload_photo.setOnClickListener(this)

        edt_delivery_from_date1.setOnClickListener(this)     //zip add
        edt_delivery_from_time1.setOnClickListener(this)     //zip add
        edt_to_date1.setOnClickListener(this)                 //zip add
        edt_to_time1.setOnClickListener(this)                 //zip add*/

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

    fun checkOrAskLocationPermission(callback: () -> Unit) {
        // Check GPS is enabled
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show()
            buildAlertMessageNoGps(this)
            // return
        }

        // Check location permission is granted - if it is, start
        // the service, otherwise request the permission
        val permission =
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            callback.invoke()
        } else {
            // callback will be inside the activity's onRequestPermissionsResult(
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    107
            )
        }
    }


    fun buildAlertMessageNoGps(context: Context) {
        val builder = AlertDialog.Builder(context);
        builder.setMessage("Your GPS is disabled. Do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ -> context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
                .setNegativeButton("No") { dialog, _ -> dialog.cancel(); }
        val alert = builder.create();
        alert.show();
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 107) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("yes", "yes");

            } else {
                Log.d("yes", "no");
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        } else {
            if (requestCode == 1340) {

                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val photoPickerIntent = Intent(Intent.ACTION_PICK)
                    photoPickerIntent.type = "image/*"
                    startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY)
                } else {
                    Toast.makeText(this, "Premission Required", Toast.LENGTH_SHORT).show()
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == androidx.appcompat.app.AppCompatActivity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                val selectedImageUri = data?.data
                CropImage.activity(selectedImageUri)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setAspectRatio(1, 1)
                    .setInitialCropWindowPaddingRatio(0f)
                        .setGuidelines(CropImageView.Guidelines.OFF)
                        .setActivityTitle(getResources().getString(R.string.my_crop))
                        .start(this)
            }
            if (requestCode == REQUEST_CAMERA) {
                val selectedImageUri = Uri.fromFile(mFileTemp)
                CropImage.activity(selectedImageUri)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setAspectRatio(1, 1)
                    .setInitialCropWindowPaddingRatio(0f)
                        .setGuidelines(CropImageView.Guidelines.OFF)
                        .setActivityTitle(getResources().getString(R.string.my_crop))
                        .start(this)
            }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val result = CropImage.getActivityResult(data)
                val resultUri = result.getUri()
                //imgProfile.setImageURI(resultUri);
                path = resultUri.getPath()
                if (resultUri.getPath() == null) {
                    return
                } else {

//                    bitmap = BitmapFactory.decodeFile(path)

                    bitmap = ScaleFile.compressImage(path, this)


                    img_upload_photo?.setImageBitmap(bitmap)
                }
            }
        }
    }

    private fun showCalender() {
        val today = Date()
        myCalendar!!.time = today
        myCalendar.add(Calendar.DATE, 1)

        val minDate = myCalendar.timeInMillis

        val datePickerDialog = DatePickerDialog(
                this@CustomOrderPreferenceActivity, this,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = minDate
        datePickerDialog.setTitle("")
        datePickerDialog.show()
    }

    fun setLayout() {
        if (order_type.equals("Store Pickup")) {
            constraint_store_pickup.visibility = View.VISIBLE
            nested_location_delivery.visibility = View.GONE
            nested_zip_add_delivery.visibility = View.GONE

        } else if (order_type.equals("Location Delivery")) {
            constraint_store_pickup.visibility = View.GONE
            nested_location_delivery.visibility = View.VISIBLE
            nested_zip_add_delivery.visibility = View.GONE
        } else if (order_type.equals("Zip Address Delivery")) {
            constraint_store_pickup.visibility = View.GONE
            nested_location_delivery.visibility = View.GONE
            nested_zip_add_delivery.visibility = View.VISIBLE
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myCalendar!!.set(Calendar.YEAR, year)
        myCalendar!!.set(Calendar.MONTH, month)
        myCalendar!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        if (flag == false) {
            updateLabel()
        } else {
            updateLabel1()
        }
    }

    private fun updateLabel() {
        val myFormat = "dd/MM/yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat,Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val tz = myCalendar!!.getTimeZone()

        from_date = sdf.format(myCalendar.getTime())
        edt_from_date?.setText(from_date)
    }

    private fun updateLabel1() {
        val myFormat = "dd/MM/yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat,Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val tz = myCalendar!!.getTimeZone()

        from_date1 = sdf.format(myCalendar.getTime())
        edt_delivery_from_date1?.setText(from_date1)
    }

    private fun timepicker() {
        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            from_time = cal.getTimeInMillis()
            edt_from_time!!.setText(SimpleDateFormat("hh:mm a",Locale.US).format(cal.time))
            Log.d("TAG", "time_format-----" + SimpleDateFormat("hh:mm a").format(cal.time))
            edt_to_time!!.setText("")
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

            if (from_time != null && cal.getTimeInMillis() >= from_time!!) {
                //it's after current
                edt_to_time!!.setText(SimpleDateFormat("hh:mm a",Locale.US).format(cal.time))

                Log.d("TAG", "time_format-----" + SimpleDateFormat("hh:mm a",Locale.US).format(cal.time))

            } else {
                //it's before current'
                Toast.makeText(applicationContext, "Invalid Time", Toast.LENGTH_LONG).show()
            }

            //   txt_to_time!!.text=SimpleDateFormat("KK:mm a").format(cal.time)
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

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            from_time1 = cal.getTimeInMillis()
            edt_delivery_from_time1!!.setText(SimpleDateFormat("hh:mm a",Locale.US).format(cal.time))
            Log.d("TAG", "time_format-----" + SimpleDateFormat("hh:mm a",Locale.US).format(cal.time))
            edt_to_time1!!.setText("")
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



            if (from_time1 != null && cal.getTimeInMillis() >= from_time1!!) {
                //it's after current
                edt_to_time1!!.setText(SimpleDateFormat("hh:mm a",Locale.US).format(cal.time))
                Log.d("TAG", "time_format-----" + SimpleDateFormat("hh:mm a",Locale.US).format(cal.time))
            } else {
                //it's before current'
                Toast.makeText(applicationContext, "Invalid Time", Toast.LENGTH_LONG).show()
            }

            //   txt_to_time!!.text=SimpleDateFormat("KK:mm a").format(cal.time)
        }
        TimePickerDialog(
                this,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                false
        ).show()
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
                        //  var list = listPhoneCode.get(i).code
                        city_list.add(cityList.get(i).city.toString())
                    }

                    val adapter = ArrayAdapter(this@CustomOrderPreferenceActivity, android.R.layout.simple_spinner_item, city_list)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    val spinr_city = findViewById<Spinner>(R.id.spinr_city)
                    spinr_city.adapter = adapter

                    spinr_city.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                            city_name = city_list[position]

                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // write code to perform some action
                        }
                    }

                }
            }

            override fun onFailure(call: Call<GetAllCityResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
            }
        })
    }
}