package bluesharklabs.com.medical.loginactivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import bluesharklabs.com.medical.R
import bluesharklabs.com.medical.interfaces.RetrofitInterface
import bluesharklabs.com.medical.model.Verification
import bluesharklabs.com.medical.response.VerificationResponse
import bluesharklabs.com.medical.utils.AppConstant
import bluesharklabs.com.medical.utils.AppConstant.CONSTANT_NUMBER
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException

import kotlinx.android.synthetic.main.activity_verify1.*
import kotlinx.android.synthetic.main.activity_verify1.btn_verify
import kotlinx.android.synthetic.main.activity_verify1.edt_otp_1
import kotlinx.android.synthetic.main.activity_verify1.edt_otp_2
import kotlinx.android.synthetic.main.activity_verify1.edt_otp_3
import kotlinx.android.synthetic.main.activity_verify1.edt_otp_4
import kotlinx.android.synthetic.main.activity_verify1.edt_otp_5
import kotlinx.android.synthetic.main.activity_verify1.edt_otp_6
import kotlinx.android.synthetic.main.activity_verify1.text_resend
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.lang.Exception
import java.lang.StringBuilder
import java.util.concurrent.TimeUnit

class Verify1Activity : AppCompatActivity(), View.OnClickListener {


    private var device_id = ""
    private var device_token = ""
    // [START declare_auth]
  //  private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    private var verificationInProgress = false
    private var storedVerificationId: String? = ""
    private var numbr: String = ""
    private var otp: String = ""
  //  private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    // private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN)

        getWindow().getDecorView()
            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)//  set status text dark

        setContentView(R.layout.activity_verify1)


      //  auth = FirebaseAuth.getInstance()
        device_id = AppConstant.getPreferenceText(AppConstant.DEVICE_ID)
        device_token = AppConstant.getPreferenceText(AppConstant.DEVICE_TOKEN)



        if (intent != null) {
            numbr ="+91" + intent.getStringExtra(CONSTANT_NUMBER)
            verificationApi(numbr)

            //   startPhoneNumberVerification(numbr)
            txt_num.setText(numbr)
        }

        // txtEnterMobile.text = resources.getString(R.string.txt_otp_we_sent, number)
        moveIndexes()

        btn_verify.setOnClickListener(this)
        text_resend.setOnClickListener(this)
        img_back.setOnClickListener(this)
    }


    fun moveIndexes() {
        edt_otp_1.requestFocus()
        edt_otp_1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


                edt_otp_1.inputType = InputType.TYPE_CLASS_NUMBER
                //    edt_otp_1.transformationMethod = PasswordTransformationMethod.getInstance()

                if (s?.length == 1) {
                    edt_otp_2.requestFocus()
                    edt_otp_1.background =
                        ContextCompat.getDrawable(this@Verify1Activity, R.drawable.otp_box_selected)
                } else {
                    edt_otp_1.background =
                        ContextCompat.getDrawable(this@Verify1Activity, R.drawable.otp_box_empty)
                }

            }

            override fun afterTextChanged(editable: Editable) {

            }
        })


        edt_otp_2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                edt_otp_2.inputType = InputType.TYPE_CLASS_NUMBER
                //  edt_otp_2.transformationMethod = PasswordTransformationMethod.getInstance()


                if (s?.length == 1) {
                    edt_otp_3.requestFocus()
                    edt_otp_2.background =
                        ContextCompat.getDrawable(this@Verify1Activity, R.drawable.otp_box_selected)
                } else {
                    edt_otp_1.requestFocus()
                    edt_otp_2.background =
                        ContextCompat.getDrawable(this@Verify1Activity, R.drawable.otp_box_empty)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        edt_otp_3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                edt_otp_3.inputType = InputType.TYPE_CLASS_NUMBER
                //   edt_otp_3.transformationMethod = PasswordTransformationMethod.getInstance()


                if (s?.length == 1) {
                    edt_otp_4.requestFocus()
                    edt_otp_3.background =
                        ContextCompat.getDrawable(this@Verify1Activity, R.drawable.otp_box_selected)
                } else {
                    edt_otp_2.requestFocus()
                    edt_otp_3.background =
                        ContextCompat.getDrawable(this@Verify1Activity, R.drawable.otp_box_empty)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        edt_otp_4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                edt_otp_4.inputType = InputType.TYPE_CLASS_NUMBER
                //   edt_otp_4.transformationMethod = PasswordTransformationMethod.getInstance()


                if (s?.length == 1) {
                    edt_otp_5.requestFocus()
                    edt_otp_4.background =
                        ContextCompat.getDrawable(this@Verify1Activity, R.drawable.otp_box_selected)
                } else {
                    edt_otp_3.requestFocus()
                    edt_otp_4.background =
                        ContextCompat.getDrawable(this@Verify1Activity, R.drawable.otp_box_empty)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


        edt_otp_5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                edt_otp_5.inputType = InputType.TYPE_CLASS_NUMBER
                //   edt_otp_5.transformationMethod = PasswordTransformationMethod.getInstance()


                if (s?.length == 1) {
                    edt_otp_6.requestFocus()
                    edt_otp_5.background =
                        ContextCompat.getDrawable(this@Verify1Activity, R.drawable.otp_box_selected)
                } else {
                    edt_otp_4.requestFocus()
                    edt_otp_5.background =
                        ContextCompat.getDrawable(this@Verify1Activity, R.drawable.otp_box_empty)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        edt_otp_6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                edt_otp_6.inputType = InputType.TYPE_CLASS_NUMBER
                //   edt_otp_5.transformationMethod = PasswordTransformationMethod.getInstance()


                if (s?.length == 0) {
                    edt_otp_5.requestFocus()
                    edt_otp_6.background =
                        ContextCompat.getDrawable(this@Verify1Activity, R.drawable.otp_box_empty)
                } else
                    edt_otp_6.background =
                        ContextCompat.getDrawable(this@Verify1Activity, R.drawable.otp_box_selected)
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }


   /* private fun startPhoneNumberVerification(phoneNumber: String) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber, // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            this, // Activity (for callback binding)
            callbacks
        ) // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        //  progressBar.visibility = View.VISIBLE
        verificationInProgress = true
    }

    var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d("TAG", "onVerificationCompleted:$credential")
            // [START_EXCLUDE silent]
            progressBar.visibility = View.GONE
            verificationInProgress = false
            // [END_EXCLUDE]

            // [START_EXCLUDE silent]
            // Update the UI and attempt sign in with the phone credential
            // updateUI(STATE_VERIFY_SUCCESS, credential)
            // [END_EXCLUDE]
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w("TAG", "onVerificationFailed", e)
            // [START_EXCLUDE silent]
            progressBar.visibility = View.GONE
            verificationInProgress = false
            // [END_EXCLUDE]

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // [START_EXCLUDE]
                // fieldPhoneNumber.error = "Invalid phone number."
                // [END_EXCLUDE]
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // [START_EXCLUDE]
                Snackbar.make(
                    findViewById(android.R.id.content), "Quota exceeded.",
                    Snackbar.LENGTH_SHORT
                ).show()
                // [END_EXCLUDE]
            }

            // Show a message and update the UI
            // [START_EXCLUDE]
            // updateUI(STATE_VERIFY_FAILED)
            // [END_EXCLUDE]
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d("TAG", "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            resendToken = token

            // [START_EXCLUDE]
            // Update UI
            //  updateUI(STATE_CODE_SENT)
            // [END_EXCLUDE]
        }
    }
    // [END phone_auth_callbacks]


    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {

        progressBar.visibility = View.VISIBLE
        // [START verify_with_code]
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential)
    }

    // [START sign_in_with_phone]
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    progressBar.visibility = View.GONE
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")

                    val user = task.result?.user

                   // AppConstant.setPreferenceBoolean(AppConstant.IS_LOGIN, true)

                    //getDeviceDetailApi()

                    startActivity(Intent(this@Verify1Activity, Register1Activity::class.java))
                    finish()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    // [START_EXCLUDE]
                    //  updateUI(STATE_SIGNIN_SUCCESS, user)
                    // [END_EXCLUDE]
                } else {
                    progressBar.visibility = View.GONE
                    // Sign in failed, display a message and update the UI
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        // [START_EXCLUDE silent]
                        //fieldVerificationCode.error = "Invalid code."

                        Log.d("TAG", "Invalid--> " + "Invalid code.");
                        // [END_EXCLUDE]
                    }
                    // [START_EXCLUDE silent]
                    // Update UI
                    Log.d(
                        "TAG",
                        "onVerificationFailed" + "Somthing is wrong, we will fix it soon..."
                    );
                    // [END_EXCLUDE]
                }
            }
    }
    // [END sign_in_with_phone]


    // [START resend_verification]
    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?
    ) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber, // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            this, // Activity (for callback binding)
            callbacks, // OnVerificationStateChangedCallbacks
            token
        ) // ForceResendingToken from callbacks
    }
// [END resend_verification]

    private fun signOut() {
        auth.signOut()
        // updateUI(STATE_INITIALIZED)
    }
*/

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.text_resend -> {
                AppConstant.hideSoftKeyBoard(this@Verify1Activity,v)
                edt_otp_1.setText("")
                edt_otp_2.setText("")
                edt_otp_3.setText("")
                edt_otp_4.setText("")
                edt_otp_5.setText("")
                edt_otp_6.setText("")

                verificationApi(numbr)

             //resendVerificationCode(numbr, resendToken)
            }
            R.id.img_back -> {
                finish()

            }
            R.id.btn_verify -> {

                AppConstant.hideSoftKeyBoard(this@Verify1Activity,v)


                val stringBuilder = StringBuilder()
                stringBuilder.append(edt_otp_1.text)
                stringBuilder.append(edt_otp_2.text)
                stringBuilder.append(edt_otp_3.text)
                stringBuilder.append(edt_otp_4.text)
                stringBuilder.append(edt_otp_5.text)
                stringBuilder.append(edt_otp_6.text)

                if (stringBuilder.toString().length < 6 || !stringBuilder.toString().equals(otp)) {
                    Toast.makeText(this, getString(R.string.enter_valid_otp), Toast.LENGTH_SHORT)
                        .show()
                } else {
                    startActivity(Intent(this@Verify1Activity, Register1Activity::class.java))
                    finish()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)                }
            }
        }
    }

    fun verificationApi(num: String) {

        progressBar.visibility = View.VISIBLE
        val verification = Verification()
        verification.mobile = num

        val apiService =
            AppConstant.getClient()!!.create<RetrofitInterface>(RetrofitInterface::class.java)
        val call = apiService.verification(verification)

        call.enqueue(object : Callback<VerificationResponse> {
            override fun onResponse(
                call: Call<VerificationResponse>,
                response: retrofit2.Response<VerificationResponse>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {



                    otp = response.body()!!.otp.toString()
                    Log.e("HelloOTPS","Otp   "+otp)

                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(
                            this@Verify1Activity,
                            "" + jObjError.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()


                    } catch (e: Exception) {
                        Toast.makeText(this@Verify1Activity, e.message, Toast.LENGTH_LONG).show()
                    }

                }
            }


            override fun onFailure(call: Call<VerificationResponse>, t: Throwable) {
                // progressBarHandler.hide();
                progressBar.visibility = View.GONE
            }
        })
    }
}
