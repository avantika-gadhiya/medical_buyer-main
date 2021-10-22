package bluesharklabs.com.medical.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class PrescriptionResponse {

    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("response")
    @Expose
   var response: String? = null

    @SerializedName("data")
    @Expose
   var data: ArrayList<Datum>? = null

    inner class Datum {
        @SerializedName("order_id")
        @Expose
        var orderId: String? = null

        @SerializedName("order_no")
        @Expose
        var orderNo: String? = null

        @SerializedName("prescription_image")
        @Expose
        var prescriptionImage: String? = null

        @SerializedName("created_date")
        @Expose
        var createdDate: String? = null


    }

}