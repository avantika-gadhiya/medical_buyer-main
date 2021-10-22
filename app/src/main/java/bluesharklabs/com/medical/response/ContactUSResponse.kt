package bluesharklabs.com.medical.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ContactUSResponse {


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

        @SerializedName("store_phone")
        @Expose
        var storePhone: String? = null

        @SerializedName("store_email")
        @Expose
        var storeEmail: String? = null

        @SerializedName("buyer_phone")
        @Expose
        var buyerPhone: String? = null

        @SerializedName("buyer_email")
        @Expose
        var buyerEmail: String? = null

        @SerializedName("created_date")
        @Expose
        var createdDate: String? = null
    }

}