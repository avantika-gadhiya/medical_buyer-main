package bluesharklabs.com.medical.loginactivities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.AddToken
import bluesharklabs.com.medical.model.Login
import bluesharklabs.com.medical.response.CommonResponse
import bluesharklabs.com.medical.response.LoginResponse
import bluesharklabs.com.medical.utils.AppConstant
import bluesharklabs.com.medical.utils.AppConstant.CONSTANT_NUMBER
import bluesharklabs.com.medical.utils.AppConstant.isEmpty
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private var number = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }//  set status text dark
        setContentView(R.layout.activity_login)

        btn_otp.setOnClickListener(this)
        text_register.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_otp -> {

                AppConstant.hideSoftKeyBoard(this@LoginActivity, v)


                number = text_num.text.toString().trim()

                when {
                    isEmpty(number) -> {
                        Toast.makeText(
                                this,
                                getString(R.string.enter_mobilenumber),
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                    number.length < 10 -> {
                        Toast.makeText(
                                this,
                                getString(R.string.enter_valid_number),
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        loginApi()
                    }
                }
            }
            R.id.text_register -> {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }


    fun loginApi() {

        progressBar.visibility = View.VISIBLE
        val login = Login()
        login.phone = number

        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.login(login)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                    call: Call<LoginResponse>,
                    response: retrofit2.Response<LoginResponse>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {

                    AppConstant.setPreferenceText(
                            AppConstant.PREF_USER_PROFILE_IMAGE,
                            response.body()?.data!!.profilePic!!
                    )
                    AppConstant.setPreferenceText(
                            AppConstant.PREF_USER_ID,
                            response.body()?.data!!.userId!!
                    )

                    AppConstant.setPreferenceText(
                            AppConstant.PREF_USER_NAME,
                            response.body()?.data!!.fullname!!
                    )
                    AppConstant.setPreferenceText(AppConstant.PREF_USER_PHONE, response.body()?.data!!.phone!!)
                    AppConstant.setPreferenceText(AppConstant.PREF_USER_ZIPCODE, response.body()?.data!!.zipcode!!)
                    AppConstant.setPreferenceText(AppConstant.PREF_USER_LANDMARK, response.body()?.data!!.landmark!!)
                    AppConstant.setPreferenceText(AppConstant.PREF_USER_ADDRESSLINE1, response.body()?.data!!.addressLine1!!)
                    AppConstant.setPreferenceText(AppConstant.PREF_USER_ADDRESSLINE2, response.body()?.data!!.addressLine2!!)
                    AppConstant.setPreferenceText(AppConstant.PREF_USER_CITY, response.body()?.data!!.city!!)


                    startActivity(
                        Intent(this@LoginActivity, VerifyActivity::class.java).putExtra(
                            CONSTANT_NUMBER,
                            number
                        )
                    )
                    finish()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@LoginActivity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_LONG).show()
                    }

                }
            }


            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar.visibility = View.GONE
            }
        })
    }



}