package bluesharklabs.com.medical.activities

import android.Manifest
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.adapters.MakePaymentAdapter
import bluesharklabs.com.medical.interfaces.ApiInterface
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.GetStorePayment
import bluesharklabs.com.medical.model.SkipPaymentBuyer
import bluesharklabs.com.medical.model.VerifyPayment
import bluesharklabs.com.medical.response.GetStorePaymentResponse
import bluesharklabs.com.medical.response.SkipPaymentBuyerResponse
import bluesharklabs.com.medical.response.VerifyPaymentResponse
import bluesharklabs.com.medical.utils.AppConstant
import bluesharklabs.com.medical.utils.AppConstant.MERCHANT_ID
import bluesharklabs.com.medical.utils.AppConstant.url_base
import bluesharklabs.com.medical.utils.CacheUtils
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.activity_make_payment.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*
import java.util.*


class MakePaymentActivity : AppCompatActivity(), View.OnClickListener,
        MakePaymentAdapter.Paymentmode, MakePaymentAdapter.QrDownload {
    // PaytmPaymentTransactionCallback {

    val PAYPAL_REQUEST_CODE = 123
    val DEBIT_REQUEST_CODE = 777
    private val LOAD_PAYMENT_DATA_REQUEST_CODE = 991

    //  var profileRequest: TransactionRequest? = null
    private var paymentAmount: String? = null
    val PHONEPE_PACKAGE_NAME = "com.phonepe.app"
    var apiServicePaytm: ApiInterface? = null
    var custid: String = ""
    var bitmap: Bitmap? = null

    var payymntName = ""
    var payymntId = ""
    var orderId: String = ""
    var offerId: String = ""
    var storeId: String = ""
    private var recycler: RecyclerView? = null
    private var checkbox: CheckBox? = null
    private var makePaymentAdapter: MakePaymentAdapter? = null
    var paymentListArray: List<GetStorePaymentResponse.Payment> = arrayListOf()
    // private lateinit var paymentsClient: PaymentsClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//  set status text dark

        setContentView(R.layout.activity_make_payment)


        txt_view_invoice1.setOnClickListener(this)
        img_back.setOnClickListener(this)
        btn_make_paymnt.setOnClickListener(this)
        txt_sskip.setOnClickListener(this)
        //recycl

        recycler = findViewById(R.id.recycl)
        recycler!!.setHasFixedSize(true)
        recycler!!.layoutManager =
                LinearLayoutManager(this, RecyclerView.VERTICAL, false) as RecyclerView.LayoutManager?


        if (intent != null) {
            if (intent.getStringExtra("storeId") != null) {
                storeId = intent.getStringExtra("storeId")
            }
            if (intent.getStringExtra("offerId") != null) {
                offerId = intent.getStringExtra("offerId")
            }
            if (intent.getStringExtra("isCredit") != null) {
                if (intent.getStringExtra("isCredit").equals("1")) {
                    constraint_skip.visibility = View.VISIBLE
                }

            }
            if (intent.getStringExtra("orderId") != null) {
                orderId = intent.getStringExtra("orderId")

                paymentListApi()
            }
        }
        checkBox.setOnClickListener(View.OnClickListener {
            if (checkBox.isChecked) {
                payymntId = "5"
                checkbox?.isChecked = false
            } else {
                payymntId = ""
            }
        })


        // startDebit()//pay using Phonepe
    }

    object ApiClient {

        var retrofit: Retrofit? = null

        fun getClient(): Retrofit? {

            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                        .baseUrl(url_base)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }
            return retrofit
        }

    }

    fun skipPaymentApi() {

        progressBar.visibility = View.VISIBLE
        val skipPaymentBuyer = SkipPaymentBuyer()
        skipPaymentBuyer.offer_id = offerId
        skipPaymentBuyer.store_id = storeId

        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.skippaymentbuyer(skipPaymentBuyer)

        call.enqueue(object : Callback<SkipPaymentBuyerResponse> {
            override fun onResponse(
                    call: Call<SkipPaymentBuyerResponse>,
                    response: retrofit2.Response<SkipPaymentBuyerResponse>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val intent = Intent()
                    intent.putExtra("date", response.body()!!.data!!.paymentVerifyDate)
                    setResult(1001, intent)
                    finish()
                    /* startActivity(Intent(this@TrackOrderActivity, RateStoreActivity::class.java))
                     finish()
                     overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)*/
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@MakePaymentActivity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@MakePaymentActivity, e.message, Toast.LENGTH_LONG)
                                .show()
                    }

                }
            }


            override fun onFailure(call: Call<SkipPaymentBuyerResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar.visibility = View.GONE
            }
        })
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("date", "")
        setResult(1001, intent)
        finish()
//        super.onBackPressed()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                onBackPressed()
            }
            R.id.txt_sskip -> {
                skipPaymentApi()
            }
            R.id.btn_make_paymnt -> {

                if (payymntId.equals("")) {
                    Toast.makeText(
                            this@MakePaymentActivity,
                            "Select Payment Mode" + payymntId,
                            Toast.LENGTH_SHORT
                    ).show()
                } else {

                    verifyPaymentApi()

                }

                // paytmPermission()
                // googlePay()


            }
            R.id.txt_view_invoice1 -> {
                startActivity(Intent(this@MakePaymentActivity, InvoiceActivity::class.java).putExtra("offerId",offerId))
                //showDialog()
            }
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.cutsom_dialog_layout)
        dialog.window.setBackgroundDrawableResource(android.R.color.white)

        //  dialog .setCancelable(false)
        val close_icon = dialog.findViewById(R.id.img_close) as AppCompatImageView

        close_icon.setOnClickListener {
            dialog.dismiss()
        }

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.BOTTOM
        //   lp.windowAnimations = R.style.DialogAnimation;
        dialog.window!!.attributes = lp
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    fun paymentListApi() {

        progressBar.visibility = View.VISIBLE
        val getStorePayment = GetStorePayment()
        getStorePayment.order_id = orderId
        getStorePayment.offer_id = offerId
        getStorePayment.store_id = storeId

        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.getstorepayment(getStorePayment)

        call.enqueue(object : Callback<GetStorePaymentResponse> {
            override fun onResponse(
                    call: Call<GetStorePaymentResponse>,
                    response: retrofit2.Response<GetStorePaymentResponse>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    paymentListArray = response.body()?.data?.payment!!
                    paymentAmount = response.body()?.data?.offerPrice
                    txt_id.text = "ID: " + response.body()?.data?.orderNo
                    txt_price.text = "Rs. " + response.body()?.data?.offerPrice
                    txt_perc.text = response.body()?.data?.discountPercentage + "% OFF"

//                    var arraytwo: List<GetStorePaymentResponse.Payment> = arrayListOf()
//                    for (i in paymentListArray.indices) {
//                        if (paymentListArray.get(i).paymentName.equals("COD")) {
//                            arraytwo = paymentListArray.drop(i)
//                        }
//                    }

                    makePaymentAdapter = MakePaymentAdapter(
                            this@MakePaymentActivity,
                            paymentListArray,
                            this@MakePaymentActivity
                            , this@MakePaymentActivity)

                    recycler!!.adapter = makePaymentAdapter

                    makePaymentAdapter!!.notifyDataSetChanged()
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@MakePaymentActivity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@MakePaymentActivity, e.message, Toast.LENGTH_LONG)
                                .show()
                    }
                }

            }

            override fun onFailure(call: Call<GetStorePaymentResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar.visibility = View.GONE
            }
        })
    }

    fun verifyPaymentApi() {

        progressBar.visibility = View.VISIBLE
        val verifyPayment = VerifyPayment()
        verifyPayment.order_id = orderId
        verifyPayment.offer_id = offerId
        verifyPayment.mode_of_payment = payymntId
        verifyPayment.amount_received = paymentAmount

        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.verifyPayment(verifyPayment)

        call.enqueue(object : Callback<VerifyPaymentResponse> {
            override fun onResponse(
                    call: Call<VerifyPaymentResponse>,
                    response: retrofit2.Response<VerifyPaymentResponse>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    //  startActivity(Intent(this@MakePaymentActivity, TrackOrderActivity::class.java))
                    val intent = Intent()
                    intent.putExtra("date", "")
                    setResult(1001, intent)
                    finish()
                    //    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@MakePaymentActivity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@MakePaymentActivity, e.message, Toast.LENGTH_LONG)
                                .show()
                    }
                }
            }


            override fun onFailure(call: Call<VerifyPaymentResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar.visibility = View.GONE
            }
        })
    }

    fun startDebit() {
        val amount = CacheUtils.getInstance(this).amountForTransaction
        val txnId = UUID.randomUUID().toString().substring(0, 35)
        val userId = CacheUtils.getInstance(this).userId
        val apiEndPoint = "/v3/debit"

        //val oInfo = OfferInfo("offerId", "Amazing offer")
        ///val discountInfo = DiscountInfo("discountId", "Discount info", "Some Info")

        val data = HashMap<String, Any>()
        data["merchantId"] = MERCHANT_ID
        data["transactionId"] = txnId
        data["amount"] = amount!! * 100
        data["merchantOrderId"] = "OD139924923"
        data["message"] = "Payment towards order No. OD139924923."
        data["merchantUserId"] = userId
//        data.put("offer", oInfo);
//        data.put("discount", discountInfo);

        Log.d("Transaction id", txnId)

        data.put("mobileNumber", "8997542302")

        data.put("email", "abc@gmail.com")

        data.put("shortName", "ABC")
        data["providerName"] = "xMerchantId"

        val dataString = Gson().toJson(data)
        var dataString64: String? = null

        try {
            dataString64 =
                    Base64.encodeToString(dataString.toByteArray(charset("UTF-8")), Base64.DEFAULT)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }


        /* val checksumV2 = CheckSumUtils.getCheckSum(
             dataString64,
             apiEndPoint,
             SALT,
             SALT_KEY_INDEX
         )

         val transactionRequest2 = TransactionRequestBuilder()
             .setData(dataString64)
             .setChecksum(checksumV2)
             .setUrl(apiEndPoint)
             .build()

         try {
             startActivityForResult(PhonePe.getTransactionIntent(this, transactionRequest2), 300)
         } catch (e: PhonePeInitException) {
             e.printStackTrace()
         }*/

    }

    /*com.phonepe.intent.sdk.api.PhonePe*/
    fun initPhonepe() {
        //  PhonePe.init(this)

        //doesPhonePeExist(this)

        /* PhonePe.shouldShow(ShowPhonePeCallback {
             if (it) {
                 Toast.makeText(
                     this,
                     "Phonepe Pay is  available on this device",
                     Toast.LENGTH_LONG
                 ).show();
             } else {
                 Toast.makeText(
                     this,
                     "Phonepe Pay is not available on this device",
                     Toast.LENGTH_LONG
                 ).show();
             }


         })*/

        val amount = CacheUtils.getInstance(this).getAmountForTransaction()
        val txnId = UUID.randomUUID().toString().substring(0, 35)
        val userId = CacheUtils.getInstance(this).getUserId()
        val apiEndPoint = "/v3/debit"

        /*   val oInfo = OfferInfo("offerId", "Amazing offer")
           val discountInfo = DiscountInfo("discountId", "Discount info", "Some Info")*/

        val data = HashMap<String, Any>()

        val headers = HashMap<String, String>()



        data["merchantId"] = MERCHANT_ID
        data["transactionId"] = txnId
        data["amount"] = amount!! * 100
        data["merchantOrderId"] = "OD139924923"
        data["message"] = "Payment towards order No. OD139924923."
        data["merchantUserId"] = userId
//        data.put("offer", oInfo);
//        data.put("discount", discountInfo);

        Log.d("Transaction id", txnId)


        /*Map<String, String> headers = new HashMap();
headers.put("X-CALLBACK-URL","https://www.demoMerchant.com");  // Merchant server URL
headers.put("X-CALL-MODE","POST");
headers.put("X-PROVIDER-ID","M2401563246873249082352"); // ONLY if x-provider-id has been allocated to you.
TransactionRequest debitRequest = new TransactionRequestBuilder()
	.setData(base64Body)
	.setChecksum(checksum)
	.setUrl(apiEndPoint)
	.setHeaders(headers)
	.build();*/


        data["providerName"] = "xMerchantId"

        data.put("mobileNumber", "7542389902")

        data.put("email", "abc@gmail.com")

        data.put("shortName", "ABC")


        val dataString = Gson().toJson(data)
        var dataString64: String? = null


        dataString64 =
                Base64.encodeToString(dataString.toByteArray(charset("UTF-8")), Base64.DEFAULT)


        headers["Content-Type"] = "application/json"

        /* val checksum: String =
             "83A41A36020D6FD2F0E0F02528ADB8784D7800BD56C6C743A28FC49319A4FFDAF8B40A2F3F475BD40968E4644690F874AE63AE744BCE602EC577E1BEBE2364B1###1"*/

        //  headers["X-VERIFY"] =checksum
        /*val checksum: String =
            CheckSumUtils.getCheckSum(dataString64, apiEndPoint, SALT, SALT_KEY_INDEX);
        //  checksum = SHA256(dataString64 + apiEndPoint + salt) + ### + saltIndex
        *//*val checksumV2 = CheckSumUtils.getCheckSum(
            dataString64,
            apiEndPoint,
           SALT,
   SALT_KEY_INDEX
        )*//*

        profileRequest = TransactionRequestBuilder()
            .setData(dataString64)
            .setChecksum(checksum)
            // .setHeaders(headers)
            .setUrl(apiEndPoint)
            .build()

        Log.d("TAG", "PHONREPEFJK-->" + profileRequest)

        *//* startActivity(
             PhonePe.getTransactionIntent(profileRequest!!)
         )*//*

        startActivityForResult(
            PhonePe.getTransactionIntent(this, profileRequest!!),
            DEBIT_REQUEST_CODE
        );*/

    }

    fun doesPhonePeExist(context: Context): Boolean {
        var packageInfo: PackageInfo? = null;
        var phonePeVersionCode: Long = -1L;
        try {

            packageInfo = getPackageManager().getPackageInfo(
                    PHONEPE_PACKAGE_NAME,
                    PackageManager.GET_ACTIVITIES
            );


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                phonePeVersionCode =
                        packageInfo.getLongVersionCode() // avoid huge version numbers and you will be ok
            } else {
                //noinspection deprecation
                phonePeVersionCode = packageInfo.versionCode.toLong()
            }


        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(
                    "TAG", String.format(
                    "failed to get package info for package name = {%s}, exception message = {%s}",
                    PHONEPE_PACKAGE_NAME, e
            )
            )
        }

        if (packageInfo == null) {
            return false;
        }

        if (phonePeVersionCode > 94033) {
            return true;
        }
        return false;
    }

    fun payPAl() {
        /* //Paypal Configuration Object
       config = PayPalConfiguration()
           // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
           // or live (ENVIRONMENT_PRODUCTION)
           .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
           .clientId(PAYPAL_CLIENT_ID)*/
    }

    /* fun googlePay() {
        paymentsClient = createPaymentsClient(this)

        val readyToPayRequest =
            IsReadyToPayRequest.fromJson(googlePayBaseConfiguration.toString())

        val task = paymentsClient.isReadyToPay(readyToPayRequest)

        task.addOnCompleteListener { task ->
            try {
                task.getResult(ApiException::class.java)?.let(::setGooglePayAvailable)
            } catch (exception: ApiException) {
                // Error determining readiness to use Google Pay.
                // Inspect the logs for more details.
            }
        }

    }*/

    private fun setGooglePayAvailable(available: Boolean) {
        if (!available) {
            Toast.makeText(
                    this,
                    "Unfortunately, Google Pay is not available on this device",
                    Toast.LENGTH_LONG
            ).show();
        } else {
            //  requestPayment()
        }
    }

    /*fun createPaymentsClient(activity: Activity): PaymentsClient {
        val walletOptions = Wallet.WalletOptions.Builder()
            .setEnvironment(WalletConstants.ENVIRONMENT_TEST).build()
        return Wallet.getPaymentsClient(activity, walletOptions)
    }*/

    private val baseCardPaymentMethod = JSONObject().apply {
        put("type", "CARD")
        put("parameters", JSONObject().apply {
            put("allowedCardNetworks", JSONArray(listOf("VISA", "MASTERCARD")))
            put("allowedAuthMethods", JSONArray(listOf("PAN_ONLY", "CRYPTOGRAM_3DS")))
        })
    }

    private val googlePayBaseConfiguration = JSONObject().apply {
        put("apiVersion", 2)
        put("apiVersionMinor", 0)
        put("allowedPaymentMethods", JSONArray().put(baseCardPaymentMethod))
    }

    fun generateRandomString() {
        val uuid: String = UUID.randomUUID().toString();
        uuid.replace("-", "")
    }

    fun paytmPermission() {
        if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_SMS
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS),
                    222
            )
        }
        //creating random orderId and custId just for demonstration.
        orderId = generateRandomString().toString()
        custid = generateRandomString().toString()

        apiServicePaytm = ApiClient.getClient()!!.create(ApiInterface::class.java)


        //generateCheckSum()
    }

    override fun getId(name: String, id: String, checkbox1: CheckBox) {
        payymntName = name
        payymntId = id

        checkbox = checkbox1

        if (!payymntId.equals("5")) {
            checkBox.isChecked = false
        }
        btn_make_paymnt.text = ("Confirm Payment")

        if (payymntName.equals("Google Pay")) {
            val url = "https://play.google.com/store/apps/details?id=com.google.android.apps.nbu.paisa.user"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)

        } else if (payymntName.equals("Paytm")) {
            val url = "https://play.google.com/store/apps/details?id=net.one97.paytm"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        } else if (payymntName.equals("Phone Pe")) {
            val url = "https://play.google.com/store/apps/details?id=com.phonepe.app"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        } else {

        }


        //  startActivity(Intent(this@MakePaymentActivity,WebviewPaymentActivity::class.java))
    }


    override fun getQR(qr_img: String) {


    }


}
/*private fun generateCheckSum() {

    Log.d("TAG", "PAYIH")

    val apiService = ApiClient.getClient()!!.create<ApiInterface>(ApiInterface::class.java)

    val call = apiService.getChecksum(
        PAYTM_MERCHANT_ID,
        orderId,
        custid,
        "WAP",
        "100",
        "WEBSTAGING",
        url_paytm_callback,
        "Retail"
    )

    call.enqueue(object : Callback<Checksum>, PaytmPaymentTransactionCallback {
        override fun onTransactionResponse(inResponse: Bundle?) {
            Log.d("TAG", "onTransactionResponse: " + inResponse.toString());
        }

        override fun clientAuthenticationFailed(inErrorMessage: String?) {
            Log.d("TAG", "clientAuthenticationFailed: " + inErrorMessage);
        }

        override fun someUIErrorOccurred(inErrorMessage: String?) {
            Log.d("TAG", "someUIErrorOccurred: " + inErrorMessage);
        }

        override fun onTransactionCancel(inErrorMessage: String?, inResponse: Bundle?) {
            Log.d("TAG", "onTransactionCancel: " + inErrorMessage);
            Log.d("TAG", "onTransactionCancel: " + inResponse);
        }

        override fun networkNotAvailable() {
            Log.d("TAG", "networkNotAvailable: ");
        }

        override fun onErrorLoadingWebPage(
            iniErrorCode: Int,
            inErrorMessage: String?,
            inFailingUrl: String?
        ) {
            Log.d("TAG", "onErrorLoadingWebPage: " + (iniErrorCode));
            Log.d("TAG", "onErrorLoadingWebPage: " + inErrorMessage);
            Log.d("TAG", "onErrorLoadingWebPage: " + inFailingUrl);
        }

        override fun onBackPressedCancelTransaction() {
            Log.d("TAG", "onBackPressedCancelTransaction: ");
        }

        override fun onResponse(call: Call<Checksum>, response: Response<Checksum>) {
            Log.d("TAG", "PFDSFAYIH")
            //   progressBarHandler.hide();

            val checksum: Checksum? = response.body()

            Log.d("TAG", "FDSDFSD")

            val service: PaytmPGService = PaytmPGService.getStagingService();
            // when app is ready to publish use production service
            // PaytmPGService  Service = PaytmPGService.getProductionService();
            //below parameter map is required to construct PaytmOrder object, Merchant should replace below map values with his own values
            val paramMap: HashMap<String, String> = HashMap<String, String>();
            //these are mandatory parameters
            paramMap.put("MID", "rxazcv89315285244163");
// Key in your staging and production MID available in your dashboard
            paramMap.put("ORDER_ID", "order1");
            paramMap.put("CUST_ID", "cust123");
            paramMap.put("MOBILE_NO", "7777777777");
            paramMap.put("EMAIL", "username@emailprovider.com");
            paramMap.put("CHANNEL_ID", "WAP");
            paramMap.put("TXN_AMOUNT", "100.12");
            paramMap.put("WEBSITE", "WEBSTAGING");
// This is the staging value. Production value is available in your dashboard
            paramMap.put("INDUSTRY_TYPE_ID", "Retail");
// This is the staging value. Production value is available in your dashboard
            paramMap.put(
                "CALLBACK_URL",
                "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=order1"
            )

            paramMap.put(
                "CHECKSUMHASH",
                "w2QDRMgp1234567JEAPCIOmNgQvsi+BhpqijfM9KvFfRiPmGSt3Ddzw+oTaGCLneJwxFFq5mqTMwJXdQE2EzK4px2xruDqKZjHupz9yXev4="
            )
            val Order: PaytmOrder = PaytmOrder(paramMap);

            service.initialize(Order, null);

            // start payment service call here
            service.startPaymentTransaction(
                this@MakePaymentActivity, true, true,
                this@MakePaymentActivity
            )


            *//*Service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
	/*Call Backs*//*
                       public void someUIErrorOccurred(String inErrorMessage) {}
                       public void onTransactionResponse(Bundle inResponse) {}
                       public void networkNotAvailable() {}
                       public void clientAuthenticationFailed(String inErrorMessage) {}
                       public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {}
                       public void onBackPressedCancelTransaction() {}
                       public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {}
     });*//*
            }
            *//* } else {
                 try {
                     val jObjError = JSONObject(response.errorBody()!!.string())
                     Toast.makeText(
                         this@MakePaymentActivity,
                         "" + jObjError.getString("message"),
                         Toast.LENGTH_SHORT
                     ).show()


                 } catch (e: Exception) {
                     Toast.makeText(this@MakePaymentActivity, e.message, Toast.LENGTH_LONG)
                         .show()
                 }

             }
         }*//*


            override fun onFailure(call: Call<Checksum>, t: Throwable) {
                // progressBarHandler.hide();

            }
        })
    }
*/



    private fun requestPayment() {

        // Disables the button to prevent multiple clicks.
        btn_make_paymnt.isClickable = false

        val price = "1144"

        val paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(price)
        if (paymentDataRequestJson == null) {
            Log.e("RequestPayment", "Can't fetch payment data request")
            return
        }
        val request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString())

        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
        // AutoResolveHelper to wait for the user interacting with it. Once completed,
        // onActivityResult will be called with the result.
        if (request != null) {
            AutoResolveHelper.resolveTask(
                paymentsClient.loadPaymentData(request), this, LOAD_PAYMENT_DATA_REQUEST_CODE
            )
        }
    }

    fun getPayment() {
        //Getting the amount from editText
        paymentAmount = "1234"

        //Creating a paypalpayment
        val payment = PayPalPayment(
            BigDecimal((paymentAmount)), "USD", "Simplified Coding Fee",
            PayPalPayment.PAYMENT_INTENT_SALE
        )

        //Creating Paypal Payment activity intent
        val intent = Intent(this, PaymentActivity::class.java)

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment)


        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 300) {
            val bundle: Bundle?
            var txnResult: String? = null
            if (data != null) {
                bundle = data.extras
                txnResult = bundle!!.getString(BundleConstants.KEY_TRANSACTION_RESULT)

            }
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this@MakePaymentActivity, txnResult, Toast.LENGTH_SHORT).show()
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this@MakePaymentActivity, "Cancel", Toast.LENGTH_SHORT).show()
            }
        }
        if (requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK ->
                    data?.let { intent ->
                        PaymentData.getFromIntent(intent)?.let(::handlePaymentSuccess)
                    }
                Activity.RESULT_CANCELED -> {
                    // Nothing to do here normally - the user simply cancelled without selecting a
                    // payment method.
                }

                AutoResolveHelper.RESULT_ERROR -> {
                    AutoResolveHelper.getStatusFromIntent(data)?.let {
                        handleError(it.statusCode)
                    }
                }
            }
            // Re-enables the Google Pay payment button.
            btn_make_paymnt.isClickable = true
        }
        if (requestCode == DEBIT_REQUEST_CODE) {
            Log.i("phonepeExample", data.toString())
            if (resultCode == Activity.RESULT_OK) {
                /*This callback indicates only about completion of UI flow.
                Inform your server to make the transaction
                status call to get the status. Update your app with the
                success/failure status.*/
            } else if (resultCode == Activity.RESULT_CANCELED) {
                /*This callback indicates that the transaction has been cancelled by the end user.
                Inform your server to make the transaction
                status call to get the status. Update your app with the
                failure status.*/
            }
        }
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                val confirm: PaymentConfirmation =
                    data!!.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION)

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        val paymentDetails = confirm.toJSONObject().toString(4)
                        Log.i("paymentExample", paymentDetails)

                        //Starting a new activity for the payment details and also putting the payment details with intent
                        /* startActivity(
                             Intent(this, ConfirmationActivity::class.java)
                                 .putExtra("PaymentDetails", paymentDetails)
                                 .putExtra("PaymentAmount", paymentAmount)
                         )*/

                        btn_make_paymnt.setText(R.string.verify_paymnt)
                        txt_paymnt_successful.visibility = View.VISIBLE
                        textView43.visibility = View.GONE
                        constraint_skip.visibility = View.GONE
                        Log.d("paymentExample", "paymentAmount==> " + paymentAmount)

                    } catch (e: JSONException) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e)
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.")
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                    "paymentExample",
                    "An invalid Payment or PayPalConfiguration was submitted. Please see the docs."
                )
            }
        }
    }

    private fun handleError(statusCode: Int) {
        Log.w("loadPaymentData failed", String.format("Error code: %d", statusCode))
    }

    private fun handlePaymentSuccess(paymentData: PaymentData) {
        val paymentInformation = paymentData.toJson() ?: return

        try {
            // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
            val paymentMethodData =
                JSONObject(paymentInformation).getJSONObject("paymentMethodData")

            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".
            if (paymentMethodData
                    .getJSONObject("tokenizationData")
                    .getString("type") == "PAYMENT_GATEWAY" && paymentMethodData
                    .getJSONObject("tokenizationData")
                    .getString("token") == "examplePaymentMethodToken"
            ) {

                AlertDialog.Builder(this)
                    .setTitle("Warning")
                    .setMessage(
                        "Gateway name set to \"example\" - please modify " +
                                "Constants.java and replace it with your own gateway."
                    )
                    .setPositiveButton("OK", null)
                    .create()
                    .show()
            }

            val billingName = paymentMethodData.getJSONObject("info")
                .getJSONObject("billingAddress").getString("name")
            Log.d("BillingName", billingName)

            Toast.makeText(
                this,
                getString(R.string.payments_show_name, billingName),
                Toast.LENGTH_LONG
            ).show()

            // Logging token string.
            Log.d(
                "GooglePaymentToken", paymentMethodData
                    .getJSONObject("tokenizationData")
                    .getString("token")
            )

        } catch (e: JSONException) {
            Log.e("handlePaymentSuccess", "Error: " + e.toString())
        }

    }

    override fun onTransactionResponse(inResponse: Bundle?) {
        Log.d("TAG", "onTransactionResponse: " + inResponse.toString());
    }

    override fun clientAuthenticationFailed(inErrorMessage: String?) {
        Log.d("TAG", "clientAuthenticationFailed: " + inErrorMessage.toString());
    }

    override fun someUIErrorOccurred(inErrorMessage: String?) {
        Log.d("TAG", "someUIErrorOccurred: " + inErrorMessage.toString());
    }

    override fun onTransactionCancel(inErrorMessage: String?, inResponse: Bundle?) {
        Log.d("TAG", "onTransactionCancel: " + inErrorMessage.toString());
    }

    override fun networkNotAvailable() {
        Log.d("TAG", "networkNotAvailable: ");
    }

    override fun onErrorLoadingWebPage(
        iniErrorCode: Int,
        inErrorMessage: String?,
        inFailingUrl: String?
    ) {
        Log.d("TAG", "onErrorLoadingWebPage: " + inErrorMessage.toString());
    }

    override fun onBackPressedCancelTransaction() {
        Log.d("TAG", "onBackPressedCancelTransaction: ");
    }

    override fun onDestroy() {
        stopService(
            Intent(this, PayPalService::class.java)
        )
        PhonePe.logout()

        super.onDestroy()
    }*/

