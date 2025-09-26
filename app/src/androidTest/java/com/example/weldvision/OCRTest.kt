package com.example.weldvision

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weldvision.cv.OCR
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileOutputStream

/**
 * Instrumentation test for the OCR class.
 * Verifies that ML Kit's text recognition is working as expected on a known test image.
 */
@RunWith(AndroidJUnit4::class)
class OCRTest {

    /**
     * Helper method to copy an asset image to a temporary file and return its Uri.
     * Adjust 'assetSubPath' and 'imageName' to match your test image location.
     */
    private fun copyAssetToCache(imageName: String, assetSubPath: String = "TestImages/OCRContext"): Uri {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val assetPath = "$assetSubPath/$imageName"
        val assetManager = context.assets

        // Copy the asset to a temp file in cache directory
        val inputStream = assetManager.open(assetPath)
        val tempFile = File(context.cacheDir, imageName)
        FileOutputStream(tempFile).use { output ->
            inputStream.copyTo(output)
        }
        inputStream.close()
        return Uri.fromFile(tempFile)
    }

    @Test
    fun testOCRReadsSampleText() = runBlocking {
        // 1. Copy the test image from assets to a temporary file, get its Uri.
        val testUri = copyAssetToCache("ArrowSide_25_3_40.jpg")

        // 2. Create an InputImage from the Uri.
        val context = ApplicationProvider.getApplicationContext<Context>()
        val inputImage = InputImage.fromFilePath(context, testUri)

        // 3. Instantiate your OCR class and run the process.
        val ocr = OCR()
        val recognizedTexts = ocr.process(inputImage)

        // 4. Verify the results. For a basic test, check if we found any text at all.
        assertTrue("Expected some recognized text, but got none.", recognizedTexts.isNotEmpty())

        // 5. (Optional) If you know exactly what text to expect, you can do more specific assertions:
        //    e.g., check if recognizedTexts contains a line with "Hello World" or similar.
         val allTexts = recognizedTexts.joinToString { it.text }
         assertTrue("Expected '25' in recognized text.", allTexts.contains("25"))
         assertTrue("Expected '3' in recognized text.", allTexts.contains("3"))
         assertTrue("Expected '40' in recognized text.", allTexts.contains("40"))
    }

    @Test
    fun testDoubleGrooveSample() = runBlocking {
        // 1. Copy the test image from assets to a temporary file, get its Uri.
        val testUri = copyAssetToCache("45_V_And_30_U.jpg")

        // 2. Create an InputImage from the Uri.
        val context = ApplicationProvider.getApplicationContext<Context>()
        val inputImage = InputImage.fromFilePath(context, testUri)

        // 3. Instantiate your OCR class and run the
        val ocr = OCR()
        val recognizedTexts = ocr.process(inputImage)

        val allTexts = recognizedTexts.joinToString { it.text }
        Log.d("OCRTest", "Recognized text: $allTexts")

        // 4. Verify the results have 45 and 30 degrees.
        assertTrue("Expected '45' in recognized text.", allTexts.contains("45"))
        assertTrue("Expected '30' in recognized text.", allTexts.contains("30"))
    }

    @Test
    fun test_OCR_Reads_Text_From_25_U_And_40_U_Image() = runBlocking {
        // 1. Copy the test image from assets to a temporary file, get its Uri.
        val testUri = copyAssetToCache("25_U_And_40_U.jpg")

        // 2. Create an InputImage from the Uri.
        val context = ApplicationProvider.getApplicationContext<Context>()
        val inputImage = InputImage.fromFilePath(context, testUri)

        // 3. Instantiate your OCR class and run the
        val ocr = OCR()
        val recognizedTexts = ocr.process(inputImage)

        val allTexts = recognizedTexts.joinToString { it.text }
        Log.d("OCRTest", "Recognized text: $allTexts")

        // 4. Verify the results have 45 and 30 degrees.
        assertTrue("Expected '45' in recognized text.", allTexts.contains("40"))
        assertTrue("Expected '30' in recognized text.", allTexts.contains("25"))
    }
}
