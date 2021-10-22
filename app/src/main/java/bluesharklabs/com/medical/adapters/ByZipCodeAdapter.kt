package bluesharklabs.com.medical.adapters

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.interfaces.FiltersInterFace
import bluesharklabs.com.medical.response.FilterResponse

class ByZipCodeAdapter(
        var context: Context,
        var zipCodeList: List<String>,
        var filterListener: FiltersInterFace
) : RecyclerView.Adapter<ByZipCodeAdapter.MyHolder>() {

    var zipcode_array: ArrayList<String> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ByZipCodeAdapter.MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_by_type, parent, false)
        return ByZipCodeAdapter.MyHolder(v)
    }

    override fun getItemCount(): Int {
        return zipCodeList.size
    }

    override fun onBindViewHolder(holder: ByZipCodeAdapter.MyHolder, pos: Int) {

        holder.txt.text = zipCodeList[pos]

        holder.txt.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                zipcode_array.add(zipCodeList[pos].toString())
                val allIds = TextUtils.join(",", zipcode_array)
                filterListener.onClickZipcode(allIds)

            } else {

                zipcode_array.remove(zipCodeList[pos].toString())
                val allIds = TextUtils.join(",", zipcode_array)
                filterListener.onClickZipcode(allIds)
            }

        }

    }

    class MyHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val txt = itemView.findViewById(R.id.checkBox1) as CheckBox
    }
}