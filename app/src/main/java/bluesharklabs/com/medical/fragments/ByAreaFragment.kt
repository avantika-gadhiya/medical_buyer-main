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
import bluesharklabs.com.medical.adapters.ByAreaAdapter
import bluesharklabs.com.medical.interfaces.FiltersInterFace
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.Filter
import bluesharklabs.com.medical.response.FilterResponse
import bluesharklabs.com.medical.utils.AppConstant
import com.bumptech.glide.Glide.init
import kotlinx.android.synthetic.main.dashboard_fragment.*
import kotlinx.android.synthetic.main.fragment_by_status.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import kotlinx.android.synthetic.main.dashboard_fragment.progressBar as progressBar1

class ByAreaFragment(val filterListener: FiltersInterFace) : Fragment() {

    private var recycl: RecyclerView? = null
    private var byAreaAdapter: ByAreaAdapter? = null
    private var areaList:List<String> = listOf()

    //val areaList = arrayOf("Satellite")
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

        val view =inflater.inflate(R.layout.fragment_by_area, container, false)

        commonFilter()
        init(view)

        // Inflate the layout for this fragment
        return view
    }

    private fun init(view: View) {

        recycl = view.findViewById(R.id.recycl)
        recycl?.setHasFixedSize(true)
        recycl?.layoutManager =
                    LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)


    }

//    companion object {
//
//        @JvmStatic
//        fun newInstance() =
//            ByAreaFragment().apply {
//                arguments = Bundle().apply {
//
//                }
//            }
//    }

    fun commonFilter() {

        progressBar?.visibility = View.VISIBLE
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
                progressBar?.visibility = View.GONE
                if (response.isSuccessful) {
                    areaList = ArrayList()
                    areaList= response.body()?.getData()?.area!!
                    byAreaAdapter = ByAreaAdapter(activity!!, areaList,filterListener)
                    recycl?.adapter = byAreaAdapter


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
                progressBar?.visibility = View.GONE
            }
        })
    }
}
