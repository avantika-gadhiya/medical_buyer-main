package bluesharklabs.com.medical.adapters

import android.content.Context
import android.content.Intent
import android.icu.util.ValueIterator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.activities.FinalOrderActivity
import bluesharklabs.com.medical.activities.MyPrescriptionsActivity
import bluesharklabs.com.medical.response.BuyerDetailResponse
import bluesharklabs.com.medical.response.PrescriptionResponse
import bluesharklabs.com.medical.utils.AppConstant
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.item_my_presc.view.*


class PrescriptionAdapter(var context: MyPrescriptionsActivity, var foodsList: ArrayList<PrescriptionResponse.Datum>?) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {


        val convertView = LayoutInflater.from(parent?.context).inflate(R.layout.item_my_presc, parent, false)



       val imgFood =convertView?.findViewById<ImageView>(R.id.imgFood)
       val prescription_id =convertView?.findViewById<TextView>(R.id.tvName)
       val date =convertView?.findViewById<TextView>(R.id.textView100)
       val orderNow =convertView?.findViewById<TextView>(R.id.txt_order_now)



       prescription_id?.text= foodsList?.get(position)?.orderNo
       date?.text= (foodsList?.get(position)?.createdDate.toString())
        Glide.with(context)  //2
                .load(foodsList?.get(position)?.prescriptionImage) //3
                .into(imgFood!!) //8


        orderNow?.setOnClickListener {
            context.startActivity(
                    Intent(context, FinalOrderActivity::class.java)
                            .putExtra("reorder", "")
                            .putExtra("orderid", foodsList?.get(position)?.orderId)
            )
        }
        return convertView
    }

    override fun getItem(position: Int): Any {
        return foodsList?.get(position)!!
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return foodsList!!.size
    }
}