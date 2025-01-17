package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class Division(
    val id: String = "",
    val country: String = "",
    val state: String = ""
) : Parcelable {
    val address: String
        get() = "$state, $country"
}