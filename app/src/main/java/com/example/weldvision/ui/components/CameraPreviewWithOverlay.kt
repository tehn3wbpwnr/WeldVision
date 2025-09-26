package com.example.weldvision.ui.components

import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import coil.compose.AsyncImage
import com.example.weldvision.utils.*
import androidx.navigation.NavController

// CameraPreviewWithOverlay is a Composable that displays a camera preview with an overlay
@Composable
fun CameraPreviewWithOverlay(
    navController: NavController,
    onPhotoCaptured: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var previewView: PreviewView? by remember { mutableStateOf(null) }

    // Create an ImageCapture use case for taking photos.
    val imageCapture = remember {
        ImageCapture.Builder().build()
    }

    // Set up the camera preview and bind both Preview and ImageCapture use cases.
    LaunchedEffect(previewView) {
        previewView?.let { pv ->
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(pv.surfaceProvider)
                }
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        context as LifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                } catch (exc: Exception) {
                    Log.e("CameraPreview", "Error binding camera use cases", exc)
                }
            }, ContextCompat.getMainExecutor(context))
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Camera preview
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).also { pv ->
                    previewView = pv
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Overlay drawing
        Canvas(modifier = Modifier.fillMaxSize()) {
            val overlayColor = Color.Black.copy(alpha = 0.8f)
            val overlayBorderColor = Color.White.copy(alpha = 0.3f)
            val boundingBoxWidth = size.width * 0.75f
            val boundingBoxHeight = size.height * 0.75f
            val left = (size.width - boundingBoxWidth) / 2f
            val top = (size.height - boundingBoxHeight) / 2f
            val right = left + boundingBoxWidth
            val bottom = top + boundingBoxHeight

            // Fill outside the bounding box.
            drawRect(
                color = overlayColor,
                topLeft = Offset(0f, 0f),
                size = androidx.compose.ui.geometry.Size(size.width, top)
            )
            drawRect(
                color = overlayColor,
                topLeft = Offset(0f, bottom),
                size = androidx.compose.ui.geometry.Size(size.width, size.height - bottom)
            )
            drawRect(
                color = overlayColor,
                topLeft = Offset(0f, top),
                size = androidx.compose.ui.geometry.Size(left, boundingBoxHeight)
            )
            drawRect(
                color = overlayColor,
                topLeft = Offset(right, top),
                size = androidx.compose.ui.geometry.Size(size.width - right, boundingBoxHeight)
            )

            // "Disconnected" outline along the edges.
            val gapSize = 40.dp.toPx()
            val strokeWidth = 4.dp.toPx()
            val centerX = left + boundingBoxWidth / 2f
            val centerY = top + boundingBoxHeight / 2f

            // TOP side segments
            drawLine(
                color = overlayBorderColor,
                start = Offset(left, top),
                end = Offset(centerX - gapSize / 8f, top),
                strokeWidth = strokeWidth
            )
            drawLine(
                color = overlayBorderColor,
                start = Offset(centerX + gapSize / 8f, top),
                end = Offset(right, top),
                strokeWidth = strokeWidth
            )

            // BOTTOM side segments
            drawLine(
                color = overlayBorderColor,
                start = Offset(left, bottom),
                end = Offset(centerX - gapSize / 8f, bottom),
                strokeWidth = strokeWidth
            )
            drawLine(
                color = overlayBorderColor,
                start = Offset(centerX + gapSize / 8f, bottom),
                end = Offset(right, bottom),
                strokeWidth = strokeWidth
            )

            // LEFT side segments
            drawLine(
                color = overlayBorderColor,
                start = Offset(left, top),
                end = Offset(left, centerY - gapSize / 8f),
                strokeWidth = strokeWidth
            )
            drawLine(
                color = overlayBorderColor,
                start = Offset(left, centerY + gapSize / 8f),
                end = Offset(left, bottom),
                strokeWidth = strokeWidth
            )

            // RIGHT side segments
            drawLine(
                color = overlayBorderColor,
                start = Offset(right, top),
                end = Offset(right, centerY - gapSize / 8f),
                strokeWidth = strokeWidth
            )
            drawLine(
                color = overlayBorderColor,
                start = Offset(right, centerY + gapSize / 8f),
                end = Offset(right, bottom),
                strokeWidth = strokeWidth
            )

            // Horizontal reference line across the center.
            drawLine(
                color = Color.Green.copy(alpha = 0.5f),
                start = Offset(left, centerY),
                end = Offset(right, centerY),
                strokeWidth = 5.dp.toPx()
            )

            // Draw text just above the reference line.
            val largeTextPaint = android.graphics.Paint().apply {
                color = android.graphics.Color.WHITE
                alpha = (255 * 0.6).toInt() // 60% opacity
                textSize = 18.dp.toPx()
                textAlign = android.graphics.Paint.Align.CENTER
                isAntiAlias = true
            }
            val textY = centerY - 20.dp.toPx()
            drawContext.canvas.nativeCanvas.drawText(
                "Place your weld symbol here",
                centerX,
                textY,
                largeTextPaint
            )
        }

        Row(
            //horizontalArrangement = Arrangement.spacedBy(80.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
        {
            // manual button
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(
                    onClick = {
                        navController.navigate("confirmation") // Navigate to ConfirmationScreen
                    },
                    modifier = Modifier
                        .padding(start = 2.dp, bottom = 24.dp)
                ) {
                    Text("Manual Mode", fontSize = 18.sp)
                }
            }


            // "Take Photo" button.
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(
                    onClick = {
                        takePhoto(context, imageCapture) { uri ->
                            onPhotoCaptured(uri)
                        }
                    },
                    modifier = Modifier
                        .padding(end = 2.dp, bottom = 24.dp)
                ) {
                    Text("Take Photo", fontSize = 18.sp)
                }
            }
        }
    }
}
