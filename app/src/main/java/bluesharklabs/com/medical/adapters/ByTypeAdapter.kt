package bluesharklabs.com.medical.adapters

import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.interfaces.FiltersInterFace
import bluesharklabs.com.medical.response.FilterResponse

class ByTypeAdapter(
        var context: Context,
        var typeList: List<FilterResponse.OrderType>, val filterListener: FiltersInterFace
) : RecyclerView.Adapter<ByTypeAdapter.MyHolder>() {

    var type_array: ArrayList<String> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ByTypeAdapter.MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_by_type, parent, false)
        return ByTypeAdapter.MyHolder(v)
    }

    override fun getItemCount(): Int {
        return typeList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ByTypeAdapter.MyHolder, pos: Int) {

        Log.e("sdsdsd", "ADD===A-->" + typeList.get(pos))
        holder.check_type.text = typeList[pos].value

        holder.check_type.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                type_array.add(typeList[pos].key.toString())
                val allIds = TextUtils.join(",", type_array)
                Log.e("HElloType","isChecked   "+allIds)

                filterListener.onClickType(allIds)

            } else {

                type_array.remove(typeList[pos].key.toString())
                val allIds = TextUtils.join(",", type_array)
                filterListener.onClickType(allIds)
            }

        }


    }

    class MyHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val check_type = itemView.findViewById(R.id.checkBox1) as CheckBox
    }

}