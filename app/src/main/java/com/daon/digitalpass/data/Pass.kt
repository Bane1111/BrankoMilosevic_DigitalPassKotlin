package com.daon.digitalpass.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pass(
    val id: String,
    val description: String,
    val icon: String,
    val name: String): Parcelable