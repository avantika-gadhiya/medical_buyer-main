package bluesharklabs.com.medical.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Vipul Asri on 05-12-2015.
 */

class TimeLineModel(
        var message: String,
        var date: String,
        var status: OrderStatus
)
