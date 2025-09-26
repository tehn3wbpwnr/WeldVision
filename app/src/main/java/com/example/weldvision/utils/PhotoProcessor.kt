package com.example.weldvision.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

// Example pre-processing function that applies your custom logic.
fun processBitmap(original: Bitmap): Bitmap {
    // TODO: Replace this with your actual processing logic.
    // For example, convert to grayscale or crop the image.
    Log.d("CameraPreview", "Processing image with dimensions: ${original.width}x${original.height}")
    return original
}

@OptIn(DelicateCoroutinesApi::class)
fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    onImageCaptured: (Uri) -> Unit
) {
    val photoFile = File(
        context.externalCacheDir,
        "photo_${System.currentTimeMillis()}.jpg"
    )
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                // Now that the photo is saved, process it.
                // Using a coroutine to do heavy processing off the main thread.
                kotlinx.coroutines.GlobalScope.launch(Dispatchers.IO) {
                    // Decode the file into a Bitmap
                    val originalBitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                    // Process the image using your pre-processing function
                    val processedBitmap = processBitmap(originalBitmap)

                    // (Optional) Save the processed bitmap back to a file
                    val processedFile = File(
                        context.externalCacheDir,
                        "processed_${System.currentTimeMillis()}.jpg"
                    )
                    FileOutputStream(processedFile).use { fos ->
                        processedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
                    }

                    // Add processed image to gallery if needed, or update UI.
                    // Here we convert it to a Uri
                    val savedUri = Uri.fromFile(processedFile)

                    // Switch back to the main thread to update UI via the callback
                    withContext(Dispatchers.Main) {
                        onImageCaptured(savedUri)
                    }
                }
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("CameraPreview", "Photo capture failed: ${exception.message}", exception)
            }
        }
    )
}