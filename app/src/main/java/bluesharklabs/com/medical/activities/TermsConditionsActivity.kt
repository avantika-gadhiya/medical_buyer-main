package bluesharklabs.com.medical.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.response.GetColorCodeResponse
import bluesharklabs.com.medical.response.GetTermsConditionResponse
import bluesharklabs.com.medical.response.TandCResponse
import bluesharklabs.com.medical.utils.AppConstant
import kotlinx.android.synthetic.main.activity_terms_conditions.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TermsConditionsActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView()
            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//  set status text dark
        setContentView(R.layout.activity_terms_conditions)
        img_back.setOnClickListener(this)
//        getColorCodeListApi()
        getTandC()

    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
               finish()
            }
        }
    }

    private fun getColorCodeListApi() {
        progressBar.visibility = View.VISIBLE
        val apiService = AppConstant.getClient()!!.create(RetrofitInterface::class.java)

        val call = apiService.gettermscoddition()

        call.enqueue(object : Callback<GetTermsConditionResponse> {
            override fun onResponse(
                call: Call<GetTermsConditionResponse>,
                response: Response<GetTermsConditionResponse>
            ) {

                progressBar.visibility = View.GONE
                if (response.isSuccessful()) {
                  //  webview.loadUrl("https://mindorks.com/")
                   webview.loadUrl(response.body()!!.data!!.url)
                }
            }

            override fun onFailure(call: Call<GetTermsConditionResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
            }
        })
    }



    fun getTandC() {

        progressBar.visibility = View.VISIBLE
        val apiService =
            AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.getTandC()

        call.enqueue(object : Callback<TandCResponse> {
            override fun onResponse(
                call: Call<TandCResponse>,
                response: retrofit2.Response<TandCResponse>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {

                    val storeTandC = response.body()!!.getData()?.get(0)?.buyerTermsConditions
                    txt_TandC.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(storeTandC, Html.FROM_HTML_MODE_COMPACT)
                    } else {
                        Html.fromHtml(storeTandC)
                    }

                } else {

                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                            this@TermsConditionsActivity,
                            "" + jObjError.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@TermsConditionsActivity, e.message, Toast.LENGTH_LONG)
                            .show()
                    }

                }
            }

            override fun onFailure(call: Call<TandCResponse>, t: Throwable) {
                progressBar.visibility = View.GONE


            }
        })
    }
}
