package com.ps.omarmattr.qalby.other

import android.widget.ImageView
import androidx.databinding.BindingAdapter

object HolderAdapter {




    @JvmStatic
    @BindingAdapter("loadImageRec")
    fun loadImage(image:ImageView,img:Int){
        image.setImageResource(img)
    }



}