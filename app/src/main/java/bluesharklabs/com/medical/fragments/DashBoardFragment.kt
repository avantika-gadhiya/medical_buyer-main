package bluesharklabs.com.medical.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.appcompat.widget.*
import android.view.*
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.activities.AttachPrescriptionActivity
import bluesharklabs.com.medical.activities.CustomOrderActivity
import bluesharklabs.com.medical.activities.RecentOrderViewAllActivity
import bluesharklabs.com.medical.adapters.RecentOrderAdapter
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.OrderList
import bluesharklabs.com.medical.response.OrderListResponse
import bluesharklabs.com.medical.utils.AppConstant
import kotlinx.android.synthetic.main.dashboard_fragment.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback


class DashBoardFragment : Fragment(), View.OnClickListener, RecentOrderAdapter.editEntry, SwipeRefreshLayout.OnRefreshListener {


    private var recycler_dashboard: RecyclerView? = null
    private var rela_sortby: RelativeLayout? = null
    private var card_by_prescri: CardView? = null
    private var cardCstmOrdr: CardView? = null
    private var txt_recent_ordr_viewall: AppCompatTextView? = null
    private var progressbar: ProgressBar? = null
    var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    private var recentOrderAdapter: RecentOrderAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
// Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.dashboard_fragment, container, false)

        recycler_dashboard = view.findViewById(R.id.recycl_recent_order)
        mSwipeRefreshLayout = view.findViewById(R.id.simpleSwipeRefreshLayout)
        card_by_prescri = view.findViewById(R.id.card_byprscri) as CardView?
        cardCstmOrdr = view.findViewById(R.id.card_custm_ordr) as CardView?
        rela_sortby = view.findViewById(R.id.rel_sortby) as RelativeLayout?
        progressbar = view.findViewById(R.id.progressBar) as ProgressBar?
        txt_recent_ordr_viewall =
            view.findViewById(R.id.txt_recent_ordr_viewall) as AppCompatTextView?

        mSwipeRefreshLayout?.setOnRefreshListener(this)
        mSwipeRefreshLayout?.setColorSchemeResources(R.color.colorAccent,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark)

        mSwipeRefreshLayout?.post(Runnable {
            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout!!.isRefreshing = true
            }
            orderListApi()
        })
        rela_sortby?.setOnClickListener(this)
        card_by_prescri?.setOnClickListener(this)
        cardCstmOrdr?.setOnClickListener(this)
        txt_recent_ordr_viewall?.setOnClickListener(this)

        recycler_dashboard!!.setHasFixedSize(true)
        recycler_dashboard!!.layoutManager =
            LinearLayoutManager(activity!!, LinearLayout.VERTICAL, false)

        orderListApi()

        return view
    }

    companion object {

        var orderListdata: List<OrderListResponse.Datum> = arrayListOf()


        @JvmStatic
        fun newInstance() =
            DashBoardFragment().apply {
                arguments = Bundle().apply {

                }
            }

    }

    private fun showDialog() {
        val dialog = Dialog(activity)
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

    override fun edt(pos: Int) {
        showDialog()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.card_byprscri -> {

                startActivity(
                    Intent(context, AttachPrescriptionActivity::class.java)
                )
                //finish()
            }
            R.id.card_custm_ordr -> {

                startActivity(
                    Intent(context, CustomOrderActivity::class.java)
                )
                //finish()
            }
            R.id.rel_sortby -> {
                /*recycl_recent_order.visibility = View.GONE
                nested_nothing_here.visibility = View.VISIBLE
                rel_sortby.visibility = View.GONE*/

                /* startActivity(
                     Intent(context, AttachPrescriptionActivity::class.java)
                 )*/
                //finish()
            }
            R.id.txt_recent_ordr_viewall -> {
                startActivity(
                    Intent(context, RecentOrderViewAllActivity::class.java)
                )
            }
        }
    }

    fun orderListApi() {

        progressbar!!.visibility = View.VISIBLE
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
                progressbar!!.visibility = View.GONE
                if (response.isSuccessful) {
                    mSwipeRefreshLayout!!.isRefreshing = false
                    orderListdata = arrayListOf()

                    orderListdata = response.body()?.data!!


                    if (recycl_recent_order != null) {
                        recycl_recent_order.visibility = View.VISIBLE
                    }

                    if (nested_nothing_here != null) {
                        nested_nothing_here.visibility = View.GONE
                    }
                    if (rel_sortby != null) {
                        rel_sortby.visibility = View.VISIBLE

                    }
                    recentOrderAdapter =
                        RecentOrderAdapter(activity!!, this@DashBoardFragment, "0", orderListdata)
                    recycler_dashboard!!.adapter = recentOrderAdapter

                    recentOrderAdapter!!.notifyDataSetChanged()

                } else {

                    recycl_recent_order.visibility = View.GONE
                    nested_nothing_here.visibility = View.VISIBLE
                    rel_sortby.visibility = View.GONE

                    mSwipeRefreshLayout!!.isRefreshing = false

                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                            activity,
                            "" + jObjError.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
                    }

                }
            }


            override fun onFailure(call: Call<OrderListResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressbar!!.visibility = View.GONE

                mSwipeRefreshLayout!!.isRefreshing = false

                recycl_recent_order.visibility = View.GONE
                nested_nothing_here.visibility = View.VISIBLE
                rel_sortby.visibility = View.GONE

            }
        })
    }

    override fun onRefresh() {
        orderListApi()
    }
    override fun onResume() {
        super.onResume()
        orderListApi()
    }
}
