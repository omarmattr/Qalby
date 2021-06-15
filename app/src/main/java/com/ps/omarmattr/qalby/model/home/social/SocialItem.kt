package com.ps.omarmattr.qalby.model.home.social


import com.google.gson.annotations.SerializedName

data class SocialItem(
    @SerializedName("description")
    var description: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("imageLink")
    var imageLink: String,
    @SerializedName("imageUrl")
    var imageUrl: String,
    @SerializedName("source")
    var source: String
)