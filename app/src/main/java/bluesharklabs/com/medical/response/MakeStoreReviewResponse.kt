package bluesharklabs.com.medical.response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class MakeStoreReviewResponse {

    @SerializedName("status")
    @Expose
    private var status: Boolean? = null

    @SerializedName("response")
    @Expose
    private var response: String? = null

    @SerializedName("message")
    @Expose
    private var message: String? = null
}