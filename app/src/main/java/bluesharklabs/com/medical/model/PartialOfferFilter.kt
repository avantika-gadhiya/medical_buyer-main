package bluesharklabs.com.medical.model

import java.io.Serializable
import java.util.*

class PartialOfferFilter : Serializable {
    var order_id = ""
    var from_dateandTime = ""
    var to_dateandTime = ""
    var is_selected = ""
    var color_id = listOf<String>()


}