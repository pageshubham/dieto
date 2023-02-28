package com.dieto.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.preference.PreferenceManager

class SplashActivity : AppCompatActivity() {

    private var mDelayHandler: Handler? = null
    private val sDelay: Long = 1000
    private var COMPLETED_ONBOARDING_PREF_NAME = "OnBoarding"

    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            PreferenceManager.getDefaultSharedPreferences(this).apply {
                // Check if we need to display our OnBoarding Fragment
                if (!getBoolean(COMPLETED_ONBOARDING_PREF_NAME, false)) {
                    // The user hasn't seen the OnBoarding Fragment yet, so show it
                    val intent = Intent(this@SplashActivity, WalkthroughActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, sDelay)

    }
    public override fun onDestroy() {
        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }
        super.onDestroy()
    }

    override fun onBackPressed() {
        finish()
    }
}