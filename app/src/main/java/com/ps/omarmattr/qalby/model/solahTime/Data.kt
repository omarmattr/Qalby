package com.ps.omarmattr.qalby.model.solahTime


import com.google.gson.annotations.SerializedName
import com.ps.omarmattr.qalby.model.solahTime.Date
import com.ps.omarmattr.qalby.model.solahTime.Meta
import com.ps.omarmattr.qalby.model.solahTime.Timings

data class Data(
    @SerializedName("date")
    var date: Date,
    @SerializedName("meta")
    var meta: Meta,
    @SerializedName("timings")
    var timings: Timings
)