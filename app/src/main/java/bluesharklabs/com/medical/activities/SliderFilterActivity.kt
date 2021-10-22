package bluesharklabs.com.medical.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.fragments.*
import bluesharklabs.com.medical.interfaces.FiltersInterFace
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.Filter
import bluesharklabs.com.medical.model.NotiFications
import bluesharklabs.com.medical.response.FilterResponse
import bluesharklabs.com.medical.response.NotificationResponse
import bluesharklabs.com.medical.utils.AppConstant
import kotlinx.android.synthetic.main.activity_slider_filter.*
import kotlinx.android.synthetic.main.dashboard_fragment.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class SliderFilterActivity : AppCompatActivity(), View.OnClickListener, FiltersInterFace {


    private var byType = ""
    private var byStatus = ""
    private var byStoreName = ""
    private var byCity = ""
    private var byArea = ""
    private var byZipcode = ""
    private var byOffer = ""
    private lateinit var mBroadcastManager: LocalBroadcastManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR//  set status text dark

        setContentView(R.layout.activity_slider_filter)

        txt_by_store_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        txt_by_area.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        txt_by_zip_code.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        txt_by_status.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

        img_close.setOnClickListener(this)
        btn_reset.setOnClickListener(this)
        btn_apply.setOnClickListener(this)
        txt_by_type.setOnClickListener(this)
        txt_by_status.setOnClickListener(this)
        txt_by_store_name.setOnClickListener(this)
        txt_by_city.setOnClickListener(this)
        txt_by_area.setOnClickListener(this)
        txt_by_zip_code.setOnClickListener(this)
        txt_by_offer.setOnClickListener(this)
        mBroadcastManager = LocalBroadcastManager.getInstance(this)

        clickType()
        //commonFilter()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_apply -> {
                mBroadcastManager.sendBroadcast(
                        Intent(resources.getString(R.string.broadcastFilterResult))
                                .putExtra(
                                        resources.getString(R.string.filterType),
                                        byType
                                )
                                .putExtra(
                                        resources.getString(R.string.filterStatus),
                                        byStatus
                                )
                                .putExtra(
                                        resources.getString(R.string.filterStoreName),
                                        byStoreName
                                )
                                .putExtra(
                                        resources.getString(R.string.filterCity),
                                        byCity
                                )
                                .putExtra(
                                        resources.getString(R.string.filterArea),
                                        byArea
                                )
                                .putExtra(
                                        resources.getString(R.string.filterZipcode),
                                        byZipcode
                                )
                                .putExtra(
                                        resources.getString(R.string.filterOffer),
                                        byOffer
                                )
                )

                finish()
            }
            R.id.btn_reset -> {
                byType = ""
                byStatus = ""
                byStoreName = ""
                byCity = ""
                byArea = ""
                byZipcode = ""
                byOffer = ""

                mBroadcastManager.sendBroadcast(
                        Intent(resources.getString(R.string.broadcastFilterResult))
                                .putExtra(
                                        resources.getString(R.string.filterType),
                                        byType
                                )
                                .putExtra(
                                        resources.getString(R.string.filterStatus),
                                        byStatus
                                )
                                .putExtra(
                                        resources.getString(R.string.filterStoreName),
                                        byStoreName
                                )
                                .putExtra(
                                        resources.getString(R.string.filterCity),
                                        byCity
                                )
                                .putExtra(
                                        resources.getString(R.string.filterArea),
                                        byArea
                                )
                                .putExtra(
                                        resources.getString(R.string.filterZipcode),
                                        byZipcode
                                )
                                .putExtra(
                                        resources.getString(R.string.filterOffer),
                                        byOffer
                                )
                )

                finish()

            }
            R.id.img_close -> {
                finish()
            }
            R.id.txt_by_type -> {
                clickType()
            }
            R.id.txt_by_store_name -> {
                clickStoreName()
            }
            R.id.txt_by_area -> {
                clickArea()
            }
            R.id.txt_by_city -> {
                clickCity()
            }
            R.id.txt_by_zip_code -> {

                clickZipCode()
            }
            R.id.txt_by_offer -> {

                clickOffer()
            }
            R.id.txt_by_status -> {
                txt_by_status.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                clickStatus()
//                startActivity(
//                    Intent(this@SliderFilterActivity, SliderFilterActivity::class.java)
//                )
//                //finish()
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

    fun clickZipCode() {
        txt_by_zip_code.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightGray))
        txt_by_status.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_type.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_store_name.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_area.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_city.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_offer.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))

        val newFragment = ByZipCodeFragment(this)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_frame, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }

    fun clickStatus() {
        txt_by_status.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightGray))
        txt_by_type.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_store_name.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_area.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_city.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_zip_code.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_offer.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))

        val newFragment = ByStatusFragment(this)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_frame, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun clickCity() {
        txt_by_city.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightGray))
        txt_by_status.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_type.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_store_name.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_area.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_zip_code.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_offer.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))

        val newFragment = ByCityFragment(this)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_frame, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun clickArea() {
        txt_by_area.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightGray))
        txt_by_city.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_status.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_type.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_store_name.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_zip_code.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_offer.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))

        val newFragment = ByAreaFragment(this)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_frame, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun clickStoreName() {
        txt_by_store_name.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightGray))
        txt_by_city.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_status.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_type.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_area.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_zip_code.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_offer.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))

        val newFragment = ByStoreNameFragment(this)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_frame, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun clickType() {
        txt_by_type.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightGray))
        txt_by_city.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_status.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_store_name.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_area.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_zip_code.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_offer.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))

        val newFragment = ByTypeFragment(this)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_frame, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun clickOffer() {
        txt_by_offer.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLightGray))
        txt_by_type.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_city.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_status.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_store_name.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_area.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))
        txt_by_zip_code.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite))

        val newFragment = ByOfferFragment(this)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_frame, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    fun commonFilter() {

        progressBarSlider.visibility = View.VISIBLE
        val filter = Filter()
        filter.user_id = AppConstant.getPreferenceText(AppConstant.PREF_USER_ID)
        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.common_filter(filter)

        call.enqueue(object : Callback<FilterResponse> {
            override fun onResponse(
                    call: Call<FilterResponse>,
                    response: retrofit2.Response<FilterResponse>
            ) {
                progressBarSlider.visibility = View.GONE
                if (response.isSuccessful) {


                } else {

                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@SliderFilterActivity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@SliderFilterActivity, e.message, Toast.LENGTH_LONG).show()
                    }

                }
            }


            override fun onFailure(call: Call<FilterResponse>, t: Throwable) {
                // progressBarSlider.hide();
                progressBarSlider.visibility = View.GONE


            }
        })
    }


    override fun onClickType(type: String) {
        byType = type
    }

    override fun onClickStatus(status: String) {
        byStatus = status
    }

    override fun onClickStoreName(storeName: String) {
        byStoreName = storeName
    }

    override fun onClickCity(city: String) {
        byCity = city
    }

    override fun onClickArea(area: String) {
        byArea = area
    }

    override fun onClickZipcode(zipcode: String) {
        byZipcode = zipcode

    }

    override fun onClickOffer(offer: String) {
        byOffer = offer
    }
}
