package com.ps.omarmattr.qalby.model.solahTime


import com.google.gson.annotations.SerializedName

data class Hijri(
    @SerializedName("date")
    var date: String,
    @SerializedName("day")
    var day: String,
    @SerializedName("designation")
    var designation: DesignationX,
    @SerializedName("format")
    var format: String,
    @SerializedName("holidays")
    var holidays: List<Any>,
    @SerializedName("month")
    var month: MonthX,
    @SerializedName("weekday")
    var weekday: WeekdayX,
    @SerializedName("year")
    var year: String
)