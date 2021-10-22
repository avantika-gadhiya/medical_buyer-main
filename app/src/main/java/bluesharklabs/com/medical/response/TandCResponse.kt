package bluesharklabs.com.medical.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class TandCResponse {

    @SerializedName("status")
    @Expose
    private var status: Boolean? = null

    @SerializedName("response")
    @Expose
    private var response: String? = null

    @SerializedName("data")
    @Expose
    private var data: List<Datum?>? = null

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

    fun getData(): List<Datum?>? {
        return data
    }

    fun setData(data: List<Datum?>?) {
        this.data = data
    }


   inner class Datum {
        @SerializedName("id")
        @Expose
        var id: String? = null

        @SerializedName("buyer_terms_conditions")
        @Expose
        var buyerTermsConditions: String? = null

        @SerializedName("store_terms_conditions")
        @Expose
        var storeTermsConditions: String? = null

        @SerializedName("created_date")
        @Expose
        var createdDate: String? = null
    }

}