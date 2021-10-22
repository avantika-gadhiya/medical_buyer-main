package bluesharklabs.com.medical.model

import bluesharklabs.com.medical.response.PartialCoverageOrderResponse

class Table {
    var tableNo: String? = null
    var status: String? = null
    var color: List<String>? = null
    var bestInPrice: PartialCoverageOrderResponse.BestInPrice? = null
    var bestInDelivery: PartialCoverageOrderResponse.BestInDelivery? = null
    var type: Int = 0
    var orderRequest: String? = null
    var orderStatus: String? = null
}