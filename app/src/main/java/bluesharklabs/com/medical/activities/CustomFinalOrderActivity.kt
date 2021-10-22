package bluesharklabs.com.medical.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.activities.FindPlatformActivity.Companion.finalCity
import bluesharklabs.com.medical.activities.FindPlatformActivity.Companion.finalZipcode
import bluesharklabs.com.medical.activities.FindPlatformActivity.Companion.final_md_id
import bluesharklabs.com.medical.activities.FindPlatformActivity.Companion.final_type_id
import bluesharklabs.com.medical.activities.FindPlatformActivity.Companion.locationCriteria
import bluesharklabs.com.medical.activities.FindPlatformActivity.Companion.locationCriteriaDelivery
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.blockNum
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.buildName
import bluesharklabs.com.medical.adapters.ViewAllProductsAdapter
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.AddOrder
import bluesharklabs.com.medical.model.OrderDetail
import bluesharklabs.com.medical.response.OrderDetailResponse
import bluesharklabs.com.medical.utils.AppConstant
import bluesharklabs.com.medical.utils.AppConstant.photoFinishBitmap
import bluesharklabs.com.medical.utils.Utils.Companion.GettingMiliSeconds
import bluesharklabs.com.medical.utils.afterMeasured
import kotlinx.android.synthetic.main.activity_custom_final_order.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.util.*
import java.util.concurrent.TimeUnit

class CustomFinalOrderActivity : AppCompatActivity(), View.OnClickListener {

    private var viewAllProductsAdapter: ViewAllProductsAdapter? = null
    private var isViewAll = "0"
    private var isReorder = "1"
    private var timer = ""
    private var orderId = ""
    private var offerId = ""
    private var viewOrder = "0"
    private var orderType = ""
    private var orderTypeId = ""
    private var latitude = ""
    private var longitude = ""
    private var store_latitude = ""
    private var store_longitude = ""
    private var isCustom = false
    var bmCon: Bitmap? = null
    var myBmp: Bitmap? = null
    var imgAttchmentWidth = ""
    var imgAttchmentHeight = ""
    var viewWidth = ""
    var viewHeight = ""
    var iv: ImageView? = null
    var childcount: Int = 0
    var hashMap: HashMap<Int, Int> = HashMap<Int, Int>()

    var cTimer: CountDownTimer? = null
    var isEditable = false
    var isCustomOrder = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//  set status text dark

        setContentView(R.layout.activity_custom_final_order)

        img_back.setOnClickListener(this)
        txt_view_all_produts.setOnClickListener(this)
        edt_prodcts.setOnClickListener(this)
        edt_order.setOnClickListener(this)
        edt_prefer_scedule.setOnClickListener(this)
        edt_order_prefrnc.setOnClickListener(this)
        edt_delivery_prefrnc.setOnClickListener(this)
        btn_sendtostor.setOnClickListener(this)
        btn_sendtopltfrm.setOnClickListener(this)
        btn_review_ordr.setOnClickListener(this)
        txt_geo_location.setOnClickListener(this)
        edt_prescription.setOnClickListener(this)
        img_prescr.setOnClickListener(this)

        if (intent != null) {

            if (intent.getBooleanExtra("custom", true)) {
                isCustom = intent.getBooleanExtra("custom", true)
            }

            if (intent.getStringExtra("offerId") != null) {
                offerId = intent.getStringExtra("offerId")
            }

            if (intent.getStringExtra("reorder") != null) {
                isReorder = intent.getStringExtra("reorder")
                Log.d("TAG", "isReorder----" + isReorder)

                setLayoutForReorder()
            }

            if (intent.getStringExtra("reditorder") != null) {
                setLayoutEdtUploadAgain()
            }

            if (intent.getStringExtra("Timer") != null) {
                timer = intent.getStringExtra("Timer")
                txt.text = timer
            }

            if (intent.getStringExtra("orderid") != null) {
                orderId = intent.getStringExtra("orderid")
                Log.e("HelloReOrde", "orderDetailApi ")
                orderDetailApi()

            } else {
                Log.e("HelloReOrde", "init ")
                init()
            }
            if (intent.getStringExtra("offeracc") != null) {
                textView9.text = resources.getString(R.string.ordering_for)
                txt_order_prefrnc.text = resources.getString(R.string.buying_prefrnc)
                prefer_scedule.text = resources.getString(R.string.prefer_schedule)
            }
            if (intent.getStringExtra("vieworder") != null) {

                viewOrder = "1"
                viewOrder()

                init()
            }
        }
    }

    @SuppressLint("WrongConstant")
    private fun init() {
        orderType = OrderingForActivity.orderForTxt.toString()


        orderTypeId = OrderingForActivity.orderFor.toString()
        txt_ordering_for.text = OrderingForActivity.orderForTxt
        txt_title_delivery_prefrnc.text = OrderPreferenceActivity.delprerncetext
        if (OrderPreferenceActivity.drugPrefrncTxt.equals("")) {
            txt_ordr_prefrnc.text = OrderPreferenceActivity.buyingPrefrncTxt
        } else {
            txt_ordr_prefrnc.text = OrderPreferenceActivity.drugPrefrncTxt
        }

        Log.e("HelloReOrde", "drugPrefrncTxt " + OrderPreferenceActivity.drugPrefrncTxt.toString() + "   buyingPrefrnc " + OrderPreferenceActivity.buyingPrefrnc.toString() + "   buyingPrefrncTxt " + OrderPreferenceActivity.buyingPrefrncTxt.toString())
        txt_delivery_date.text = OrderPreferenceActivity.todate_fromdate
        //  txt_delivery_time.text = OrderPreferenceActivity.toDeliveryDate+" "+OrderPreferenceActivity.endTimeFormat
        if (OrderPreferenceActivity.delType.equals("0")) {
            dellivery_date.text = resources.getString(R.string.pickup_date)

            //delivery_time.text = resources.getString(R.string.pickup_time)
            if (intent != null && intent.getStringExtra("offeracc") == null) {
                prefer_scedule.text = resources.getString(R.string.pickup_schedule)
            }
            txt_delivery_prefrnc.visibility = View.GONE

        } else if (OrderPreferenceActivity.delType.equals("1")) {
            txt_geo_location.visibility = View.VISIBLE
            val locaTion = blockNum + ", " + buildName
            txt_title_delivery_prefrnc.text = OrderPreferenceActivity.delprerncetext + " in " + OrderPreferenceActivity.delivery_city
            txt_geo_location.text = OrderPreferenceActivity.locaTion
            latitude = OrderPreferenceActivity.order_lat
            longitude = OrderPreferenceActivity.order_long

            store_latitude = OrderPreferenceActivity.store_lat
            store_longitude = OrderPreferenceActivity.store_long
            txt_delivery_prefrnc.visibility = View.VISIBLE
            txt_delivery_prefrnc.text = locaTion
        } else {
            txt_title_delivery_prefrnc.text = OrderPreferenceActivity.delprerncetext + " in "+ OrderPreferenceActivity.delivery_city
            val locaTion =
                    blockNum + ", " + OrderPreferenceActivity.buildName + ", " + OrderPreferenceActivity.streeT + ", " +
                            OrderPreferenceActivity.area + ", " + OrderPreferenceActivity.landMark + ", " + OrderPreferenceActivity.zipCode

            txt_delivery_prefrnc.visibility = View.VISIBLE
            txt_delivery_prefrnc.text = locaTion
        }



        if(!isCustomOrder){
            bmCon = photoFinishBitmap
            if (bmCon != null) {
                rel_priscription.visibility = View.VISIBLE
                //  img_prescr.visibility=View.VISIBLE
                // edt_prescription.visibility=View.VISIBLE
                constrain_recycler?.visibility = View.GONE
                constrain_prescription.setBackgroundColor(resources.getColor(R.color.bg_color))
                myBmp = scaleBitmap(bmCon!!)
                img_prescr.setImageDrawable(BitmapDrawable(resources, myBmp))

                rel_priscription.afterMeasured {
                    viewWidth = rel_priscription.width.toString()
                    viewHeight = rel_priscription.height.toString()

                    addTags()

                }


            } else {
                rel_priscription.visibility = View.GONE
                // img_prescr.visibility=View.GONE
                // edt_prescription.visibility=View.GONE
                constrain_recycler?.visibility = View.VISIBLE
                constrain_prescription.setBackgroundColor(resources.getColor(R.color.colorWhite))
            }
        }else{
            rel_priscription.visibility = View.GONE
            // img_prescr.visibility=View.GONE
            // edt_prescription.visibility=View.GONE
            constrain_recycler?.visibility = View.VISIBLE
            constrain_prescription.setBackgroundColor(resources.getColor(R.color.colorWhite))
        }


        recycl?.setHasFixedSize(true)
        recycl?.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false) as RecyclerView.LayoutManager?
        viewAllProductsAdapter = ViewAllProductsAdapter(this, CustomOrderActivity.arrProduct, isViewAll)
        recycl?.adapter = viewAllProductsAdapter
    }
/*
    private fun scaleBitmap(bmp: Bitmap): Bitmap {
        var bm = bmp
        var width = bm.width
        var height = bm.height
        imgAttchmentWidth = width.toString()
        imgAttchmentHeight = height.toString()
        return bm
    }*/


    private fun setLayoutForReorder() {

        if (!isReorder.equals("1")) {
            btn_sendtopltfrm.visibility = View.VISIBLE
            btn_sendtostor.visibility = View.VISIBLE
            btn_review_ordr.visibility = View.GONE
            edt_prodcts.visibility = View.INVISIBLE
            edt_order.visibility = View.INVISIBLE
            edt_prefer_scedule.visibility = View.INVISIBLE
            edt_order_prefrnc.visibility = View.INVISIBLE
            edt_delivery_prefrnc.visibility = View.INVISIBLE
            txt1.visibility = View.VISIBLE
            txt.visibility = View.VISIBLE
            txt.setText(isReorder)

        } else {
            btn_sendtopltfrm.visibility = View.GONE
            btn_sendtostor.visibility = View.GONE
            btn_review_ordr.visibility = View.VISIBLE
            txt1.visibility = View.GONE
            txt.visibility = View.GONE
            edt_prodcts.visibility = View.VISIBLE
            edt_order.visibility = View.VISIBLE
            edt_prefer_scedule.visibility = View.VISIBLE
            edt_order_prefrnc.visibility = View.VISIBLE
            edt_delivery_prefrnc.visibility = View.VISIBLE

        }


    }

    private fun setLayoutEdtUploadAgain() {
        btn_sendtopltfrm.visibility = View.VISIBLE
        btn_sendtostor.visibility = View.VISIBLE
        btn_review_ordr.visibility = View.GONE
        txt1.visibility = View.GONE
        txt.visibility = View.GONE
        edt_order.visibility = View.VISIBLE
        edt_prefer_scedule.visibility = View.VISIBLE
        edt_order_prefrnc.visibility = View.VISIBLE
        edt_delivery_prefrnc.visibility = View.VISIBLE
    }

    private fun orderDetailApi() {
        progressBar!!.visibility = View.VISIBLE
        val orderdetail = OrderDetail()
        orderdetail.user_id = AppConstant.getPreferenceText(AppConstant.PREF_USER_ID)
        orderdetail.order_id = orderId
        orderdetail.offer_id = offerId


        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.orderdetail(orderdetail)

        call.enqueue(object : Callback<OrderDetailResponse> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                    call: Call<OrderDetailResponse>,
                    response: retrofit2.Response<OrderDetailResponse>
            ) {
                progressBar!!.visibility = View.GONE
                if (response.isSuccessful) {


                    data = response.body()?.data!!

                    Log.e("HelloReOrde", "user_id  " + AppConstant.getPreferenceText(AppConstant.PREF_USER_ID) + "   order_id   " + orderId + "   offer_id   " + offerId)


                    CustomOrderActivity.arrProduct = arrayListOf()

                    txt_order_id.text = "ID: " + data!!.orderNo

                    txt_order_dtntime.text = (response.body()!!.data!!.createdDate!!)
                    //  AppConstant.convertdatentimetoOrderTime(data!!.createdDate!!).toLowerCase()

                    // OrderingForActivity.orderForTxt = data?.orderForName!!


                    OrderPreferenceActivity.startTime = data?.startTime!!
                    OrderPreferenceActivity.endTime = data?.endTime!!


                    Log.e("HelloReOrde", "OnComes   delDateFormatAPI " + data?.deliveryDate!! + "   toDateFormatAPI   " + data?.toDeliveryDate!!)


                    OrderPreferenceActivity.delDate = data?.deliveryDate!!
                    OrderPreferenceActivity.toDeliveryDate = data?.toDeliveryDate!!
                    OrderPreferenceActivity.delDateFormat = data?.deliveryDate!!
                    OrderPreferenceActivity.toDateFormat = data?.toDeliveryDate!!
                    OrderPreferenceActivity.startTimeFormat = data?.startTime!!
                    OrderPreferenceActivity.endTimeFormat = data?.endTime!!
                    OrderPreferenceActivity.todate_fromdate = data?.todateFromdate!!

                    OrderPreferenceActivity.delprerncetext = data?.deliveryTypeName!!
                    OrderPreferenceActivity.delType = data?.deliveryType!!
                    OrderPreferenceActivity.delivery_city = data?.deliveryCity!!
                    // OrderPreferenceActivity.
                    OrderPreferenceActivity.locaTion = response.body()!!.data!!.location!!

                    if (data?.orderPreferenceName != null &&
                            !data?.orderPreferenceName!!.equals("")) {
                        OrderPreferenceActivity.drugPrefrncTxt = data?.orderPreferenceName!!
                        OrderPreferenceActivity.drugPrefrnc = data?.orderPreference!!
                    } else {
                        OrderPreferenceActivity.buyingPrefrncTxt = data?.buyingPreferenceName!!
                        OrderPreferenceActivity.buyingPrefrnc = data?.buyingPreference!!
                    }

                    OrderingForActivity.orderForTxt = data?.orderForName!!
                    orderType = data?.orderForName!!
                    orderTypeId = data?.orderFor!!
                    OrderingForActivity.orderFor = data?.orderFor!!
                    OrderPreferenceActivity.photoId = data?.photoId!!
                    OrderPreferenceActivity.area = data?.area!!
                    OrderPreferenceActivity.order_lat = data?.orderLatitude!!
                    OrderPreferenceActivity.order_long = data?.orderLongitude!!

                    Log.e("HElfmkodsgoh", "   Store_LAT   " + data?.orderType!!+"   Store_LOGI   " + data?.orderTypeName!!+"   Lat   " + data?.deliveryLatitude!!+"   Long   " + data?.deliveryLongitude!!+"   Latssss   " + data?.orderLatitude!!+"   LOTIII   " + data?.orderLongitude!!)

                    if(data!!.orderTypeName.equals("Custom",true)){
                        isCustomOrder = true
                    }


                    OrderPreferenceActivity.store_lat = data?.store_latitude!!
                    OrderPreferenceActivity.store_long = data?.store_longitude!!
                    blockNum = data?.blockNo!!
                    buildName = data?.buildingName!!
                    locationCriteriaDelivery = data?.locationCriteriaDeliveryLocation!!
                    locationCriteria = data?.locationCriteriaLocation!!
                    OrderPreferenceActivity.landMark = data?.landmark!!
                    OrderPreferenceActivity.del_lat = data?.deliveryLatitude!!
                    OrderPreferenceActivity.del_long = data?.deliveryLongitude!!
                    OrderPreferenceActivity.city = data?.storeCriteriaCity!!
                    OrderPreferenceActivity.zipCode = data?.zipcode!!
                    final_md_id = data?.storeCriteriaStoreMdId!!
                    final_type_id = data?.storeCriteriaStoreTypeId!!
                    finalZipcode = data?.storeCriteriaZipcode!!
                    finalCity = data?.storeCriteriaCity!!
                    OrderPreferenceActivity.turnOnLocation = data?.turnOnLocation!!
                    OrderPreferenceActivity.streeT = data?.street!!



                    for (i in 0 until response.body()?.data!!.products!!.size) {

                        val custmProducts = AddOrder.customProducts()

                        custmProducts.color = response.body()?.data!!.products!!.get(i).color
                        custmProducts.product_brand =
                                response.body()?.data!!.products!!.get(i).productBrand
                        custmProducts.product_category =
                                response.body()?.data!!.products!!.get(i).productCategory
                        custmProducts.product_content =
                                response.body()?.data!!.products!!.get(i).productContent
                        custmProducts.product_name =
                                response.body()?.data!!.products!!.get(i).productName
                        custmProducts.product_pack =
                                response.body()?.data!!.products!!.get(i).productPack
                        custmProducts.product_pack_name =
                                response.body()?.data!!.products!!.get(i).productPackName
                        custmProducts.product_unit =
                                response.body()?.data!!.products!!.get(i).productUnit
                        custmProducts.product_unit_name =
                                response.body()?.data!!.products!!.get(i).productUnitName
                        custmProducts.quantity = response.body()?.data!!.products!!.get(i).quantity
                        custmProducts.product_id = response.body()?.data!!.products!!.get(i).productId


                        CustomOrderActivity.arrProduct.add(custmProducts)

                    }
                    init()


                    /*  if ( CustomOrderActivity.arrProduct.size>3){
                          txt_view_all_produts.visibility = View.VISIBLE
                      }*/

                    viewAllProductsAdapter =
                            ViewAllProductsAdapter(
                                    this@CustomFinalOrderActivity,
                                    CustomOrderActivity.arrProduct,
                                    isViewAll
                            )
                    recycl!!.adapter = viewAllProductsAdapter


                    // for showing clock of 24 hours
//                    if (!response.body()?.data!!.requestStatus!!.equals("Timed out", true)) {
                    isEditable = false
                    btn_sendtopltfrm.visibility = View.GONE
                    btn_sendtostor.visibility = View.GONE
                    btn_review_ordr.visibility = View.GONE
                    txt1.visibility = View.VISIBLE
                    txt.visibility = View.VISIBLE
                    edt_prodcts.visibility = View.INVISIBLE
                    edt_order.visibility = View.INVISIBLE
                    edt_prefer_scedule.visibility = View.INVISIBLE
                    edt_order_prefrnc.visibility = View.INVISIBLE
                    edt_delivery_prefrnc.visibility = View.INVISIBLE

                    val date = Date()
                    //This method returns the time in millis
                    val timeMilli = date.time
                    val wastedTime: Long = timeMilli - GettingMiliSeconds(response.body()?.data!!.createdDate!!)!!
                    val remainingTime: Long = 86400000 - wastedTime

                    cTimer = object : CountDownTimer(remainingTime, 1000) {
                        override fun onTick(millisUntilFinished: Long) {

                            val day1 = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
                            val hh1 = (TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(day1))
                            val mm1 = (TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)))
                            val ss = (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))

                            timer = ("" + hh1 + "h : " + mm1 + "m : " + ss + "s")
                            txt.text = timer
                        }

                        override fun onFinish() {
                            cancelTimer()
                            isEditable = true
                            btn_sendtopltfrm.visibility = View.GONE
                            btn_sendtostor.visibility = View.GONE
                            btn_review_ordr.visibility = View.VISIBLE
                            txt1.visibility = View.GONE
                            txt.visibility = View.GONE
                            edt_prodcts.visibility = View.VISIBLE
                            edt_order.visibility = View.VISIBLE
                            edt_prefer_scedule.visibility = View.VISIBLE
                            edt_order_prefrnc.visibility = View.VISIBLE
                            edt_delivery_prefrnc.visibility = View.VISIBLE
                        }
                    }
                    (cTimer as CountDownTimer).start()

//                    } else {
//                        btn_sendtopltfrm.visibility = View.GONE
//                        btn_sendtostor.visibility = View.GONE
//                        btn_review_ordr.visibility = View.VISIBLE
//                        txt1.visibility = View.GONE
//                        txt.visibility = View.GONE
//                        edt_prodcts.visibility = View.VISIBLE
//                        edt_order.visibility = View.VISIBLE
//                        edt_prefer_scedule.visibility = View.VISIBLE
//                        edt_order_prefrnc.visibility = View.VISIBLE
//                        edt_delivery_prefrnc.visibility = View.VISIBLE
//                    }

                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@CustomFinalOrderActivity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@CustomFinalOrderActivity, e.message, Toast.LENGTH_LONG)
                                .show()
                    }

                }
            }


            override fun onFailure(call: Call<OrderDetailResponse>, t: Throwable) {
                // progressBarHandler.hide();
                Log.e("HelloReOrde", "onFailure  else " + t)
                progressBar!!.visibility = View.GONE
            }
        })
    }

    //cancel timer
    fun cancelTimer() {
        if (cTimer != null) cTimer!!.cancel()
    }

    private fun coverrtdate(from_date: String, offerDeliveryTimeStart: String, offerDeliveryTimeEnd: String) {

        /*  OrderPreferenceActivity.delDate = from_date
          OrderPreferenceActivity.toDeliveryDate = from_date
          OrderPreferenceActivity.delDateFormat = from_date
          OrderPreferenceActivity.startTimeFormat = offerDeliveryTimeStart
          OrderPreferenceActivity.endTimeFormat = offerDeliveryTimeEnd*/
    }

    private fun viewOrder() {

        txt_title.visibility = View.GONE
        constraint_bottom.visibility = View.GONE

        edt_order_prefrnc.visibility = View.INVISIBLE
        edt_order.visibility = View.INVISIBLE
        edt_prefer_scedule.visibility = View.INVISIBLE
        edt_delivery_prefrnc.visibility = View.INVISIBLE
        edt_order_prefrnc.visibility = View.INVISIBLE
        edt_prodcts.visibility = View.INVISIBLE

        txt_order_id.visibility = View.VISIBLE
        txt_order_dtntime.visibility = View.VISIBLE
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.edt_prescription -> {
                Log.e("HelloReOrde", "edt_prescription ")
                startActivity(
                        Intent(this@CustomFinalOrderActivity, PrescriptionOrderActivity::class.java) // chnages for showing recyclerview below clickable point image
                                .putExtra("edit", "1")
                )
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)


            }
            R.id.img_prescr -> {
                Log.e("HelloReOrde", "edt_prescription ")
                startActivity(
                        Intent(this@CustomFinalOrderActivity, PrescriptionOrderViewActivity::class.java) // chnages for showing recyclerview below clickable point image
                                .putExtra("edit", "1")
                )
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

            }
            R.id.txt_geo_location -> {
                Log.e("hellmakd", "Store_LAt   " + store_latitude + "   Store_Long   " + store_longitude+"   LAt   " + latitude + "   Long   " + longitude)

                if (!store_longitude.equals("")) {
//                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + latitude + "," + longitude))
//                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+store_latitude + "," + store_longitude+"&daddr=" + latitude + "," + longitude))


                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + latitude + "," + longitude + "&daddr=" + OrderPreferenceActivity.store_lat + "," + OrderPreferenceActivity.store_long))
                    startActivity(intent)
                } else {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + latitude + "," + longitude))
                    startActivity(intent)
                }

                Log.e("HElfmkodsgoh", "LAt  " + store_longitude)


            }
            R.id.img_back -> {
                store_longitude = ""
                store_latitude = ""
                finish()
            }
            R.id.txt_view_all_produts -> {
                Log.e("HelloReOrde", "txt_view_all_produts ")
                var edit = ""
                if (!isReorder.equals("1") || viewOrder.equals("1")) {

                    edit = "0"
                } else {
                    edit = "1"
                }
                startActivity(
                        Intent(
                                this@CustomFinalOrderActivity,
                                CustomAllProductsActivity::class.java
                        ).putExtra("edit", edit).putExtra("isEditable", isEditable)


                )


                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.edt_prodcts -> {
                Log.e("HelloReOrde", "edt_prodcts ")
                startActivity(
                        Intent(
                                this@CustomFinalOrderActivity,
                                CustomOrderActivity::class.java
                        )
                                .putExtra("edit", "1")
                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.btn_sendtostor -> {
                Log.e("HelloReOrde", "btn_sendtostor ")
                startActivity(
                        Intent(
                                this@CustomFinalOrderActivity,
                                FindStoreActivity::class.java
                        )
                                .putExtra("custom", "1")
                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.btn_sendtopltfrm -> {
                Log.e("HelloReOrde", "btn_sendtopltfrm ")
                startActivity(
                        Intent(
                                this@CustomFinalOrderActivity,
                                FindPlatformActivity::class.java
                        )
                                .putExtra("edit", "1")
                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.btn_review_ordr -> {


                Log.e("HelloReOrde", "btn_review_ordr ")

                txt_title.setText(R.string.reorder)

                btn_sendtopltfrm.visibility = View.VISIBLE
                btn_sendtostor.visibility = View.VISIBLE
                btn_review_ordr.visibility = View.GONE
                txt1.visibility = View.GONE
                txt.visibility = View.GONE
            }
            R.id.edt_prefer_scedule -> {
                Log.e("HelloReOrde", "edt_prefer_scedule ")
                startActivity(
                        Intent(
                                this@CustomFinalOrderActivity,
                                OrderPreferenceActivity::class.java
                        ).putExtra("edit", "1")
                                .putExtra("custom", isCustom)

                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.edt_order_prefrnc -> {
                Log.e("HelloReOrde", "edt_order_prefrnc ")
                startActivity(
                        Intent(
                                this@CustomFinalOrderActivity,
                                OrderPreferenceActivity::class.java
                        ).putExtra("edit", "1")
                                .putExtra("custom", isCustom)
                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

            }
            R.id.edt_delivery_prefrnc -> {
                Log.e("HelloReOrde", "edt_delivery_prefrnc ")
                startActivity(
                        Intent(
                                this@CustomFinalOrderActivity,
                                OrderPreferenceActivity::class.java
                        ).putExtra("edit", "1")
                                .putExtra("custom", isCustom)
                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.edt_order -> {
                Log.e("HelloReOrde", "edt_order ")
                startActivity(
                        Intent(
                                this@CustomFinalOrderActivity,
                                OrderingForActivity::class.java
                        )
                                .putExtra("edit", true)
                                .putExtra("custom", isCustom)
                                .putExtra("ORDER_FOR", orderType)
                                .putExtra("ORDER_ID", orderTypeId)
                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }

        }
    }

    companion object {
        var data: OrderDetailResponse.Data? = null
    }


    private fun scaleBitmap(bmp: Bitmap): Bitmap {
        var bm = bmp
        var width = bm.width
        var height = bm.height
        imgAttchmentWidth = width.toString()
        imgAttchmentHeight = height.toString()
        Log.v("Pictures", "Width and height are $width--$height")
        Log.v("Pictures", "WidthHeight" + constrain_prescription.height)

        /*  val rectangle = Rect()
          val window = getWindow()
          window.getDecorView().getWindowVisibleDisplayFrame(rectangle)
          val statusBarHeight = rectangle.top

          var maxWidth = Resources.getSystem().getDisplayMetrics().widthPixels
          var maxHeight =
                  Resources.getSystem().getDisplayMetrics().heightPixels - constrain_prescription.height - constrain_prescription.height
          if (maxWidth <= width) {
              if (width > height) {
                  // landscape
                  val ratio = width.toFloat() / maxWidth
                  width = maxWidth
                  height = (height / ratio).toInt()
              } else if (height > width) {
                  // portrait
                  val ratio = height.toFloat() / maxHeight
                  height = maxHeight
                  width = (width / ratio).toInt()
              } else {
                  // square
                  height = maxHeight
                  width = maxWidth
              }

              Log.v("Pictures", "after scaling Width and height are $width--$height")

              bm = Bitmap.createScaledBitmap(bm, width, height, true)
          }
  */
        return bm
    }

    fun addTags() {


        for (i in 0 until PrescriptionOrderActivity.array.size) {

            iv = ImageView(this)

            val bm: Bitmap?

            bm = BitmapFactory.decodeResource(resources, R.drawable.pin)

            if (bm == null) {
                Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show()
            } else {
                iv!!.setImageBitmap(bm)
                //  iv!!.setPadding(3, 3, 3, 3)

                val params = RelativeLayout.LayoutParams(bm.width, bm.height)

                childcount = rel_img.getChildCount()

                /* params.leftMargin = array.get(i).product_x_position!!.toFloat().toInt()
                 params.topMargin = array.get(i).product_y_position!!.toFloat().toInt()*/

                Log.d("TAG", "ID--left--> " + PrescriptionOrderActivity.array.get(i).product_x_position!!.toFloat().toInt())
                Log.d("TAG", "ID--right-> " + PrescriptionOrderActivity.array.get(i).product_y_position!!.toFloat().toInt())

                viewWidth = rel_img.width.toString()
                viewHeight = rel_img.height.toString()

                imgAttchmentWidth = photoFinishBitmap!!.width.toString()
                imgAttchmentHeight = photoFinishBitmap!!.height.toString()

                Log.d("TAG", "ID--viewWidth--> $viewWidth && $viewHeight")
                Log.d("TAG", "ID--iviewWidth--> $imgAttchmentWidth && $imgAttchmentHeight")
                // Log.d("TAG", "ID--OriginviewWidth--> $OriginX && $OriginY")
                //  Log.d("TAG", "ID--OiginviewWidth--> $touchX && $touchY")


                params.leftMargin =
                        (viewWidth.toInt() *
                                PrescriptionOrderActivity.array.get(i).product_x_position!!.toFloat().toInt()) /
                                imgAttchmentWidth.toInt()
                params.topMargin =
                        (viewHeight.toInt() * PrescriptionOrderActivity.array.get(i).product_y_position!!.toFloat().toInt()) / imgAttchmentHeight.toInt()


                Log.d("TAG", "ID--top--> " + params.leftMargin)
                Log.d("TAG", "ID--bottom-> " + params.topMargin)
                hashMap.put(
                        PrescriptionOrderActivity.array.get(i).product_x_position!!.toInt(),
                        PrescriptionOrderActivity.array.get(i).product_y_position!!.toInt()
                )

                rel_priscription.addView(iv, params)

                // for (i in 0 until childcount) {
                val v = rel_priscription.getChildAt(i)
                changeBitmapColor(bm, iv!!, Color.parseColor("#" + PrescriptionOrderActivity.array.get(i).color))
                // }
            }
        }
    }

    private fun changeBitmapColor(sourceBitmap: Bitmap, image: ImageView, color: Int) {

        val paint = Paint()
        paint.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        val bitmapResult =
                Bitmap.createBitmap(
                        sourceBitmap.getWidth(),
                        sourceBitmap.getHeight(),
                        Bitmap.Config.ARGB_8888
                )

        val canvas = Canvas(bitmapResult)
        canvas.drawBitmap(sourceBitmap, 0F, 0F, paint)

        image.setImageBitmap(bitmapResult)
    }
}