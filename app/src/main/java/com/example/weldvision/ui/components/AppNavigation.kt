package com.example.weldvision.ui.components

import com.example.weldvision.viewmodel.ImageProcessingViewModelFactory
import android.app.Application
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weldvision.viewmodel.ImageProcessingViewModel


@Composable
fun AppNavigation() {
    val context = LocalContext.current.applicationContext as Application
    val viewModel: ImageProcessingViewModel = viewModel(
        factory = ImageProcessingViewModelFactory(context)
    )

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "camera") {
        composable(route = "camera") {
            CameraScreen(
                navController = navController,
                processingViewModel = viewModel,
                onNavigateToVisualization = { navController.navigate("confirmation") }
            )
        }

        composable( route = "confirmation") {
            ConfirmationScreen(
                processingViewModel = viewModel,
                onConfirm = { navController.navigate("visualization") },
                onCancel = {
                    viewModel.grooveData.value = null
                    navController.popBackStack()
                }
            )
        }

        composable(route = "visualization") {
            val grooveData = viewModel.grooveData.value
            VisualizationScreen(
                arrowGroove = grooveData?.arrowGroove,
                otherGroove = grooveData?.otherGroove,
                arrowDepth = grooveData?.arrowDepth,
                otherDepth = grooveData?.otherDepth,
                rootOpening = grooveData?.rootOpening,
                arrowAngle = grooveData?.arrowAngle,
                otherAngle = grooveData?.otherAngle,
                modifier = Modifier.fillMaxSize(),
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
