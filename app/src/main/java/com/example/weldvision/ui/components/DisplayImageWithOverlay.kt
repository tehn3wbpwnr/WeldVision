// File: app/src/main/java/com/example/weldvision/ui/components/DisplayImageWithOverlay.kt
package com.example.weldvision.ui.components

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weldvision.model.RecognizedText

/**
 * Displays a bitmap image and overlays bounding boxes with labels for recognized text.
 * The Box is sized exactly to the bitmap's intrinsic size (converted to dp) so that the bounding
 * box coordinates match the displayed image.
 *
 * @param bitmap The image to display.
 * @param recognizedTexts A list of RecognizedText objects containing text and bounding boxes.
 */
@Composable
fun DisplayImageWithOverlay(
    bitmap: android.graphics.Bitmap,
    recognizedTexts: List<RecognizedText>,
    modifier: Modifier = Modifier
) {
    // Get the density so we can convert pixel dimensions to dp.
    val density = LocalDensity.current
    val imageWidthDp = with(density) { bitmap.width.toDp() }
    val imageHeightDp = with(density) { bitmap.height.toDp() }

    Box(modifier = modifier.size(width = imageWidthDp, height = imageHeightDp)) {
        // Draw the image at its intrinsic size.
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Image with OCR overlay",
            modifier = Modifier.size(width = imageWidthDp, height = imageHeightDp)
        )

        // Draw the overlay with bounding boxes and labels.
        Canvas(modifier = Modifier.size(width = imageWidthDp, height = imageHeightDp)) {
            // Create an Android Paint object for drawing text.
            val textPaint = Paint().apply {
                color = android.graphics.Color.RED
                textSize = 14.sp.toPx()  // Convert 14 sp to pixels.
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            }

            recognizedTexts.forEach { recognizedText ->
                val rect = recognizedText.boundingBox

                // Draw the bounding box with thinner lines (1.dp).
                drawRect(
                    color = Color.Red,
                    topLeft = androidx.compose.ui.geometry.Offset(rect.left.toFloat(), rect.top.toFloat()),
                    size = androidx.compose.ui.geometry.Size(rect.width().toFloat(), rect.height().toFloat()),
                    style = Stroke(width = 1.dp.toPx())
                )

                // Draw the recognized text label above the bounding box.
                drawIntoCanvas { canvas ->
                    // Draw text slightly above the top left corner of the box.
                    canvas.nativeCanvas.drawText(
                        recognizedText.text,
                        rect.left.toFloat(),
                        rect.top.toFloat() - 4,  // Adjust as needed for padding.
                        textPaint
                    )
                }
            }
        }
    }
}
