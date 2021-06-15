package com.ps.omarmattr.qalby.model.solahTime

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SolahItem(val image:Int=0,val name:String,val time:String,val state:Boolean):Parcelable
