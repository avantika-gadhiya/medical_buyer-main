package bluesharklabs.com.medical.activities

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.DateTimePatternGenerator.PatternInfo.OK
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.MakeStoreReview
import bluesharklabs.com.medical.response.MakeStoreReviewResponse
import bluesharklabs.com.medical.utils.AppConstant
import kotlinx.android.synthetic.main.activity_rate_store.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class RateStoreActivity : AppCompatActivity(), View.OnClickListener {
    var offerid=""
    var orderId=""
    var orderType=""
    var storeid=""
    var rateStar=""
    private val REQUEST_CODE_ASK_PERMISSIONS_CALL_PHONE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView()
            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//  set status text dark

        setContentView(R.layout.activity_rate_store)

        if (intent != null) {
            if (intent.getStringExtra("orderType") != null) {
                orderType = intent.getStringExtra("orderType")
            }
            if (intent.getStringExtra("orderId") != null) {
                orderId = intent.getStringExtra("orderId")
            }
            if (intent.getStringExtra("storeId") != null) {
                storeid = intent.getStringExtra("storeId")
            }
            if (intent.getStringExtra("offerId") != null) {
                offerid = intent.getStringExtra("offerId")
            }
        }

        textView120.text=AppConstant.getPreferenceText(AppConstant.PREF_STORE_NAME)

        img_bad.setOnClickListener(this)
        img_meh.setOnClickListener(this)
        img_nice.setOnClickListener(this)
        img_good.setOnClickListener(this)
        img_great.setOnClickListener(this)
        btn_submit_review.setOnClickListener(this)
        txt_view_ordr.setOnClickListener(this)
        txt_call.setOnClickListener(this)
        img_back.setOnClickListener(this)
    }

    private fun setBadEmojiLayout() {
        img_bad.setImageDrawable(
            ContextCompat.getDrawable(
                this@RateStoreActivity,
                R.drawable.rate_bad_select
            )
        )
        img_meh.setImageDrawable(
            ContextCompat.getDrawable(
                this@RateStoreActivity,
                R.drawable.rate_meh_unselect
            )
        )
        img_nice.setImageDrawable(
            ContextCompat.getDrawable(
                this@RateStoreActivity,
                R.drawable.rate_good_unselect
            )
        )
        img_good.setImageDrawable(
            ContextCompat.getDrawable(
                this@RateStoreActivity,
                R.drawable.rate_food_unselect
            )
        )
        img_great.setImageDrawable(
            ContextCompat.getDrawable(
                this@RateStoreActivity,
                R.drawable.rate_great_unselected
            )
        )

        txt_bad.setTextColor(ContextCompat.getColor(this, R.color.black_txt))
        txt_meh.setTextColor(ContextCompat.getColor(this, R.color.grey_txt))
        txt_nice.setTextColor(ContextCompat.getColor(this, R.color.grey_txt))
        txt_good.setTextColor(ContextCompat.getColor(this, R.color.grey_txt))
        txt_great.setTextColor(ContextCompat.getColor(this, R.color.grey_txt))
    }

    private fun setMehEmojiLayout() {
        img_bad.setImageDrawable(
            ContextCompat.getDrawable(
                this@RateStoreActivity,
                R.drawable.rate_bad_unselect
            )
        )
        img_meh.setImageDrawable(
            ContextCompat.getDrawable(
                this@RateStoreActivity,
                R.drawable.rate_meh_select
            )
        )
        img_nice.setImageDrawable(
            ContextCompat.getDrawable(
                this@RateStoreActivity,
                R.drawable.rate_good_unselect
            )
        )
        img_good.setImageDrawable(
            ContextCompat.getDrawable(
                this@RateStoreActivity,
                R.drawable.rate_food_unselect
            )
        )
        img_great.setImageDrawable(
            ContextCompat.getDrawable(
                this@RateStoreActivity,
                R.drawable.rate_great_unselected
            )
        )

        txt_bad.setTextColor(ContextCompat.getColor(this, R.color.grey_txt))
        txt_meh.setTextColor(ContextCompat.getColor(this, R.color.black_txt))
        txt_nice.setTextColor(ContextCompat.getColor(this, R.color.grey_txt))
        txt_good.setTextColor(ContextCompat.getColor(this, R.color.grey_txt))
        txt_great.setTextColor(ContextCompat.getColor(this, R.color.grey_txt))
    }

    private fun setNiceEmojiLayout() {
        img_bad.setImageDrawable(
            ContextCompat.getDrawable(
                this@RateStoreActivity,
                R.drawable.rate_bad_unselect
            )
        )
        img_meh.setImageDrawable(
            ContextCompat.getDrawable(
                this@RateStoreActivity,
                R.drawable.rate_meh_unselect
            )
        )
        img_nice.setImageDrawable(
            ContextCompat.getDrawable(
                this@RateStoreActivity,
                R.drawable.rate_good_select
            )
        )
        img_good.setImageDrawable(
            ContextCompat.getDrawable(
                this@RateStoreActivity,
                R.drawable.rate_food_unselect
            )
        )
        img_great.setImageDrawable(
            ContextCompat.getDrawable(
                this@RateStoreActivity,
                R.drawable.rate_great_unselected
            )
        )

        txt_bad.setTextColor(ContextCompat.getColor(this, R.color.grey_txt))
        txt_meh.setTextColor(ContextCompat.getColor(this, R.color.grey_txt))
        txt_nice.setTextColor(ContextCompat.getColor(this, R.color.black_txt))
        txt_good.setTextColor(ContextCompat.getColor(this, R.color.grey_txt))
        txt_great.setTextColor(ContextCompat.getColor(this, R.color.grey_txt))
    }

    private fun setGoodEmojiLayout() {
        img_bad.setImageDrawable(
            ContextCompat.getDrawable(
                this@RateStoreActivity,
                R.drawable.rate_bad_unselect
            )
        )
        img_meh.setImageDrawable(
            ContextCompat.getDrawable(
                this@RateStoreActivity,
                R.drawable.rate_meh_unselect
            )
        )
        img_nice.setImageDrawable(
            ContextCompat.getDrawable(
                this@RateStoreActivity,
                R.drawable.rate_good_unselect
            )
        )
        img_good.setImageDrawable(
            ContextCompat.getDrawable(
                this@RateStoreActivity,
                R.drawable.rate_food_select
            )
        )
        img_great.setImageDrawable(
            ContextCompat.getDrawable(
                this@RateStoreActivity,
                R.drawable.rate_great_unselected
        )
        )

        txt_bad.setTextColor(ContextCompat.getColor(this, R.color.grey_txt))
        txt_meh.setTextColor(ContextCompat.getColor(this, R.color.grey_txt))
        txt_nice.setTextColor(ContextCompat.getColor(this, R.color.grey_txt))
        txt_good.setTextColor(ContextCompat.getColor(this, R.color.black_txt))
        txt_great.setTextColor(ContextCompat.getColor(this, R.color.grey_txt))
    }

    private fun setGreatEmojiLayout() {
        img_bad.setImageDrawable(
            ContextCompat.getDrawable(
                this@RateStoreActivity,
                R.drawable.rate_bad_unselect
            )
        )
        img_meh.setImageDrawable(
            ContextCompat.getDrawable(
                this@RateStoreActivity,
                R.drawable.rate_meh_unselect
            )
        )
        img_nice.setImageDrawable(
            ContextCompat.getDrawable(
                this@RateStoreActivity,
                R.drawable.rate_good_unselect
            )
        )
        img_good.setImageDrawable(
            ContextCompat.getDrawable(
                this@RateStoreActivity,
                R.drawable.rate_food_unselect
            )
        )
        img_great.setImageDrawable(
            ContextCompat.getDrawable(
                this@RateStoreActivity,
                R.drawable.rate_great_selected
            )
        )

        txt_bad.setTextColor(ContextCompat.getColor(this, R.color.grey_txt))
        txt_meh.setTextColor(ContextCompat.getColor(this, R.color.grey_txt))
        txt_nice.setTextColor(ContextCompat.getColor(this, R.color.grey_txt))
        txt_good.setTextColor(ContextCompat.getColor(this, R.color.grey_txt))
        txt_great.setTextColor(ContextCompat.getColor(this, R.color.black_txt))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.img_bad -> {
                rateStar= "1"
                setBadEmojiLayout()
            }
            R.id.img_meh -> {
                rateStar= "2"
                setMehEmojiLayout()
            }
            R.id.img_nice -> {
                rateStar= "3"
                setNiceEmojiLayout()
            }
            R.id.img_good -> {
                rateStar= "4"
                setGoodEmojiLayout()
            }
            R.id.img_great -> {
                rateStar= "5"
                setGreatEmojiLayout()
            }
            R.id.btn_submit_review -> {

                val review = edt_box_review.text.toString().trim()

                if (rateStar.equals("")){
                    Toast.makeText(this,"Rate Store", Toast.LENGTH_LONG)
                        .show()
                }/*else if (review.equals("")){
                    Toast.makeText(this,"Write Review", Toast.LENGTH_LONG)
                        .show()
                }*/ else{
                    rateStoreApi(review)
                }


            }
            R.id.txt_view_ordr -> {
                if (orderType.equals("0")) {
                startActivity(
                    Intent(this@RateStoreActivity, FinalOrderActivity::class.java)
                        .putExtra("vieworder", "1")
                        .putExtra("orderid", orderId)
                            .putExtra("offerId", offerid)
                )
                }else{
                    startActivity(
                            Intent(this@RateStoreActivity, CustomFinalOrderActivity::class.java)
                                    .putExtra("vieworder", "1")
                                    .putExtra("orderid", orderId)
                                    .putExtra("offerId", offerid)
                    )
                }
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.txt_call -> {
                if (Build.VERSION.SDK_INT >= 23) {
                    val hasPermission = ContextCompat.checkSelfPermission(this@RateStoreActivity, Manifest.permission.CALL_PHONE)
                    if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this@RateStoreActivity,
                                        Manifest.permission.CALL_PHONE)) {
                            // Display UI and wait for user interaction
                            getPermissionInfoDialog(this@RateStoreActivity.getString(R.string.call_phone_permission), this@RateStoreActivity)?.show()
                        } else {
                            ActivityCompat.requestPermissions(this@RateStoreActivity, arrayOf(Manifest.permission.CALL_PHONE),
                                    REQUEST_CODE_ASK_PERMISSIONS_CALL_PHONE)
                        }
                        return
                    }
                }

                val builder = AlertDialog.Builder(this@RateStoreActivity, R.style.AppCompatAlertDialogStyle)
                builder.setTitle(R.string.confirm_call)
                builder.setIcon(R.mipmap.ic_launcher)

                builder.setPositiveButton(R.string.OK, DialogInterface.OnClickListener { dialog, which ->
                    val callIntent = Intent(Intent.ACTION_CALL)
                    callIntent.data = Uri.parse("tel: "+AppConstant.getPreferenceText(AppConstant.PREF_STORE_PHONE))
                    startActivity(callIntent)
                    //Utils.psLog("OK clicked.")
                })
                builder.show()
            }
        }
    }
    fun getPermissionInfoDialog(message: String?, context: Activity?): android.app.AlertDialog.Builder? {
        val alertDialog = android.app.AlertDialog.Builder(context)
        alertDialog.setTitle(getString(R.string.app_name)).setMessage(message)
        alertDialog.setPositiveButton("Ok") { dialog, which ->
            dialog.dismiss()
            ActivityCompat.requestPermissions(context!!, arrayOf(Manifest.permission.CALL_PHONE),
                    REQUEST_CODE_ASK_PERMISSIONS_CALL_PHONE)
        }
        return alertDialog
    }
    fun rateStoreApi(review: String) {
        progressBar.visibility = View.VISIBLE
        val makeStoreReview = MakeStoreReview()
        makeStoreReview.offer_id = offerid
        makeStoreReview.store_id = storeid
        makeStoreReview.user_id = AppConstant.getPreferenceText(AppConstant.PREF_USER_ID)
        makeStoreReview.rate_experience = rateStar
        makeStoreReview.review =review

        val apiService =
            AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.makestorereview(makeStoreReview)

        call.enqueue(object : Callback<MakeStoreReviewResponse> {
            override fun onResponse(
                call: Call<MakeStoreReviewResponse>,
                response: retrofit2.Response<MakeStoreReviewResponse>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {/*
                    startActivity(Intent(this@RateStoreActivity, MyRatingActivity::class.java)
                        .putExtra("offerId", offerid)
                        .putExtra("storeId", storeid))
                    //finish()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)*/
                    Toast.makeText(
                        this@RateStoreActivity,
                        "Review Submitted" ,
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@RateStoreActivity, MainActivity::class.java))
                    finish()

                    //btn_submit_review.isEnabled = false
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                            this@RateStoreActivity,
                            "" + jObjError.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@RateStoreActivity, e.message, Toast.LENGTH_LONG)
                            .show()
                    }

                }
            }


            override fun onFailure(call: Call<MakeStoreReviewResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar.visibility = View.GONE
            }
        })
    }
}
