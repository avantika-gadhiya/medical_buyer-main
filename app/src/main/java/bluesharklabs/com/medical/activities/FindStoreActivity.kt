package bluesharklabs.com.medical.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.adapters.FindStoreAdapter
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.StoreList
import bluesharklabs.com.medical.response.StoreListResponse
import bluesharklabs.com.medical.utils.AppConstant
import kotlinx.android.synthetic.main.activity_find_store.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class FindStoreActivity : AppCompatActivity(), View.OnClickListener, FindStoreAdapter.FindStore {


    private var findStoreAdapter: FindStoreAdapter? = null
    private var recycler_store: RecyclerView? = null

    var customer: String = ""
    var isSearch = false

    // var storeListArray = List<StoreListResponse.Datum>()
    var storeListArray: List<StoreListResponse.Datum> = arrayListOf()


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR//  set status text dark

        setContentView(R.layout.activity_find_store)


        img_back.setOnClickListener(this)

        if (intent != null && intent.getStringExtra("custom") != null) {
            customer = "1"
        }
        if (intent != null && intent.getStringExtra("availstore") != null) {
            txt_title.text = resources.getString(R.string.avail_store)
        }

        recycler_store = findViewById(R.id.recycl_store)
        recycler_store?.setHasFixedSize(true)
        recycler_store?.layoutManager =
            LinearLayoutManager(this, LinearLayout.VERTICAL, false)


        storeListApi()

        edt_search.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {

                // filter your list from your input
                filter(s.toString())
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
        }
    }

    fun filter(text: String) {
        val temp = arrayListOf<StoreListResponse.Datum>()
        for (d in storeListArray) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.name!!.contains(text, ignoreCase = true) || d.zipcode!!.contains(
                    text,
                    ignoreCase = true
                )
                || d.city!!.contains(text, ignoreCase = true) || d.area!!.contains(
                    text,
                    ignoreCase = true
                )
                || d.building!!.contains(
                    text,
                    ignoreCase = true
                ) || d.merchandiseCategory!!.contains(text, ignoreCase = true)
            ) {
                temp.add(d)
            }
        }
        //update recyclerview
        findStoreAdapter!!.updateList(temp)
    }

    private fun storeListApi() {
        progressBar!!.visibility = View.VISIBLE
        val storeList = StoreList()
        storeList.user_id = AppConstant.getPreferenceText(AppConstant.PREF_USER_ID)

        val apiService =
            AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.storelist(storeList)

        call.enqueue(object : Callback<StoreListResponse> {
            override fun onResponse(
                call: Call<StoreListResponse>,
                response: retrofit2.Response<StoreListResponse>
            ) {


                progressBar!!.visibility = View.GONE
                if (response.isSuccessful) {
                    Log.e("HelloStores", "isSuccessful ")
                    storeListArray = arrayListOf()
                    storeListArray = response.body()?.data!!

                    findStoreAdapter = FindStoreAdapter(
                        this@FindStoreActivity,
                        customer,
                        storeListArray,
                        this@FindStoreActivity
                    )
                    recycler_store!!.adapter = findStoreAdapter

                } else {
                    Log.e("HelloStores", "Faildes ")
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                            this@FindStoreActivity,
                            "" + jObjError.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@FindStoreActivity, e.message, Toast.LENGTH_LONG)
                            .show()
                    }

                }
            }


            override fun onFailure(call: Call<StoreListResponse>, t: Throwable) {
                // progressBarHandler.hide();
                Log.e("HelloStores", "Faildes 22")
                progressBar!!.visibility = View.GONE
            }
        })
    }

    override fun findStore(phone: String, customer: String) {

        progressBar.visibility = View.VISIBLE

        Handler().postDelayed({

            if (customer.equals("1")) {
                startActivity(
                    Intent(this@FindStoreActivity, CustomStoreDetailActivity::class.java)
                        .putExtra("phone", phone)
                )
                progressBar.visibility = View.GONE
            } else {
                startActivity(
                    Intent(this@FindStoreActivity, StoreDetailActivity::class.java)
                        .putExtra("phone", phone)
                )
                progressBar.visibility = View.GONE
            }

        }, 2000)

    }

}
