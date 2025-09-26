package com.example.weldvision.interfaces

import org.opencv.core.Mat

interface ImagePreProcessor {
    fun preProcess(image: Mat): Mat
}
