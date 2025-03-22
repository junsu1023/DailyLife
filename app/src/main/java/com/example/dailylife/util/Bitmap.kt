package com.example.dailylife.util

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap

fun convertDrawableToBitMap(
    context: Context,
    @DrawableRes resId: Int
): Bitmap? = ContextCompat.getDrawable(context, resId)?.toBitmap()