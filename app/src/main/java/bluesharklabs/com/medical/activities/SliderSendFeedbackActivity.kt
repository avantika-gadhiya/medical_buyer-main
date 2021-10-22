package bluesharklabs.com.medical.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.Feedback
import bluesharklabs.com.medical.response.CommonResponse
import bluesharklabs.com.medical.utils.AppConstant
import kotlinx.android.synthetic.main.activity_slider_send_feedback.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class SliderSendFeedbackActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView()
            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//  set status text dark

        setContentView(R.layout.activity_slider_send_feedback)


        btn.setOnClickListener(this)
        img_back.setOnClickListener(this)

       // edt_submit
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {

                finish()
            }
            R.id.btn -> {

                val edtSubmit = edt_submit.text.toString().trim()
                if (edtSubmit.isEmpty()){
                    Toast.makeText(
                        this@SliderSendFeedbackActivity,
                        R.string.message_is_empty, Toast.LENGTH_SHORT
                    ).show()
                }else{
                    sendFeedBack()
//                    finish()
                }
            }
        }

    }


    fun sendFeedBack() {

        progressBarfeedback.visibility = View.VISIBLE
        val feedback = Feedback()
        feedback.user_type = "2"
        feedback.user_id = AppConstant.getPreferenceText(AppConstant.PREF_USER_ID)
        feedback.feedback = edt_submit.text.toString()
        val apiService =
            AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.sendFeedBack(feedback)

        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: retrofit2.Response<CommonResponse>
            ) {
                progressBarfeedback.visibility = View.GONE
                if (response.isSuccessful) {
                    edt_submit.setText("")
                    Toast.makeText(this@SliderSendFeedbackActivity,"Your feedback submitted successfully!",Toast.LENGTH_LONG).show()
                    finish()
                } else {

                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                            this@SliderSendFeedbackActivity,
                            "" + jObjError.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@SliderSendFeedbackActivity, e.message, Toast.LENGTH_LONG)
                            .show()
                    }

                }
            }


            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                progressBarfeedback.visibility = View.GONE
            }
        })
    }
}
