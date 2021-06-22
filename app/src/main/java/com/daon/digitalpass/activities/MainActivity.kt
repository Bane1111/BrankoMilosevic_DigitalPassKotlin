package com.daon.digitalpass.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.daon.digitalpass.R
import com.daon.digitalpass.data.DataManager
import com.daon.digitalpass.data.Pass
import com.daon.digitalpass.data.User
import com.daon.digitalpass.databinding.ActivityMainBinding
import com.daon.digitalpass.network.NetworkApi
import com.daon.digitalpass.network.data.PassJsonResponse
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
    }

    /*
     *====================================== Helper methods ========================================
     */

    private fun setupUi(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.createAccountBtn.setOnClickListener { createAccount() }
    }

    private fun createAccount() {
        NetworkApi.retrofitService.createAccount().enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful && response.body() != null){
                    val jsonObject = JSONObject(response.body()!!.string())
                    var user: User? = null
                    val userPassList = ArrayList<Pass>()

                    val keys = jsonObject.keys()
                    while (keys.hasNext()) {
                        val key = keys.next()
                        if (jsonObject[key] is JSONObject) {
                            if (key.equals("user", ignoreCase = true)) {
                                user = Gson().fromJson(jsonObject[key].toString(), User::class.java)
                            } else if (key.startsWith("pass")) {
                                val passJsonResponse: PassJsonResponse = Gson().fromJson(
                                    jsonObject[key].toString(), PassJsonResponse::class.java
                                )
                                val pass = Pass(
                                    key, passJsonResponse.description, passJsonResponse.icon,
                                    passJsonResponse.name
                                )
                                userPassList.add(pass)
                            }
                        }
                    }

                    if (user != null && userPassList.size > 0) {
                        DataManager.user = user
                        DataManager.passes = userPassList
                        val intent = Intent(this@MainActivity, PassListActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_left, R.anim.no_change)
                    } else {
                        Toast.makeText(this@MainActivity,
                            getString(R.string.missing_data),
                            Toast.LENGTH_SHORT).show()
                    }

                }else{
                    Toast.makeText(this@MainActivity,
                        getString(R.string.error_receiving_data),
                        Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@MainActivity,
                    getString(R.string.network_connection_issue),
                    Toast.LENGTH_SHORT).show()
            }

        })

    }

}