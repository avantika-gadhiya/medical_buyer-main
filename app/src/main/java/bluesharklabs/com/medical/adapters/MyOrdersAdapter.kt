package bluesharklabs.com.medical.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.activities.*
import bluesharklabs.com.medical.response.MyOrderResponse
import bluesharklabs.com.medical.response.OrderListResponse
import bluesharklabs.com.medical.utils.AppConstant

class MyOrdersAdapter( var context: Context,var myOrder:ArrayList<MyOrderResponse.Datum>
): RecyclerView.Adapter<MyOrdersAdapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyOrdersAdapter.MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_my_orders, parent, false)
        return MyOrdersAdapter.MyHolder(v)
    }

    override fun getItemCount(): Int {
        return myOrder.size
    }

    override fun onBindViewHolder(holder: MyOrdersAdapter.MyHolder, position: Int) {

        var reorder = ""
        holder.txt_status1.setText(myOrder[position].requestStatus)
        holder.txt_status.setText(myOrder[position].orderStatus)
        holder.txt_id.setText("ID: " + myOrder[position].orderNo)
        holder.text_dtntime.setText( myOrder[position].createdDate.toString())

        try {
            Log.e("HEllpoCOlorError","Color   "+myOrder[position].requestStatus!!)
            DrawableCompat.setTint(
                    holder.txt_status1.getBackground(),
                    ContextCompat.getColor(
                            context,
                            AppConstant.setsStatus(myOrder[position].requestStatus!!)
                    )
            )
            DrawableCompat.setTint(
                    holder.txt_status.getBackground(),
                    ContextCompat.getColor(
                            context,
                            AppConstant.setsStatus(myOrder[position].orderStatus!!)
                    )
            )
        }catch (e:Exception){
            Log.e("HEllpoCOlorError","Error   "+e)
        }



        if (myOrder[position].requestStatus.equals(">24 hrs", ignoreCase = true) &&
                myOrder[position].orderStatus.equals("Pending", ignoreCase = true)) {
            holder.txt_time_elapsed.visibility = View.GONE
            holder.txt_edt_n_upload_again.visibility = View.VISIBLE
        } else if (myOrder[position].requestStatus.equals(">24 hrs", ignoreCase = true)) {
            reorder = "1"
            holder.txt_time_elapsed.visibility = View.VISIBLE
            holder.txt_edt_n_upload_again.visibility = View.GONE
        } else {
            reorder = holder.timer
            holder.txt_time_elapsed.visibility = View.VISIBLE
            holder.txt_edt_n_upload_again.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            if (myOrder[position].requestStatus.equals(">24 hrs", ignoreCase = true) && myOrder[position].orderStatus.equals("Pending", ignoreCase = true)) {
                if (myOrder[position].orderTypeName.equals("Custom", ignoreCase = true)) {
                    context.startActivity(
                            Intent(context, CustomFinalOrderActivity::class.java)
                                    .putExtra("reditorder", "1")
                                    .putExtra("orderid", myOrder[position].orderId)
                    )
                } else {

                    context.startActivity(
                            Intent(context, FinalOrderActivity::class.java)
                                    .putExtra("reditorder", "1")
                                    .putExtra("orderid", myOrder[position].orderId)
                    )

                }
            } else if (myOrder[position].requestStatus.equals("Active", ignoreCase = true) && myOrder[position].orderStatus.equals("Delivered", ignoreCase = true)) {

                if (myOrder[position].orderTypeName.equals("Custom", ignoreCase = true)) {

                    context.startActivity(
                            Intent(context, TrackOrderActivity::class.java)
                                    .putExtra("orderType", myOrder[position].orderType)
                                    .putExtra("orderid", myOrder[position].orderId)
                                    .putExtra("offerId", myOrder[position].offerId))



                } else {
                    context.startActivity(
                            Intent(context, TrackOrderActivity::class.java)
                                    .putExtra("orderid", myOrder[position].orderId)
                                    .putExtra("offerId", myOrder[position].offerId)
                                    .putExtra("orderType", myOrder[position].orderType))

                }

            } else if (myOrder[position].orderPlatformValue.equals("2")) {
                if (myOrder[position].orderStatus.equals("Offer Made", ignoreCase = true) ||
                        myOrder[position].orderStatus.equals("Pending", ignoreCase = true) ||
                        myOrder[position].orderStatus.equals("Invoiced", ignoreCase = true) ||
                        myOrder[position].orderStatus.equals("Accepted", ignoreCase = true)) {
                    context.startActivity(
                            Intent(context, StoreOrderAcceptedActivity::class.java)
                                    .putExtra("orderid", myOrder[position].orderId)
                                    .putExtra("status", myOrder[position].orderStatus)
                    )

                } else if (myOrder[position].orderTypeName.equals("Custom", ignoreCase = true)) {
                    context.startActivity(
                            Intent(context, CustomFinalOrderActivity::class.java)
                                    .putExtra("reorder", reorder)
                                    .putExtra("orderid", myOrder[position].orderId)
                    )
                } else {

                    context.startActivity(
                            Intent(context, FinalOrderActivity::class.java)
                                    .putExtra("reorder", reorder)
                                    .putExtra("orderid", myOrder[position].orderId)
                    )

                }
            } else {
                if (myOrder[position].orderStatus.equals("Offer Made", ignoreCase = true)) {
                    context.startActivity(
                            Intent(context, OfferAcceptedActivity::class.java)
                                    .putExtra("orderid", myOrder[position].orderId)
                    )

                } else if (myOrder[position].orderStatus.equals("Invoiced", ignoreCase = true) ||
                        myOrder[position].orderStatus.equals("Accepted", ignoreCase = true)) {
                    context.startActivity(
                            Intent(context, FullCoverageActivity::class.java)
                                    .putExtra("orderid", myOrder[position].orderId)
                                    .putExtra("status", myOrder[position].orderStatus)
                                .putExtra("isAvailable", "1")
                    )
                } else if (myOrder[position].orderTypeName.equals("Custom", ignoreCase = true)) {
                    context.startActivity(
                            Intent(context, CustomFinalOrderActivity::class.java)
                                    .putExtra("reorder", reorder)
                                    .putExtra("orderid", myOrder[position].orderId)
                    )
                } else {

                    context.startActivity(
                            Intent(context, FinalOrderActivity::class.java)
                                    .putExtra("reorder", reorder)
                                    .putExtra("orderid", myOrder[position].orderId)
                    )

                }
            }


        }

    }
    class MyHolder(itemview: View) : RecyclerView.ViewHolder(itemview)  {

        var txt_id=itemview.findViewById<AppCompatTextView>(R.id.txt_id)
        var text_dtntime=itemview.findViewById<AppCompatTextView>(R.id.text_dtntime)
        var txt_status1=itemview.findViewById<AppCompatTextView>(R.id.txt_status1)
        var txt_status=itemview.findViewById<AppCompatTextView>(R.id.txt_status)
        var txt_time_elapsed=itemview.findViewById<AppCompatTextView>(R.id.txt_time_elapsed)
        var txt_edt_n_upload_again=itemview.findViewById<AppCompatTextView>(R.id.txt_edt_n_upload_again)
        var img_edit=itemview.findViewById<AppCompatImageView>(R.id.img_edit)
        var timer = ""
    }


}