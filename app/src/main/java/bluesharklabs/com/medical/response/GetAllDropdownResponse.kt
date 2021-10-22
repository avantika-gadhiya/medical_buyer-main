package bluesharklabs.com.medical.response

import bluesharklabs.com.medical.response.OrderDetailResponse.PaymentMethod
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class GetAllDropdownResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null
    @SerializedName("response")
    @Expose
    var response: String? = null
    @SerializedName("data")
    @Expose
    var data: Data? = null

    inner class Data {
        @SerializedName("store_type")
        @Expose
        var storeType: List<StoreType>? = null

        @SerializedName("product_category")
        @Expose
        var productCategory: List<ProductCategory>? = null

        @SerializedName("product_unit")
        @Expose
        var productUnit: List<ProductUnit>? = null

        @SerializedName("product_pack")
        @Expose
        var productPack: List<ProductPack>? = null

        @SerializedName("merchandise_category")
        @Expose
        var merchandiseCategory: List<MerchandiseCategory>? = null

        @SerializedName("payment_methods")
        @Expose
        var paymentMethods: List<PaymentMethod>? = null

        @SerializedName("buying_preference")
        @Expose
        var buyingPreference: List<BuyingPreference>? = null

        @SerializedName("drug_preference")
        @Expose
        var drugPreference: List<DrugPreference>? = null

        @SerializedName("ordering_for")
        @Expose
        var orderingFor: List<OrderingFor>? = null

    }
    inner class BuyingPreference {

        @SerializedName("id")
        @Expose
        var id: Int? = null
        @SerializedName("name")
        @Expose
        var name: String? = null

    }

    inner class DrugPreference {

        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("name")
        @Expose
        var name: String? = null

    }

    inner class MerchandiseCategory {

        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("name")
        @Expose
        var name: String? = null
        @SerializedName("created_date")
        @Expose
        var createdDate: String? = null

    }

    inner class ProductCategory {

        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("name")
        @Expose
        var name: String? = null
        @SerializedName("created_date")
        @Expose
        var createdDate: String? = null

    }

    inner class ProductPack {

        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("name")
        @Expose
        var name: String? = null
        @SerializedName("created_date")
        @Expose
        var createdDate: String? = null

    }
    inner class ProductUnit {

        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("name")
        @Expose
        var name: String? = null
        @SerializedName("created_date")
        @Expose
        var createdDate: String? = null

    }
    inner class StoreType {

        @SerializedName("id")
        @Expose
        var id: String? = null
        @SerializedName("name")
        @Expose
        var name: String? = null
        @SerializedName("created_date")
        @Expose
        var createdDate: String? = null
    }
    inner class OrderingFor {
        @SerializedName("id")
        @Expose
        var id: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

    }
}