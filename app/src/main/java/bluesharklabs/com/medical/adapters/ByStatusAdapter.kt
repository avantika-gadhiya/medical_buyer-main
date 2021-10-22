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

class ByStatusAdapter(
        var context: Context,
        var statusList: List<FilterResponse.OrderStatus>, val filterListener: FiltersInterFace
) : RecyclerView.Adapter<ByStatusAdapter.MyHolder>() {
    var status_array: ArrayList<String> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ByStatusAdapter.MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_by_type, parent, false)
        return ByStatusAdapter.MyHolder(v)
    }

    override fun getItemCount(): Int {
        return statusList.size
    }

    override fun onBindViewHolder(holder: ByStatusAdapter.MyHolder, pos: Int) {

        Log.d("TAG", "ADD===A-->" + statusList.get(pos))
        holder.txt.text = statusList[pos].value
        holder.txt.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                status_array.add(statusList[pos].key.toString())
                val allIds = TextUtils.join(",", status_array)
                filterListener.onClickStatus(allIds)

            } else {

                status_array.remove(statusList[pos].key.toString())
                val allIds = TextUtils.join(",", status_array)
                filterListener.onClickStatus(allIds)
            }

        }

    }

    class MyHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val txt = itemView.findViewById(R.id.checkBox1) as CheckBox
    }
}