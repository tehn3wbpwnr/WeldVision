package com.example.weldvision

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import com.example.weldvision.cv.OCR
import com.example.weldvision.viewmodel.ImageProcessingViewModel
import com.example.weldvision.utils.OCRContext
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.FileOutputStream
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.util.Log
import org.opencv.core.Rect as OpenCVRect
import com.example.weldvision.model.Detection
import com.example.weldvision.model.GrooveSymbol
import com.example.weldvision.model.RecognizedText
import com.example.weldvision.utils.GrooveBuilderImpl
import kotlinx.coroutines.delay
import org.opencv.android.Utils
import org.opencv.core.Mat


class ContextTesting {

    // ViewModel to test
    private lateinit var viewModel: ImageProcessingViewModel
    // Delay to allow coroutines to finish
    private val kDelay: Long = 5000L

    @Before
    fun setUp() {
        // Load OpenCV native library.
        System.loadLibrary("opencv_java4")
        // Get the application context and initialize the ViewModel.
        val app = ApplicationProvider.getApplicationContext<Application>()
        viewModel = ImageProcessingViewModel(app)
    }

    /**
     * Helper function to copy an asset image to a temporary file and return its Uri.
     * Points to assets/TestImages/OCRContext by default.
     */
    private fun processImage(
        imageName: String,
        assetSubPath: String = "TestImages/OCRContext"
    ): Uri {
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

    private fun resetViewModel() {
        viewModel.detectionResults.value = emptyList()
        viewModel.recognizedTextResults.value = emptyList()
        viewModel.grooveData.value = null
        viewModel.weldSymbolContext.value = null
    }

    private fun checkForIrregularAngle(text: String): String {
        val angle = text.toIntOrNull() ?: return text

        return if (angle in 0..140) {
            angle.toString()
        } else {
            (angle / 10).toString()
        }
    }

    @Test
    fun testCheckForIrregularAngle() {
        assertEquals("60", checkForIrregularAngle("60"))     // normal case
        assertEquals("60", checkForIrregularAngle("600"))    // fix needed
        assertEquals("25", checkForIrregularAngle("25"))     // normal case
        assertEquals("16", checkForIrregularAngle("160"))    // likely invalid
        assertEquals("abc", checkForIrregularAngle("abc"))   // non-numeric
        assertEquals("140", checkForIrregularAngle("140"))   // upper bound
        assertEquals("15", checkForIrregularAngle("150"))    // over limit
    }

    @Test
    fun test_OCRContext_Corrects_600_To_60_Without_Image() = runBlocking {
        // Simulate OCR output with a misread angle
        val mockedRecognizedTexts = listOf(
            RecognizedText(
                text = "600",
                boundingBox = Rect(100, 200, 150, 250)
            )
        )

        // Pass into OCRContext and extract context
        val ocrContext = OCRContext()
        val contextResult = ocrContext.extractWeldSymbolContext(
            arrowTexts = mockedRecognizedTexts,
            otherTexts = emptyList()
        )

        // Make sure the "angle" is corrected from 600 to 60
        assertEquals("60", contextResult.arrowSide.angle)
        assertTrue(contextResult.arrowSide.dimensions.isEmpty())
    }

    @Test
    fun manually_Test_OCR_Context_Without_ViewModel_Arrow_J(): Unit = runBlocking {
        // 1. Load the test image from assets (adjust the filename as needed).
        val testUri = processImage("ArrowJ.jpg")

        // convert the uri to an InputImage
        val context = ApplicationProvider.getApplicationContext<Context>()
        val inputImage = InputImage.fromFilePath(context, testUri)

        // 2. Create an OCR instance and process the image.
        val ocr = OCR()
        val recognizedTexts = ocr.process(inputImage)

        Log.d("ContextTesting", "Recognized Texts: $recognizedTexts")


        // 4. Create an OCRContext instance and extract the WeldSymbolContext.
        val ocrContext = OCRContext()
        val contextResult = ocrContext.extractWeldSymbolContext(recognizedTexts, emptyList())

        // 5. Verify the arrow side has angle 40 and dimensions 25 and 3.
        //assertEquals("Arrow angles: ", null, contextResult.arrowSide.angles)
        assertEquals("Arrow dimensions: ", listOf("3/4"), contextResult.arrowSide.dimensions)
    }

    @Test
    fun manually_Test_OCR_Context_Without_ViewModel_Arrow_U(): Unit = runBlocking {
        // 1. Load the test image from assets (adjust the filename as needed).
        val testUri = processImage("ArrowU.jpg")

        // convert the uri to an InputImage
        val context = ApplicationProvider.getApplicationContext<Context>()
        val inputImage = InputImage.fromFilePath(context, testUri)

        // 2. Create an OCR instance and process the image.
        val ocr = OCR()
        val recognizedTexts = ocr.process(inputImage)

        Log.d("ContextTesting", "Recognized Texts: $recognizedTexts")


        // 4. Create an OCRContext instance and extract the WeldSymbolContext.
        val ocrContext = OCRContext()
        val contextResult = ocrContext.extractWeldSymbolContext(recognizedTexts, emptyList())

        // 5. Verify the arrow side has angle 40 and dimensions 25 and 3.
        assertEquals("Arrow angles: ", "60", contextResult.arrowSide.angle)
        assertEquals("Arrow dimensions: ", listOf("1/2", "1/8"), contextResult.arrowSide.dimensions)
    }

    @Test
    fun automatic_Test_OCR_Context_With_ViewModel_ArrowJ(): Unit = runBlocking {
        // 1. Load the test image from assets (adjust the filename as needed).
        val testUri = processImage("ArrowJ.jpg")

        // 2. Reset relevant states in the ViewModel.
        resetViewModel()

        // 3. Analyze the image (this triggers your detection + OCR pipeline).
        viewModel.analyzeCapturedImage(testUri)
        delay(kDelay)

        // 4. Retrieve the OCR context (if your ViewModel sets it).
        val contextResult = viewModel.weldSymbolContext.value
        assertNotNull("WeldSymbolContext should not be null", contextResult)

        contextResult?.let {
        // 5. Verify the arrow side has angle 40 and dimensions 25 and 3.
        assertEquals("Arrow angles: ", "", it.arrowSide.angle)
        assertEquals("Arrow dimensions: ", listOf("3/4"), it.arrowSide.dimensions)
        }
    }

    @Test
    fun manual_Build_of_ViewModel_With_Split_ArrowJ (): Unit = runBlocking {
        // 1. Load the test image from assets.
        val testUri = processImage("ArrowJ.jpg")
        val context = ApplicationProvider.getApplicationContext<Context>()
        val inputStream = context.contentResolver.openInputStream(testUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        // 2. Convert Bitmap to OpenCV Mat.
        val srcMat = Mat()
        Utils.bitmapToMat(bitmap, srcMat)

        // 3. Pre-process the image.
        val processedMat = viewModel.imagePreProcessor.preProcess(srcMat)

        // 4. Crop the image using the public cropImage() function.
        val croppedMat = viewModel.cropImage(processedMat, 0.2)

        // 5. Split the image using the public splitImageByLongestHorizontalLine() function.
        val splitResult = viewModel.splitImageByLongestHorizontalLine(croppedMat)
        val (otherSideMat, arrowSideMat) = splitResult ?: Pair(srcMat, srcMat)

        // 6. Convert the arrow side Mat to a Bitmap using the public MatToBitmap() function.
        // Convert Mats back to Bitmaps for OCR.
        val otherBitmap = Bitmap.createBitmap(otherSideMat.cols(), otherSideMat.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(otherSideMat, otherBitmap)
        val arrowBitmap = Bitmap.createBitmap(arrowSideMat.cols(), arrowSideMat.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(arrowSideMat, arrowBitmap)

        // 7. Create an InputImage from the arrow side Bitmap and run OCR.
        val arrowInputImage = InputImage.fromBitmap(arrowBitmap, 0)
        val arrowRecognizedTexts = viewModel.ocrProcessor.process(arrowInputImage)

        Log.d("TestSplit", "Arrow OCR results: $arrowRecognizedTexts")

        // 8. Extract the WeldSymbolContext using your OCRContext (or WeldSymbolContextExtractor).
        val ocrContext = OCRContext()
        val weldContext = ocrContext.extractWeldSymbolContext(arrowRecognizedTexts, emptyList())

        delay(kDelay)

        // 9. Verify that the arrow side has angle 40 and dimensions 25 and 3.
        assertEquals("Arrow angles mismatch", "", weldContext.arrowSide.angle)
        assertEquals("Arrow dimensions mismatch", listOf("3/4"), weldContext.arrowSide.dimensions)
    }

    @Test
    fun test_GrooveBuilder_FieldMapping_ArrowBevel(): Unit = runBlocking {
        val testUri = processImage("ArrowBevel.jpg", "TestImages/SingleSidedSymbols")
        val context = ApplicationProvider.getApplicationContext<Context>()
        val inputStream = context.contentResolver.openInputStream(testUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        val imageWidth = bitmap.width
        val imageHeight = bitmap.height

        val ocr = OCR()
        val recognizedTexts = ocr.process(InputImage.fromBitmap(bitmap, 0))

        val ocrContext = OCRContext()
        val contextResult = ocrContext.extractWeldSymbolContext(
            arrowTexts = recognizedTexts,
            otherTexts = emptyList()
        )

        val fakeDetection = Detection(
            rect = OpenCVRect(0, 0, 100, 100),
            templateName = "ArrowBevel",
            confidence = 0.99
        )

        val grooveBuilder = GrooveBuilderImpl()
        val groove = grooveBuilder.buildGroove(
            detections = listOf(fakeDetection),
            recognizedTexts = recognizedTexts,
            context = contextResult,
            imageWidth = imageWidth,
            imageHeight = imageHeight
        )

        with(groove) {
            assertEquals("Bevel", arrowGroove)
            assertEquals("", otherGroove)

            // Updated to check arrow-specific dimension fields
            assertEquals(25.0, arrowDepth)      // 25 as arrow side depth
            assertEquals(null, otherDepth)      // No other side depth
            assertEquals(3.0, rootOpening)      // Root opening = 3
            assertEquals(40, arrowAngle)        // Angle under arrow
            assertEquals(null, otherAngle)      // No angle on other side
        }
    }

    @Test
    fun test_GrooveBuilder_FieldMapping_ArrowU(): Unit = runBlocking {
        val testUri = processImage("ArrowU.jpg", "TestImages/OCRContext")
        val context = ApplicationProvider.getApplicationContext<Context>()
        val inputStream = context.contentResolver.openInputStream(testUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        val imageWidth = bitmap.width
        val imageHeight = bitmap.height

        val ocr = OCR()
        val recognizedTexts = ocr.process(InputImage.fromBitmap(bitmap, 0))

        val ocrContext = OCRContext()
        val contextResult = ocrContext.extractWeldSymbolContext(
            arrowTexts = recognizedTexts,
            otherTexts = emptyList()
        )

        val fakeDetection = Detection(
            rect = OpenCVRect(0, 0, 100, 100),
            templateName = "ArrowU",
            confidence = 0.99
        )

        val grooveBuilder = GrooveBuilderImpl()
        val groove = grooveBuilder.buildGroove(
            detections = listOf(fakeDetection),
            recognizedTexts = recognizedTexts,
            context = contextResult,
            imageWidth = imageWidth,
            imageHeight = imageHeight
        )

        Log.d("GrooveDebug", "Result: $groove")

        assertEquals("U", groove.arrowGroove)
        assertEquals("", groove.otherGroove)
        assertEquals(0.5, groove.arrowDepth)
        assertEquals(null, groove.otherDepth)
        assertEquals(0.125, groove.rootOpening)
        assertEquals(60, groove.arrowAngle)
        assertEquals(null, groove.otherAngle)
    }

    @Test
    fun test_GrooveBuilder_FieldMapping_DoubleSided_U_U(): Unit = runBlocking {
        // 1. Load the test image from assets.
        val testUri = processImage("25_U_And_40_U.jpg", "TestImages/OCRContext")
        val context = ApplicationProvider.getApplicationContext<Context>()
        val inputStream = context.contentResolver.openInputStream(testUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        val imageWidth = bitmap.width
        val imageHeight = bitmap.height

        // 2. Run OCR on the image.
        val ocr = OCR()
        val recognizedTexts = ocr.process(InputImage.fromBitmap(bitmap, 0))

        // 3. Split the OCR result into "arrow" side and "other" side based on Y-position.
        val ocrContext = OCRContext()
        val (arrowTexts, otherTexts) = ocrContext.splitOCRBySide(recognizedTexts, imageHeight)

        // 4. Parse the OCR text into WeldSymbolContext.
        val contextResult = ocrContext.extractWeldSymbolContext(
            arrowTexts = arrowTexts,
            otherTexts = otherTexts
        )

        // 5. Create fake detections for the arrow and other sides (this should be based on actual detection logic in the app).
        val fakeDetectionArrow = Detection(
            rect = OpenCVRect(0, 0, 100, 100),
            templateName = "ArrowU",
            confidence = 0.95
        )
        val fakeDetectionOther = Detection(
            rect = OpenCVRect(0, 0, 100, 100),
            templateName = "OtherU",
            confidence = 0.95
        )

        // 6. Use GrooveBuilder to build the groove from detections and OCR context.
        val grooveBuilder = GrooveBuilderImpl()
        val groove = grooveBuilder.buildGroove(
            detections = listOf(fakeDetectionArrow, fakeDetectionOther),
            recognizedTexts = recognizedTexts,
            context = contextResult,
            imageWidth = imageWidth,
            imageHeight = imageHeight
        )

        // 7. Assert the expected values based on the OCR and detection results.
        assertEquals("U", groove.arrowGroove)
        assertEquals("U", groove.otherGroove)
        assertEquals(25, groove.arrowAngle)  // Ensure this value is what you expect for the arrow side.
        assertEquals(40, groove.otherAngle) // Ensure this value is what you expect for the other side.
        assertEquals(null, groove.arrowDepth)    // Depth should be null if it is not provided.
        assertEquals(null, groove.rootOpening) // Root opening should be null if it is not provided.
    }

    @Test
    fun test_ArrowBevel_Image_From_Uri(): Unit = runBlocking {
        // 1. Load the test image from assets.
        val testUri = processImage("ArrowBevel.jpg", "TestImages/SingleSidedSymbols")
        val context = ApplicationProvider.getApplicationContext<Context>()
        val inputStream = context.contentResolver.openInputStream(testUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        val imageWidth = bitmap.width
        val imageHeight = bitmap.height

        // 2. Run OCR on the image.
        val ocr = OCR()
        val recognizedTexts = ocr.process(InputImage.fromBitmap(bitmap, 0))

        // 3. Split the OCR result into "arrow" side and "other" side based on Y-position.
        val ocrContext = OCRContext()
        val (arrowTexts, otherTexts) = ocrContext.splitOCRBySide(recognizedTexts, imageHeight)

        // 4. Parse the OCR text into WeldSymbolContext.
        val contextResult = ocrContext.extractWeldSymbolContext(
            arrowTexts = arrowTexts,
            otherTexts = otherTexts
        )

        // 5. Create fake detections for the arrow and other sides (this should be based on actual detection logic in the app).
        val fakeDetectionArrow = Detection(
            rect = OpenCVRect(0, 0, 100, 100),
            templateName = "ArrowBevel",
            confidence = 0.95
        )
        val fakeDetectionOther = Detection(
            rect = OpenCVRect(0, 0, 100, 100),
            templateName = "OtherBevel",
            confidence = 0.95
        )

        // 6. Use GrooveBuilder to build the groove from detections and OCR context.
        val grooveBuilder = GrooveBuilderImpl()
        val groove = grooveBuilder.buildGroove(
            detections = listOf(fakeDetectionArrow, fakeDetectionOther),
            recognizedTexts = recognizedTexts,
            context = contextResult,
            imageWidth = imageWidth,
            imageHeight = imageHeight
        )

        Log.d("GrooveDebug", "Result: $groove")

        // 7. Assert the expected values based on the OCR and detection results.
        // Disregarding the groove type and focusing on the dimensions and angles.
        assertEquals(25.0, groove.arrowDepth)     // Ensure the depth is extracted correctly.
        assertEquals(null, groove.otherDepth) // Since there is no depth for the other side.
        assertEquals(3.0, groove.rootOpening) // Ensure the root opening is correctly extracted.
        assertEquals(null, groove.otherAngle) // Since there is no angle for the other side.
        assertEquals(40, groove.arrowAngle)  // Ensure the arrow angle is correctly identified.
    }

    @Test
    fun test_ViewModel_GrooveBuilder_Handles_45_V_And_30_U_Correctly(): Unit = runBlocking {
        // 1. Load the test image from assets.
        val testUri = processImage("45_V_And_30_U.jpg", "TestImages/OCRContext")
        val context = ApplicationProvider.getApplicationContext<Context>()
        val inputStream = context.contentResolver.openInputStream(testUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        val imageWidth = bitmap.width
        val imageHeight = bitmap.height

        // 2. Run OCR on the image.
        val ocr = OCR()
        val recognizedTexts = ocr.process(InputImage.fromBitmap(bitmap, 0))

        // 3. Split the OCR result into "arrow" side and "other" side based on Y-position.
        val ocrContext = OCRContext()
        val (arrowTexts, otherTexts) = ocrContext.splitOCRBySide(recognizedTexts, imageHeight)

        // 4. Parse the OCR text into WeldSymbolContext.
        val contextResult = ocrContext.extractWeldSymbolContext(
            arrowTexts = arrowTexts,
            otherTexts = otherTexts
        )

        // 5. Create fake detections for the arrow and other sides (this should be based on actual detection logic in the app).
        val fakeDetectionArrow = Detection(
            rect = OpenCVRect(0, 0, 100, 100),
            templateName = "ArrowBevel",
            confidence = 0.95
        )
        val fakeDetectionOther = Detection(
            rect = OpenCVRect(0, 0, 100, 100),
            templateName = "OtherBevel",
            confidence = 0.95
        )

        // 6. Use GrooveBuilder to build the groove from detections and OCR context.
        val grooveBuilder = GrooveBuilderImpl()
        val groove = grooveBuilder.buildGroove(
            detections = listOf(fakeDetectionArrow, fakeDetectionOther),
            recognizedTexts = recognizedTexts,
            context = contextResult,
            imageWidth = imageWidth,
            imageHeight = imageHeight
        )

        Log.d("GrooveDebug", "Result: $groove")

        // 7. Assert the expected values based on the OCR and detection results.
        // Disregarding the groove type and focusing on the dimensions and angles.
        assertEquals(null, groove.arrowDepth)     // Ensure the depth is extracted correctly.
        assertEquals(null, groove.otherDepth) // Since there is no depth for the other side.
        assertEquals(null, groove.rootOpening) // Ensure the root opening is correctly extracted.
        assertEquals(45, groove.otherAngle) // Since there is no angle for the other side.
        assertEquals(30, groove.arrowAngle)  // Ensure the arrow angle is correctly identified.
    }

    @Test
    fun test_ViewModel_BuildsGroove_For_45_V_And_30_U_Image(): Unit = runBlocking {
        // 1. Load image from assets
        val testUri = processImage("45_V_And_30_U.jpg", "TestImages/OCRContext")
        val context = ApplicationProvider.getApplicationContext<Application>()

        // 2. Initialize ViewModel and reset state
        val viewModel = ImageProcessingViewModel(context)
        viewModel.detectionResults.value = emptyList()
        viewModel.recognizedTextResults.value = emptyList()
        viewModel.grooveData.value = null
        viewModel.weldSymbolContext.value = null

        // 3. Trigger full pipeline
        viewModel.analyzeCapturedImage(testUri)

        // 4. Wait for result
        val timeout = 5000L
        val startTime = System.currentTimeMillis()
        var result: GrooveSymbol? = null

        while (System.currentTimeMillis() - startTime < timeout) {
            result = viewModel.grooveData.value
            if (result != null) break
            delay(100)
        }

        // 5. Assert groove was built
        assertNotNull("Groove result should not be null", result)

        // 6. Verify groove content
        with(result!!) {
            assertEquals(null, arrowDepth)           // No depth in image
            assertEquals(null, rootOpening)     // No root opening either
            assertEquals(30, arrowAngle)        // Angle below the groove symbol
            assertEquals(45, otherAngle)        // Angle above the groove symbol
        }

        // Optional debug
        Log.d("GrooveValidation", "Validated groove: $result")
    }

    @Test
    fun test_ViewModel_BuildsGroove_For_25_U_And_40_U_Image(): Unit = runBlocking {
        // 1. Load the test image from assets.
        val testUri = processImage("25_U_And_40_U.jpg", "TestImages/OCRContext")
        val context = ApplicationProvider.getApplicationContext<Application>()

        // 2. Initialize ViewModel and reset state.
        val viewModel = ImageProcessingViewModel(context)
        viewModel.detectionResults.value = emptyList()
        viewModel.recognizedTextResults.value = emptyList()
        viewModel.grooveData.value = null
        viewModel.weldSymbolContext.value = null

        // 3. Analyze the image using full pipeline.
        viewModel.analyzeCapturedImage(testUri)

        // 4. Wait for grooveData to be set.
        val timeout = 5000L
        val startTime = System.currentTimeMillis()
        var result: GrooveSymbol? = null

        while (System.currentTimeMillis() - startTime < timeout) {
            result = viewModel.grooveData.value
            if (result != null) break
            delay(100)
        }

        // 5. Validate result is not null.
        assertNotNull("Groove data should not be null", result)

        // Optional: log the result for debugging
        Log.d("GrooveDebug", "Groove built from 25_U_And_40_U: $result")

        // 6. Assert the expected groove values.
        with(result!!) {
            assertEquals(25, arrowAngle)     // angle under the bottom groove
            assertEquals(40, otherAngle)     // angle above the top groove
            assertEquals(null, arrowDepth)        // no dimension info in this image
            assertEquals(null, rootOpening)  // no root opening either
        }
    }

    @Test
    fun test_GrooveBuilder_Parses_25_U_And_40_U_Without_ViewModel(): Unit = runBlocking {
        // 1. Load image from assets and decode to Bitmap
        val testUri = processImage("25_U_And_40_U.jpg", "TestImages/OCRContext")
        val context = ApplicationProvider.getApplicationContext<Context>()
        val inputStream = context.contentResolver.openInputStream(testUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        val imageWidth = bitmap.width
        val imageHeight = bitmap.height

        // 2. Run OCR
        val ocr = OCR()
        val recognizedTexts = ocr.process(InputImage.fromBitmap(bitmap, 0))

        // 3. Use OCRContext to split and extract weld data
        val ocrContext = OCRContext()
        val (arrowTexts, otherTexts) = ocrContext.splitOCRBySide(recognizedTexts, imageHeight)
        val contextResult = ocrContext.extractWeldSymbolContext(arrowTexts, otherTexts)

        // 4. Provide fake detections to simulate template matching
        val fakeDetectionArrow = Detection(
            rect = OpenCVRect(0, 0, 100, 100),
            templateName = "ArrowU",
            confidence = 0.95
        )
        val fakeDetectionOther = Detection(
            rect = OpenCVRect(0, 0, 100, 100),
            templateName = "OtherU",
            confidence = 0.95
        )

        // 5. Build groove
        val grooveBuilder = GrooveBuilderImpl()
        val groove = grooveBuilder.buildGroove(
            detections = listOf(fakeDetectionArrow, fakeDetectionOther),
            recognizedTexts = recognizedTexts,
            context = contextResult,
            imageWidth = imageWidth,
            imageHeight = imageHeight
        )

        // 6. Assert parsed data
        assertEquals("U", groove.arrowGroove)
        assertEquals("U", groove.otherGroove)
        assertEquals(25, groove.arrowAngle)  // Should be lower text
        assertEquals(40, groove.otherAngle)  // Should be higher text
        assertEquals(null, groove.arrowDepth)
        assertEquals(null, groove.rootOpening)
    }

    @Test
    fun test_Viewmodel_For_Arrow_U(): Unit = runBlocking {
// 1. Load the test image from assets.
        val testUri = processImage("ArrowU.jpg", "TestImages/OCRContext")
        val context = ApplicationProvider.getApplicationContext<Application>()

        // 2. Initialize ViewModel and reset state.
        val viewModel = ImageProcessingViewModel(context)
        viewModel.detectionResults.value = emptyList()
        viewModel.recognizedTextResults.value = emptyList()
        viewModel.grooveData.value = null
        viewModel.weldSymbolContext.value = null

        // 3. Analyze the image using full pipeline.
        viewModel.analyzeCapturedImage(testUri)

        // 4. Wait for grooveData to be set.
        val timeout = 5000L
        val startTime = System.currentTimeMillis()
        var result: GrooveSymbol? = null

        while (System.currentTimeMillis() - startTime < timeout) {
            result = viewModel.grooveData.value
            if (result != null) break
            delay(100)
        }

        // 5. Validate result is not null.
        assertNotNull("Groove data should not be null", result)

        // Optional: log the result for debugging
        Log.d("GrooveDebug", "Groove built from ArrowU: $result")

        // 6. Assert the expected groove values.
        with(result!!) {
            assertEquals(60, arrowAngle)           // angle under the bottom groove
            assertEquals(null, otherAngle)         // angle above the top groove
            assertEquals(0.5, arrowDepth)              // depth (1/2 interpreted as 0.5)
            assertEquals(null, otherDepth)         // no depth for the other side
            assertEquals(0.125, rootOpening)      // root opening (1/8 interpreted as 0.125)
        }

    }
}