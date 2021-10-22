package bluesharklabs.com.medical.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.activities.*
import bluesharklabs.com.medical.model.Table
import bluesharklabs.com.medical.utils.PinnedSectionListView
import kotlinx.android.synthetic.main.activity_full_coverage.*
import java.util.*


class PartialCoverageAdapter(
    internal var context: Context,
    var offeracc: offerAcc
) : BaseAdapter(),
    PinnedSectionListView.PinnedSectionListAdapter {

    internal var count = 0

    var mData = ArrayList<Table>()
    private val sectionHeader = TreeSet<Int>()
    private val mInflater: LayoutInflater
    internal var holder: ViewHolder? = null

    init {
        mInflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    fun addItem(level1Data: Table) {
        mData.add(level1Data)
        notifyDataSetChanged()
    }

    fun addSectionHeaderItem(level1Data: Table) {
        mData.add(level1Data)

        count++
        sectionHeader.add(mData.size - 1)
        notifyDataSetChanged()
    }

    fun clearData() {

        mData.clear()
        notifyDataSetChanged()
    }


    override fun getItemViewType(position: Int): Int {
        return if (sectionHeader.contains(position)) TYPE_SEPARATOR else TYPE_ITEM
    }

    override fun getViewTypeCount(): Int {
        return TYPE_MAX_COUNT
    }

    override fun getCount(): Int {
        return mData.size
    }

    override fun getItem(position: Int): ArrayList<Table> {
        return mData
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun isEnabled(position: Int): Boolean {
        return getItemViewType(position) != TYPE_SEPARATOR
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val rowType = getItemViewType(position)


        if (convertView == null) {
            holder = ViewHolder()
            when (rowType) {
                TYPE_SEPARATOR -> {
                    convertView = mInflater.inflate(
                        R.layout.recycl_header_partial_cvrage, parent,
                        false
                    )
                    holder!!.txt_title = convertView!!.findViewById(R.id.textView121)
                    holder!!.ll_code1 = convertView.findViewById(R.id.ll_code)


                    holder!!.ll_code1?.removeAllViews()
                    for (i in 0 until mData[position].color!!.size) {
                        val layout = LinearLayout(context)
                        layout.id = i
                        layout.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        layout.orientation = LinearLayout.HORIZONTAL
                        layout.setPadding(10, 0, 0, 0)

                        val imageView = ImageView(context)
                        imageView.layoutParams = LinearLayout.LayoutParams(40, 40)
                        imageView.setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.custom_color_code
                            )
                        )

                        imageView.setColorFilter(
                            Color.parseColor(
                                "#" + mData[position].color!!.get(
                                    i
                                )
                            )
                        )

                        imageView.setPadding(0, 0, 0, 0)

                        layout.addView(imageView)

                        holder!!.ll_code1?.addView(layout)
                    }


                }
                TYPE_ITEM -> {
                    convertView =
                        mInflater.inflate(R.layout.recycl_item_partial_cvrage, parent, false)


                    holder!!.txtBipDelType = convertView!!.findViewById(R.id.txt_bip_del_type)
                    holder!!.txtBipViewOffer = convertView.findViewById(R.id.txt_bip_view_offer)
                    holder!!.txtBipDelDate = convertView.findViewById(R.id.txt_bip_del_date)
                    holder!!.txtBipDelTime = convertView.findViewById(R.id.txt_bip_del_time)
                    holder!!.txtBipDDate = convertView.findViewById(R.id.txt_1)
                    holder!!.txtBipDTime = convertView.findViewById(R.id.txt_2)
                    holder!!.txtBipDelPerc = convertView.findViewById(R.id.txt_bip_del_perc)
                    holder!!.txtBipPrice = convertView.findViewById(R.id.txt_bip_price)
                    holder!!.txtBipReorder = convertView.findViewById(R.id.txt_bip_reorder)
                    holder!!.txtBipAccept = convertView.findViewById(R.id.txt_bip_accept)
                    holder!!.txtBipAcceptText = convertView.findViewById(R.id.txt_bip_accept_text)
                    holder!!.constrain_delivry_time_out_OM =
                        convertView.findViewById(R.id.constrain_delivry_time_out_OM)

                    holder!!.txtBidDelType = convertView.findViewById(R.id.txt_bid_del_typ)
                    holder!!.txtBidViewOffer = convertView.findViewById(R.id.txt_bid_view_offer)
                    holder!!.txtBidDelDate = convertView.findViewById(R.id.txt_bid_del_date)
                    holder!!.txtBidDelTime = convertView.findViewById(R.id.txt_bid_del_time)
                    holder!!.txtBidDDate = convertView.findViewById(R.id.txt_del_1)
                    holder!!.txtBidDTime = convertView.findViewById(R.id.txt_del_2)
                    holder!!.txtBidPerc = convertView.findViewById(R.id.txt_bid_perc)
                    holder!!.txtBidPrice = convertView.findViewById(R.id.txt_bid_price)
                    holder!!.txtBidReorder = convertView.findViewById(R.id.txt_bid_reorder)
                    holder!!.txtBidAccept = convertView.findViewById(R.id.txt_bid_accept)
                    holder!!.txtBidAcceptText = convertView.findViewById(R.id.txt_bid_accept_text)
                    holder!!.constrain_delivry_time_out_OM_BED =
                        convertView.findViewById(R.id.constrain_delivry_time_out_OM_BED)


                }
            }
            convertView!!.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        when (rowType) {
            TYPE_SEPARATOR ->

                holder!!.txt_title!!.text = mData[position].status
            TYPE_ITEM -> {


                /* if (mData.get(position).bestInPrice!!.deliveryType!!.equals("0")) {
                     holder!!.txtBipDelDate!!.text = "-"
                     holder!!.txtBipDelTime!!.text = "-"
                 } else*/

                Log.e("HelloDatas", "orderStatus   " + mData.get(position).orderStatus)
                Log.e("HelloDatas", "orderRequest   " + mData.get(position).orderRequest)


                if (mData.get(position).bestInPrice!!.offerDeliveryDate!!.equals("")) {
                    if (mData.get(position).bestInPrice!!.deliveryType!!.equals("0")) {
                        holder!!.txtBipDDate!!.text = context.getString(R.string.pickup_date)
                        holder!!.txtBipDTime!!.text = context.getString(R.string.pickup_time)
                    }
                    holder!!.txtBipDelType!!.text =
                        mData.get(position).bestInPrice!!.deliveryTypeName



                    holder!!.txtBipDelTime!!.text =
                        mData.get(position).bestInPrice!!.deliveryTimeStart!! + " to " + mData.get(
                            position
                        ).bestInPrice!!.deliveryTimeEnd!!
//                    holder!!.txtBipDelDate!!.text =
//                        mData.get(position).bestInPrice!!.toDateFromDate!!


                    val tempDate0 = mData.get(position).bestInPrice!!.toDateFromDate!!?.split("to")?.get(0)
                    val tempDate1 = mData.get(position).bestInPrice!!.toDateFromDate!!?.split("to")?.get(1)
                    holder!!.txtBipDelDate!!.text = (tempDate0.toString().trim() + " to\n" + tempDate1.toString().trim())
//                    holder!!.txtBipDelDate!!.text = mData.get(position).bestInPrice!!.deliveryDate!!

                    //Commented By Rahul Bambhaniya

//                    holder!!.txtBipDelDate!!.text =
//                            AppConstant.convertDelDate2(mData.get(position).bestInPrice!!.deliveryDate!!)
//                    holder!!.txtBipDelTime!!.text = AppConstant.convertTime(
//                            mData.get(position).bestInPrice!!.deliveryTimeStart!!,
//                            mData.get(position).bestInPrice!!.deliveryTimeEnd!!
//                    )

                } else {
                    if (mData.get(position).bestInPrice!!.offerDeliveryPreference!!.equals("0")) {
                        holder!!.txtBipDDate!!.text = context.getString(R.string.pickup_date)
                        holder!!.txtBipDTime!!.text = context.getString(R.string.pickup_time)
                    }
                    holder!!.txtBipDelType!!.text =
                        mData.get(position).bestInPrice!!.offerDeliveryPreferenceName

                    holder!!.txtBipDelTime!!.text =
                        mData.get(position).bestInPrice!!.offerDeliveryTimeStart!! + " to " + mData.get(
                            position
                        ).bestInPrice!!.offerDeliveryTimeEnd!!
//                    holder!!.txtBipDelDate!!.text =
//                        mData.get(position).bestInPrice!!.toDateFromDate!!
                    val tempDate0 = mData.get(position).bestInPrice!!.toDateFromDate!!?.split("to")?.get(0)
                    val tempDate1 = mData.get(position).bestInPrice!!.toDateFromDate!!?.split("to")?.get(1)
                    holder!!.txtBipDelDate!!.text = (tempDate0.toString().trim() + " to\n" + tempDate1.toString().trim())
//                    holder!!.txtBipDelDate!!.text = mData.get(position).bestInPrice!!.offerDeliveryDate!!

                    //Commented By Rahul Bambhaniya
//                    holder!!.txtBipDelTime!!.text = AppConstant.convertTime(
//                        mData.get(position).bestInPrice!!.offerDeliveryTimeStart!!,
//                        mData.get(position).bestInPrice!!.offerDeliveryTimeEnd!!
//                    )
//                    holder!!.txtBipDelDate!!.text =
//                            AppConstant.convertDelDate2(mData.get(position).bestInPrice!!.offerDeliveryDate!!)
                }
                holder!!.txtBipDelPerc!!.text =
                    mData.get(position).bestInPrice!!.offerDiscountPercentage + "% OFF"
                holder!!.txtBipPrice!!.text =
                    "₹ " + mData.get(position).bestInPrice!!.offerFinalAmount


                if (mData.get(position).bestInDelivery!!.offerDeliveryDate!!.equals("")) {
                    if (mData.get(position).bestInDelivery!!.deliveryType!!.equals("0")) {
                        holder!!.txtBidDDate!!.text = context.getString(R.string.pickup_date)
                        holder!!.txtBidDTime!!.text = context.getString(R.string.pickup_time)
                    }
                    holder!!.txtBidDelType!!.text =
                        mData.get(position).bestInDelivery!!.deliveryTypeName


                    holder!!.txtBipDelTime!!.text =
                        mData.get(position).bestInDelivery!!.deliveryTimeStart!! + " to " + mData.get(
                            position
                        ).bestInDelivery!!.deliveryTimeEnd!!
//                    holder!!.txtBidDelDate!!.text =
//                        mData.get(position).bestInDelivery!!.toDateFromDate!!

                    val tempDate0 = mData.get(position).bestInDelivery!!.toDateFromDate!!?.split("to")?.get(0)
                    val tempDate1 = mData.get(position).bestInDelivery!!.toDateFromDate!!?.split("to")?.get(1)
                    holder!!.txtBidDelDate!!.text = (tempDate0.toString().trim() + " to\n" + tempDate1.toString().trim())


//                    holder!!.txtBipDelDate!!.text = mData.get(position).bestInDelivery!!.deliveryDate!!

                    //Commented By Rahul Bambhaniya
//                    holder!!.txtBidDelDate!!.text =
//                            AppConstant.convertDelDate2(mData.get(position).bestInDelivery!!.deliveryDate!!)
//                    holder!!.txtBidDelTime!!.text = AppConstant.convertTime(
//                            mData.get(position).bestInDelivery!!.deliveryTimeStart!!,
//                            mData.get(position).bestInDelivery!!.deliveryTimeEnd!!
//                    )

                } else {
                    if (mData.get(position).bestInDelivery!!.offerDeliveryPreference!!.equals("0")) {
                        holder!!.txtBidDDate!!.text = context.getString(R.string.pickup_date)
                        holder!!.txtBidDTime!!.text = context.getString(R.string.pickup_time)
                    }
                    holder!!.txtBidDelType!!.text =
                        mData.get(position).bestInDelivery!!.offerDeliveryPreferenceName



                    holder!!.txtBidDelTime!!.text =
                        mData.get(position).bestInDelivery!!.offerDeliveryTimeStart!! + " to " + mData.get(
                            position
                        ).bestInDelivery!!.offerDeliveryTimeEnd!!
//                    holder!!.txtBidDelDate!!.text =
//                        mData.get(position).bestInDelivery!!.toDateFromDate!!

                    val tempDate0 = mData.get(position).bestInDelivery!!.toDateFromDate!!?.split("to")?.get(0)
                    val tempDate1 = mData.get(position).bestInDelivery!!.toDateFromDate!!?.split("to")?.get(1)
                    holder!!.txtBidDelDate!!.text = (tempDate0.toString().trim() + " to\n" + tempDate1.toString().trim())

//                    holder!!.txtBidDelDate!!.text = mData.get(position).bestInDelivery!!.offerDeliveryDate!!

                    //Commented By Rahul Bambhaniya
//                    holder!!.txtBidDelTime!!.text = AppConstant.convertTime(
//                            mData.get(position).bestInDelivery!!.offerDeliveryTimeStart!!,
//                            mData.get(position).bestInDelivery!!.offerDeliveryTimeEnd!!
//                    )
//                    holder!!.txtBidDelDate!!.text =
//                            AppConstant.convertDelDate2(mData.get(position).bestInDelivery!!.offerDeliveryDate!!)
                }
                holder!!.txtBidPerc!!.text =
                    mData.get(position).bestInDelivery!!.offerDiscountPercentage + "% OFF"
                holder!!.txtBidPrice!!.text =
                    "₹ " + mData.get(position).bestInDelivery!!.offerFinalAmount

                holder!!.txtBidViewOffer?.setOnClickListener {
                    context.startActivity(
                        Intent(context, ViewOfferActivity::class.java)
                            .putExtra("orderid", mData.get(position).bestInDelivery!!.orderId)
                            .putExtra("storeid", mData.get(position).bestInDelivery!!.storeId)
                            .putExtra("isAccepted", "0")
                    )
                    //finish()
                }
                holder!!.txtBipViewOffer?.setOnClickListener {
                    context.startActivity(
                        Intent(context, ViewOfferActivity::class.java)
                            .putExtra("orderid", mData.get(position).bestInPrice!!.orderId)
                            .putExtra("storeid", mData.get(position).bestInPrice!!.storeId)
                            .putExtra("isAccepted", "0")
                    )
                    //finish()
                }
                holder!!.txtBipReorder?.setOnClickListener {
                    if (mData.get(position).bestInPrice!!.orderType!!.equals("0")) {
                        context.startActivity(
                            Intent(context, FinalOrderActivity::class.java)
                                .putExtra("reorder", "1")
                                .putExtra("offerId", mData.get(position).bestInPrice!!.offerId!!)
                                .putExtra("orderid", mData.get(position).bestInDelivery!!.orderId!!)
                        )
                    } else {
                        context.startActivity(
                            Intent(context, CustomFinalOrderActivity::class.java)
                                .putExtra("reorder", "1")
                                .putExtra("offerId", mData.get(position).bestInPrice!!.offerId!!)
                                .putExtra("orderid", mData.get(position).bestInDelivery!!.orderId!!)
                        )
                    }
                    //finish()
                }
                holder!!.txtBidReorder?.setOnClickListener {
                    if (mData.get(position).bestInDelivery!!.orderType!!.equals("0")) {
                        context.startActivity(
                            Intent(context, FinalOrderActivity::class.java)
                                .putExtra("reorder", "1")
                                .putExtra("offerId", mData.get(position).bestInDelivery!!.offerId!!)
                                .putExtra("orderid", mData.get(position).bestInDelivery!!.orderId!!)

                        )
                    } else {
                        context.startActivity(
                            Intent(context, CustomFinalOrderActivity::class.java)
                                .putExtra("reorder", "1")
                                .putExtra("offerId", mData.get(position).bestInDelivery!!.offerId!!)
                                .putExtra("orderid", mData.get(position).bestInDelivery!!.orderId!!)
                        )
                    }
                    //finish()
                }
                holder!!.txtBipAccept?.setOnClickListener {
                    offeracc.acc(
                        mData.get(position).bestInPrice!!.offerId!!,
                        mData.get(position).bestInPrice!!.orderId!!,
                        mData.get(position).bestInPrice!!.storeId!!, "0"
                    )
                }
                holder!!.txtBidAccept?.setOnClickListener {
                    offeracc.acc(
                        mData.get(position).bestInDelivery!!.offerId!!,
                        mData.get(position).bestInDelivery!!.orderId!!,
                        mData.get(position).bestInDelivery!!.storeId!!, "1"
                    )
                }

                if (mData.get(position).orderRequest!!.equals("Timed out", true)) {
                    if (mData.get(position).orderStatus!!.equals("Offer Made", true)) {
                        ForDeliveryTimeOut(holder!!)

                    }
                }
            }
        }
        return convertView
    }

    private fun ForDeliveryTimeOut(holder: ViewHolder) {
        holder.constrain_delivry_time_out_OM!!.visibility = View.VISIBLE
        holder.txtBipAccept!!.isClickable = false
        holder.txtBipAcceptText!!.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.colorBlackWithAlpha
            )
        )
        holder.txtBipAcceptText!!.setTextViewDrawableColor(R.color.colorBlackWithAlpha)

        holder.constrain_delivry_time_out_OM_BED!!.visibility = View.VISIBLE
        holder.txtBidAccept!!.isClickable = false
        holder.txtBidAcceptText!!.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.colorBlackWithAlpha
            )
        )
        holder.txtBidAcceptText!!.setTextViewDrawableColor(R.color.colorBlackWithAlpha)


    }

    private fun AppCompatTextView.setTextViewDrawableColor(color: Int) {
        for (drawable in this.compoundDrawablesRelative) {
            drawable?.mutate()
            drawable?.colorFilter = PorterDuffColorFilter(
                color, PorterDuff.Mode.SRC_IN
            )
        }
    }


    override fun isItemViewTypePinned(viewType: Int): Boolean {
        return viewType == PartialCoverageActivity.SECTION
    }

    internal inner class ViewHolder {

        var txt_title: AppCompatTextView? = null
        var ll_code1: LinearLayout? = null

        //item
        private val linear: LinearLayout? = null


        var txtBipDelType: AppCompatTextView? = null
        var txtBipViewOffer: AppCompatTextView? = null
        var txtBipDelDate: AppCompatTextView? = null
        var txtBipDDate: AppCompatTextView? = null
        var txtBipDTime: AppCompatTextView? = null
        var txtBipDelTime: AppCompatTextView? = null
        var txtBipDelPerc: AppCompatTextView? = null
        var txtBipPrice: AppCompatTextView? = null
        var constrain_delivry_time_out_OM: ConstraintLayout? = null


        internal var txtBipReorder: RelativeLayout? = null
        internal var txtBipAccept: RelativeLayout? = null
        internal var txtBipAcceptText: AppCompatTextView? = null

        var txtBidDelType: AppCompatTextView? = null
        var txtBidViewOffer: AppCompatTextView? = null
        var txtBidDelDate: AppCompatTextView? = null
        var txtBidDelTime: AppCompatTextView? = null
        var txtBidDDate: AppCompatTextView? = null
        var txtBidDTime: AppCompatTextView? = null
        var txtBidPerc: AppCompatTextView? = null
        var txtBidPrice: AppCompatTextView? = null
        var constrain_delivry_time_out_OM_BED: ConstraintLayout? = null


        internal var txtBidReorder: RelativeLayout? = null
        internal var txtBidAccept: RelativeLayout? = null
        internal var txtBidAcceptText: AppCompatTextView? = null
    }

    companion object {
        private val TYPE_ITEM = 0
        private val TYPE_SEPARATOR = 1
        private val TYPE_MAX_COUNT = TYPE_SEPARATOR + 1
    }

    interface offerAcc {
        fun acc(offerid: String, orderId: String, storeId: String, bit: String)
    }
}