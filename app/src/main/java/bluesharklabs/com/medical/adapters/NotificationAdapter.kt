package bluesharklabs.com.medical.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.response.NotificationResponse

class NotificationAdapter(
    var context: Context,var notiLiat :List<NotificationResponse.Datum>
) : RecyclerView.Adapter<NotificationAdapter.MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): NotificationAdapter.MyHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return NotificationAdapter.MyHolder(v)
    }

    override fun getItemCount(): Int {
        return notiLiat.size
    }

    override fun onBindViewHolder(holder: NotificationAdapter.MyHolder, pos: Int) {


        holder.txt_date.text = notiLiat[pos].createdDate
        holder.txt_title.text = notiLiat[pos].title
        holder.txt_desc.text = notiLiat[pos].text


    }

    class MyHolder(itemview: View) : RecyclerView.ViewHolder(itemview){
        val txt_date = itemView.findViewById(R.id.textView97) as TextView
        val txt_title = itemView.findViewById(R.id.txt_title) as TextView
        val txt_desc = itemView.findViewById(R.id.textView98) as TextView
    }
}