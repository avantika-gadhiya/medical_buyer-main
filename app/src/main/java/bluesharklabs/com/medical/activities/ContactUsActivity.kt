package bluesharklabs.com.medical.activities

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.response.ContactUSResponse
import bluesharklabs.com.medical.utils.AppConstant
import kotlinx.android.synthetic.main.activity_contact_us.*
import kotlinx.android.synthetic.main.activity_contact_us.img_back
import kotlinx.android.synthetic.main.activity_terms_conditions.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class ContactUsActivity : AppCompatActivity(), View.OnClickListener {

    private val REQUEST_CODE_ASK_PERMISSIONS_CALL_PHONE = 100
    private var myPhone = ""
    private var myEmail = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR//  set status text dark

        setContentView(R.layout.activity_contact_us)

        getContactUS()
        img_back.setOnClickListener(this)
        con.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()

            }
            R.id.con -> {

                if (Build.VERSION.SDK_INT >= 23) {
                    val hasPermission = ContextCompat.checkSelfPermission(
                        this@ContactUsActivity,
                        Manifest.permission.CALL_PHONE
                    )
                    if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                this@ContactUsActivity,
                                Manifest.permission.CALL_PHONE
                            )
                        ) {
                            // Display UI and wait for user interaction
                            getPermissionInfoDialog(
                                this@ContactUsActivity.getString(R.string.call_phone_permission),
                                this@ContactUsActivity
                            )?.show()
                        } else {
                            ActivityCompat.requestPermissions(
                                this@ContactUsActivity, arrayOf(Manifest.permission.CALL_PHONE),
                                REQUEST_CODE_ASK_PERMISSIONS_CALL_PHONE
                            )
                        }
                        return
                    }
                }

                val builder =
                    AlertDialog.Builder(this@ContactUsActivity, R.style.AppCompatAlertDialogStyle)
                builder.setTitle(R.string.confirm_call)
                builder.setIcon(R.mipmap.ic_launcher)

                builder.setPositiveButton(
                    R.string.OK,
                    DialogInterface.OnClickListener { dialog, which ->
                        val callIntent = Intent(Intent.ACTION_CALL)
                        callIntent.data = Uri.parse("tel: " + myPhone)
                        startActivity(callIntent)
                        //Utils.psLog("OK clicked.")
                    })
                builder.show()
            }
        }
    }


    fun getPermissionInfoDialog(
        message: String?,
        context: Activity?
    ): android.app.AlertDialog.Builder? {
        val alertDialog = android.app.AlertDialog.Builder(context)
        alertDialog.setTitle(getString(R.string.app_name)).setMessage(message)
        alertDialog.setPositiveButton("Ok") { dialog, which ->
            dialog.dismiss()
            ActivityCompat.requestPermissions(
                context!!, arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_CODE_ASK_PERMISSIONS_CALL_PHONE
            )
        }
        return alertDialog
    }

    fun getContactUS() {

        progressBar_contact.visibility = View.VISIBLE
        val apiService =
            AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.getContactUS()

        call.enqueue(object : Callback<ContactUSResponse> {
            override fun onResponse(
                call: Call<ContactUSResponse>,
                response: retrofit2.Response<ContactUSResponse>
            ) {
                progressBar_contact.visibility = View.GONE
                if (response.isSuccessful) {


                    myPhone = response.body()!!.data?.get(0)?.buyerPhone!!
                    myEmail = response.body()!!.data?.get(0)?.buyerEmail!!
                    textView139.text = myPhone
                    txt1.text = myEmail

                } else {

                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                            this@ContactUsActivity,
                            "" + jObjError.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@ContactUsActivity, e.message, Toast.LENGTH_LONG)
                            .show()
                    }

                }
            }

            override fun onFailure(call: Call<ContactUSResponse>, t: Throwable) {
                progressBar_contact.visibility = View.GONE
            }
        })
    }
}
