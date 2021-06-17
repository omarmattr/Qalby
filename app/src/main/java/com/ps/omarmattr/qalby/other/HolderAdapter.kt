package com.ps.omarmattr.qalby.other

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions

object HolderAdapter {




    @JvmStatic
    @BindingAdapter("loadImageRec")
    fun loadImage(image:ImageView,img:Int){
        image.setImageResource(img)
    }
    @JvmStatic
    @BindingAdapter("uriImage")
    fun uriImage(image: ImageView, img: String?) {
        Glide
            .with(image.context)
            .load(img)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .apply(RequestOptions().transform(CenterCrop()))
            .into(image)
    }



}