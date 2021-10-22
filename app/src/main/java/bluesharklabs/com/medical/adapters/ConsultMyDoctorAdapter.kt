package bluesharklabs.com.medical.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.interfaces.ConsultDoctorInterFace
import bluesharklabs.com.medical.response.ConsultDoctorResponse
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.Target
import de.hdodenhof.circleimageview.CircleImageView

class ConsultMyDoctorAdapter(
    var context: Context,
    var doctorData: ArrayList<ConsultDoctorResponse.Datum>,
    var listener: ConsultDoctorInterFace
) : RecyclerView.Adapter<ConsultMyDoctorAdapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ConsultMyDoctorAdapter.MyHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_consult_my_doctor, parent, false)
        return MyHolder(v)
    }

    override fun getItemCount(): Int {
        return doctorData.size
    }

    override fun onBindViewHolder(holder: MyHolder, p1: Int) {

        val mNumber = doctorData[p1].doctorPhone
        val mNAme = doctorData[p1].doctorName
        val mImage = doctorData[p1].profilePic


        holder.txt_dname.text = mNAme
        holder.txt_dnumber.text = mNumber
        holder.image_whatsapp.setOnClickListener { listener.onClickWA(1, mNumber!!,mNAme!!) }
        holder.txt_dcall.setOnClickListener { listener.onClickWA(2, mNumber!!,mNAme!!) }



        try {
            Glide.with(context)
                .load(mImage)
                .listener(object : com.bumptech.glide.request.RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                }).placeholder(R.drawable.ic_launcher_background)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image_receiver)
        } catch (e: Exception) {
            Log.e("HElloPicError", "E   " + e)
        }
    }

    class MyHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val image_whatsapp = itemView.findViewById(R.id.image_whatsapp) as CircleImageView
        val image_receiver = itemView.findViewById(R.id.image_receiver) as CircleImageView
        val txt_dcall = itemView.findViewById(R.id.txt_dcall) as TextView
        val txt_dname = itemView.findViewById(R.id.txt_dname) as TextView
        val txt_dnumber = itemView.findViewById(R.id.txt_dnumber) as TextView
    }
}