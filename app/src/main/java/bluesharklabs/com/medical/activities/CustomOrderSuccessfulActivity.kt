package bluesharklabs.com.medical.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import bluesharklabs.com.medical.R
import kotlinx.android.synthetic.main.activity_custom_order_successful.*

class CustomOrderSuccessfulActivity : AppCompatActivity(),View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView()
            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//  set status text dark

        setContentView(R.layout.activity_custom_order_successful)


        btn_back_t_dasbrd.setOnClickListener (this)
        img_back.setOnClickListener (this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.img_back->{
                finish()
            }
            R.id.btn_back_t_dasbrd->{
                startActivity(Intent(this@CustomOrderSuccessfulActivity, MainActivity::class.java))
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }
}
