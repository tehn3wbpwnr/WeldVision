package com.example.weldvision.cv

import android.util.Log
import com.example.weldvision.interfaces.ImageAnalyzer
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Rect
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import com.example.weldvision.model.Detection
import com.example.weldvision.model.TemplateDefinition


class ImageAnalysis : ImageAnalyzer {

    companion object {
        private const val TAG = "ImageAnalysis"
    }

    override fun detectSymbols(processedMat: Mat, templates: List<TemplateDefinition>, side: String): List<Detection> {
        // Existing implementation from detectAllSymbols
        return detectAllSymbols(processedMat, templates, side)
    }

    /**
     * For each template, scales it from minScale to maxScale (in increments of scaleStep)
     * and runs template matching on the inputMat.
     *
     * The [side] parameter determines which templates to use:
     * - If side equals "Arrow", only templates with names starting with "Arrow" are processed.
     * - If side equals "Other", only templates with names starting with "Other" are processed.
     */
    fun detectAllSymbols(
        inputMat: Mat,
        templates: List<TemplateDefinition>,
        side: String, // expects "Arrow" or "Other"
        minScale: Double = 2.0,
        maxScale: Double = 2.5,
        scaleStep: Double = 0.5
    ): List<Detection> {
        // Filter templates based on the side parameter.
        val filteredTemplates = templates.filter { tmpl ->
            when (side) {
                "Arrow" -> tmpl.name.startsWith("Arrow")
                "Other" -> tmpl.name.startsWith("Other")
                else -> true // if side is unrecognized, process all templates
            }
        }

        val allDetections = mutableListOf<Detection>()

        filteredTemplates.forEach { tmpl ->
            var scale = minScale
            while (scale <= maxScale) {
                // Scale the template according to the current scale factor.
                val scaledTemplate = Mat()
                val newSize = Size(tmpl.mat.cols() * scale, tmpl.mat.rows() * scale)
                Imgproc.resize(tmpl.mat, scaledTemplate, newSize)

                // Run template matching on the scaled template.
                val detections = findTemplate(
                    inputMat = inputMat,
                    templateMat = scaledTemplate,
                    threshold = tmpl.threshold,
                    templateName = tmpl.name,
                    scale = scale
                )
                allDetections.addAll(detections)
                scale += scaleStep
            }
        }

        val bestDetections = allDetections
            .groupBy { it.templateName }
            .mapNotNull { (_, detections) -> detections.maxByOrNull { it.confidence } }
            .sortedByDescending { it.confidence }

        Log.d(TAG, "Sorted Best Detections (Best match first):")
        bestDetections.forEach { detection ->
            Log.d(
                TAG, "Template: ${detection.templateName} (Scale: ${detection.scale}) - " +
                        "Score: ${"%.4f".format(detection.confidence)} at location " +
                        "(${detection.rect.x}, ${detection.rect.y}), " +
                        "Template Size: (${detection.rect.width}, ${detection.rect.height})"
            )
        }
        return bestDetections
    }

    /**
     * Runs template matching using OpenCV's matchTemplate.
     * Returns a list of Detection objects containing the bounding rectangle,
     * template name, match confidence, and the scale factor used.
     */
    private fun findTemplate(
        inputMat: Mat,
        templateMat: Mat,
        matchMethod: Int = Imgproc.TM_CCOEFF_NORMED,
        threshold: Double = 0.8,
        templateName: String,
        scale: Double
    ): List<Detection> {
        // Convert template to grayscale if inputMat is grayscale and templateMat is not.
        val matchingTemplate: Mat = if (inputMat.channels() == 1 && templateMat.channels() > 1) {
            val converted = Mat()
            Imgproc.cvtColor(templateMat, converted, Imgproc.COLOR_BGRA2GRAY)
            Log.d(TAG, "Converted template '$templateName' to grayscale at scale $scale")
            converted
        } else {
            templateMat
        }

        // This is a mask to possibly use later, it would be used in the matchTemplate method
        // to only consider certain pixels unused currently
        val mask = Mat()
        Imgproc.threshold(matchingTemplate, mask, 240.0, 255.0, Imgproc.THRESH_BINARY_INV)

        // Guard against the case where the template is larger than the input image.
        if (matchingTemplate.cols() > inputMat.cols() || matchingTemplate.rows() > inputMat.rows()) {
            Log.d(TAG, "Template '$templateName' at scale $scale is larger than input image; skipping.")
            return emptyList()
        }

        val resultCols = inputMat.cols() - matchingTemplate.cols() + 1
        val resultRows = inputMat.rows() - matchingTemplate.rows() + 1
        val result = Mat(resultRows, resultCols, CvType.CV_32FC1)

        // Perform template matching.
        Imgproc.matchTemplate(inputMat, matchingTemplate, result, matchMethod)
        Log.d(TAG, "matchTemplate completed for '$templateName' at scale $scale (result size: ${result.size()})")

        val detections = mutableListOf<Detection>()

        // Loop through the result matrix and collect detections above the threshold.
        for (y in 0 until result.rows()) {
            for (x in 0 until result.cols()) {
                val value = result.get(y, x)[0]
                if (value >= threshold) {
                    val rect = Rect(x, y, matchingTemplate.cols(), matchingTemplate.rows())
                    detections.add(
                        Detection(
                            rect = rect,
                            templateName = templateName,
                            confidence = value,
                            scale = scale
                        )
                    )
                }
            }
        }
        Log.d(TAG, "findTemplate: Found ${detections.size} detections for template '$templateName' at scale $scale")
        return detections
    }
}

