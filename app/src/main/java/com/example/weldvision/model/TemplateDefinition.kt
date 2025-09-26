package com.example.weldvision.model

import org.opencv.core.Mat

data class TemplateDefinition(
    val name: String,
    val mat: Mat,
    val threshold: Double = 0.6 //this sets the default threshold to use
)