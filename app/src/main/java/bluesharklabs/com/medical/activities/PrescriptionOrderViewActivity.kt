package bluesharklabs.com.medical.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_prescription_order.*
import android.util.Log
import android.widget.*
import android.app.Dialog
import android.content.Intent
import android.view.*
import android.graphics.*
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import kotlin.collections.ArrayList
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import bluesharklabs.com.medical.utils.AppConstant.photoFinishBitmap
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.activities.CustomOrderActivity.Companion.arrProduct
import bluesharklabs.com.medical.activities.MainActivity.Companion.colorList
import bluesharklabs.com.medical.adapters.CodeAdapter
import bluesharklabs.com.medical.model.AddOrder
import java.sql.Array
import java.util.Collections.copy
import kotlin.collections.HashMap
import kotlin.random.Random
import com.google.android.gms.common.util.CollectionUtils
import java.util.Collections.addAll

class PrescriptionOrderViewActivity : AppCompatActivity(), View.OnClickListener, View.OnTouchListener {


    var iv: ImageView? = null
    var childcount: Int = 0
    var count: Int = 0
    var countChild: Int = 0
    private var flag: Boolean = false
    private var isTrue: Boolean = false
    private var isTruee: Boolean = false
    var bm: Bitmap? = null
    var bmCon: Bitmap? = null
    var qty: String = "0"
    var qtyy: String = ""
    var dialoge: Dialog? = null
    var hashMap: HashMap<Int, Int> = HashMap<Int, Int>()
    var colour = ""
    var productId = ""
    var arrayOne = ArrayList<AddOrder.Products>()
    var productsS = AddOrder.Products()


    var viewWidth = ""
    var viewHeight = ""
    var imgAttchmentWidth = ""
    var imgAttchmentHeight = ""
    var OriginX = 0
    var OriginY = 0

    var isEditable = false
    var isFromAttach = false

    private var codeAdapter: CodeAdapter? = null
    private var recycler_code: RecyclerView? = null

    var tempArray = ArrayList<AddOrder.Products>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//  set status text dark

        setContentView(R.layout.activity_prescription_order_view)


        img_back.setOnClickListener(this)
        imageView.setOnTouchListener(this)
        img_point.setOnClickListener(this)

        isFromAttach = intent.getBooleanExtra("isFromAttach", false)

        // txt_title.setText(R.string.prescription_order)

        /* if (childcount > 2){
            imageView.isEnabled = false

            Log.d("TAG", "childCount123==>" + rel_img.childCount)
        }else{

        }*/
        // addProducts()

        recycler_code = findViewById(R.id.recycl_code)

//        recycler_code = findViewById(R.id.recycl_code)

        recycler_code!!.setHasFixedSize(true)
        recycler_code!!.layoutManager =
                LinearLayoutManager(this, RecyclerView.VERTICAL, false) as RecyclerView.LayoutManager?
        codeAdapter = CodeAdapter(this, PrescriptionOrderActivity.array)
        recycler_code!!.adapter = codeAdapter



        arrayOne = arrayListOf()
        if (intent != null) {
            if (intent.getStringExtra("edit") != null) {

                isEditable = true
                imageView.isEnabled = false
                //arrayOne = array.toMutableList()
                // arrayOne.addAll(.filterNotNull())

                arrayOne = ArrayList<AddOrder.Products>().apply { addAll(PrescriptionOrderActivity.array) }
                //   arrayOne = (ArrayList<AddOrder.Products>())array
                //add to list
                // array = arrayOf(productsS)
                //generate a new real clone list
                // val cloneNames = array.map{it}
            }
        }

        bmCon = photoFinishBitmap
        if (bmCon != null) {

            myBmp = scaleBitmap(bmCon!!)
            imageView.setImageDrawable(BitmapDrawable(resources, myBmp))

        }


    }

    private fun randomGenerator(): String {
        val rnd: Random = Random
        val number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        val str = String.format("%06d", number)

        productId = str
        return str
    }

    private fun scaleBitmap(bmp: Bitmap): Bitmap {
        var bm = bmp
        var width = bm.width
        var height = bm.height
        imgAttchmentWidth = width.toString()
        imgAttchmentHeight = height.toString()
        Log.v("Pictures", "Width and height are $width--$height")
        Log.v("Pictures", "WidthHeight" + con_toolbar.height)
/*
        val rectangle = Rect()
        val window = getWindow()
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle)
        val statusBarHeight = rectangle.top

        var maxWidth = Resources.getSystem().getDisplayMetrics().widthPixels
        var maxHeight =
            Resources.getSystem().getDisplayMetrics().heightPixels - con_toolbar.height - constraint.height
        if (maxWidth <= width) {
            if (width > height) {
                // landscape
                val ratio = width.toFloat() / maxWidth
                width = maxWidth
                height = (height / ratio).toInt()
            } else if (height > width) {
                // portrait
                val ratio = height.toFloat() / maxHeight
                height = maxHeight
                width = (width / ratio).toInt()
            } else {
                // square
                height = maxHeight
                width = maxWidth
            }

            Log.v("Pictures", "after scaling Width and height are $width--$height")

            bm = Bitmap.createScaledBitmap(bm, width, height, true)
        }*/

        return bm
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                // arrayOne = array
                finish()
            }
            R.id.img_point -> {
                if (myBmp != null) {
                    startActivity(
                            Intent(
                                    this@PrescriptionOrderViewActivity,
                                    ImgActivity::class.java
                            )
                    )
                    //finish()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }

        }
    }


    fun addTags() {

        for (i in 0 until arrayOne.size) {
            iv = ImageView(this)

            // colorFinalList!!.add("#"+arrayOne.get(i).color)


            val bm: Bitmap?

            bm = BitmapFactory.decodeResource(resources, R.drawable.pin)

            if (bm == null) {
                Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show()
            } else {
                iv!!.setImageBitmap(bm)
                // iv!!.setPadding(3, 3, 3, 3)

                val params = RelativeLayout.LayoutParams(bm.width, bm.height)

                childcount = rel_img.getChildCount()

                /* params.leftMargin = arrayOne.get(i).product_x_position!!.toInt()
                 params.topMargin = arrayOne.get(i).product_y_position!!.toInt()*/

                viewWidth = rel_img.width.toString()
                viewHeight = rel_img.height.toString()

                Log.d("TAG", "ID--viewWidth--> $viewWidth && $viewHeight")
                Log.d("TAG", "ID--iviewWidth--> $imgAttchmentWidth && $imgAttchmentHeight")

                params.leftMargin =
                        (viewWidth.toInt() * arrayOne.get(i).product_x_position!!.toInt()) / imgAttchmentWidth.toInt()
                params.topMargin =
                        (viewHeight.toInt() * arrayOne.get(i).product_y_position!!.toInt()) / imgAttchmentHeight.toInt()

                /*  viewWidth = rel_img.width.toString()
                  viewHeight = rel_img.height.toString()

                  imgAttchmentWidth = imageView.width.toString()
                  imgAttchmentHeight = imageView.height.toString()

                  OriginX = ((imgAttchmentWidth.toInt()*params.leftMargin) / viewWidth.toInt())
                  OriginY = ((imgAttchmentHeight.toInt()* params.topMargin) / viewHeight.toInt())*/


                hashMap.put(
                        arrayOne.get(i).product_x_position!!.toInt(),
                        arrayOne.get(i).product_y_position!!.toInt()
                )

                rel_img.addView(iv, params)

                // for (i in 0 until childcount) {
                val v = rel_img.getChildAt(i)
                changeBitmapColor(bm, iv!!, Color.parseColor("#" + arrayOne.get(i).color))
                // }


            }
        }
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        val viewCoords = IntArray(2)
        imageView.getLocationOnScreen(viewCoords)
        randomGenerator()


        val touchX = event?.getX()?.toInt() as Int
        val touchY = event.getY().toInt() as Int
        iv = ImageView(this)

        bm = BitmapFactory.decodeResource(resources, R.drawable.pin)

        if (bm == null) {
            Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show()
        } else {
            iv!!.setImageBitmap(bm)
            //  iv!!.setPadding(3, 3, 3, 3)

            val params = RelativeLayout.LayoutParams(bm!!.width, bm!!.height)
            params.leftMargin = touchX
            params.topMargin = touchY
            val x = event.getX().toString()
            val y = event.getY().toString()


            OriginX = ((imgAttchmentWidth.toInt() * touchX) / viewWidth.toInt())
            OriginY = ((imgAttchmentHeight.toInt() * touchY) / viewHeight.toInt())

            /* params.leftMargin = OriginX
             params.topMargin = OriginY*/

            /*    Log.d("TAG", "ID--viewWidth--> $viewWidth && $viewHeight")
                Log.d("TAG", "ID--iviewWidth--> $imgAttchmentWidth && $imgAttchmentHeight")
                Log.d("TAG", "ID--OriginviewWidth--> $OriginX && $OriginY")
                Log.d("TAG", "ID--OiginviewWidth--> $touchX && $touchY")*/

            childcount = rel_img.getChildCount()
            /* if (flag) {
                 imageView.isEnabled = false
                 for (i in 0 until childcount) {
                     val v = rel_img.getChildAt(i)
                     v.setOnClickListener {
                         if (data!!.size > 0) {

                             Toast.makeText(
                                 applicationContext,
                                 "OnclickGetData==>" + data?.get(i - 1),
                                 Toast.LENGTH_SHORT
                             ).show()
                         }
                     }
                 }
             } else {*/
            isTrue = false



//            view?.let { showAlert(rel_img, iv!!, params, OriginX, OriginY, it, productId) }
            // }
        }
        return false
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        viewWidth = rel_img.width.toString()
        viewHeight = rel_img.height.toString()

        if (!viewWidth.equals("0") && intent.getStringExtra("edit") != null) {
            addTags()
        }
    }

    private fun handleTouch(m: MotionEvent) {
        val pointerCount = m.pointerCount

        for (i in 0 until pointerCount) {
            val x = m.getX(i)
            val y = m.getY(i)
            val id = m.getPointerId(i)
            val action = m.actionMasked
            val actionIndex = m.actionIndex
            var actionString: String

            when (action) {
                MotionEvent.ACTION_DOWN -> actionString = "DOWN"
                MotionEvent.ACTION_UP -> actionString = "UP"
                MotionEvent.ACTION_POINTER_DOWN -> actionString = "PNTR DOWN"
                MotionEvent.ACTION_POINTER_UP -> actionString = "PNTR UP"
                MotionEvent.ACTION_MOVE -> actionString = "MOVE"
                else -> actionString = ""
            }

            val touchStatus =
                    "Action: $actionString Index: $actionIndex ID: $id X: $x Y: $y"

            if (id == 0) {
                // textView1.text = touchStatus
                Log.d("TAG", "ID--0--STATUS $touchStatus")
            } else {
                //  textView2.text = touchStatus
                Log.d("TAG", "ID--STATUS $touchStatus")
            }
        }
    }

    private fun changeBitmapColor(sourceBitmap: Bitmap, image: ImageView, color: Int) {

        /* val resultBitmap = Bitmap.createBitmap(
             sourceBitmap, 0, 0,
             sourceBitmap.width - 1, sourceBitmap.height - 1
         )
         val p = Paint()
         val filter = LightingColorFilter(color, 1)
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


    companion object {

        var myBmp: Bitmap? = null
    }
}