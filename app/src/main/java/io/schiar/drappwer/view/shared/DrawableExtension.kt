package io.schiar.drappwer.view.shared

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


fun Drawable.toByteArray(): ByteArray {
    val bitmap = this.toBitmap()
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    return stream.toByteArray()
}

fun ByteArray.toBitmap(): Bitmap {
    val byteArrayInputStream = ByteArrayInputStream(this)
    return BitmapFactory.decodeStream(byteArrayInputStream)
}