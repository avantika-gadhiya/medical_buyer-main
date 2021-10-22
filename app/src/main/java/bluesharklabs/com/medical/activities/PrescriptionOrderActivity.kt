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

class PrescriptionOrderActivity : AppCompatActivity(), View.OnClickListener, View.OnTouchListener {


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

        setContentView(R.layout.activity_prescription_order)


        btn_make_order_prefernc.setOnClickListener(this)
        img_back.setOnClickListener(this)
        imageView.setOnTouchListener(this)
        img_point.setOnClickListener(this)

        isFromAttach = intent.getBooleanExtra("isFromAttach", false)
        if (isFromAttach) {
            array.clear()
        }
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
        codeAdapter = CodeAdapter(this, array)
        recycler_code!!.adapter = codeAdapter

        arrayOne = arrayListOf()
        if (intent != null) {
            if (intent.getStringExtra("edit") != null) {

                isEditable = true
                imageView.isEnabled = false
                //arrayOne = array.toMutableList()
                // arrayOne.addAll(.filterNotNull())

                arrayOne = ArrayList<AddOrder.Products>().apply { addAll(array) }
                //   arrayOne = (ArrayList<AddOrder.Products>())array
                //add to list
                // array = arrayOf(productsS)
                //generate a new real clone list
                // val cloneNames = array.map{it}
            } else {
                array = arrayListOf()
                arrProduct = arrayListOf()


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
                                    this@PrescriptionOrderActivity,
                                    ImgActivity::class.java
                            )
                    )
                    //finish()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
            R.id.btn_make_order_prefernc -> {
                if (arrayOne.size > 0) {
                    //  for (i in 0..arrayOne.size){
                    array = arrayOne
                    //   }

                    Log.e("OnEditChange", "IsEditable   " + isEditable)

                    if (isEditable) {
                        startActivity(
                                Intent(
                                        this@PrescriptionOrderActivity,
                                        OrderPreferenceActivity::class.java
                                )
                                        .putExtra("edit", "1")
                                        .putExtra("custom", false)
                        )
                    } else {
                        startActivity(

                                Intent(
                                        this@PrescriptionOrderActivity,
                                        OrderPreferenceActivity::class.java
                                ).putExtra("custom", false)
                        )
                    }

                    //finish()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                } else {
                    Toast.makeText(this, R.string.add_tag, Toast.LENGTH_LONG).show()
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

                iv!!.setOnClickListener {

                    showAlert1(
                            iv!!,
                            i,
                            Color.parseColor("#" + arrayOne.get(i).color),
                            params,
                            productId
                    )
                }
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



            view?.let { showAlert(rel_img, iv!!, params, OriginX, OriginY, it, productId) }
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

    fun showAlert(
            rl: RelativeLayout,
            iv: ImageView,
            params: RelativeLayout.LayoutParams,
            imageX: Int?,
            imageY: Int?,
            view: View,
            productId: String
    ) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window.setBackgroundDrawableResource(android.R.color.transparent)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.getWindow().getAttributes())
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.NO_GRAVITY
        //  lp.windowAnimations = R.style.DialogAnimation;
        dialog.window!!.attributes = lp

        //val radiogroup = dialog.findViewById(R.id.radiogroup) as RadioGroup?
        val rdio = dialog.findViewById(R.id.radiobutton_full_prescri) as RadioButton?
        val rdio1 = dialog.findViewById(R.id.radiobutton_less) as RadioButton?
        val txt_cancel = dialog.findViewById(R.id.txt_cancel) as AppCompatTextView?
        val txt_confirm = dialog.findViewById(R.id.txt_confirm) as AppCompatTextView?
        val txt_product_id = dialog.findViewById(R.id.txt_product_id) as AppCompatTextView?
        val img_close = dialog.findViewById(R.id.img_close) as AppCompatImageView?
        val edtQty = dialog.findViewById(R.id.edt_qty) as AppCompatEditText?
        val txt_qty = dialog.findViewById(R.id.txt_qty) as AppCompatTextView?
        val img_clr = dialog.findViewById(R.id.img_clr_code) as AppCompatImageView?


        txt_product_id!!.setText(productId)

        productsS = AddOrder.Products()


        productsS.product_id = productId





        for (i in 0 until childcount) {
            img_clr!!.setColorFilter(Color.parseColor(colorList!!.get(i)))

            //  colour = colorList!!.get(i)
        }

        rdio!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                rdio1!!.isChecked = false
                isTrue = false
                edtQty!!.visibility = View.GONE
                txt_qty!!.visibility = View.GONE
            }
        }

        rdio1!!.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                rdio.isChecked = false
                isTrue = true
                edtQty!!.visibility = View.VISIBLE
                txt_qty!!.visibility = View.VISIBLE
            }
        }

        img_close!!.setOnClickListener {
            dialog.dismiss()
        }

        txt_cancel!!.setOnClickListener {
            dialog.dismiss()
        }

        txt_confirm?.setOnClickListener(View.OnClickListener {

            qty = edtQty!!.text.toString().trim()



            if (isTrue && qty.equals("")) {
                Toast.makeText(
                        applicationContext,
                        "Please fill in the blanks",
                        Toast.LENGTH_SHORT
                ).show()
            } else {

                if (!qty.equals("")) {

                    productsS.quantity_type = "1"
                    productsS.quantity = qty

                    productsS.product_x_position = imageX.toString()
                    productsS.product_y_position = imageY.toString()


                    productsS.color = colorList!!.get(childcount - 1).removePrefix("#")

                    arrayOne.add(productsS)
                } else if (!isTrue) {
                    productsS.quantity_type = "0"
                    productsS.quantity = "0"
                    productsS.product_x_position = imageX.toString()
                    productsS.product_y_position = imageY.toString()


                    productsS.color = colorList!!.get(childcount - 1).removePrefix("#")



                    arrayOne.add(productsS)
                }


                if (childcount <= colorList!!.size) {
                    iv.id = count
                    iv.setOnClickListener(this)
                    rel_img.addView(iv, params)
                    count++
                }
                for (i in 0 until childcount) {
                    val v = rel_img.getChildAt(i)
                    bm?.let { it1 ->
                        changeBitmapColor(
                                it1,
                                iv,
                                Color.parseColor(colorList!!.get(i))
                        )
                    }
                    // img_clr!!.=(Color.parseColor(colorList!!.get(i)))

                    iv.setOnClickListener {
                        showAlert1(iv, i, Color.parseColor(colorList!!.get(i)), params, productId)
                    }
                }

                Log.d("TAG", "AARRAY==> $array")
                dialog.dismiss()
            }
        })

        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    fun alertDialog(
            v: View,
            params: RelativeLayout.LayoutParams,
            j: Int, productId: String
    ) {
        AlertDialog.Builder(this)
                //.setTitle("Delete Tag")
                .setMessage("Are you sure you want to delete this entry?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes,
                        DialogInterface.OnClickListener { dialog, which ->
                            // Continue with delete operation

                            v.visibility = View.GONE

                            // rel_img.removeView(v)
                            /*   xData!!.removeAt(j)
                               yData!!.removeAt(j)*/
                            //   colorFinalList!!.removeAt(j)
                            colorList!!.removeAt(j)
                            arrayOne.removeAt(j)

                            /*  for(i in arrayOne.indices)
                              {

                                  arrayOne.get(i).co
                              }


                              arrayOne.contains(j)
          */


                            dialog.dismiss()
                            dialoge!!.dismiss()

                            this@PrescriptionOrderActivity.recreate()


                        })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
    }


    fun showAlert1(
            v: View,
            j: Int,
            parseColor: Int,
            params: RelativeLayout.LayoutParams,
            productId: String
    ) {
        dialoge = Dialog(this)
        dialoge!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialoge!!.setContentView(R.layout.custom_dialog_one)
        dialoge!!.window.setBackgroundDrawableResource(android.R.color.transparent)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialoge!!.getWindow().getAttributes())
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.NO_GRAVITY
        //  lp.windowAnimations = R.style.DialogAnimation
        dialoge!!.window!!.attributes = lp

        //val radiogroup = dialoge.findViewById(R.id.radiogroup) as RadioGroup?
        val rdio = dialoge!!.findViewById(R.id.radiobutton_full_prescri) as RadioButton?
        val rdio1 = dialoge!!.findViewById(R.id.radiobutton_less) as RadioButton?
        val txtDel = dialoge!!.findViewById(R.id.txt_del) as AppCompatTextView?
        val txt_confirm = dialoge!!.findViewById(R.id.txt_update) as AppCompatTextView?
        val txt_product_id = dialoge!!.findViewById(R.id.txt_product_id) as AppCompatTextView?
        val img_close = dialoge!!.findViewById(R.id.img_close) as AppCompatImageView?
        val edtqqty = dialoge!!.findViewById(R.id.edt_qqty) as AppCompatEditText?
        val txt_qty = dialoge!!.findViewById(R.id.txt_qty) as AppCompatTextView?
        val img_colr = dialoge!!.findViewById(R.id.img_clr_code) as AppCompatImageView?

        img_colr!!.setColorFilter(parseColor)
        txt_product_id!!.setText(productId)

        //   Log.d("TAG", "ID--arrayone--t" + arrayOne.get(j).quantity)
        //    Log.d("TAG", "ID--array--t" + array.get(j).quantity)

        if (arrayOne.get(j).quantity_type.equals("0")) {
            rdio!!.isChecked = true
            rdio1!!.isChecked = false
            isTruee = false
        } else {
            rdio!!.isChecked = false
            rdio1!!.isChecked = true
            isTruee = true
            edtqqty!!.visibility = View.VISIBLE
            txt_qty!!.visibility = View.VISIBLE
            edtqqty.setText(arrayOne.get(j).quantity)
        }

        rdio.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                rdio1.isChecked = false
                isTruee = false
                edtqqty!!.visibility = View.GONE
                txt_qty!!.visibility = View.GONE
            }
        }

        rdio1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                rdio.isChecked = false
                isTruee = true
                edtqqty!!.visibility = View.VISIBLE
                txt_qty!!.visibility = View.VISIBLE
            }
        }

        img_close!!.setOnClickListener {
            dialoge!!.dismiss()
        }

        txtDel!!.setOnClickListener {
            // arrayOne!!.removeAt(j)
            //  rel_img.removeView(v)

            if (arrayOne.size == 1) {
                Toast.makeText(
                        applicationContext,
                        "Need at least one tag",
                        Toast.LENGTH_SHORT
                ).show()
            } else {
                alertDialog(v, params, j, productId)
            }

        }

        txt_confirm?.setOnClickListener(View.OnClickListener {
            qtyy = edtqqty!!.text.toString().trim()
            //    Log.d("TAG", "ID--arrayone" + arrayOne.get(j).quantity)
            //   Log.d("TAG", "ID--array" + array.get(j).quantity)

            if (isTruee && qtyy.equals("")) {
                Toast.makeText(
                        applicationContext,
                        "Please fill in the blanks",
                        Toast.LENGTH_SHORT
                ).show()
            } else {
                if (!qtyy.equals("")) {

                    //  data!!.set(j, qtyy)

                    arrayOne[j].quantity = qtyy
                    arrayOne[j].quantity_type = "1"
                }
                dialoge!!.dismiss()
            }
            if (!isTruee) {

                //  data!!.set(j, "full")

                arrayOne[j].quantity_type = "0"
                arrayOne[j].quantity = "0"


                dialoge!!.dismiss()
            }
            codeAdapter!!.notifyDataSetChanged()
        })

        dialoge!!.setCanceledOnTouchOutside(false)
        dialoge!!.show()
    }

    companion object {
        var array = ArrayList<AddOrder.Products>()
        var myBmp: Bitmap? = null
    }
}