package bluesharklabs.com.medical.adapters

import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.interfaces.FiltersInterFace
import bluesharklabs.com.medical.response.FilterResponse

class ByOfferAdapter(
        var context: Context,
        var offerList: List<FilterResponse.OrderOffer>, val filterListener: FiltersInterFace
) : RecyclerView.Adapter<ByOfferAdapter.MyHolder>() {

    var offer_array: ArrayList<String> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ByOfferAdapter.MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_by_type, parent, false)
        return ByOfferAdapter.MyHolder(v)
    }

    override fun getItemCount(): Int {
        return offerList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ByOfferAdapter.MyHolder, pos: Int) {

        holder.check_type.text = offerList[pos].value

        holder.check_type.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                offer_array.add(offerList[pos].key.toString())
                val allIds = TextUtils.join(",", offer_array)
                filterListener.onClickOffer(allIds)

            } else {

                offer_array.remove(offerList[pos].key.toString())
                val allIds = TextUtils.join(",", offer_array)
                filterListener.onClickOffer(allIds)
            }
        }
    }

    class MyHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val check_type = itemView.findViewById(R.id.checkBox1) as CheckBox
    }
}