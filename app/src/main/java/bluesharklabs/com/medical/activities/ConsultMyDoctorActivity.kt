package bluesharklabs.com.medical.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.adapters.ConsultMyDoctorAdapter
import bluesharklabs.com.medical.interfaces.ConsultDoctorInterFace
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.response.ConsultDoctorResponse
import bluesharklabs.com.medical.utils.AppConstant
import kotlinx.android.synthetic.main.activity_consult_my_doctor.*
import kotlinx.android.synthetic.main.activity_consult_my_doctor.img_back
import kotlinx.android.synthetic.main.activity_slider_send_feedback.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback


class ConsultMyDoctorActivity : AppCompatActivity(), View.OnClickListener, ConsultDoctorInterFace {

    var doctorData: ArrayList<ConsultDoctorResponse.Datum> = arrayListOf()
    private val REQUEST_CODE_ASK_PERMISSIONS_CALL_PHONE = 100

    private var consultMyDoctorAdapter: ConsultMyDoctorAdapter? = null
    private var recyclerView: RecyclerView? = null


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR//  set status text dark

        setContentView(R.layout.activity_consult_my_doctor)


        recyclerView = findViewById(R.id.recycl)

        img_back.setOnClickListener(this)

        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager =
            LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        getAllDoctors()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
        }
    }


    // Need to change this after actual api made by backend devs.
    fun getAllDoctors() {

        progressBardoctor.visibility = View.VISIBLE
        val apiService =
            AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.getDoctors()

        call.enqueue(object : Callback<ConsultDoctorResponse> {
            override fun onResponse(
                call: Call<ConsultDoctorResponse>,
                response: retrofit2.Response<ConsultDoctorResponse>
            ) {
                progressBardoctor.visibility = View.GONE
                if (response.isSuccessful) {
                    doctorData.clear()
                    doctorData = response.body()!!.data!!

                    consultMyDoctorAdapter =
                        ConsultMyDoctorAdapter(
                            this@ConsultMyDoctorActivity,
                            doctorData,
                            this@ConsultMyDoctorActivity
                        )
                    recyclerView!!.adapter = consultMyDoctorAdapter


                } else {

                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                            this@ConsultMyDoctorActivity,
                            "" + jObjError.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()

                    } catch (e: Exception) {
                        Toast.makeText(this@ConsultMyDoctorActivity, e.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }


            override fun onFailure(call: Call<ConsultDoctorResponse>, t: Throwable) {
                progressBardoctor.visibility = View.GONE
            }
        })
    }

    override fun onClickWA(from: Int, number: String, name: String) {


        if (from == 1) {
            try {
                val packageManager: PackageManager = packageManager
                val i = Intent(Intent.ACTION_VIEW)
                val url =
                    "https://api.whatsapp.com/send?phone=$number"
                i.setPackage("com.whatsapp")
                i.data = Uri.parse(url)
                if (i.resolveActivity(packageManager) != null) {
                    startActivity(i)
                } else {
                    Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                        .show()
                    //download for example after dialog
                    val uri = Uri.parse("market://details?id=com.whatsapp")
                    val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                }
            } catch (e: java.lang.Exception) {
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT)
                    .show()

            }
        } else if (from == 2) {

            if (Build.VERSION.SDK_INT >= 23) {
                val hasPermission = ContextCompat.checkSelfPermission(
                    this@ConsultMyDoctorActivity,
                    Manifest.permission.CALL_PHONE
                )
                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this@ConsultMyDoctorActivity,
                            Manifest.permission.CALL_PHONE
                        )
                    ) {
                        // Display UI and wait for user interaction
                        getPermissionInfoDialog(
                            this@ConsultMyDoctorActivity.getString(R.string.call_phone_permission),
                            this@ConsultMyDoctorActivity
                        )!!.show()
                    } else {
                        ActivityCompat.requestPermissions(
                            this@ConsultMyDoctorActivity, arrayOf(Manifest.permission.CALL_PHONE),
                            REQUEST_CODE_ASK_PERMISSIONS_CALL_PHONE
                        )
                    }
                    return
                }
            }

            val builder =
                AlertDialog.Builder(this@ConsultMyDoctorActivity, R.style.AppCompatAlertDialogStyle)
            builder.setTitle(getString(R.string.confirm_call_doctor, name))
            builder.setIcon(R.mipmap.ic_launcher)

            builder.setPositiveButton(
                R.string.OK,
                DialogInterface.OnClickListener { dialog, which ->
                    val callIntent = Intent(Intent.ACTION_CALL)
                    callIntent.data = Uri.parse("tel: " + number)
                    startActivity(callIntent)
                })
            builder.show()

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


}