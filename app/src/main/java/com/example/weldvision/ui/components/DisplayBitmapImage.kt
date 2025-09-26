// File: app/src/main/java/com/example/weldvision/ui/components/DisplayBitmapImage.kt
package com.example.weldvision.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap

@Composable
fun DisplayBitmapImage(bitmap: Bitmap, modifier: Modifier = Modifier) {
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = "Displayed Bitmap",
        modifier = modifier
    )
}
