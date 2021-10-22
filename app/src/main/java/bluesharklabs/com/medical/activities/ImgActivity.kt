package bluesharklabs.com.medical.activities

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.activities.PrescriptionOrderActivity.Companion.myBmp
import bluesharklabs.com.medical.utils.AppConstant
import com.github.chrisbanes.photoview.PhotoView
import kotlinx.android.synthetic.main.activity_edit_prescription_order.*
import kotlinx.android.synthetic.main.activity_img.*
import kotlinx.android.synthetic.main.activity_img.imageView
import kotlinx.android.synthetic.main.activity_img.img_back
import kotlinx.android.synthetic.main.activity_edit_prescription_order.rel as rel1


class ImgActivity : AppCompatActivity(), View.OnClickListener {

    var iv: PhotoView? = null
    var childcount: Int = 0
    var hashMap: HashMap<Int, Int> = HashMap<Int, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView()
            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//  set status text dark

        setContentView(R.layout.activity_img)


        if (myBmp != null) {
            imageView.setImageBitmap(myBmp)
        }
        img_back.setOnClickListener(this)
        addTags()

    }

    fun addTags() {

        for (i in 0 until AppConstant.xData!!.size) {
            iv = PhotoView(this)


            val bm: Bitmap?

            bm = BitmapFactory.decodeResource(resources, R.drawable.pin)

            if (bm == null) {
                Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show()
            } else {
                iv!!.setImageBitmap(bm)
                // iv!!.setPadding(3, 3, 3, 3)

                val params = RelativeLayout.LayoutParams(bm.height, bm.width)

                childcount = relative.getChildCount()

                params.leftMargin = AppConstant.xData!!.get(i).toInt()
                params.topMargin = AppConstant.yData!!.get(i).toInt()


                hashMap.put(AppConstant.xData!!.get(i).toInt(), AppConstant.yData!!.get(i).toInt())

                relative.addView(iv, params)

                // for (i in 0 until childcount) {
                val v = relative.getChildAt(i)
                changeBitmapColor(bm, iv!!, Color.parseColor(AppConstant.colorFinalList!!.get(i)))
                // }

               /* iv!!.setOnClickListener {
                    showAlert1(iv!!, i, Color.parseColor(AppConstant.colorFinalList!!.get(i)), params)
                }*/
            }
        }
    }

    private fun changeBitmapColor(sourceBitmap: Bitmap, image: PhotoView, color: Int) {

        val resultBitmap = Bitmap.createBitmap(
                sourceBitmap, 0, 0,
                sourceBitmap.width - 1, sourceBitmap.height - 1
        )
        val p = Paint()
        val filter = LightingColorFilter(color.toInt(), 1)
        p.setColorFilter(filter)
        image.setImageBitmap(resultBitmap)

        val canvas = Canvas(resultBitmap)
        canvas.drawBitmap(resultBitmap, 0F, 0F, p)
    }


    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.img_back -> {
                finish()
            }}
    }
}
