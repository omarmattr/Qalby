package com.ps.omarmattr.qalby.model.solahTime


import com.google.gson.annotations.SerializedName
import com.ps.omarmattr.qalby.model.solahTime.Method
import com.ps.omarmattr.qalby.model.solahTime.Offset

data class Meta(
    @SerializedName("latitude")
    var latitude: Double,
    @SerializedName("latitudeAdjustmentMethod")
    var latitudeAdjustmentMethod: String,
    @SerializedName("longitude")
    var longitude: Double,
    @SerializedName("method")
    var method: Method,
    @SerializedName("midnightMode")
    var midnightMode: String,
    @SerializedName("offset")
    var offset: Offset,
    @SerializedName("school")
    var school: String,
    @SerializedName("timezone")
    var timezone: String
)