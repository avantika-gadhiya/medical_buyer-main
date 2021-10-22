package bluesharklabs.com.medical.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class SkipPaymentBuyerResponse {

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
        @SerializedName("payment_verify_date")
        @Expose
        var paymentVerifyDate: String? = null

    }
}