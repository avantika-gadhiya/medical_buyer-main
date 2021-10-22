package bluesharklabs.com.medical.loginactivities

import android.Manifest
import android.annotation.TargetApi
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.activities.MainActivity
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.loginactivities.Register1Activity.Companion.address_line_1
import bluesharklabs.com.medical.loginactivities.Register1Activity.Companion.address_line_2
import bluesharklabs.com.medical.loginactivities.Register1Activity.Companion.city
import bluesharklabs.com.medical.loginactivities.Register1Activity.Companion.landmark
import bluesharklabs.com.medical.loginactivities.Register1Activity.Companion.zipcode
import bluesharklabs.com.medical.loginactivities.RegisterActivity.Companion.age
import bluesharklabs.com.medical.loginactivities.RegisterActivity.Companion.fullname
import bluesharklabs.com.medical.loginactivities.RegisterActivity.Companion.gender
import bluesharklabs.com.medical.loginactivities.RegisterActivity.Companion.phone
import bluesharklabs.com.medical.model.AddToken
import bluesharklabs.com.medical.model.Register
import bluesharklabs.com.medical.response.CommonResponse
import bluesharklabs.com.medical.response.RegisterResponse
import bluesharklabs.com.medical.utils.AppConstant
import bluesharklabs.com.medical.utils.InternalStorageContentProvider
import bluesharklabs.com.medical.utils.ScaleFile
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_register2.*
import kotlinx.android.synthetic.main.activity_register2.progressBar
import kotlinx.android.synthetic.main.activity_verify.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class Register2Activity : AppCompatActivity(), View.OnClickListener {

    private var mFileTemp: File? = null
    private val SELECT_FILE = 1
    var bitmap: Bitmap? = null
    var path = ""
    private val REQUEST_CAMERA = 0
    val REQUEST_CODE_TAKE_PICTURE = 2
    val REQUEST_CODE_GALLERY = 1
    val INITIAL_GALLERY = arrayOf<String>(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    )

    var profilePic = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.TRANSPARENT
        }
        /*getWindow().setFlags(
             WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
             WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
         )*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }//  set status text dark

        setContentView(R.layout.activity_register2)

        val termsOfCondition = "<font color=#4db6ac><u>Terms & Conditions.</u></font>"


        val result: Spanned =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(
                            "By registering, you agree to the $termsOfCondition",
                            Html.FROM_HTML_MODE_LEGACY
                    )
                } else {
                    Html.fromHtml("By registering, you agree to the $termsOfCondition")
                }

        txt_terms_condition.text = result

        img_back.setOnClickListener(this)
        btn_next2.setOnClickListener(this)
        txt_terms_condition.setOnClickListener(this)
        img_upload.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.btn_next2 -> {

                signup()

            }
            R.id.txt_terms_condition -> {
                //  finish()
            }
            R.id.img_upload -> {
                choice()
                selectFromGallery()
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
                    img_upload?.setImageBitmap(bitmap)

                    profilePic = AppConstant.convert(bitmap!!)
                }
            }
        }
    }


    private fun signup() {

        progressBar.visibility = View.VISIBLE
        val register = Register()
        register.fullname = fullname
        register.phone = phone
        register.age = age
        register.gender = gender
        register.address_line_1 = address_line_1
        register.address_line_2 = address_line_2
        register.landmark = landmark
        register.city = city
        register.zipcode = zipcode
        register.profile_pic = profilePic


        val apiService = AppConstant.getClient()!!.create(RetrofitInterface::class.java)
        val call = apiService.register(register)

        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
            ) {

                Log.d("TAG", "RESPONSE")
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {

                    AppConstant.setPreferenceText(
                            AppConstant.PREF_USER_PROFILE_IMAGE,
                            response.body()!!.data!!.profilePic!!
                    )
                    AppConstant.setPreferenceText(
                            AppConstant.PREF_USER_ID,
                            response.body()!!.data!!.userid!!
                    )

                    AppConstant.setPreferenceText(
                            AppConstant.PREF_USER_NAME,
                            response.body()!!.data!!.fullname!!
                    )
                    AppConstant.setPreferenceText(
                            AppConstant.PREF_USER_PHONE,
                            response.body()!!.data!!.phone!!
                    )


                    addTokenApi()

                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@Register2Activity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@Register2Activity, e.message, Toast.LENGTH_LONG).show()
                    }

                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                progressBar.visibility = View.GONE

            }
        })
    }

    companion object {
        var photo_id: String = ""

    }


    fun addTokenApi() {

        progressBar.visibility = View.VISIBLE
        val addToken = AddToken()
        addToken.device_token_id = AppConstant.getPreferenceText(AppConstant.PREF_FCM_TOKEN)
        addToken.user_id = AppConstant.getPreferenceText(AppConstant.PREF_USER_ID)
        addToken.user_type = "1"
        addToken.device_type = "1"

        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.addToken(addToken)

        call.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                    call: Call<CommonResponse>,
                    response: retrofit2.Response<CommonResponse>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    startActivity(Intent(this@Register2Activity, MainActivity::class.java))
                    finish()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@Register2Activity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: java.lang.Exception) {
                        Toast.makeText(this@Register2Activity, e.message, Toast.LENGTH_LONG).show()
                    }

                }
            }


            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar.visibility = View.GONE
            }
        })
    }
}