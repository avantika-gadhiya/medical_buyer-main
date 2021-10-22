package bluesharklabs.com.medical.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.activities.PrescriptionOrderActivity.Companion.array
import bluesharklabs.com.medical.adapters.CodeAdapter
import bluesharklabs.com.medical.utils.AppConstant.photoFinishBitmap
import bluesharklabs.com.medical.utils.afterMeasured
import kotlinx.android.synthetic.main.activity_custom_final_order.*
import kotlinx.android.synthetic.main.activity_img_fullscreen.*
import kotlinx.android.synthetic.main.activity_img_fullscreen.img_back
import kotlinx.android.synthetic.main.activity_img_fullscreen.rel_img

class ImgFullscreenActivity : AppCompatActivity(), View.OnClickListener {

    var viewWidth = ""
    var viewHeight = ""
    var imgAttchmentWidth = ""
    var imgAttchmentHeight = ""


    var bmCon: Bitmap? = null
    var myBmp: Bitmap? = null
    var iv: ImageView? = null
    var childcount: Int = 0
    private var edit = ""
    var hashMap: HashMap<Int, Int> = HashMap<Int, Int>()

    private var codeAdapter: CodeAdapter? = null
    private var recycler_code: RecyclerView? = null

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//  set status text dark

        setContentView(R.layout.activity_img_fullscreen)


        if (intent != null) {

            if (intent.getStringExtra("vieworder") != null) {
                edt_ordr_prefrnc.visibility = View.GONE
            }

            if (intent.getStringExtra("edit") != null) {

                edit = intent.getStringExtra("edit")

                if (edit.equals("0")) {
                    edt_ordr_prefrnc.visibility = View.GONE
                }
            }


            val isEditable = intent.getBooleanExtra("isEditable", false)
            if (!isEditable) {
                edt_ordr_prefrnc.visibility = View.GONE
            }
        }
        imageView.setOnClickListener(this)
        img_point.setOnClickListener(this)
        img_back.setOnClickListener(this)
        edt_ordr_prefrnc.setOnClickListener(this)

        recycler_code = findViewById(R.id.recycl_code)

        recycler_code!!.setHasFixedSize(true)
        recycler_code!!.layoutManager =
                LinearLayoutManager(this, LinearLayout.VERTICAL, false) as RecyclerView.LayoutManager?
        codeAdapter = CodeAdapter(this, array)
        recycler_code!!.adapter = codeAdapter

        //edt_ordr_prefrnc

        bmCon = photoFinishBitmap
        if (bmCon != null) {
            myBmp = scaleBitmap(bmCon!!)
            imageView.setImageDrawable(BitmapDrawable(resources, myBmp))
            rel_img.afterMeasured {
                viewWidth = rel_img.width.toString()
                viewHeight = rel_img.height.toString()

                addTags()

            }
        }

        //  addTags()
    }

    fun addTags() {

        for (i in 0 until array.size) {
            iv = ImageView(this)


            val bm: Bitmap?

            bm = BitmapFactory.decodeResource(resources, R.drawable.pin)

            if (bm == null) {
                Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show()
            } else {
                iv!!.setImageBitmap(bm)
                //iv!!.setPadding(3, 3, 3, 3)

                val params = RelativeLayout.LayoutParams(bm.width, bm.height)

                childcount = rel_img.getChildCount()


                viewWidth = rel_img.width.toString()
                viewHeight = rel_img.height.toString()

                imgAttchmentWidth = photoFinishBitmap!!.width.toString()
                imgAttchmentHeight = photoFinishBitmap!!.height.toString()

                params.leftMargin =
                        (viewWidth.toInt() *
                                array.get(i).product_x_position!!.toFloat().toInt()) /
                                imgAttchmentWidth.toInt()
                params.topMargin =
                        (viewHeight.toInt() * array.get(i).product_y_position!!.toFloat().toInt()) / imgAttchmentHeight.toInt()

                hashMap.put(
                        array.get(i).product_x_position!!.toInt(),
                        array.get(i).product_y_position!!.toInt()
                )

                rel_img.addView(iv, params)

                changeBitmapColor(bm, iv!!, Color.parseColor("#" + array.get(i).color))
            }
        }
    }

    private fun changeBitmapColor(sourceBitmap: Bitmap, image: ImageView, color: Int) {

        /* val resultBitmap = Bitmap.createBitmap(
             sourceBitmap, 0, 0,
             sourceBitmap.width - 1, sourceBitmap.height - 1
         )
         val p = Paint()
         val filter = LightingColorFilter(color.toInt(), 1)
         p.setColorFilter(filter)
         image.setImageBitmap(resultBitmap)

         val canvas = Canvas(resultBitmap)
         canvas.drawBitmap(resultBitmap, 0F, 0F, p)*/

        val paint = Paint()
        paint.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        val bitmapResult =
                Bitmap.createBitmap(
                        sourceBitmap.getWidth(),
                        sourceBitmap.getHeight(),
                        Bitmap.Config.ARGB_8888
                )

        val canvas = Canvas(bitmapResult)
        canvas.drawBitmap(sourceBitmap, 0F, 0F, paint)

        image.setImageBitmap(bitmapResult)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.img_point -> {
                if (myBmp != null) {
                    PrescriptionOrderActivity.myBmp = myBmp
                    startActivity(
                            Intent(
                                    this,
                                    ImgActivity::class.java
                            )
                    )
                    //finish()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
            R.id.edt_ordr_prefrnc -> {
                startActivity(
                        Intent(this@ImgFullscreenActivity, PrescriptionOrderActivity::class.java)
                                .putExtra("edit", "1")
                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

    private fun scaleBitmap(bmp: Bitmap): Bitmap {
        var bm = bmp
        var width = bm.width
        var height = bm.height
        imgAttchmentWidth = width.toString()
        imgAttchmentHeight = height.toString()
        //   Log.v("Pictures", "Width and height are $width--$height")
//        Log.v("Pictures", "WidthHeight" + constrain_prescription.height)
        return bm
    }
}