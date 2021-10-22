package bluesharklabs.com.medical.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.AddOrder
import bluesharklabs.com.medical.model.StoreDetail
import bluesharklabs.com.medical.response.AddOrderResponse
import bluesharklabs.com.medical.response.StoreDetailResponse
import bluesharklabs.com.medical.utils.AppConstant
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_custom_store_detail.*
import kotlinx.android.synthetic.main.activity_custom_store_detail.edt_gst_no
import kotlinx.android.synthetic.main.activity_custom_store_detail.img_back
import kotlinx.android.synthetic.main.activity_custom_store_detail.img_store
import kotlinx.android.synthetic.main.activity_custom_store_detail.progressBar
import kotlinx.android.synthetic.main.activity_custom_store_detail.txt_drug_licens_no
import kotlinx.android.synthetic.main.activity_custom_store_detail.txt_establis_since
import kotlinx.android.synthetic.main.activity_custom_store_detail.txt_geo_location
import kotlinx.android.synthetic.main.activity_custom_store_detail.txt_mrchandiz_category
import kotlinx.android.synthetic.main.activity_custom_store_detail.txt_ownr_name
import kotlinx.android.synthetic.main.activity_custom_store_detail.txt_pharmacist_name
import kotlinx.android.synthetic.main.activity_custom_store_detail.txt_prefrd_paymnt
import kotlinx.android.synthetic.main.activity_custom_store_detail.txt_stor_nm
import kotlinx.android.synthetic.main.activity_custom_store_detail.txt_store_add
import kotlinx.android.synthetic.main.activity_ordering_for.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class CustomStoreDetailActivity : AppCompatActivity(), View.OnClickListener {

    var phone = ""
    var storeid = ""
    var base64Imag = ""
    var latitude = ""
    var longitude = ""
    var bitmap: Bitmap? = null
    var path = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR//  set status text dark

        setContentView(R.layout.activity_custom_store_detail)


        btn_send_ordr_to_this_store.setOnClickListener(this)
        img_back.setOnClickListener(this)
        txt_store_add.setOnClickListener(this)


        if (intent != null) {
            progressBar.visibility = View.VISIBLE
            if (intent.getStringExtra("storeoffer") != null) {
                btn_send_ordr_to_this_store.visibility = View.GONE

                // progressBar.visibility=View.GONE
            }
            if (intent.getStringExtra("phone") != null) {
                phone = intent.getStringExtra("phone")

                //  progressBar.visibility=View.GONE
            }

            if (AppConstant.photoFinishBitmap != null) {

                bitmap = BitmapFactory.decodeFile(AppConstant.photoFinishBitmap.toString())
                base64Imag = AppConstant.convert(AppConstant.photoFinishBitmap!!)
                progressBar.visibility = View.GONE
            }
            // progressBar.visibility=View.GONE
        }

        storeDetailApi()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txt_store_add -> {
//                val currentlat = MainActivity.current_latitude
//                val currentlong = MainActivity.current_longitude
//
//                Log.e("HelloBroad", "currentlat_cust   " + currentlat + "  currentlat2   " + latitude)
//                Log.e("HelloBroad", "currentlong_cust   " + currentlong + "  currentlong2   " + longitude)
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + OrderPreferenceActivity.order_lat + "," + OrderPreferenceActivity.order_long + "&daddr=" + latitude + "," + longitude))
//
////                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + latitude + "," + longitude))
//                startActivity(intent)


                var orderLat:String = ""
                var orderLong:String = ""
                if(!OrderPreferenceActivity.del_lat.equals("")){
                    orderLat = OrderPreferenceActivity.del_lat
                    orderLong = OrderPreferenceActivity.del_long
                }else if(!OrderPreferenceActivity.order_lat.equals("")){
                    orderLat = OrderPreferenceActivity.order_lat
                    orderLong = OrderPreferenceActivity.order_long
                }



                if(orderLat.equals("")){
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + latitude + "," + longitude))

                    startActivity(intent)
                }else {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + orderLat + "," + orderLong + "&daddr=" + latitude + "," + longitude))

                    startActivity(intent)
                }
            }
            R.id.img_back -> {
                finish()
            }
            R.id.btn_send_ordr_to_this_store -> {
                addOrderApi()

            }
        }
    }

    fun storeDetailApi() {

        progressBar.visibility = View.VISIBLE
        val storeDetail = StoreDetail()
        storeDetail.phone = phone

        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.storedetail(storeDetail)

        call.enqueue(object : Callback<StoreDetailResponse> {
            override fun onResponse(
                    call: Call<StoreDetailResponse>,
                    response: retrofit2.Response<StoreDetailResponse>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {

                    val locaTion =
                            response.body()!!.data!!.shopNo + ", " + response.body()!!.data!!.building + ", " + response.body()!!.data!!.street + ", " + response.body()!!.data!!.area + ", " + response.body()!!.data!!.landmark + ", " + response.body()!!.data!!.city + ", " + response.body()!!.data!!.zipcode

                    txt_stor_nm.text = response.body()?.data?.name
                    txt_store_add.text = response.body()?.data?.geoLocation
                    txt_ownr_name.text = response.body()?.data?.ownerName
                    edt_gst_no.text = response.body()?.data?.gstNumber
                    txt_drug_licens_no.text = response.body()?.data?.establishedSince
                    txt_establis_since.text = response.body()?.data?.drugLicenseNumber
                    txt_geo_location.text = response.body()?.data?.geoLocation
                    txt_store_type.text = response.body()?.data?.type
                    txt_geo_location.text = locaTion
                    val input: String = response.body()?.data?.paymentMethod!!
                    val builder = StringBuilder()
                    val items = input.split("\\p{Punct}".toRegex()).toTypedArray()
                    for (details in items) {
                        builder.append(details + "\n")
                    }
                    txt_prefrd_paymnt.text = builder.toString()

                    val category: String = response.body()?.data?.merchandiseCategory!!
                    val builderr = StringBuilder()
                    val merchandise = category.split("\\p{Punct}".toRegex()).toTypedArray()
                    for (details in merchandise) {
                        builderr.append(details + "\n")
                    }
                    txt_mrchandiz_category.text = builderr.toString()
                    txt_pharmacist_name.text = response.body()?.data?.registeredPharmacistName


                    latitude = response.body()?.data?.latitude.toString()
                    longitude = response.body()?.data?.longitude.toString()

                    storeid = response.body()!!.data!!.id.toString()

                    Glide.with(applicationContext)  //2
                            .load(response.body()!!.data!!.storePhoto) //3
                            .centerCrop() //4
                            .placeholder(R.drawable.ic_launcher_background) //5
                            .into(img_store) //8

                    /*
                                       startActivity(
                                           Intent(this@LoginActivity, VerifyActivity::class.java).putExtra(
                                               CONSTANT_NUMBER,
                                               number
                                           )
                                       )
                                       finish()
                                       overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)*/

                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@CustomStoreDetailActivity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@CustomStoreDetailActivity, e.message, Toast.LENGTH_LONG)
                                .show()
                    }

                }
            }


            override fun onFailure(call: Call<StoreDetailResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar.visibility = View.GONE
            }
        })
    }


    fun addOrderApi() {

        progressBar.visibility = View.VISIBLE
        val addorder = AddOrder()
        addorder.store_id = storeid
        addorder.store_latitude = latitude
        addorder.store_longitude = longitude
        addorder.user_id = AppConstant.getPreferenceText(AppConstant.PREF_USER_ID)
        addorder.area = OrderPreferenceActivity.area
        addorder.block_no = OrderPreferenceActivity.blockNum
        addorder.building_name = OrderPreferenceActivity.buildName
        addorder.delivery_latitude = OrderPreferenceActivity.del_lat
        addorder.delivery_longitude = OrderPreferenceActivity.del_long
        addorder.delivery_type = OrderPreferenceActivity.delType
        addorder.delivery_city = OrderPreferenceActivity.delivery_city
        addorder.landmark = OrderPreferenceActivity.landMark
        addorder.location = OrderPreferenceActivity.locaTion
        addorder.location_criteria_delivery_location = FindPlatformActivity.locationCriteriaDelivery
        addorder.location_criteria_location = FindPlatformActivity.locationCriteria
        addorder.order_for = OrderingForActivity.orderFor
        addorder.order_latitude = OrderPreferenceActivity.order_lat
        addorder.order_longitude = OrderPreferenceActivity.order_long
        addorder.order_preference = OrderPreferenceActivity.drugPrefrnc
        addorder.buying_preference = OrderPreferenceActivity.buyingPrefrnc
        addorder.photo_id = OrderPreferenceActivity.photoId
        addorder.prescription_image = base64Imag
        addorder.start_time = OrderPreferenceActivity.startTime
        addorder.end_time = OrderPreferenceActivity.endTime
        addorder.delivery_date = OrderPreferenceActivity.delDate
        addorder.to_delivery_date = OrderPreferenceActivity.toDeliveryDate
        addorder.store_criteria_area = FindPlatformActivity.finalArea
        addorder.store_criteria_city = FindPlatformActivity.finalCity
        addorder.store_criteria_store_md_id = FindPlatformActivity.final_md_id
        addorder.store_criteria_store_type_id = FindPlatformActivity.final_type_id
        addorder.store_criteria_zipcode = FindPlatformActivity.finalZipcode
        addorder.street = OrderPreferenceActivity.streeT
        addorder.turn_on_location = OrderPreferenceActivity.turnOnLocation
        addorder.zipcode = OrderPreferenceActivity.zipCode
        addorder.products = PrescriptionOrderActivity.array
        addorder.custom_products = CustomOrderActivity.arrProduct
        addorder.order_platform = "2"
        addorder.select_by_location_criteria = "0"
        addorder.select_by_store_criteria = "0"


        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.addorder(addorder)

        call.enqueue(object : Callback<AddOrderResponse> {
            override fun onResponse(
                    call: Call<AddOrderResponse>,
                    response: retrofit2.Response<AddOrderResponse>
            ) {

                Log.d("TAG","response djxhfjgjdgdf")

                Log.e("TAG","Response"+response.body())




                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    startActivity(
                            Intent(this@CustomStoreDetailActivity, OrderSuccessfulActivity::class.java))
                    //finish()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@CustomStoreDetailActivity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@CustomStoreDetailActivity, e.message, Toast.LENGTH_LONG)
                                .show()
                    }

                }
            }


            override fun onFailure(call: Call<AddOrderResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar.visibility = View.GONE
            }
        })
    }

}
