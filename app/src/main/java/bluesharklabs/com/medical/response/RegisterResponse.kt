package bluesharklabs.com.medical.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class RegisterResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null
    @SerializedName("response")
    @Expose
    var response: String? = null
    @SerializedName("data")
    @Expose
    var data: Data? = null
    @SerializedName("message")
    @Expose
    var message: String? = null

    inner class Data {

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
        @SerializedName("user_id")
        @Expose
        var userid: String? = null

    }
}