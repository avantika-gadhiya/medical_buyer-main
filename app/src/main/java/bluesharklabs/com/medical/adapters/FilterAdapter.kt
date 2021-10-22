package bluesharklabs.com.medical.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.interfaces.FiltersInterFace
import bluesharklabs.com.medical.interfaces.PartialOfferFiltersInterFace

class FilterAdapter(
        var context: Context,
        var stringList: List<String>,val filterListener: PartialOfferFiltersInterFace
) : RecyclerView.Adapter<FilterAdapter.MyHolder>() {

    var selected: Boolean = false
    var stList: ArrayList<String>? = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, int: Int): FilterAdapter.MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_filter, parent, false)
        return FilterAdapter.MyHolder(v)
    }

    override fun getItemCount(): Int {
        return stringList.size
    }

    override fun onBindViewHolder(holder: FilterAdapter.MyHolder, pos: Int) {
        val drawable = holder.imageView.background as GradientDrawable
        val drawable2 = holder.imageView.background as GradientDrawable

        drawable2.setColor(Color.parseColor("#" + stringList.get(pos))) // set solid color

        holder.itemView.setOnClickListener(View.OnClickListener {

            val color_id = pos.plus(1)

            if (stList!!.contains(color_id.toString())) {
                stList?.remove(color_id.toString())
                drawable.setStroke(3, Color.TRANSPARENT) // set stroke width and stroke color
            } else {
                stList?.add(color_id.toString())
                drawable.setStroke(3, Color.BLACK)
            }

            filterListener.onClickColor(stList!!)
        })
    }

    class MyHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        val imageView = itemView.findViewById(R.id.imageView) as AppCompatImageView
    }


}