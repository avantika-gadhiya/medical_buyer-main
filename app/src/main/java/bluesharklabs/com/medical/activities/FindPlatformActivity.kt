package bluesharklabs.com.medical.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.blockNum
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.buildName
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.city
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.delType
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.AddOrder
import bluesharklabs.com.medical.model.ZipCode
import bluesharklabs.com.medical.response.*
import bluesharklabs.com.medical.utils.AppConstant
import bluesharklabs.com.medical.utils.AppConstant.photoFinishBitmap
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_find_platform.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class FindPlatformActivity : AppCompatActivity(), View.OnClickListener {

    var types: List<GetAllDropdownResponse.StoreType> = arrayListOf()
    var merch_ct: List<GetAllDropdownResponse.MerchandiseCategory> = arrayListOf()
    var areaList: List<GetAllStoreAreaResponse.Datum> = arrayListOf()
    var zipCodeList: List<GetAllZipCodeResponse.Datum> = arrayListOf()

    var base64Imag = ""

    var md_id = ""
    var type_id = ""
    var city_name = ""
    var area_name = ""
    var zipcode_name = ""
    var zipcode = ""
    var type = ""
    var area = ""
    private var onLocationClicked = false
    private var geocoder: Geocoder? = null
    val PERMISSION_ID = 42
    var isConfirmed = false


    var select_by_location_criteria = "0"
    var select_by_store_criteria = "0"


    private var mFusedLocationClient: FusedLocationProviderClient? = null

    var productsS = AddOrder.Products()

    private var type_list = ArrayList<String>()
    private var typeId_list = ArrayList<String>()
    private var merch_list = ArrayList<String>()
    private var check_merch_list = ArrayList<String>()
    private var merchId_list = ArrayList<String>()
    private var checkedOnes = ArrayList<String>()
    private val checkedItems = booleanArrayOf()
    var booleanArray = BooleanArray(10)

    //area
    private var area_list = ArrayList<String>()

    //zipcode
    private var zipcode_list = ArrayList<String>()


    private var isCityChecked = false
    private var isAreaChecked = false
    private var isZipcodeChecked = false
    private var isTypeChecked = false
    private var isMDChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR//  set status text dark

        setContentView(R.layout.activity_find_platform)

        city_name = city


        btn_sendtplatfrm.setOnClickListener(this)
        img_back.setOnClickListener(this)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geocoder = Geocoder(this@FindPlatformActivity, Locale.US)

        Log.e("HelloLocas", "hi   " + OrderPreferenceActivity.city)
        if (delType.equals("0") && OrderPreferenceActivity.turnOnLocation.equals("0") && (OrderPreferenceActivity.order_lat.equals(
                ""
            ) && OrderPreferenceActivity.order_long.equals(""))) {
            switch_location_search.visibility = View.VISIBLE
        } else {
            switch_location_search.visibility = View.GONE
        }

//        if(delType.equals("0",true)){
//            if(OrderPreferenceActivity.turnOnLocation.equals("0",true) && (OrderPreferenceActivity.order_lat.equals(
//                    ""
//                ) && OrderPreferenceActivity.order_long.equals(""))){
//                switch_location_search.visibility = View.VISIBLE
//            }else{
//                switch_location_search.visibility = View.GONE
//            }
//        }else{
//            switch_location_search.visibility = View.GONE
//        }



        // edt_city.setText(OrderPreferenceActivity.city)
        // edt_area.setText(OrderPreferenceActivity.area)
        // edt_zip_code.setText(OrderPreferenceActivity.zipCode)

        chkbox_by_delivery_location.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {

                select_by_store_criteria = "0"
                select_by_location_criteria = "1"

                set_checkbox_delivery()
                disableEnableTopOptions(false, true)
            } else {
                select_by_store_criteria = "1"
                disableEnableTopOptions(true, false)
            }
        }

        chkbox_by_location.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {

                select_by_store_criteria = "0"
                select_by_location_criteria = "1"

                set_checkbox_location()
                disableEnableTopOptions(false, true)
            } else {
                select_by_store_criteria = "1"
                disableEnableTopOptions(true, false)
            }
        }

        chkbox_by_type.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                isTypeChecked = true
                final_type_id = type_id
                select_by_store_criteria = "1"
            } else {
                isTypeChecked = false
                final_type_id = ""

                if (chkbox_by_location.isChecked || chkbox_by_delivery_location.isChecked) {
                    select_by_store_criteria = "0"
                    select_by_location_criteria = "1"
                } else {
                    select_by_store_criteria = "0"
                    select_by_location_criteria = "0"
                }

            }
        }


        chkbox_by_merch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                isMDChecked = true
                select_by_store_criteria = "1"
            } else {
                isMDChecked = false
                if (chkbox_by_location.isChecked || chkbox_by_delivery_location.isChecked) {
                    select_by_store_criteria = "0"
                    select_by_location_criteria = "1"
                } else {
                    select_by_store_criteria = "0"
                    select_by_location_criteria = "0"
                }
            }
        }

        chkbox_by_merch.isChecked = true
        chkbox_by_merch.isEnabled = false

        chkbox_by_city.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                isCityChecked = true
                finalCity = city_name
                select_by_store_criteria = "1"

                spinr_area.isEnabled = true
                spinr_zipcode.isEnabled = true
                chkbox_by_area.isEnabled = true
                chkbox_by_zipcode.isEnabled = true
            } else {
                isCityChecked = false
                finalCity = ""

                if (chkbox_by_location.isChecked || chkbox_by_delivery_location.isChecked) {
                    select_by_store_criteria = "0"
                    select_by_location_criteria = "1"
                } else {
                    select_by_store_criteria = "0"
                    select_by_location_criteria = "0"
                }

                spinr_area.isEnabled = false
                spinr_zipcode.isEnabled = false
                chkbox_by_area.isEnabled = false
                chkbox_by_zipcode.isEnabled = false
            }
        }
        chkbox_by_city.isChecked = true
        chkbox_by_city.isEnabled = false

        chkbox_by_area.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                isAreaChecked = true
                finalArea = area_name
                select_by_store_criteria = "1"
            } else {
                isAreaChecked = false
                finalArea = ""

                if (chkbox_by_location.isChecked || chkbox_by_delivery_location.isChecked) {
                    select_by_store_criteria = "0"
                    select_by_location_criteria = "1"
                } else {
                    select_by_store_criteria = "0"
                    select_by_location_criteria = "0"
                }
            }
        }
        chkbox_by_zipcode.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                isZipcodeChecked = true
                finalZipcode = zipcode_name
                select_by_store_criteria = "1"
            } else {
                isZipcodeChecked = false
                finalZipcode = ""

                if (chkbox_by_location.isChecked || chkbox_by_delivery_location.isChecked) {
                    select_by_store_criteria = "0"
                    select_by_location_criteria = "1"
                } else {
                    select_by_store_criteria = "0"
                    select_by_location_criteria = "0"
                }
            }
        }


        if (photoFinishBitmap != null) {
            base64Imag = AppConstant.convert(photoFinishBitmap!!)
        }

        getdropdownApi()

        getAllZipCode(city_name)
        getAllArea(city_name)


        if (delType == "2") {
            disableBottomOptions(true, true)
        } else if (delType == "1") {
            disableBottomOptions(false, true)
        } else {
            disableBottomOptions(true, false)
        }

        switch_location_search.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                onLocationClicked = true
                OrderPreferenceActivity.turnOnLocation = "1"
                getLastLocation()
            } else {
                onLocationClicked = false
                OrderPreferenceActivity.turnOnLocation = "0"
                OrderPreferenceActivity.order_lat = ""
                OrderPreferenceActivity.order_long = ""
            }
        }

        if (!chkbox_by_city.isChecked) {
            spinr_area.isEnabled = false
            spinr_zipcode.isEnabled = false
            chkbox_by_area.isEnabled = false
            chkbox_by_zipcode.isEnabled = false
        }

        txt_city_name.text = city_name

        txt_merchandice_cates.setOnClickListener(this)

    }


    fun checkIfAnySelected() {

        if (select_by_store_criteria.equals("1") && !chkbox_by_type.isChecked && !chkbox_by_city.isChecked && !chkbox_by_area.isChecked && !chkbox_by_zipcode.isChecked && !chkbox_by_merch.isChecked) {
            Toast.makeText(
                this,
                "Please select at least one option from Store Criteria.",
                Toast.LENGTH_SHORT
            ).show()
            return
        } else if (select_by_location_criteria.equals("1")) {
            if (delType.equals("0") && !chkbox_by_location.isChecked) {
                Toast.makeText(
                    this,
                    "Please select option from Location Criteria.",
                    Toast.LENGTH_SHORT
                ).show()
                return
            } else if (delType.equals("0") && chkbox_by_location.isChecked) {
                sendToPlatform()
            }

            if (delType.equals("1") && !chkbox_by_delivery_location.isChecked) {
                Toast.makeText(
                    this,
                    "Please select option from Location Criteria.",
                    Toast.LENGTH_SHORT
                ).show()
                return
            } else if (delType.equals("1") && chkbox_by_delivery_location.isChecked) {
                sendToPlatform()
            }
        } else if (select_by_store_criteria.equals("0") && select_by_location_criteria.equals("0") && !chkbox_by_type.isChecked && !chkbox_by_city.isChecked && !chkbox_by_area.isChecked && !chkbox_by_zipcode.isChecked && !chkbox_by_merch.isChecked) {

            if (delType.equals("0") || delType.equals("1")) {
                Toast.makeText(
                    this,
                    "Please select at least one Option from either Store Criteria or Location Criteria.",
                    Toast.LENGTH_SHORT
                ).show()
                return
            } else {
                Toast.makeText(
                    this,
                    "Please select at least one option from Store Criteria.",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
        } else {
            sendToPlatform()
        }

    }

    fun sendToPlatform() {
        //  citye = edt_city.text.toString().trim()
        //  area = edt_area.text.toString().trim()
        //  zipcode = edt_zip_code.text.toString().trim()

        if (select_by_store_criteria.equals("1")) {


            if (isAreaChecked) {
                finalArea = area_name
            } else {
                finalArea = ""
            }


            if (isZipcodeChecked) {
                finalZipcode = zipcode_name
            } else {
                finalZipcode = ""
            }




        }

        if (isCityChecked) {
            finalCity = city_name
        } else {
            finalCity = ""
        }

        if (isMDChecked) {
            if (md_id.equals("")) {
                Toast.makeText(
                    this@FindPlatformActivity,
                    "Please select at least one Merchandise Category",
                    Toast.LENGTH_LONG
                ).show()
                return
            } else {
                final_md_id = md_id
            }


        } else {
            final_md_id = ""
        }

        Log.e("HelloMDS", "HelloFM   " + final_md_id + "   MDID   " + md_id)

        if (isTypeChecked) {
            if (!select_by_store_criteria.equals("1")) {
                final_type_id = ""
            }
        }

        if(isConfirmed){
            addOrderApi()
        }else{
            Toast.makeText(
                this@FindPlatformActivity,
                "Please confirm Merchandise Category",
                Toast.LENGTH_LONG
            ).show()
        }



    }

    private fun disableBottomOptions(isStore: Boolean, isLocation: Boolean) {

        if (isStore) {
            chkbox_by_delivery_location.isChecked = false
            chkbox_by_delivery_location.isEnabled = false
            txt_near_by_delivery_location.setTextColor(
                ContextCompat.getColor(
                    this@FindPlatformActivity,
                    R.color.grey_txt
                )
            )
        }

        if (isLocation) {
            chkbox_by_location.isChecked = false
            chkbox_by_location.isEnabled = false
            txt_near_by_location.setTextColor(
                ContextCompat.getColor(
                    this@FindPlatformActivity,
                    R.color.grey_txt
                )
            )
        }
    }

    private fun disableEnableTopOptions(isEnable: Boolean, isBottomChecked: Boolean) {


//            chkbox_by_type.isChecked = isEnable
        chkbox_by_type.isEnabled = isEnable

        spinr_type.isEnabled = isEnable


//            chkbox_by_merch.isChecked = isEnable
//        chkbox_by_merch.isEnabled = isEnable
//        spinr_mrchn_ctgr.isEnabled = isEnable

//            chkbox_by_city.isChecked = isEnable
//        chkbox_by_city.isEnabled = isEnable

        // edt_city.isClickable = isEnable
        // edt_city.isEnabled = isEnable


//            chkbox_by_area.isChecked = isEnable
        chkbox_by_area.isEnabled = isEnable

        // edt_area.isClickable = isEnable
        // edt_area.isEnabled = isEnable


//            chkbox_by_zipcode.isChecked = isEnable
        chkbox_by_zipcode.isEnabled = isEnable

        // edt_zip_code.isClickable = isEnable
        //  edt_zip_code.isEnabled = isEnable

        if (isEnable) {
            // select_by_store_criteria = "1"

//            textView37.setTextColor(
//                ContextCompat.getColor(
//                    this@FindPlatformActivity,
//                    R.color.black_txt
//                )
//            )

//            edt_city.setTextColor(
//                    ContextCompat.getColor(
//                            this@FindPlatformActivity,
//                            R.color.black_txt
//                    ))

            textView33.setTextColor(
                    ContextCompat.getColor(
                            this@FindPlatformActivity,
                            R.color.black_txt
                    ))

            tvArea.setTextColor(
                    ContextCompat.getColor(
                            this@FindPlatformActivity,
                            R.color.black_txt
                    ))

            tvZipCode.setTextColor(
                    ContextCompat.getColor(
                            this@FindPlatformActivity,
                            R.color.black_txt
                    ))

            if (chkbox_by_city.isChecked) {
                spinr_area.isEnabled = true
                spinr_zipcode.isEnabled = true
                chkbox_by_area.isEnabled = true
                chkbox_by_zipcode.isEnabled = true
            } else {
                spinr_area.isEnabled = false
                spinr_zipcode.isEnabled = false
                chkbox_by_area.isEnabled = false
                chkbox_by_zipcode.isEnabled = false
            }


        } else {
            //  select_by_store_criteria = "0"

//            textView37.setTextColor(
//                ContextCompat.getColor(
//                    this@FindPlatformActivity,
//                    R.color.grey_txt
//                )
//            )


            textView33.setTextColor(
                ContextCompat.getColor(
                    this@FindPlatformActivity,
                    R.color.grey_txt
                ))

            tvArea.setTextColor(
                ContextCompat.getColor(
                    this@FindPlatformActivity,
                    R.color.grey_txt
                ))

            tvZipCode.setTextColor(
                ContextCompat.getColor(
                    this@FindPlatformActivity,
                    R.color.grey_txt
                ))

//            edt_city.setTextColor(
//                    ContextCompat.getColor(
//                            this@FindPlatformActivity,
//                            R.color.grey_txt
//                    ))

//            edt_area.setTextColor(
//                    ContextCompat.getColor(
//                            this@FindPlatformActivity,
//                            R.color.grey_txt
//                    ))

//            edt_zip_code.setTextColor(
//                    ContextCompat.getColor(
//                            this@FindPlatformActivity,
//                            R.color.grey_txt
//                    ))


            spinr_area.isEnabled = false
            spinr_zipcode.isEnabled = false
            chkbox_by_area.isEnabled = false
            chkbox_by_zipcode.isEnabled = false
        }

        select_by_location_criteria = if (isBottomChecked) {
            "1"
        } else {
            "0"
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.txt_merchandice_cates -> {
                multiSelectSpinner()
            }
            R.id.btn_sendtplatfrm -> {

                if (chkbox_by_location.isChecked) {
                    if ((!OrderPreferenceActivity.order_lat.equals(
                            ""
                        ) && !OrderPreferenceActivity.order_long.equals(""))
                    ) {
                        checkIfAnySelected()
                    } else {
                        Toast.makeText(this, "Please Turn on your Location.", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    checkIfAnySelected()
                }


            }
        }
    }

    private fun multiSelectSpinner() {


        val builder: AlertDialog.Builder = AlertDialog.Builder(this@FindPlatformActivity)
        builder.setTitle("Add/Remove Merchandise Category")
        builder.setMultiChoiceItems(merch_list.toArray(arrayOfNulls<String>(merch_list.size)),
            booleanArray,
            OnMultiChoiceClickListener { dialog, which, isChecked ->

                val actual_value = (which + 1)

                if (isChecked) {
                    booleanArray[which] = true
                    checkedOnes.add(actual_value.toString())
                } else {
                    booleanArray[which] = false
                    checkedOnes.remove(actual_value.toString())
                }
            })

        builder.setPositiveButton("Confirm", null)
        builder.setNegativeButton("Close", null)


        val dialog: AlertDialog = builder.create()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val closeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        closeButton.setOnClickListener{
            dialog.dismiss()
        }
        positiveButton.setOnClickListener {

            var isOneSelect = false

            for (values in booleanArray) {
                if (values) {
                    isOneSelect = true
                    break
                }
            }

            var catSID = StringBuilder()

            for (item in checkedOnes) {
                catSID.append(item + ",")
            }
            md_id = catSID.toString()

            if (isOneSelect) {
                progressBar.visibility = View.VISIBLE



                Handler().postDelayed(Runnable {
                    check_merch_list.clear()
//                    booleanArray = BooleanArray(merch_list.size)
                    for (item in checkedOnes) {
                        Log.e("HelloTrye", "Itema   " + item)
                        if (merchId_list.contains(item)) {
                            Log.e(
                                "HelloTrye",
                                "ItemaContains   " + merch_list[item.toInt() - 1]
                            )
                            check_merch_list.add(merch_list[item.toInt() - 1])
                        }
                    }

                    val adapter1 = ArrayAdapter(
                        this@FindPlatformActivity,
                        android.R.layout.simple_spinner_item, check_merch_list
                    )
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinr_mrchn_ctgr.adapter = adapter1

                    progressBar.visibility = View.GONE
                }, 1500)

                final_md_id = md_id

                Log.e("IDTSS", "Hiii   " + md_id)
                isConfirmed = true
                dialog.dismiss()
            } else {
                final_md_id = md_id
                Toast.makeText(
                    this@FindPlatformActivity,
                    "Please select at least one Merchandise Category",
                    Toast.LENGTH_LONG
                ).show()

            }

        }




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

                if (response.isSuccessful) {

//                    merch_list.add(0, "Select Merchandise Category")
//                    merchId_list.add(0, "0")
                    merch_ct = arrayListOf()

                    merch_ct = response.body()!!.data!!.merchandiseCategory!!

                    for (i in 0 until merch_ct.size) {
                        //  var list = listPhoneCode.get(i).code
                        merch_list.add(merch_ct.get(i).name.toString())
                        merchId_list.add(merch_ct.get(i).id.toString())
                    }

                    Handler().postDelayed(Runnable {

                        booleanArray = BooleanArray(merchId_list.size)

                        for (items in 0 until booleanArray.size) {
                            booleanArray[items] = false
                        }


                        var catSID = StringBuilder()

                        for (i in CustomOrderActivity.arrProduct) {
                            if (merchId_list.contains(i.product_category)) {

                                checkedOnes.add(i.product_category!!)
                                catSID.append(i.product_category!! + ",")
                                check_merch_list.add(merch_list[i.product_category!!.toInt() - 1])
                            }

                            for (k in 0 until merchId_list.size) {
                                if (i.product_category.equals(merchId_list[k])) {
                                    Log.e("HelloTrye", "True")
                                    booleanArray[k] = true
                                    break
                                }
//                                else {
//                                    Log.e("HelloTrye", "False")
//                                    booleanArray[k] = false
//                                }
                            }
                        }

                        val adapter1 = ArrayAdapter(
                            this@FindPlatformActivity,
                            android.R.layout.simple_spinner_item, check_merch_list
                        )
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinr_mrchn_ctgr.adapter = adapter1

                        md_id = catSID.toString()
                        final_md_id = md_id

                        Log.e("IDTSS", "Progrees   " + md_id)
                        progressBar.visibility = View.GONE
                    }, 1500)

                    types = arrayListOf()

                    types = response.body()!!.data!!.storeType!!

                    for (i in 0 until types.size) {
                        //  var list = listPhoneCode.get(i).code
                        type_list.add(types.get(i).name.toString())
                        typeId_list.add(types.get(i).id.toString())
                    }

                    val adapter = ArrayAdapter(
                        this@FindPlatformActivity,
                        android.R.layout.simple_spinner_item,
                        type_list
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinr_type.adapter = adapter

                    for (i in 0 until types.size) {
                        if (types.get(i).id.equals(type)) {
                            spinr_type.setSelection(i, true)
                            break
                        }

                    }


                    spinr_type.onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View,
                            position: Int,
                            id: Long
                        ) {

                            if (position > 0) {
                                type_id = type_list[position]
                            }

//                            type_id = typeId_list[position]
                            // code= phonecode_list[position]
                            //  code = phonecode_list[position].removePrefix("+")

                            /*  Toast.makeText(this@FindPlatformActivity,
                                  "" + type_list[position], Toast.LENGTH_SHORT).show()*/
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // write code to perform some action
                        }
                    }

                }
            }

            override fun onFailure(call: Call<GetAllDropdownResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
            }
        })
    }

    private fun set_checkbox_location() {
        chkbox_by_delivery_location.isChecked = false

        locationCriteria = "1"
        locationCriteriaDelivery = "0"

        txt_near_by_delivery_location.setTextColor(
            ContextCompat.getColor(
                this@FindPlatformActivity,
                R.color.grey_txt
            )
        )
        txt_near_by_location.setTextColor(
            ContextCompat.getColor(
                this@FindPlatformActivity,
                R.color.black_txt
            )
        )
    }

    private fun set_checkbox_delivery() {
        chkbox_by_location.isChecked = false

        locationCriteria = "0"
        locationCriteriaDelivery = "1"

        txt_near_by_delivery_location.setTextColor(
            ContextCompat.getColor(
                this@FindPlatformActivity,
                R.color.black_txt
            )
        )
        txt_near_by_location.setTextColor(
            ContextCompat.getColor(
                this@FindPlatformActivity,
                R.color.grey_txt
            )
        )

    }

    private fun getTypeListApi() {
        progressBar.visibility = View.VISIBLE
        val apiService = AppConstant.getClient()!!.create(RetrofitInterface::class.java)

        val call = apiService.getstoretype()

        call.enqueue(object : Callback<GetStoreTypeResponse> {
            override fun onResponse(
                call: Call<GetStoreTypeResponse>,
                response: Response<GetStoreTypeResponse>
            ) {

                progressBar.visibility = View.GONE
                if (response.isSuccessful) {


                }
            }

            override fun onFailure(call: Call<GetStoreTypeResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
            }
        })
    }


    private fun getAllZipCode(city: String) {
        progressBar.visibility = View.VISIBLE
        val apiService = AppConstant.getClient()!!.create(RetrofitInterface::class.java)
        val filter = ZipCode()
        filter.city = city

        val call = apiService.getAllStoreZipcode(filter)

        call.enqueue(object : Callback<GetAllZipCodeResponse> {
            override fun onResponse(
                call: Call<GetAllZipCodeResponse>,
                response: Response<GetAllZipCodeResponse>
            ) {

                progressBar.visibility = View.GONE
                if (response.isSuccessful) {

                    zipcode_list.clear()
                    zipcode_list.add(0, "Select Zipcode")

                    zipCodeList = response.body()?.data!!
                    for (i in 0 until zipCodeList.size) {
                        //  var list = listPhoneCode.get(i).code
                        zipcode_list.add(zipCodeList.get(i).zipcode.toString())
                    }

                    val adapter = ArrayAdapter(
                        this@FindPlatformActivity,
                        android.R.layout.simple_spinner_item,
                        zipcode_list
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinr_zipcode.adapter = adapter

                    for (i in 0 until zipCodeList.size) {
                        if (zipCodeList.get(i).zipcode.equals(zipcode)) {
                            spinr_zipcode.setSelection(i, true)
                            break
                        }

                    }

                    spinr_zipcode.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View,
                                position: Int,
                                id: Long
                            ) {

                                zipcode_name = zipcode_list[position]

                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                                // write code to perform some action
                            }
                        }

                }
            }

            override fun onFailure(call: Call<GetAllZipCodeResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
            }
        })
    }

    private fun getAllArea(city: String) {
        progressBar.visibility = View.VISIBLE
        val apiService = AppConstant.getClient()!!.create(RetrofitInterface::class.java)
        val filter = ZipCode()
        filter.city = city
        val call = apiService.getAllStoreArea(filter)

        call.enqueue(object : Callback<GetAllStoreAreaResponse> {
            override fun onResponse(
                call: Call<GetAllStoreAreaResponse>,
                response: Response<GetAllStoreAreaResponse>
            ) {

                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    area_list.clear()
                    area_list.add(0, "Select Area")

                    areaList = response.body()?.data!!
                    for (i in 0 until areaList.size) {
                        //  var list = listPhoneCode.get(i).code
                        area_list.add(
                            areaList.get(i).area
                                .toString()
                        )
                    }

                    val adapter = ArrayAdapter(
                        this@FindPlatformActivity,
                        android.R.layout.simple_spinner_item,
                        area_list
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinr_area.adapter = adapter

                    for (i in 0 until areaList.size) {
                        if (areaList.get(i).area.equals(area)) {
                            spinr_area.setSelection(i, true)
                            break
                        }

                    }

                    spinr_area.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View,
                                position: Int,
                                id: Long
                            ) {
                                if (position > 0) {
                                    area_name = area_list[position]
                                }

                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                                // write code to perform some action
                            }
                        }

                }
            }

            override fun onFailure(call: Call<GetAllStoreAreaResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
            }
        })
    }

    fun addOrderApi() {
        progressBar.visibility = View.VISIBLE
        val addorder = AddOrder()
        addorder.user_id = AppConstant.getPreferenceText(AppConstant.PREF_USER_ID)
        addorder.area = OrderPreferenceActivity.area
        addorder.block_no = blockNum
        addorder.building_name = buildName
        addorder.delivery_latitude = OrderPreferenceActivity.del_lat
        addorder.delivery_longitude = OrderPreferenceActivity.del_long
        addorder.delivery_type = delType
        addorder.delivery_city = OrderPreferenceActivity.delivery_city
        addorder.landmark = OrderPreferenceActivity.landMark
        addorder.location = OrderPreferenceActivity.locaTion

        addorder.order_for = OrderingForActivity.orderFor
        addorder.store_latitude = ""
        addorder.store_longitude = ""
        addorder.order_latitude = OrderPreferenceActivity.order_lat
        addorder.order_longitude = OrderPreferenceActivity.order_long
        addorder.order_preference = OrderPreferenceActivity.drugPrefrnc
        addorder.buying_preference = OrderPreferenceActivity.buyingPrefrnc
        addorder.photo_id = OrderPreferenceActivity.photoId
        addorder.prescription_image = base64Imag
        addorder.start_time = OrderPreferenceActivity.startTime
        addorder.end_time = OrderPreferenceActivity.endTime
        addorder.delivery_date = OrderPreferenceActivity.delDate
        addorder.to_delivery_date = OrderPreferenceActivity.toDeliveryDate
        addorder.store_criteria_area = finalArea
        addorder.store_criteria_city = finalCity


        val sb = StringBuffer(final_md_id)
        sb.deleteCharAt(sb.length - 1)

        addorder.store_criteria_store_md_id = sb.toString()
        addorder.store_criteria_store_type_id = final_type_id
        addorder.store_criteria_zipcode = finalZipcode
        addorder.street = OrderPreferenceActivity.streeT
        addorder.turn_on_location = OrderPreferenceActivity.turnOnLocation
        addorder.zipcode = OrderPreferenceActivity.zipCode
        addorder.products = PrescriptionOrderActivity.array
        addorder.custom_products = CustomOrderActivity.arrProduct
        addorder.order_platform = "1"
        addorder.store_id = ""


        addorder.select_by_location_criteria = select_by_location_criteria
        addorder.select_by_store_criteria = select_by_store_criteria
        addorder.location_criteria_delivery_location = locationCriteriaDelivery
        addorder.location_criteria_location = locationCriteria


        val apiService =
            AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.addorder(addorder)
        call.enqueue(object : Callback<AddOrderResponse> {
            override fun onResponse(
                call: Call<AddOrderResponse>,
                response: retrofit2.Response<AddOrderResponse>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    startActivity(
                        Intent(this@FindPlatformActivity, OrderSuccessfulActivity::class.java)
                            .putExtra("fromplatform", "1")
                    )
                    //finish()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                            this@FindPlatformActivity,
                            "" + jObjError.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@FindPlatformActivity, e.message, Toast.LENGTH_LONG)
                            .show()
                    }

                }
            }

            override fun onFailure(call: Call<AddOrderResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar.visibility = View.GONE
            }
        })
    }


    companion object {
        // var array = ArrayList<AddOrder.Products>()
        var final_type_id = ""
        var final_md_id = ""
        var finalCity = ""
        var finalZipcode = ""
        var finalArea = ""
        var locationCriteriaDelivery = ""
        var locationCriteria = ""
    }

    override fun onResume() {
        super.onResume()
        if (onLocationClicked && isLocationEnabled()) {
            switch_location_search.isChecked = true
            getLastLocation()
        } else {
            switch_location_search.isChecked = false
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
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
            val mLastLocation: Location = locationResult.lastLocation


            val latitude = mLastLocation.latitude
            val longitude = mLastLocation.longitude

            OrderPreferenceActivity.order_lat = latitude.toString()
            OrderPreferenceActivity.del_lat = latitude.toString()
            OrderPreferenceActivity.order_long = longitude.toString()
            OrderPreferenceActivity.del_long = longitude.toString()


            val addresses = geocoder!!.getFromLocation(latitude, longitude, 1)
            val addressLine1 = addresses.get(0).getAddressLine(0)
            //  Log.e("line1", addressLine1)

            val citye = addresses.get(0).locality
            val city3 = addresses.get(0).subLocality
            val pinCode = addresses.get(0).postalCode

//            OrderPreferenceActivity.city = citye
            if (city3 != null) {
//                OrderPreferenceActivity.area = city3
            }
//            OrderPreferenceActivity.zipCode = pinCode

            // txt_location.setText(addressLine1)
            OrderPreferenceActivity.locaTion = addressLine1
            //  findViewById<TextView>(R.id.latTextView).text = mLastLocation.latitude.toString()
            // findViewById<TextView>(R.id.lonTextView).text = mLastLocation.longitude.toString()
        }
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

                        OrderPreferenceActivity.order_lat = latitude.toString()
                        OrderPreferenceActivity.del_lat = latitude.toString()
                        OrderPreferenceActivity.order_long = longitude.toString()
                        OrderPreferenceActivity.del_long = longitude.toString()

                        val addresses = geocoder!!.getFromLocation(latitude, longitude, 1)

                        val addressLine1 = addresses.get(0).getAddressLine(0)
                        val citye = addresses.get(0).locality
                        val city3 = addresses.get(0).subLocality

                        val pinCode = addresses.get(0).postalCode

//                        OrderPreferenceActivity.city = citye
                        if (city3 != null) {
//                            OrderPreferenceActivity.area = city3

                        }
//                        OrderPreferenceActivity.zipCode = pinCode
                        OrderPreferenceActivity.locaTion = addressLine1


                        // edt_city.setText(OrderPreferenceActivity.city)
                        // edt_area.setText(OrderPreferenceActivity.area)
                        // edt_zip_code.setText(OrderPreferenceActivity.zipCode)

                    }
                }
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
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
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }
}