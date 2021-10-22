package bluesharklabs.com.medical.adapters

import android.content.Context
import android.content.Intent
import androidx.appcompat.widget.AppCompatTextView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.activities.OrderPreferenceActivity
import java.util.ArrayList

class AddressAdapter(
    var context: Context,
    var addList: ArrayList<String>
): RecyclerView.Adapter<AddressAdapter.MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): AddressAdapter.MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_add, parent, false)
        return AddressAdapter.MyHolder(v)
    }

    override fun getItemCount(): Int {
        return addList.size
    }

    override fun onBindViewHolder(holder: AddressAdapter.MyHolder, pos: Int) {

        Log.d("TAG","ADD===A-->"+addList.get(pos))
        holder.txt.text = addList.get(pos)
        holder.txt.setOnClickListener {
            context.startActivity(Intent(context,OrderPreferenceActivity :: class.java)
                .putExtra("address",addList.get(pos)))
        }

    }

    class MyHolder(itemview: View) : RecyclerView.ViewHolder(itemview)  {
        val txt = itemView.findViewById(R.id.textView94) as AppCompatTextView
    }
}