 package bluesharklabs.com.medical.activities

import android.R.attr
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.OrderTimeline
import bluesharklabs.com.medical.model.SkipPaymentBuyer
import bluesharklabs.com.medical.model.VerifyOrder
import bluesharklabs.com.medical.response.OrderTimelineResponse
import bluesharklabs.com.medical.response.SkipPaymentBuyerResponse
import bluesharklabs.com.medical.response.VerifyOrderResponse
import bluesharklabs.com.medical.utils.AppConstant
import kotlinx.android.synthetic.main.activity_track_order.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.text.SimpleDateFormat


class TrackOrderActivity : AppCompatActivity(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    var offerId = ""
    var orderId = ""
    var storeId = ""
    var orderType = ""
    var offerReceiverVerifyDate = ""
    var orderConfirmDate = ""
    var paymentVerifyDate = ""
    var orderRecieved = ""
    var isStoreReview = ""
    var isCredit = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//  set status text dark

        setContentView(R.layout.activity_track_order)

        simpleSwipeRefreshLayout.setOnRefreshListener(this)
        btn_track_order.setOnClickListener(this)
        img_back.setOnClickListener(this)
        txt_download_invoice.setOnClickListener(this)
        txt_sskip.setOnClickListener(this)
        txt_view_order.setOnClickListener(this)



        if (intent != null) {
            if (intent.getStringExtra("orderType") != null) {
                orderType = intent.getStringExtra("orderType")
            }
            if (intent.getStringExtra("offerId") != null) {
                offerId = intent.getStringExtra("offerId")
                Log.d("TAG", "offerId--" + offerId)
            }
            if (intent.getStringExtra("orderid") != null) {
                orderId = intent.getStringExtra("orderid")

                orderTimelineApi()
            }
        }



        simpleSwipeRefreshLayout?.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark)

        simpleSwipeRefreshLayout?.post(Runnable {
            if (simpleSwipeRefreshLayout != null) {
                simpleSwipeRefreshLayout!!.isRefreshing = true
            }
            orderTimelineApi()
        })
        //constraint_id
        //constraint_add
        //constraint_skip


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.txt_sskip -> {
                skipPaymentApi()
            }
            R.id.txt_view_order -> {

                if (orderType.equals("0")) {
                    startActivity(
                            Intent(this@TrackOrderActivity, FinalOrderActivity::class.java)
                                    .putExtra("reorder", "1")
                                    .putExtra("orderid", orderId)
                                    .putExtra("offerId", offerId)
                    )
                }else{
                    startActivity(
                            Intent(this@TrackOrderActivity, CustomFinalOrderActivity::class.java)
                                    .putExtra("reorder", "1")
                                    .putExtra("orderid", orderId)
                                    .putExtra("offerId", offerId)
                    )
                }

//                startActivity(
//                        Intent(this@TrackOrderActivity, FinalOrderActivity::class.java)
//                                .putExtra("reorder", "1")
//                                .putExtra("orderid", orderId)
//                )

//                startActivity(
//                        Intent(this@TrackOrderActivity, ViewOfferActivity::class.java)
//                                .putExtra("orderid", orderId)
//                                .putExtra("storeid", storeId)
//
//                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.txt_download_invoice -> {
                startActivity(
                        Intent(this@TrackOrderActivity, InvoiceActivity::class.java)
                                .putExtra("offerId", offerId)
                )
            }
            R.id.btn_track_order -> {
                if (isStoreReview.equals("1")) {
                    startActivity(
                            Intent(this@TrackOrderActivity, MyRatingActivity::class.java)
                                    .putExtra("offerId", offerId)
                                    .putExtra("storeId", storeId)
                    )
                } else if (!orderRecieved.equals("")) {
                    startActivity(
                            Intent(this@TrackOrderActivity, RateStoreActivity::class.java)
                                    .putExtra("offerId", offerId)
                                    .putExtra("storeId", storeId)
                                    .putExtra("orderId", orderId)
                                    .putExtra("orderType", orderType)
                    )
                } else if (!paymentVerifyDate.equals("") && orderRecieved.equals("")) {
                    verifyPaymentApi()
                } else if (!offerReceiverVerifyDate.equals("") || paymentVerifyDate.equals("")) {
                    startActivityForResult(
                            Intent(this@TrackOrderActivity, MakePaymentActivity::class.java)
                                    .putExtra("offerId", offerId)
                                    .putExtra("orderId", orderId)
                                    .putExtra("storeId", storeId)
                                    .putExtra("isCredit", isCredit), 1001
                    )
                }
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001) {
            val message: String = data!!.getStringExtra("date")
            if (!message.equals("")) {
                txt_date_pymnt_verfy.setText(message)
            }else {
                orderTimelineApi()
            }

            btn_track_order.setText("Order Received")
            btn_track_order.alpha=0.5f
            btn_track_order.isClickable=false
            constraint_skip.visibility = View.GONE
        }
    }

    fun skipPaymentApi() {

        progressBar.visibility = View.VISIBLE
        val skipPaymentBuyer = SkipPaymentBuyer()
        skipPaymentBuyer.offer_id = offerId
        skipPaymentBuyer.store_id = storeId

        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.skippaymentbuyer(skipPaymentBuyer)

        call.enqueue(object : Callback<SkipPaymentBuyerResponse> {
            override fun onResponse(
                    call: Call<SkipPaymentBuyerResponse>,
                    response: retrofit2.Response<SkipPaymentBuyerResponse>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {

                    orderTimelineApi()
                    /* startActivity(Intent(this@TrackOrderActivity, RateStoreActivity::class.java))
                     finish()
                     overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)*/
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@TrackOrderActivity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@TrackOrderActivity, e.message, Toast.LENGTH_LONG)
                                .show()
                    }

                }
            }

            override fun onFailure(call: Call<SkipPaymentBuyerResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar.visibility = View.GONE
            }
        })
    }

    fun verifyPaymentApi() {

        progressBar.visibility = View.VISIBLE
        val verifyOrder = VerifyOrder()
        verifyOrder.offer_id = offerId
        verifyOrder.order_id = orderId

        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.verifyorder(verifyOrder)

        call.enqueue(object : Callback<VerifyOrderResponse> {
            override fun onResponse(
                    call: Call<VerifyOrderResponse>,
                    response: retrofit2.Response<VerifyOrderResponse>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    orderTimelineApi()
                    /* startActivity(Intent(this@TrackOrderActivity, RateStoreActivity::class.java))
                     finish()
                     overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)*/
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@TrackOrderActivity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@TrackOrderActivity, e.message, Toast.LENGTH_LONG)
                                .show()
                    }

                }
            }


            override fun onFailure(call: Call<VerifyOrderResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar.visibility = View.GONE
            }
        })
    }

    fun orderTimelineApi() {

        progressBar.visibility = View.VISIBLE
        val orderTimeline = OrderTimeline()
        orderTimeline.order_id = orderId
        orderTimeline.offer_id = offerId

        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.getordertimeline(orderTimeline)

        call.enqueue(object : Callback<OrderTimelineResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                    call: Call<OrderTimelineResponse>,
                    response: retrofit2.Response<OrderTimelineResponse>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {

                    simpleSwipeRefreshLayout!!.isRefreshing = false

                    offerReceiverVerifyDate = response.body()?.data!!.offerReceiverVerifyDate!!
                    orderConfirmDate = response.body()?.data!!.offerConfirmDate!!
                    paymentVerifyDate = response.body()?.data!!.paymentVerifyDate!!
                    orderRecieved = response.body()?.data!!.orderRecieved!!
                    isStoreReview = response.body()?.data!!.isStoreReview!!.toString()
                    AppConstant.setPreferenceText(AppConstant.PREF_STORE_PHONE, response.body()?.data?.storePhone.toString())
                    AppConstant.setPreferenceText(AppConstant.PREF_STORE_NAME,response.body()?.data?.storeName!!
                    )
                    isCredit = response.body()?.data?.isCredit.toString()



                    storeId = response.body()?.data!!.storeId!!
                    txt_id.text = "ID: " + response.body()?.data!!.orderNo
                    txt_order_id.text = "ID: " + response.body()?.data!!.orderNo

                    txt_datentime.text = response.body()?.data?.createdDate!!
                    txt_price.text = "₹ " + response.body()?.data!!.offerPrice
                    txt_percn.text = response.body()?.data!!.discountPercentage + "% OFF"


                    if (response.body()?.data?.timelineFlag.equals("0")) {
                        chkbx_ordr_confrmd.isChecked = true
                        txt_date_ordr_confrm.visibility = View.VISIBLE
                        constraint_skip.visibility = View.GONE
                        txt_date_ordr_confrm.text = response.body()?.data!!.offerConfirmDate.toString()

                        txt_order_confrmd.setTextColor(
                                ContextCompat.getColor(
                                        this@TrackOrderActivity,
                                        R.color.black_txt
                                )
                        )

                        btn_track_order.text = resources.getString(R.string.make_payment)
                        btn_track_order.isClickable = false
                        btn_track_order.alpha = 0.5f
                    } else if (response.body()?.data?.timelineFlag.equals("1")) {

                        chkbx_recevr_vrfyd.isChecked = true

                        txt_ph_no_recevr_verfy.visibility = View.VISIBLE
                        txt_date_recevr_verfy.visibility = View.VISIBLE
                        txt_date_recevr_verfy.text = response.body()?.data!!.offerReceiverVerifyDate.toString()
                        txt_ph_no_recevr_verfy.text = response.body()?.data!!.receiverOptPhone
                        constraint_skip.visibility = View.VISIBLE
                        btn_track_order.text = resources.getString(R.string.make_payment)
                        btn_track_order.isClickable = true
                        btn_track_order.alpha = 1f

                        txt_receivr_verifyd.setTextColor(
                                ContextCompat.getColor(
                                        this@TrackOrderActivity,
                                        R.color.black_txt
                                )
                        )
                        v1.background =
                                ContextCompat.getDrawable(this@TrackOrderActivity, R.drawable.dotted)

                        chkbx_ordr_confrmd.isChecked = true
                        txt_date_ordr_confrm.visibility = View.VISIBLE
                        txt_date_ordr_confrm.text = response.body()?.data!!.offerConfirmDate.toString()

                        txt_order_confrmd.setTextColor(
                                ContextCompat.getColor(
                                        this@TrackOrderActivity,
                                        R.color.black_txt
                                )
                        )

                    } else if(response.body()?.data?.timelineFlag.equals("2"))
                    {
                        chkbx_recevr_vrfyd.isChecked = true

                        txt_ph_no_recevr_verfy.visibility = View.VISIBLE
                        txt_date_recevr_verfy.visibility = View.VISIBLE
                        txt_date_recevr_verfy.text = response.body()?.data!!.offerReceiverVerifyDate.toString()
                        txt_ph_no_recevr_verfy.text = response.body()?.data!!.receiverOptPhone
                        constraint_skip.visibility = View.VISIBLE
                        constraint_skip.visibility = View.GONE
                        btn_track_order.text = resources.getString(R.string.order_received)
                        btn_track_order.isClickable = false
                        btn_track_order.alpha = 0.5f

                        txt_receivr_verifyd.setTextColor(
                                ContextCompat.getColor(
                                        this@TrackOrderActivity,
                                        R.color.black_txt
                                )
                        )
                        v1.background =
                                ContextCompat.getDrawable(this@TrackOrderActivity, R.drawable.dotted)

                        chkbx_ordr_confrmd.isChecked = true
                        txt_date_ordr_confrm.visibility = View.VISIBLE
                        txt_date_ordr_confrm.text = response.body()?.data!!.offerConfirmDate.toString()

                        txt_order_confrmd.setTextColor(
                                ContextCompat.getColor(
                                        this@TrackOrderActivity,
                                        R.color.black_txt
                                )
                        )

                        if(response.body()?.data?.paymentName.equals("Skip"))
                        {
                            txt_date_pymnt_verfy.visibility = View.VISIBLE
                            txt_pymnt_amount.visibility = View.VISIBLE
                            txt_pymnt_amount.text= response.body()?.data?.paymentName+ ":₹ " + response.body()?.data!!.offerPrice
                            txt_date_pymnt_verfy.text = response.body()?.data!!.paymentVerifyDate.toString()
                        }
                    }
                    else if (response.body()?.data?.timelineFlag.equals("3")) {
                        chkbx_paymnt_vrfyd.isChecked = true
                        txt_date_pymnt_verfy.visibility = View.VISIBLE
                        txt_download_invoice.visibility = View.VISIBLE
                        txt_pymnt_amount.visibility = View.VISIBLE
                        constraint_skip.visibility = View.GONE
                        txt_date_pymnt_verfy.text = response.body()?.data!!.paymentVerifyDate.toString()
                        btn_track_order.text = resources.getString(R.string.verify_order)
                        btn_track_order.isClickable = true
                        btn_track_order.alpha = 1f
                        if (response.body()?.data?.offerPriceChangeByStore.isNullOrBlank() || response.body()?.data?.offerPriceChangeByStore.equals("0.00")) {
                            txt_pymnt_amount.text =
                                    response.body()?.data!!.paymentName + ":₹ " + response.body()?.data!!.offerPrice
                        } else {
                            txt_pymnt_amount.text =
                                    response.body()?.data!!.paymentName + ":₹ " + response.body()?.data!!.offerPriceChangeByStore
                        }

                        txt_paymnt_verifyd.setTextColor(
                                ContextCompat.getColor(
                                        this@TrackOrderActivity,
                                        R.color.black_txt
                                )
                        )
                        v2.background =
                                ContextCompat.getDrawable(this@TrackOrderActivity, R.drawable.dotted)

                        chkbx_recevr_vrfyd.isChecked = true
                        if (response.body()?.data?.offerReceiverVerifyDate.equals("") && response.body()?.data?.receiverOptPhone.equals("Skip")) {

                            txt_date_recevr_verfy.visibility = View.VISIBLE
                            txt_ph_no_recevr_verfy.visibility = View.VISIBLE
                            txt_date_recevr_verfy.text = (response.body()?.data!!.receiverOptPhone)
                            txt_ph_no_recevr_verfy.text = response.body()?.data?.storePhone
                            txt_receivr_verifyd.setTextColor(
                                    ContextCompat.getColor(
                                            this@TrackOrderActivity,
                                            R.color.black_txt
                                    )
                            )
                        } else {

                            txt_date_recevr_verfy.visibility = View.VISIBLE
                            txt_ph_no_recevr_verfy.visibility = View.VISIBLE
                            txt_date_recevr_verfy.text = response.body()?.data!!.offerReceiverVerifyDate.toString()
                            txt_ph_no_recevr_verfy.text = response.body()?.data!!.receiverOptPhone
                            txt_receivr_verifyd.setTextColor(
                                    ContextCompat.getColor(
                                            this@TrackOrderActivity,
                                            R.color.black_txt
                                    )
                            )
                        }


                        v1.background =
                                ContextCompat.getDrawable(this@TrackOrderActivity, R.drawable.dotted)

                        chkbx_ordr_confrmd.isChecked = true
                        txt_date_ordr_confrm.visibility = View.VISIBLE
                        txt_date_ordr_confrm.text = response.body()?.data!!.offerConfirmDate.toString()

                        txt_order_confrmd.setTextColor(
                                ContextCompat.getColor(
                                        this@TrackOrderActivity,
                                        R.color.black_txt
                                )
                        )

                    } else if (response.body()?.data?.timelineFlag.equals("4")) {
                        chkbx_receivd.isChecked = true
                        txt_ordr_reciv_dt.visibility = View.VISIBLE
                        constraint_skip.visibility = View.GONE
                        constraint_add.visibility = View.VISIBLE
                        constraint_add2.visibility = View.VISIBLE
                        txt_ordr_reciv_dt.text = response.body()?.data!!.orderRecieved.toString()
//                        textView104.text = response.body()?.data?.offerDeliveryTypeName  // need to check
//                        textView104.text = response.body()?.data?.offerDeliveryTypeName  + " at " + response.body()?.data?.storeArea
                        textView104.text = response.body()?.data?.offerDeliveryTypeName + " in " + response.body()?.data?.deliveryCity

                        textView107.text = resources.getString(R.string.pickup_date)

                        if (response.body()?.data?.offerDeliveryType.equals("1")) {
                            textView104.text = response.body()?.data?.offerDeliveryTypeName + " in " + response.body()?.data?.deliveryCity
                            textView107.text = resources.getString(R.string.delivery_date)
                            textView105.visibility = View.VISIBLE
                            textView105.text = response.body()?.data?.location
                        } else if (response.body()?.data?.offerDeliveryType.equals("2")) {
                            textView107.text = resources.getString(R.string.delivery_date)
                            textView104.text = response.body()?.data?.offerDeliveryTypeName + " in " + response.body()?.data?.deliveryCity
                            textView105.visibility = View.VISIBLE
                            textView105.text = response.body()?.data?.blockNo + ", " + response.body()?.data?.buildingName + ", " + response.body()?.data?.street +
                                    ", " + response.body()?.data?.area +
                                    ", " + response.body()?.data?.landmark +
                                    ", " + response.body()?.data?.zipcode
                        } else {
                            textView105.visibility = View.GONE
                        }

                        textView108.text = response.body()?.data?.todate_fromdate

                        if (response.body()?.data?.isStoreReview == 1) {
                            btn_track_order.text = resources.getString(R.string.view_stores_rating_for_you)
                        } else {
                            btn_track_order.text = resources.getString(R.string.rate_store)
                        }
                        txt_ordr_receivd.setTextColor(
                                ContextCompat.getColor(
                                        this@TrackOrderActivity,
                                        R.color.black_txt
                                )
                        )
                        v3.background =
                                ContextCompat.getDrawable(this@TrackOrderActivity, R.drawable.dotted)

                        chkbx_paymnt_vrfyd.isChecked = true
                        txt_date_pymnt_verfy.visibility = View.VISIBLE
                        //txt_ph_no_recevr_verfy.visibility = View.VISIBLE
                        txt_pymnt_amount.visibility = View.VISIBLE
                        txt_download_invoice.visibility = View.VISIBLE
                        txt_date_pymnt_verfy.text = response.body()?.data!!.paymentVerifyDate.toString()
                        //txt_ph_no_recevr_verfy.text =
                        //"Phone Num: " + response.body()?.data!!.receiverOptPhone

                        if (response.body()?.data?.offerPriceChangeByStore.isNullOrBlank() || response.body()?.data?.offerPriceChangeByStore.equals("0.00")) {
                            txt_pymnt_amount.text =
                                    response.body()?.data!!.paymentName + ":₹ " + response.body()?.data!!.offerPrice
                        } else {
                            txt_pymnt_amount.text =
                                    response.body()?.data!!.paymentName + ":₹ " + response.body()?.data!!.offerPriceChangeByStore
                        }

                        txt_paymnt_verifyd.setTextColor(
                                ContextCompat.getColor(
                                        this@TrackOrderActivity,
                                        R.color.black_txt
                                )
                        )
                        v2.background =
                                ContextCompat.getDrawable(this@TrackOrderActivity, R.drawable.dotted)

                        chkbx_recevr_vrfyd.isChecked = true
                        if (response.body()?.data?.offerReceiverVerifyDate.equals("") && response.body()?.data?.receiverOptPhone.equals("Skip")) {

                            txt_date_recevr_verfy.visibility = View.VISIBLE
                            txt_ph_no_recevr_verfy.visibility = View.VISIBLE
                            txt_date_recevr_verfy.text = (response.body()?.data!!.receiverOptPhone)
                            txt_ph_no_recevr_verfy.text = response.body()?.data?.storePhone
                            txt_receivr_verifyd.setTextColor(
                                    ContextCompat.getColor(
                                            this@TrackOrderActivity,
                                            R.color.black_txt
                                    )
                            )


                        } else {

                            txt_date_recevr_verfy.visibility = View.VISIBLE
                            txt_ph_no_recevr_verfy.visibility = View.VISIBLE
                            txt_date_recevr_verfy.text = response.body()?.data!!.offerReceiverVerifyDate.toString()
                            txt_ph_no_recevr_verfy.text = response.body()?.data!!.receiverOptPhone
                            txt_receivr_verifyd.setTextColor(
                                    ContextCompat.getColor(
                                            this@TrackOrderActivity,
                                            R.color.black_txt
                                    )
                            )
                        }

                        v1.background =
                                ContextCompat.getDrawable(this@TrackOrderActivity, R.drawable.dotted)

                        chkbx_ordr_confrmd.isChecked = true
                        txt_date_ordr_confrm.visibility = View.VISIBLE
                        txt_date_ordr_confrm.text = response.body()?.data!!.offerConfirmDate.toString()

                        txt_order_confrmd.setTextColor(
                                ContextCompat.getColor(
                                        this@TrackOrderActivity,
                                        R.color.black_txt
                                )
                        )


                    }


                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@TrackOrderActivity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@TrackOrderActivity, e.message, Toast.LENGTH_LONG)
                                .show()
                    }


                }
            }



            override fun onFailure(call: Call<OrderTimelineResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar.visibility = View.GONE
                simpleSwipeRefreshLayout!!.isRefreshing = false
            }
        })
    }

    override fun onRefresh() {
        orderTimelineApi()
    }
}