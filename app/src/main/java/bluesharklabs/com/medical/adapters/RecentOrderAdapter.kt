package bluesharklabs.com.medical.adapters

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.Chronometer.OnChronometerTickListener
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.activities.*
import bluesharklabs.com.medical.response.OrderListResponse
import bluesharklabs.com.medical.utils.AppConstant
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class RecentOrderAdapter(
    var context: Context,
    var edtEntry: editEntry,
    var viewAll: String,
    var orderListdata: List<OrderListResponse.Datum>
) : RecyclerView.Adapter<RecentOrderAdapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.item_recent_order, parent, false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int {
        if (viewAll.equals("0") && orderListdata.size > 3)
            return 3
        else
            return orderListdata.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyHolder, pos: Int) {
        var reorder = ""
        holder.txtStatus1.text = orderListdata[pos].requestStatus
        holder.txtStatus.text = orderListdata[pos].orderStatus
        holder.txtId.text = "ID: " + orderListdata[pos].orderNo
        // val secDate = SimpleDateFormat("dd MMM,yyyy  hh:mm a").format(oneDate)

        holder.textDtntime.text = (orderListdata[pos].createdDate.toString())

        val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US)
        val cal1 = Calendar.getInstance()
        val currDate1 = dateFormat.format(cal1.time)

        cal1.time = dateFormat.parse(orderListdata[pos].createdDate.toString())
        cal1.get(Calendar.HOUR_OF_DAY)
        val convertedDate1 = dateFormat.format(cal1.time)
        val stopTime1 = dateFormat.parse(convertedDate1)
        val startTime1 = dateFormat.parse(currDate1)
        val difference1 = startTime1.time - stopTime1.time
        val day1 = TimeUnit.MILLISECONDS.toDays(difference1)
        val hh1 = (TimeUnit.MILLISECONDS.toHours(difference1) - TimeUnit.DAYS.toHours(day1))
        val mm1 = (TimeUnit.MILLISECONDS.toMinutes(difference1) - TimeUnit.HOURS.toMinutes(
            TimeUnit.MILLISECONDS.toHours(difference1)
        ))


        try {
            holder.simpleChronometer.base = stopTime1.time
            holder.simpleChronometer.onChronometerTickListener = OnChronometerTickListener {
                var t: Long = System.currentTimeMillis() - holder.simpleChronometer.base


                val days = TimeUnit.MILLISECONDS.toDays(t)
                t -= TimeUnit.DAYS.toMillis(days)
                val hours = TimeUnit.MILLISECONDS.toHours(t)
                t -= TimeUnit.HOURS.toMillis(hours)
                val minutes = TimeUnit.MILLISECONDS.toMinutes(t)
                t -= TimeUnit.MINUTES.toMillis(minutes)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(t)
                var stopwatchDisplay = "%dd %dh %dm %ds"
                stopwatchDisplay = String.format(stopwatchDisplay, days, hours, minutes, seconds)
                Log.e("HelloWatch", "Hiii   " + stopwatchDisplay)
                //                holder.txtTimeElapsed.setText("Time Elapsed: "+stopwatchDisplay)
                holder.simpleChronometer.text = "Time Elapsed: " + stopwatchDisplay
            }
            holder.simpleChronometer.start()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        holder.timer = ("" + hh1 + "h:" + mm1 + "m")
//        holder.simpleChronometer.visibility =View.INVISIBLE
        DrawableCompat.setTint(
            holder.txtStatus1.background,
            ContextCompat.getColor(
                context,
                AppConstant.setsStatus(orderListdata[pos].requestStatus!!)
            )
        )
        DrawableCompat.setTint(
            holder.txtStatus.background,
            ContextCompat.getColor(
                context,
                AppConstant.setsStatus(orderListdata[pos].orderStatus!!)
            )
        )

        Log.e("HelloOrderStatus", "Status   " + orderListdata[pos].requestStatus!!)

        if (orderListdata[pos].requestStatus.equals(
                ">24 hrs",
                ignoreCase = true
            ) && orderListdata[pos].orderStatus.equals("Pending", ignoreCase = true)
        ) {
            holder.simpleChronometer.visibility = View.GONE
            holder.txtEdtnUploadAgain.visibility = View.VISIBLE
        } else if (orderListdata[pos].requestStatus.equals(">24 hrs", ignoreCase = true)) {
            reorder = "1"
            holder.simpleChronometer.visibility = View.VISIBLE
            holder.txtEdtnUploadAgain.visibility = View.GONE
        } else {
            reorder = holder.timer
            holder.simpleChronometer.visibility = View.VISIBLE
            holder.txtEdtnUploadAgain.visibility = View.GONE

        }

        if (orderListdata[pos].requestStatus.equals(
                "Timed Out",
                ignoreCase = true
            ) && orderListdata[pos].orderStatus.equals("Pending", ignoreCase = true)
        ) {
            holder.simpleChronometer.visibility = View.GONE
            holder.txtEdtnUploadAgain.visibility = View.VISIBLE
        } else if (orderListdata[pos].requestStatus.equals("Timed Out", ignoreCase = true)) {
            reorder = "1"
            holder.simpleChronometer.visibility = View.VISIBLE
            holder.txtEdtnUploadAgain.visibility = View.GONE
        } else {
            reorder = holder.timer
            holder.simpleChronometer.visibility = View.VISIBLE
            holder.txtEdtnUploadAgain.visibility = View.GONE

        }

        holder.imgEdit.setOnClickListener {

            edtEntry.edt(pos)
        }

        holder.itemView.setOnClickListener {

            Log.e(
                "MyOrderStatus",
                "orderListdata[pos].requestStatus   " + orderListdata[pos].requestStatus + "   " + orderListdata[pos].orderStatus
            )

            if (orderListdata[pos].requestStatus.equals(
                    ">24 hrs",
                    ignoreCase = true
                ) && orderListdata[pos].orderStatus.equals("Pending", ignoreCase = true)
            ) {
                Log.e(
                    "MyOrderStatus",
                    ">24   " + orderListdata[pos].requestStatus + "   " + orderListdata[pos].orderStatus
                )
                if (orderListdata[pos].orderTypeName.equals("Custom", ignoreCase = true)) {
                    context.startActivity(
                        Intent(context, CustomFinalOrderActivity::class.java)
                            .putExtra("reditorder", "1")
                            .putExtra("orderid", orderListdata[pos].orderId)
                    )
                } else {
                    context.startActivity(
                        Intent(context, FinalOrderActivity::class.java)
                            .putExtra("reditorder", "1")
                            .putExtra("orderid", orderListdata[pos].orderId)
                    )
                }
            } else if (orderListdata[pos].requestStatus.equals(
                    "Timed Out",
                    ignoreCase = true
                ) && orderListdata[pos].orderStatus.equals("Pending", ignoreCase = true)
            ) {
                Log.e(
                    "MyOrderStatus",
                    "Timeoutsss   " + orderListdata[pos].requestStatus + "   " + orderListdata[pos].orderStatus
                )
                if (orderListdata[pos].orderTypeName.equals("Custom", ignoreCase = true)) {
                    context.startActivity(
                        Intent(context, CustomFinalOrderActivity::class.java)
                            .putExtra("reditorder", "1")
                            .putExtra("orderid", orderListdata[pos].orderId)
                    )
                } else {

                    context.startActivity(
                        Intent(context, FinalOrderActivity::class.java)
                            .putExtra("reditorder", "1")
                            .putExtra("orderid", orderListdata[pos].orderId)
                    )
                }

            } else if (orderListdata[pos].requestStatus.equals(
                    "Active",
                    ignoreCase = true
                ) && orderListdata[pos].orderStatus.equals("Delivered", ignoreCase = true)
            ) {
                Log.e(
                    "MyOrderStatus",
                    "Active   " + orderListdata[pos].requestStatus + "   " + orderListdata[pos].orderStatus
                )
                if (orderListdata[pos].orderTypeName.equals("Custom", ignoreCase = true)) {

                    context.startActivity(
                        Intent(context, TrackOrderActivity::class.java)
                            .putExtra("orderType", orderListdata[pos].orderType)
                            .putExtra("orderid", orderListdata[pos].orderId)
                            .putExtra("offerId", orderListdata[pos].offerId)
                    )


                    /*  context.startActivity(
                              Intent(context, CustomFinalOrderActivity::class.java)
                                      .putExtra("reorder", "1")
                                      .putExtra("orderid", orderListdata[pos].orderId)
                      )*/
                } else {
                    context.startActivity(
                        Intent(context, TrackOrderActivity::class.java)
                            .putExtra("orderid", orderListdata[pos].orderId)
                            .putExtra("offerId", orderListdata[pos].offerId)
                            .putExtra("orderType", orderListdata[pos].orderType)
                    )
                    /* context.startActivity(
                             Intent(context, FinalOrderActivity::class.java)
                                     .putExtra("reorder", "1")
                                     .putExtra("orderid", orderListdata[pos].orderId)
                     )*/

                }

            } else if (orderListdata[pos].orderPlatformValue.equals("2")) {
                Log.e(
                    "MyOrderStatus",
                    "Platform2   " + orderListdata[pos].requestStatus + "   " + orderListdata[pos].orderStatus
                )
                if (orderListdata[pos].orderStatus.equals(
                        "Offer Made",
                        ignoreCase = true
                    ) || orderListdata[pos].orderStatus.equals("Invoiced", ignoreCase = true) ||
                    orderListdata[pos].orderStatus.equals("Accepted", ignoreCase = true)
                ) {

                    Log.e(
                        "MyOrderStatus",
                        "IFFF   " + orderListdata[pos].requestStatus + "   " + orderListdata[pos].orderStatus
                    )
                    context.startActivity(
                        Intent(context, StoreOrderAcceptedActivity::class.java)
                            .putExtra("orderid", orderListdata[pos].orderId)
                            .putExtra("status", orderListdata[pos].orderStatus)
                    )

                } else if (orderListdata[pos].orderTypeName.equals("Custom", ignoreCase = true)) {
                    if (orderListdata[pos].orderStatus.equals("Delivered")) {
                        context.startActivity(
                            Intent(context, TrackOrderActivity::class.java)
                                .putExtra("orderid", orderListdata[pos].orderId)
                                .putExtra("offerId", orderListdata[pos].offerId)
                                .putExtra("orderType", orderListdata[pos].orderType)
                        )
                    } else {
                        Log.e(
                            "MyOrderStatus",
                            "ELSEE IFFF   " + orderListdata[pos].requestStatus + "   " + orderListdata[pos].orderStatus + "   " + orderListdata[pos].reOrder
                        )
                        context.startActivity(
                            Intent(context, CustomFinalOrderActivity::class.java)
                                .putExtra("reorder", orderListdata[pos].reOrder)
                                .putExtra("orderid", orderListdata[pos].orderId)
//                                    .putExtra("Timer", holder.timer)
                        )
                    }

                } else {
                    Log.d("TAG", "orderListdata[pos].reOrder" + orderListdata[pos].reOrder)
                    Log.e(
                        "MyOrderStatus",
                        "ELSEE   " + orderListdata[pos].requestStatus + "   " + orderListdata[pos].orderStatus
                    )
                    if (orderListdata[pos].orderStatus.equals("Delivered", ignoreCase = true)) {
                        context.startActivity(
                            Intent(context, TrackOrderActivity::class.java)
                                .putExtra("orderid", orderListdata[pos].orderId)
                                .putExtra("offerId", orderListdata[pos].offerId)
                                .putExtra("orderType", orderListdata[pos].orderType)
                        )
                    } else {
                        context.startActivity(
                            Intent(context, FinalOrderActivity::class.java)
                                .putExtra("reorder", orderListdata[pos].reOrder)
                                .putExtra("orderid", orderListdata[pos].orderId)
                        )
                    }


                }
            } else {
                Log.e(
                    "MyOrderStatus",
                    "Last Else   " + orderListdata[pos].requestStatus + "   " + orderListdata[pos].orderStatus
                )

                if (orderListdata[pos].orderStatus.equals("Offer Made", ignoreCase = true)) {
                    context.startActivity(
                        Intent(context, OfferAcceptedActivity::class.java)
                            .putExtra("orderid", orderListdata[pos].orderId)
                    )

                } else if (orderListdata[pos].orderStatus.equals("Invoiced", ignoreCase = true) ||
                    orderListdata[pos].orderStatus.equals("Accepted", ignoreCase = true)
                ) {
                    context.startActivity(
                        Intent(context, FullCoverageActivity::class.java)
                            .putExtra("orderid", orderListdata[pos].orderId)
                            .putExtra("status", orderListdata[pos].orderStatus)
                            .putExtra("isAvailable", "1")
                    )
                } /*else if (orderListdata[pos].orderStatus.equals("Accepted", ignoreCase = true)) {
                    context.startActivity(
                            Intent(context, FullCoverageActivity::class.java)
                                    .putExtra("orderid", orderListdata[pos].orderId)
                    )
                } */ else if (orderListdata[pos].orderTypeName.equals(
                        "Custom",
                        ignoreCase = true
                    )
                ) {

                    if (orderListdata[pos].orderStatus.equals("Delivered", ignoreCase = true)) {
                        context.startActivity(
                            Intent(context, TrackOrderActivity::class.java)
                                .putExtra("orderid", orderListdata[pos].orderId)
                                .putExtra("offerId", orderListdata[pos].offerId)
                                .putExtra("orderType", orderListdata[pos].orderType)
                        )
                    } else {
                        context.startActivity(
                            Intent(context, CustomFinalOrderActivity::class.java)
                                .putExtra("reorder", orderListdata[pos].reOrder)
                                .putExtra("orderid", orderListdata[pos].orderId)
                                .putExtra("Timer", holder.timer)
                        )
                    }


                } else {

                    if (orderListdata[pos].orderStatus.equals("Delivered", ignoreCase = true)) {
                        context.startActivity(
                            Intent(context, TrackOrderActivity::class.java)
                                .putExtra("orderid", orderListdata[pos].orderId)
                                .putExtra("offerId", orderListdata[pos].offerId)
                                .putExtra("orderType", orderListdata[pos].orderType)
                        )
                    } else {
                        context.startActivity(
                            Intent(context, FinalOrderActivity::class.java)
                                .putExtra("reorder", orderListdata[pos].reOrder)
                                .putExtra("orderid", orderListdata[pos].orderId)
                                .putExtra("Timer", holder.timer)
                        )
                    }

                }
            }
        }
    }

    class MyHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val txtId = itemView.findViewById(R.id.txt_id) as AppCompatTextView
        val textDtntime = itemView.findViewById(R.id.text_dtntime) as AppCompatTextView
        val txtStatus1 = itemView.findViewById(R.id.txt_status1) as AppCompatTextView
        val txtStatus = itemView.findViewById(R.id.txt_status) as AppCompatTextView
        val txtTimeElapsed = itemView.findViewById(R.id.txt_time_elapsed) as AppCompatTextView
        val simpleChronometer = itemView.findViewById(R.id.simpleChronometer) as Chronometer
        val txtEdtnUploadAgain =
            itemView.findViewById(R.id.txt_edt_n_upload_again) as AppCompatTextView
        val imgEdit = itemView.findViewById(R.id.img_edit) as AppCompatImageView
        var timer = ""
    }

    interface editEntry {
        fun edt(pos: Int)
    }

    fun getDate(milliSeconds: Long): String? {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US)

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }
}