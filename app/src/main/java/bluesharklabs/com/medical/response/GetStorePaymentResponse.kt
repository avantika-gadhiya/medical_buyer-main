package bluesharklabs.com.medical.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class GetStorePaymentResponse {
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
        @SerializedName("order_no")
        @Expose
        var orderNo: String? = null

        @SerializedName("order_id")
        @Expose
        var orderId: String? = null

        @SerializedName("offer_id")
        @Expose
        var offerId: String? = null

        @SerializedName("discount_percentage")
        @Expose
        var discountPercentage: String? = null

        @SerializedName("offer_price")
        @Expose
        var offerPrice: String? = null

        @SerializedName("invoice_image")
        @Expose
        var invoiceImage: String? = null

        @SerializedName("payment")
        @Expose
        var payment: List<Payment>? = null



    }
    class Payment {
        @SerializedName("payment_method_id")
        @Expose
        var paymentMethodId: String? = null

        @SerializedName("payment_name")
        @Expose
        var paymentName: String? = null

        @SerializedName("upi")
        @Expose
        var upi: String? = null

        @SerializedName("qr_img")
        @Expose
        var qrImg: String? = null

    }
}