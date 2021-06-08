package com.ps.omarmattr.qalby.model.solahTime


import com.google.gson.annotations.SerializedName

data class DesignationX(
    @SerializedName("abbreviated")
    var abbreviated: String,
    @SerializedName("expanded")
    var expanded: String
)