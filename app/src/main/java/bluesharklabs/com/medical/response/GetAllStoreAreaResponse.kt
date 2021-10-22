package bluesharklabs.com.medical.response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class GetAllStoreAreaResponse {
    @SerializedName("status")
    @Expose
     val status: Boolean? = null

    @SerializedName("response")
    @Expose
     val response: String? = null

    @SerializedName("data")
    @Expose
    val data: List<Datum>? = null

    class Datum {
        @SerializedName("area")
        @Expose
        var area: String? = null

    }
}