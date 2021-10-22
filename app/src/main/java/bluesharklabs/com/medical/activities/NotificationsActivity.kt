package bluesharklabs.com.medical.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.adapters.NotificationAdapter
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.NotiFications
import bluesharklabs.com.medical.response.NotificationResponse
import bluesharklabs.com.medical.utils.AppConstant
import kotlinx.android.synthetic.main.activity_notifications.*
import kotlinx.android.synthetic.main.activity_slider_filter.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class NotificationsActivity : AppCompatActivity(), View.OnClickListener {


    private var notificationAdapter: NotificationAdapter? = null
    private var recycler: RecyclerView? = null

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR//  set status text dark

        setContentView(R.layout.activity_notifications)


        img_back.setOnClickListener(this)

        recycler = findViewById(R.id.recycl)

        recycler!!.setHasFixedSize(true)
        recycler!!.layoutManager =
            LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        getNotis()

    }


    fun getNotis() {

        progressBarnotis.visibility = View.VISIBLE
        val filter = NotiFications()
        filter.user_type = "1"
        filter.user_id = AppConstant.getPreferenceText(AppConstant.PREF_USER_ID)
        val apiService =
            AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.getNtifications(filter)

        call.enqueue(object : Callback<NotificationResponse> {
            override fun onResponse(
                call: Call<NotificationResponse>,
                response: retrofit2.Response<NotificationResponse>
            ) {
                progressBarnotis.visibility = View.GONE
                if (response.isSuccessful) {

                    val notiList = response.body()!!.data

                    notificationAdapter =
                        NotificationAdapter(this@NotificationsActivity, notiList!!)
                    recycler!!.adapter = notificationAdapter


                } else {

                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                            this@NotificationsActivity,
                            "" + jObjError.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@NotificationsActivity, e.message, Toast.LENGTH_LONG)
                            .show()
                    }

                }
            }


            override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                progressBarnotis.visibility = View.GONE


            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
        }
    }
}
