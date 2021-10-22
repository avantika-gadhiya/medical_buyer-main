package bluesharklabs.com.medical.activities

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatImageView
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.adapters.RecentOrderAdapter
import bluesharklabs.com.medical.fragments.DashBoardFragment.Companion.orderListdata
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.OrderList
import bluesharklabs.com.medical.response.OrderListResponse
import bluesharklabs.com.medical.utils.AppConstant
import kotlinx.android.synthetic.main.activity_recent_order_view_all.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class RecentOrderViewAllActivity : AppCompatActivity(), View.OnClickListener,
    RecentOrderAdapter.editEntry, SwipeRefreshLayout.OnRefreshListener {


    private var recycler_dashboard: RecyclerView? = null
    private var recentOrderAdapter: RecentOrderAdapter? = null

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView()
            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//  set status text dark

        setContentView(R.layout.activity_recent_order_view_all)


        recycler_dashboard = findViewById(R.id.recycl_store)
        img_back.setOnClickListener(this)

        recycler_dashboard!!.setHasFixedSize(true)
        recycler_dashboard!!.layoutManager =
            LinearLayoutManager(this, LinearLayout.VERTICAL, false)


        simpleSwipeRefreshLayout.setOnRefreshListener(this)
        simpleSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark)

        simpleSwipeRefreshLayout.post(Runnable {
            if (simpleSwipeRefreshLayout != null) {
                simpleSwipeRefreshLayout!!.isRefreshing = true
            }
            orderListApi()
        })

    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
        }
    }
    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.custom_edit_dialog)
        dialog.window.setBackgroundDrawableResource(android.R.color.white)

        //  dialog .setCancelable(false)
        val close_icon = dialog.findViewById(R.id.img_close) as AppCompatImageView

        close_icon.setOnClickListener {
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

    fun orderListApi() {

        progressBar!!.visibility = View.VISIBLE
        val orderlist = OrderList()
        orderlist.user_id = AppConstant.getPreferenceText(AppConstant.PREF_USER_ID)

        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.orderlist(orderlist)

        call.enqueue(object : Callback<OrderListResponse> {
            override fun onResponse(
                    call: Call<OrderListResponse>,
                    response: retrofit2.Response<OrderListResponse>
            ) {
                progressBar!!.visibility = View.GONE
                if (response.isSuccessful) {
                    simpleSwipeRefreshLayout.isRefreshing = false
                    orderListdata = arrayListOf()

                    orderListdata = response.body()!!.data!!


                    recentOrderAdapter = RecentOrderAdapter(this@RecentOrderViewAllActivity,
                            this@RecentOrderViewAllActivity,"1", orderListdata)
                    recycler_dashboard!!.adapter = recentOrderAdapter
                    recycler_dashboard!!.adapter = recentOrderAdapter

                    recentOrderAdapter!!.notifyDataSetChanged()

                } else {

                    simpleSwipeRefreshLayout.isRefreshing = false

                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@RecentOrderViewAllActivity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@RecentOrderViewAllActivity, e.message, Toast.LENGTH_LONG).show()
                    }

                }
            }


            override fun onFailure(call: Call<OrderListResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar!!.visibility = View.GONE
                simpleSwipeRefreshLayout.isRefreshing = false



            }
        })
    }

    override fun edt(pos: Int) {
        showDialog()
    }
    override fun onResume() {
        super.onResume()
        orderListApi()
    }

    override fun onRefresh() {
        orderListApi()
    }
}