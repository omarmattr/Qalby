package com.ps.omarmattr.qalby.model.dua


import com.google.gson.annotations.SerializedName

data class DuaRequestItem(
    @SerializedName("ayat")
    var ayat: String,
    @SerializedName("femaleAudioUrl")
    var femaleAudioUrl: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("isHifz")
    var isHifz: Boolean,
    @SerializedName("isSaved")
    var isSaved: Boolean,
    @SerializedName("maleAudioUrl")
    var maleAudioUrl: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("reference")
    var reference: String,
    @SerializedName("source")
    var source: String,
    @SerializedName("translation")
    var translation: String
)