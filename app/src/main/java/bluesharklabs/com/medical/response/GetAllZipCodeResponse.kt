package bluesharklabs.com.medical.response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class GetAllZipCodeResponse {

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
        @SerializedName("zipcode")
        @Expose
        var zipcode: String? = null

    }
}