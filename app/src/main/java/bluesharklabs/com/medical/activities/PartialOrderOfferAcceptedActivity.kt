package bluesharklabs.com.medical.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.BuyerOrderAcceptDetail
import bluesharklabs.com.medical.response.BuyerOrderAcceptDetailResponse
import bluesharklabs.com.medical.utils.AppConstant
import kotlinx.android.synthetic.main.activity_partial_order_offer_accepted.*
import retrofit2.Call
import retrofit2.Callback

class PartialOrderOfferAcceptedActivity : AppCompatActivity(), View.OnClickListener {
    var storedata: BuyerOrderAcceptDetailResponse.Store? = null
    var orderType = ""
    var orderId = ""
    var storeId = ""
    var offerId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView()
            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//  set status text dark

        setContentView(R.layout.activity_partial_order_offer_accepted)

           if (intent != null) {
            if (intent.getStringExtra("orderId") != null) {
                orderId = intent.getStringExtra("orderId")
            }
        }
        init()
    }

    private fun init() {
        txt_befor_confrm_order.setOnClickListener(this)
        txt_view_order.setOnClickListener(this)
        txt_view_invoice.setOnClickListener(this)
        txt_track_ordr_n_make_payment.setOnClickListener(this)
        txt_view_p_offer.setOnClickListener(this)
        img_back.setOnClickListener(this)

        offerListApi()
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

                    /*txt_store_name.text = storedata!!.name
                    txt_store_type.text = storedata!!.type
                    //  img_map.text = storedata!!.name
                    txt_contact_no.text =  "+91 "+storedata!!.phone
                    txt_store_add.text =storedata!!.geoLocation*/

                    offerId = response.body()!!.data!!.offerId!!
                    if (response.body()!!.data!!.bestInType.equals("1")){
                        txt_price_best_in_price.setText(R.string.best_in_delivery)
                    }


                    txt_bip_deltype.text = response.body()!!.data!!.deliveryTypeName!!


                    if (response.body()!!.data!!.deliveryType!!.equals("0")) {
                        txt_bip_del_date.text = "-"
                        txt_bip_del_time.text = "-"
                    } else if (response.body()!!.data!!.offerTodateFromdate!!.equals("")) {
                        txt_bip_del_date.text = (response.body()?.data!!.todateFromdate!!)
                       /* txt_bip_del_time.text = AppConstant.convertTime(
                            response.body()?.data?.startTime!!.toLowerCase()+" to ",
                            response.body()?.data?.endTime!!.toLowerCase()
                        )*/
                    } else {
                        txt_bip_del_date.text = (response.body()?.data!!.offerTodateFromdate!!)
                      /*  txt_bip_del_time.text = AppConstant.convertTime(
                            response.body()!!.data!!.offerDeliveryTimeStart!!.toLowerCase()+" to ",
                            response.body()!!.data!!.offerDeliveryTimeEnd!!.toLowerCase()
                        )*/
                    }


                    storeId =  response.body()!!.data!!.storeId!!
                    txt_bip_perc.text =  response.body()!!.data!!.offerDiscountPercentage!! + "% OFF"
                    txt_bip_price_rs.text = "Rs." +  response.body()!!.data!!.offerFinalAmount
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
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.txt_befor_confrm_order -> {

                txt_view_order.visibility = View.VISIBLE
                view.visibility = View.VISIBLE
                txt_ur_order_confirmed.visibility = View.VISIBLE
                card3.visibility = View.VISIBLE
                card4.visibility = View.VISIBLE

                txt_befor_confrm_order.visibility = View.GONE
            }
            R.id.txt_view_order -> {
                startActivity(
                    Intent(this@PartialOrderOfferAcceptedActivity, FinalOrderActivity::class.java)
                        .putExtra("vieworder", "1")
                            .putExtra("offerId", offerId)
                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.txt_view_p_offer -> {
                startActivity(
                    Intent(
                        this@PartialOrderOfferAcceptedActivity,
                        ViewOfferActivity::class.java
                    )
                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.txt_view_invoice -> {
                startActivity(
                    Intent(
                        this@PartialOrderOfferAcceptedActivity,
                        InvoiceActivity::class.java
                    )
                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.txt_track_ordr_n_make_payment -> {
                startActivity(
                    Intent(
                        this@PartialOrderOfferAcceptedActivity,
                        TrackOrderActivity::class.java
                    )
                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }
}
