package bluesharklabs.com.medical.activities

import android.Manifest
import android.annotation.TargetApi
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.utils.AppConstant.photoFinishBitmap
import bluesharklabs.com.medical.utils.InternalStorageContentProvider
import bluesharklabs.com.medical.utils.ScaleFile
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_attach_prescription.*
import java.io.File

class AttachPrescriptionActivity : AppCompatActivity(), View.OnClickListener {

    var resizedBitmap: Bitmap? = null

    private var mFileTemp: File? = null
    private val SELECT_FILE = 1

    //    var bitmap: Bitmap? = null
    var path = ""
    private val REQUEST_CAMERA = 2
    val REQUEST_CODE_TAKE_PICTURE = 2
    val REQUEST_CODE_GALLERY = 1
    val INITIAL_GALLERY = arrayOf<String>(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR//  set status text dark

        setContentView(R.layout.activity_attach_prescription)


        btn_confirm_medicin_qnt.setOnClickListener(this)
        img_camera.setOnClickListener(this)
        img_gallery.setOnClickListener(this)
        img_e_prescri_folder.setOnClickListener(this)
        img_cancel.setOnClickListener(this)
        img_back.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {

                finish()
            }
            R.id.btn_confirm_medicin_qnt -> {

//                startActivity(
//                        Intent(
//                                this@AttachPrescriptionActivity,
//                                PrescriptionOrderActivity::class.java
//                        )
//                )

                startActivity(
                    Intent(
                        this@AttachPrescriptionActivity,
                        PrescriptionOrderActivity::class.java
                    )
                        .putExtra("isFromAttach", true)
                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }

            R.id.img_camera -> {
                choice()
                openCamera()
            }
            R.id.img_gallery -> {

                choice()
                selectFromGallery()
            }
            R.id.img_e_prescri_folder -> {

            }
            R.id.img_cancel -> {

                img_add.visibility = View.GONE
                img_cancel.visibility = View.GONE

                btn_confirm_medicin_qnt?.alpha = 0.5F
                btn_confirm_medicin_qnt?.isEnabled = false
            }
        }
    }

    fun choice() {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = File(
                Environment
                    .getExternalStorageDirectory(), "IMG_" + System.currentTimeMillis()
                        + ".jpg"
            )
        } else {
            mFileTemp = File(
                filesDir, "IMG_" + System.currentTimeMillis()
                        + ".jpg"
            )
        }
    }


    private fun openCamera() {

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        if (Build.VERSION.SDK_INT >= 23) {
            if (!canAccessCamera()) {
                requestPermissions(INITIAL_GALLERY, 140)
            } else {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                var mImageCaptureUri: Uri? = null
                val state = Environment.getExternalStorageState()
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    mImageCaptureUri = Uri.fromFile(mFileTemp)
                } else {
                    mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI
                }
                intent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    mImageCaptureUri
                )
                intent.putExtra("return-data", true)
                startActivityForResult(intent, REQUEST_CAMERA)
            }
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            var mImageCaptureUri: Uri? = null
            val state = Environment.getExternalStorageState()
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                mImageCaptureUri = Uri.fromFile(mFileTemp)
            } else {
                mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI
            }
            intent.putExtra(
                MediaStore.EXTRA_OUTPUT,
                mImageCaptureUri
            )
            intent.putExtra("return-data", true)
            startActivityForResult(intent, REQUEST_CAMERA)
        }
    }

    private fun selectFromGallery() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!canAccessCamera()) {
                requestPermissions(INITIAL_GALLERY, 1340)
            } else {
                val photoPickerIntent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.type = "image/*"
                startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY)
            }
        } else {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY)
        }
    }

    private fun canAccessCamera(): Boolean {
        return hasPermission(Manifest.permission.CAMERA) && hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE) && hasPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    private fun canAccessCameraA(): Boolean {
        return hasPermission(Manifest.permission.CAMERA)
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun hasPermission(perm: String): Boolean {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1340) {

            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val photoPickerIntent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.type = "image/*"
                startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY)
            } else {
                Toast.makeText(this, "Premission Required", Toast.LENGTH_SHORT).show()
            }
        } else {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try {
                    var mImageCaptureUri: Uri? = null
                    val state = Environment.getExternalStorageState()
                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        mImageCaptureUri = Uri.fromFile(mFileTemp)
                    } else {
                        mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI
                    }
                    intent.putExtra(
                        MediaStore.EXTRA_OUTPUT,
                        mImageCaptureUri
                    )
                    intent.putExtra("return-data", true)
                    startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }

            } else {
                Toast.makeText(this, R.string.permission_required, Toast.LENGTH_SHORT).show()
            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("TAG", "REQUEST==>" + requestCode)
        Log.d("TAG", "data==>" + data)

/*

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                val bitmap = data?.extras?.get("data") as Bitmap
                img_add.visibility = View.VISIBLE
                img_cancel.visibility = View.VISIBLE
                img_add.setImageBitmap(bitmap)

                photoFinishBitmap = bitmap

                btn_confirm_medicin_qnt?.setAlpha(1F)
                btn_confirm_medicin_qnt?.setEnabled(true)
            } else if (requestCode == SELECT_FILE) {
                val uri = data?.getData()

                img_add.visibility = View.VISIBLE
                img_cancel.visibility = View.VISIBLE
                img_add.setImageURI(uri)

                var bitmap: Bitmap? = null
                try {
                    bitmap = getBitmap(this.contentResolver, uri)
                    photoFinishBitmap = bitmap
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                btn_confirm_medicin_qnt?.setAlpha(1F)
                btn_confirm_medicin_qnt?.setEnabled(true)

            }
        }
*/


        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_FILE) {
                val selectedImageUri = data?.data
                CropImage.activity(selectedImageUri)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setGuidelines(CropImageView.Guidelines.OFF)
                    .setAspectRatio(3, 4)
                    .setInitialCropWindowPaddingRatio(0F)
                    .setFixAspectRatio(true)
                    .setActivityTitle(resources.getString(R.string.my_crop))
                    .start(this)
            }

            if (requestCode == REQUEST_CAMERA) {
                val selectedImageUri = Uri.fromFile(mFileTemp)
                    CropImage.activity(selectedImageUri)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setFixAspectRatio(true)
                    .setGuidelines(CropImageView.Guidelines.OFF)
                    .setAspectRatio(3, 4)
                    .setInitialCropWindowPaddingRatio(0f)
                    .setActivityTitle(resources.getString(R.string.my_crop))
                    .start(this)
            }
            //C8:8A:98:55:E8:A5:88:1B:87:DA:A1:78:30:F5:24:FE:06:C6:83:1C
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val result = CropImage.getActivityResult(data)
                val resultUri = result.uri
                path = resultUri.path
                if (resultUri.path == null) {
                    return
                } else {

                    img_add.visibility = View.VISIBLE
                    img_cancel.visibility = View.VISIBLE
//                    bitmap = BitmapFactory.decodeFile(path)
                    val bitmap11 = ScaleFile.compressImage(path, this)

                    Log.e(
                        "MYVITD",
                        "decoded   " + bitmap11!!.byteCount + "   Bit   " + bitmap11.byteCount
                    )

//                    Log.e("MYVITD", "ORI   " + bitmap!!.byteCount)


//
//                    val out = ByteArrayOutputStream()
//                    bitmap!!.compress(Bitmap.CompressFormat.JPEG, 50, out)
//                    val decoded = BitmapFactory.decodeStream(ByteArrayInputStream(out.toByteArray()))


//                    scaleImage(bitmap11!!)

                    img_add?.setImageBitmap(bitmap11)
                    //   photoFinishBitmap = resizedBitmap
                    photoFinishBitmap = bitmap11


                    //  imgResolution()
                    btn_confirm_medicin_qnt?.alpha = 1F
                    btn_confirm_medicin_qnt?.isEnabled = true
                    //resizedBitmap = resizeBitmap(bitmap!!, 700, 500)

                    if (determineScreenDensityCode().equals("xxxhdpi")) {
                        Log.d("TAG", "DISPLAY==>" + determineScreenDensityCode())
                    } else if (determineScreenDensityCode().equals("xxhdpi")) {
                        Log.d("TAG", "DISPLAY==>" + determineScreenDensityCode())
                    } else if (determineScreenDensityCode().equals("xhdpi")) {
                        Log.d("TAG", "DISPLAY==>" + determineScreenDensityCode())
                    } else if (determineScreenDensityCode().equals("hdpi")) {
                        Log.d("TAG", "DISPLAY==>" + determineScreenDensityCode())
                    } else if (determineScreenDensityCode().equals("mdpi")) {
                        Log.d("TAG", "DISPLAY==>" + determineScreenDensityCode())
                    } else if (determineScreenDensityCode().equals("ldpi")) {
                        Log.d("TAG", "DISPLAY==>" + determineScreenDensityCode())
                    }


                }
            }
        }
    }


    // Method to resize a bitmap programmatically
    private fun resizeBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        /*
            *** reference source developer.android.com ***
            Bitmap createScaledBitmap (Bitmap src, int dstWidth, int dstHeight, boolean filter)
                Creates a new bitmap, scaled from an existing bitmap, when possible. If the specified
                width and height are the same as the current width and height of the source bitmap,
                the source bitmap is returned and no new bitmap is created.

            Parameters
                src Bitmap : The source bitmap.
                    This value must never be null.

            dstWidth int : The new bitmap's desired width.
            dstHeight int : The new bitmap's desired height.
            filter boolean : true if the source should be filtered.

            Returns
                Bitmap : The new scaled bitmap or the source bitmap if no scaling is required.

            Throws
                IllegalArgumentException : if width is <= 0, or height is <= 0
        */
        return Bitmap.createScaledBitmap(
            bitmap,
            width,
            height,
            false
        )
    }


    private fun imgResolution() {

        val width = img_add.drawable.intrinsicWidth
        val height = img_add.drawable.intrinsicHeight
        val scale: Int = (img_add.width) / width
        val final_height: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Math.toIntExact(Math.round((scale * height).toDouble()))
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        Log.d("TAG", "Final Height: $final_height Width: $width height: $height")
        /* var yourImageViewWidth: Int
         var yourImageViewHeight: Int
         val vto = img_add.getViewTreeObserver()
         vto.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
             override fun onPreDraw(): Boolean {
                 img_add.getViewTreeObserver().removeOnPreDrawListener(this)
                 yourImageViewHeight = img_add.getMeasuredHeight()
                 yourImageViewWidth = img_add.getMeasuredWidth()
                 Log.d("TAG","Height: $yourImageViewHeight Width: $yourImageViewWidth")
                 return true
             }
         })*/
    }

    private fun scaleImage(bitmap: Bitmap) {
        /* code for scaling bitmap using screen dimensions to suit all screen sizes*/

/* DisplayMetrics A structure describing general information about a display, such as its size, density, and font scaling. */

        val displaymetrics = DisplayMetrics()
//getting display data
        windowManager.defaultDisplay.getMetrics(displaymetrics)
        val height = displaymetrics.heightPixels//screen height in pixels
        val width = displaymetrics.widthPixels//screen width in pixels
        val w = (width / 2).toFloat()//scaling w.r.t screen dimensions
        val h = (height / 2).toFloat()

        val w1 = w.toInt()//casting float values to int as the createScaledBitmap takes int args
        val h1 = h.toInt()


        Log.d("TAG", "displaymetrics.height==>" + height)
        Log.d("TAG", "displaymetrics.width==>" + width)
        Log.d("Medicine", "bitmap.height==>" + bitmap.height)
        Log.d("Medicine", "bitmap.width==>" + bitmap.width)

        val scaledBmap = Bitmap.createScaledBitmap(bitmap, w1, h1, false)
/*displaying captured pic*/
        photoFinishBitmap = scaledBmap
    }

    fun determineScreenDensityCode(): String {
        return when (resources.displayMetrics.densityDpi) {
            DisplayMetrics.DENSITY_LOW -> "ldpi"
            DisplayMetrics.DENSITY_MEDIUM -> "mdpi"
            DisplayMetrics.DENSITY_HIGH -> "hdpi"
            DisplayMetrics.DENSITY_XHIGH, DisplayMetrics.DENSITY_280 -> "xhdpi"
            DisplayMetrics.DENSITY_XXHIGH, DisplayMetrics.DENSITY_360, DisplayMetrics.DENSITY_400, DisplayMetrics.DENSITY_420 -> "xxhdpi"
            DisplayMetrics.DENSITY_XXXHIGH, DisplayMetrics.DENSITY_560 -> "xxxhdpi"
            else -> "Unknown code ${resources.displayMetrics.densityDpi}"
        }
    }
}