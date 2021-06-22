package com.daon.digitalpass.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.daon.digitalpass.R
import com.daon.digitalpass.databinding.ActivityActivateCredentialsBinding
import com.daon.digitalpass.network.NetworkApi
import com.daon.digitalpass.network.data.JwtResponse
import com.daon.digitalpass.util.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivateCredentialsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityActivateCredentialsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupUi()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }

    /*
     *====================================== Helper methods ========================================
     */

    private fun setupUi(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_activate_credentials)

        binding.activateCredentialsBackBtn.setOnClickListener { onBackPressed() }
        binding.activateUserCredentialsBtn.setOnClickListener { createCredential(Constants.USER_CREDENTIALS) }
        binding.activateReadyCredentialsBtn.setOnClickListener { createCredential(Constants.READY_CREDENTIALS) }
    }

    @SuppressLint("ApplySharedPref")
    private fun createCredential(path:String){
        NetworkApi.retrofitService.createCredential(path).enqueue(object: Callback<JwtResponse> {
            override fun onResponse(call: Call<JwtResponse>, response: Response<JwtResponse>) {
                if (response.isSuccessful && response.body() != null){
                    val token: JwtResponse = response.body() as JwtResponse
                    val sharedPref = getSharedPreferences(Constants.SHARED_PREF_DEFAULT, MODE_PRIVATE)
                    when {
                        token.type.equals(Constants.USER_CREDENTIALS, ignoreCase = true) -> {
                            val editor = sharedPref.edit()
                            editor.putString(Constants.USER_JWT_TOKEN, token.jwt)
                            editor.commit()
                            Toast.makeText(this@ActivateCredentialsActivity,
                                getString(R.string.user_credential_updated),
                                Toast.LENGTH_SHORT).show()
                            onBackPressed()
                        }
                        token.type.equals(Constants.READY_CREDENTIALS, ignoreCase = true) -> {
                            val editor = sharedPref.edit()
                            editor.putString(Constants.READY_JWT_TOKEN, token.jwt)
                            editor.commit()
                            Toast.makeText(this@ActivateCredentialsActivity,
                                getString(R.string.ready_credential_updated),
                                Toast.LENGTH_SHORT).show()
                            onBackPressed()
                        }
                        else -> {
                            Toast.makeText(
                                this@ActivateCredentialsActivity,
                                getString(R.string.unknown_credential_received),
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this@ActivateCredentialsActivity,
                        getString(R.string.error_creating_credentials),
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JwtResponse>, t: Throwable) {
                Toast.makeText(this@ActivateCredentialsActivity,
                    getString(R.string.network_connection_issue),
                    Toast.LENGTH_SHORT).show()
            }

        })
    }

}