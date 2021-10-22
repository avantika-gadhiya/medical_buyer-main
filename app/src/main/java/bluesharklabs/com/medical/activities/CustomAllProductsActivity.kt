package bluesharklabs.com.medical.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.activities.CustomOrderActivity.Companion.arrProduct
import bluesharklabs.com.medical.activities.ViewOfferActivity.Companion.customOrderdata
import bluesharklabs.com.medical.adapters.CustomerOrderOfferAdapter
import bluesharklabs.com.medical.adapters.OrderOfferAdapter
import bluesharklabs.com.medical.adapters.ViewAllProductsAdapter
import bluesharklabs.com.medical.utils.afterMeasured
import kotlinx.android.synthetic.main.activity_custom_all_products.*
import kotlinx.android.synthetic.main.activity_custom_all_products.img_back
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.android.synthetic.main.activity_custom_all_products.rel_image
import kotlinx.android.synthetic.main.activity_img_fullscreen.*


class CustomAllProductsActivity : AppCompatActivity(), View.OnClickListener {


    private var viewAllProductsAdapter: ViewAllProductsAdapter? = null

    private var orderOfferAdapter: OrderOfferAdapter? = null
    private var customOrderOfferAdapter: CustomerOrderOfferAdapter? = null
    private var recycler: RecyclerView? = null
    private var isViewAll = "1"
    private var edit = ""
    private var isCustom = ""

    var myBmp: Bitmap? = null
    var iv: ImageView? = null
    var viewWidth = ""
    var viewHeight = ""
    var imgAttchmentWidth = ""
    var imgAttchmentHeight = ""
    var childcount: Int = 0
    var hashMap: HashMap<Int, Int> = HashMap<Int, Int>()


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//  set status text dark

        setContentView(R.layout.activity_custom_all_products)

        img_back.setOnClickListener(this)
        edt_produts.setOnClickListener(this)
        img_point_view_all.setOnClickListener(this)

        recycler = findViewById(R.id.recycl)
        recycler!!.setHasFixedSize(true)
        recycler!!.layoutManager =
                LinearLayoutManager(this, LinearLayout.VERTICAL, false) as RecyclerView.LayoutManager?

        if (intent != null) {
            if (intent.getStringExtra("edit") != null) {

                edit = intent.getStringExtra("edit")

                if (edit.equals("0")) {
                    edt_produts.visibility = View.GONE
                }

                val isEditable = intent.getBooleanExtra("isEditable", false)
                if (!isEditable) {
                    edt_produts.visibility = View.GONE
                }
            }

            if (intent.getStringExtra("custom") != null) {
                if (intent.getStringExtra("custom").equals("Custom", ignoreCase = true)) {
                    isCustom = "Custom"

                    Log.d("TAG", "customOrderdata---------" + customOrderdata!!.products)
                    customOrderOfferAdapter = CustomerOrderOfferAdapter(this, customOrderdata!!.products, isViewAll)
                    recycler!!.adapter = customOrderOfferAdapter
//                    if(!customOrderdata!!.prescriptionImage.equals("")){
//                        LoadImageWithDots(customOrderdata!!.prescriptionImage!!).execute()
//                    }

                    rel_image.visibility = View.GONE


                } else {
                    Log.d("TAG", "customOrderdata ELSEE---------" + customOrderdata!!.products!!.get(0).productMrp)
                    orderOfferAdapter = OrderOfferAdapter(this, customOrderdata!!.products, isViewAll)
                    recycler!!.adapter = orderOfferAdapter
                    rel_image.visibility = View.VISIBLE
                    if (!customOrderdata!!.prescriptionImage.equals("")) {
                        LoadImageWithDots(customOrderdata!!.prescriptionImage!!).execute()
                    }
                }

            } else {

                Log.d("TAG", "customOrderdata ELSEE MAINN ---------")
                viewAllProductsAdapter = ViewAllProductsAdapter(this, arrProduct, isViewAll)
                recycler!!.adapter = viewAllProductsAdapter
                rel_image.visibility = View.GONE

//                if(!customOrderdata!!.prescriptionImage.equals("")){
//                    LoadImageWithDots(customOrderdata!!.prescriptionImage!!).execute()
//                }
            }
        }

//
//        if (!customOrderdata?.prescriptionImage!!.equals("")) {
//            urlToBitmap(customOrderdata?.prescriptionImage!!)
//        }
        progressBar.visibility = View.VISIBLE
    }

    @SuppressLint("StaticFieldLeak")
    inner class LoadImageWithDots(var imageSrc: String) : AsyncTask<Bitmap?, Bitmap?, Bitmap>() {


        override fun onPostExecute(result: Bitmap) {
            SetBits(result)
            progressBar.visibility = View.GONE
        }

        override fun doInBackground(vararg params: Bitmap?): Bitmap? {
            return urlToBitmap(imageSrc)

        }
    }

    fun SetBits(result: Bitmap) {
        if (result != null) {
            myBmp = scaleBitmap(result)
            img_prescri.setImageDrawable(BitmapDrawable(resources, myBmp))
            rel_image.afterMeasured {
                Log.e("HelloTags", "hii")
                viewWidth = rel_image.width.toString()
                viewHeight = rel_image.height.toString()
                addTags()

            }
        } else {
            Toast.makeText(this@CustomAllProductsActivity, "Null", Toast.LENGTH_SHORT).show()
        }
    }

    fun urlToBitmap(prescriptionImage: String): Bitmap {
        val url = URL(prescriptionImage)
        val connection: HttpURLConnection
        connection = url.openConnection() as HttpURLConnection
        connection.setDoInput(true)
        connection.connect()
        val input = connection.getInputStream()
        val myBitmap = BitmapFactory.decodeStream(input)

        return myBitmap

    }


    private fun scaleBitmap(bmp: Bitmap): Bitmap {
        val bm = bmp
        val width = bm.width
        val height = bm.height
        imgAttchmentWidth = width.toString()
        imgAttchmentHeight = height.toString()
        Log.e("Picturesas", "Width and height are $width--$height")
//        Log.v("Pictures", "WidthHeight" + constrain_prescription.height)
        return bm
    }

    fun addTags() {
        Log.e("HelloTags", "addTags  " + customOrderdata?.products!!.size)

        for (i in 0 until customOrderdata?.products!!.size) {
            Log.e("HelloTags", "addTags  For  " + i)
            iv = ImageView(this)

            val bm: Bitmap?

            bm = BitmapFactory.decodeResource(resources, R.drawable.pin)

            if (bm == null) {
                Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show()
            } else {
                iv!!.setImageBitmap(bm)
                //  iv!!.setPadding(3, 3, 3, 3)

                val params = RelativeLayout.LayoutParams(bm.width, bm.height)

                childcount = rel_image.getChildCount()
                viewWidth = rel_image.width.toString()
                viewHeight = rel_image.height.toString()

                imgAttchmentWidth = myBmp!!.width.toString()
                imgAttchmentHeight = myBmp!!.height.toString()
                params.leftMargin =
                        (viewWidth.toInt() *
                                customOrderdata?.products!!.get(i).productxposition!!.toFloat().toInt()) /
                                imgAttchmentWidth.toInt()
                params.topMargin =
                        (viewHeight.toInt() * customOrderdata?.products!!.get(i).productyposition!!.toFloat().toInt()) / imgAttchmentHeight.toInt()


                Log.d("TAG", "ID--top--> " + params.leftMargin)
                Log.d("TAG", "ID--bottom-> " + params.topMargin)
                hashMap.put(
                        customOrderdata?.products!!.get(i).productxposition!!.toInt(),
                        customOrderdata?.products!!.get(i).productyposition!!.toInt()
                )

                rel_image.addView(iv, params)

                changeBitmapColor(bm, iv!!, Color.parseColor("#" + customOrderdata?.products!!.get(i).color))
                // }
            }
        }
    }

    private fun changeBitmapColor(sourceBitmap: Bitmap, image: ImageView, color: Int) {

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
            }    R.id.img_point_view_all -> {
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
            R.id.edt_produts -> {
                startActivity(
                        Intent(
                                this@CustomAllProductsActivity,
                                CustomOrderActivity::class.java
                        ).putExtra("edit", "1")
                )
                //finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }
}
