package bluesharklabs.com.medical.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class GetAllProductColorsResponse {

    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("response")
    @Expose
    var response: String? = null

    @SerializedName("data")
    @Expose
    val data: Datum? = null

    inner class Datum {

        @SerializedName("color")
        @Expose
        val color: List<String>? = null
    }
}