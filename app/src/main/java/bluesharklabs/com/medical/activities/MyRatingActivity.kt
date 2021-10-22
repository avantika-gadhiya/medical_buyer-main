package bluesharklabs.com.medical.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import android.view.View
import android.widget.Toast
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.BuyerRating
import bluesharklabs.com.medical.response.BuyerRatingResponse
import bluesharklabs.com.medical.utils.AppConstant
import kotlinx.android.synthetic.main.activity_my_rating.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class MyRatingActivity : AppCompatActivity(), View.OnClickListener {

    var offerid=""
    var storeid=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//  set status text dark

        setContentView(R.layout.activity_my_rating)


        if (intent != null) {
            if (intent.getStringExtra("storeId") != null) {
                storeid = intent.getStringExtra("storeId")
            }
            if (intent.getStringExtra("offerId") != null) {
                offerid = intent.getStringExtra("offerId")
            }
        }


        img_back.setOnClickListener(this)
        btn.setOnClickListener(this)


        getRateStoreApi()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.img_back->{
                finish()
            }
            R.id.btn->{
                startActivity(Intent(this@MyRatingActivity, MainActivity::class.java))
                finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

    fun getRateStoreApi() {
        progressBar.visibility = View.VISIBLE
        val buyerRating = BuyerRating()
        buyerRating.offer_id = offerid
        buyerRating.store_id = storeid
        buyerRating.user_id = AppConstant.getPreferenceText(AppConstant.PREF_USER_ID)

        val apiService =
            AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.buyerating(buyerRating)

        call.enqueue(object : Callback<BuyerRatingResponse> {
            override fun onResponse(
                call: Call<BuyerRatingResponse>,
                response: retrofit2.Response<BuyerRatingResponse>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {


                    txt_created_date.text=response.body()!!.data!!.createdDate
                    txt_review.text=response.body()!!.data!!.review
                    txt_store_name.text=response.body()!!.data!!.storeName
                    setEmoji(response.body()!!.data!!.rateExperience)


                } else {
                    noDataFound()

                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                            this@MyRatingActivity,
                            "" + jObjError.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@MyRatingActivity, e.message, Toast.LENGTH_LONG)
                            .show()
                    }

                }
            }


            override fun onFailure(call: Call<BuyerRatingResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar.visibility = View.GONE
              //  noDataFound()
            }
        })
    }

    
    fun setEmoji(rateExperience: String?) {
        if (rateExperience.equals("1")) {
            img.setImageDrawable(
                ContextCompat.getDrawable(
                    this@MyRatingActivity,
                    R.drawable.rate_bad_select
                )
            )
            txt_first.text = resources.getString(R.string.bad)
        }else if (rateExperience.equals("2")) {
            img.setImageDrawable(
                ContextCompat.getDrawable(
                    this@MyRatingActivity,
                    R.drawable.rate_meh_select
                )
            )
            txt_first.text = resources.getString(R.string.meh)
        }else if (rateExperience.equals("3")) {
            img.setImageDrawable(
                ContextCompat.getDrawable(
                    this@MyRatingActivity,
                    R.drawable.rate_good_select
                )
            )
            txt_first.text = resources.getString(R.string.nice)
        }else if (rateExperience.equals("4")) {
            img.setImageDrawable(
                ContextCompat.getDrawable(
                    this@MyRatingActivity,
                    R.drawable.rate_food_select
                )
            )
            txt_first.text = resources.getString(R.string.good)
        }else if (rateExperience.equals("5")) {
            img.setImageDrawable(
                ContextCompat.getDrawable(
                    this@MyRatingActivity,
                    R.drawable.rate_great_selected
                )
            )
            txt_first.text = resources.getString(R.string.great)
        }
    }
    fun noDataFound() {
        val builder = AlertDialog.Builder(this)

        // Set the alert dialog title
        //builder.setTitle("App background color")

        // Display a message on alert dialog
        builder.setMessage("You did not get any review from store.")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("Ok") { dialog, which ->
            // Do something when u ser press the positive button

            finish()
        }

        // Display a negative button on alert dialog


        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        // Display the alert dialog on app interface
        dialog.show()

    }
}
