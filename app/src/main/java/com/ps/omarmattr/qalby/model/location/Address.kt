package com.ps.omarmattr.qalby.model.location


import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("city")
    var city: String,
    @SerializedName("country")
    var country: String,
    @SerializedName("country_code")
    var countryCode: String,
    @SerializedName("county")
    var county: String,
    @SerializedName("postcode")
    var postcode: String,
    @SerializedName("region")
    var region: String,
    @SerializedName("suburb")
    var suburb: String,
    @SerializedName("village")
    var village: String
)