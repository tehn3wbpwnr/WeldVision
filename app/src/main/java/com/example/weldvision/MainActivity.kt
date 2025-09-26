package com.example.weldvision

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.weldvision.ui.components.AppNavigation
import com.example.weldvision.ui.theme.WeldVisionTheme

// MainActivity is the entry point of the app
class MainActivity : ComponentActivity() {
    companion object {
        init {
            try {
                // Manually load the OpenCV native library.
                System.loadLibrary("opencv_java4")
                Log.d("OpenCV", "OpenCV native library loaded successfully!")
            } catch (e: UnsatisfiedLinkError) {
                Log.e("OpenCV", "Failed to load OpenCV native library: ${e.message}")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeldVisionTheme {
                AppNavigation()
            }
        }
    }
}