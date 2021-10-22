package bluesharklabs.com.medical.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class FilterResponse {

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

    class Data {

        @SerializedName("store_criteria_city")
        @Expose
        var city: List<String>? = null
        
        @SerializedName("store_criteria_zipcode")
        @Expose
        var zipcode: List<String>? = null

        @SerializedName("store_criteria_area")
        @Expose
        var area: List<String>? = null

        @SerializedName("order_status")
        @Expose
        var orderStatus: List<OrderStatus>? = null

        @SerializedName("store_name")
        @Expose
        var storeName: List<StoreName>? = null

        @SerializedName("order_type")
        @Expose
        var orderType: List<OrderType>? = null



        @SerializedName("offer_status")
        @Expose
        var orderOffer: List<OrderOffer>? = null


    }

    class City {
        @SerializedName("key")
        @Expose
        var key: String? = null

        @SerializedName("value")
        @Expose
        var value: String? = null

    }

    class Area {
        @SerializedName("key")
        @Expose
        var key: String? = null

        @SerializedName("value")
        @Expose
        var value: String? = null

    }

    class OrderStatus {
        @SerializedName("key")
        @Expose
        var key: String? = null

        @SerializedName("value")
        @Expose
        var value: String? = null

    }
    class OrderType {
        @SerializedName("key")
        @Expose
        var key: String? = null

        @SerializedName("value")
        @Expose
        var value: String? = null

    }

    class OrderOffer {
        @SerializedName("key")
        @Expose
        var key: String? = null

        @SerializedName("value")
        @Expose
        var value: String? = null

    }
    class StoreName {
        @SerializedName("key")
        @Expose
        var key: String? = null

        @SerializedName("value")
        @Expose
        var value: String? = null

    }
    class Zipcode {
        @SerializedName("key")
        @Expose
        var key: String? = null

        @SerializedName("value")
        @Expose
        var value: String? = null

    }


}
