package com.example.weldvision

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import android.util.Log
import org.junit.Test
import org.junit.runner.RunWith
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.junit.Assert.assertNotNull

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun testOpenCV() {
        // For demonstration: load the native OpenCV library if necessary
        System.loadLibrary("opencv_java4")

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        assertNotNull(context)

        val testMat = Mat(5, 5, CvType.CV_8UC1)
        Log.d("OpenCVTest", "Matrix = $testMat")

        // Add your asserts
        // ...
        assert(true)
    }
}
