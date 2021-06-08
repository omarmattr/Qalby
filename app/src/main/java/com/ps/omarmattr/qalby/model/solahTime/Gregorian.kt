package com.ps.omarmattr.qalby.model.solahTime


import com.google.gson.annotations.SerializedName

data class Gregorian(
    @SerializedName("date")
    var date: String,
    @SerializedName("day")
    var day: String,
    @SerializedName("designation")
    var designation: Designation,
    @SerializedName("format")
    var format: String,
    @SerializedName("month")
    var month: Month,
    @SerializedName("weekday")
    var weekday: Weekday,
    @SerializedName("year")
    var year: String
)