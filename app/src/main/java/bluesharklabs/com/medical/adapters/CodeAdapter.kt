package bluesharklabs.com.medical.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
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

class CodeAdapter(
    var context: Context,
    var array: ArrayList<AddOrder.Products>
) : RecyclerView.Adapter<CodeAdapter.MyHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CodeAdapter.MyHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.item_recycl_code, parent, false)
        return CodeAdapter.MyHolder(v)
    }

    override fun getItemCount(): Int {
        return array.size
    }

    override fun onBindViewHolder(holder: CodeAdapter.MyHolder, p1: Int) {

        //  val color = ContextCompat.getColor(context, color)

        val clr = ("#" + array.get(p1).color!!)
        val color = Color.parseColor(clr)

        val drawableBg = holder.imgClrCode.getBackground().mutate() as GradientDrawable
        drawableBg.setColor(color)

        /*holder.imgClrCode.setColorFilter(
            ContextCompat.getColor(context,),
            PorterDuff.Mode.SRC_IN
        )*/

        holder.txtCode.setText(array.get(p1).product_id)

        if (!array.get(p1).quantity_type.equals("0")){
            holder.txtQntt.setText("QTY: "+array.get(p1).quantity)
        }else{
            holder.txtQntt.setText("QTY: Full")
        }

        // holder.imgClrCode.setBackgroundColor(Color.parseColor(("#"+array.get(p1).color!!)))

        //   holder.imgClrCode.getBackground().setColorFilter(ContextCompat.getColor(context,("#"+array.get(p1).color!!).toInt()), PorterDuff.Mode.SRC_ATOP);
        //
    }

    class MyHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val txtCode = itemView.findViewById(R.id.txt_code) as AppCompatTextView
        val txtQntt = itemView.findViewById(R.id.txt_qntt) as AppCompatTextView
        val imgClrCode = itemView.findViewById(R.id.img_clr_code) as AppCompatTextView
    }
}