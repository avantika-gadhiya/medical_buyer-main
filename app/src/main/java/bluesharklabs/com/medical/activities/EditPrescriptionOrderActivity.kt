package bluesharklabs.com.medical.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RelativeLayout
import android.widget.Toast
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.utils.AppConstant.colorFinalList
import bluesharklabs.com.medical.utils.AppConstant.photoFinishBitmap
import bluesharklabs.com.medical.utils.AppConstant.xData
import bluesharklabs.com.medical.utils.AppConstant.yData
import kotlinx.android.synthetic.main.activity_edit_prescription_order.*
import kotlinx.android.synthetic.main.activity_edit_prescription_order.img_back
import kotlinx.android.synthetic.main.activity_edit_prescription_order.rel
import kotlinx.android.synthetic.main.activity_partial_order_offer_accepted.view.*

class EditPrescriptionOrderActivity : AppCompatActivity(), View.OnClickListener {

    var bmCon: Bitmap? = null
    var iv: ImageView? = null
    var childcount: Int = 0
    var hashMap: HashMap<Int, Int> = HashMap<Int, Int>()
    var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView()
            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//  set status text dark

        setContentView(R.layout.activity_edit_prescription_order)


        img_back.setOnClickListener(this)
        btn_save.setOnClickListener(this)

        bmCon = photoFinishBitmap
        if (bmCon != null) {
            imageView.setImageDrawable(BitmapDrawable(resources, bmCon))
        }

        addTags()
    }

    fun addTags() {

        for (i in 0 until xData!!.size) {
            iv = ImageView(this)


            val bm: Bitmap?

            bm = BitmapFactory.decodeResource(resources, R.drawable.pin)

            if (bm == null) {
                Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show()
            } else {
                iv!!.setImageBitmap(bm)
               // iv!!.setPadding(3, 3, 3, 3)

                val params = RelativeLayout.LayoutParams(bm.height, bm.width)

                childcount = rel.getChildCount()

                params.leftMargin = xData!!.get(i).toInt()
                params.topMargin = yData!!.get(i).toInt()


                hashMap.put(xData!!.get(i).toInt(), yData!!.get(i).toInt())

                rel.addView(iv, params)

                // for (i in 0 until childcount) {
                val v = rel.getChildAt(i)
                changeBitmapColor(bm, iv!!, Color.parseColor(colorFinalList!!.get(i)))
                // }

                iv!!.setOnClickListener {
                    showAlert1(iv!!, i, Color.parseColor(colorFinalList!!.get(i)), params)
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.btn_save -> {
                //  startActivity(Intent(this@EditPrescriptionOrderActivity, FinalOrderActivity::class.java))
                finish()
                //  overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

    private fun changeBitmapColor(sourceBitmap: Bitmap, image: ImageView, color: Int) {

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

    fun showAlert1(
        v: View,
        j: Int,
        parseColor: Int,
        params: RelativeLayout.LayoutParams
    ) {
        dialog = Dialog(this)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.custom_dialog_one)
        dialog!!.window.setBackgroundDrawableResource(android.R.color.transparent)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog!!.getWindow().getAttributes())
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.NO_GRAVITY
        //  lp.windowAnimations = R.style.DialogAnimation
        dialog!!.window!!.attributes = lp

        //val radiogroup = dialog.findViewById(R.id.radiogroup) as RadioGroup?
        val rdio = dialog!!.findViewById(R.id.radiobutton_full_prescri) as RadioButton?
        val rdio1 = dialog!!.findViewById(R.id.radiobutton_less) as RadioButton?
        val txtDel = dialog!!.findViewById(R.id.txt_del) as AppCompatTextView?
        val txt_confirm = dialog!!.findViewById(R.id.txt_update) as AppCompatTextView?
        val img_close = dialog!!.findViewById(R.id.img_close) as AppCompatImageView?
        val edtqqty = dialog!!.findViewById(R.id.edt_qqty) as AppCompatEditText?
        val txt_qty = dialog!!.findViewById(R.id.txt_qty) as AppCompatTextView?
        val img_colr = dialog!!.findViewById(R.id.img_clr_code) as AppCompatImageView?

        img_colr!!.setColorFilter(parseColor)

        /*if (data!!.get(j).equals("full")) {
            rdio!!.isChecked = true
            rdio1!!.isChecked = false
            isTruee = false
        } else {
            rdio!!.isChecked = false
            rdio1!!.isChecked = true
            isTruee = true
            edtqqty!!.visibility = View.VISIBLE
            txt_qty!!.visibility = View.VISIBLE
            edtqqty.setText(data!!.get(j))
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
        }*/

        img_close!!.setOnClickListener {
            dialog!!.dismiss()
        }

        /*   txtDel!!.setOnClickListener {
               // data!!.removeAt(j)
               //  rel_img.removeView(v)
               alertDialog(v, params, j)
           }

           txt_confirm?.setOnClickListener(View.OnClickListener {
               qtyy = edtqqty!!.text.toString().trim()
               if (isTruee!! && qtyy.equals("")) {
                   Toast.makeText(
                       applicationContext,
                       "Please fill in the blanks",
                       Toast.LENGTH_SHORT
                   ).show()
               } else {

                   if (!qtyy.equals("")) {

                       data!!.set(j, qtyy)
                   }
                   dialog!!.dismiss()
               }
               if (!isTruee!!) {

                   data!!.set(j, "full")
                   dialog!!.dismiss()
               }
           })*/

        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.show()
    }
}