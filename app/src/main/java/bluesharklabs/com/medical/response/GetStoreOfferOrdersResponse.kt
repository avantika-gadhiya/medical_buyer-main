package bluesharklabs.com.medical.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class GetStoreOfferOrdersResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("response")
    @Expose
    var response: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    class Data {
        @SerializedName("offer_is_prescription")
        @Expose
        val offerIsPrescription: String? = null

        @SerializedName("offer_delivery_preference")
        @Expose
        val offerDeliveryPreference: String? = null

        @SerializedName("offer_delivery_preference_name")
        @Expose
        val offerDeliveryPreferenceName: String? = null

        @SerializedName("offer_todate_fromdate")
        @Expose
        val offerTodateFromdate: String? = null
        @SerializedName("store_detail")
        @Expose
        val storeDetail: StoreDetail? = null

        @SerializedName("offer_id")
        @Expose
        val offerId: String? = null

        @SerializedName("order_id")
        @Expose
        val orderId: String? = null

        @SerializedName("delivery_type")
        @Expose
        val deliveryType: String? = null

        @SerializedName("delivery_type_name")
        @Expose
        val deliveryTypeName: String? = null

        @SerializedName("order_type")
        @Expose
        val orderType: String? = null

        @SerializedName("order_type_name")
        @Expose
        val orderTypeName: String? = null

        @SerializedName("delivery_longitude")
        @Expose
        val deliveryLongitude: String? = null

        @SerializedName("area")
        @Expose
        val area: String? = null

        @SerializedName("block_no")
        @Expose
        val blockNo: String? = null

        @SerializedName("building_name")
        @Expose
        val buildingName: String? = null

        @SerializedName("street")
        @Expose
        val street: String? = null

        @SerializedName("zipcode")
        @Expose
        val zipcode: String? = null

        @SerializedName("landmark")
        @Expose
        val landmark: String? = null

        @SerializedName("location")
        @Expose
        val location: String? = null

        @SerializedName("delivery_date")
        @Expose
        val deliveryDate: String? = null

        @SerializedName("delivery_time_start")
        @Expose
        val deliveryTimeStart: String? = null

        @SerializedName("delivery_time_end")
        @Expose
        val deliveryTimeEnd: String? = null

        @SerializedName("todate_fromdate")
        @Expose
        val todateFromdate: String? = null

        @SerializedName("store_id")
        @Expose
        val storeId: String? = null

        @SerializedName("offer_delivery_date")
        @Expose
        val offerDeliveryDate: String? = null

        @SerializedName("offer_delivery_time_start")
        @Expose
        val offerDeliveryTimeStart: String? = null

        @SerializedName("offer_delivery_time_end")
        @Expose
        val offerDeliveryTimeEnd: String? = null

        @SerializedName("discount_percentage")
        @Expose
        val discountPercentage: String? = null

        @SerializedName("final_amount")
        @Expose
        val finalAmount: String? = null
    }

    class StoreDetail {
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

    }
}