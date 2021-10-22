package bluesharklabs.com.medical.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class BuyerDetailResponse :Serializable{
    @SerializedName("status")
    @Expose
    private var status: Boolean? = null

    @SerializedName("response")
    @Expose
    private var response: String? = null

    @SerializedName("data")
    @Expose
    private var data: Data? = null

    fun getStatus(): Boolean? {
        return status
    }

    fun setStatus(status: Boolean?) {
        this.status = status
    }

    fun getResponse(): String? {
        return response
    }

    fun setResponse(response: String?) {
        this.response = response
    }

    fun getData(): Data? {
        return data
    }

    fun setData(data: Data?) {
        this.data = data
    }

    class Data :Serializable{
        @SerializedName("id")
        @Expose
        var id: String? = null

        @SerializedName("fullname")
        @Expose
        var fullname: String? = null

        @SerializedName("phone")
        @Expose
        var phone: String? = null

        @SerializedName("age")
        @Expose
        var age: String? = null

        @SerializedName("gender")
        @Expose
        var gender: String? = null

        @SerializedName("address_line_1")
        @Expose
        var addressLine1: String? = null

        @SerializedName("address_line_2")
        @Expose
        var addressLine2: String? = null

        @SerializedName("landmark")
        @Expose
        var landmark: String? = null

        @SerializedName("city")
        @Expose
        var city: String? = null

        @SerializedName("zipcode")
        @Expose
        var zipcode: String? = null

        @SerializedName("profile_pic")
        @Expose
        var profilePic: String? = null

        @SerializedName("profile_pic_thumb")
        @Expose
        var profilePicThumb: String? = null

        @SerializedName("photo_id")
        @Expose
        var photoId: String? = null

        @SerializedName("user_status")
        @Expose
        var userStatus: String? = null

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

        @SerializedName("last_modefied")
        @Expose
        var lastModefied: Any? = null

        @SerializedName("last_login")
        @Expose
        var lastLogin: Any? = null

        @SerializedName("gender_id")
        @Expose
        var genderId: String? = null

    }

}