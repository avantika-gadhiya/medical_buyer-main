package bluesharklabs.com.medical.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.activities.ViewOfferActivity.Companion.customOrderdata
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.OrderDetail
import bluesharklabs.com.medical.response.OrderDetailResponse
import bluesharklabs.com.medical.utils.AppConstant
import kotlinx.android.synthetic.main.activity_offer_accepted.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.util.*

class OfferAcceptedActivity : AppCompatActivity(), View.OnClickListener {


    var orderId = ""
    var offerId = ""
    var orderType = ""
    var latitude = ""
    var longitude = ""
    private var isFullOrder = "0"
    private var isPartialOrder = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR//  set status text dark

        setContentView(R.layout.activity_offer_accepted)


        img_back.setOnClickListener(this)
        rel_full_cvrage.setOnClickListener(this)
        rel_partial_cvrage.setOnClickListener(this)
        txt_view_order.setOnClickListener(this)
        txt_geo_location.setOnClickListener(this)

        if (intent != null) {
            if (intent.getStringExtra("orderid") != null) {
                orderId = intent.getStringExtra("orderid")

                orderDetailApi()
            }
        }


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txt_geo_location -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr=" + latitude + "," + longitude)
                )
                startActivity(intent)
            }
            R.id.img_back -> {
                finish()
            }
            R.id.rel_full_cvrage -> {

                startActivity(
                    Intent(this@OfferAcceptedActivity, FullCoverageActivity::class.java)
                        .putExtra("isAvailable", isFullOrder)
                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)


            }
            R.id.rel_partial_cvrage -> {
                startActivity(
                    Intent(
                        this@OfferAcceptedActivity,
                        PartialCoverageActivity::class.java
                    ).putExtra("isAvailable", isPartialOrder)
                )
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)


            }
            R.id.txt_view_order -> {

                if (orderType.equals("0")) {
                    startActivity(
                        Intent(this@OfferAcceptedActivity, FinalOrderActivity::class.java)
                            .putExtra("vieworder", "1")
                            .putExtra("orderid", orderId)
                    )
                } else {
                    startActivity(
                        Intent(this@OfferAcceptedActivity, CustomFinalOrderActivity::class.java)
                            .putExtra("vieworder", "1")
                            .putExtra("offeracc", "1")
                            .putExtra("orderid", orderId)
                    )
                }
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }


    private fun orderDetailApi() {
        progressBar!!.visibility = View.VISIBLE
        val orderdetail = OrderDetail()
        orderdetail.user_id = AppConstant.getPreferenceText(AppConstant.PREF_USER_ID)
        orderdetail.order_id = orderId
        orderdetail.offer_id = offerId

        val apiService =
            AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.orderdetail(orderdetail)

        call.enqueue(object : Callback<OrderDetailResponse> {
            override fun onResponse(
                call: Call<OrderDetailResponse>,
                response: retrofit2.Response<OrderDetailResponse>
            ) {
                progressBar!!.visibility = View.GONE
                if (response.isSuccessful) {

                    customOrderdata = response.body()?.data!!
                    txt_order_id.text = "ID: " + customOrderdata!!.orderNo

                    txt_datentime.text = customOrderdata?.createdDate!!
                    txt_status.text = customOrderdata!!.orderStatus!!
                    txt_status1.text = customOrderdata!!.requestStatus!!

                    // Added by Rahul Bambhaniya
                    if (customOrderdata!!.orderStatus!!.equals("Accepted")) {
                        finish()
                    }
                    // Added by Rahul Bambhaniya
                    if (customOrderdata!!.orderStatus!!.equals("Invoiced")) {
                        finish()
                    }

                    isFullOrder = customOrderdata!!.isFullOrder!!
                    isPartialOrder = customOrderdata!!.isPartialOrder!!

                    //   offerId  =customOrderdata!!.offerId!!

                    textView56.text = getString(
                        R.string.storecounts,
                        customOrderdata!!.storeCount,
                        customOrderdata!!.offerCount
                    )
                    textView57.text = getString(R.string.offercounts, customOrderdata!!.offerCount)
                    orderType = customOrderdata!!.orderType!!
                    DrawableCompat.setTint(
                        txt_status1.background,
                        ContextCompat.getColor(
                            this@OfferAcceptedActivity,
                            AppConstant.setsStatus(customOrderdata!!.requestStatus!!)
                        )
                    )
                    DrawableCompat.setTint(
                        txt_status.background,
                        ContextCompat.getColor(
                            this@OfferAcceptedActivity,
                            AppConstant.setsStatus(customOrderdata!!.orderStatus!!)
                        )
                    )

                    if (customOrderdata!!.orderPreferenceName != null && !customOrderdata!!.orderPreferenceName!!.equals(
                            ""
                        )
                    ) {
                        txt_buying_pref.text = customOrderdata!!.orderPreferenceName!!
                    } else {
                        txt_buying_pref.text = customOrderdata!!.buyingPreferenceName!!
                    }
                    txt_del_pref.text = customOrderdata!!.deliveryTypeName!!

                    if (customOrderdata!!.deliveryType!!.equals("0")) {
                        textView51.text = resources.getString(R.string.pickup_date)
                        textView52.text = resources.getString(R.string.pickup_time)
                        txt_del_pref_add.visibility = View.GONE

                    } else if (customOrderdata!!.deliveryType!!.equals("1")) {
                        txt_del_pref.text =
                            customOrderdata!!.deliveryTypeName!! + " in " + customOrderdata!!.deliveryCity
                        txt_geo_location.visibility = View.VISIBLE
                        val locaTion =
                            customOrderdata!!.blockNo + ", " + customOrderdata!!.buildingName
                        txt_geo_location.text = customOrderdata!!.location
                        latitude = customOrderdata?.deliveryLatitude.toString()
                        longitude = customOrderdata?.deliveryLatitude.toString()
                        txt_del_pref_add.visibility = View.VISIBLE
                        txt_del_pref_add.text = locaTion
                    } else {
                        txt_del_pref.text =
                            customOrderdata!!.deliveryTypeName!! + " in " + customOrderdata!!.deliveryCity
                        val locaTion =
                            customOrderdata!!.blockNo + ", " + customOrderdata!!.buildingName + ", " + customOrderdata!!.street + ", " +
                                    customOrderdata!!.area + ", " + customOrderdata!!.landmark + ", " + customOrderdata!!.zipcode

                        txt_del_pref_add.visibility = View.VISIBLE
                        txt_del_pref_add.text = locaTion
                    }
                    if (!customOrderdata!!.todateFromdate!!.equals("")) {
                        txt_del_date.text = (customOrderdata!!.todateFromdate!!)
                        // txt_del_time.text = (customOrderdata!!.startTime+" to "+ customOrderdata!!.endTime)
                    }

                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                            this@OfferAcceptedActivity,
                            "" + jObjError.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@OfferAcceptedActivity, e.message, Toast.LENGTH_LONG)
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


    override fun onResume() {
        super.onResume()
        if (intent != null) {
            if (intent.getStringExtra("orderid") != null) {
                orderId = intent.getStringExtra("orderid")

                orderDetailApi()
            }
        }
    }
}
