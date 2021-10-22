package bluesharklabs.com.medical.activities

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import bluesharklabs.com.medical.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_full_q_r_image.*
import kotlinx.android.synthetic.main.activity_make_payment.*
import kotlinx.android.synthetic.main.activity_make_payment.img_back
import java.io.File

class FullQRImageActivity : AppCompatActivity(), View.OnClickListener {

    private var img_qr: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_q_r_image)

        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

        if (intent.getStringExtra("IMAGE") != null) {
            img_qr = intent.getStringExtra("IMAGE")
        }

        img_back.setOnClickListener(this)
        txt_save.setOnClickListener(this)

        Glide.with(this)  //2
                .load(img_qr) //3
                .centerCrop() //4
                .placeholder(R.drawable.ic_launcher_background) //5
                .error(R.drawable.ic_launcher_background) //6
                .fallback(R.drawable.ic_launcher_background) //7
                .placeholder(R.drawable.ic_launcher_background) //5
                .into(img_qr_code) //8

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
            Toast.makeText(this, "The QR Code saved in your Pictures folder. ", Toast.LENGTH_SHORT).show()
        } catch (e: java.lang.Exception) {
            Toast.makeText(this, "QR Code download failed.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }

            R.id.txt_save -> {
                val imageName = img_qr.substring(img_qr.lastIndexOf('/') + 1)

                downloadImageNew(imageName, img_qr)
            }

        }
    }
}