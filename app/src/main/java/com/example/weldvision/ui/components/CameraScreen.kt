package com.example.weldvision.ui.components

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.weldvision.viewmodel.ImageProcessingViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weldvision.model.Detection
import androidx.navigation.NavController


@Composable
fun CameraScreen(
    navController: NavController,
    processingViewModel: ImageProcessingViewModel = viewModel(),
    onNavigateToVisualization: () -> Unit
) {
    val context = LocalContext.current
    var hasCameraPermission by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            hasCameraPermission = true
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if (hasCameraPermission) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Camera preview in the background.
            CameraPreviewWithOverlay(
                navController = navController,
                onPhotoCaptured = { uri ->
                processingViewModel.analyzeCapturedImage(uri)
            })

            // Detection results overlay at the top. commented out for demo
//            val detections = processingViewModel.detectionResults.value
//            Column(
//                modifier = Modifier
//                    .align(Alignment.TopStart)
//                    .padding(16.dp)
//                    .background(Color(0x80000000)) // semi-transparent background for readability
//            ) {
//                if (detections.isNotEmpty()) {
//                    val filteredDetections = detections.filter { it.confidence > 0.75 }
//                    val bestUniqueDetections = filteredDetections
//                        .groupBy { it.templateName }
//                        .mapNotNull { (_, group) -> group.maxByOrNull { it.confidence } }
//                        .sortedByDescending { it.confidence }
//
//                    if (bestUniqueDetections.isNotEmpty()) {
//                        bestUniqueDetections.forEach { detection ->
//                            Text(
//                                text = "Template: ${detection.templateName} (Scale: ${detection.scale}) - " +
//                                        "Score: ${"%.4f".format(detection.confidence)} at " +
//                                        "(${detection.rect.x}, ${detection.rect.y})",
//                                color = Color.White,
//                                modifier = Modifier.padding(vertical = 4.dp)
//                            )
//                        }
//                    } else {
//                        Text(
//                            text = "No detections above threshold",
//                            color = Color.White,
//                            modifier = Modifier.padding(vertical = 4.dp)
//                        )
//                    }
//                } else {
//                    Text(
//                        text = "No detections available",
//                        color = Color.White,
//                        modifier = Modifier.padding(vertical = 4.dp)
//                    )
//                }
//            }

            // OCR recognized text overlay at the bottom. commented out for demo
//            val recognizedTexts = processingViewModel.recognizedTextResults.value
//            Column(
//                modifier = Modifier
//                    .align(Alignment.BottomStart)
//                    .padding(16.dp)
//                    .background(Color(0x80000000))
//            ) {
//                if (recognizedTexts.isNotEmpty()) {
//                    recognizedTexts.forEach { recognizedText ->
//                        Text(
//                            text = "OCR: ${recognizedText.text}",
//                            color = Color.White,
//                            modifier = Modifier.padding(vertical = 4.dp)
//                        )
//                    }
//                } else {
//                    Text(
//                        text = "No text recognized",
//                        color = Color.White,
//                        modifier = Modifier.padding(vertical = 4.dp)
//                    )
//                }
//            }

            // Groove data overlay at the top right.
//            val groove = processingViewModel.grooveData.value
//            if (groove != null) {
//                Box(
//                    modifier = Modifier
//                        .align(Alignment.TopEnd)
//                        .padding(16.dp)
//                        .background(Color(0x80000000))
//                ) {
//                    Text(
//                        text = "Groove Data:\nArrow: ${groove.arrowGroove}\nOther: ${groove.otherGroove}\n" +
//                                "Arrow Angle: ${groove.arrowAngle}\nOther Angle: ${groove.otherAngle}",
//                        color = Color.White,
//                        modifier = Modifier.padding(8.dp)
//                    )
//                }
//            }
            // Show loading overlay while processing
            LoadingOverlay(isVisible = processingViewModel.isLoading.value)


            // Navigate when the groove data becomes available.
            LaunchedEffect(processingViewModel.grooveData.value) {
                if (processingViewModel.grooveData.value != null) {
                    onNavigateToVisualization()
                }
            }
        }
    }
}

