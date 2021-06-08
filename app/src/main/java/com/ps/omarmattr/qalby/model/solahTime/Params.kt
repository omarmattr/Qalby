package com.ps.omarmattr.qalby.model.solahTime


import com.google.gson.annotations.SerializedName

data class Params(
    @SerializedName("Fajr")
    var fajr: Double,
    @SerializedName("Isha")
    var isha: String
)