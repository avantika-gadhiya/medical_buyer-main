package bluesharklabs.com.medical.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.adapters.CustomerOrderOfferAdapter
import bluesharklabs.com.medical.adapters.OrderOfferAdapter
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.OrderDetailOffer
import bluesharklabs.com.medical.response.OrderDetailResponse
import bluesharklabs.com.medical.utils.AppConstant
import kotlinx.android.synthetic.main.activity_my_orders_detail.*
import kotlinx.android.synthetic.main.activity_my_orders_detail.btn
import kotlinx.android.synthetic.main.activity_my_orders_detail.img_back
import kotlinx.android.synthetic.main.activity_view_offer.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import kotlinx.android.synthetic.main.activity_view_offer.progressBar as progressBar1

class MyOrdersDetailActivity : AppCompatActivity(), View.OnClickListener {

    private var orderId=""
    private var storeId=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView()
            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//  set status text dark

        setContentView(R.layout.activity_my_orders_detail)


        img_back.setOnClickListener(this)
        btn.setOnClickListener(this)
        card_view_invoice.setOnClickListener(this)

        if(intent.getStringExtra("Order_Id")!= null) {
           // orderId = intent.getStringExtra("Order_Id")
           // storeId = intent.getStringExtra("Store_Id")

            orderDetailApi(orderId, storeId)
        }

    }
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.card_view_invoice -> {
                startActivity(
                    Intent(this@MyOrdersDetailActivity, InvoiceActivity::class.java))
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.btn -> {
                startActivity(
                    Intent(this@MyOrdersDetailActivity, FinalOrderActivity::class.java))
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

    fun orderDetailApi(orderId: String,storeId: String) {

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

                    textView28.text=response.body()?.data?.orderNo
                    textView30.text=response.body()?.data?.createdDate
                    appCompatTextView2.text=response.body()?.data?.requestStatus
                    appCompatTextView.text=response.body()?.data?.orderStatus
                    textView102.text=response.body()?.data?.buyingPreferenceName
                    textView111.text=response.body()?.data?.deliveryDate
                    textView113.text=response.body()?.data?.startTime
                    textView114.text=response.body()?.data?.deliveryTypeName

                } else {


                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@MyOrdersDetailActivity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@MyOrdersDetailActivity, e.message, Toast.LENGTH_LONG)
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

}
