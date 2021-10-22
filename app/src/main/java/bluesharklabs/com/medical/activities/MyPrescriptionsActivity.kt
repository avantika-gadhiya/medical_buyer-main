package bluesharklabs.com.medical.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.adapters.MyOrdersAdapter
import bluesharklabs.com.medical.adapters.PrescriptionAdapter
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.OrderList
import bluesharklabs.com.medical.response.BuyerDetailResponse
import bluesharklabs.com.medical.response.PrescriptionResponse
import bluesharklabs.com.medical.utils.AppConstant
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_my_prescriptions.*
import kotlinx.android.synthetic.main.activity_my_prescriptions.img_back
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.dashboard_fragment.*
import kotlinx.android.synthetic.main.dashboard_fragment.progressBar
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class MyPrescriptionsActivity : AppCompatActivity(), View.OnClickListener {


    private var prescriptionAdapter: PrescriptionAdapter? = null
    var foodsList:ArrayList<PrescriptionResponse.Datum> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView()
            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//  set status text dark

        setContentView(R.layout.activity_my_prescriptions)

        img_back.setOnClickListener(this)

        PrescriptionDetail()


    }
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.img_back -> {
                finish()
            }
        }
    }

    fun PrescriptionDetail() {

        progressBar.visibility = View.VISIBLE
        val orderlist = OrderList()
        orderlist.user_id = AppConstant.getPreferenceText(AppConstant.PREF_USER_ID)

        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.prescriptionDetail(orderlist)

        call.enqueue(object : Callback<PrescriptionResponse> {
            override fun onResponse(
                    call: Call<PrescriptionResponse>,
                    response: retrofit2.Response<PrescriptionResponse>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {


                    foodsList= response.body()?.data!!

                    prescriptionAdapter = PrescriptionAdapter(this@MyPrescriptionsActivity, foodsList)
                    gvFoods.adapter = prescriptionAdapter

                } else {


                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@MyPrescriptionsActivity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@MyPrescriptionsActivity, e.message, Toast.LENGTH_LONG).show()
                    }

                }
            }


            override fun onFailure(call: Call<PrescriptionResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar.visibility = View.GONE



            }
        })
    }


}
