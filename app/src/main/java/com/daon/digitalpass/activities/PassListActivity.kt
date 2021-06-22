package com.daon.digitalpass.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.daon.digitalpass.R
import com.daon.digitalpass.adapters.PassListAdapter
import com.daon.digitalpass.adapters.PassListListener
import com.daon.digitalpass.data.DataManager
import com.daon.digitalpass.data.Pass
import com.daon.digitalpass.databinding.ActivityPassListBinding
import com.daon.digitalpass.util.Constants

class PassListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPassListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_pass_list)
        setupData()

    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }

    /*
     *======================================= Helper methods =======================================
     */

    private fun setupData(){
        if(DataManager.passes != null){
            val adapter = PassListAdapter(DataManager.passes!!, PassListListener {
                openFlashPassScreen(it)
            })
            binding.passListRv.adapter = adapter
        }
    }

    private fun openFlashPassScreen(pass: Pass){
        val intent = Intent(this, FlashPassActivity::class.java)
        intent.putExtra(Constants.SELECTED_PASS, pass)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.no_change)
    }

}

