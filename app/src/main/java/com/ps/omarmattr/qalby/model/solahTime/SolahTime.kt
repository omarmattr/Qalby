package com.ps.omarmattr.qalby.model.solahTime


import com.google.gson.annotations.SerializedName

data class SolahTime(
    @SerializedName("code")
    var code: Int,
    @SerializedName("data")
    var `data`: List<Data>,
    @SerializedName("status")
    var status: String
)