package com.ps.omarmattr.qalby.model.solahTime


import com.google.gson.annotations.SerializedName

data class Month(
    @SerializedName("en")
    var en: String,
    @SerializedName("number")
    var number: Int
)