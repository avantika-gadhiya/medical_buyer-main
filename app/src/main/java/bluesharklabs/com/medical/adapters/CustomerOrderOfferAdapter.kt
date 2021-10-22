package bluesharklabs.com.medical.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.response.OrderDetailResponse

class CustomerOrderOfferAdapter(
        var context: Context,
        var products: List<OrderDetailResponse.Product>?,
        var viewall: String
) : RecyclerView.Adapter<CustomerOrderOfferAdapter.MyHolder>() {

    override fun onCreateViewHolder(
            parent: ViewGroup,
            p1: Int
    ): CustomerOrderOfferAdapter.MyHolder {
        val v =
                LayoutInflater.from(parent.context).inflate(R.layout.item_customer_order_offer, parent, false)
        return CustomerOrderOfferAdapter.MyHolder(v)
    }

    override fun getItemCount(): Int {
        if (viewall.equals("0") && products!!.size > 3)
            return 3
        else
            return products!!.size
    }

    override fun onBindViewHolder(holder: MyHolder, pos: Int) {
        val clr = ("#" + products!!.get(pos).color!!)
        val color = Color.parseColor(clr)

        val drawableBg = holder.imageView.getBackground().mutate() as GradientDrawable
        drawableBg.setColor(color)

        val avail = products!!.get(pos).availableProduct
        Log.e("HellodsdLog","avail   "+products!!.get(pos).availableProduct!!+"   "+products!!.get(pos).productMrp)
        if (!avail.equals(null)) {

            if (products!!.get(pos).availableProduct!!.equals("1")) {
                holder.txtAvail.text = context.resources.getString(R.string.available)
                holder.txtAvail.setTextColor(context.resources.getColor(R.color.green_txt))

                holder.txtRs.visibility = View.VISIBLE
                holder.txtPrice.setText(products!!.get(pos).productMrp)
            } else {
                holder.txtAvail.text = context.resources.getString(R.string.not_available)
                holder.txtAvail.setTextColor(context.resources.getColor(R.color.red_txt))
                holder.txtRs.visibility = View.INVISIBLE
            }
        }

        if (!products!!.get(pos).productBrand.equals("")) {
            holder.txtBrandName.text = products!!.get(pos).productBrand
        } else {
            holder.txtBrandName.visibility = View.GONE
        }
        if (products!!.get(pos).productContent.equals("") && products!!.get(pos).productUnitName.equals("")) {
            holder.txtQntty.setText(products!!.get(pos).productPackName.toString())
        } else if (products!!.get(pos).productContent.equals("")) {
            holder.txtQntty.setText(products!!.get(pos).productUnitName.toString() + " " +
                    products!!.get(pos).productPackName.toString())
        } else if (products!!.get(pos).productUnitName.equals("")) {
            holder.txtQntty.setText(products!!.get(pos).productContent.toString() + " " +
                    products!!.get(pos).productPackName.toString())
        } else {
            holder.txtQntty.setText(products!!.get(pos).productContent.toString() + " " +
                    products!!.get(pos).productUnitName.toString() + " " +
                    products!!.get(pos).productPackName.toString())
        }

        val presc = products!!.get(pos).prescrStatus

        if (!presc.equals(null)) {
            if (products!!.get(pos).prescrStatus!!.equals("1")) {
                holder.txt_presc_only.visibility = View.VISIBLE
            } else
                holder.txt_presc_only.visibility = View.INVISIBLE
        }

        holder.txtProductName.text = products!!.get(pos).productName
        holder.txtQnttys.text = "QTY: "+products!!.get(pos).storeAvailableQuantity
    }

    class MyHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val imageView = itemView.findViewById(R.id.img_clr_code) as AppCompatTextView
        val txtQntty = itemView.findViewById(R.id.txt_qntty) as AppCompatTextView
        val txtQnttys = itemView.findViewById(R.id.txt_qntt) as AppCompatTextView
        val txtBrandName = itemView.findViewById(R.id.txt_brand_name) as AppCompatTextView
        val txtProductName = itemView.findViewById(R.id.txt_product_name) as AppCompatTextView
        val txtAvail = itemView.findViewById(R.id.txt_avail) as AppCompatTextView
        val txtPrice = itemView.findViewById(R.id.txt_price) as AppCompatTextView
        val txtRs = itemView.findViewById(R.id.txt_rs) as AppCompatTextView
        val txt_presc_only = itemView.findViewById(R.id.txt_presc_only) as AppCompatTextView
    }



}