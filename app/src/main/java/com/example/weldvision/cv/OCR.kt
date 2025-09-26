// File: app/src/main/java/com/example/weldvision/OCR.kt
package com.example.weldvision.cv

import android.graphics.Rect
import android.util.Log
import com.example.weldvision.interfaces.OCRProcessor
import com.example.weldvision.model.RecognizedText
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.tasks.await

class OCR : OCRProcessor {
    /**
     * Processes the given [inputImage] using ML Kit's text recognition and returns a list of recognized text with bounding boxes.
     */
    override suspend fun process(inputImage: InputImage): List<RecognizedText> {
        return try {
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            val result = recognizer.process(inputImage).await()
            val recognizedList = mutableListOf<RecognizedText>()
            for (block in result.textBlocks) {
                for (line in block.lines) {
                    recognizedList.add(RecognizedText(line.text, line.boundingBox ?: Rect()))
                }
            }
            recognizedList
        } catch (e: Exception) {
            Log.e("OCR", "Error during OCR processing", e)
            emptyList()
        }
    }
}
