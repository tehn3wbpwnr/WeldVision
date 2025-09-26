package com.example.weldvision

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.weldvision.cv.*
import com.example.weldvision.interfaces.TemplateLoader
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.opencv.core.Mat

class ImageAnalysisTest {

    private lateinit var context: Context
    private lateinit var testImage: Mat
    private lateinit var preProcess: PreProcess
    private lateinit var imageAnalysis: ImageAnalysis
    private lateinit var templateLoader: TemplateLoader


    @Before
    fun setUp() {
        System.loadLibrary("opencv_java4") //manual load of the library for tests.
        // Get a Context instance
        context = ApplicationProvider.getApplicationContext()
        // Load a test image from assets (ensure testImage.png is in app/src/main/assets)
        templateLoader = TemplateLoaderImpl()
        testImage = (templateLoader as TemplateLoaderImpl).loadMat(context, "TestImages/BothVTest.jpg")
        preProcess = PreProcess()
        imageAnalysis = ImageAnalysis()
    }

    //From MAIN
//    @Test
//    fun testTemplateMatchingPipeline() {
//        // Preprocess the test image (convert to grayscale and resize if needed)
//        val processedImage = preProcess.preProcessForTemplateMatching(testImage)
//
//        // Load all the templates from assets
//        val templates = loadTemplates(context)
//
//        // Run template matching over multiple scales
//        val detections = imageAnalysis.detectAllSymbols(processedImage, templates)
//
//        // Example assertions:
//        // Ensure that some detections were found.
//        assertTrue("No detections found", detections.isNotEmpty())
//
//        // Optionally, verify that a specific template is detected.
//        // For instance, if you expect a detection from "ArrowV":
//        val otherVDetections = detections.filter { it.templateName == "ArrowV" }
//        assertTrue("OtherV template was not detected", otherVDetections.isNotEmpty())
//
//        // If you have expected confidence or count, check that as well.
//        // For example, assert that the first detection has a high enough confidence.
//        val firstDetection = detections.first()
//        assertTrue("Detection confidence too low", firstDetection.confidence > 0.8)
//    }
    //From KAN-40
//    @Test
//    fun testTemplateMatchingPipeline() {
//        // Preprocess the test image (convert to grayscale and resize if needed)
//        val processedImage = preProcess.preProcessForTemplateMatching(testImage)
//
//        // Load all the templates from assets
//        val templates = (templateLoader as TemplateLoaderImpl).loadTemplates(context)
//
//        // Run template matching over multiple scales
//        val detections = imageAnalysis.detectAllSymbols(processedImage, templates)
//
//        // Example assertions:
//        // Ensure that some detections were found.
//        assertTrue("No detections found", detections.isNotEmpty())
//
//        // Optionally, verify that a specific template is detected.
//        // For instance, if you expect a detection from "ArrowV":
//        val otherVDetections = detections.filter { it.templateName == "ArrowV" }
//        assertTrue("OtherV template was not detected", otherVDetections.isNotEmpty())
//
//        // If you have expected confidence or count, check that as well.
//        // For example, assert that the first detection has a high enough confidence.
//        val firstDetection = detections.first()
//        assertTrue("Detection confidence too low", firstDetection.confidence > 0.8)
//    }
}
