package com.daon.digitalpass.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

object DataManager {
    var user:User? = null
        set(value) {
            field = value
        }

    var passes:List<Pass>? = null
        set(value) {
        field = value
        }
}


//data class User(
//    val firstName: String,
//    val image: String,
//    val lastName: String)


//@Parcelize
//data class Pass(
//    val id: String,
//    val description: String,
//    val icon: String,
//    val name: String):Parcelable