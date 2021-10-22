package bluesharklabs.com.medical.response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class MyOrderResponse {

    @SerializedName("status")
    @Expose
     var status: Boolean? = null

    @SerializedName("response")
    @Expose
    var response: String? = null

    @SerializedName("data")
    @Expose
    var data: ArrayList<Datum>? = null

    class Datum {
        @SerializedName("order_id")
        @Expose
        var orderId: String? = null

        @SerializedName("order_no")
        @Expose
        var orderNo: String? = null

        @SerializedName("created_date")
        @Expose
        var createdDate: String? = null

        @SerializedName("store_id")
        @Expose
        var storeId: String? = null

        @SerializedName("offer_id")
        @Expose
        var offerId: String? = null

        @SerializedName("order_platform_value")
        @Expose
        var orderPlatformValue: String? = null

        @SerializedName("order_platform")
        @Expose
        var orderPlatform: String? = null

        @SerializedName("order_status")
        @Expose
        var orderStatus: String? = null

        @SerializedName("order_type")
        @Expose
        var orderType: String? = null

        @SerializedName("order_type_name")
        @Expose
        var orderTypeName: String? = null

        @SerializedName("request_status")
        @Expose
        var requestStatus: String? = null

        @SerializedName("re_order")
        @Expose
        var reOrder: String? = null

    }
}