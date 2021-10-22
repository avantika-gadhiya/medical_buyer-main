package bluesharklabs.com.medical.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.area
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.blockNum
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.buildName
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.buyingPrefrnc
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.buyingPrefrncTxt
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.city
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.delDate
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.delDateFormat
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.delType
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.del_lat
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.del_long
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.delprerncetext
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.drugPrefrnc
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.drugPrefrncTxt
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.endTime
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.endTimeFormat
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.landMark
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.locaTion
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.order_lat
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.order_long
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.photoId
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.startTime
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.startTimeFormat
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.store_lat
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.store_long
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.streeT
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.toDateFormat
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.toDeliveryDate
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.todate_fromdate
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.turnOnLocation
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.zipCode
import kotlinx.android.synthetic.main.activity_order_successful.*

class OrderSuccessfulActivity : AppCompatActivity() ,View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView()
            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//  set status text dark

        setContentView(R.layout.activity_order_successful)


        if (intent!=null){
            if (intent.getStringExtra("fromplatform")!=null){
                txt_order_text.setText(R.string.ordr_success_txt_one)
            }
            if (intent.getStringExtra("fromstore")!=null){
                txt_order_text.setText(R.string.ordr_success_txt)
            }
        }


        btn_back_t_dasbrd.setOnClickListener (this)
        img_back.setOnClickListener (this)


        drugPrefrnc = ""
        drugPrefrncTxt = ""
        buyingPrefrnc = ""
        buyingPrefrncTxt = ""
        delprerncetext = ""
        delType = ""
        order_lat = ""
        order_long = ""
        store_lat = ""
        store_long = ""
        del_lat = ""
        del_long = ""
        turnOnLocation = "0"
        photoId = ""
        startTime = ""
        endTime = ""
        delDate = ""
        toDeliveryDate = ""
        todate_fromdate = ""
        delDateFormat = ""
        toDateFormat = ""
        startTimeFormat = ""
        endTimeFormat = ""
        locaTion = ""
        buildName = ""
        blockNum = ""
        zipCode = ""
        landMark = ""
        area = ""
        city = ""
        streeT = ""
    }

    override fun onClick(v: View?) {
      when(v?.id){
          R.id.img_back->{
              startActivity(Intent(this@OrderSuccessfulActivity, MainActivity::class.java))
              finish()
              overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
          }
          R.id.btn_back_t_dasbrd->{
              startActivity(Intent(this@OrderSuccessfulActivity, MainActivity::class.java))
              finish()
              overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
          }
      }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@OrderSuccessfulActivity, MainActivity::class.java))
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

    }
}