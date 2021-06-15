package com.ps.omarmattr.qalby.model.home.tv


import com.google.gson.annotations.SerializedName

data class TVItem(
    @SerializedName("id")
    var id: Int,
    @SerializedName("thumbnailUrl")
    var thumbnailUrl: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("videoId")
    var videoId: String,
    @SerializedName("videoUrl")
    var videoUrl: String
)