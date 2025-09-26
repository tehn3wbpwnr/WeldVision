package com.example.weldvision.interfaces

import com.google.mlkit.vision.common.InputImage
import com.example.weldvision.model.RecognizedText

interface OCRProcessor {
    suspend fun process(inputImage: InputImage): List<RecognizedText>
}
