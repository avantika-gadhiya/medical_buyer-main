package bluesharklabs.com.medical.adapters

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.model.AddOrder
import java.util.ArrayList

class YourProductsAdapter(
    var context: Context,
    var arrProduct: ArrayList<AddOrder.customProducts>,
    var deletPos: DeletPos
) : RecyclerView.Adapter<YourProductsAdapter.MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): YourProductsAdapter.MyHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.item_your_produts, parent, false)
        return MyHolder(v)
    }

    var count: Int = 0
    override fun getItemCount(): Int {
        return arrProduct.size
    }

    override fun onBindViewHolder(holder: YourProductsAdapter.MyHolder, pos: Int) {
        holder.txtnum.setText(arrProduct[pos].quantity.toString())
        holder.txtProductName.setText(arrProduct[pos].product_name.toString())
        if (!arrProduct[pos].product_brand.equals("")) {
            holder.txtBrandName.setText(arrProduct[pos].product_brand.toString())
        }else{
            holder.txtBrandName.visibility = View.GONE
        }
        if (arrProduct[pos].product_content.equals("") && arrProduct[pos].product_unit_name.equals("")) {


            holder.txtQuantity.setText(arrProduct[pos].product_pack_name.toString())

        }else if (arrProduct[pos].product_content.equals("") ){

            holder.txtQuantity.setText(arrProduct[pos].product_unit_name.toString()+" "+arrProduct[pos].product_pack_name.toString())

        }else if (arrProduct[pos].product_unit_name.equals("") ){

            holder.txtQuantity.setText(arrProduct[pos].product_content.toString() + " " + arrProduct[pos].product_pack_name.toString())

        }else{

            holder.txtQuantity.setText(arrProduct[pos].product_content.toString() + " " + arrProduct[pos].product_unit_name.toString() + " " + arrProduct[pos].product_pack_name.toString())
        }

        holder.imageDel.setOnClickListener {
            deletPos.del(pos)
        }
        holder.txtMinus.setOnClickListener {

            count = arrProduct[pos].quantity!!.toInt()

            if (count > 0) {
                count = count - 1
                arrProduct[pos].quantity = count.toString()

                holder.txtnum.setText(count.toString())
            }
        }
        holder.txtAdd.setOnClickListener {

            count = arrProduct[pos].quantity!!.toInt()

            count = count + 1
            arrProduct[pos].quantity = count.toString()

            holder.txtnum.setText(count.toString())
        }
    }

    class MyHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        val imageDel = itemView.findViewById(R.id.img_del) as AppCompatImageView
        val txtMinus = itemView.findViewById(R.id.txt_minus) as AppCompatTextView
        val txtAdd = itemView.findViewById(R.id.txt_add) as AppCompatTextView
        val txtnum = itemView.findViewById(R.id.txt_no_) as AppCompatTextView
        val txtProductName = itemView.findViewById(R.id.txt_product_name) as AppCompatTextView
        val txtBrandName = itemView.findViewById(R.id.txt_brand_name) as AppCompatTextView
        val txtQuantity = itemView.findViewById(R.id.txt_quantity) as AppCompatTextView
    }

    interface DeletPos {
        fun del(pos: Int)
    }
}