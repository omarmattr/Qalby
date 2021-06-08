package com.ps.omarmattr.qalby.model.solahTime


import com.google.gson.annotations.SerializedName

data class Designation(
    @SerializedName("abbreviated")
    var abbreviated: String,
    @SerializedName("expanded")
    var expanded: String
)