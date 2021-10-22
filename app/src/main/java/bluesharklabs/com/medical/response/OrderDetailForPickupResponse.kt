package bluesharklabs.com.medical.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class OrderDetailForPickupResponse {
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
        @SerializedName("store_detail")
        @Expose
        val storeDetail: StoreDetail? = null

        @SerializedName("order_id")
        @Expose
        val orderId: String? = null

        @SerializedName("to_delivery_date")
        @Expose
        val toDeliveryDate: String? = null

        @SerializedName("customer_name")
        @Expose
        val customerName: String? = null

        @SerializedName("order_no")
        @Expose
        val orderNo: String? = null

        @SerializedName("created_date")
        @Expose
        val createdDate: String? = null

        @SerializedName("order_status")
        @Expose
        val orderStatus: String? = null

        @SerializedName("request_status")
        @Expose
        val requestStatus: String? = null

        @SerializedName("re_order")
        @Expose
        val reOrder: String? = null

        @SerializedName("buying_preference")
        @Expose
        val buyingPreference: String? = null

        @SerializedName("buying_preference_name")
        @Expose
        val buyingPreferenceName: String? = null

        @SerializedName("order_preference")
        @Expose
        val orderPreference: String? = null

        @SerializedName("order_preference_name")
        @Expose
        val orderPreferenceName: String? = null

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

        @SerializedName("delivery_latitude")
        @Expose
        val deliveryLatitude: String? = null

        @SerializedName("delivery_type")
        @Expose
        val deliveryType: String? = null


        @SerializedName("delivery_city")
        @Expose
        val deliveryCity: String? = null


        @SerializedName("delivery_type_name")
        @Expose
        val deliveryTypeName: String? = null

        @SerializedName("end_time")
        @Expose
        val endTime: String? = null

        @SerializedName("todate_fromdate")
        @Expose
        val todateFromdate: String? = null

        @SerializedName("location_criteria_delivery_location")
        @Expose
        val locationCriteriaDeliveryLocation: String? = null

        @SerializedName("location_criteria_location")
        @Expose
        val locationCriteriaLocation: String? = null

        @SerializedName("order_for")
        @Expose
        val orderFor: String? = null

        @SerializedName("order_for_name")
        @Expose
        val orderForName: String? = null

        @SerializedName("order_latitude")
        @Expose
        val orderLatitude: String? = null

        @SerializedName("order_longitude")
        @Expose
        val orderLongitude: String? = null

        @SerializedName("turn_on_location")
        @Expose
        val turnOnLocation: String? = null

        @SerializedName("photo_id")
        @Expose
        val photoId: String? = null

        @SerializedName("prescription_image")
        @Expose
        val prescriptionImage: String? = null

        @SerializedName("start_time")
        @Expose
        val startTime: String? = null

        @SerializedName("store_criteria_area")
        @Expose
        val storeCriteriaArea: String? = null

        @SerializedName("store_criteria_city")
        @Expose
        val storeCriteriaCity: String? = null

        @SerializedName("store_criteria_store_md_name")
        @Expose
        val storeCriteriaStoreMdName: String? = null

        @SerializedName("store_criteria_store_md_id")
        @Expose
        val storeCriteriaStoreMdId: String? = null

        @SerializedName("store_criteria_store_type_id")
        @Expose
        val storeCriteriaStoreTypeId: String? = null

        @SerializedName("store_criteria_store_type_name")
        @Expose
        val storeCriteriaStoreTypeName: String? = null

        @SerializedName("store_criteria_zipcode")
        @Expose
        val storeCriteriaZipcode: String? = null

        @SerializedName("products")
        @Expose
        val products: List<Product>? = null


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
        var gstCertificate: Any? = null

        @SerializedName("drug_license_image")
        @Expose
        var drugLicenseImage: Any? = null

        @SerializedName("store_photo")
        @Expose
        var storePhoto: String? = null

        @SerializedName("created_date")
        @Expose
        var createdDate: String? = null

        @SerializedName("latitude")
        @Expose
        var store_latitude: String? = null

        @SerializedName("longitude")
        @Expose
        var store_longitude: String? = null

    }

    class Product {
        @SerializedName("id")
        @Expose
        val id: String? = null

        @SerializedName("product_id")
        @Expose
        val productId: String? = null

        @SerializedName("product_name")
        @Expose
        val productName: String? = null

        @SerializedName("order_id")
        @Expose
        val orderId: String? = null

        @SerializedName("color_id")
        @Expose
        val colorId: String? = null

        @SerializedName("color")
        @Expose
        val color: String? = null

        @SerializedName("quantity_type")
        @Expose
        val quantityType: String? = null

        @SerializedName("quantity")
        @Expose
        val quantity: String? = null

        @SerializedName("product_category")
        @Expose
        val productCategory: String? = null

        @SerializedName("product_content")
        @Expose
        val productContent: String? = null

        @SerializedName("product_unit")
        @Expose
        val productUnit: String? = null

        @SerializedName("product_unit_name")
        @Expose
        val productUnitName: String? = null

        @SerializedName("product_pack")
        @Expose
        val productPack: String? = null

        @SerializedName("product_pack_name")
        @Expose
        val productPackName: String? = null

        @SerializedName("product_brand")
        @Expose
        val productBrand: String? = null


    }
}