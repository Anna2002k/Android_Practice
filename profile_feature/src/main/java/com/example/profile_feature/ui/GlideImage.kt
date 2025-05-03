package com.example.profile_feature.ui

import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@Composable
fun GlideImage(url: String, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            ImageView(ctx).apply {
                Glide.with(ctx)
                    .load(url)
                    .apply(RequestOptions().centerInside())
                    .into(this)
            }
        }
    )
}