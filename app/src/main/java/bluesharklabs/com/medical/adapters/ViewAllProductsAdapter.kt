package bluesharklabs.com.medical.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.model.AddOrder
import java.util.ArrayList

class ViewAllProductsAdapter(
    var context: Context,
    var arrProduct: ArrayList<AddOrder.customProducts>,
    var viewAll: String
) : RecyclerView.Adapter<ViewAllProductsAdapter.MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewAllProductsAdapter.MyHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_all_products, parent, false)
        return ViewAllProductsAdapter.MyHolder(v)
    }

    override fun getItemCount(): Int {

        if (viewAll.equals("0") && arrProduct.size > 3)
            return 3
        else
            return arrProduct.size
    }

    override fun onBindViewHolder(holder: ViewAllProductsAdapter.MyHolder, pos: Int) {

        val clr = ("#" + arrProduct[pos].color!!)
        val color = Color.parseColor(clr)

        val drawableBg = holder.imgClrCode.getBackground().mutate() as GradientDrawable
        drawableBg.setColor(color)


        if (!arrProduct[pos].product_brand.equals("")) {
            holder.txtBrandName.setText(arrProduct[pos].product_brand.toString())
        }else{
            holder.txtBrandName.visibility = View.GONE
        }
        if (arrProduct[pos].product_content.equals("") && arrProduct[pos].product_unit_name.equals("")) {
            holder.txtQuantity.setText(arrProduct[pos].product_pack_name.toString())
        }else if (arrProduct[pos].product_content.equals("") ){
            holder.txtQuantity.setText(arrProduct[pos].product_unit_name.toString() + " " +
                    arrProduct[pos].product_pack_name.toString())
        }else if (arrProduct[pos].product_unit_name.equals("") ){
            holder.txtQuantity.setText(arrProduct[pos].product_content.toString() + " " +
                    arrProduct[pos].product_pack_name.toString())
        }else{
            holder.txtQuantity.setText(arrProduct[pos].product_content.toString() + " " +
                    arrProduct[pos].product_unit_name.toString() + " " +
                    arrProduct[pos].product_pack_name.toString())
        }

        holder.edtQntt.setText(arrProduct[pos].quantity.toString())
        holder.txtProductName.setText(arrProduct[pos].product_name.toString())

    }

    class MyHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val edtQntt = itemView.findViewById(R.id.edt_qntt) as AppCompatTextView
        val imgClrCode = itemView.findViewById(R.id.img_clr_code) as AppCompatTextView
        val txtQuantity = itemView.findViewById(R.id.txt_quantity) as AppCompatTextView
        val txtBrandName = itemView.findViewById(R.id.txt_brand_name) as AppCompatTextView
        val txtProductName = itemView.findViewById(R.id.txt_product_name) as AppCompatTextView
    }
}