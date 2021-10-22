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

class ByCityAdapter(var context: Context,
                    var cityList: List<String>, val filterListener: FiltersInterFace
) : RecyclerView.Adapter<ByCityAdapter.MyHolder>() {

    var city_array: ArrayList<String> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ByCityAdapter.MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_by_type, parent, false)
        return ByCityAdapter.MyHolder(v)
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    override fun onBindViewHolder(holder: ByCityAdapter.MyHolder, pos: Int) {

        holder.txt.text = cityList[pos]

        holder.txt.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                city_array.add(cityList[pos].toString())
                val allIds = TextUtils.join(",", city_array)
                Log.e("HElloType","isChecked   "+allIds)

                filterListener.onClickCity(allIds)

            } else {

                city_array.remove(cityList[pos].toString())
                val allIds = TextUtils.join(",", city_array)
                filterListener.onClickCity(allIds)
            }

        }

    }

    class MyHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val txt = itemView.findViewById(R.id.checkBox1) as CheckBox
    }
}