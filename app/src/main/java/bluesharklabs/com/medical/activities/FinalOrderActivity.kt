package bluesharklabs.com.medical.activities

import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.StrictMode
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.activities.FindPlatformActivity.Companion.finalCity
import bluesharklabs.com.medical.activities.FindPlatformActivity.Companion.finalZipcode
import bluesharklabs.com.medical.activities.FindPlatformActivity.Companion.final_md_id
import bluesharklabs.com.medical.activities.FindPlatformActivity.Companion.final_type_id
import bluesharklabs.com.medical.activities.FindPlatformActivity.Companion.locationCriteria
import bluesharklabs.com.medical.activities.FindPlatformActivity.Companion.locationCriteriaDelivery
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.blockNum
import bluesharklabs.com.medical.activities.OrderPreferenceActivity.Companion.buildName
import bluesharklabs.com.medical.activities.PrescriptionOrderActivity.Companion.array
import bluesharklabs.com.medical.activities.ViewOfferActivity.Companion.customOrderdata
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.AddOrder
import bluesharklabs.com.medical.model.OrderDetail
import bluesharklabs.com.medical.response.OrderDetailResponse
import bluesharklabs.com.medical.utils.AppConstant
import bluesharklabs.com.medical.utils.AppConstant.photoFinishBitmap
import bluesharklabs.com.medical.utils.Utils.Companion.GettingMiliSeconds
import bluesharklabs.com.medical.utils.afterMeasured
import kotlinx.android.synthetic.main.activity_custom_final_order.*
import kotlinx.android.synthetic.main.activity_final_order.*
import kotlinx.android.synthetic.main.activity_final_order.btn_review_ordr
import kotlinx.android.synthetic.main.activity_final_order.btn_sendtostor
import kotlinx.android.synthetic.main.activity_final_order.con_toolbar
import kotlinx.android.synthetic.main.activity_final_order.dellivery_date
import kotlinx.android.synthetic.main.activity_final_order.edt_delivery_prefrnc
import kotlinx.android.synthetic.main.activity_final_order.edt_order
import kotlinx.android.synthetic.main.activity_final_order.edt_order_prefrnc
import kotlinx.android.synthetic.main.activity_final_order.edt_prefer_scedule
import kotlinx.android.synthetic.main.activity_final_order.img_back
import kotlinx.android.synthetic.main.activity_final_order.img_prescr
import kotlinx.android.synthetic.main.activity_final_order.progressBar
import kotlinx.android.synthetic.main.activity_final_order.txt
import kotlinx.android.synthetic.main.activity_final_order.txt1
import kotlinx.android.synthetic.main.activity_final_order.txt_delivery_date
import kotlinx.android.synthetic.main.activity_final_order.txt_delivery_prefrnc
import kotlinx.android.synthetic.main.activity_final_order.txt_geo_location
import kotlinx.android.synthetic.main.activity_final_order.txt_order_dtntime
import kotlinx.android.synthetic.main.activity_final_order.txt_order_id
import kotlinx.android.synthetic.main.activity_final_order.txt_ordering_for
import kotlinx.android.synthetic.main.activity_final_order.txt_ordr_prefrnc
import kotlinx.android.synthetic.main.activity_final_order.txt_title
import kotlinx.android.synthetic.main.activity_final_order.txt_title_delivery_prefrnc
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

class FinalOrderActivity : AppCompatActivity(), View.OnClickListener {


    var viewWidth = ""
    var viewHeight = ""
    var imgAttchmentWidth = ""
    var imgAttchmentHeight = ""
    var latitude = ""
    var longitude = ""
    var store_latitude = ""
    var store_longitude = ""

    var isDetail = false
    var isEditable = false

    var bmCon: Bitmap? = null
    var iv: ImageView? = null
    var childcount: Int = 0
    private var isReorder = "1"
    private var timer = ""
    private var orderId = ""
    private var offerId = ""
    private var OrderType = ""
    private var OrderTypeId = ""
    var hashMap: HashMap<Int, Int> = HashMap<Int, Int>()

    var cTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//  set status text dark

        setContentView(R.layout.activity_final_order)

        Log.e("FINALORDERSS","FinalOrderActivity")


        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)



        img_prescr.setOnClickListener(this)
        button.setOnClickListener(this)
        btn_sendtostor.setOnClickListener(this)
        img_back.setOnClickListener(this)
        btn_review_ordr.setOnClickListener(this)
        edt_img.setOnClickListener(this)
        edt_order.setOnClickListener(this)
        edt_order_prefrnc.setOnClickListener(this)
        edt_delivery_prefrnc.setOnClickListener(this)
        edt_prefer_scedule.setOnClickListener(this)
        txt_geo_location.setOnClickListener(this)

        if (intent != null) {

            if (intent.getStringExtra("offerId") != null) {


                offerId = intent.getStringExtra("offerId")
            }
            if (intent.getStringExtra("reditorder") != null) {
                setLayoutEdtUploadAgain()
            }
            if (intent.getStringExtra("reorder") != null) {


                isReorder = intent.getStringExtra("reorder")
                setLayout()
            }

            if (intent.getStringExtra("Timer") != null) {
                timer = intent.getStringExtra("Timer")
                txt.text = timer
            }

            if (intent.getStringExtra("orderid") != null) {
                orderId = intent.getStringExtra("orderid")
                orderDetailApi()
            } else {
                init()
            }
            if (intent.getStringExtra("vieworder") != null) {
                viewOrder()
                //  init()
            }
        }
    }


    fun init() {

        /* if (!customOrderdata!!.createdDate!!.equals("")) {
             txt_order_id.text = "ID: " + customOrderdata?.orderNo
             txt_order_dtntime.text =
                 AppConstant.convertdatentimetoOrderTime(customOrderdata!!.createdDate!!)
         }*/

        txt_ordering_for.text = OrderingForActivity.orderForTxt
        OrderType = OrderingForActivity.orderForTxt.toString()
        OrderTypeId = OrderingForActivity.orderFor.toString()
        txt_title_delivery_prefrnc.text = OrderPreferenceActivity.delprerncetext

        if (OrderPreferenceActivity.drugPrefrncTxt.equals("")) {
            txt_ordr_prefrnc.text = OrderPreferenceActivity.buyingPrefrncTxt
        } else {
            txt_ordr_prefrnc.text = OrderPreferenceActivity.drugPrefrncTxt
        }

        Log.e("HelloReOrde", "txt_delivery_date  Final  " + OrderPreferenceActivity.todate_fromdate)
        txt_delivery_date.text = OrderPreferenceActivity.todate_fromdate

        Log.d("TAG", "startTimeFormat-------" + OrderPreferenceActivity.startTimeFormat)

        //  txt_delivery_time.text =((OrderPreferenceActivity.startTimeFormat).toLowerCase()+" to "+(OrderPreferenceActivity.endTimeFormat).toLowerCase())

        if (OrderPreferenceActivity.delType.equals("0")) {
            dellivery_date.text = resources.getString(R.string.pickup_date)
            //  delivery_time.text = resources.getString(R.string.pickup_time)
            //prefer_scedule.text = resources.getString(R.string.pickup_schedule)
            txt_delivery_prefrnc.visibility = View.GONE

        } else if (OrderPreferenceActivity.delType.equals("1")) {
            txt_geo_location.visibility = View.VISIBLE
            val locaTion = blockNum + ", " + buildName
            txt_geo_location.text = OrderPreferenceActivity.locaTion
            txt_title_delivery_prefrnc.text = OrderPreferenceActivity.delprerncetext + " in "+ OrderPreferenceActivity.delivery_city
            latitude = OrderPreferenceActivity.order_lat
            longitude = OrderPreferenceActivity.order_long
            store_latitude = OrderPreferenceActivity.store_lat
            store_longitude = OrderPreferenceActivity.order_long
            txt_delivery_prefrnc.visibility = View.VISIBLE
            txt_delivery_prefrnc.text = locaTion
        } else {
            txt_title_delivery_prefrnc.text = OrderPreferenceActivity.delprerncetext + " in "+ OrderPreferenceActivity.delivery_city
            val locaTion =
                    blockNum + ", " + buildName + ", " + OrderPreferenceActivity.streeT + ", " +
                            OrderPreferenceActivity.area + ", " + OrderPreferenceActivity.landMark + ", " + OrderPreferenceActivity.zipCode

            txt_delivery_prefrnc.visibility = View.VISIBLE
            txt_delivery_prefrnc.text = locaTion
        }

        bmCon = photoFinishBitmap
        if (bmCon != null) {
            // img_prescr.setImageDrawable(BitmapDrawable(resources, bmCon))
            rel.visibility = View.VISIBLE
            bmp = scaleBitmap(bmCon!!)
            img_prescr.setImageDrawable(BitmapDrawable(resources, bmp))

            rel.afterMeasured {
                viewWidth = rel.width.toString()
                viewHeight = rel.height.toString()
                addTags()
            }
            //  addTaggs()
        }
    }

    private fun scaleBitmap(bmp: Bitmap): Bitmap {
        var bm = bmp
        var width = bm.width
        var height = bm.height
        imgAttchmentWidth = width.toString()
        imgAttchmentHeight = height.toString()
        Log.v("Pictures", "Width and height are $width--$height")
        Log.v("Pictures", "WidthHeight" + con_toolbar.height)

        val rectangle = Rect()
        val window = getWindow()
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle)
        val statusBarHeight = rectangle.top

        var maxWidth = Resources.getSystem().getDisplayMetrics().widthPixels
        var maxHeight =
                Resources.getSystem().getDisplayMetrics().heightPixels - con_toolbar.height - constraint.height
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

        return bm
    }

    fun addTags() {


        for (i in 0 until array.size) {

            iv = ImageView(this)

            val bm: Bitmap?

            bm = BitmapFactory.decodeResource(resources, R.drawable.pin)

            if (bm == null) {
                Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show()
            } else {
                iv!!.setImageBitmap(bm)
                //  iv!!.setPadding(3, 3, 3, 3)

                val params = RelativeLayout.LayoutParams(bm.width, bm.height)

                childcount = rel.getChildCount()

                /* params.leftMargin = array.get(i).product_x_position!!.toFloat().toInt()
                 params.topMargin = array.get(i).product_y_position!!.toFloat().toInt()*/

                Log.d("TAG", "ID--left--> " + array.get(i).product_x_position!!.toFloat().toInt())
                Log.d("TAG", "ID--right-> " + array.get(i).product_y_position!!.toFloat().toInt())

                viewWidth = rel.width.toString()
                viewHeight = rel.height.toString()

                imgAttchmentWidth = photoFinishBitmap!!.width.toString()
                imgAttchmentHeight = photoFinishBitmap!!.height.toString()

                Log.d("TAG", "ID--viewWidth--> $viewWidth && $viewHeight")
                Log.d("TAG", "ID--iviewWidth--> $imgAttchmentWidth && $imgAttchmentHeight")
                // Log.d("TAG", "ID--OriginviewWidth--> $OriginX && $OriginY")
                //  Log.d("TAG", "ID--OiginviewWidth--> $touchX && $touchY")


                params.leftMargin =
                        (viewWidth.toInt() *
                                array.get(i).product_x_position!!.toFloat().toInt()) /
                                imgAttchmentWidth.toInt()
                params.topMargin =
                        (viewHeight.toInt() * array.get(i).product_y_position!!.toFloat().toInt()) / imgAttchmentHeight.toInt()


                Log.d("TAG", "ID--top--> " + params.leftMargin)
                Log.d("TAG", "ID--bottom-> " + params.topMargin)
                hashMap.put(
                        array.get(i).product_x_position!!.toInt(),
                        array.get(i).product_y_position!!.toInt()
                )

                rel.addView(iv, params)

                // for (i in 0 until childcount) {
                val v = rel.getChildAt(i)
                changeBitmapColor(bm, iv!!, Color.parseColor("#" + array.get(i).color))
                // }
            }
        }
    }

    /*  override fun onWindowFocusChanged(hasFocus: Boolean) {
          super.onWindowFocusChanged(hasFocus)

          if (hasFocus) {
              viewWidth = rel.width.toString()
              viewHeight = rel.height.toString()
              *//* if (!viewWidth.equals("0")){
            addTaggs()
        }*//*
            if (!viewWidth.equals("0") && isDetail) {
                addTags()

                Log.d("tag", """lkglldgj""")
            }

        }
    }
*/
    private fun changeBitmapColor(sourceBitmap: Bitmap, image: ImageView, color: Int) {

        /* val resultBitmap = Bitmap.createBitmap(
             sourceBitmap, 0, 0,
             sourceBitmap.width - 1, sourceBitmap.height - 1
         )
         val p = Paint()
         val filter = LightingColorFilter(color.toInt(), 1)
         p.setColorFilter(filter)
         image.setImageBitmap(resultBitmap)

         val canvas = Canvas(resultBitmap)
         canvas.drawBitmap(resultBitmap, 0F, 0F, p)*/

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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txt_geo_location -> {
                Log.e("hellmakd", "Store_LAt   " + store_latitude + "   Store_Long   " + store_longitude+"   LAt   " + latitude + "   Long   " + longitude)

                if (!store_latitude.equals("")) {

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + latitude + "," + longitude + "&daddr=" + OrderPreferenceActivity.store_lat + "," + OrderPreferenceActivity.store_long))
                    startActivity(intent)
                } else {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + latitude + "," + longitude))
                    startActivity(intent)
                }


            }
            R.id.img_back -> {
                finish()
            }
            R.id.img_prescr -> {
                var edit = ""
                if (!isReorder.equals("1")) {

                    edit = "0"
                } else {
                    edit = "1"
                }



                if (intent != null && intent.getStringExtra("vieworder") != null) {
                    startActivity(
                            Intent(this@FinalOrderActivity, ImgFullscreenActivity::class.java)
                                    .putExtra("vieworder", "1")

                    )
                    Log.e("HElloBoy", "ViewOrder  ")
                    //finish()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                } else {
                    Log.e("HElloBoy", "ViewOrder EDIT ")
                    startActivity(
                            Intent(
                                    this@FinalOrderActivity,
                                    ImgFullscreenActivity::class.java
                            ).putExtra("edit", edit).putExtra("isEditable", isEditable)
                    )
                    //finish()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
            R.id.button -> {
                //send_to_platform
                startActivity(Intent(this@FinalOrderActivity, FindPlatformActivity::class.java))
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.btn_sendtostor -> {
                //send_to_store
                startActivity(Intent(this@FinalOrderActivity, FindStoreActivity::class.java))
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.btn_review_ordr -> {

                txt_title.setText(R.string.reorder)
                button.visibility = View.VISIBLE
                btn_sendtostor.visibility = View.VISIBLE
                btn_review_ordr.visibility = View.GONE

                /* //send_to_store
                 startActivity(Intent(this@FinalOrderActivity, FindStoreActivity::class.java))
                 //finish()
                 overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)*/
            }
            R.id.edt_img -> {
                Log.e("HelloEdit", "Edit CLicked")
                startActivity(
                        Intent(this@FinalOrderActivity, ImgFullscreenActivity::class.java)
                                .putExtra("edit", "1")
                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.edt_order -> {
                startActivity(
                        Intent(
                                this@FinalOrderActivity,
                                OrderingForActivity::class.java
                        )
                                .putExtra("edit", true)
                                .putExtra("custom", false)
                                .putExtra("ORDER_FOR", OrderType)
                                .putExtra("ORDER_ID", OrderTypeId)
                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.edt_order_prefrnc -> {
                startActivity(
                        Intent(
                                this@FinalOrderActivity,
                                OrderPreferenceActivity::class.java
                        ).putExtra("edit", "1")
                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.edt_delivery_prefrnc -> {
                startActivity(
                        Intent(
                                this@FinalOrderActivity,
                                OrderPreferenceActivity::class.java
                        ).putExtra("edit", "1")
                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.edt_prefer_scedule -> {
                startActivity(
                        Intent(
                                this@FinalOrderActivity,
                                OrderPreferenceActivity::class.java
                        ).putExtra("edit", "1")
                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

    private fun setLayoutEdtUploadAgain() {
        txt_title.setText(R.string.reorder)
        button.visibility = View.VISIBLE
        btn_sendtostor.visibility = View.VISIBLE
        txt.visibility = View.GONE
        txt1.visibility = View.GONE
        edt_img.visibility = View.VISIBLE
        edt_order.visibility = View.VISIBLE
        edt_prefer_scedule.visibility = View.VISIBLE
        btn_review_ordr.visibility = View.VISIBLE
    }

    private fun setLayout() {
        if (!isReorder.equals("1")) {
            button.visibility = View.GONE
            btn_sendtostor.visibility = View.GONE
            txt.visibility = View.VISIBLE
            txt.setText(isReorder)
            edt_img.visibility = View.INVISIBLE
            edt_order.visibility = View.INVISIBLE
            edt_prefer_scedule.visibility = View.INVISIBLE
            txt1.visibility = View.VISIBLE
            btn_review_ordr.visibility = View.GONE
        } else {
            txt_title.setText(R.string.reorder)
            button.visibility = View.GONE
            btn_sendtostor.visibility = View.GONE
            txt.visibility = View.GONE
            txt1.visibility = View.GONE
            edt_img.visibility = View.VISIBLE
            edt_order.visibility = View.VISIBLE
            edt_prefer_scedule.visibility = View.VISIBLE
            btn_review_ordr.visibility = View.VISIBLE
        }

    }

    private fun viewOrder() {

        txt_title.visibility = View.GONE
        button.visibility = View.GONE
        btn_sendtostor.visibility = View.GONE
        btn_review_ordr.visibility = View.GONE
        txt1.visibility = View.GONE
        txt.visibility = View.GONE

        edt_order_prefrnc.visibility = View.INVISIBLE
        edt_order.visibility = View.INVISIBLE
        edt_delivery_prefrnc.visibility = View.INVISIBLE
        edt_prefer_scedule.visibility = View.INVISIBLE
        edt_img.visibility = View.INVISIBLE

        txt_order_id.visibility = View.VISIBLE
        txt_order_dtntime.visibility = View.VISIBLE
        view7.visibility = View.VISIBLE
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
            override fun onResponse(
                    call: Call<OrderDetailResponse>,
                    response: retrofit2.Response<OrderDetailResponse>
            ) {
                progressBar!!.visibility = View.GONE
                if (response.isSuccessful) {

                    isDetail = true
                    customOrderdata = response.body()?.data!!

                    Log.d("TAG", "data....." + response.body()?.data!!)
                    Log.d("TAG", "customOrderdata Response....." + customOrderdata!!.products!!.get(0).productMrp)
                    Log.e("TAGsdsdsd", "StoreLatLong....." + customOrderdata!!.store_latitude)

                    PrescriptionOrderActivity.array = arrayListOf()

                    OrderingForActivity.orderForTxt = customOrderdata?.orderForName!!
                    OrderType = customOrderdata?.orderForName!!
                    OrderTypeId = customOrderdata?.orderFor!!




                    OrderPreferenceActivity.startTime = customOrderdata?.startTime!!
                    OrderPreferenceActivity.endTime = customOrderdata?.endTime!!
                    //coverrtdate(customOrderdata?.deliveryDate!!,customOrderdata?.startTime!!,customOrderdata?.endTime!!)

                    OrderPreferenceActivity.delDate = customOrderdata?.deliveryDate!!
                    OrderPreferenceActivity.delDateFormat = customOrderdata?.deliveryDate!!
                    OrderPreferenceActivity.toDeliveryDate = customOrderdata?.toDeliveryDate!!
                    OrderPreferenceActivity.toDateFormat = customOrderdata?.toDeliveryDate!!
                    OrderPreferenceActivity.startTimeFormat = customOrderdata?.startTime!!
                    OrderPreferenceActivity.endTimeFormat = customOrderdata?.endTime!!

                    OrderPreferenceActivity.delprerncetext = customOrderdata?.deliveryTypeName!!
                    OrderPreferenceActivity.delType = customOrderdata?.deliveryType!!
                    OrderPreferenceActivity.delivery_city = customOrderdata?.deliveryCity!!



                    if (customOrderdata?.orderPreferenceName != null &&
                            !customOrderdata?.orderPreferenceName!!.equals("")) {
                        OrderPreferenceActivity.drugPrefrncTxt = customOrderdata?.orderPreferenceName!!
                        OrderPreferenceActivity.drugPrefrnc = customOrderdata?.orderPreference!!
                    } else {
                        OrderPreferenceActivity.buyingPrefrncTxt = customOrderdata?.buyingPreferenceName!!
                        OrderPreferenceActivity.buyingPrefrnc = customOrderdata?.buyingPreference!!
                    }
                    OrderPreferenceActivity.locaTion = customOrderdata?.location!!
                    OrderingForActivity.orderForTxt = customOrderdata?.orderForName!!

                    OrderType = customOrderdata?.orderForName!!
                    OrderTypeId = customOrderdata?.orderFor!!

                    OrderingForActivity.orderFor = customOrderdata?.orderFor!!
                    OrderPreferenceActivity.photoId = customOrderdata?.photoId!!
                    OrderPreferenceActivity.area = customOrderdata?.area!!
                    OrderPreferenceActivity.store_lat = customOrderdata?.store_latitude!!
                    OrderPreferenceActivity.store_long = customOrderdata?.store_longitude!!
                    OrderPreferenceActivity.order_lat = customOrderdata?.orderLatitude!!
                    OrderPreferenceActivity.order_long = customOrderdata?.orderLongitude!!
                    OrderPreferenceActivity.todate_fromdate = customOrderdata?.todateFromdate!!
                    blockNum = customOrderdata?.blockNo!!
                    buildName = customOrderdata?.buildingName!!
                    locationCriteriaDelivery = customOrderdata?.locationCriteriaDeliveryLocation!!
                    locationCriteria = customOrderdata?.locationCriteriaLocation!!
                    OrderPreferenceActivity.landMark = customOrderdata?.landmark!!
                    OrderPreferenceActivity.del_lat = customOrderdata?.deliveryLatitude!!
                    OrderPreferenceActivity.del_long = customOrderdata?.deliveryLongitude!!
                    OrderPreferenceActivity.city = customOrderdata?.storeCriteriaCity!!
                    OrderPreferenceActivity.zipCode = customOrderdata?.zipcode!!
                    final_md_id = customOrderdata?.storeCriteriaStoreMdId!!
                    final_type_id = customOrderdata?.storeCriteriaStoreTypeId!!
                    finalZipcode = customOrderdata?.storeCriteriaZipcode!!
                    finalCity = customOrderdata?.storeCriteriaCity!!
                    OrderPreferenceActivity.turnOnLocation = customOrderdata?.turnOnLocation!!
                    OrderPreferenceActivity.streeT = customOrderdata?.street!!

                    Log.e("HElfmkodsgoh", "PRI   Store_LAT   " + customOrderdata?.store_latitude!!+"   Store_LOGI   " + customOrderdata?.store_longitude!!+"   Lat   " + customOrderdata?.deliveryLatitude!!+"   Long   " + customOrderdata?.deliveryLongitude!!+"   Latssss   " + customOrderdata?.orderLatitude!!+"   LOTIII   " + customOrderdata?.orderLongitude!!)


                    txt_order_id.text = "ID: " + customOrderdata?.orderNo

                    txt_order_dtntime.text = (customOrderdata?.createdDate!!)
                    // AppConstant.convertdatentimetoOrderTime(customOrderdata!!.createdDate!!).toLowerCase()

                    for (i in 0 until response.body()?.data!!.products!!.size) {

                        val products = AddOrder.Products()

                        products.color = response.body()?.data!!.products!!.get(i).color
                        products.quantity =
                                response.body()?.data!!.products!!.get(i).quantity
                        products.quantity_type =
                                response.body()?.data!!.products!!.get(i).quantityType
                        products.product_id =
                                response.body()?.data!!.products!!.get(i).productId
                        products.product_y_position =
                                response.body()?.data!!.products!!.get(i).productyposition
                        products.product_x_position =
                                response.body()?.data!!.products!!.get(i).productxposition


                        PrescriptionOrderActivity.array.add(products)

                    }

                    if (!customOrderdata?.prescriptionImage!!.equals("")) {
                        urlToBitmap(customOrderdata?.prescriptionImage!!)
                    }

                    /*  Glide.with(applicationContext)  //2
                          .load(customOrderdata?.prescriptionImage) //3
                          .placeholder(R.drawable.ic_launcher_background) //5
                        .error(R.drawable.ic_launcher_background) //6
                        .fallback(R.drawable.ic_launcher_background) //7
                        .into(img_prescr) //8*/

                    init()

//                    if (!response.body()?.data!!.requestStatus!!.equals("Timed out", true)) {
                    Log.e("HelloStatus", "Status STATUS  " + response.body()?.data!!.requestStatus!!)

                    isEditable = false
                    button.visibility = View.GONE
                    btn_sendtostor.visibility = View.GONE
                    btn_review_ordr.visibility = View.GONE
                    txt1.visibility = View.VISIBLE
                    txt.visibility = View.VISIBLE
                    edt_img.visibility = View.INVISIBLE
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
                            button.visibility = View.GONE
                            btn_sendtostor.visibility = View.GONE
                            btn_review_ordr.visibility = View.VISIBLE
                            txt1.visibility = View.GONE
                            txt.visibility = View.GONE
                            edt_img.visibility = View.VISIBLE
                            edt_order.visibility = View.VISIBLE
                            edt_prefer_scedule.visibility = View.VISIBLE
                            edt_order_prefrnc.visibility = View.VISIBLE
                            edt_delivery_prefrnc.visibility = View.VISIBLE
                        }
                    }
                    (cTimer as CountDownTimer).start()

//                    } else {
//                        btn_sendtostor.visibility = View.GONE
//                        btn_review_ordr.visibility = View.VISIBLE
//                        txt1.visibility = View.GONE
//                        txt.visibility = View.GONE
//                        edt_order.visibility = View.VISIBLE
//                        edt_prefer_scedule.visibility = View.VISIBLE
//                        edt_order_prefrnc.visibility = View.VISIBLE
//                        edt_delivery_prefrnc.visibility = View.VISIBLE
//                    }
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@FinalOrderActivity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@FinalOrderActivity, e.message, Toast.LENGTH_LONG)
                                .show()
                    }

                }
            }


            override fun onFailure(call: Call<OrderDetailResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar!!.visibility = View.GONE
            }
        })
    }

    //cancel timer
    fun cancelTimer() {
        if (cTimer != null) cTimer!!.cancel()
    }

    private fun addTaggs() {


        Handler().postDelayed({
            if (!viewWidth.equals("0") && isDetail) {
                addTags()

            }
        }, 5000)


    }

    private fun coverrtdate(from_date: String, startTime: String, endTime: String) {
        val dateFormat1 = SimpleDateFormat("HH:mm:ss", Locale.US)
        val createdConvertedSTime = dateFormat1.parse(startTime)
        val createdConvertedETime = dateFormat1.parse(endTime)
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        if (!from_date.equals("")) {
            val createdConvertedDate = dateFormat.parse(from_date)
            OrderPreferenceActivity.delDate =
                    SimpleDateFormat("yyyy-MM-dd",Locale.US).format(createdConvertedDate)
            OrderPreferenceActivity.delDateFormat = SimpleDateFormat("dd MMM, yyyy",Locale.US).format(createdConvertedDate)
            OrderPreferenceActivity.startTimeFormat = SimpleDateFormat("hh:mm a",Locale.US).format(createdConvertedSTime).toString()
            OrderPreferenceActivity.endTimeFormat = SimpleDateFormat("hh:mm a",Locale.US).format(createdConvertedETime).toString()
        }
    }

    fun urlToBitmap(prescriptionImage: String) {
        val url = URL(prescriptionImage)
        val connection: HttpURLConnection
        connection = url.openConnection() as HttpURLConnection
        connection.setDoInput(true)
        connection.connect()
        val input = connection.getInputStream()
        val myBitmap = BitmapFactory.decodeStream(input)

        photoFinishBitmap = myBitmap


    }

    private fun showPictureialog() {

        val dialog = Dialog(this)
        //  dialog.setContentView(R.layout.custom_dialog_final_order)
        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)

        val btnsendtstor = dialog.findViewById(R.id.btn_sendtostor) as AppCompatButton
        val btnsendtplatfrm = dialog.findViewById(R.id.button) as AppCompatButton
        val btnreviewordr = dialog.findViewById(R.id.btn_review_ordr) as AppCompatButton
        val txt1 = dialog.findViewById(R.id.txt1) as AppCompatTextView
        val txt = dialog.findViewById(R.id.txt) as AppCompatTextView


        val edt = dialog.findViewById(R.id.edt_order_prefrnc) as AppCompatTextView
        val edt1 = dialog.findViewById(R.id.edt_order) as AppCompatTextView
        val edt2 = dialog.findViewById(R.id.edt_delivery_prefrnc) as AppCompatTextView
        val edt3 = dialog.findViewById(R.id.edt_prefer_scedule) as AppCompatTextView


        btnsendtplatfrm.setOnClickListener {
            txt.visibility = View.VISIBLE
            txt1.visibility = View.VISIBLE
            btnreviewordr.visibility = View.GONE
            btnsendtplatfrm.visibility = View.GONE
            btnsendtstor.visibility = View.GONE


            edt_img.visibility = View.INVISIBLE
            edt.visibility = View.INVISIBLE
            edt1.visibility = View.INVISIBLE
            edt2.visibility = View.INVISIBLE
            edt3.visibility = View.INVISIBLE
        }

        btnsendtstor.setOnClickListener {
            txt.visibility = View.GONE
            txt1.visibility = View.GONE
            btnreviewordr.visibility = View.VISIBLE
            btnsendtplatfrm.visibility = View.GONE
            btnsendtstor.visibility = View.GONE
        }

        btnreviewordr.setOnClickListener {

        }

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.BOTTOM
        lp.windowAnimations = R.style.DialogAnimation
        dialog.window!!.attributes = lp
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    companion object {
        var bmp: Bitmap? = null
        // var data: OrderDetailResponse.Data? = null
    }
}