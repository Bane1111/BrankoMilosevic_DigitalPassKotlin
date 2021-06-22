package com.daon.digitalpass.activities

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.auth0.android.jwt.JWT
import com.daon.digitalpass.R
import com.daon.digitalpass.data.DataManager
import com.daon.digitalpass.data.Pass
import com.daon.digitalpass.data.User
import com.daon.digitalpass.databinding.ActivityFlashPassBinding
import com.daon.digitalpass.util.Constants
import com.daon.digitalpass.util.decodeBitmap
import com.daon.digitalpass.util.formatDate

class FlashPassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFlashPassBinding

    //Variables for periodic 30sec token check
    private var mPeriodicTokenCheckHandler: Handler = Handler(Looper.getMainLooper())
    private var mPeriodicTokenCheckRunnable: Runnable = object : Runnable {
        override fun run() {
            checkTokens()
            mPeriodicTokenCheckHandler.postDelayed(this, 30000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupUi()
        setupPassInfoData()
        setupUserData()
    }

    override fun onResume() {
        super.onResume()
        mPeriodicTokenCheckHandler.post(mPeriodicTokenCheckRunnable)
    }

    override fun onStop() {
        super.onStop()
        mPeriodicTokenCheckHandler.removeCallbacks(mPeriodicTokenCheckRunnable)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }

    /*
     *======================================= Helper methods =======================================
     */

    private fun setupUi(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_flash_pass)
        binding.flashPassBackBtn.setOnClickListener { onBackPressed() }
        binding.flashPassUpdateCredentialsBtn.setOnClickListener { openActivateCredentialsScreen() }
    }

    private fun setupPassInfoData(){
        val pass: Pass? = intent.getParcelableExtra(Constants.SELECTED_PASS)
        pass?.let {
            binding.pass = pass
        }
    }

    private fun setupUserData(){
        val user:User? = DataManager.user
        user?.let {
            binding.user = user
        }
    }

    private fun openActivateCredentialsScreen(){
        val intent = Intent(this, ActivateCredentialsActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.no_change)
    }

    private fun checkTokens() {
        val sharedPref = getSharedPreferences(Constants.SHARED_PREF_DEFAULT, MODE_PRIVATE)
        val userJwt = sharedPref.getString(Constants.USER_JWT_TOKEN, "")
        val readyJwt = sharedPref.getString(Constants.READY_JWT_TOKEN, "")
        var isReadyJwtValid = false
        var isUserJwtValid = false

        if (userJwt!!.isEmpty()) {
            binding.flashPassUserExpirationTimestamp.text = resources.getString(R.string.unavailable_label)
        } else {
            val jwt = JWT(userJwt)
            binding.flashPassUserExpirationTimestamp.text = formatDate(jwt.getExpiresAt())
            isUserJwtValid = !jwt.isExpired(10)
        }

        if (readyJwt!!.isEmpty()) {
            binding.flashPassReadyExpirationTimestamp.text = resources.getString(R.string.unavailable_label)
        } else {
            val jwt = JWT(readyJwt)
            binding.flashPassReadyExpirationTimestamp.text = formatDate(jwt.getExpiresAt())
            isReadyJwtValid = !jwt.isExpired(10)
        }

        if (isUserJwtValid && isReadyJwtValid) {
            binding.flashPassUserDataContainer.background = ResourcesCompat.getDrawable(
                resources, R.drawable.background_frame_pass_valid, theme)
            binding.flashPassValidityIcon.setImageDrawable(ResourcesCompat.getDrawable(resources,
                R.drawable.ic_pass_valid, theme))
        } else {
            binding.flashPassUserDataContainer.background = ResourcesCompat.getDrawable(
                resources, R.drawable.background_frame_pass_invalid, theme)
            binding.flashPassValidityIcon.setImageDrawable(ResourcesCompat.getDrawable(resources,
                R.drawable.ic_pass_invalid, theme))
        }
    }
}