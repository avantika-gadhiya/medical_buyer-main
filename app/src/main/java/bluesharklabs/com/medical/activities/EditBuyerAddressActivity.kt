package bluesharklabs.com.medical.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.OrderList
import bluesharklabs.com.medical.model.Register
import bluesharklabs.com.medical.response.BuyerDetailResponse
import bluesharklabs.com.medical.response.ProfileUpdateResponse
import bluesharklabs.com.medical.utils.AppConstant
import kotlinx.android.synthetic.main.activity_edit_buyer_address.*
import kotlinx.android.synthetic.main.activity_edit_buyer_address.progressBar
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.dashboard_fragment.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class EditBuyerAddressActivity : AppCompatActivity(), View.OnClickListener {

    var addressLine1 = ""
    var addressLine2 = ""
    var landmark = ""
    var city = ""
    var zipcode = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_buyer_address)

        val imgback: AppCompatImageView = findViewById(R.id.img_back)
        val btn_save: AppCompatButton = findViewById(R.id.btn_save)

        imgback.setOnClickListener(this)
        btn_save.setOnClickListener(this)
        myProfile()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
//                startActivity(Intent(this@EditBuyerAddressActivity,MyProfileActivity::class.java))
                finish()
            }

            R.id.btn_save -> {
                addressLine1 = edt_addressline_one.text.toString().trim()
                addressLine2 = edt_addressline_two.text.toString().trim()
                landmark = edt_landmark.text.toString().trim()
                city = edt_city.text.toString().trim()
                zipcode = edt_zip_code.text.toString().trim()
                if (addressLine1.length < 1) {
                    Toast.makeText(this@EditBuyerAddressActivity, "Enter Address1", Toast.LENGTH_LONG).show()
                } else if (addressLine2.length < 1) {
                    Toast.makeText(this@EditBuyerAddressActivity, "Enter Address2", Toast.LENGTH_LONG).show()
                } else if (city.length < 1) {
                    Toast.makeText(this@EditBuyerAddressActivity, "Enter City", Toast.LENGTH_LONG).show()
                } else if (zipcode.length < 1) {
                    Toast.makeText(this@EditBuyerAddressActivity, "Enter Zipcode", Toast.LENGTH_LONG).show()
                } else {
                    myProfileUpdate(addressLine1, addressLine2, landmark, city, zipcode)
                }
            }

        }

    }

    fun myProfile() {

        progressBar.visibility = View.VISIBLE
        val orderlist = OrderList()
        orderlist.user_id = AppConstant.getPreferenceText(AppConstant.PREF_USER_ID)

        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.buyer_detail(orderlist)

        call.enqueue(object : Callback<BuyerDetailResponse> {
            override fun onResponse(
                    call: Call<BuyerDetailResponse>,
                    response: retrofit2.Response<BuyerDetailResponse>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {

                    edt_addressline_one.setText(response.body()?.getData()?.addressLine1!!)
                    edt_addressline_two.setText(response.body()?.getData()?.addressLine2!!)
                    edt_landmark.setText(response.body()?.getData()?.landmark!!)
                    edt_city.setText(response.body()?.getData()?.city!!)
                    edt_zip_code.setText(response.body()?.getData()?.zipcode!!)


                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@EditBuyerAddressActivity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@EditBuyerAddressActivity, e.message, Toast.LENGTH_LONG).show()
                    }

                }
            }


            override fun onFailure(call: Call<BuyerDetailResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar.visibility = View.GONE


            }
        })
    }


    fun myProfileUpdate(addressLine1: String, addressLine2: String, landmark: String, city: String, zipcode: String) {

        progressBar.visibility = View.VISIBLE
        val register = Register()
        register.user_id = AppConstant.getPreferenceText(AppConstant.PREF_USER_ID)
        register.address_line_1 = addressLine1
        register.address_line_2 = addressLine2
        register.landmark = landmark
        register.city = city
        register.zipcode = zipcode

        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.buyer_detail_update(register)

        call.enqueue(object : Callback<ProfileUpdateResponse> {
            override fun onResponse(
                    call: Call<ProfileUpdateResponse>,
                    response: retrofit2.Response<ProfileUpdateResponse>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    myProfile()

                    Toast.makeText(
                            this@EditBuyerAddressActivity,
                            "Update successfully",
                            Toast.LENGTH_SHORT).show()
                } else {


                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                this@EditBuyerAddressActivity,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@EditBuyerAddressActivity, e.message, Toast.LENGTH_LONG).show()
                    }

                }
            }


            override fun onFailure(call: Call<ProfileUpdateResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar.visibility = View.GONE


            }
        })
    }
}