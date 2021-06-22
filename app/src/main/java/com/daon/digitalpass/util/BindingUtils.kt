package com.daon.digitalpass.util

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.daon.digitalpass.R
import com.daon.digitalpass.data.Pass
import com.daon.digitalpass.data.User

/*
 *======================================= Pass Adapters ============================================
 */
@BindingAdapter("setPassIcon")
fun ImageView.setPassIcon(item: Pass?){
    item?.let {
        val icon: Bitmap? = decodeBitmap(item.icon)
        if(icon == null){
            setImageResource(R.drawable.ic_broken_image)
        }else{
            setImageBitmap(icon)
        }
    }
}

@BindingAdapter("setPassTitle")
fun TextView.setPassTitle(item: Pass?) {
    item?.let {
        text = item.name
    }
}

@BindingAdapter("setPassDescription")
fun TextView.setPassDescription(item: Pass?) {
    item?.let {
        text = item.description
    }
}

/*
 *======================================= User Adapters ============================================
 */

@BindingAdapter("setUserImage")
fun ImageView.setUserImage(item: User?){
    item?.let {
        val icon: Bitmap? = decodeBitmap(item.image)
        if(icon == null){
            setImageResource(R.drawable.ic_broken_image)
        }else{
            setImageBitmap(icon)
        }
    }
}

@BindingAdapter("setUserFullName")
fun TextView.setUserFullName(item: User?) {
    item?.let {
        val fullName: String = item.firstName + " " + item.lastName
        text = fullName
    }
}