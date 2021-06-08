package com.ps.omarmattr.qalby.model.location


import com.google.gson.annotations.SerializedName

data class ResultLocation(
    @SerializedName("address")
    var address: Address,
    @SerializedName("boundingbox")
    var boundingbox: List<String>,
    @SerializedName("display_name")
    var displayName: String,
    @SerializedName("lat")
    var lat: String,
    @SerializedName("licence")
    var licence: String,
    @SerializedName("lon")
    var lon: String,
    @SerializedName("osm_id")
    var osmId: Int,
    @SerializedName("osm_type")
    var osmType: String,
    @SerializedName("place_id")
    var placeId: Int
)