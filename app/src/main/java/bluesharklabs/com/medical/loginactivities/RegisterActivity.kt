package bluesharklabs.com.medical.loginactivities

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.utils.AppConstant
import bluesharklabs.com.medical.utils.AppConstant.isEmpty
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity(), View.OnClickListener {


    var fmlae:String = ""

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
            getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }//  set status text dark


        setContentView(R.layout.activity_register)

        radiobutton_f.setOnClickListener {
            fmlae = "1"

            radiobutton_f.setButtonDrawable(R.drawable.female_selected)


            radiobutton_f.setTextColor(
                ContextCompat.getColor(
                    this@RegisterActivity,
                    R.color.black_txt
                )
            )
            radiobutton_m.setTextColor(
                ContextCompat.getColor(
                    this@RegisterActivity,
                    R.color.grey_txt
                )
            )


            radiobutton_m.setButtonDrawable(R.drawable.male_deselect)
        }
        radiobutton_m.setOnClickListener {
            fmlae = "0"
            radiobutton_m.setButtonDrawable(R.drawable.male_selected)
            radiobutton_m.setTextColor(
                ContextCompat.getColor(
                    this@RegisterActivity,
                    R.color.black_txt
                )
            )
            radiobutton_f.setTextColor(
                ContextCompat.getColor(
                    this@RegisterActivity,
                    R.color.grey_txt
                )
            )
            radiobutton_f.setButtonDrawable(R.drawable.female_deselected)
        }
        img_back.setOnClickListener(this)
        btn_next.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.btn_next -> {

                fullname = txt_name.text.toString().trim()
                phone = txt_mobil_num.text.toString().trim()
                age = txt_age.text.toString().trim()
                gender = fmlae

                if (fullname.length < 1) {
                    Toast.makeText(this, getString(R.string.enter_name), Toast.LENGTH_SHORT).show()
                } else if (isEmpty(phone)) {
                    Toast.makeText(this, getString(R.string.enter_mobilenumber), Toast.LENGTH_SHORT)
                        .show()
                } else if (phone.length < 10) {
                    Toast.makeText(this, getString(R.string.enter_valid_number), Toast.LENGTH_SHORT)
                        .show()
                } /*else if (age.length < 1) {
                    Toast.makeText(this, getString(R.string.enter_age), Toast.LENGTH_SHORT).show()
                }*/ else {
                    startActivity(Intent(this@RegisterActivity, Verify1Activity::class.java)
                        .putExtra(
                            AppConstant.CONSTANT_NUMBER,
                            phone
                        ))
                    //  finish()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
        }
    }


    companion object {
        var fullname: String = ""
        var phone: String = ""
        var age: String = ""
        var gender: String = ""
    }
}