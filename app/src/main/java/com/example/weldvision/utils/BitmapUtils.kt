// File: app/src/main/java/com/example/weldvision/utils/BitmapUtils.kt
package com.example.weldvision.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix

/**
 * Decodes a bitmap from resources and scales it so that its width matches the target width,
 * while preserving its aspect ratio.
 *
 * @param res The resources.
 * @param resId The resource ID of the drawable.
 * @param targetWidth The desired width in pixels (e.g. device width).
 * @return A bitmap scaled to the target width with the aspect ratio preserved.
 */
fun decodeAndScaleBitmapPreserveAspectRatioFromResource(
    res: Resources,
    resId: Int,
    targetWidth: Int
): Bitmap {
    // First, decode with inJustDecodeBounds=true to get original dimensions.
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    }
    BitmapFactory.decodeResource(res, resId, options)

    val originalWidth = options.outWidth
    //height of the bitmap, could be used but not needed in my current use case.
    val originalHeight = options.outHeight

    // Calculate the scale factor to get the desired target width.
    val scaleFactor = targetWidth.toFloat() / originalWidth

    // Calculate an approximate inSampleSize (power of 2) to avoid decoding a huge bitmap.
    // We can use a simple ratio calculation here.
    var inSampleSize = 1
    if (originalWidth > targetWidth) {
        inSampleSize = (originalWidth / targetWidth).toInt()
        if (inSampleSize < 1) inSampleSize = 1
    }
    options.inSampleSize = inSampleSize
    options.inJustDecodeBounds = false

    // Decode the bitmap with the calculated inSampleSize.
    val decodedBitmap = BitmapFactory.decodeResource(res, resId, options)

    // Recalculate scale factor based on the decoded bitmap's width.
    val finalScaleFactor = targetWidth.toFloat() / decodedBitmap.width
    val targetHeight = (decodedBitmap.height * finalScaleFactor).toInt()

    // Scale the bitmap to exactly the target width while preserving aspect ratio.
    return Bitmap.createScaledBitmap(decodedBitmap, targetWidth, targetHeight, true)
}

/**
 * Rotates a given [bitmap] by the specified [degrees].
 *
 * @param bitmap The source bitmap.
 * @param degrees The rotation angle in degrees (e.g., 180f for a 180° rotation).
 * @return A new rotated bitmap.
 */
fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}
