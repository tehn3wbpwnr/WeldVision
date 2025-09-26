package com.example.weldvision.utils

import android.graphics.Rect
import android.util.Log
import com.example.weldvision.model.RecognizedText
import com.example.weldvision.model.WeldSideData
import com.example.weldvision.model.WeldSymbolContext

class OCRContext {


    /*
    * Function : extractWeldSymbolContext
    * Parameters : arrowTexts : List<RecognizedText>, otherTexts : List<RecognizedText>
    * Return : WeldSymbolContext
    * Description : This function is used to extract the weld symbol context from the OCR detected texts.
     */
    fun extractWeldSymbolContext(
        arrowTexts: List<RecognizedText>,
        otherTexts: List<RecognizedText>
    ): WeldSymbolContext {
        val arrowData = parseSideData(arrowTexts)
        val otherData = parseSideData(otherTexts)

        return WeldSymbolContext(
            arrowSide = arrowData,
            otherSide = otherData
        )
    }


    /*
    * Function : splitOCRBySide
    * Parameters : allTexts : List<RecognizedText>, imageHeight : Int
    * Description : This function is used to split the OCR detected texts based on the Y-position of the bounding box.
    * Return : Pair<List<RecognizedText>, List<RecognizedText>
    * Note : So far only used in testing
     */
    fun splitOCRBySide(
        allTexts: List<RecognizedText>,
        imageHeight: Int
    ): Pair<List<RecognizedText>, List<RecognizedText>> {
        // Split the texts based on the Y-position of the bounding box
        return allTexts.partition {
            (it.boundingBox.top ?: 0) > imageHeight / 2
        }
    }

    /*
     * Function : parseSideData
     * Parameters : texts : List<RecognizedText>
     * Return : WeldSideData
     * Description : This function is used to parse the side data from the OCR detected texts.
     */
    private fun parseSideData(texts: List<RecognizedText>): WeldSideData {
        var angle: String? = null
        val dimensions = mutableListOf<String>()

        texts.forEach { recognized ->
            val rawText = recognized.text.trim()

            // Check if the text is likely to be an angle
            if (isLikelyAngle(rawText)) {
                // Only accept angles that contain the degree symbol or are clearly NOT also used as dimensions
                if (!rawText.contains("°")) {
                    val corrected = checkForIrregularAngle(rawText)
                    Log.d("OCRContext", "Corrected angle: $corrected from raw: $rawText")
                    angle = parseAngle(corrected).toString()
                } else {
                    angle = parseAngle(rawText).toString()
                }
            } else {
                dimensions.add(rawText)
            }
        }

        return WeldSideData(angle ?: "", dimensions)
    }

    fun isLikelyRootOpening(box: Rect, imageWidth: Int, imageHeight: Int): Boolean {
        val verticalCenter = (box.top + box.bottom) / 2.0
        val horizontalCenter = (box.left + box.right) / 2.0

        return horizontalCenter > imageWidth * 0.4 && horizontalCenter < imageWidth * 0.6
    }

    fun isLikelyDepth(box: Rect, imageWidth: Int, imageHeight: Int): Boolean {
        val centerX = (box.left + box.right) / 2.0
        val centerY = (box.top + box.bottom) / 2.0

        return centerX < imageWidth * 0.5
    }

    fun parseNumericValue(text: String): Double? {
        return when {
            "/" in text -> {
                val parts = text.split("/")
                if (parts.size == 2) {
                    val numerator = parts[0].toDoubleOrNull()
                    val denominator = parts[1].toDoubleOrNull()
                    if (numerator != null && denominator != null && denominator != 0.0)
                        numerator / denominator
                    else null
                } else null
            }
            else -> text.toDoubleOrNull()
        }
    }

    /*
        * Function : checkForIrregularAngle
        * Parameters : text : String
        * Return : String
        * Description : This function is used to check for irregular angles and correct them.
     */
    private fun checkForIrregularAngle(text: String): String {
        val angle = text.toIntOrNull() ?: return text

        return if (angle in 0..140) {
            angle.toString()
        } else {
            (angle / 10).toString()
        }
    }


    /*
     * Function : isLikelyAngle
     * Parameters : text : String
     * Return : Boolean
     * Description : This function is used to check if the text is likely to be an angle.
     */
    private fun isLikelyAngle(text: String): Boolean {
        return text.contains("°") || text.matches(Regex("\\d{2,3}"))
    }

    /*
     * Function : parseAngle
     * Parameters : text : String
     * Return : Int
     * Description : This function is used to parse the angle from the text.
     */
    private fun parseAngle(text: String): Int {
        val cleaned = text.replace("\\D".toRegex(), "")
        return cleaned.toIntOrNull() ?: 0
    }

    /*
        * Function : isLikelyDimension
        * Parameters : text : String
        * Return : Boolean
        * Description : This function is used to check if the text is likely to be a dimension.
        * Note : Currently, not in use. From a different implementation.
     */
    private fun isLikelyDimension(text: String): Boolean {
        return text.contains("/") || text.contains("\"") || text.contains(".") || text.matches(Regex("^\\d+$"))
    }
}
