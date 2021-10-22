package bluesharklabs.com.medical.adapters

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.appcompat.widget.AppCompatTextView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.activities.FullQRImageActivity
import bluesharklabs.com.medical.response.GetStorePaymentResponse
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_my_profile.*

class MakePaymentAdapter(
    var context: Context,
    var paymentList: List<GetStorePaymentResponse.Payment>,
    var paymntmode: Paymentmode,var qrDownload:QrDownload
) : RecyclerView.Adapter<MakePaymentAdapter.MyHolder>() {
    var selected_position = -1

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MakePaymentAdapter.MyHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_make_payment, parent, false)
        return MakePaymentAdapter.MyHolder(v)
    }

    override fun getItemCount(): Int {
        return paymentList.size
    }

    override fun onBindViewHolder(holder: MakePaymentAdapter.MyHolder, pos: Int) {


     //   holder.txt.setChecked(pos==selected_position);



        if (pos == selected_position)
        {
            holder.txt.setTextColor(ContextCompat.getColor(context,R.color.black_txt))
            holder.txt.setChecked(true)
        }
        else
        {
            holder.txt.setTextColor(ContextCompat.getColor(context,R.color.grey_txt))
            holder.txt.setChecked(false)
        }

        if (!paymentList.get(pos).upi.equals("")){
            holder.txtUpi.visibility = View.VISIBLE
//            holder.img_qr_code.visibility = View.VISIBLE

        }else{
            holder.txtUpi.visibility = View.GONE
//            holder.img_qr_code.visibility = View.GONE
        }

        if (!paymentList.get(pos).paymentName.equals("On Credit")){
            holder.img_qr_code.visibility = View.VISIBLE

        }else{
            holder.img_qr_code.visibility = View.GONE
        }



        Log.d("TAG", "ADD===A-->" + paymentList.get(pos))
        holder.txt.text = paymentList.get(pos).paymentName
        holder.txtUpi.text = paymentList.get(pos).upi
        Glide.with(context)  //2
                .load(paymentList[pos].qrImg) //3
                .centerCrop() //4
                .placeholder(R.drawable.ic_launcher_background) //5
                .error(R.drawable.ic_launcher_background) //6
                .fallback(R.drawable.ic_launcher_background) //7
                .placeholder(R.drawable.ic_launcher_background) //5
                .into(holder.img_qr_code) //8

        holder.txt.setOnClickListener {

            paymntmode.getId(paymentList.get(pos).paymentName!!, paymentList.get(pos).paymentMethodId!!,holder.txt)

            selected_position = pos
            notifyDataSetChanged()
        }

        holder.img_qr_code.setOnClickListener {

            context.startActivity(Intent(context,FullQRImageActivity::class.java).putExtra("IMAGE",paymentList.get(pos).qrImg))
          //  qrDownload.getQR(paymentList.get(pos).qrImg.toString())

        }

        if(paymentList.get(pos).paymentName.equals("COD")){
            holder.main_ll.visibility = View.GONE
        }

    }

    class MyHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val main_ll = itemView.findViewById(R.id.main_ll) as ConstraintLayout
        val txt = itemView.findViewById(R.id.checkBox1) as CheckBox
        val txtUpi = itemView.findViewById(R.id.text_upi) as AppCompatTextView
        val img_qr_code = itemView.findViewById(R.id.img_qr_code) as AppCompatImageView
    }
    interface Paymentmode {
        fun getId(name:String,id:String,checkBox1:CheckBox)
    }

    interface QrDownload {
        fun getQR(qr_img:String)
    }
}