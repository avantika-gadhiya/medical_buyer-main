package bluesharklabs.com.medical.activities

import android.R.color
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.BuyerOrderAccept
import bluesharklabs.com.medical.model.GetStoreOfferOrders
import bluesharklabs.com.medical.model.OrderDetailForPickup
import bluesharklabs.com.medical.response.BuyerOrderAcceptResponse
import bluesharklabs.com.medical.response.GetStoreOfferOrdersResponse
import bluesharklabs.com.medical.response.OrderDetailForPickupResponse
import bluesharklabs.com.medical.utils.AppConstant
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_offer_accepted.*
import kotlinx.android.synthetic.main.activity_store_order_accepted.*
import kotlinx.android.synthetic.main.activity_store_order_accepted.img_back
import kotlinx.android.synthetic.main.activity_store_order_accepted.progressBar
import kotlinx.android.synthetic.main.activity_store_order_accepted.textView51
import kotlinx.android.synthetic.main.activity_store_order_accepted.textView52
import kotlinx.android.synthetic.main.activity_store_order_accepted.txt_buying_pref
import kotlinx.android.synthetic.main.activity_store_order_accepted.txt_del_date
import kotlinx.android.synthetic.main.activity_store_order_accepted.txt_del_pref
import kotlinx.android.synthetic.main.activity_store_order_accepted.txt_del_pref_add
import kotlinx.android.synthetic.main.activity_store_order_accepted.txt_geo_location
import kotlinx.android.synthetic.main.activity_store_order_accepted.txt_status
import kotlinx.android.synthetic.main.activity_store_order_accepted.txt_status1
import kotlinx.android.synthetic.main.activity_store_order_accepted.txt_view_order
import kotlinx.android.synthetic.main.activity_store_order_accepted.view2
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback


class StoreOrderAcceptedActivity : AppCompatActivity(), View.OnClickListener {


    var orderId = ""
    var storeId = ""
    var offerId = ""
    var isAccepted = "0"
    var phoneNo = ""
    var isCustom = ""
    var latitude = ""
    var longitude = ""
    var store_latitude = ""
    var store_longitude = ""
    var data: OrderDetailForPickupResponse.Data? = null
    var storeData: OrderDetailForPickupResponse.StoreDetail? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//  set status text dark

        setContentView(R.layout.activity_store_order_accepted)

        Log.e("HelloReOrde","HIi   ")


        img_back.setOnClickListener(this)
        img_store_detail.setOnClickListener(this)
        txt_view_order.setOnClickListener(this)
        rel_price_reorder.setOnClickListener(this)
        rel_price_accept.setOnClickListener(this)
        constraint1.setOnClickListener(this)
        txt_view_invoice.setOnClickListener(this)
        txt_track_ordr_n_make_payment.setOnClickListener(this)
        txt_geo_location.setOnClickListener(this)

        //received an offer from ur store
        // setLayout1()

        //received an offer from ur store
        // setLayout2()
        if (intent != null) {
            if (intent.getStringExtra("status") != null) {
                val status = intent.getStringExtra("status")

                if (status.equals("Offer Made", ignoreCase = true)) {
                    setLayout1()
                } else if (status.equals("Invoiced", ignoreCase = true)) {
                    setLayout3()
                } else if (status.equals("Accepted", ignoreCase = true)) {
                    setLayout2()
                }

            }
            if (intent.getStringExtra("orderid") != null) {
                orderId = intent.getStringExtra("orderid")
                Log.e("HelloReOrde","orderDetailApi   ")
                orderDetailApi()
            }
        }

        //order hass been invoiced

    }

    private fun setTextViewDrawableColor(textView: TextView, color: Int) {
        for (drawable in textView.compoundDrawables) {
            if (drawable != null) {
                drawable.setTint(ContextCompat.getColor(textView.context, color))/* =
                    PorterDuffColorFilter(
                        ContextCompat.getColor(textView.context, color),
                        PorterDuff.Mode.MULTIPLY
                    )*/
            }
        }
    }

    private fun setLayout3() {
        txt_order_status.visibility = View.INVISIBLE
        txt_order_invoicd.visibility = View.VISIBLE
        txt_awaiting_stor_invoice.visibility = View.GONE

        constraint1.visibility = View.VISIBLE
        constraint3.visibility = View.VISIBLE
    }

    private fun setLayout2() {
        txt_order_status.visibility = View.INVISIBLE
        txt_awaiting_stor_invoice.visibility = View.VISIBLE
        txt_recivd_ofr_frm_stor.visibility = View.GONE
        setTextViewDrawableColor(txt_order_status, R.color.colorAccent)
        constraint1.visibility = View.VISIBLE

        view2.visibility = View.GONE
        rel_price_reorder.visibility = View.GONE
        rel_price_accept.visibility = View.GONE
        view3.visibility = View.GONE
    }

    private fun setLayout1() {

        txt_order_status.visibility = View.INVISIBLE
        txt_recivd_ofr_frm_stor.visibility = View.VISIBLE

        view2.visibility = View.VISIBLE
        constraint1.visibility = View.VISIBLE
        rel_price_reorder.visibility = View.VISIBLE
        rel_price_accept.visibility = View.VISIBLE
        view3.visibility = View.VISIBLE
    }


    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.txt_geo_location -> {

                Log.e("HElloLATLONGS","Latitude   "+latitude+"   Longitude   "+longitude+"   Store_lat   "+store_latitude+"   Store_long   "+store_longitude)

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + latitude + "," + longitude + "&daddr=" + store_latitude + "," + store_longitude))

//                val intent = Intent(Intent.ACTION_VIEW,Uri.parse("http://maps.google.com/maps?daddr="+latitude+","+longitude))
                startActivity(intent)
            }
            R.id.img_back -> {
                finish()
            }
            R.id.img_store_detail -> {
                if (!phoneNo.equals("")) {
                    startActivity(
                            Intent(
                                    this@StoreOrderAcceptedActivity,
                                    StoreDetailActivity::class.java
                            )
                                    .putExtra("storeoffer", "1")
                                    .putExtra("phone", phoneNo)
                    )
                    //finish()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
            R.id.txt_view_order -> {

                Log.e("HelloReOrde","txt_view_order   ")
                if (isCustom.equals("0")) {
                    startActivity(
                            Intent(this@StoreOrderAcceptedActivity, FinalOrderActivity::class.java)
                                    .putExtra("vieworder", "1")
                                    .putExtra("orderid", orderId)
                                    .putExtra("offerId", offerId)
                    )
                    //finish()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                } else {
                    startActivity(
                            Intent(this@StoreOrderAcceptedActivity, CustomFinalOrderActivity::class.java)
                                    .putExtra("vieworder", "1")
                                    .putExtra("orderid", orderId)
                                    .putExtra("offerId", offerId)
                    )
                    //finish()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
            R.id.rel_price_reorder -> {
                Log.e("HelloReOrde","rel_price_reorder   ")
                if (isCustom.equals("0")) {
                    Log.e("HelloReOrde","rel_price_reorder  if ")
                    startActivity(
                            Intent(this@StoreOrderAcceptedActivity, FinalOrderActivity::class.java)
                                    .putExtra("reorder", "1")
                                    .putExtra("offerId", offerId)
                                    .putExtra("orderid", orderId) // changes RahulB
                    )
                    //finish()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                } else {
                    Log.e("HelloReOrde","rel_price_reorder  else ")
                    startActivity(
                            Intent(this@StoreOrderAcceptedActivity, CustomFinalOrderActivity::class.java)
                                    .putExtra("reorder", "1")
                                    .putExtra("offerId", offerId)
                                    .putExtra("orderid", orderId) // changes RahulB
                    )
                    //finish()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
            R.id.rel_price_accept -> {

              offerAcceptApi()
                Log.e("HelloReOrde","rel_price_accept   ")
            }
            R.id.constraint1 -> {
                Log.e("HelloReOrde","txt_view_offer   ")
                startActivity(
                        Intent(this@StoreOrderAcceptedActivity, ViewOfferActivity::class.java)
                                .putExtra("orderid", orderId)
                                .putExtra("storeid", storeId)
                                .putExtra("isAccepted", isAccepted)
                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.txt_view_invoice -> {

                startActivity(
                        Intent(this@StoreOrderAcceptedActivity, InvoiceActivity::class.java)
                                .putExtra("offerId", offerId)
                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.txt_track_ordr_n_make_payment -> {
                Log.e("HelloReOrde","txt_track_ordr_n_make_payment   ")
                startActivity(
                        Intent(this@StoreOrderAcceptedActivity, TrackOrderActivity::class.java)
                                .putExtra("orderid", orderId)
                                .putExtra("offerId", offerId)
                                .putExtra("orderType", isCustom)
                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

    fun orderDetailApi() {

        progressBar.visibility = View.VISIBLE
        val orderDetailForPickup = OrderDetailForPickup()
        orderDetailForPickup.order_id = orderId
        orderDetailForPickup.user_id = AppConstant.getPreferenceText(AppConstant.PREF_USER_ID)

        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.orderdetail(orderDetailForPickup)

        call.enqueue(object : Callback<OrderDetailForPickupResponse> {
            override fun onResponse(
                    call: Call<OrderDetailForPickupResponse>,
                    response: retrofit2.Response<OrderDetailForPickupResponse>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {

                    data = response.body()?.data
                    storeData = response.body()?.data?.storeDetail

                    txt_id.text = "ID: " + response.body()!!.data!!.orderNo!!
                    isCustom = response.body()!!.data!!.orderType.toString()

                    txt_date.text = response.body()?.data!!.createdDate!!
                    txt_status.text = response.body()!!.data!!.orderStatus!!
                    txt_status1.text = response.body()!!.data!!.requestStatus!!

                    DrawableCompat.setTint(
                            txt_status1.getBackground(),
                            ContextCompat.getColor(
                                    this@StoreOrderAcceptedActivity,
                                    AppConstant.setsStatus(response.body()!!.data!!.requestStatus!!)
                            )
                    )

                    DrawableCompat.setTint(
                            txt_status.getBackground(),
                            ContextCompat.getColor(
                                    this@StoreOrderAcceptedActivity,
                                    AppConstant.setsStatus(response.body()!!.data!!.orderStatus!!)
                            )
                    )
                    if (!storeData?.storePhoto.equals("")) {
                        Picasso.get()
                                .load(storeData?.storePhoto)
                                .into(img_store)
                    }

                    txt_stor_nm.text = storeData?.name
                    phoneNo = storeData?.phone.toString()
                    storeId = storeData?.id.toString()

//                    store_latitude = response.body()!!.data!!.store

                    Log.e("HeLoooDelivery","deliveryType   "+response.body()!!.data!!.deliveryType!!+"   deliveryTypeName   "+response.body()!!.data!!.deliveryTypeName!!)


                    if (!storeId.equals("")) {
                        if (!response.body()!!.data!!.orderStatus!!.equals("Pending", ignoreCase = true)) {
                            Log.e("HeLoooDelivery","offerMadeApi   ")
                            offerMadeApi()
                        }
                    }
                    val locaTion =
                            storeData?.shopNo + ", " + storeData?.street + ", " + storeData?.area + ", " + storeData?.landmark + ", " + storeData?.city + ", " + storeData?.zipcode
                    txt_stor_add.text = locaTion
                    if (response.body()!!.data!!.orderPreferenceName != null && !response.body()!!.data!!.orderPreferenceName!!.equals("")) {
                        txt_buying_pref.text = response.body()!!.data!!.orderPreferenceName!!
                    } else {
                        txt_buying_pref.text = response.body()!!.data!!.buyingPreferenceName!!
                    }



                    txt_del_pref.text = response.body()!!.data!!.deliveryTypeName!!


                    if (response.body()!!.data!!.deliveryType.equals("0")) {

                        textView51.text = resources.getString(R.string.pickup_date)
                        textView52.text = resources.getString(R.string.pickup_time)

                        txt_del_pref_add.visibility = View.GONE
                        txt_geo_location.visibility = View.GONE

                    } else if (response.body()!!.data!!.deliveryType.equals("1")) {
                        txt_del_pref.text  = response.body()!!.data!!.deliveryTypeName!! + " in "+response.body()!!.data!!.deliveryCity!!
                        textView51.text = resources.getString(R.string.deliveryDate)
                        textView52.text = resources.getString(R.string.deliveryTime)
                        txt_geo_location.visibility = View.VISIBLE
                        val locaTion2 =
                                response.body()!!.data!!.blockNo + ", " + response.body()!!.data!!.buildingName
                        txt_del_pref_add.text = locaTion2
                        txt_geo_location.text = response.body()?.data!!.location
                        latitude = response.body()?.data?.deliveryLatitude.toString()
                        longitude = response.body()?.data?.deliveryLongitude.toString()
                        store_latitude = storeData?.store_latitude!!
                        store_longitude = storeData?.store_longitude!!
                    } else {
                        txt_del_pref.text  = response.body()!!.data!!.deliveryTypeName!! + " in "+ response.body()!!.data!!.deliveryCity!!
                        val locaTion11 =
                                response.body()!!.data!!.blockNo + ", " + response.body()!!.data!!.buildingName + ", " + response.body()!!.data!!.street + ", " +
                                        response.body()!!.data!!.area + ", " + response.body()!!.data!!.landmark + ", " + response.body()!!.data!!.zipcode
                        txt_del_pref_add.text = locaTion11
                    }


                    txt_del_date.text = response.body()?.data?.todateFromdate


                    if(response.body()?.data!!.requestStatus!!.equals("Timed out",true)){
                        if(response.body()?.data!!.orderStatus!!.equals("Offer Made",true)){
                            ForDeliveryTimeOut()

                        }
                    }




                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@StoreOrderAcceptedActivity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@StoreOrderAcceptedActivity, e.message, Toast.LENGTH_LONG).show()
                    }

                }
            }

            override fun onFailure(call: Call<OrderDetailForPickupResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar.visibility = View.GONE
            }
        })
    }

    fun ForDeliveryTimeOut() {
        constrain_delivry_time_out_OM.visibility = View.VISIBLE
        rel_price_accept.isClickable = false
//        rel_price_accept.isEnabled = false
        txt_accept.compoundDrawables[0]!!.setTint(resources.getColor(R.color.colorBlackWithAlpha))
        txt_accept.setTextColor(resources.getColor(R.color.colorBlackWithAlpha))

    }


    fun offerMadeApi() {

        progressBar!!.visibility = View.VISIBLE
        val getStoreOfferOrders = GetStoreOfferOrders()
        getStoreOfferOrders.order_id = orderId
        getStoreOfferOrders.store_id = storeId
        getStoreOfferOrders.user_id = AppConstant.getPreferenceText(AppConstant.PREF_USER_ID)

        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.offerorders(getStoreOfferOrders)

        call.enqueue(object : Callback<GetStoreOfferOrdersResponse> {
            override fun onResponse(
                    call: Call<GetStoreOfferOrdersResponse>,
                    response: retrofit2.Response<GetStoreOfferOrdersResponse>
            ) {
                progressBar!!.visibility = View.GONE
                if (response.isSuccessful) {

                    // del_pref_type.text = response.body()!!.data!!.deliveryTypeName!!
                    if (response.body()!!.data!!.offerDeliveryDate!!.equals("")) {

                        txt_price_date.text = response.body()?.data?.todateFromdate.toString()
                        Log.d("TAG", "price_date----" + response.body()?.data?.todateFromdate.toString())

                        val category: String = (response.body()?.data?.todateFromdate.toString())
                        val split: List<String> = category.split("to")
                        val firstSubString = split[0]
                        val secondSubString = split[1]
                        txt_price_date.text = firstSubString + "to"
                        txt_price_to_date.text = secondSubString

                    } else {

                        txt_price_date.text = response.body()?.data?.offerTodateFromdate.toString()
                        Log.d("TAG", "price_date----" + response.body()?.data?.offerTodateFromdate.toString())
                        val category: String = (response.body()?.data?.offerTodateFromdate.toString())
                        val split: List<String> = category.split("to")
                        val firstSubString = split[0]
                        val secondSubString = split[1]
                        txt_price_date.text = firstSubString + "to"
                        txt_price_to_date.text = secondSubString
                    }

                    if (response.body()!!.data!!.offerDeliveryPreference != null && !response.body()!!.data!!.offerDeliveryPreference.equals("")) {
                        del_pref_type.text = response.body()!!.data!!.offerDeliveryPreferenceName

                        if(response.body()!!.data!!.offerDeliveryPreference.equals("0"))
                        {
                            txt_1.setText(R.string.pickup_date)
                        }

                    } else {
                        del_pref_type.text = response.body()!!.data!!.deliveryTypeName
                        if(response.body()!!.data!!.deliveryType.equals("0"))
                        {
                            txt_1.setText(R.string.pickup_date)
                        }
                    }


                    txt_prcntg.text =
                            response.body()!!.data!!.discountPercentage!! + "% OFF"
                    txt_price_rs.text = "₹ " + response.body()!!.data!!.finalAmount

                    offerId = response.body()!!.data!!.offerId!!
                } else {

                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        //Toast.makeText(this@StoreOrderAcceptedActivity, "" + jObjError.getString("message"), Toast.LENGTH_SHORT).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@StoreOrderAcceptedActivity, e.message, Toast.LENGTH_LONG)
                                .show()
                    }

                }
            }

            override fun onFailure(call: Call<GetStoreOfferOrdersResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar!!.visibility = View.GONE
            }
        })
    }

    private fun offerAcceptApi() {
        progressBar!!.visibility = View.VISIBLE
        val buyerOrderAccept = BuyerOrderAccept()
        buyerOrderAccept.user_id = AppConstant.getPreferenceText(AppConstant.PREF_USER_ID)
            buyerOrderAccept.order_id = orderId
        buyerOrderAccept.store_id = storeId
        buyerOrderAccept.offer_id = offerId
        buyerOrderAccept.best_in_type = ""
        buyerOrderAccept.offer_coverage_type = ""

        val apiService = AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.buyerorderaccept(buyerOrderAccept)

        call.enqueue(object : Callback<BuyerOrderAcceptResponse> {
            override fun onResponse(
                    call: Call<BuyerOrderAcceptResponse>,
                    response: retrofit2.Response<BuyerOrderAcceptResponse>
            ) {
                progressBar!!.visibility = View.GONE

                Log.d("HelloAccept","Status   "+response.body()!!.status+"   Message   "+response.body()!!.message+"  Id  "+AppConstant.getPreferenceText(AppConstant.PREF_USER_ID)+
                "   OID   "+orderId+"   store_id   "+storeId+"   offerId   "+offerId)


                if (response.isSuccessful) {
                    isAccepted = "1"
                    setLayout2()
                    orderDetailApi()
                   // startActivity(Intent(this@StoreOrderAcceptedActivity, MainActivity::class.java))
                   // finish()
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@StoreOrderAcceptedActivity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@StoreOrderAcceptedActivity, e.message, Toast.LENGTH_LONG)
                                .show()
                    }

                }
            }

            override fun onFailure(call: Call<BuyerOrderAcceptResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar!!.visibility = View.GONE
            }
        })
    }
}
