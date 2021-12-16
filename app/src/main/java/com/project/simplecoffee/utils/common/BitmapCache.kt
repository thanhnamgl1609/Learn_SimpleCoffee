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
    fun loadBitmap(url: String, imageView: ImageView): Bitmap {
        val bitmap = BitmapFactory.decodeStream(URL(url).openStream())
        memoryCache.put(url, bitmap)
        return bitmap
    }

    @DelicateCoroutinesApi
    fun setBitMap(imageView: ImageView, url: String?) = GlobalScope.launch(Dispatchers.IO) {
        url?.let {
            var bitmap = memoryCache.get(it)
            if (bitmap == null) {
                bitmap = loadBitmap(url, imageView)
            }
            withContext(Dispatchers.Main) {
                imageView.setImageBitmap(bitmap)
            }
        }
    }
}