package bluesharklabs.com.medical.activities

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.activities.MainActivity.Companion.colorList
import bluesharklabs.com.medical.activities.PrescriptionOrderActivity.Companion.array
import bluesharklabs.com.medical.adapters.YourProductsAdapter
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.AddOrder
import bluesharklabs.com.medical.model.ProductsCount
import bluesharklabs.com.medical.response.GetAllDropdownResponse
import bluesharklabs.com.medical.utils.AppConstant
import bluesharklabs.com.medical.utils.AppConstant.photoFinishBitmap
import kotlinx.android.synthetic.main.activity_custom_order.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.random.Random


class CustomOrderActivity : AppCompatActivity(), View.OnClickListener,
        YourProductsAdapter.DeletPos {

    private var recycl: RecyclerView? = null
    private var yourProductsAdapter: YourProductsAdapter? = null
    private lateinit var productsCount: ProductsCount


    var custmProducts = AddOrder.customProducts()
    var productqty = ""
    var productContent = ""

    var productCategoryArray: List<GetAllDropdownResponse.ProductCategory> = arrayListOf()
    var producuUnitArray: List<GetAllDropdownResponse.ProductUnit> = arrayListOf()
    var productPackArray: List<GetAllDropdownResponse.ProductPack> = arrayListOf()

    private var productCategoryList = ArrayList<String>()
    private var productCategoryIdList = ArrayList<String>()
    private var producuUnitList = ArrayList<String>()
    private var producuUnitIdList = ArrayList<String>()
    private var productPackList = ArrayList<String>()
    private var productQtyList = ArrayList<String>()
    private var productContentList = ArrayList<String>()
    private var productPackIdList = ArrayList<String>()

    var isEdit = "0"

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//  set status text dark

        setContentView(R.layout.activity_custom_order)


        recycl = findViewById(R.id.recycl_your_products)

        photoFinishBitmap = null
        if (intent != null) {
            if (intent.getStringExtra("edit") != null) {
                isEdit = intent.getStringExtra("edit")

            } else {
                arrProduct = arrayListOf()
            }
        }

        img_back.setOnClickListener(this)
        btn_set_order_prefrnce.setOnClickListener(this)
        txt_add.setOnClickListener(this)

        getdropdownApi()


        recycl!!.setHasFixedSize(true)
        recycl!!.layoutManager =
                LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        yourProductsAdapter = YourProductsAdapter(this, arrProduct, this)
        recycl!!.adapter = yourProductsAdapter
    }

    private fun getdropdownApi() {
        progressBar.visibility = View.VISIBLE
        val apiService = AppConstant.getClient()!!.create(RetrofitInterface::class.java)

        val call = apiService.getalldropdown()

        call.enqueue(object : Callback<GetAllDropdownResponse> {
            override fun onResponse(
                    call: Call<GetAllDropdownResponse>,
                    response: Response<GetAllDropdownResponse>
            ) {

                progressBar.visibility = View.GONE
                if (response.isSuccessful) {

                    productCategoryArray = arrayListOf()
                    productCategoryList = arrayListOf()
                    productCategoryIdList = arrayListOf()
                    productCategoryList.clear()
//                    productCategoryList.add(0, "Select Product Category")
//                    productCategoryIdList.add(0, "Select Product Category")
                    productCategoryArray = response.body()!!.data!!.productCategory!!

                    for (i in 0 until productCategoryArray.size) {
                        productCategoryList.add(productCategoryArray[i].name!!)
                        productCategoryIdList.add(productCategoryArray[i].id!!)
                    }



                    val adapter12 = ArrayAdapter(
                            this@CustomOrderActivity,
                            android.R.layout.simple_spinner_item, productCategoryList
                    )
                    adapter12.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner_product_category.adapter = adapter12

                    spinner_product_category.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View, position: Int, id: Long
                        ) {

//                            if (position > 0) {
                                Log.e("HElloProductName","position  =  "+ productCategoryIdList[position])
                                Log.e("HElloProductName","position22  =  "+ productCategoryList[position])

                                productCategoryId = productCategoryIdList[position]
                                productCategory = productCategoryList[position]
//                            }



                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // write code to perform some action
                        }
                    }
                    productPackArray = arrayListOf()
                    productPackList = arrayListOf()
                    productQtyList = arrayListOf()
                    productContentList = arrayListOf()
                    productPackIdList = arrayListOf()

                    productPackArray = response.body()!!.data!!.productPack!!

                    for (i in 0 until productPackArray.size) {
                        productPackList.add(productPackArray[i].name!!)
                        productPackIdList.add(productPackArray[i].id!!)
                    }

                    val adapter1 = ArrayAdapter(
                            this@CustomOrderActivity,
                            android.R.layout.simple_spinner_item, productPackList
                    )
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spin_pack.adapter = adapter1

                    spin_pack.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View, position: Int, id: Long
                        ) {

                            productPackId = productPackIdList[position]
                            productPack = productPackList[position]


                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // write code to perform some action
                        }
                    }
                    productQtyList = arrayListOf()
                    productContentList = arrayListOf()
                    productQtyList.add("10")
                    productContentList.add("Select")
                    for (i in 1..1000) {
                        productQtyList.add(i.toString())
                        productContentList.add(i.toString())
                    }

                    val adapterqty = ArrayAdapter(
                            this@CustomOrderActivity,
                            android.R.layout.simple_spinner_item, productQtyList
                    )
                    adapterqty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spin_qty.adapter = adapterqty

                    spin_qty.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View, position: Int, id: Long
                        ) {

                            productqty = productQtyList[position]


                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // write code to perform some action
                        }
                    }
                    val adaptercontent = ArrayAdapter(
                            this@CustomOrderActivity,
                            android.R.layout.simple_spinner_item, productContentList
                    )
                    adaptercontent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spin_content.adapter = adaptercontent
                    spin_content.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View, position: Int, id: Long
                        ) {


                            val tv = view as TextView
                            productContent = productContentList[position]
                            /* if (position == 0) {
                                 tv.setTextColor(ContextCompat.getColor(this@CustomOrderActivity, R.color.colorGreyText))
                                 productContent = ""
                             } else {
                                 productContent = productContentList[position]
                             }
 */

                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // write code to perform some action
                        }
                    }
                    producuUnitArray = arrayListOf()
                    producuUnitList = arrayListOf()
                    producuUnitIdList = arrayListOf()

                    producuUnitArray = response.body()!!.data!!.productUnit!!
                    producuUnitList.add("Select")
                    producuUnitIdList.add("0")
                    for (i in 0 until producuUnitArray.size) {
                        producuUnitList.add(producuUnitArray[i].name!!)
                        producuUnitIdList.add(producuUnitArray[i].id!!)

                    }

                    val adapterr = ArrayAdapter(
                            this@CustomOrderActivity,
                            android.R.layout.simple_spinner_item, producuUnitList
                    )
                    adapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spin_unit.adapter = adapterr

                    spin_unit.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View, position: Int, id: Long
                        ) {
                            val tv = view as TextView
                            productUnitId = producuUnitIdList[position]
                            productUnit = producuUnitList[position]
                            /* if (position == 0) {
                                 tv.setTextColor(ContextCompat.getColor(this@CustomOrderActivity, R.color.colorGreyText))
                                 productUnitId = ""
                                 productUnit = ""
                             } else {
                                 productUnitId = producuUnitIdList[position]
                                 productUnit = producuUnitList[position]



                             }
 */

                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // write code to perform some action
                        }
                    }
                }
            }

            override fun onFailure(call: Call<GetAllDropdownResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
            }
        })
    }

    private fun randomGenerator(): String {
        val rnd: Random = Random
        val number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        val str = String.format("%06d", number)

        return str
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.txt_add -> {


                AppConstant.hideSoftKeyBoard(this@CustomOrderActivity, v)

                val productName = edt_productnm.text.toString().trim()
                val productbrand = edt_brand.text.toString().trim()
                // val productcontent = edt_content.text.toString().trim()
                //val productqty = edt_qty.text.toString().trim()

                if (productName.length < 1) {
                    Toast.makeText(this, "Enter Product Name", Toast.LENGTH_SHORT).show()
                }/* else if (productcontent.length < 1) {
                    Toast.makeText(this, "Enter Product Content", Toast.LENGTH_SHORT).show()
                }*/ /*else if (productcontent.length < 1) {
                    Toast.makeText(this, "Enter Product ContearrProduct.get(0)nt", Toast.LENGTH_SHORT).show()
                }*/ /*else if (productqty.length < 1) {
                    Toast.makeText(this, "Enter Product Quantity", Toast.LENGTH_SHORT).show()
                }*/ else {

                    if (!productContent.equals("Select") && productUnit.equals("Select")) {
                        Toast.makeText(this, "Enter Product Unit", Toast.LENGTH_SHORT).show()
                    } else if (productContent.equals("Select") && !productUnit.equals("Select")) {
                        Toast.makeText(this, "Enter Product Content", Toast.LENGTH_SHORT).show()
                    } else if (productCategoryId.equals("")) {
                        Toast.makeText(this, "Select Product Category", Toast.LENGTH_SHORT).show()
                    }
                    else {

                        Log.e("HElloProductName","OnAdd  =  "+ productCategoryId)

                        array = arrayListOf()
                        custmProducts = AddOrder.customProducts()
                        val i = arrProduct.size
                        try {
                            custmProducts.color = colorList?.get(i)!!.removePrefix("#")
                        }catch (e:Exception){


                        }


                        custmProducts.product_brand = productbrand
                        custmProducts.product_category = productCategoryId
                        custmProducts.product_id = randomGenerator()

                        custmProducts.product_name = productName
                        custmProducts.product_pack = productPackId
                        custmProducts.product_pack_name = productPack
                        Log.d("TAG", "productUnitId---" + productUnitId)
                        Log.d("TAG", "productUnit---" + productUnit)
                        custmProducts.product_unit = productUnitId

                        custmProducts.quantity = productqty

                        if (productContent.equals("Select") && productUnit.equals("Select")) {
                            custmProducts.product_unit_name = ""
                            custmProducts.product_content = ""
                        } else {
                            custmProducts.product_unit_name = productUnit
                            custmProducts.product_content = productContent
                        }


                        Log.d("Custom_ORDER","Set Order Preference   "+ custmProducts.product_category+"   PName   "+custmProducts.product_name+"   Brand   "+custmProducts.product_brand
                                +"   Brand   "+custmProducts.product_brand+"   Item   "+custmProducts.product_unit_name+"   Qty   "+custmProducts.product_content)


                        arrProduct.add(custmProducts)
                        yourProductsAdapter!!.notifyDataSetChanged()

                        edt_productnm.setText("")
                        edt_brand.setText("")
                        productCategoryId = ""
                        //edt_content.setText("")
                        //edt_qty.setText("")

                        spinner_product_category.clearSelection()
                        spin_pack.setSelection(0)
                        spin_unit.setSelection(0)
                        spin_qty.setSelection(0)
                        spin_content.setSelection(0)
                    }

                }
            }
            R.id.btn_set_order_prefrnce -> {
                /* if (intent != null && intent.getStringExtra("edit") != null) {
                     finish()
                 } else {*/


                if (isEdit.equals("1")) {



                    startActivity(
                            Intent(
                                    this@CustomOrderActivity,
                                    CustomFinalOrderActivity::class.java
                            )
                                    .putExtra("custom", "1")
                                    .putExtra("edit", isEdit)


                    )
                    //finish()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                } else {
                    if (arrProduct.size == 0) {
                        Toast.makeText(this, "Add Product", Toast.LENGTH_SHORT).show()
                    } else {

                            Log.e("Custom_ORDER","Set Order Preference   "+ arrProduct.get(0).product_category+"   PName   "+arrProduct.get(0).product_name+"   Brand   "+arrProduct.get(0).product_brand
                            +"   Brand   "+arrProduct.get(0).product_brand+"   Item   "+arrProduct.get(0).product_unit_name+"   Qty   "+arrProduct.get(0).product_content)

                        startActivity(
                                Intent(
                                        this@CustomOrderActivity,
                                        OrderPreferenceActivity::class.java
                                )
                                        .putExtra("custom", "1")
                                        .putExtra("edit", isEdit)


                        )
                        //finish()
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }
                }


                //  }
            }
        }
    }

    fun alertDialog(pos: Int) {
        AlertDialog.Builder(this)
                //.setTitle("Delete Tag")
                .setMessage("Are you sure you want to delete this entry?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes,
                        DialogInterface.OnClickListener { dialog, which ->
                            // Continue with delete operation

                            arrProduct.removeAt(pos)
                            yourProductsAdapter!!.notifyDataSetChanged()

                            dialog.dismiss()
                        })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
    }

    override fun del(pos: Int) {
        alertDialog(pos)
    }

    companion object {
        var productCategoryId = ""
        var productCategory = ""
        var productPackId = ""
        var productPack = ""
        var productUnitId = ""
        var productUnit = ""
        var arrProduct = ArrayList<AddOrder.customProducts>()
    }
}