package com.ps.omarmattr.qalby.model.solahTime


import com.google.gson.annotations.SerializedName
import com.ps.omarmattr.qalby.model.solahTime.Params

data class Method(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("params")
    var params: Params
)