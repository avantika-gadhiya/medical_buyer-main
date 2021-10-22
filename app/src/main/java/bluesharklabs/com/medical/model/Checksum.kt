package bluesharklabs.com.medical.model

import com.google.gson.annotations.SerializedName

class Checksum(
    val checksumHash: String, @field:SerializedName("ORDER_ID")
    val orderId: String, @field:SerializedName("payt_STATUS")
    val paytStatus: String
)
