package com.ps.omarmattr.qalby.model.solahTime


import com.google.gson.annotations.SerializedName

data class Offset(
    @SerializedName("Asr")
    var asr: Int,
    @SerializedName("Dhuhr")
    var dhuhr: Int,
    @SerializedName("Fajr")
    var fajr: Int,
    @SerializedName("Imsak")
    var imsak: Int,
    @SerializedName("Isha")
    var isha: Int,
    @SerializedName("Maghrib")
    var maghrib: Int,
    @SerializedName("Midnight")
    var midnight: Int,
    @SerializedName("Sunrise")
    var sunrise: Int,
    @SerializedName("Sunset")
    var sunset: Int
)