package com.example.weldvision

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import com.example.weldvision.viewmodel.ImageProcessingViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.FileOutputStream

class SingleSidedTests {

    private lateinit var viewModel: ImageProcessingViewModel

    private val kDelay: Long = 5000L

    @Before
    fun setUp() {
        // Load OpenCV native library.
        System.loadLibrary("opencv_java4")
        // Get the application context and initialize the view model.
        val app = ApplicationProvider.getApplicationContext<Application>()
        viewModel = ImageProcessingViewModel(app)
    }

    /**
     * Helper function to copy an asset image to a temporary file and return its Uri.
     */
    private fun processImage(imageName: String, assetSubPath: String = "TestImages/SingleSidedSymbols"): Uri {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val assetPath = "$assetSubPath/$imageName"
        val assetStream = context.assets.open(assetPath)
        val tempFile = File(context.cacheDir, imageName)
        FileOutputStream(tempFile).use { output ->
            assetStream.copyTo(output)
        }
        assetStream.close()
        return Uri.fromFile(tempFile)
    }

    @Test
    fun testAnalyzeArrowV() = runBlocking {
        val testUri = processImage("ArrowV.jpg")
        // Reset state before processing.
        viewModel.detectionResults.value = emptyList()
        viewModel.recognizedTextResults.value = emptyList()
        viewModel.grooveData.value = null

        viewModel.analyzeCapturedImage(testUri)
        // Allow processing to complete.
        delay(kDelay)

        print("Groove data: ${viewModel.grooveData.value}")

        assertNotNull("Groove data should not be null for ArrowV", viewModel.grooveData.value)

        // Ensure that the arrow groove type is V.
        val grooveResult = viewModel.grooveData.value
        assertEquals("V", grooveResult?.arrowGroove)
        // Ensure that the opposite groove is empty.
        assertTrue("Other groove should be empty for ArrowV", grooveResult?.otherGroove.isNullOrEmpty())
    }

    @Test
    fun testAnalyzeArrowU() = runBlocking {
        val testUri = processImage("ArrowU.jpg")
        viewModel.detectionResults.value = emptyList()
        viewModel.recognizedTextResults.value = emptyList()
        viewModel.grooveData.value = null

        viewModel.analyzeCapturedImage(testUri)
        delay(kDelay)

        assertNotNull("Groove data should not be null for ArrowU", viewModel.grooveData.value)

        // Ensure that the arrow groove type is U.
        val grooveResult = viewModel.grooveData.value
        assertEquals("U", grooveResult?.arrowGroove)
        // Ensure that the opposite groove is empty.
        assertTrue("Other groove should be empty for ArrowU", grooveResult?.otherGroove.isNullOrEmpty())
    }

    @Test
    fun testAnalyzeArrowJ() = runBlocking {
        val testUri = processImage("ArrowJ.jpg")
        viewModel.detectionResults.value = emptyList()
        viewModel.recognizedTextResults.value = emptyList()
        viewModel.grooveData.value = null

        viewModel.analyzeCapturedImage(testUri)
        delay(kDelay)

        assertNotNull("Groove data should not be null for ArrowJ", viewModel.grooveData.value)

        // Ensure that the arrow groove type is J.
        val grooveResult = viewModel.grooveData.value
        assertEquals("J", grooveResult?.arrowGroove)
        // Ensure that the opposite groove is empty.
        assertTrue("Other groove should be empty for ArrowJ", grooveResult?.otherGroove.isNullOrEmpty())
    }

    @Test
    fun testAnalyzeArrowBevel() = runBlocking {
        val testUri = processImage("ArrowBevel.jpg")
        viewModel.detectionResults.value = emptyList()
        viewModel.recognizedTextResults.value = emptyList()
        viewModel.grooveData.value = null

        viewModel.analyzeCapturedImage(testUri)
        delay(kDelay)

        assertNotNull("Groove data should not be null for ArrowBevel", viewModel.grooveData.value)

        // Ensure that the arrow groove type is Bevel.
        val grooveResult = viewModel.grooveData.value
        assertEquals("Bevel", grooveResult?.arrowGroove)
        // Ensure that the opposite groove is empty.
        assertTrue("Other groove should be empty for ArrowBevel", grooveResult?.otherGroove.isNullOrEmpty())
    }

    @Test
    fun testAnalyzeOtherV() = runBlocking {
        val testUri = processImage("OtherV.jpg")
        viewModel.detectionResults.value = emptyList()
        viewModel.recognizedTextResults.value = emptyList()
        viewModel.grooveData.value = null

        viewModel.analyzeCapturedImage(testUri)
        delay(kDelay)

        assertNotNull("Groove data should not be null for OtherV", viewModel.grooveData.value)

        // Ensure that the other groove type is V.
        val grooveResult = viewModel.grooveData.value
        assertEquals("V", grooveResult?.otherGroove)
        // Ensure that the opposite groove is empty.
        assertTrue("Arrow groove should be empty for OtherV", grooveResult?.arrowGroove.isNullOrEmpty())
    }

    @Test
    fun testAnalyzeOtherU() = runBlocking {
        val testUri = processImage("OtherU.jpg")
        viewModel.detectionResults.value = emptyList()
        viewModel.recognizedTextResults.value = emptyList()
        viewModel.grooveData.value = null

        viewModel.analyzeCapturedImage(testUri)
        delay(kDelay)

        assertNotNull("Groove data should not be null for OtherU", viewModel.grooveData.value)

        // Ensure that the other groove type is U.
        val grooveResult = viewModel.grooveData.value
        assertEquals("U", grooveResult?.otherGroove)
        // Ensure that the opposite groove is empty.
        assertTrue("Arrow groove should be empty for OtherU", grooveResult?.arrowGroove.isNullOrEmpty())
    }

    @Test
    fun testAnalyzeOtherJ() = runBlocking {
        val testUri = processImage("OtherJ.jpg")
        viewModel.detectionResults.value = emptyList()
        viewModel.recognizedTextResults.value = emptyList()
        viewModel.grooveData.value = null

        viewModel.analyzeCapturedImage(testUri)
        delay(kDelay)

        assertNotNull("Groove data should not be null for OtherJ", viewModel.grooveData.value)

        // Ensure that the other groove type is J.
        val grooveResult = viewModel.grooveData.value
        assertEquals("J", grooveResult?.otherGroove)
        // Ensure that the opposite groove is empty.
        assertTrue("Arrow groove should be empty for OtherJ", grooveResult?.arrowGroove.isNullOrEmpty())
    }

    @Test
    fun testAnalyzeOtherBevel() = runBlocking {
        val testUri = processImage("OtherBevel.jpg")
        viewModel.detectionResults.value = emptyList()
        viewModel.recognizedTextResults.value = emptyList()
        viewModel.grooveData.value = null

        viewModel.analyzeCapturedImage(testUri)
        delay(kDelay)

        assertNotNull("Groove data should not be null for OtherBevel", viewModel.grooveData.value)

        // Ensure that the other groove type is Bevel.
        val grooveResult = viewModel.grooveData.value
        assertEquals("Bevel", grooveResult?.otherGroove)
        // Ensure that the opposite groove is empty.
        assertTrue("Arrow groove should be empty for OtherBevel", grooveResult?.arrowGroove.isNullOrEmpty())
    }
}