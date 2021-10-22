@file:JvmName("AppConstant")

package bluesharklabs.com.medical.utils


import android.R.attr
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.preference.PreferenceManager
import android.util.Base64
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import bluesharklabs.com.medical.R
import com.google.android.gms.location.FusedLocationProviderClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


object AppConstant {

    //  const val BASE_URL = "http://192.168.1.40/medical/api/"
    const val BASE_URL = "http://96.126.124.89/medical/api/"
    const val SHARED_PREF_NAME = "MEDICINEPreference"
    const val IS_LOGIN = "MEDICINE_IS_LOGIN"
    const val CONSTANT_NUMBER = "NUMBER"

    const val PREF_FCM_TOKEN = "fcmToken"
    const val PREF_USER_ID = "userid"
    const val PREF_USER_PROFILE_IMAGE = "user_profile_image"
    const val PREF_USER_PHONE = "user_phone"
    const val PREF_USER_NAME = "user_name"
    const val PREF_USER_ZIPCODE = "zipcode"
    const val PREF_USER_LANDMARK = "landmark"
    const val PREF_USER_BUILDING = "building"
    const val PREF_USER_CITY = "city"
    const val PREF_USER_ADDRESSLINE1 = "address_line1"
    const val PREF_USER_ADDRESSLINE2 = "address_line2"
    const val PREF_STORE_PHONE = "store_phone"
    const val PREF_STORE_NAME = "store_name"


    const val BASE_URL_GOOGLE = "https://maps.googleapis.com"
    const val GOOGLE_PLACE_API = "/maps/api/place/autocomplete/json"

    const val ADD_BILL_API = "party/bill/add"
    const val PAYPAL_CLIENT_ID = "AS2NYVU8Rie2Ofxek29t78RnumNG1JhLNvXei5yf5wH5efNV2IhawgDel2TVyTBxKRmwupUF2SMw2sHW"

    const val DB_NAME = "paytm.medicine"
    const val ORDER_STATUS_COMPLETED = "COMPLETED"
    const val ORDER_STATUS_FAILED = "FAILED"
    const val ORDER_STATUS_PROCESSING = "PROCESSING"

    const val PAYTM_MERCHANT_ID = "M2306160483220675579140" //YOUR TEST MERCHANT ID
    const val url_paytm_callback = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp"
    const val url_base = "https://demo.androidhive.info/paytm/public/api/"

    const val SALT = "8289e078-be0b-484d-ae60-052f117f8deb"
    const val SALT_KEY_INDEX = 1
    const val MERCHANT_ID = "M2306160483220675579140"
    const val DEVICE_TOKEN = "device_token"
    const val DEVICE_ID = "device_id"

    val PERMISSION_ID = 42
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var geocoder: Geocoder? = null
    private var context: Context? = null

    const val REGISTER_API = "buyer_app/buyerRegister"
    const val LOGIN_API = "buyer_app/buyerLogin"
    const val ADD_TOKEN = "common/addDeviceToken"
    const val GET_COLOR_CODE_API = "common/getColorCode"
    const val GET_TERMS_CONDITION_API = "common/getTermsCondition"
    const val ADD_ORDER_API = "order/addOrder"
    const val GET_STORE_TYPE_API = "common/getStoreType"
    const val GET_MERCH_CATEGORY_API = "common/getMerchandiseCategory"
    const val GET_ALL_DROPDOWN_API = "common/getAllDropdown"
    const val ORDER_LIST_API = "order/orderList"
    const val ORDER_DETAIL_API = "order/orderDetail"
    const val GET_STORE_DETAIL_API = "store_app/getStoreDetailByMobile"
    const val GET_STORE_LIST_API = "store_app/getStoreList"
    const val GET_FULL_COVERAGE_ORDERS_API = "buyer_app/getFullCoverageOrders"
    const val BUYER_ORDER_ACCEPT_API = "buyer_app/buyerOrderAccept"
    const val BUYER_ORDER_ACCEPT_LIST_API = "buyer_app/buyerOrderAcceptList"
    const val GET_INVOICE_API = "store_app/getInvoice"
    const val ORDER_DETAIL_FOR_OFFER_API = "order/orderDetailForOffer"
    const val GET_PARTIAL_COVERAGE_ORDERS_API = "buyer_app/getPartialCoverageOrders"
    const val GET_ORDER_TIMELINE_API = "buyer_app/getOrderTimeLine"
    const val VERIFY_PAYMENT_API = "buyer_app/verifyPayment"
    const val GET_STORE_PAYMENT_METHOD_API = "buyer_app/getStorePaymentMethod"
    const val VERIFY_ORDER_API = "buyer_app/verifyOrder"
    const val MAKE_STORE_REVIEW_API = "store_app/makeStoreReview"
    const val GET_BUYER_RATING_API = "buyer_app/getBuyerRating"
    const val SKIP_PAYMENT_BUYER_API = "buyer_app/skipPaymentBuyer"
    const val VERIFICATION_API = "common/sendOtpMessage"
    const val ORDER_DETAIL_FOR_PICKUP_API = "order/orderDetailForStorePicUp"
    const val GET_STORE_OFFER_ORDERS_API = "buyer_app/getStoreOfferOrders"
    const val GET_PRECRIPTION_LIST_API = "Buyer_app/getAllPrescription"
    const val BUYER_DETAIL_API = "Buyer_app/getBuyerDetail"
    const val BUYER_DETAIL_UPDATE_API = "Buyer_app/buyerDetailUpdate"
    const val BUYER_ALL_ORDER_API = "buyer_app/getFilterBuyerOrder"
    const val BUYER_ALL_ORDER_COLORS = "buyer_app/getPartialCoverageOrdersFilter"
    const val BUYER_PARTIAL_FILTER_APPLY = "buyer_app/getPartialCoverageOrdersFilterData"

    //    const val BUYER_ALL_ORDER_API = "Buyer_app/getBuyerOrder"
    const val BUYER_CHECK_API = "Buyer_app/checkBuyer"
    const val COMMON_FILTER_API = "common/getFilterList"
    const val COMMON_NOTIFICATION_API = "common/getAppNotificationList"
    const val ALL_STORE_CITY_API = "store_app/allStoreCity"
    const val ALL_STORE_ZIP_CODE_API = "store_app/allStoreZipcode"
    const val ALL_STORE_AREA_API = "store_app/allStoreArea"
    const val TANDC = "common/getTermsConditions"
    const val SEND_FEEDBACK = "common/addfeedback"
    const val CONTACT_US = "common/getContactUS"
    const val DOCTOR_LIST = "buyer_app/getConsultDoctor"


    var photoFinishBitmap: Bitmap? = null
    var xData: ArrayList<String>? = arrayListOf()
    var yData: ArrayList<String>? = arrayListOf()
    var colorFinalList: ArrayList<String>? = arrayListOf()
    var data: ArrayList<String>? = arrayListOf()


    fun Context.setPreference(key: String, value: Any) {
        val sharedPreference = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
        val editor = sharedPreference.edit()
        when (value) {
            is String -> {
                editor.putString(key, value)
            }
            is Boolean -> {
                editor.putBoolean(key, value)
            }
            is Int -> {
                editor.putInt(key, value)
            }
        }
        editor.apply()
    }

    fun updateStatusBarColor(color: String) {// Color must be in hexadecimal fromat
        /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
             val window = window
             window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
             window.statusBarColor = Color.parseColor(color)
         }*/
    }

    fun setsStatus(
            status1: String
    ): Int {
        if (status1.equals("Pending", ignoreCase = true)) {
            return R.color.st_pending
        } else if (status1.equals("Delivered", ignoreCase = true)) {
            return R.color.st_delivered
        } else if (status1.equals("Offer Made", ignoreCase = true)) {
            return R.color.st_offer_made
        } else if (status1.equals(">24 hrs", ignoreCase = true)) {
            return R.color.st_hrs
        } else if (status1.equals("Active", ignoreCase = true)) {
            return R.color.st_active
        } else if (status1.equals("Invoiced", ignoreCase = true)) {
            return R.color.st_invoiced
        } else if (status1.equals("Accepted", ignoreCase = true)) {
            return R.color.st_accepted
        } else if (status1.equals("Timed Out", ignoreCase = true)) {
            return R.color.st_timed_out
        } else
            return 0
    }

    fun convertDateToTimer(datenTime: String): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US)

        val cal1 = Calendar.getInstance()
        val currDate1 = dateFormat.format(cal1.time)
        cal1.time = dateFormat.parse(datenTime)
        val convertedDate1 = dateFormat.format(cal1.time)

        val stopTime1 = dateFormat.parse(convertedDate1)
        val startTime1 = dateFormat.parse(currDate1)

        val difference1 = startTime1.time - stopTime1.time

        val InSec1 = TimeUnit.MILLISECONDS.toSeconds(difference1) % 60
        val day1 = TimeUnit.MILLISECONDS.toDays(difference1)
        val hh1 = (TimeUnit.MILLISECONDS.toHours(difference1) - TimeUnit.DAYS.toHours(day1))
        /* if (!day1.equals("0")){
             hh1 = day1 * 24
         }*/

        val mm1 = (TimeUnit.MILLISECONDS.toMinutes(difference1) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(difference1)
        ))

        return "Time Elapsed: " + day1 + "d " + +hh1 + "h " + mm1 + "m " + InSec1 + "s"
    }

    fun convertdatentimetoOrderTime(datenTime: String): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US)
        val createdConvertedDate = dateFormat.parse(datenTime)

        return SimpleDateFormat("dd MMM,yyyy hh:mm a", Locale.US).format(createdConvertedDate)

    }

    fun convertdatentimetoOrderTime1(datenTime: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        val createdConvertedDate = dateFormat.parse(datenTime)

        return SimpleDateFormat("dd MMM,yyyy hh:mm a", Locale.US).format(createdConvertedDate)

    }

    fun convertDelDate(from_date: String): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US)
        val createdConvertedDate = dateFormat.parse(from_date)

        return SimpleDateFormat("dd/mm/yyyy  hh:mm a").format(createdConvertedDate)

        // return SimpleDateFormat("dd/mm/yyyy , yyyy").format(createdConvertedDate)
    }

    fun convertFromDelDate(from_date: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val createdConvertedDate = dateFormat.parse(from_date)

        return SimpleDateFormat("dd MMM, yyyy").format(createdConvertedDate)
    }

    fun convertDelDate1(from_date: String): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US)
        val createdConvertedDate = dateFormat.parse(from_date)

        return SimpleDateFormat("dd MMM,yyyy").format(createdConvertedDate) + " " + SimpleDateFormat("hh:mm a").format(createdConvertedDate).toLowerCase()
    }

    fun convertTime2(startTime: String, endTime: String): String {
        val dateFormat1 = SimpleDateFormat("HH:mm:ss", Locale.US)
        val createdConvertedTime = dateFormat1.parse(startTime)
        val createdConvertedTime1 = dateFormat1.parse(endTime)
        return SimpleDateFormat("hh:mm a").format(createdConvertedTime)
    }

    fun convertDelDate3(from_date: String): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
        val createdConvertedDate = dateFormat.parse(from_date)

        return SimpleDateFormat("MMM dd, yyyy").format(createdConvertedDate)
    }

    fun convertDelDate2(from_date: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val createdConvertedDate = dateFormat.parse(from_date)

        return SimpleDateFormat("MMM dd, yyyy").format(createdConvertedDate)
    }

    fun convertFromtoTime(startTime: String, endTime: String): String {
        val dateFormat1 = SimpleDateFormat("HH:mm:ss", Locale.US)
        val createdConvertedTime = dateFormat1.parse(startTime)
        val createdConvertedTime1 = dateFormat1.parse(endTime)
        return SimpleDateFormat("hh:mm a").format(createdConvertedTime).toLowerCase() + " to " + SimpleDateFormat("hh:mm a").format(createdConvertedTime1
        ).toLowerCase()
    }

    fun convertTime(startTime: String, endTime: String): String {
        val dateFormat1 = SimpleDateFormat("HH:mm:ss", Locale.US)
        val createdConvertedTime = dateFormat1.parse(startTime)
        val createdConvertedTime1 = dateFormat1.parse(endTime)
        return SimpleDateFormat("hh:mm a").format(createdConvertedTime).toLowerCase() + " to " + SimpleDateFormat("hh:mm a").format(createdConvertedTime1).toLowerCase()
    }

    fun convert(bitmap: Bitmap): String {

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val encoded = Base64.encodeToString(byteArray, Base64.DEFAULT)

        return encoded

//        val outputStream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
    }

    fun Context.getPreferenceInt(key: String): Int =
            getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).getInt(key, 0)

    fun Context.getPreferenceString(key: String): String =
            getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).getString(key, "")!!

    fun Context.getPreferenceBoolean(key: String): Boolean =
            getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).getBoolean(key, false)

    fun remove(key: String) {
        val sharedPreference =
                PreferenceManager.getDefaultSharedPreferences(AppController.instance?.appContext)
        val editor = sharedPreference.edit()
        editor.remove(key)
        editor.apply()

    }

    fun setPreferenceText(key: String, value: String) {
        val sharedPreference =
                PreferenceManager.getDefaultSharedPreferences(AppController.instance?.appContext)
        //  SharedPreferences sharedPreference = AppController.getInstance().getAppContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        val editor = sharedPreference.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getPreferenceText(key: String): String {
        val sharedPreference =
                PreferenceManager.getDefaultSharedPreferences(AppController.instance?.appContext)
        // SharedPreferences sharedPreference = AppController.getInstance().getAppContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedPreference.getString(key, "")
    }

    fun setPreferenceBoolean(key: String, value: Boolean) {
        val sharedPreference =
                PreferenceManager.getDefaultSharedPreferences(AppController.instance?.appContext)
        // SharedPreferences sharedPreference = AppController.getInstance().getAppContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        val editor = sharedPreference.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getPreferenceBoolean(key: String): Boolean {
        val sharedPreference =
                PreferenceManager.getDefaultSharedPreferences(AppController.instance?.appContext)
        // SharedPreferences sharedPreference = AppController.getInstance().getAppContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedPreference.getBoolean(key, false)
    }

    fun hideSoftKeyBoard(context: Context, view: View) {
        try {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (e: Exception) {
            // TODO: handle exception
            e.printStackTrace()
        }

    }

    fun urlToBitmap(prescriptionImage: String) {
        val url = URL(prescriptionImage)
        val connection: HttpURLConnection
        connection = url.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        val input = connection.inputStream
        val myBitmap = BitmapFactory.decodeStream(input)
        photoFinishBitmap = myBitmap


    }

    var retrofit: Retrofit? = null

    fun getClient(): Retrofit? {

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY


        val client = OkHttpClient().newBuilder()
                .connectTimeout(100000, TimeUnit.SECONDS)
                .readTimeout(100000, TimeUnit.SECONDS)
                .writeTimeout(100000, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(loggingInterceptor)
                .build()


        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        return retrofit
    }


    val String.isValidEmail: Boolean
        get() = Patterns.EMAIL_ADDRESS.matcher(this).matches()


    fun Context.removePreference(key: String) {
        val sharedPreference = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.remove(key).apply()

    }

    fun isEmpty(s: String?): Boolean {
        var b = true
        if (s != null && !s.isEmpty()) {
            b = false
        }
        return b


    }

}





