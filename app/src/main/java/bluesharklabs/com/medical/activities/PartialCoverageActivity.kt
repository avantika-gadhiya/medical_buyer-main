package bluesharklabs.com.medical.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.activities.ViewOfferActivity.Companion.customOrderdata
import bluesharklabs.com.medical.adapters.PartialCoverageAdapter
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.BuyerOrderAccept
import bluesharklabs.com.medical.model.PartialCoverageOrder
import bluesharklabs.com.medical.model.PartialOfferFilter
import bluesharklabs.com.medical.model.Table
import bluesharklabs.com.medical.response.BuyerOrderAcceptResponse
import bluesharklabs.com.medical.response.PartialCoverageOrderResponse
import bluesharklabs.com.medical.utils.AppConstant
import bluesharklabs.com.medical.utils.PinnedSectionListView
import kotlinx.android.synthetic.main.activity_filter.*
import kotlinx.android.synthetic.main.activity_partial_coverage.*
import kotlinx.android.synthetic.main.activity_partial_coverage.img_back
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class PartialCoverageActivity : AppCompatActivity(), View.OnClickListener,
        PartialCoverageAdapter.offerAcc {


    private var listView: PinnedSectionListView? = null
    private var table: Table? = null
    private val FILTER_REQUEST_CODE = 125

    private var partialCoverageAdapter: PartialCoverageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR//  set status text dark

        setContentView(R.layout.activity_partial_coverage)

        if (intent != null) {
            if (intent.getStringExtra("isAvailable").equals("1")) {
                listView = findViewById(R.id.recycl_partial_cvrage)

                img_filter.setOnClickListener(this)
                img_back.setOnClickListener(this)
                //  partialCoverageAdapter?.clearData()

                partialCoverageAdapter = PartialCoverageAdapter(this, this)

                table = Table()

                offerListApi()
            } else {
                showDialog("There is no Partial Coverage Offer till now.", 0)
            }
        }


    }

    private fun showDialog(msg: String, from: Int) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.cutsom_dialog_layout)
        dialog.window.setBackgroundDrawableResource(android.R.color.white)

        //  dialog .setCancelable(false)
        val close_icon = dialog.findViewById(R.id.img_close) as AppCompatImageView
        val textView116 = dialog.findViewById(R.id.textView116) as AppCompatTextView
        val textView115 = dialog.findViewById(R.id.textView115) as AppCompatTextView
        textView115.text = "Partial Coverage Offer"
        textView116.text = msg
        close_icon.setOnClickListener {
            if (from == 1) {
                dialog.dismiss()
                sendForResult()
            } else {
                dialog.dismiss()
                finish()
            }

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

    private fun offerListApi() {
        progressBar!!.visibility = View.VISIBLE
        val partialCoverageOrder = PartialCoverageOrder()
        partialCoverageOrder.order_id = customOrderdata!!.orderId!!
        partialCoverageOrder.user_id = AppConstant.getPreferenceText(AppConstant.PREF_USER_ID)

        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.partialcoverage(partialCoverageOrder)

        call.enqueue(object : Callback<PartialCoverageOrderResponse> {
            override fun onResponse(
                    call: Call<PartialCoverageOrderResponse>,
                    response: retrofit2.Response<PartialCoverageOrderResponse>
            ) {
                progressBar!!.visibility = View.GONE
                if (response.isSuccessful) {
                    txt_order_id.text = "ID: " + response.body()!!.data!!.orderNo
                    //  orderId = response.body()!!.data!!.orderId!!
                    txt_datentime.text =
                            (response.body()!!.data!!.createdDate!!)
                    txt_status.text = response.body()!!.data!!.orderStatus!!
                    txt_status1.text = response.body()!!.data!!.requestStatus!!
                    DrawableCompat.setTint(
                            txt_status1.background,
                            ContextCompat.getColor(
                                    this@PartialCoverageActivity,
                                    AppConstant.setsStatus(response.body()!!.data!!.requestStatus!!)
                            )
                    )
                    DrawableCompat.setTint(
                            txt_status.background,
                            ContextCompat.getColor(
                                    this@PartialCoverageActivity,
                                    AppConstant.setsStatus(response.body()!!.data!!.orderStatus!!)
                            )
                    )
                    storedata = response.body()!!.data!!
                    partialCoverageAdapter!!.clearData()

                    if (storedata!!.offer!!.size > 0) {

                        //   partialCoverageAdapter!!.clearData()
                        for (i in storedata!!.offer!!.indices) {

                            table = Table()

                            table?.status = "offers for"
                            table?.orderStatus = response.body()!!.data!!.orderStatus!!
                            table?.orderRequest = response.body()!!.data!!.requestStatus!!
                            table?.color = storedata!!.offer!!.get(i).color
                            table?.type = (SECTION)
                            table?.let { partialCoverageAdapter?.addSectionHeaderItem(it) }

                            table?.bestInPrice = storedata!!.offer!!.get(i).bestInPrice
                            table?.bestInDelivery = storedata!!.offer!!.get(i).bestInDelivery
                            table?.type = ITEM
                            table?.let { partialCoverageAdapter!!.addItem(it) }
                        }

                        listView!!.adapter = partialCoverageAdapter
                        partialCoverageAdapter!!.notifyDataSetChanged()
                    }
                    /* storedata = response.body()!!.data!!.store

                     txt_store_name.text = storedata!!.name
                     txt_store_type.text = storedata!!.type
                     //  img_map.text = storedata!!.name
                     txt_contact_no.text =  "+91 "+storedata!!.phone
                     txt_store_add.text =storedata!!.geoLocation
                     // img_info*/
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@PartialCoverageActivity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@PartialCoverageActivity, e.message, Toast.LENGTH_LONG)
                                .show()
                    }

                }
            }

            override fun onFailure(call: Call<PartialCoverageOrderResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar!!.visibility = View.GONE
            }
        })
    }

    private fun offerAcceptApi(
            offerId: String,
            orderId: String,
            storeId: String,
            bip: String
    ) {
        progressBar!!.visibility = View.VISIBLE
        val buyerOrderAccept = BuyerOrderAccept()
        buyerOrderAccept.user_id = AppConstant.getPreferenceText(AppConstant.PREF_USER_ID)
        buyerOrderAccept.order_id = orderId
        buyerOrderAccept.store_id = storeId
        buyerOrderAccept.offer_id = offerId
        buyerOrderAccept.best_in_type = bip
        buyerOrderAccept.offer_coverage_type = "1"

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
                    startActivity(Intent(this@PartialCoverageActivity, FullCoverageActivity::class.java)
                            .putExtra("partial", orderId)
                    )
                    finish()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

                } else {
                     try {
                         val jObjError = JSONObject(response.errorBody()!!.string())
                         Toast.makeText(
                             this@PartialCoverageActivity,
                             "" + jObjError.getString("message"),
                             Toast.LENGTH_SHORT
                         ).show()


                     } catch (e: Exception) {
                         Toast.makeText(this@PartialCoverageActivity, e.message, Toast.LENGTH_LONG)
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.img_filter -> {

                sendForResult()
            }
        }
    }


    fun sendForResult(){
        val intent = Intent(this@PartialCoverageActivity, FilterActivity::class.java)
        intent.putExtra("order_id", customOrderdata!!.orderId!!)
        startActivityForResult(intent, FILTER_REQUEST_CODE)// Activity is started with requestCode 2
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    companion object {
        val ITEM = 0
        val SECTION = 1

        var storedata: PartialCoverageOrderResponse.Data? = null
    }

    override fun acc(offerid: String, orderId: String, storeId: String, bit: String) {
        offerAcceptApi(offerid, orderId, storeId, bit)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILTER_REQUEST_CODE) {
            val isReset = data!!.getBooleanExtra("isReset",false)
            if(isReset){
                offerListApi()
            }else{
                val my_filter = data.getSerializableExtra("filterData") as PartialOfferFilter
                onApplyFilter(my_filter)
            }

        }
    }


    private fun onApplyFilter(offerfilter: PartialOfferFilter) {
        progressBar!!.visibility = View.VISIBLE

        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.buyerpartialofferfilter(offerfilter)

        call.enqueue(object : Callback<PartialCoverageOrderResponse> {
            override fun onResponse(
                    call: Call<PartialCoverageOrderResponse>,
                    response: retrofit2.Response<PartialCoverageOrderResponse>
            ) {
                progressBar!!.visibility = View.GONE
                if (response.isSuccessful) {
                    txt_order_id.text = "ID: " + response.body()!!.data!!.orderNo
                    //  orderId = response.body()!!.data!!.orderId!!
                    txt_datentime.text =
                            (response.body()!!.data!!.createdDate!!)
                    txt_status.text = response.body()!!.data!!.orderStatus!!
                    txt_status1.text = response.body()!!.data!!.requestStatus!!
                    DrawableCompat.setTint(
                            txt_status1.background,
                            ContextCompat.getColor(
                                    this@PartialCoverageActivity,
                                    AppConstant.setsStatus(response.body()!!.data!!.requestStatus!!)
                            )
                    )
                    DrawableCompat.setTint(
                            txt_status.background,
                            ContextCompat.getColor(
                                    this@PartialCoverageActivity,
                                    AppConstant.setsStatus(response.body()!!.data!!.orderStatus!!)
                            )
                    )
                    storedata = response.body()!!.data!!
                    partialCoverageAdapter!!.clearData()

                    if (storedata!!.offer!!.size > 0) {

                        //   partialCoverageAdapter!!.clearData()
                        for (i in storedata!!.offer!!.indices) {

                            table = Table()

                            table?.status = "offers for"
                            table?.orderStatus = response.body()!!.data!!.orderStatus!!
                            table?.orderRequest = response.body()!!.data!!.requestStatus!!
                            table?.color = storedata!!.offer!!.get(i).color
                            table?.type = (SECTION)
                            table?.let { partialCoverageAdapter?.addSectionHeaderItem(it) }

                            table?.bestInPrice = storedata!!.offer!!.get(i).bestInPrice
                            table?.bestInDelivery = storedata!!.offer!!.get(i).bestInDelivery
                            table?.type = ITEM
                            table?.let { partialCoverageAdapter!!.addItem(it) }
                        }

                        listView!!.adapter = partialCoverageAdapter
                        partialCoverageAdapter!!.notifyDataSetChanged()
                    }
                    /* storedata = response.body()!!.data!!.store

                     txt_store_name.text = storedata!!.name
                     txt_store_type.text = storedata!!.type
                     //  img_map.text = storedata!!.name
                     txt_contact_no.text =  "+91 "+storedata!!.phone
                     txt_store_add.text =storedata!!.geoLocation
                     // img_info*/
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())

//                        Log.e("HElloError","Erroris   "+jObjError.getString("message"))
//                        Toast.makeText(
//                                this@PartialCoverageActivity,
//                                "" + jObjError.getString("message"),
//                                Toast.LENGTH_SHORT
//                        ).show()

                        showDialog("There is no Offer for selected Product(s) till now.",1)


                    } catch (e: Exception) {
                        Toast.makeText(this@PartialCoverageActivity, e.message, Toast.LENGTH_LONG)
                                .show()
                    }

                }
            }

            override fun onFailure(call: Call<PartialCoverageOrderResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar!!.visibility = View.GONE
            }
        })
    }
}