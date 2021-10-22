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

class ByStoreNameAdapter(var context: Context,
                         var storeNameList: List<FilterResponse.StoreName>,val filterListener: FiltersInterFace
) : RecyclerView.Adapter<ByStatusAdapter.MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ByStatusAdapter.MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_by_type, parent, false)
        return ByStatusAdapter.MyHolder(v)
    }
    var storeName_array: ArrayList<String> = arrayListOf()

    override fun getItemCount(): Int {
        return storeNameList.size
    }

    override fun onBindViewHolder(holder: ByStatusAdapter.MyHolder, pos: Int) {

        Log.d("TAG", "ADD===A-->" + storeNameList.get(pos))
        holder.txt.text = storeNameList.get(pos).value
        holder.txt.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                storeName_array.add(storeNameList[pos].key.toString())
                val allIds = TextUtils.join(",", storeName_array)
                filterListener.onClickStoreName(allIds)

            } else {

                storeName_array.remove(storeNameList[pos].key.toString())
                val allIds = TextUtils.join(",", storeName_array)
                filterListener.onClickStoreName(allIds)
            }

        }

    }

    class MyHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val txt = itemView.findViewById(R.id.checkBox1) as CheckBox
    }
}