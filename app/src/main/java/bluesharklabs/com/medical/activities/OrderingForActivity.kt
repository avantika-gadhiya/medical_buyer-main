package bluesharklabs.com.medical.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.StoreDetail
import bluesharklabs.com.medical.response.GetAllDropdownResponse
import bluesharklabs.com.medical.response.StoreDetailResponse
import bluesharklabs.com.medical.utils.AppConstant
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_order_preference.*
import kotlinx.android.synthetic.main.activity_ordering_for.*
import kotlinx.android.synthetic.main.activity_ordering_for.edt_gst_no
import kotlinx.android.synthetic.main.activity_ordering_for.img_back
import kotlinx.android.synthetic.main.activity_ordering_for.img_store
import kotlinx.android.synthetic.main.activity_ordering_for.progressBar
import kotlinx.android.synthetic.main.activity_ordering_for.txt_drug_licens_no
import kotlinx.android.synthetic.main.activity_ordering_for.txt_establis_since
import kotlinx.android.synthetic.main.activity_ordering_for.txt_geo_location
import kotlinx.android.synthetic.main.activity_ordering_for.txt_mrchandiz_category
import kotlinx.android.synthetic.main.activity_ordering_for.txt_ownr_name
import kotlinx.android.synthetic.main.activity_ordering_for.txt_pharmacist_name
import kotlinx.android.synthetic.main.activity_ordering_for.txt_prefrd_paymnt
import kotlinx.android.synthetic.main.activity_ordering_for.txt_stor_nm
import kotlinx.android.synthetic.main.activity_ordering_for.txt_store_add
import kotlinx.android.synthetic.main.activity_store_detail.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class OrderingForActivity : AppCompatActivity(), View.OnClickListener {

    private var order_type = ""
    private var order_id = ""
    private var isUserInteracting = false
    private var isEditable = false
    private var isCustom = true
    private var latitude = ""
    private var longitude = ""

    var orderingForArray: List<GetAllDropdownResponse.OrderingFor> = arrayListOf()

    private var orderingFor_list: ArrayList<String> = ArrayList<String>()
    private var orderingForId_list: ArrayList<String> = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//  set status text dark

        setContentView(R.layout.activity_ordering_for)
        val spinner1 = findViewById<Spinner>(R.id.spinner)


        if (intent != null) {

            isEditable = intent.getBooleanExtra("edit", isEditable)
            isCustom = intent.getBooleanExtra("custom", true)
            orderForTxt= intent.getStringExtra("ORDER_FOR")
            orderFor= intent.getStringExtra("ORDER_ID")
        }

        getdropdownApi()


        if (spinner1 != null) {
            val arrayAdapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, orderingFor_list)
            spinner1.adapter = arrayAdapter

            if (isEditable) {
                var spinpos = 0
                for (i in 0 until orderingFor_list.size) {

                    if (order_type.equals(orderingFor_list.get(i))) {
                        spinpos = i
                    }
                    spinner1.setSelection(spinpos)
                }
            }

            spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                ) {


                    if (isUserInteracting) {
                        isEditable = false
                    }


                    if (!isEditable) {
                        order_type = orderingFor_list[position]
                        order_id = orderingForId_list[position]

                        setLayout()
                    }
                    //order_type=orderingFor_list[position]


                    /*Toast.makeText(
                        this@OrderingForActivity,
                        "Slected Item" + " " + orderingFor[position],
                        Toast.LENGTH_SHORT
                    ).show()*/

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }
        //myStoreDetailAvail
        btn_review_order.setOnClickListener(this)
        img_back.setOnClickListener(this)
        txt_store_add.setOnClickListener(this)
    }

    override fun onUserInteraction() {
        super.onUserInteraction()

        isUserInteracting = true
    }

    fun storeDetailApi() {

        progressBar.visibility = View.VISIBLE
        val storeDetail = StoreDetail()
        storeDetail.phone = AppConstant.getPreferenceText(AppConstant.PREF_USER_PHONE)

        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.storedetail(storeDetail)

        call.enqueue(object : Callback<StoreDetailResponse> {
            override fun onResponse(
                    call: Call<StoreDetailResponse>,
                    response: retrofit2.Response<StoreDetailResponse>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {


                    btn_review_order.setBackgroundColor(
                            ContextCompat.getColor(
                                    this@OrderingForActivity,
                                    R.color.colorAccent
                            )
                    )
                    btn_review_order.setTextColor(
                            ContextCompat.getColor(
                                    this@OrderingForActivity,
                                    R.color.colorWhite
                            )
                    )
                    btn_review_order.setText(R.string.confirm_proceed)
                    btn_review_order.isEnabled = true

                    constraint_no_info.visibility = View.GONE
                    constraint_ordering_for.visibility = View.VISIBLE
                    btn_review_order.setText(R.string.confrm_n_proceed)


                    val locaTion =
                            response.body()?.data?.shopNo + ", " + response.body()!!.data!!.building + ", " + response.body()?.data?.street + ", " + response.body()?.data?.area + ", " + response.body()?.data?.landmark + ", " + response.body()?.data?.city + ", " + response.body()?.data?.zipcode

                    txt_stor_nm.setText(response.body()?.data?.name)
                    txt_store_add.setText(response.body()?.data?.geoLocation)
                    txt_stor_type.setText(response.body()?.data?.type)
                    txt_ownr_name.setText(response.body()?.data?.ownerName)
                    edt_gst_no.setText(response.body()?.data?.gstNumber)
                    txt_drug_licens_no.setText(response.body()?.data?.establishedSince)
                    txt_establis_since.setText(response.body()?.data?.drugLicenseNumber)
                    txt_geo_location.setText(locaTion)
                    txt_pharmacist_name.setText(response.body()?.data?.registeredPharmacistName)

                    val input :String=response.body()?.data?.paymentMethod!!
                    val builder = StringBuilder()
                    val items = input.split("\\p{Punct}".toRegex()).toTypedArray()
                    for (details in items) {
                        builder.append(details + "\n")
                    }
                    txt_prefrd_paymnt.setText(builder.toString())

                    val category :String=response.body()?.data?.merchandiseCategory!!
                    val builderr = StringBuilder()
                    val merchandise = category.split("\\p{Punct}".toRegex()).toTypedArray()
                    for (details in merchandise) {
                        builderr.append(details + "\n")
                    }
                    txt_mrchandiz_category.setText(builderr.toString())

                   latitude= response.body()?.data?.latitude.toString()
                   longitude= response.body()?.data?.longitude.toString()

                    //  storeid = response.body()!!.data!!.id.toString()
                    Glide.with(applicationContext)  //2
                            .load(response.body()?.data?.storePhoto) //3
                            .centerCrop() //4
                            .placeholder(R.drawable.ic_launcher_background) //5
                            .into(img_store) //8*/

                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@OrderingForActivity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@OrderingForActivity, e.message, Toast.LENGTH_LONG)
                                .show()
                    }

                }
            }


            override fun onFailure(call: Call<StoreDetailResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar.visibility = View.GONE
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txt_store_add -> {
                val currentlat= MainActivity.current_latitude
                val currentlong= MainActivity.current_longitude
                Log.e("HelloBroad","OrderingFOr   "+currentlat+"  currentlong   "+currentlong)

                val intent = Intent(Intent.ACTION_VIEW,Uri.parse("http://maps.google.com/maps?daddr="+latitude+","+longitude))
                startActivity(intent)
            }
            R.id.img_back -> {
                finish()
            }
            R.id.btn_review_order -> {

                Log.e("HelloReOrde", "OnComes   isCustom "+ isCustom)


                if (isCustom) {
                    startActivity(Intent(this@OrderingForActivity, CustomFinalOrderActivity::class.java)
                            //.putExtra("offerId", offerId)
                    )
                    //finish()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                } else {
                    startActivity(
                            // Intent(this@OrderingForActivity, FinalOrderActivity::class.java)
                            Intent(this@OrderingForActivity, CustomFinalOrderActivity::class.java)
                                    .putExtra("custom",false)
                            //.putExtra("offerId", offerId)
                    )
                    //finish()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
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

                progressBar.visibility = View.GONE

                if (response.isSuccessful()) {

                    orderingForArray = arrayListOf()
                    orderingFor_list = ArrayList()
                    orderingForId_list = ArrayList()


                    orderingForArray = response.body()!!.data!!.orderingFor!!

                    for (i in 0 until orderingForArray.size) {
                        //  var list = listPhoneCode.get(i).code
                        orderingFor_list.add(orderingForArray.get(i).name.toString())
                        orderingForId_list.add(orderingForArray.get(i).id.toString())
                    }


                    val adapter = ArrayAdapter(
                            this@OrderingForActivity,
                            android.R.layout.simple_spinner_item, orderingFor_list
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item)
                    spinner.adapter = adapter

                    if (isEditable) {
                        order_type = orderForTxt.toString()
                        order_id= orderFor.toString()

                        var pos = 0
                        for (i in 0 until orderingFor_list.size) {

                            if (order_id.equals(orderingForId_list[i])) {
                                pos = i
                                order_id=orderingForId_list.get(i)
                            }
                            spinner.setSelection(pos)
                        }
                        setLayout()
                    }

                }

            }

            override fun onFailure(call: Call<GetAllDropdownResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
            }
        })
    }

    fun setLayout() {


        if (!order_type.equals("My Pharmacy Store")) {

            orderFor = order_id
            orderForTxt = order_type

            constraint_ordering_for.visibility = View.GONE
            btn_review_order.setText(R.string.review_order)
            constraint_no_info.visibility = View.GONE
            btn_review_order.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
            btn_review_order.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
            btn_review_order.isEnabled = true
        } else {

            orderFor = order_id
            orderForTxt = order_type
            constraint_no_info.visibility = View.VISIBLE

            btn_review_order.setBackgroundColor(
                    ContextCompat.getColor(
                            this,
                            R.color.colorLightGray
                    )
            )
            btn_review_order.setTextColor(ContextCompat.getColor(this, R.color.black_txt))
            btn_review_order.isEnabled = false
            storeDetailApi()


        }
    }


    companion object {
        var orderFor: String? = ""
        var orderForTxt: String? = ""

    }
}


