package bluesharklabs.com.medical.activities

import android.Manifest
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.GetInvoice
import bluesharklabs.com.medical.response.GetInvoiceResponse
import bluesharklabs.com.medical.utils.AppConstant
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_invoice.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.File

class InvoiceActivity : AppCompatActivity(), View.OnClickListener {

    var offerid = ""
    var img = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR//  set status text dark

        setContentView(R.layout.activity_invoice)

        img_back.setOnClickListener(this)
        btn_download_invoice.setOnClickListener(this)

        if (intent != null) {
            /*txt_id
            txt_date
            txt_id.text = "ID: " + orderNo
            txt_date.text =
                AppConstant.convertdatentimetoOrderTime(fullCoveragedata!!.createdDate!!)*/

            if (intent.getStringExtra("offerId") != null) {
                offerid = intent.getStringExtra("offerId")
                downloadInvoiceApi()
            } else {
                showDialog()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.btn_download_invoice -> {

                if (isStoragePermissionGranted()) {

                    val img_qr: String = img
                    val imageName = img_qr.substring(img_qr.lastIndexOf('/') + 1)

                    downloadImageNew(imageName, img_qr)

                    //DownloadAndSaveImageTask(this@InvoiceActivity).execute(img)
                }

            }
        }
    }

    private fun downloadImageNew(filename: String, downloadUrlOfImage: String) {
        try {
            val dm: DownloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloadUri = Uri.parse(downloadUrlOfImage)
            val request: DownloadManager.Request = DownloadManager.Request(downloadUri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(filename)
                    .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator.toString() + filename + ".jpg")
            dm.enqueue(request)
            Toast.makeText(this, "Invoice Downloaded.", Toast.LENGTH_SHORT).show()
            btn_download_invoice.setText(R.string.download_invoice)


        } catch (e: java.lang.Exception) {
            Toast.makeText(this, "Download Invoice Failed.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.cutsom_dialog_layout)
        dialog.window.setBackgroundDrawableResource(android.R.color.white)

        //  dialog .setCancelable(false)
        val close_icon = dialog.findViewById(R.id.img_close) as AppCompatImageView

        close_icon.setOnClickListener {
            finish()
            dialog.dismiss()
        }

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.BOTTOM
        //   lp.windowAnimations = R.style.DialogAnimation;
        dialog.window!!.attributes = lp
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }


    fun downloadInvoiceApi() {

        progressBar.visibility = View.VISIBLE
        val getInvoice = GetInvoice()
        getInvoice.offer_id = offerid

        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.getinvoice(getInvoice)

        call.enqueue(object : Callback<GetInvoiceResponse> {
            override fun onResponse(
                    call: Call<GetInvoiceResponse>,
                    response: retrofit2.Response<GetInvoiceResponse>
            ) {

                if (response.isSuccessful) {
                    // val invoice = response.body()!!.data!!.offerInvoice
                    if (!response.body()!!.data!!.offerInvoice.equals("")) {

                        Picasso.get()
                                .load(response.body()!!.data!!.offerInvoice)
                                .into(imageView)

                        progressBar.visibility = View.GONE
                        img = response.body()!!.data!!.offerInvoice.toString()
                    } else {
                        showDialog()
                    }

                    /* Glide.with(applicationContext)  //2
                         .load(response.body()!!.data!!.offerInvoice) //3
                         //.placeholder(R.drawable.photo_id) //5
                         .centerCrop() //4
                         .into(imageView) //8*/

                    /*if (response.body()!!.data!!.offerInvoice!!.contains(".png")) {
                        DownloadAndSaveImageTask(this@InvoiceActivity).execute(response.body()!!.data!!.offerInvoice)
                    }*/
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@InvoiceActivity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@InvoiceActivity, e.message, Toast.LENGTH_LONG)
                                .show()
                    }

                }
            }


            override fun onFailure(call: Call<GetInvoiceResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar.visibility = View.GONE
            }
        })
    }

    fun isStoragePermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) === PackageManager.PERMISSION_GRANTED) {
                return true
            } else {

                ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        1
                )
                return false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true
        }
    }
}
