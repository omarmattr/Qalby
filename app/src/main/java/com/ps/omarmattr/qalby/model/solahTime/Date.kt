package com.ps.omarmattr.qalby.model.solahTime


import com.google.gson.annotations.SerializedName
import com.ps.omarmattr.qalby.model.solahTime.Gregorian
import com.ps.omarmattr.qalby.model.solahTime.Hijri

data class Date(
    @SerializedName("gregorian")
    var gregorian: Gregorian,
    @SerializedName("hijri")
    var hijri: Hijri,
    @SerializedName("readable")
    var readable: String,
    @SerializedName("timestamp")
    var timestamp: String
)