package bluesharklabs.com.medical.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ConsultDoctorResponse {

    @SerializedName("status")
    @Expose
    private var status: Boolean? = null

    @SerializedName("response")
    @Expose
    private var response: String? = null

    @SerializedName("data")
    @Expose
    var data: ArrayList<Datum>? = null

    class Datum {
        @SerializedName("id")
        @Expose
        var id: String? = null

        @SerializedName("doctor_name")
        @Expose
        var doctorName: String? = null

        @SerializedName("doctor_phone")
        @Expose
        var doctorPhone: String? = null

        @SerializedName("profile_pic")
        @Expose
        var profilePic: String? = null

        @SerializedName("created_date")
        @Expose
        var createdDate: String? = null
    }

}