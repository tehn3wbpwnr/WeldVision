// File: app/src/main/java/com/example/weldvision/utils/RecognizedText.kt
package com.example.weldvision.model

import android.graphics.Rect

data class RecognizedText(
    val text: String,
    val boundingBox: Rect
)
