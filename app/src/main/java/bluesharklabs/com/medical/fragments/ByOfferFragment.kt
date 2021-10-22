package bluesharklabs.com.medical.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.adapters.ByOfferAdapter
import bluesharklabs.com.medical.adapters.ByTypeAdapter
import bluesharklabs.com.medical.interfaces.FiltersInterFace
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.Filter
import bluesharklabs.com.medical.response.FilterResponse
import bluesharklabs.com.medical.utils.AppConstant
import kotlinx.android.synthetic.main.dashboard_fragment.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class ByOfferFragment(val filterListener: FiltersInterFace) : Fragment() {

    private var byOfferAdapter: ByOfferAdapter? = null
    private var orderOffers: List<FilterResponse.OrderOffer> = listOf()
    private var recycl: RecyclerView? = null

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

        val view = inflater.inflate(R.layout.fragment_by_offer, container, false)

        init(view)
        commonFilter()
        // Inflate the layout for this fragment
        return view
    }

    @SuppressLint("WrongConstant")
    private fun init(view: View) {

        recycl = view.findViewById(R.id.recycl)
        recycl?.setHasFixedSize(true)
        recycl?.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)

    }

//    companion object {
//
//        @JvmStatic
//        fun newInstance() =
//            ByTypeFragment().apply {
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

                    orderOffers = response.body()?.getData()?.orderOffer!!
                    byOfferAdapter = ByOfferAdapter(activity!!, orderOffers, filterListener)
                    recycl?.adapter = byOfferAdapter

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
