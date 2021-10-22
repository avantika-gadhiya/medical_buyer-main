package bluesharklabs.com.medical.interfaces

import bluesharklabs.com.medical.model.*
import bluesharklabs.com.medical.response.*
import bluesharklabs.com.medical.utils.AppConstant.ADD_ORDER_API
import bluesharklabs.com.medical.utils.AppConstant.ADD_TOKEN
import bluesharklabs.com.medical.utils.AppConstant.ALL_STORE_AREA_API
import bluesharklabs.com.medical.utils.AppConstant.ALL_STORE_CITY_API
import bluesharklabs.com.medical.utils.AppConstant.ALL_STORE_ZIP_CODE_API
import bluesharklabs.com.medical.utils.AppConstant.BUYER_ALL_ORDER_API
import bluesharklabs.com.medical.utils.AppConstant.BUYER_ALL_ORDER_COLORS
import bluesharklabs.com.medical.utils.AppConstant.BUYER_CHECK_API
import bluesharklabs.com.medical.utils.AppConstant.BUYER_DETAIL_API
import bluesharklabs.com.medical.utils.AppConstant.BUYER_DETAIL_UPDATE_API
import bluesharklabs.com.medical.utils.AppConstant.BUYER_ORDER_ACCEPT_API
import bluesharklabs.com.medical.utils.AppConstant.BUYER_ORDER_ACCEPT_LIST_API
import bluesharklabs.com.medical.utils.AppConstant.BUYER_PARTIAL_FILTER_APPLY
import bluesharklabs.com.medical.utils.AppConstant.COMMON_FILTER_API
import bluesharklabs.com.medical.utils.AppConstant.COMMON_NOTIFICATION_API
import bluesharklabs.com.medical.utils.AppConstant.CONTACT_US
import bluesharklabs.com.medical.utils.AppConstant.DOCTOR_LIST
import bluesharklabs.com.medical.utils.AppConstant.GET_ALL_DROPDOWN_API
import bluesharklabs.com.medical.utils.AppConstant.GET_BUYER_RATING_API
import bluesharklabs.com.medical.utils.AppConstant.GET_COLOR_CODE_API
import bluesharklabs.com.medical.utils.AppConstant.GET_FULL_COVERAGE_ORDERS_API
import bluesharklabs.com.medical.utils.AppConstant.GET_INVOICE_API
import bluesharklabs.com.medical.utils.AppConstant.GET_MERCH_CATEGORY_API
import bluesharklabs.com.medical.utils.AppConstant.GET_ORDER_TIMELINE_API
import bluesharklabs.com.medical.utils.AppConstant.GET_PARTIAL_COVERAGE_ORDERS_API
import bluesharklabs.com.medical.utils.AppConstant.GET_PRECRIPTION_LIST_API
import bluesharklabs.com.medical.utils.AppConstant.GET_STORE_DETAIL_API
import bluesharklabs.com.medical.utils.AppConstant.GET_STORE_LIST_API
import bluesharklabs.com.medical.utils.AppConstant.GET_STORE_OFFER_ORDERS_API
import bluesharklabs.com.medical.utils.AppConstant.GET_STORE_PAYMENT_METHOD_API
import bluesharklabs.com.medical.utils.AppConstant.GET_STORE_TYPE_API
import bluesharklabs.com.medical.utils.AppConstant.GET_TERMS_CONDITION_API
import bluesharklabs.com.medical.utils.AppConstant.GOOGLE_PLACE_API
import bluesharklabs.com.medical.utils.AppConstant.LOGIN_API
import bluesharklabs.com.medical.utils.AppConstant.MAKE_STORE_REVIEW_API
import bluesharklabs.com.medical.utils.AppConstant.ORDER_DETAIL_API
import bluesharklabs.com.medical.utils.AppConstant.ORDER_DETAIL_FOR_OFFER_API
import bluesharklabs.com.medical.utils.AppConstant.ORDER_DETAIL_FOR_PICKUP_API
import bluesharklabs.com.medical.utils.AppConstant.ORDER_LIST_API
import bluesharklabs.com.medical.utils.AppConstant.REGISTER_API
import bluesharklabs.com.medical.utils.AppConstant.SEND_FEEDBACK
import bluesharklabs.com.medical.utils.AppConstant.SKIP_PAYMENT_BUYER_API
import bluesharklabs.com.medical.utils.AppConstant.TANDC
import bluesharklabs.com.medical.utils.AppConstant.VERIFICATION_API
import bluesharklabs.com.medical.utils.AppConstant.VERIFY_ORDER_API
import bluesharklabs.com.medical.utils.AppConstant.VERIFY_PAYMENT_API
import retrofit2.Call
import retrofit2.http.*


interface RetrofitInterface {

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @GET(GOOGLE_PLACE_API)
    fun getResults(
        @Query("input") input: String,
        @Query("types") types: String,
        @Query("key") key: String
    ): Call<PlaceResultsResponse>

   /* @POST(REGISTER_API)
    @Multipart
    abstract fun register(
        @Part("fullname") fullname: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("age") age: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("address_line_1") address_line_1: RequestBody,
        @Part("address_line_2") address_line_2: RequestBody,
        @Part("landmark") landmark: RequestBody,
        @Part("city") city: RequestBody,
        @Part("zipcode") zipcode: RequestBody,
        @Part photo_id: MultipartBody.Part
    ): Call<RegisterResponse>*/

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(REGISTER_API)
    fun register(@Body body: Register): Call<RegisterResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(ADD_TOKEN)
    fun addToken(@Body body: AddToken): Call<CommonResponse>


    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(LOGIN_API)
    fun login(@Body body: Login): Call<LoginResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(ADD_ORDER_API)
    fun addorder(@Body body: AddOrder): Call<AddOrderResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(ORDER_LIST_API)
    fun orderlist(@Body body: OrderList): Call<OrderListResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(GET_STORE_LIST_API)
    fun storelist(@Body body: StoreList): Call<StoreListResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(ORDER_DETAIL_API)
    fun orderdetail(@Body body: OrderDetail): Call<OrderDetailResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(GET_STORE_DETAIL_API)
    fun storedetail(@Body body: StoreDetail): Call<StoreDetailResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @GET(GET_STORE_TYPE_API)
    fun getstoretype(): Call<GetStoreTypeResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @GET(GET_MERCH_CATEGORY_API)
    fun getmerchcategory(): Call<GetMerchCategoryResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @GET(GET_ALL_DROPDOWN_API)
    fun getalldropdown(): Call<GetAllDropdownResponse>


    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(COMMON_NOTIFICATION_API)
    fun getNtifications(@Body body: NotiFications): Call<NotificationResponse>


    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @GET(GET_COLOR_CODE_API)
    fun getcolorcode(): Call<GetColorCodeResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @GET(GET_TERMS_CONDITION_API)
    fun gettermscoddition(): Call<GetTermsConditionResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(GET_FULL_COVERAGE_ORDERS_API)
    fun getfullcoverage(@Body body: GetFullCoverage): Call<GetFullCoverageResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(BUYER_ORDER_ACCEPT_API)
    fun buyerorderaccept(@Body body: BuyerOrderAccept): Call<BuyerOrderAcceptResponse>


    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(BUYER_ALL_ORDER_COLORS)
    fun buyerorderfilter(@Body body: BuyerOrderAcceptDetail): Call<GetAllProductColorsResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(BUYER_PARTIAL_FILTER_APPLY)
    fun buyerpartialofferfilter(@Body body: PartialOfferFilter): Call<PartialCoverageOrderResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(GET_INVOICE_API)
    fun getinvoice(@Body body: GetInvoice): Call<GetInvoiceResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(BUYER_ORDER_ACCEPT_LIST_API)
    fun buyerorderacceptdetail(@Body body: BuyerOrderAcceptDetail): Call<BuyerOrderAcceptDetailResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(ORDER_DETAIL_FOR_OFFER_API)
    fun orderdetailoffer(@Body body: OrderDetailOffer): Call<OrderDetailResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(GET_PARTIAL_COVERAGE_ORDERS_API)
    fun partialcoverage(@Body body: PartialCoverageOrder): Call<PartialCoverageOrderResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(GET_ORDER_TIMELINE_API)
    fun getordertimeline(@Body body: OrderTimeline): Call<OrderTimelineResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(VERIFY_PAYMENT_API)
    fun verifyPayment(@Body body: VerifyPayment): Call<VerifyPaymentResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(GET_STORE_PAYMENT_METHOD_API)
    fun getstorepayment(@Body body: GetStorePayment): Call<GetStorePaymentResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(VERIFY_ORDER_API)
    fun verifyorder(@Body body: VerifyOrder): Call<VerifyOrderResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(GET_BUYER_RATING_API)
    fun buyerating(@Body body: BuyerRating): Call<BuyerRatingResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(MAKE_STORE_REVIEW_API)
    fun makestorereview(@Body body: MakeStoreReview): Call<MakeStoreReviewResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(SKIP_PAYMENT_BUYER_API)
    fun skippaymentbuyer(@Body body: SkipPaymentBuyer): Call<SkipPaymentBuyerResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(VERIFICATION_API)
    fun verification(@Body body: Verification): Call<VerificationResponse>

    //CHeck this api
    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(ORDER_DETAIL_FOR_PICKUP_API)
    fun orderdetail(@Body body: OrderDetailForPickup): Call<OrderDetailForPickupResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(GET_STORE_OFFER_ORDERS_API)
    fun offerorders(@Body body: GetStoreOfferOrders): Call<GetStoreOfferOrdersResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(BUYER_DETAIL_API)
    fun buyer_detail(@Body body: OrderList): Call<BuyerDetailResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(BUYER_DETAIL_UPDATE_API)
    fun buyer_detail_update(@Body body: Register): Call<ProfileUpdateResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(GET_PRECRIPTION_LIST_API)
    fun prescriptionDetail(@Body body: OrderList): Call<PrescriptionResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(BUYER_ALL_ORDER_API)
    fun myOrder(@Body body: OrderListByFilter): Call<MyOrderResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(BUYER_CHECK_API)
    fun buyerCheck(@Body body: OrderList): Call<PrescriptionResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(COMMON_FILTER_API)
    fun common_filter(@Body body: Filter): Call<FilterResponse>


    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(ALL_STORE_CITY_API)
    fun getAllStoreCity(): Call<GetAllCityResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(ALL_STORE_ZIP_CODE_API)
    fun getAllStoreZipcode(@Body body: ZipCode): Call<GetAllZipCodeResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(ALL_STORE_AREA_API)
    fun getAllStoreArea(@Body body: ZipCode): Call<GetAllStoreAreaResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(TANDC)
    fun getTandC(): Call<TandCResponse>

    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(CONTACT_US)
    fun getContactUS(): Call<ContactUSResponse>



    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(SEND_FEEDBACK)
    fun sendFeedBack(@Body body: Feedback): Call<CommonResponse>



    @Headers("Content-Type: application/json", "Cache-Control: no-cache")
    @POST(DOCTOR_LIST)
    fun getDoctors(): Call<ConsultDoctorResponse>



}