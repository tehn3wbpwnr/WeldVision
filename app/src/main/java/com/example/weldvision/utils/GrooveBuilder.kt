package com.example.weldvision.utils

import android.util.Log
import com.example.weldvision.model.Detection
import com.example.weldvision.model.GrooveSymbol
import com.example.weldvision.model.RecognizedText
import com.example.weldvision.interfaces.GrooveBuilder
import com.example.weldvision.model.WeldSymbolContext

class GrooveBuilderImpl(
    private val confidenceThreshold: Double = 0.7,
    // Add ocrContext as a property
    private val ocrContext: OCRContext = OCRContext()
) : GrooveBuilder {

    override fun buildGroove(
        detections: List<Detection>,
        recognizedTexts: List<RecognizedText>,
        context: WeldSymbolContext,
        imageWidth: Int,
        imageHeight: Int
    ): GrooveSymbol {
        // Step 1: For each side, select the best detection based on highest confidence.
        // For Arrow side:
        val arrowDetection = detections.filter { it.templateName.startsWith("Arrow") }
            .maxByOrNull { it.confidence }
        val arrowGroove = arrowDetection?.let { detection ->
            val candidate = detection.templateName.removePrefix("Arrow")
            val requiredThreshold = if (candidate in listOf("J", "U")) 0.71 else 0.71
            if (detection.confidence >= requiredThreshold) candidate else ""
        } ?: ""

        // For Other side:
        val otherDetection = detections.filter { it.templateName.startsWith("Other") }
            .maxByOrNull { it.confidence }
        val otherGroove = otherDetection?.let { detection ->
            val candidate = detection.templateName.removePrefix("Other")
            val requiredThreshold = if (candidate in listOf("J", "U")) 0.71 else 0.71
            if (detection.confidence >= requiredThreshold) candidate else ""
        } ?: ""

        // Step 2: Pull structured OCR values from context.
        val arrowAngle = context.arrowSide.angle.toIntOrNull()
        val otherAngle = context.otherSide.angle.toIntOrNull()

        // Step 3: Assign depth and root opening
        var arrowDepth: Double? = null
        var otherDepth: Double? = null
        var rootOpening: Double? = null

        // Step 4: Refine depth and root opening using OCR
        recognizedTexts.forEach { text ->
            val raw = text.text.trim()
            val box = text.boundingBox ?: return@forEach

            val value = ocrContext.parseNumericValue(raw)
            Log.d("GrooveDebug", "Parsed value: $value from text '$raw' at ${box.left},${box.top},${box.right},${box.bottom}")

            if (value != null) {
                val boxCenterY = (box.top + box.bottom) / 2
                val imageCenterY = imageHeight / 2

                when {
                    ocrContext.isLikelyRootOpening(box, imageWidth, imageHeight) && rootOpening == null -> {
                        rootOpening = value
                    }
                    ocrContext.isLikelyDepth(box, imageWidth, imageHeight) -> {
                        if (boxCenterY > imageCenterY && arrowDepth == null) {
                            arrowDepth = value
                        } else if (boxCenterY <= imageCenterY && otherDepth == null) {
                            otherDepth = value
                        }
                    }
                }
            }
        }

        // Step 5: Return the final GrooveSymbol
        return GrooveSymbol(
            arrowGroove = arrowGroove,
            otherGroove = otherGroove,
            arrowDepth = arrowDepth,
            otherDepth = otherDepth,
            rootOpening = rootOpening,
            arrowAngle = arrowAngle,
            otherAngle = otherAngle
        )
    }
}
