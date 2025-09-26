package com.example.weldvision.model

import org.opencv.core.Rect

data class Detection(
    val rect: Rect,
    val templateName: String,
    val confidence: Double,
    val scale: Double = 1.0
)