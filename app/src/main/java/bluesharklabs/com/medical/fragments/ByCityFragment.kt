package bluesharklabs.com.medical.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.adapters.ByCityAdapter
import bluesharklabs.com.medical.adapters.ByTypeAdapter
import bluesharklabs.com.medical.interfaces.FiltersInterFace
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.Filter
import bluesharklabs.com.medical.response.FilterResponse
import bluesharklabs.com.medical.utils.AppConstant
import kotlinx.android.synthetic.main.dashboard_fragment.*
import kotlinx.android.synthetic.main.fragment_by_city.*
import kotlinx.android.synthetic.main.fragment_by_store_name.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class ByCityFragment(val filterListener: FiltersInterFace) : Fragment() {


    private var recycl: RecyclerView? = null
    private var byCityAdapter: ByCityAdapter? = null
    private var orderCities: List<String> = listOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_by_city, container, false)

        init(view)
        commonFilter()

        // Inflate the layout for this fragment
        return view
    }

    override fun onResume() {
        super.onResume()
        commonFilter()
    }

    @SuppressLint("WrongConstant")
    private fun init(view: View) {

        recycl = view.findViewById(R.id.recycl)
        recycl?.setHasFixedSize(true)
        recycl?.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)

    }

//    companion object {
//
//        @JvmStatic
//        fun newInstance() =
//            ByCityFragment().apply {
//                arguments = Bundle().apply {
//
//                }
//            }
//    }


    fun commonFilter() {

        progressBarCity?.visibility = View.VISIBLE
        val filter = Filter()
        filter.user_id = AppConstant.getPreferenceText(AppConstant.PREF_USER_ID)
        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.common_filter(filter)

        call.enqueue(object : Callback<FilterResponse> {
            override fun onResponse(
                    call: Call<FilterResponse>,
                    response: retrofit2.Response<FilterResponse>
            ) {
                progressBarCity?.visibility = View.GONE
                if (response.isSuccessful) {
                    orderCities = ArrayList()
                    orderCities = response.body()?.getData()?.city!!
                    Log.e("MYFITRTR","City   "+orderCities.size)
                    byCityAdapter = ByCityAdapter(activity!!, orderCities, filterListener)
                    recycl?.adapter = byCityAdapter

                } else {

                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                                context,
                                "" + jObjError.getString("message"),
                                Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }

                }
            }


            override fun onFailure(call: Call<FilterResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBarCity?.visibility = View.GONE


            }
        })
    }


}
