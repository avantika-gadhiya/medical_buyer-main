package bluesharklabs.com.medical.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.adapters.MyOrdersAdapter
import bluesharklabs.com.medical.adapters.SortByAdapter
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.OrderListByFilter
import bluesharklabs.com.medical.response.MyOrderResponse
import bluesharklabs.com.medical.utils.AppConstant
import kotlinx.android.synthetic.main.activity_my_orders.*
import kotlinx.android.synthetic.main.dashboard_fragment.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class MyOrdersActivity : AppCompatActivity(), View.OnClickListener, SortByAdapter.sortBy {


    private var recyclerView: RecyclerView? = null
    private var dialog: Dialog? = null
    private var myOrdersAdapter: MyOrdersAdapter? = null
    private lateinit var mBroadcastManager: LocalBroadcastManager

    private var mysort_value = 1
    private var type = ""
    private var status = ""
    private var storeName = ""
    private var city = ""
    private var area = ""
    private var zipcode = ""
    private var offer = ""

    private var myOrder: ArrayList<MyOrderResponse.Datum> = arrayListOf()
    var language = arrayOf(
            "By Date",
            "By Invoice Value"
//            ,
//            "By Full Coverage",
//            "By Partial Coverage"
    )

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR//  set status text dark

        setContentView(R.layout.activity_my_orders)


        recyclerView = findViewById(R.id.recycl)

        img_back.setOnClickListener(this)
        txt_sortby.setOnClickListener(this)
        txt_filtr.setOnClickListener(this)


        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager =
                LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        mBroadcastManager = LocalBroadcastManager.getInstance(this)
        mBroadcastManager.registerReceiver(mReceiverFilterResult, IntentFilter(resources.getString(R.string.broadcastFilterResult)))

        myOrder("", "", "","", "", "", "", 1)
    }

    fun showDialog() {
        dialog = Dialog(this)
        dialog!!.setContentView(R.layout.dialog_sort_by)
        dialog!!.window.setBackgroundDrawableResource(android.R.color.white)
        //  dialog .setCancelable(false)
        val lv = dialog!!.findViewById(R.id.recycl) as RecyclerView
        val sortByAdapter: SortByAdapter

        lv.layoutManager = LinearLayoutManager(this)

        sortByAdapter = SortByAdapter(this, language, this)
        lv.adapter = sortByAdapter
        /*close_icon.setOnClickListener {
            dialog.dismiss()
        }*/

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog!!.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.BOTTOM
        //   lp.windowAnimations = R.style.DialogAnimation;
        dialog!!.window!!.attributes = lp
        dialog!!.setCanceledOnTouchOutside(true)
        dialog!!.show()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.txt_sortby -> {
                showDialog()
            }
            R.id.txt_filtr -> {
                startActivity(
                        Intent(this@MyOrdersActivity, SliderFilterActivity::class.java))
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

    override fun sortby(pos: Int) {

        mysort_value = pos
        myOrder(type, status,storeName, city, area, zipcode, offer, mysort_value)
        dialog!!.dismiss()

    }

    fun myOrder(type: String, status: String, storeName: String, city: String, area: String, zipcode: String, myoffers: String, sorts: Int) {

        progressBarAllOrders.visibility = View.VISIBLE
        val orderlist = OrderListByFilter()
        orderlist.user_id = AppConstant.getPreferenceText(AppConstant.PREF_USER_ID)

        orderlist.delivery_type = type
        orderlist.status = status
        orderlist.store_id = storeName
        orderlist.store_criteria_city = city
        orderlist.store_criteria_area = area
        orderlist.store_criteria_zipcode = zipcode
        orderlist.offer_status = myoffers
        orderlist.sort_value = sorts
        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.myOrder(orderlist)

        call.enqueue(object : Callback<MyOrderResponse> {
            override fun onResponse(
                    call: Call<MyOrderResponse>,
                    response: retrofit2.Response<MyOrderResponse>
            ) {
                progressBarAllOrders.visibility = View.GONE
                if (response.isSuccessful) {
                    myOrder = arrayListOf()
                    myOrder = response.body()?.data!!

                    myOrdersAdapter = MyOrdersAdapter(this@MyOrdersActivity, myOrder)
                    recyclerView!!.adapter = myOrdersAdapter
                    myOrdersAdapter!!.notifyDataSetChanged()

                } else {

                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@MyOrdersActivity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@MyOrdersActivity, e.message, Toast.LENGTH_LONG).show()
                    }

                }
            }


            override fun onFailure(call: Call<MyOrderResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBarAllOrders.visibility = View.GONE
            }
        })
    }

    /*
*  Receiver which get FIlters
*/
    private val mReceiverFilterResult: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            type = intent.getStringExtra(resources.getString(R.string.filterType))
            status = intent.getStringExtra(resources.getString(R.string.filterStatus))
            storeName = intent.getStringExtra(resources.getString(R.string.filterStoreName))
            city = intent.getStringExtra(resources.getString(R.string.filterCity))
            area = intent.getStringExtra(resources.getString(R.string.filterArea))
            zipcode = intent.getStringExtra(resources.getString(R.string.filterZipcode))
            offer = intent.getStringExtra(resources.getString(R.string.filterOffer))


            myOrder(type, status, storeName, city, area, zipcode, offer, mysort_value)


        }
    }
}
