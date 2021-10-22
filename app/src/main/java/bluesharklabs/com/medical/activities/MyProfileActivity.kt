package bluesharklabs.com.medical.activities

import android.Manifest
import android.annotation.TargetApi
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.OrderList
import bluesharklabs.com.medical.model.Register
import bluesharklabs.com.medical.response.BuyerDetailResponse
import bluesharklabs.com.medical.response.ProfileUpdateResponse
import bluesharklabs.com.medical.utils.AppConstant
import bluesharklabs.com.medical.utils.InternalStorageContentProvider
import bluesharklabs.com.medical.utils.ScaleFile
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.Target
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_final_order.img_back
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.dashboard_fragment.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.File


class MyProfileActivity : AppCompatActivity(), View.OnClickListener {

    private var mFileTemp: File? = null
    private val SELECT_FILE = 1
    var bitmap: Bitmap? = null
    var path = ""
    var gender = ""
    var profilePic=""

    private val REQUEST_CAMERA = 0
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

        setContentView(R.layout.activity_my_profile)

        val spin_gen = arrayOf("Male", "Female")
        spinGender(spin_gen)

        img_profile.setOnClickListener(this)
        txt_logout.setOnClickListener(this)
        img_back.setOnClickListener(this)
        btn_save.setOnClickListener(this)
        txt_edt.setOnClickListener(this)

        myProfile()
    }


    fun spinGender(spinGen: Array<String>) {
        val arrayAdapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinGen)
        spiner.adapter = arrayAdapter

        spiner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                gender = spinGen.get(position)

                if (gender.equals("Male")) {
                    gender = "0"
                } else {
                    gender = "1"
                }

                Log.d("TAG", "gender----" + gender)
                //   spiner.setSelection(position)

                /* Toast.makeText(
                     this@OrderPreferenceActivity,
                     "Slected Item" + " " + orderTypes[position],
                     Toast.LENGTH_SHORT
                 ).show()*/

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {

                finish()
            }
            R.id.txt_logout -> {

            }
            R.id.img_profile -> {
                Choice()

            }
            R.id.btn_save -> {
                var name = txt_full_name.text.toString().trim()
                var number = txt_mobil_num.text.toString().trim()
                var age = txt_age.text.toString().trim()
                var gender = gender

                if (name.isEmpty()) {
                    Toast.makeText(this@MyProfileActivity, "Enter Name", Toast.LENGTH_LONG).show()
                } else if (number.isEmpty()) {
                    Toast.makeText(this@MyProfileActivity, "Enter Number", Toast.LENGTH_LONG).show()
                }else if (number.length != 10) {
                    Toast.makeText(this@MyProfileActivity, "Enter valid Number", Toast.LENGTH_LONG).show()
                } else if (age.isEmpty()) {
                    Toast.makeText(this@MyProfileActivity, "Enter Age", Toast.LENGTH_LONG).show()
                } else if (gender.isEmpty()) {
                    Toast.makeText(this@MyProfileActivity, "Enter Gender", Toast.LENGTH_LONG).show()
                } else {
                    myProfileUpdate(name, number, age, gender)
                }

            }
            R.id.txt_edt -> {
                startActivity(Intent(this@MyProfileActivity, EditBuyerAddressActivity::class.java))
            }
        }
    }

    private fun Choice() {
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
        val items =
                arrayOf(resources.getString(R.string.take_from_camera), resources.getString(R.string.select_from_gallery))
        val builderSingle = AlertDialog.Builder(this)
        builderSingle.setTitle(resources.getString(R.string.make_your_selection))
        builderSingle.setItems(items, DialogInterface.OnClickListener { dialog, which ->
            if (which == 0) {
                openCamera()
            } else if (which == 1) {
                selectFromGallery()
            }
        }).create().show()
    }

    private fun openCamera() {

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
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
            startActivityForResult(intent, REQUEST_CAMERA)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
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

    @TargetApi(Build.VERSION_CODES.M)
    private fun hasPermission(perm: String): Boolean {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
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

        if (resultCode == androidx.appcompat.app.AppCompatActivity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                val selectedImageUri = data?.data
                CropImage.activity(selectedImageUri)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setAspectRatio(1, 1)
                    .setInitialCropWindowPaddingRatio(0f)
                        .setGuidelines(CropImageView.Guidelines.OFF)
                        .setActivityTitle(resources.getString(R.string.my_crop))
                        .start(this)
            }
            if (requestCode == REQUEST_CAMERA) {
                val selectedImageUri = Uri.fromFile(mFileTemp)
                CropImage.activity(selectedImageUri)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setAspectRatio(1, 1)
                    .setInitialCropWindowPaddingRatio(0f)
                        .setGuidelines(CropImageView.Guidelines.OFF)
                        .setActivityTitle(resources.getString(R.string.my_crop))
                        .start(this)
            }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val result = CropImage.getActivityResult(data)
                val resultUri = result.uri
                //imgProfile.setImageURI(resultUri);
                path = resultUri.path
                if (resultUri.path == null) {
                    return
                } else {
//                    bitmap = BitmapFactory.decodeFile(path)
                    bitmap = ScaleFile.compressImage(path, this)

                    try {
                        Glide.with(this@MyProfileActivity)
                                .load(bitmap)
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
                                        profilePic= AppConstant.convert(bitmap!!)
                                        return false
                                    }

                                }).placeholder(R.drawable.ic_launcher_background)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imageView2)

//                                 Glide.with(this@MyProfileActivity)  //2
//                                .load(response.body()?.getData()?.profilePic) //3
//                                .error(R.drawable.ic_launcher_background) //6
//                                .into(imageView2) //8


                    } catch (e: Exception) {
                        Log.e("HElloPicError", "E   " + e)
                    }


//                    imageView2?.setImageBitmap(bitmap)



                }
            }
        }
    }

    fun myProfile() {

        progressBar_profile.visibility = View.VISIBLE
        val orderlist = OrderList()
        orderlist.user_id = AppConstant.getPreferenceText(AppConstant.PREF_USER_ID)

        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.buyer_detail(orderlist)

        call.enqueue(object : Callback<BuyerDetailResponse> {
            override fun onResponse(
                    call: Call<BuyerDetailResponse>,
                    response: retrofit2.Response<BuyerDetailResponse>
            ) {
                progressBar_profile.visibility = View.GONE
                if (response.isSuccessful) {
                    txt_full_name.setText(response.body()?.getData()?.fullname)
                    txt_mobil_num.setText(response.body()?.getData()?.phone)
                    txt_age.setText(response.body()?.getData()?.age)
                    txt_add.text = response.body()?.getData()?.addressLine1 + ", " + response.body()?.getData()?.addressLine2 + ", " +
                            response.body()?.getData()?.city + ", " + response.body()?.getData()?.zipcode

                    if (response.body()?.getData()?.genderId!!.equals("0")) {
                        spiner.setSelection(0)
                    } else {
                        spiner.setSelection(1)
                    }

                    try {

                        Log.e("HelloUrl", "hi    " + response.body()?.getData()?.profilePic)

                        AppConstant.setPreferenceText(
                                AppConstant.PREF_USER_PROFILE_IMAGE,
                                response.body()?.getData()!!.profilePic!!
                        )

                        Glide.with(this@MyProfileActivity)
                                .load(response.body()?.getData()?.profilePic)
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
                                .into(imageView2)

//                                 Glide.with(this@MyProfileActivity)  //2
//                                .load(response.body()?.getData()?.profilePic) //3
//                                .error(R.drawable.ic_launcher_background) //6
//                                .into(imageView2) //8


                    } catch (e: Exception) {
                        Log.e("HElloPicError", "E   " + e)
                    }


                } else {


                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@MyProfileActivity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@MyProfileActivity, e.message, Toast.LENGTH_LONG).show()
                    }

                }
            }


            override fun onFailure(call: Call<BuyerDetailResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar_profile.visibility = View.GONE


            }
        })
    }


    fun myProfileUpdate(name: String, number: String, age: String, gender: String) {

        progressBar_profile.visibility = View.VISIBLE
        val register = Register()
        register.user_id = AppConstant.getPreferenceText(AppConstant.PREF_USER_ID)
        register.fullname = name
        register.phone = number
        register.age = age
        register.gender = gender
        register.profile_pic = profilePic

        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.buyer_detail_update(register)

        call.enqueue(object : Callback<ProfileUpdateResponse> {
            override fun onResponse(
                    call: Call<ProfileUpdateResponse>,
                    response: retrofit2.Response<ProfileUpdateResponse>
            ) {
                progressBar_profile.visibility = View.GONE
                if (response.isSuccessful) {


                    myProfile()
                    Toast.makeText(
                            this@MyProfileActivity,
                            "Update successfully",
                            Toast.LENGTH_SHORT).show()
                } else {


                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@MyProfileActivity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@MyProfileActivity, e.message, Toast.LENGTH_LONG).show()
                    }

                }
            }


            override fun onFailure(call: Call<ProfileUpdateResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar_profile.visibility = View.GONE


            }
        })
    }
}
