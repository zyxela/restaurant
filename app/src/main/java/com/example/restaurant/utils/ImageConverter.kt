package com.example.restaurant.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun ByteArray.toBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, this.size)
}
