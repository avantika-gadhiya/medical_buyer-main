package bluesharklabs.com.medical.response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class GetAllCityResponse {

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
        @SerializedName("city")
        @Expose
        var city: String? = null

    }

}