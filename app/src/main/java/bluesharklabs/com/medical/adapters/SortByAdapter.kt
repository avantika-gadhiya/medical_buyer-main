package bluesharklabs.com.medical.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R

class SortByAdapter(
    var context: Context, var language: Array<String>, var sortby: sortBy
) : RecyclerView.Adapter<SortByAdapter.MyHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): SortByAdapter.MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_sortby, parent, false)
        return SortByAdapter.MyHolder(v)
    }

    override fun getItemCount(): Int {
        return language.size
    }

    override fun onBindViewHolder(holder: SortByAdapter.MyHolder, i: Int) {

        holder.txtt.text = language.get(i)


        holder.itemView.setOnClickListener {
            sortby.sortby(i.plus(1))
        }
    }

    class MyHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val txtt = itemView.findViewById(R.id.txt) as TextView
    }

    interface sortBy {
        fun sortby(pos: Int)
    }
}