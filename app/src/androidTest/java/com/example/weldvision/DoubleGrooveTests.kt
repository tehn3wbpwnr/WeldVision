package com.example.weldvision

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import com.example.weldvision.viewmodel.ImageProcessingViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.FileOutputStream

class DoubleGrooveTests {

    private lateinit var viewModel: ImageProcessingViewModel
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
    private fun processImage(imageName: String, assetSubPath: String = "TestImages/DoubleSidedSymbols"): Uri {
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

    private fun resetState() {
        viewModel.detectionResults.value = emptyList()
        viewModel.recognizedTextResults.value = emptyList()
        viewModel.grooveData.value = null
    }

    /*
* Test to ensure that the double bevel groove is detectable
* This test currently fails since the edge detection is recognizing the other groove as a V groove
 */
    @Test
    fun ensureDoubleBevelIsDetectable() = runBlocking {
        val testUri = processImage("Double-Bevel-Groove.jpg")
        resetState()
        viewModel.analyzeCapturedImage(testUri)

        delay(10000L)
        val grooveResult = viewModel.grooveData.value
        assertEquals("Bevel", grooveResult?.arrowGroove)
        assertEquals("Bevel", grooveResult?.otherGroove)
    }

    @Test
    fun ensureDoubleJGrooveIsDetectable() = runBlocking {
        val testUri = processImage("Double-J-Groove.jpg")
        resetState()
        viewModel.analyzeCapturedImage(testUri)


        delay(10000L)
        val grooveResult = viewModel.grooveData.value
        assertEquals("J", grooveResult?.arrowGroove)
        assertEquals("J", grooveResult?.otherGroove)
    }

    @Test
    fun ensureDoubleUGrooveIsDetectable() = runBlocking {
        val testUri = processImage("Double-U-Groove-Symbol.jpg")
        resetState()
        viewModel.analyzeCapturedImage(testUri)
        delay(10000L)

        //Assert that the groove type is U
        val grooveResult = viewModel.grooveData.value
        assertEquals("U", grooveResult?.arrowGroove)
        assertEquals("U", grooveResult?.otherGroove)
    }

    @Test
    fun ensureJAndUGrooveIsDetectable() = runBlocking {
        val testUri = processImage("J-U-Groove.jpg")
        resetState()
        viewModel.analyzeCapturedImage(testUri)

        delay(10000L)
        val grooveResult = viewModel.grooveData.value
        assertEquals("U", grooveResult?.arrowGroove)
        assertEquals("J", grooveResult?.otherGroove)
    }

    @Test
    fun ensureJAndVGrooveIsDetectable() = runBlocking {
        val testUri = processImage("J-V-Groove.jpg")
        resetState()
        viewModel.analyzeCapturedImage(testUri)

        delay(10000L)
        val grooveResult = viewModel.grooveData.value
        assertEquals("V", grooveResult?.arrowGroove)
        assertEquals("J", grooveResult?.otherGroove)
    }

    @Test
    fun ensureVAndUGrooveIsDetectable() = runBlocking {
        val testUri = processImage("V-U-Groove.jpg")
        resetState()
        viewModel.analyzeCapturedImage(testUri)

        delay(10000L)
        val grooveResult = viewModel.grooveData.value
        assertEquals("U", grooveResult?.arrowGroove)
        assertEquals("V", grooveResult?.otherGroove)
    }
}
