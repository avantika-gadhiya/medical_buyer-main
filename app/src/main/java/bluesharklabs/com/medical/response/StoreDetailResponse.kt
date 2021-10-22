package bluesharklabs.com.medical.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class StoreDetailResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("response")
    @Expose
    var response: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    inner class Data {

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


        @SerializedName("building")
        @Expose
        var building: String? = null

        @SerializedName("city")
        @Expose
        var city: String? = null

        @SerializedName("zipcode")
        @Expose
        var zipcode: String? = null

        @SerializedName("registered_pharmacist_name")
        @Expose
        var registeredPharmacistName: String? = null

        @SerializedName("geo_location")
        @Expose
        var geoLocation: String? = null

        @SerializedName("gst_certificate")
        @Expose
        var gstCertificate: Any? = null

        @SerializedName("drug_license_image")
        @Expose
        var drugLicenseImage: Any? = null

        @SerializedName("payment_method")
        @Expose
        var paymentMethod: String? = null

        @SerializedName("store_photo")
        @Expose
        var storePhoto: Any? = null

        @SerializedName("latitude")
        @Expose
        var latitude: String? = null

        @SerializedName("longitude")
        @Expose
        var longitude: String? = null

    }
}