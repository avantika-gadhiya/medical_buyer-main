package bluesharklabs.com.medical.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class NotificationResponse {
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

        @SerializedName("user_type")
        @Expose
        var userType: String? = null

        @SerializedName("user_id")
        @Expose
        var userId: String? = null

        @SerializedName("title")
        @Expose
        var title: String? = null

        @SerializedName("text")
        @Expose
        var text: String? = null

        @SerializedName("created_date")
        @Expose
        var createdDate: String? = null
    }

}