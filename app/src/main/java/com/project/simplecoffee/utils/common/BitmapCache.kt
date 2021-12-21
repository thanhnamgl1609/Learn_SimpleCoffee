package com.project.simplecoffee.utils.common

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import android.widget.ImageView
import com.project.simplecoffee.R
import kotlinx.coroutines.*
import java.io.BufferedInputStream
import java.lang.Exception
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
    fun loadBitmap(url: String, imageView: ImageView): Bitmap? {
        return try {
            val bitmap = URL(url).openStream()?.run { BitmapFactory.decodeStream(this) }
            bitmap?.run { memoryCache.put(url, bitmap) }
            bitmap
        } catch (e: Exception) {
            null
        }
    }

    @DelicateCoroutinesApi
    fun setBitMap(imageView: ImageView, url: String?) = GlobalScope.launch(Dispatchers.IO) {
        url?.let {
            var bitmap = memoryCache.get(it)
            if (bitmap == null) {
                bitmap = loadBitmap(url, imageView)
            }
            bitmap?.run {
                withContext(Dispatchers.Main) {
                    imageView.setImageBitmap(this@run)
                }
            }
        }
    }
}