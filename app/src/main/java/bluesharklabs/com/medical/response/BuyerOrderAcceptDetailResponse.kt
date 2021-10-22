package bluesharklabs.com.medical.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class BuyerOrderAcceptDetailResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("response")
    @Expose
    var response: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    @SerializedName("message")
    @Expose
    var message: String? = null


    class Data {
        @SerializedName("order_id")
        @Expose
        var orderId: String? = null

        @SerializedName("order_type")
        @Expose
        var orderType: String? = null

        @SerializedName("order_type_name")
        @Expose
        var orderTypeName: String? = null

        @SerializedName("color")
        @Expose
        var color: List<Any>? = null

        @SerializedName("best_in_type")
        @Expose
        var bestInType: String? = null

        @SerializedName("store_id")
        @Expose
        var storeId: String? = null

        @SerializedName("offer_coverage_type")
        @Expose
        var offerCoverageType: String? = null

        @SerializedName("store")
        @Expose
        var store: Store? = null

        @SerializedName("order_no")
        @Expose
        var orderNo: String? = null

        @SerializedName("created_date")
        @Expose
        var createdDate: String? = null

        @SerializedName("order_status")
        @Expose
        var orderStatus: String? = null

        @SerializedName("request_status")
        @Expose
        var requestStatus: String? = null

        @SerializedName("re_order")
        @Expose
        var reOrder: String? = null

        @SerializedName("delivery_type")
        @Expose
        var deliveryType: String? = null

        @SerializedName("delivery_type_name")
        @Expose
        var deliveryTypeName: String? = null

        @SerializedName("delivery_date")
        @Expose
        var deliveryDate: String? = null

        @SerializedName("end_time")
        @Expose
        var endTime: String? = null

        @SerializedName("start_time")
        @Expose
        var startTime: String? = null

        @SerializedName("offer_id")
        @Expose
        var offerId: String? = null

        @SerializedName("offer_discount_percentage")
        @Expose
        var offerDiscountPercentage: String? = null

        @SerializedName("offer_discount_price")
        @Expose
        var offerDiscountPrice: String? = null

        @SerializedName("offer_delivery_charges")
        @Expose
        var offerDeliveryCharges: String? = null

        @SerializedName("offer_delivery_preference")
        @Expose
        var offerDeliveryPreference: String? = null

        @SerializedName("todate_fromdate")
        @Expose
        var todateFromdate: String? = null

        @SerializedName("offer_todate_fromdate")
        @Expose
        var offerTodateFromdate: String? = null

        @SerializedName("offer_delivery_preference_name")
        @Expose
        var offerDeliveryPreferenceName: String? = null

        @SerializedName("offer_final_amount")
        @Expose
        var offerFinalAmount: String? = null

        @SerializedName("offer_delivery_date")
        @Expose
        var offerDeliveryDate: String? = null

        @SerializedName("offer_delivery_time_start")
        @Expose
        var offerDeliveryTimeStart: String? = null

        @SerializedName("offer_delivery_time_end")
        @Expose
        var offerDeliveryTimeEnd: String? = null

    }

    class Store {
        @SerializedName("id")
        @Expose
        var id: String? = null

        @SerializedName("owner_name")
        @Expose
        var ownerName: String? = null

        @SerializedName("phone")
        @Expose
        var phone: String? = null

        @SerializedName("otp")
        @Expose
        var otp: String? = null

        @SerializedName("is_otp_verified")
        @Expose
        var isOtpVerified: String? = null

        @SerializedName("employee1_name")
        @Expose
        var employee1Name: String? = null

        @SerializedName("employee1_mobile")
        @Expose
        var employee1Mobile: String? = null

        @SerializedName("employee2_mobile")
        @Expose
        var employee2Mobile: String? = null

        @SerializedName("employee2_name")
        @Expose
        var employee2Name: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("type")
        @Expose
        var type: String? = null

        @SerializedName("gst_number")
        @Expose
        var gstNumber: String? = null

        @SerializedName("drug_license_number")
        @Expose
        var drugLicenseNumber: String? = null

        @SerializedName("merchandise_category")
        @Expose
        var merchandiseCategory: String? = null

        @SerializedName("established_since")
        @Expose
        var establishedSince: String? = null

        @SerializedName("shop_no")
        @Expose
        var shopNo: String? = null

        @SerializedName("street")
        @Expose
        var street: String? = null

        @SerializedName("area")
        @Expose
        var area: String? = null

        @SerializedName("landmark")
        @Expose
        var landmark: String? = null

        @SerializedName("city")
        @Expose
        var city: String? = null

        @SerializedName("zipcode")
        @Expose
        var zipcode: String? = null

        @SerializedName("geo_location")
        @Expose
        var geoLocation: String? = null

        @SerializedName("gst_certificate")
        @Expose
        var gstCertificate: String? = null

        @SerializedName("drug_license_image")
        @Expose
        var drugLicenseImage: String? = null

        @SerializedName("store_photo")
        @Expose
        var storePhoto: String? = null

        @SerializedName("created_date")
        @Expose
        var createdDate: String? = null

        @SerializedName("payment_method")
        @Expose
        var paymentMethod: String? = null

    }
}