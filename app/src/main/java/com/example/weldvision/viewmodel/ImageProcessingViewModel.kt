// ImageProcessingViewModel.kt
package com.example.weldvision.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weldvision.cv.ImageAnalysis
import com.example.weldvision.cv.OCR
import com.example.weldvision.cv.PreProcess
import com.example.weldvision.cv.TemplateLoaderImpl
import com.example.weldvision.interfaces.GrooveBuilder
import com.example.weldvision.interfaces.ImageAnalyzer
import com.example.weldvision.interfaces.ImagePreProcessor
import com.example.weldvision.interfaces.OCRProcessor
import com.example.weldvision.interfaces.TemplateLoader
import com.example.weldvision.model.Detection
import com.example.weldvision.model.GrooveSymbol
import com.example.weldvision.model.RecognizedText
import com.example.weldvision.model.WeldSymbolContext
import com.example.weldvision.utils.GrooveBuilderImpl
import com.example.weldvision.utils.OCRContext
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.Rect
import org.opencv.imgproc.Imgproc
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt


class ImageProcessingViewModel @Inject constructor(
    application: Application,
    val imagePreProcessor: ImagePreProcessor = PreProcess(),
    val templateLoader: TemplateLoader = TemplateLoaderImpl(),
    val imageAnalyzer: ImageAnalyzer = ImageAnalysis(),
    val ocrProcessor: OCRProcessor = OCR(),
    val grooveBuilder: GrooveBuilder = GrooveBuilderImpl() // Using our implementation here.
) : AndroidViewModel(application) {

    val detectionResults = mutableStateOf<List<Detection>>(emptyList())
    val recognizedTextResults = mutableStateOf<List<RecognizedText>>(emptyList())
    val grooveData = mutableStateOf<GrooveSymbol?>(null)
    // 1. Declare a mutable state for your weld symbol context
    val weldSymbolContext = mutableStateOf<WeldSymbolContext?>(null)
    val isLoading = mutableStateOf(false)

    /**
     * Given the Uri from a captured image, perform the full processing pipeline:
     * 1. Convert the Uri to a Bitmap and then to an OpenCV Mat.
     * 2. Preprocess the image using PreProcess.kt.
     * 3. Load the templates.
     * 4. Run the detection via ImageAnalysis.kt.
     * 5. Update the detectionResults state.
     * 6. Builds Data Object
     */

    /**
     * TODO
     * Add further Analysis techniques if needed (feature detection? connected component?)
     * add early exit conditions if something is found above certain confidence interval
     * Build out other groove types, very railroaded into a double V groove right now.
     */
    fun analyzeCapturedImage(imageUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            // Set loading state to true for UI
            withContext(Dispatchers.Main) { isLoading.value = true }

            // Load bitmap
            val context = getApplication<Application>().applicationContext
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val imageWidth = bitmap.width
            val imageHeight = bitmap.height
            inputStream?.close()

            // STEP 1: OCR FIRST
            val inputImage = InputImage.fromBitmap(bitmap, 0)
            val recognizedTexts = ocrProcessor.process(inputImage)

            // Update OCR state
            withContext(Dispatchers.Main) {
                recognizedTextResults.value = recognizedTexts
            }

            // STEP 2: Extract Weld Context
            val ocrContext = OCRContext()
            val (arrowTexts, otherTexts) = ocrContext.splitOCRBySide(
                recognizedTexts,
                imageHeight
            )
            val weldContext = ocrContext.extractWeldSymbolContext(arrowTexts, otherTexts)

            withContext(Dispatchers.Main) {
                weldSymbolContext.value = weldContext
            }

            // STEP 3: Detection AFTER OCR
            val srcMat = Mat().apply { Utils.bitmapToMat(bitmap, this) }
            val processedMat = imagePreProcessor.preProcess(srcMat)
            val croppedMat = cropImage(processedMat, 0.05)

            val templates = templateLoader.loadTemplates(context)

            val splitResult = splitImageByLongestHorizontalLine(croppedMat)
            val (otherMat, arrowMat) = splitResult ?: Pair(srcMat, srcMat)

            val detections = imageAnalyzer.detectSymbols(otherMat, templates, "Other") +
                    imageAnalyzer.detectSymbols(arrowMat, templates, "Arrow")

            withContext(Dispatchers.Main) {
                detectionResults.value = detections
            }

            Log.d(
                "GrooveBuilderDebug",
                "$detections $recognizedTexts $weldContext $imageWidth $imageHeight"
            )

            // STEP 4: Build Groove
            val groove = grooveBuilder.buildGroove(
                detections,
                recognizedTexts,
                weldContext,
                imageWidth,
                imageHeight
            )

            withContext(Dispatchers.Main) {
                grooveData.value = groove
            }

            withContext(Dispatchers.Main) {
                isLoading.value = false
            }
        }
    }



    fun cropImage(srcMat: Mat, cropFraction: Double = 0.1): Mat {
        val cols = srcMat.cols()
        val rows = srcMat.rows()
        val cropX = (cols * cropFraction).toInt()
        val cropY = (rows * cropFraction).toInt()
        val newWidth = cols - 2 * cropX
        val newHeight = rows - 2 * cropY
        return srcMat.submat(Rect(cropX, cropY, newWidth, newHeight))
    }


    fun splitImageByLongestHorizontalLine(srcMat: Mat): Pair<Mat, Mat>? {

        // Edge detection.
        val edges = Mat()
        Imgproc.Canny(srcMat, edges, 50.0, 150.0)

        // Detect lines using HoughLinesP.
        val lines = Mat()
        Imgproc.HoughLinesP(edges, lines, 1.0, Math.PI / 180, 100, 50.0, 10.0)
        if (lines.empty()) return null

        var longestLineY = -1
        var maxLength = 0.0

        // Iterate over each detected line.
        for (i in 0 until lines.rows()) {
            val line = lines.get(i, 0) ?: continue
            if (line.size < 4) continue

            val x1 = line[0]
            val y1 = line[1]
            val x2 = line[2]
            val y2 = line[3]
            val length = sqrt((x2 - x1).pow(2) + (y2 - y1).pow(2))
            val angle = Math.toDegrees(atan2(y2 - y1, x2 - x1))

            // Check if the line is nearly horizontal (within ±10 degrees).
            if (abs(angle) < 10.0 && length > maxLength) {
                maxLength = length
                longestLineY = ((y1 + y2) / 2).toInt()
            }
        }

        if (longestLineY < 0) return null

        // Define the overlap amount.
        val overlap = 15

        // Calculate new boundaries with overlap, ensuring they are within the image.
        val topEnd = minOf(srcMat.rows(), longestLineY + overlap)
        val bottomStart = maxOf(0, longestLineY - overlap)

        // Crop the image with the overlap included.
        val topHalf = srcMat.submat(Rect(0, 0, srcMat.cols(), topEnd))
        val bottomHalf =
            srcMat.submat(Rect(0, bottomStart, srcMat.cols(), srcMat.rows() - bottomStart))

        return Pair(topHalf, bottomHalf)
    }

    fun updateGrooveData(
        arrowGroove: String,
        otherGroove: String,
        arrowDepth: Double?,
        otherDepth: Double?,
        rootOpening: Double?,
        arrowAngle: Int?,
        otherAngle: Int?
    ) {
        grooveData.value = GrooveSymbol(
            arrowGroove = arrowGroove.takeIf { it.isNotEmpty() && it != "None" },
            otherGroove = otherGroove.takeIf { it.isNotEmpty() && it != "None" },
            arrowDepth = arrowDepth,
            otherDepth = otherDepth,
            rootOpening = rootOpening,
            arrowAngle = arrowAngle,
            otherAngle = otherAngle
        )
    }

        /*
     * File: saveBitmapToDownloads
     * Parameters: context: Context, bitmap: Bitmap, filename: String
     * Return: Unit
     * Description: Saves the given bitmap to the Downloads directory with the given filename.
     * Note: This function requires the WRITE_EXTERNAL_STORAGE permission, and is only used in debugging scenarios
     *  for seeing how the image looks after processing.
     */
    fun saveBitmapToDownloads(context: Context, bitmap: Bitmap, filename: String) {
        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloadsDir.exists()) downloadsDir.mkdirs()

        val file = File(downloadsDir, filename)
        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
            Log.d("ImageSaveDebug", "Saved image to: ${file.absolutePath}")
        } catch (e: IOException) {
            Log.e("ImageSaveDebug", "Failed to save image", e)
        }
    }
}

