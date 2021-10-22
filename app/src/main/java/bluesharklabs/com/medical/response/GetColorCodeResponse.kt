package bluesharklabs.com.medical.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class GetColorCodeResponse {

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
        var name: Any? = null
        @SerializedName("code")
        @Expose
        var code: String? = null
        @SerializedName("audit_user_id")
        @Expose
        var auditUserId: String? = null
        @SerializedName("audit_ip_addr")
        @Expose
        var auditIpAddr: String? = null
        @SerializedName("audit_action")
        @Expose
        var auditAction: String? = null
        @SerializedName("audit_updated")
        @Expose
        var auditUpdated: String? = null

    }
}