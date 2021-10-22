package bluesharklabs.com.medical.adapters

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.interfaces.FiltersInterFace
import bluesharklabs.com.medical.response.FilterResponse

class ByAreaAdapter(var context: Context,
                    var areaList: List<String>,val filterListener: FiltersInterFace
) : RecyclerView.Adapter<ByAreaAdapter.MyHolder>() {

    var area_array: ArrayList<String> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ByAreaAdapter.MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_by_type, parent, false)
        return ByAreaAdapter.MyHolder(v)
    }

    override fun getItemCount(): Int {
        return areaList.size
    }

    override fun onBindViewHolder(holder: ByAreaAdapter.MyHolder, pos: Int) {

        holder.txt.text = areaList[pos]

        holder.txt.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                area_array.add(areaList[pos].toString())
                val allIds = TextUtils.join(",", area_array)
                Log.e("HElloType","isChecked   "+allIds)

                filterListener.onClickArea(allIds)

            } else {

                area_array.remove(areaList[pos].toString())
                val allIds = TextUtils.join(",", area_array)
                filterListener.onClickArea(allIds)
            }

        }

    }

    class MyHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val txt = itemView.findViewById(R.id.checkBox1) as CheckBox
    }
}