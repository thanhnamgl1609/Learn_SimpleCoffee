package com.project.simplecoffee.utils.common

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import android.widget.ImageView
import com.project.simplecoffee.R
import kotlinx.coroutines.*
import java.io.BufferedInputStream
import java.net.URL

object BitmapCache {
    private val memoryCache =
        object : LruCache<String, Bitmap>((Runtime.getRuntime().maxMemory() / 1024).toInt()) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.byteCount / 1024
            }
        }

    @DelicateCoroutinesApi
    suspend fun loadBitmap(url: String, imageView: ImageView) {
        val bitmap = GlobalScope.async(Dispatchers.IO) {
            BitmapFactory.decodeStream(URL(url).openStream())
        }
        memoryCache.put(url, bitmap.await())
        imageView.setImageBitmap(bitmap.await())
    }

    @DelicateCoroutinesApi
    fun setBitMap(imageView: ImageView, url: String?) {
        url?.let {
            memoryCache.get(it)?.also { bitmap ->
                imageView.setImageBitmap(bitmap)
            } ?: run {
                GlobalScope.launch(Dispatchers.Main) { loadBitmap(url, imageView) }

                null
            }
        }
    }
}