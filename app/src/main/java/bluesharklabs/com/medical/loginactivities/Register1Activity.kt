package bluesharklabs.com.medical.loginactivities

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import bluesharklabs.com.medical.R
import kotlinx.android.synthetic.main.activity_register1.*

class Register1Activity : AppCompatActivity(), View.OnClickListener {


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

        setContentView(R.layout.activity_register1)

        img_back.setOnClickListener(this)
        txt_skip.setOnClickListener(this)
        btn_next1.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.txt_skip -> {
                startActivity(Intent(this@Register1Activity, Register2Activity::class.java))
                // finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            R.id.btn_next1 -> {

                //  landmark
                address_line_1 = add1.text.toString().trim()
                address_line_2 = add2.text.toString().trim()
                city = citye.text.toString().trim()
                zipcode = zipcodey.text.toString().trim()
                landmark = landmarke.text.toString().trim()

                if (address_line_1.length < 1) {
                    Toast.makeText(this, getString(R.string.enter_add), Toast.LENGTH_SHORT).show()
                } else if (address_line_2.length < 1) {
                    Toast.makeText(this, getString(R.string.enter_add), Toast.LENGTH_SHORT).show()
                } else if (city.length < 1) {
                    Toast.makeText(this, getString(R.string.enter_city), Toast.LENGTH_SHORT).show()
                } else if (zipcode.length < 1) {
                    Toast.makeText(this, getString(R.string.enter_zipcode), Toast.LENGTH_SHORT)
                        .show()
                } else {
                    startActivity(Intent(this@Register1Activity, Register2Activity::class.java))
                    // finish()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
        }
    }



    companion object {
        var address_line_1:String = ""
        var address_line_2:String = ""
        var landmark:String = ""
        var city:String = ""
        var zipcode:String = ""

    }

}
