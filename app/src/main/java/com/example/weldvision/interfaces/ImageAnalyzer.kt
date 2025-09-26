package com.example.weldvision.interfaces

import org.opencv.core.Mat
import com.example.weldvision.model.Detection
import com.example.weldvision.model.TemplateDefinition

interface ImageAnalyzer {
    fun detectSymbols(processedMat: Mat, templates: List<TemplateDefinition>, side: String): List<Detection>
}
