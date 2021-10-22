package bluesharklabs.com.medical.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VerificationResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("response")
    @Expose
    var response: String? = null

    @SerializedName("otp")
    @Expose
    var otp: Int? = null
}