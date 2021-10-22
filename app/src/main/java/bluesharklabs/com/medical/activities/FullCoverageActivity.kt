package bluesharklabs.com.medical.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.activities.ViewOfferActivity.Companion.customOrderdata
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.BuyerOrderAccept
import bluesharklabs.com.medical.model.BuyerOrderAcceptDetail
import bluesharklabs.com.medical.model.GetFullCoverage
import bluesharklabs.com.medical.response.BuyerOrderAcceptDetailResponse
import bluesharklabs.com.medical.response.BuyerOrderAcceptResponse
import bluesharklabs.com.medical.response.GetFullCoverageResponse
import bluesharklabs.com.medical.utils.AppConstant
import kotlinx.android.synthetic.main.activity_full_coverage.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback


class FullCoverageActivity : AppCompatActivity(), View.OnClickListener {


    var storeIdbip = ""
    var storeIdbid = ""
    var storeId = ""
    var offerIdbip = ""
    var offerIdbid = ""
    var orderTypebip = ""
    var orderTypebid = ""
    var offerId = ""
    var orderId = ""
    var orderType = ""
    var isBip = "0"
    var isAccepted = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR//  set status text dark

        setContentView(R.layout.activity_full_coverage)

        img_back.setOnClickListener(this)
        rel_bip_price_accept.setOnClickListener(this)
        rel_delivry_accept.setOnClickListener(this)
        // txt_befor_confrm_order.setOnClickListener(this)
        rel_bip_reorder.setOnClickListener(this)
        rel_delivry_reorder.setOnClickListener(this)
        txt_view_order.setOnClickListener(this)
        txt_view_p_offer.setOnClickListener(this)
        txt_view_d_offer.setOnClickListener(this)
        txt_view_invoice.setOnClickListener(this)
        txt_track_ordr_n_make_payment.setOnClickListener(this)
        img_map.setOnClickListener(this)

        if (intent != null) {
            if (intent.getStringExtra("isAvailable").equals("1")) {
                if (intent.getStringExtra("partial") != null) {
                    orderId = intent.getStringExtra("partial")
                    txt_befor_confrm_order.visibility = View.VISIBLE
                    rel_bip_price_accept.visibility = View.GONE
                    rel_bip_reorder.visibility = View.GONE
                    view.visibility = View.GONE
                    isAccepted = "1"

                    txt_best_in_delivery.visibility = View.GONE
                    card2.visibility = View.GONE
                    con.visibility = View.VISIBLE
                    offerListApi()
                } else if (intent.getStringExtra("orderid") != null) {
                    orderId = intent.getStringExtra("orderid")

                    if (intent.getStringExtra("status").equals("Invoiced", ignoreCase = true)) {
                        set_confrm_order_layout()
                    } else if (intent.getStringExtra("status").equals("Accepted", ignoreCase = true)) {
                        setLayoutForAwaiting()
                    }
                } else {
                    orderId = customOrderdata!!.orderId!!
                    orderFullCoverageDetailApi()
                }
            } else {
                showDialog()
            }

//            if (intent.getStringExtra("partial") != null) {
//                orderId = intent.getStringExtra("partial")
//                txt_befor_confrm_order.visibility = View.VISIBLE
//                rel_bip_price_accept.visibility = View.GONE
//                rel_bip_reorder.visibility = View.GONE
//                view.visibility = View.GONE
//                isAccepted = "1"
//
//                txt_best_in_delivery.visibility = View.GONE
//                card2.visibility = View.GONE
//                con.visibility = View.VISIBLE
//                offerListApi()
//            } else if (intent.getStringExtra("orderid") != null) {
//                orderId = intent.getStringExtra("orderid")
//
//                if (intent.getStringExtra("status").equals("Invoiced", ignoreCase = true)) {
//                    set_confrm_order_layout()
//                } else if (intent.getStringExtra("status").equals("Accepted", ignoreCase = true)) {
//                    setLayoutForAwaiting()
//                }
//            } else {
//                orderId = customOrderdata!!.orderId!!
//                orderFullCoverageDetailApi()
//            }
        }


    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.cutsom_dialog_layout)
        dialog.window.setBackgroundDrawableResource(android.R.color.white)

        //  dialog .setCancelable(false)
        val close_icon = dialog.findViewById(R.id.img_close) as AppCompatImageView
        val textView116 = dialog.findViewById(R.id.textView116) as AppCompatTextView
        textView116.text = "There is no Full Coverage Offer till now."
        val textView115 = dialog.findViewById(R.id.textView115) as AppCompatTextView
        textView115.text = "Full Coverage Offer"
        close_icon.setOnClickListener {
            finish()
            dialog.dismiss()
        }

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.BOTTOM
        //   lp.windowAnimations = R.style.DialogAnimation;
        dialog.window!!.attributes = lp
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.txt_track_ordr_n_make_payment -> {
                startActivity(
                        Intent(this@FullCoverageActivity, TrackOrderActivity::class.java)
                                .putExtra("orderid", orderId)
                                .putExtra("offerId", offerId)
                                .putExtra("orderType", orderType)
                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.txt_view_invoice -> {
                startActivity(
                        Intent(this@FullCoverageActivity, InvoiceActivity::class.java)
                                .putExtra("offerId", offerId)
                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.txt_view_d_offer -> {

                startActivity(
                        Intent(this@FullCoverageActivity, ViewOfferActivity::class.java)
                                .putExtra("orderid", orderId)
                                .putExtra("storeid", storeIdbid)
                                .putExtra("isAccepted", isAccepted)
                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.txt_view_p_offer -> {
                startActivity(
                        Intent(this@FullCoverageActivity, ViewOfferActivity::class.java)
                                .putExtra("orderid", orderId)
                                .putExtra("storeid", storeIdbip)
                                .putExtra("isAccepted", isAccepted)
                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.txt_view_order -> {
                if (orderType.equals("0")) {
                    startActivity(
                            Intent(this@FullCoverageActivity, FinalOrderActivity::class.java)
                                    .putExtra("vieworder", "1")
                                    .putExtra("orderid", orderId)
                                    .putExtra("offerId", offerId)
                    )
                } else {
                    startActivity(
                            Intent(this@FullCoverageActivity, CustomFinalOrderActivity::class.java)
                                    .putExtra("vieworder", "1")
                                    .putExtra("orderid", orderId)
                                    .putExtra("offerId", offerId)
                    )
                }
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.rel_delivry_reorder -> {
                orderType = orderTypebid
                if (orderType.equals("0")) {
                    startActivity(
                            Intent(this@FullCoverageActivity, FinalOrderActivity::class.java)
                                    .putExtra("reorder", "1")
                                    .putExtra("offerId", offerId)
                                    .putExtra("orderid", orderId)
                    )
                } else {
                    startActivity(
                            Intent(this@FullCoverageActivity, CustomFinalOrderActivity::class.java)
                                    .putExtra("reorder", "1")
                                    .putExtra("offerId", offerId)
                                    .putExtra("orderid", orderId)

                            //finish()
                    )
                }
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.rel_bip_reorder -> {
                orderType = orderTypebip
                if (orderType.equals("0")) {
                    startActivity(
                            Intent(this@FullCoverageActivity, FinalOrderActivity::class.java)
                                    .putExtra("reorder", "1")
                                    .putExtra("offerId", offerId)
                                    .putExtra("orderid", orderId)
                    )
                } else {
                    startActivity(
                            Intent(this@FullCoverageActivity, CustomFinalOrderActivity::class.java)
                                    .putExtra("reorder", "1")
                                    .putExtra("offerId", offerId)
                                    .putExtra("orderid", orderId)

                            //finish()
                    )
                }
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.txt_befor_confrm_order -> {
                set_confrm_order_layout()
            }
            R.id.rel_bip_price_accept -> {
                isBip = "0"
                storeId = storeIdbip
                offerId = offerIdbip
                orderType = orderTypebip
                offerAcceptApi(isBip)
            }
            R.id.rel_delivry_accept -> {
                isBip = "1"
                storeId = storeIdbid
                offerId = offerIdbid
                orderType = orderTypebid
                offerAcceptApi(isBip)
            }
            R.id.rel_price_accept -> {
                setLayoutForAwaiting()
            }
            R.id.img_map -> {
                viewOnMap()
            }
        }
    }

    private fun setLayoutForAwaiting() {

        txt_befor_confrm_order.visibility = View.VISIBLE
        rel_bip_price_accept.visibility = View.GONE
        rel_bip_reorder.visibility = View.GONE
        view.visibility = View.GONE
        isAccepted = "1"

        txt_best_in_delivery.visibility = View.GONE
        card2.visibility = View.GONE
        // con.visibility = View.VISIBLE
        offerListApi()
    }

    fun viewOnMap() {
        val gmmIntentUri = Uri.parse("geo:37.7749,-122.4194")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        }
    }

    private fun set_confrm_order_layout() {
        txt_ur_order_confirmed.visibility = View.VISIBLE
        card3.visibility = View.VISIBLE
        card4.visibility = View.VISIBLE

        txt_best_in_delivery.visibility = View.GONE
        card2.visibility = View.GONE


        //view order
        txt_view_order.visibility = View.VISIBLE

        view.visibility = View.VISIBLE

        rel_bip_price_accept.visibility = View.GONE
        rel_bip_reorder.visibility = View.GONE
        txt_befor_confrm_order.visibility = View.GONE

        offerListApi()

    }

    private fun orderFullCoverageDetailApi() {

        try {
            progressBar!!.visibility = View.VISIBLE
            val getFullCoverage = GetFullCoverage()
            getFullCoverage.user_id = AppConstant.getPreferenceText(AppConstant.PREF_USER_ID)
            getFullCoverage.order_id = orderId

            val apiService = AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
            val call = apiService.getfullcoverage(getFullCoverage)

            call.enqueue(object : Callback<GetFullCoverageResponse> {
                override fun onResponse(
                        call: Call<GetFullCoverageResponse>,
                        response: retrofit2.Response<GetFullCoverageResponse>
                ) {
                    progressBar!!.visibility = View.GONE
                    if (response.isSuccessful) {

                        Log.e("HelloEx", "isSuccessful   ")

                        fullCoveragedata = response.body()!!.data
                        fullCoveragedatabip = response.body()!!.data!!.bestInPrice
                        fullCoveragedatabid = response.body()!!.data!!.bestInDelivery



                        if (fullCoveragedata!!.requestStatus!!.equals("Timed out", true)) {
                            if (fullCoveragedata!!.orderStatus!!.equals("Offer Made", true)) {

                                ForDeliveryTimeOut()

                            }
                        }

                        txt_order_id.text = "ID: " + fullCoveragedata!!.orderNo

                        txt_datentime.text = fullCoveragedata?.createdDate!!
                        //AppConstant.convertdatentimetoOrderTime(fullCoveragedata!!.createdDate!!)
                        txt_status.text = fullCoveragedata!!.orderStatus!!
                        txt_status1.text = fullCoveragedata!!.requestStatus!!


                        DrawableCompat.setTint(
                                txt_status1.background,
                                ContextCompat.getColor(
                                        this@FullCoverageActivity,
                                        AppConstant.setsStatus(fullCoveragedata!!.requestStatus!!)
                                )
                        )
                        DrawableCompat.setTint(
                                txt_status.background,
                                ContextCompat.getColor(
                                        this@FullCoverageActivity,
                                        AppConstant.setsStatus(fullCoveragedata!!.orderStatus!!)
                                )
                        )


                        /* if (fullCoveragedatabip!!.deliveryType!!.equals("0")) {
                             txt_bip_del_date.text = "-"
                             txt_bip_del_time.text = "-"
                         } else */

                        if (fullCoveragedatabip!!.offerDeliveryDate!!.equals("")) {
                            txt_bip_deltype.text = fullCoveragedatabip!!.deliveryTypeName!!
                            if (fullCoveragedatabip!!.deliveryType!!.equals("0")) {
                                txt_1.text = resources.getString(R.string.pickup_date)
                                txt_2.text = resources.getString(R.string.pickup_time)
                            }

                            val tempDate0 = fullCoveragedatabip!!.todateFromdate?.split("to")?.get(0)
                            val tempDate1 = fullCoveragedatabip!!.todateFromdate?.split("to")?.get(1)
                            txt_bip_del_date.text = (tempDate0.toString().trim() + " to\n" + tempDate1.toString().trim())
                            /* txt_bip_del_time.text = AppConstant.convertTime(
                                 fullCoveragedatabip!!.deliveryTimeStart!!.toLowerCase(),
                                 fullCoveragedatabip!!.deliveryTimeEnd!!.toLowerCase()
                             )*/
                        } else {
                            if (fullCoveragedatabip!!.offerDeliveryPreference!!.equals("0")) {
                                txt_1.text = resources.getString(R.string.pickup_date)
                                txt_2.text = resources.getString(R.string.pickup_time)
                            }

                            txt_bip_deltype.text = fullCoveragedatabip!!.offerDeliveryPreferenceName!!

                            val tempDate0 = fullCoveragedatabip!!.offerTodateFromdate?.split("to")?.get(0)
                            val tempDate1 = fullCoveragedatabip!!.offerTodateFromdate?.split("to")?.get(1)
                            txt_bip_del_date.text = (tempDate0.toString().trim() + " to\n" + tempDate1.toString().trim())
                            /* txt_bip_del_time.text = AppConstant.convertTime(
                                 fullCoveragedatabip!!.offerDeliveryTimeStart!!.toLowerCase(),
                                 fullCoveragedatabip!!.offerDeliveryTimeEnd!!.toLowerCase()
                             )*/
                        }

                        /*  if (fullCoveragedatabid!!.deliveryType!!.equals("0")) {
                              txt_del_date.text = "-"
                              txt_del_time.text = "-"
                          } else*/


                        Log.e("HelloEx", "isSuccessful   " + fullCoveragedatabid!!.offerTodateFromdate!!)

                        if (fullCoveragedatabid!!.offerTodateFromdate!!.equals("")) {
                            if (fullCoveragedatabid!!.todateFromdate!!.equals("0")) {
                                txt_del_1.text = resources.getString(R.string.pickup_date)
                                txt_del_2.text = resources.getString(R.string.pickup_time)
                            }
                            txt_deltype.text = fullCoveragedatabid!!.deliveryTypeName!!

                            val tempDate0 = fullCoveragedatabid!!.todateFromdate!!.split("to").get(0)
                            val tempDate1 = fullCoveragedatabid!!.todateFromdate!!.split("to").get(1)
                            txt_del_date.text = (tempDate0.toString().trim() + " to\n" + tempDate1.toString().trim())
                            // txt_del_date.text = (fullCoveragedatabid!!.todateFromdate!!)
                            /*  txt_del_time.text = AppConstant.convertTime(
                                  fullCoveragedatabid!!.deliveryTimeStart!!.toLowerCase()+" to ",
                                  fullCoveragedatabid!!.deliveryTimeEnd!!.toLowerCase()
                              )*/
                        } else {
                            if (fullCoveragedatabid!!.offerDeliveryPreference!!.equals("0")) {
                                txt_del_1.text = resources.getString(R.string.pickup_date)
                                txt_del_2.text = resources.getString(R.string.pickup_time)
                            }
                            txt_deltype.text = fullCoveragedatabid!!.offerDeliveryPreferenceName!!

                            val tempDate0 = fullCoveragedatabid?.offerTodateFromdate!!.split("to").get(0)
                            val tempDate1 = fullCoveragedatabid?.offerTodateFromdate!!.split("to").get(1)
                            txt_del_date.text = (tempDate0.toString().trim() + " to\n" + tempDate1.toString().trim())
                            //  txt_del_date.text = (fullCoveragedatabid?.offerTodateFromdate!!)
                            /* txt_del_time.text = AppConstant.convertTime(
                                 fullCoveragedatabid!!.offerDeliveryTimeStart!!.toLowerCase()+" to ",
                                 fullCoveragedatabid!!.offerDeliveryTimeEnd!!.toLowerCase()
                             )*/
                        }

                        storeIdbip = fullCoveragedatabip!!.storeId!!
                        offerIdbid = fullCoveragedatabip!!.offerId!!
                        storeIdbid = fullCoveragedatabid!!.storeId!!
                        offerIdbip = fullCoveragedatabid!!.offerId!!
                        orderTypebip = fullCoveragedatabid!!.orderType!!
                        orderTypebid = fullCoveragedatabid!!.orderType!!

                        txt_bip_perc.text = fullCoveragedatabip!!.offerDiscountPercentage!! + "% OFF"
                        txt_bip_price_rs.text = "₹ " + fullCoveragedatabip!!.offerFinalAmount



                        txt_del_perc.text = fullCoveragedatabid!!.offerDiscountPercentage!! + "% OFF"
                        txt_del_rs.text = "₹ " + fullCoveragedatabid!!.offerFinalAmount

                    } else {
                        try {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            Toast.makeText(
                                    this@FullCoverageActivity,
                                    "" + jObjError.getString("message"),
                                    Toast.LENGTH_SHORT
                            ).show()


                        } catch (e: Exception) {
                            Toast.makeText(this@FullCoverageActivity, e.message, Toast.LENGTH_LONG)
                                    .show()
                        }

                    }
                }


                override fun onFailure(call: Call<GetFullCoverageResponse>, t: Throwable) {
                    // progressBarHandler.hide();
                    Log.e("HelloEx", "onFailure   " + t.message)
                    progressBar!!.visibility = View.GONE
                }
            })

        } catch (e: Exception) {
            Log.e("HelloEx", "ErrorApi   " + e)
        }
    }


    private fun ForDeliveryTimeOut() {
        constrain_delivry_time_out_OM!!.visibility = View.VISIBLE
        rel_bip_price_accept!!.isClickable = false
        bip_accept!!.setTextColor(ContextCompat.getColor(this@FullCoverageActivity, R.color.colorBlackWithAlpha))
        bip_accept!!.setTextViewDrawableColor(R.color.colorBlackWithAlpha)

        constrain_delivry_time_out_OM_BED!!.visibility = View.VISIBLE
        rel_delivry_accept!!.isClickable = false
        bid_accept!!.setTextColor(ContextCompat.getColor(this@FullCoverageActivity, R.color.colorBlackWithAlpha))
        bid_accept!!.setTextViewDrawableColor(R.color.colorBlackWithAlpha)


    }

    private fun AppCompatTextView.setTextViewDrawableColor(color: Int) {
        for (drawable in this.compoundDrawablesRelative) {
            drawable?.mutate()
            drawable?.colorFilter = PorterDuffColorFilter(
                    color, PorterDuff.Mode.SRC_IN
            )
        }
    }


    private fun offerAcceptApi(bip: String) {
        progressBar!!.visibility = View.VISIBLE
        val buyerOrderAccept = BuyerOrderAccept()
        buyerOrderAccept.user_id = AppConstant.getPreferenceText(AppConstant.PREF_USER_ID)
        buyerOrderAccept.order_id = customOrderdata!!.orderId
        buyerOrderAccept.store_id = storeId
        buyerOrderAccept.offer_id = offerId
        buyerOrderAccept.best_in_type = bip
        buyerOrderAccept.offer_coverage_type = "0"

        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.buyerorderaccept(buyerOrderAccept)

        call.enqueue(object : Callback<BuyerOrderAcceptResponse> {
            override fun onResponse(
                    call: Call<BuyerOrderAcceptResponse>,
                    response: retrofit2.Response<BuyerOrderAcceptResponse>
            ) {
                progressBar!!.visibility = View.GONE
                if (response.isSuccessful) {

                    isAccepted = "1"

                    txt_befor_confrm_order.visibility = View.VISIBLE
                    rel_bip_price_accept.visibility = View.GONE
                    rel_bip_reorder.visibility = View.GONE
                    view.visibility = View.GONE


                    txt_best_in_delivery.visibility = View.GONE
                    card2.visibility = View.GONE
                    offerId = offerIdbip
                    orderType = orderTypebip
                    storeId = storeIdbip
                    if (fullCoveragedata!!.orderStatus!!.equals("Invoiced", ignoreCase = true)) {
                        set_confrm_order_layout()
                    } else {
                        setLayoutForAwaiting()
                    }
//                    else if(){
//                        setLayoutForAwaiting()
//                    }

                    if (bip.equals("1")) {
                        txt_price_best_in_price.setText(R.string.best_in_delivery)
                        offerId = offerIdbid
                        orderType = orderTypebid
                        setData()
                    }

                } else {
                    /* try {
                         val jObjError = JSONObject(response.errorBody()!!.string())
                         Toast.makeText(
                             this@FullCoverageActivity,
                             "" + jObjError.getString("message"),
                             Toast.LENGTH_SHORT
                         ).show()


                     } catch (e: Exception) {
                         Toast.makeText(this@FullCoverageActivity, e.message, Toast.LENGTH_LONG)
                             .show()
                     }*/

                }
            }


            override fun onFailure(call: Call<BuyerOrderAcceptResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar!!.visibility = View.GONE
            }
        })
    }

    private fun offerListApi() {
        progressBar!!.visibility = View.VISIBLE
        val buyerOrderAcceptDetail = BuyerOrderAcceptDetail()
        buyerOrderAcceptDetail.order_id = orderId

        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.buyerorderacceptdetail(buyerOrderAcceptDetail)

        call.enqueue(object : Callback<BuyerOrderAcceptDetailResponse> {
            override fun onResponse(
                    call: Call<BuyerOrderAcceptDetailResponse>,
                    response: retrofit2.Response<BuyerOrderAcceptDetailResponse>
            ) {
                progressBar!!.visibility = View.GONE
                if (response.isSuccessful) {

                    storedata = response.body()!!.data!!.store
                    orderType = response.body()!!.data!!.orderType!!
                    if (response.body()!!.data!!.offerCoverageType!!.equals("1")) {
                        txt_title.text = resources.getString(R.string.partial_cvrage)
                        con.visibility = View.VISIBLE
                    } else {
                        txt_title.text = resources.getString(R.string.full_cvrage)
                    }
                    if (response.body()!!.data!!.color!!.size > 0) {

                        for (i in 0 until response.body()!!.data!!.color!!.size) {
                            val layout = LinearLayout(this@FullCoverageActivity)
                            layout.id = i
                            layout.layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            layout.orientation = LinearLayout.HORIZONTAL
                            layout.setPadding(10, 0, 0, 0)

                            val imageView = ImageView(this@FullCoverageActivity)
                            imageView.layoutParams = LinearLayout.LayoutParams(40, 40)
                            imageView.setImageDrawable(
                                    ContextCompat.getDrawable(
                                            this@FullCoverageActivity,
                                            R.drawable.custom_color_code
                                    )
                            )

                            imageView.setColorFilter(
                                    Color.parseColor(
                                            "#" + response.body()!!.data!!.color!!.get(
                                                    i
                                            )
                                    )
                            )

                            imageView.setPadding(0, 0, 0, 0)

                            layout.addView(imageView)

                            ll_code?.addView(layout)
                        }
                    }

                    txt_order_id.text = "ID: " + response.body()!!.data!!.orderNo
                    orderId = response.body()!!.data!!.orderId!!

                    txt_datentime.text = response.body()!!.data!!.createdDate!!
                    // AppConstant.convertdatentimetoOrderTime(response.body()!!.data!!.createdDate!!).toLowerCase()
                    txt_status.text = response.body()!!.data!!.orderStatus!!
                    txt_status1.text = response.body()!!.data!!.requestStatus!!
                    DrawableCompat.setTint(
                            txt_status1.background,
                            ContextCompat.getColor(
                                    this@FullCoverageActivity,
                                    AppConstant.setsStatus(response.body()!!.data!!.requestStatus!!)
                            )
                    )
                    DrawableCompat.setTint(
                            txt_status.background,
                            ContextCompat.getColor(
                                    this@FullCoverageActivity,
                                    AppConstant.setsStatus(response.body()!!.data!!.orderStatus!!)
                            )
                    )



                    txt_store_name.text = storedata!!.name
                    txt_store_type.text = storedata!!.type
                    //  img_map.text = storedata!!.name
                    txt_contact_no.text = "+91 " + storedata!!.phone
                    txt_store_add.text = storedata!!.geoLocation

                    offerId = response.body()!!.data!!.offerId!!
                    if (response.body()!!.data!!.bestInType.equals("1")) {
                        txt_price_best_in_price.setText(R.string.best_in_delivery)
                    }

                    /*if (response.body()!!.data!!.deliveryType!!.equals("0")) {
                        txt_bip_del_date.text = "-"
                        txt_bip_del_time.text = "-"
                    } else*/ if (response.body()!!.data!!.offerTodateFromdate!!.equals("")) {
                        if (response.body()!!.data!!.todateFromdate!!.equals("0")) {
                            txt_1.text = resources.getString(R.string.pickup_date)
                            txt_2.text = resources.getString(R.string.pickup_time)
                        }
                        txt_bip_deltype.text = response.body()!!.data!!.deliveryTypeName!!

                        val tempDate0 = response.body()!!.data!!.todateFromdate!!.split("to").get(0)
                        val tempDate1 = response.body()!!.data!!.todateFromdate!!.split("to").get(1)
                        txt_bip_del_date.text = (tempDate0.toString().trim() + " to\n" + tempDate1.toString().trim())
                        /*  txt_bip_del_time.text = AppConstant.convertTime(
                              response.body()!!.data!!.startTime!!.toLowerCase(),
                              response.body()!!.data!!.endTime!!.toLowerCase()
                          )*/
                    } else {
                        if (response.body()!!.data!!.offerDeliveryPreference!!.equals("0")) {
                            txt_1.text = resources.getString(R.string.pickup_date)
                            txt_2.text = resources.getString(R.string.pickup_time)
                        }
                        txt_bip_deltype.text = response.body()!!.data!!.offerDeliveryPreferenceName!!

                        val tempDate0 = response.body()!!.data!!.offerTodateFromdate!!.split("to").get(0)
                        val tempDate1 = response.body()!!.data!!.offerTodateFromdate!!.split("to").get(1)
                        txt_bip_del_date.text = (tempDate0.toString().trim() + " to\n" + tempDate1.toString().trim())
                        /*txt_bip_del_time.text = AppConstant.convertTime(
                            response.body()!!.data!!.offerDeliveryTimeStart!!.toLowerCase(),
                            response.body()!!.data!!.offerDeliveryTimeEnd!!.toLowerCase()
                        )*/
                    }


                    storeIdbip = response.body()!!.data!!.storeId!!
                    txt_bip_perc.text =
                            response.body()?.data!!.offerDiscountPercentage!! + "% OFF"
                    txt_bip_price_rs.text = "₹ " + response.body()?.data!!.offerFinalAmount
                    // img_info
                } else {
                    /*  try {
                          val jObjError = JSONObject(response.errorBody()!!.string())
                          Toast.makeText(
                              this@FullCoverageActivity,
                              "" + jObjError.getString("message"),
                              Toast.LENGTH_SHORT
                          ).show()


                      } catch (e: Exception) {
                          Toast.makeText(this@FullCoverageActivity, e.message, Toast.LENGTH_LONG)
                              .show()
                      }*/

                }
            }

            override fun onFailure(call: Call<BuyerOrderAcceptDetailResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar!!.visibility = View.GONE
            }
        })
    }

    private fun setData() {
        /*if (fullCoveragedatabid!!.deliveryType!!.equals("0")) {
            txt_bip_del_date.text = "-"
            txt_bip_del_time.text = "-"
        } else*/ if (fullCoveragedatabid!!.offerTodateFromdate!!.equals("")) {
            if (fullCoveragedatabid!!.todateFromdate!!.equals("0")) {
                txt_1.text = resources.getString(R.string.pickup_date)
                txt_2.text = resources.getString(R.string.pickup_time)
            }
            txt_bip_deltype.text = fullCoveragedatabid!!.deliveryTypeName!!

            val tempDate0 = fullCoveragedatabid!!.todateFromdate!!.split("to").get(0)
            val tempDate1 = fullCoveragedatabid!!.todateFromdate!!.split("to").get(1)
            txt_bip_del_date.text = (tempDate0.toString().trim() + " to\n" + tempDate1.toString().trim())
            // txt_bip_del_date.text = (fullCoveragedatabid!!.todateFromdate!!)
            /*txt_bip_del_time.text = AppConstant.convertTime(
                fullCoveragedatabid!!.deliveryTimeStart!!.toLowerCase(),
                fullCoveragedatabid!!.deliveryTimeEnd!!.toLowerCase()
            )*/
        } else {
            if (fullCoveragedatabid!!.offerDeliveryPreference!!.equals("0")) {
                txt_1.text = resources.getString(R.string.pickup_date)
                txt_2.text = resources.getString(R.string.pickup_time)
            }
            txt_bip_deltype.text = fullCoveragedatabid!!.offerDeliveryPreferenceName!!

            val tempDate0 = fullCoveragedatabid!!.offerTodateFromdate!!.split("to").get(0)
            val tempDate1 = fullCoveragedatabid!!.offerTodateFromdate!!.split("to").get(1)
            txt_bip_del_date.text = (tempDate0.toString().trim() + " to\n" + tempDate1.toString().trim())
            // txt_bip_del_date.text = (fullCoveragedatabid!!.offerTodateFromdate!!)
            /* txt_bip_del_time.text = AppConstant.convertTime(
                 fullCoveragedatabid!!.offerDeliveryTimeStart!!.toLowerCase(),
                 fullCoveragedatabid!!.offerDeliveryTimeEnd!!.toLowerCase()
             )*/
        }

        storeIdbip = fullCoveragedatabid!!.storeId!!
        txt_bip_perc.text = fullCoveragedatabid!!.offerDiscountPercentage!! + "% OFF"
        txt_bip_price_rs.text = "₹ " + fullCoveragedatabid!!.offerFinalAmount
    }

    companion object {
        var storedata: BuyerOrderAcceptDetailResponse.Store? = null
        var fullCoveragedata: GetFullCoverageResponse.Data? = null
        var fullCoveragedatabip: GetFullCoverageResponse.BestInPrice? = null
        var fullCoveragedatabid: GetFullCoverageResponse.BestInDelivery? = null
    }
}