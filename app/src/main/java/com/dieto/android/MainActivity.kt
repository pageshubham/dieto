package com.dieto.android

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity(), PaymentResultListener {

    private val TAG = "MainActivity"

    private var mPlan = ""
    private var VATA_PLAN  = "VATA_PLAN"
    private var PITTA_PLAN  = "PITTA_PLAN"
    private var KAPHA_PLAN  = "KAPHA_PLAN"
    private var GYM_PLAN  = "GYM_PLAN"

    lateinit var bottom_sheet_dialog: BottomSheetDialog
    private lateinit var bottom_sheet_back: LinearLayout
    private lateinit var bottom_sheet_phone: EditText
    private lateinit var bottom_sheet_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_sheet_dialog = BottomSheetDialog(this)
        bottom_sheet_dialog.setContentView(R.layout.bottom_sheet_dialog)
        bottom_sheet_back = bottom_sheet_dialog.findViewById(R.id.bottom_sheet_back_icon)!!
        bottom_sheet_phone = bottom_sheet_dialog.findViewById(R.id.bottom_sheet_phone_input)!!
        bottom_sheet_button = bottom_sheet_dialog.findViewById(R.id.bottom_sheet_action_button)!!

        PreferenceManager.getDefaultSharedPreferences(this).apply {
            if (!getBoolean(VATA_PLAN, false)) {
                home_vata_plan_button.text = "Buy Now ₹299/-"
            } else {
                home_vata_plan_button.text = "View Plan"
            }

            if (!getBoolean(PITTA_PLAN, false)) {
                home_pitta_plan_button.text = "Buy Now ₹299/-"
            } else {
                home_pitta_plan_button.text = "View Plan"
            }

            if (!getBoolean(KAPHA_PLAN, false)) {
                home_kapha_plan_button.text = "Buy Now ₹299/-"
            } else {
                home_kapha_plan_button.text = "View Plan"
            }

            if (!getBoolean(GYM_PLAN, false)) {
                home_gym_plan_button.text = "Buy Now ₹299/-"
            } else {
                home_gym_plan_button.text = "View Plan"
            }
        }

        home_setting_layout.setOnClickListener {
            toast("in-update")
        }

        home_vata_plan_button.setOnClickListener {
            if (isConnected()) {
                if (home_vata_plan_button.text == "Buy Now ₹299/-") {
                    bottom_sheet_dialog.show()
                    mPlan = "VATA_PLAN"
                } else {
                    val intent = Intent(this@MainActivity, ViewPlanActivity::class.java)
                    intent.putExtra("plan", "vata")
                    startActivity(intent)
                }
            } else {
                toast("No Internet Connection")
            }
        }

        home_pitta_plan_button.setOnClickListener {
            if (isConnected()) {
                if (home_pitta_plan_button.text == "Buy Now ₹299/-") {
                    bottom_sheet_dialog.show()
                    mPlan = "PITTA_PLAN"
                } else {
                    val intent = Intent(this@MainActivity, ViewPlanActivity::class.java)
                    intent.putExtra("plan", "pitta")
                    startActivity(intent)
                }
            } else {
                toast("No Internet Connection")
            }
        }

        home_kapha_plan_button.setOnClickListener {
            if (isConnected()) {
                if (home_kapha_plan_button.text == "Buy Now ₹299/-") {
                    bottom_sheet_dialog.show()
                    mPlan = "KAPHA_PLAN"
                } else {
                    val intent = Intent(this@MainActivity, ViewPlanActivity::class.java)
                    intent.putExtra("plan", "kapha")
                    startActivity(intent)
                }
            } else {
                toast("No Internet Connection")
            }
        }

        home_gym_plan_button.setOnClickListener {
            if (isConnected()) {
                if (home_gym_plan_button.text == "Buy Now ₹299/-") {
                    bottom_sheet_dialog.show()
                    mPlan = "GYM_PLAN"
                } else {
                    val intent = Intent(this@MainActivity, ViewPlanActivity::class.java)
                    intent.putExtra("plan", "gym")
                    startActivity(intent)
                }
            } else {
                toast("No Internet Connection")
            }
        }

        bottom_sheet_back.setOnClickListener {
            bottom_sheet_dialog.dismiss()
        }

        bottom_sheet_button.setOnClickListener {
            if (isConnected()) {
                confirmDetails()
            } else {
                toast("No Internet Connection")
            }
        }

    }//onCreate()

    private fun confirmDetails() {
        if (!validatePhone()) {
            return
        } else {
            val phoneInput = bottom_sheet_phone.text.toString().trim()
            bottom_sheet_dialog.dismiss()
            paymentGateway(phoneInput)
        }
    }

    private fun validatePhone(): Boolean {
        val phoneInput = bottom_sheet_phone.text.toString().trim()
        return when {
            phoneInput.isEmpty() -> {
                toast("Field can't be empty")
                false
            }
            phoneInput.length != 10 -> {
                toast("Enter 10 digit number")
                false
            }
            else -> {
                true
            }
        }
    }

    private fun paymentGateway(phone: String) {
        val activity: Activity = this
        val co = Checkout()
        co.setFullScreenDisable(false)

        try {
            val options = JSONObject()
            options.put("name","Dieto Payment")
            options.put("description","Unlock Plan")
            options.put("currency","INR")
            options.put("amount", 299*100)
            options.put("send_sms_hash",true);

            val prefill = JSONObject()
            prefill.put("email", "$phone@gmail.com")
            prefill.put("contact", phone)

            options.put("prefill", prefill)
            co.open(activity, options)
        } catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        try {
            onTransactionSuccess()
            bottom_sheet_phone.text.clear()
            toast("Payment Successful")
        } catch (e: Exception){
            Log.e(TAG,"Exception in onPaymentSuccess", e)
        }
    }

    override fun onPaymentError(errorCode: Int, response: String?) {
        try {
            toast("Payment Failed")
        } catch (e: Exception){
            Log.e(TAG,"Exception in onPaymentSuccess", e)
        }
    }

    private fun onTransactionSuccess() {
        when (mPlan) {
            "VATA_PLAN" -> {
                PreferenceManager.getDefaultSharedPreferences(this).edit().apply {
                    putBoolean(VATA_PLAN, true)
                    apply()
                    home_vata_plan_button.text = "View Plan"
                }
            }
            "PITTA_PLAN" -> {
                PreferenceManager.getDefaultSharedPreferences(this).edit().apply {
                    putBoolean(PITTA_PLAN, true)
                    apply()
                    home_pitta_plan_button.text = "View Plan"
                }
            }
            "KAPHA_PLAN" -> {
                PreferenceManager.getDefaultSharedPreferences(this).edit().apply {
                    putBoolean(KAPHA_PLAN, true)
                    apply()
                    home_kapha_plan_button.text = "View Plan"
                }
            }
            "GYM_PLAN" -> {
                PreferenceManager.getDefaultSharedPreferences(this).edit().apply {
                    putBoolean(GYM_PLAN, true)
                    apply()
                    home_gym_plan_button.text = "View Plan"
                }
            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifiConn = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val mobileConn = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        return wifiConn != null && wifiConn.isConnected || mobileConn != null && mobileConn.isConnected
    }

}