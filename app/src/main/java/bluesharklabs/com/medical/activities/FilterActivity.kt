package bluesharklabs.com.medical.activities

import android.R.id.message
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.adapters.FilterAdapter
import bluesharklabs.com.medical.interfaces.PartialOfferFiltersInterFace
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.BuyerOrderAcceptDetail
import bluesharklabs.com.medical.model.PartialOfferFilter
import bluesharklabs.com.medical.response.GetAllProductColorsResponse
import bluesharklabs.com.medical.utils.AppConstant
import kotlinx.android.synthetic.main.activity_filter.*
import kotlinx.android.synthetic.main.activity_filter.img_back
import kotlinx.android.synthetic.main.activity_partial_coverage.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*


class FilterActivity : AppCompatActivity(), View.OnClickListener,
        DatePickerDialog.OnDateSetListener,PartialOfferFiltersInterFace {


    private var filterAdapter: FilterAdapter? = null
    private var recycler_filtr_byproduct: RecyclerView? = null
    private var cYear = 0
    private var cMonth = 0
    private var cDay = 0
    private var from_date = ""
    private var from_time: Long? = null
    private var to_date = ""
    private var order_id = ""
    private var flag: Boolean? = null
    internal var preferDate1: GregorianCalendar? = null
    private var myCalendar = Calendar.getInstance()
    var stringList = listOf<String>()
    var colorList = listOf<String>()
    var toDateAndTime = ""
    var fromDateAndTime = ""
    var is_selected = "0"
    lateinit var colorListdata: GetAllProductColorsResponse.Datum


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR//  set status text dark

        setContentView(R.layout.activity_filter)


        recycler_filtr_byproduct = findViewById(R.id.recy_by_product)

        txt_to_date.setOnClickListener(this)
        txt_from_date.setOnClickListener(this)
        txt_from_time.setOnClickListener(this)
        txt_to_time.setOnClickListener(this)
        img_back.setOnClickListener(this)
        btn_sendtostor.setOnClickListener(this)
        button_reset.setOnClickListener(this)

        if (intent != null) {

            order_id = intent.getStringExtra("order_id")
            getOrderColors(order_id)
        }



        recycler_filtr_byproduct!!.setHasFixedSize(true)
        recycler_filtr_byproduct!!.layoutManager =
                LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)
//        filterAdapter = FilterAdapter(this, stringList)
//        recycler_filtr_byproduct!!.adapter = filterAdapter

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.txt_to_date -> {
                flag = false
                showCalender1()
            }
            R.id.txt_from_date -> {
                flag = true
                showCalender()
            }
            R.id.txt_from_time -> {
                timepicker()
            }
            R.id.txt_to_time -> {
                timepicker1()
            }
            R.id.button_reset -> {
                onResetFilter()
            }
            R.id.btn_sendtostor -> {

                val fromtext = txt_from_date.text.toString().replace("/","-");
                val totext = txt_to_date.text.toString().replace("/","-");


                val finalfrom = fromtext + " "+txt_from_time.text.toString()
                val finalto = totext + " "+txt_to_time.text.toString()


                
                if(checkBox_low_high.isChecked){
                    is_selected = "1"
                }else{
                    is_selected = "0"
                }



                onApplyFilter(finalfrom,finalto,is_selected)
            }
        }
    }

    private fun showCalender() {
        val today = Date()
        myCalendar!!.time = today
        myCalendar.add(Calendar.DATE, 1)

        val minDate = myCalendar.timeInMillis

        val datePickerDialog = DatePickerDialog(
                this@FilterActivity, this,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
        )

        //datePickerDialog.datePicker.minDate = minDate
        datePickerDialog.setTitle("")
        datePickerDialog.show()
    }

    private fun showCalender1() {
        val today = Date()
        myCalendar!!.time = today
        myCalendar.add(Calendar.DATE, 1)

        val minDate = myCalendar.timeInMillis

        val datePickerDialog = DatePickerDialog(
                this@FilterActivity, this,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
        )


        preferDate1 = GregorianCalendar(cYear, cMonth, cDay)
        val mili = preferDate1!!.timeInMillis + 1000
        datePickerDialog.datePicker.minDate = mili
        datePickerDialog.setTitle("")
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myCalendar!!.set(Calendar.YEAR, year)
        myCalendar!!.set(Calendar.MONTH, month)
        myCalendar!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        if (flag == true) {
            updateLabel()
        } else {
            updateLabel1()
        }

    }

    private fun updateLabel() {
        val myFormat = "dd/MM/yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val tz = myCalendar!!.timeZone

        from_date = sdf.format(myCalendar.time)
        txt_from_date?.text = from_date
        txt_to_date?.setHint(R.string.to_date)
        txt_to_date?.text = ""
        coverrtdays(from_date)
    }

    private fun updateLabel1() {
        val myFormat = "dd/MM/yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val tz = myCalendar!!.timeZone

        from_date = sdf.format(myCalendar.time)
        txt_to_date?.text = from_date
    }

    private fun coverrtdays(from_date: String) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        var createdConvertedDate: Date? = null
        createdConvertedDate = dateFormat.parse(from_date)

        val cCal = Calendar.getInstance()
        cCal.time = createdConvertedDate
        cYear = cCal.get(Calendar.YEAR)
        cMonth = cCal.get(Calendar.MONTH)
        cDay = cCal.get(Calendar.DAY_OF_MONTH)
    }

    private fun timepicker() {
        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            from_time = cal.timeInMillis
            txt_from_time!!.text = SimpleDateFormat("hh:mm a", Locale.US).format(cal.time).toLowerCase()

            txt_to_time!!.text = ""
            txt_to_time!!.setHint(R.string.to_time_)
        }

        TimePickerDialog(
                this,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                false
        ).show()
    }

    private fun timepicker1() {
        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            if (from_time != null && cal.timeInMillis >= from_time!!) {
                //it's after current
                txt_to_time!!.text = SimpleDateFormat("hh:mm a", Locale.US).format(cal.time).toLowerCase()
            } else {
                //it's before current'
                Toast.makeText(applicationContext, "Invalid Time", Toast.LENGTH_LONG).show()
            }
            //   txt_to_time!!.text=SimpleDateFormat("hh:mm a").format(cal.time)
        }
        TimePickerDialog(
                this,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                false
        ).show()
    }


    private fun getOrderColors(orderId: String) {
        progressBar_partial!!.visibility = View.VISIBLE
        val buyerOrderAccept = BuyerOrderAcceptDetail()
        buyerOrderAccept.order_id = orderId


        val apiService =
                AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.buyerorderfilter(buyerOrderAccept)

        call.enqueue(object : Callback<GetAllProductColorsResponse> {
            override fun onResponse(
                    call: Call<GetAllProductColorsResponse>,
                    response: retrofit2.Response<GetAllProductColorsResponse>
            ) {
                progressBar_partial!!.visibility = View.GONE
                if (response.isSuccessful) {

                    colorListdata = response.body()!!.data!!
                    stringList = colorListdata.color!!
                    filterAdapter = FilterAdapter(this@FilterActivity, stringList,this@FilterActivity)
                    recycler_filtr_byproduct!!.adapter = filterAdapter

                } else {
                     try {
                         val jObjError = JSONObject(response.errorBody()!!.string())
                         Toast.makeText(
                             this@FilterActivity,
                             "" + jObjError.getString("message"),
                             Toast.LENGTH_SHORT
                         ).show()


                     } catch (e: Exception) {
                         Toast.makeText(this@FilterActivity, e.message, Toast.LENGTH_LONG)
                             .show()
                     }

                }
            }


            override fun onFailure(call: Call<GetAllProductColorsResponse>, t: Throwable) {
                progressBar_partial!!.visibility = View.GONE
            }
        })
    }


    private fun onApplyFilter(from_date_time:String,to_date_time:String,isChecked:String) {
//        progressBar_partial!!.visibility = View.VISIBLE
        val offerfilter = PartialOfferFilter()
        offerfilter.order_id = order_id
        offerfilter.from_dateandTime = from_date_time
        offerfilter.to_dateandTime = to_date_time
        offerfilter.is_selected = isChecked
        offerfilter.color_id = colorList


        val intent = Intent()
        intent.putExtra("filterData", offerfilter)
        intent.putExtra("isReset", false)
        setResult(2, intent)
        finish() //finishing activity

    }

    private fun onResetFilter(){


        val intent = Intent()
        intent.putExtra("filterData", "")
        intent.putExtra("isReset", true)
        setResult(2, intent)
        finish() //finishing activity

    }

    override fun onClickColor(colors: ArrayList<String>) {
        colorList = colors
    }
}