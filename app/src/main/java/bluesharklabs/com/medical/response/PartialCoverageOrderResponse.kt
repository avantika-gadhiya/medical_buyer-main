package bluesharklabs.com.medical.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class PartialCoverageOrderResponse {


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
        @SerializedName("order_id")
        @Expose
        var orderId: String? = null

        @SerializedName("order_no")
        @Expose
        var orderNo: String? = null

        @SerializedName("created_date")
        @Expose
        var createdDate: String? = null

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

        @SerializedName("offer")
        @Expose
        var offer: List<Offer>? = null

    }

    inner class Offer {

        @SerializedName("color")
        @Expose
        var color: List<String>? = null
        @SerializedName("best_in_price")
        @Expose
        var bestInPrice: BestInPrice? = null
        @SerializedName("best_in_delivery")
        @Expose
        var bestInDelivery: BestInDelivery? = null

    }

    class BestInDelivery {
        @SerializedName("order_id")
        @Expose
        var orderId: String? = null

        @SerializedName("order_no")
        @Expose
        var orderNo: String? = null

        @SerializedName("delivery_type")
        @Expose
        var deliveryType: String? = null

        @SerializedName("delivery_type_name")
        @Expose
        var deliveryTypeName: String? = null

        @SerializedName("order_type")
        @Expose
        var orderType: String? = null

        @SerializedName("order_type_name")
        @Expose
        var orderTypeName: String? = null

        @SerializedName("delivery_date")
        @Expose
        var deliveryDate: String? = null

        @SerializedName("todate_fromdate")
        @Expose
        var toDateFromDate: String? = null

        @SerializedName("delivery_time_start")
        @Expose
        var deliveryTimeStart: String? = null

        @SerializedName("delivery_time_end")
        @Expose
        var deliveryTimeEnd: String? = null

        @SerializedName("offer_id")
        @Expose
        var offerId: String? = null

        @SerializedName("store_id")
        @Expose
        var storeId: String? = null

        @SerializedName("offer_delivery_preference")
        @Expose
        var offerDeliveryPreference: String? = null

        @SerializedName("offer_delivery_preference_name")
        @Expose
        var offerDeliveryPreferenceName: String? = null

        @SerializedName("offer_delivery_date")
        @Expose
        var offerDeliveryDate: String? = null

        @SerializedName("offer_delivery_time_start")
        @Expose
        var offerDeliveryTimeStart: String? = null

        @SerializedName("offer_delivery_time_end")
        @Expose
        var offerDeliveryTimeEnd: String? = null

        @SerializedName("offer_discount_percentage")
        @Expose
        var offerDiscountPercentage: String? = null

        @SerializedName("offer_final_amount")
        @Expose
        var offerFinalAmount: String? = null

        @SerializedName("discount_percentage")
        @Expose
        var discountPercentage: String? = null

        @SerializedName("final_amount")
        @Expose
        var finalAmount: String? = null

    }
    class BestInPrice {

        @SerializedName("offer_delivery_preference")
        @Expose
        var offerDeliveryPreference: String? = null

        @SerializedName("offer_delivery_preference_name")
        @Expose
        var offerDeliveryPreferenceName: String? = null

        @SerializedName("order_id")
        @Expose
        var orderId: String? = null

        @SerializedName("delivery_type")
        @Expose
        var deliveryType: String? = null

        @SerializedName("delivery_type_name")
        @Expose
        var deliveryTypeName: String? = null

        @SerializedName("order_type")
        @Expose
        var orderType: String? = null

        @SerializedName("order_type_name")
        @Expose
        var orderTypeName: String? = null

        @SerializedName("delivery_date")
        @Expose
        var deliveryDate: String? = null


        @SerializedName("todate_fromdate")
        @Expose
        var toDateFromDate: String? = null

        @SerializedName("delivery_time_start")
        @Expose
        var deliveryTimeStart: String? = null

        @SerializedName("delivery_time_end")
        @Expose
        var deliveryTimeEnd: String? = null

        @SerializedName("offer_id")
        @Expose
        var offerId: String? = null

        @SerializedName("store_id")
        @Expose
        var storeId: String? = null

        @SerializedName("offer_delivery_date")
        @Expose
        var offerDeliveryDate: String? = null

        @SerializedName("offer_delivery_time_start")
        @Expose
        var offerDeliveryTimeStart: String? = null

        @SerializedName("offer_delivery_time_end")
        @Expose
        var offerDeliveryTimeEnd: String? = null

        @SerializedName("offer_discount_percentage")
        @Expose
        var offerDiscountPercentage: String? = null
        @SerializedName("offer_final_amount")
        @Expose
        var offerFinalAmount: String? = null

        @SerializedName("discount_percentage")
        @Expose
        var discountPercentage: String? = null

        @SerializedName("final_amount")
        @Expose
        var finalAmount: String? = null

    }

}