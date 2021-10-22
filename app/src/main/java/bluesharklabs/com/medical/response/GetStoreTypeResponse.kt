package bluesharklabs.com.medical.response

import bluesharklabs.com.medical.response.GetColorCodeResponse.Datum
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class GetStoreTypeResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null
    @SerializedName("response")
    @Expose
    var response: String? = null
    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null

    inner class Datum {

        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("name")
        @Expose
        var name: String? = null
        @SerializedName("created_date")
        @Expose
        var createdDate: String? = null

    }

}