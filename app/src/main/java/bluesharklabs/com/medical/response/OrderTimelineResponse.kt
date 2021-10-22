package bluesharklabs.com.medical.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class OrderTimelineResponse {


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
        @SerializedName("store_phone")
        @Expose
        var storePhone: String? = null

        @SerializedName("store_name")
        @Expose
        var storeName: String? = null

        @SerializedName("order_no")
        @Expose
        var orderNo: String? = null

        @SerializedName("created_date")
        @Expose
        var createdDate: String? = null

        @SerializedName("offer_id")
        @Expose
        var offerId: String? = null

        @SerializedName("store_id")
        @Expose
        var storeId: String? = null

        @SerializedName("order_id")
        @Expose
        var orderId: String? = null

        @SerializedName("receiver_opt_phone")
        @Expose
        var receiverOptPhone: String? = null

        @SerializedName("discount_percentage")
        @Expose
        var discountPercentage: String? = null

        @SerializedName("offer_price")
        @Expose
        var offerPrice: String? = null

        @SerializedName("offer_price_change_by_store")
        @Expose
        var offerPriceChangeByStore: String? = null

        @SerializedName("offer_confirm_date")
        @Expose
        var offerConfirmDate: String? = null

        @SerializedName("offer_receiver_verify_date")
        @Expose
        var offerReceiverVerifyDate: String? = null

        @SerializedName("payment_verify_date")
        @Expose
        var paymentVerifyDate: String? = null

        @SerializedName("order_recieved")
        @Expose
        var orderRecieved: String? = null

        @SerializedName("timeline_flag")
        @Expose
        var timelineFlag: String? = null

        @SerializedName("isCredit")
        @Expose
        var isCredit: Int? = null

        @SerializedName("payment_name")
        @Expose
        var paymentName: String? = null

        @SerializedName("invoice_image")
        @Expose
        var invoiceImage: String? = null

        @SerializedName("delivery_type")
        @Expose
        var deliveryType: String? = null

        @SerializedName("offer_delivery_type")
        @Expose
        var offerDeliveryType: String? = null

        @SerializedName("delivery_city")
        @Expose
        var deliveryCity: String? = null

        @SerializedName("store_area")
        @Expose
        var storeArea: String? = null

        @SerializedName("delivery_type_name")
        @Expose
        var deliveryTypeName: String? = null

        @SerializedName("offer_delivery_type_name")
        @Expose
        var offerDeliveryTypeName: String? = null

        @SerializedName("turn_on_location")
        @Expose
        var turnOnLocation: String? = null

        @SerializedName("location")
        @Expose
        var location: String? = null

        @SerializedName("block_no")
        @Expose
        var blockNo: String? = null

        @SerializedName("building_name")
        @Expose
        var buildingName: String? = null

        @SerializedName("street")
        @Expose
        var street: String? = null

        @SerializedName("area")
        @Expose
        var area: String? = null

        @SerializedName("landmark")
        @Expose
        var landmark: String? = null

        @SerializedName("zipcode")
        @Expose
        var zipcode: String? = null

        @SerializedName("todate_fromdate")
        @Expose
        var todate_fromdate: String? = null

        @SerializedName("is_store_review")
        @Expose
        var isStoreReview: Int? = null


    }
}