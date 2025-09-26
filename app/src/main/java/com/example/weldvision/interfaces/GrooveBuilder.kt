package com.example.weldvision.interfaces

import com.example.weldvision.model.Detection
import com.example.weldvision.model.RecognizedText
import com.example.weldvision.model.GrooveSymbol
import com.example.weldvision.model.WeldSymbolContext

interface GrooveBuilder {
    fun buildGroove(
        detections: List<Detection>,
        recognizedTexts: List<RecognizedText>,
        context: WeldSymbolContext,
        imageWidth: Int,
        imageHeight: Int
    ): GrooveSymbol
}
