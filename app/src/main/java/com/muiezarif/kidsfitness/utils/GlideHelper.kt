package com.muiezarif.kidsfitness.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

object GlideHelper {
    /**
     * Loads thumbnail.
     */
    fun loadThumb(image: ImageView?, thumbId: Int) {
        // We don't want Glide to crop or resize our image
        val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .override(Target.SIZE_ORIGINAL)
            .dontTransform()
        Glide.with(image!!)
            .load(thumbId)
            .apply(options)
            .into(image)
    }

    /**
     * Loads thumbnail and then replaces it with full image.
     */
    fun loadFull(
        image: ImageView?,
        imageUrl: String?,
        errorPlaceHolder: Int
    ) {
        val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
            .dontTransform()
        Glide.with(image!!)
            .load(imageUrl)
            .placeholder(errorPlaceHolder)
            .error(errorPlaceHolder)
            .apply(options)
            .into(image)
    }

    fun clear(view: ImageView) {
        // Clearing current Glide request (if any)
        Glide.with(view).clear(view)
        // Cleaning up resources
        view.setImageDrawable(null)
    }
}