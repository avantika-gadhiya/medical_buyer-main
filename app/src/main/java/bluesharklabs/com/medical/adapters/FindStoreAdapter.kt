package bluesharklabs.com.medical.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.response.StoreListResponse
import com.bumptech.glide.Glide


class FindStoreAdapter(
    var context: Context,
    var customer: String,
    var storeListArray: List<StoreListResponse.Datum>, var findStore: FindStore
) : RecyclerView.Adapter<FindStoreAdapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): FindStoreAdapter.MyHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.item_recycl_store, parent, false)
        return FindStoreAdapter.MyHolder(v)
    }

    override fun getItemCount(): Int {
        return storeListArray.size
    }

    fun updateList(list: List<StoreListResponse.Datum>) {
        storeListArray = list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: FindStoreAdapter.MyHolder, pos: Int) {

        val locaTion =
            storeListArray.get(pos).shopNo + ", " +
                    storeListArray.get(pos).building + ", " +
                    storeListArray.get(pos).street + ", " +
                    storeListArray.get(pos).area + ", " +
                    storeListArray.get(pos).landmark + ", " +
                    storeListArray.get(pos).city + ", " +
                    storeListArray.get(pos).zipcode

        Glide.with(context)  //2
            .load(storeListArray.get(pos).storePhoto) //3
            .centerCrop() //4
            .placeholder(R.drawable.ic_launcher_background) //5
            .error(R.drawable.ic_launcher_background) //6
            //.fallback(R.drawable.ic_launcher_background) //7
            .into(holder.imageView) //8

        holder.txtStoreName.text = storeListArray.get(pos).name
        holder.txtStoreAdd.text = locaTion

        holder.itemView.setOnClickListener {
            findStore.findStore(storeListArray.get(pos).phone.toString(), customer)
        }
    }

    class MyHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        val imageView = itemView.findViewById(R.id.img_store) as AppCompatImageView
        val txtStoreName = itemView.findViewById(R.id.txt_stor_nm) as AppCompatTextView
        val txtStoreAdd = itemView.findViewById(R.id.txt_store_add) as AppCompatTextView

    }

    interface FindStore {
        fun findStore(phone: String, customer: String)
    }
}