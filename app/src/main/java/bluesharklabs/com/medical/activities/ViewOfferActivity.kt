package bluesharklabs.com.medical.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.adapters.CustomerOrderOfferAdapter
import bluesharklabs.com.medical.adapters.OrderOfferAdapter
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.OrderDetailOffer
import bluesharklabs.com.medical.response.OrderDetailResponse
import bluesharklabs.com.medical.utils.AppConstant
import kotlinx.android.synthetic.main.activity_offer_accepted.*
import kotlinx.android.synthetic.main.activity_view_offer.*
import kotlinx.android.synthetic.main.activity_view_offer.img_back
import kotlinx.android.synthetic.main.activity_view_offer.progressBar
import kotlinx.android.synthetic.main.activity_view_offer.txt_buying_pref
import kotlinx.android.synthetic.main.activity_view_offer.txt_datentime
import kotlinx.android.synthetic.main.activity_view_offer.txt_del_pref
import kotlinx.android.synthetic.main.activity_view_offer.txt_geo_location
import kotlinx.android.synthetic.main.activity_view_offer.txt_order_id
import kotlinx.android.synthetic.main.activity_view_offer.txt_status
import kotlinx.android.synthetic.main.activity_view_offer.txt_status1
import kotlinx.android.synthetic.main.activity_view_offer.txt_view_order
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.net.HttpURLConnection
import java.net.URL

class ViewOfferActivity : AppCompatActivity(), View.OnClickListener {


    private var recycler_order_offer: RecyclerView? = null

    private var orderOfferAdapter: OrderOfferAdapter? = null
    private var customOrderOfferAdapter: CustomerOrderOfferAdapter? = null

    var isViewall = "0"
    var isCustom = ""
    var orderId = ""
    var latitude = ""
    var longitude = ""
    var store_latitude = ""
    var store_longitude = ""

    var isBuyerReview = ""


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//  set status text dark

        setContentView(R.layout.activity_view_offer)


        recycler_order_offer = findViewById(R.id.recycl_order_offer)
        img_back.setOnClickListener(this)
        txt_view_order.setOnClickListener(this)
        txt_view_all_produts.setOnClickListener(this)
        btn.setOnClickListener(this)
        rel_delivry_reorder.setOnClickListener(this)
        rel_delivry_accept.setOnClickListener(this)
        txt_geo_location.setOnClickListener(this)

        recycler_order_offer!!.setHasFixedSize(true)
        recycler_order_offer!!.layoutManager =
                LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        if (intent != null) {

            if (intent.getStringExtra("isAccepted") != null &&
                    intent.getStringExtra("isAccepted").equals("0")) {
                constraint_bottom.visibility = View.GONE
            } else {
                constraint_bottom.visibility = View.GONE
            }
            if (intent.getStringExtra("orderid") != null) {

                orderId = intent.getStringExtra("orderid")
                if (intent.getStringExtra("storeid") != null) {
                    orderDetailApi(intent.getStringExtra("orderid"), intent.getStringExtra("storeid"))
                }
            } else {
                setData()
            }

            if (intent.getStringExtra("custom") != null) {


                isCustom = "Custom"
            }
        }


        //constraint_bottom

    }

    override fun onResume() {
        super.onResume()
        if (intent.getStringExtra("storeid") != null) {
            orderDetailApi(intent.getStringExtra("orderid"), intent.getStringExtra("storeid"))
        }

    }

    private fun setData() {
        if (isCustom.equals("Custom")) {
            customOrderOfferAdapter = CustomerOrderOfferAdapter(
                    this,
                    customOrderdata!!.products,
                    isViewall
            )
            recycler_order_offer!!.adapter = customOrderOfferAdapter
        } else {
            orderOfferAdapter = OrderOfferAdapter(this, customOrderdata!!.products, isViewall)
            recycler_order_offer!!.adapter = orderOfferAdapter
        }

        txt_order_id.text = "ID: " + customOrderdata!!.orderNo
        txt_status.text = customOrderdata!!.orderStatus
        txt_status1.text = customOrderdata!!.requestStatus

        DrawableCompat.setTint(
                txt_status.getBackground(),
                ContextCompat.getColor(
                        this,
                        AppConstant.setsStatus(customOrderdata!!.orderStatus!!)
                )
        )
        DrawableCompat.setTint(
                txt_status1.getBackground(),
                ContextCompat.getColor(
                        this,
                        AppConstant.setsStatus(customOrderdata!!.requestStatus!!)
                )
        )

        /* if (customOrderdata!!.requestStatus.equals(">24 hrs", ignoreCase = true)) {

             isMoreHour = "1"
         }



         timeElapsed(customOrderdata!!.createdDate)*/

        txt_datentime.text = (customOrderdata?.createdDate!!)
        txt_time_elpsed.text = (customOrderdata?.createdDate!!)
        if (customOrderdata!!.orderPreferenceName != null && !customOrderdata?.orderPreferenceName!!.equals("")) {
            txt_buying_pref.text = customOrderdata?.orderPreferenceName!!
        } else {
            txt_buying_pref.text = customOrderdata?.buyingPreferenceName!!
        }

        /* if (!customOrderdata!!.location.equals("")) {
             txt_del_add.text = customOrderdata!!.location
         } else {
             txt_del_add.visibility = View.GONE
         }
 */
        if (customOrderdata!!.offerDeliveryPreference != null && !customOrderdata!!.offerDeliveryPreference.equals("")) {
            txt_del_pref.text = customOrderdata?.offerDeliveryPreferenceName!!

            if (customOrderdata!!.offerDeliveryPreference.equals("0")) {
                textView78.text = resources.getString(R.string.pickupdatetime)
                textView76.text = resources.getString(R.string.pickup_date)
                textView77.text = resources.getString(R.string.pickup_time)
                txt_del_add.text = customOrderdata!!.offerDeliveryPreferenceName
                if (customOrderdata!!.offerIsPrescription != null && !customOrderdata!!.offerIsPrescription.equals("")) {
                    if (customOrderdata!!.offerIsPrescription!!.equals("0")) {
                        txt_del_add.text = "Original Prescription Not Required"
                    } else if (customOrderdata!!.offerIsPrescription!!.equals("1")) {
                        txt_del_add.text = "Original Prescription Required"
                    }
                } else {
                    txt_del_add.visibility = View.GONE
                }
            } else if (customOrderdata!!.offerDeliveryPreference.equals("1")) {
                txt_del_pref.text  =customOrderdata!!.offerDeliveryPreferenceName!! + " in "+ customOrderdata!!.deliveryCity
                txt_geo_location.visibility = View.VISIBLE
                val locaTion =
                        customOrderdata!!.blockNo + ", " + customOrderdata!!.buildingName
                txt_geo_location.text = customOrderdata!!.location
                latitude = customOrderdata?.orderLatitude.toString()
                longitude = customOrderdata?.orderLongitude.toString()
                store_latitude = customOrderdata?.store_latitude.toString()
                store_longitude = customOrderdata?.store_longitude.toString()

                txt_del_add.text = locaTion
            } else {
                txt_del_pref.text  =customOrderdata!!.offerDeliveryPreferenceName!! + " in "+ customOrderdata!!.deliveryCity
                val locaTion =
                        customOrderdata!!.blockNo + ", " + customOrderdata!!.buildingName + ", " + customOrderdata!!.street + ", " +
                                customOrderdata!!.area + ", " + customOrderdata!!.landmark + ", " + customOrderdata!!.zipcode
                txt_del_add.text = locaTion
            }

        } else {
            txt_del_pref.text = customOrderdata?.deliveryTypeName!!


            if (customOrderdata!!.deliveryType.equals("0")) {
                textView78.text = resources.getString(R.string.pickupdatetime)
                textView76.text = resources.getString(R.string.pickup_date)
                textView77.text = resources.getString(R.string.pickup_time)
                txt_del_add.visibility = View.GONE

            } else if (customOrderdata!!.deliveryType.equals("1")) {
                txt_del_pref.text  =customOrderdata!!.deliveryTypeName!! + " in "+ customOrderdata!!.deliveryCity
                txt_geo_location.visibility = View.VISIBLE
                val locaTion =
                        customOrderdata!!.blockNo + ", " + customOrderdata!!.buildingName
                txt_geo_location.text = customOrderdata!!.location
                latitude = customOrderdata?.deliveryLatitude.toString()
                longitude = customOrderdata?.deliveryLongitude.toString()
                store_latitude = customOrderdata?.store_latitude.toString()
                store_longitude = customOrderdata?.store_longitude.toString()
                txt_del_add.text = locaTion
            } else {
                txt_del_pref.text  =customOrderdata!!.deliveryTypeName!! + " in "+ customOrderdata!!.deliveryCity
                val locaTion =
                        customOrderdata!!.blockNo + ", " + customOrderdata!!.buildingName + ", " + customOrderdata!!.street + ", " +
                                customOrderdata!!.area + ", " + customOrderdata!!.landmark + ", " + customOrderdata!!.zipcode
                txt_del_add.text = locaTion
            }
        }
        if (customOrderdata!!.offerDeliveryDate != null && customOrderdata!!.offerDeliveryDate!!.equals("")) {
            txt_delivery_date.text = (customOrderdata?.todateFromdate!!)

        } else {
            txt_delivery_date.text = (customOrderdata!!.offerTodateFromdate!!)

        }


        Log.e("LTALONG","LAt   "+latitude+"   Long   "+longitude+"   StoreLat   "+store_latitude+"   Store_long   "+store_longitude)


        txt_bill_amount.text = "₹ " + customOrderdata!!.totalMrp

        txt_discount_perc.text =
                "Discount (" + customOrderdata!!.billDiscount + "%)"
        txt_discount_total.text = "₹ " + customOrderdata!!.totalDiscount
        txt_netbill_amount.text = "₹ " + customOrderdata!!.netBillAmount
        txt_del_charge.text = "₹ " + customOrderdata!!.delCharge
        txt_total.text = "₹ " + customOrderdata!!.totalAmount


    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.txt_view_order -> {
                if (isCustom.equals("Custom")) {
                    startActivity(
                            Intent(this@ViewOfferActivity, CustomFinalOrderActivity::class.java)
                                    .putExtra("vieworder", "1")
                                    .putExtra("orderid", orderId)
                                    .putExtra("offerId", customOrderdata!!.offerId)
                    )
                } else {
                    startActivity(
                            Intent(this@ViewOfferActivity, FinalOrderActivity::class.java)
                                    .putExtra("vieworder", "1")
                                    .putExtra("orderid", orderId)
                                    .putExtra("offerId", customOrderdata!!.offerId)
                    )
                }
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.rel_delivry_accept -> {

            }
            R.id.txt_geo_location -> {

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + store_latitude + "," + store_longitude + "&daddr=" + latitude + "," + longitude))

//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + latitude + "," + longitude))
                startActivity(intent)
            }
            R.id.btn -> {
                startActivity(Intent(this@ViewOfferActivity, MyRatingActivity::class.java)
                        .putExtra("offerId", customOrderdata!!.offerId)
                        .putExtra("storeId", customOrderdata!!.storeId.toString()))
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.txt_view_all_produts -> {
                Log.e("HellodsdLog", "OnclickALlz " + isCustom)
                startActivity(Intent(this@ViewOfferActivity, CustomAllProductsActivity::class.java)
                        .putExtra("custom", isCustom)
                        .putExtra("edit", "0"))


                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

    fun orderDetailApi(orderId: String, storeId: String) {

        progressBar!!.visibility = View.VISIBLE
        val orderDetailOffer = OrderDetailOffer()
        orderDetailOffer.order_id = orderId
        orderDetailOffer.store_id = storeId

        val apiService = AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.orderdetailoffer(orderDetailOffer)

        call.enqueue(object : Callback<OrderDetailResponse> {
            override fun onResponse(
                    call: Call<OrderDetailResponse>,
                    response: retrofit2.Response<OrderDetailResponse>
            ) {
                progressBar!!.visibility = View.GONE
                if (response.isSuccessful) {


                    customOrderdata = response.body()!!.data
                    isBuyerReview = customOrderdata?.isBuyerReview.toString()
//
//                    if (isBuyerReview.equals("1")){
//                        constraint_bottom.visibility = View.VISIBLE
//                        btn.visibility = View.VISIBLE
//                        con_reorder.visibility = View.GONE
//                        btn.text = resources.getString(R.string.view_stores_rating_for_you)
//                    }

                    /*if (customOrderdata!!.products!!.size > 3) {
                        txt_view_all_produts!!.visibility = View.VISIBLE
                    }*/
                    if (customOrderdata!!.orderType.equals("0")) {
                        orderOfferAdapter = OrderOfferAdapter(
                                this@ViewOfferActivity,
                                customOrderdata!!.products, isViewall
                        )
                        recycler_order_offer!!.adapter = orderOfferAdapter

                        orderOfferAdapter!!.notifyDataSetChanged()

                    } else {

                        isCustom = "Custom"



                        customOrderOfferAdapter = CustomerOrderOfferAdapter(
                                this@ViewOfferActivity,
                                customOrderdata!!.products, isViewall
                        )
                        recycler_order_offer!!.adapter = customOrderOfferAdapter

                    }

                    if (!customOrderdata?.totalAmount!!.isEmpty() || !customOrderdata?.delCharge!!.isEmpty()) {
                        customOrderdata?.netBillAmount =
                                (customOrderdata?.totalAmount!!.toFloat() - customOrderdata?.delCharge!!.toFloat()).toString()
                        customOrderdata?.totalMrp =
                                (customOrderdata?.netBillAmount!!.toFloat() + customOrderdata?.totalDiscount!!.toFloat()).toString()

                    }


                    /*customOrderdata!!.customerPhone = response.body()!!.data!!.customerPhone!!
                    customOrderdata!!.offerId = response.body()!!.data!!.offerId!!
                    customOrderdata!!.paymentMethod = response.body()!!.data!!.paymentMethod!!*/

                    setData()
                } else {


                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@ViewOfferActivity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@ViewOfferActivity, e.message, Toast.LENGTH_LONG)
                                .show()
                    }

                }
            }


            override fun onFailure(call: Call<OrderDetailResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar!!.visibility = View.GONE


            }
        })
    }

    companion object {
        var customOrderdata: OrderDetailResponse.Data? = null

    }


}
