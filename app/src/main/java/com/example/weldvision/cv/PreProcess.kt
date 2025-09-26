package com.example.weldvision.cv

import com.example.weldvision.interfaces.ImagePreProcessor
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt

class PreProcess : ImagePreProcessor {

    override fun preProcess(image: Mat): Mat {
        return preProcessForTemplateMatching(image)
    }

    fun preProcessForTemplateMatching(src: Mat): Mat {
        val image = toGrayScale(src)

        // Align the image so that the longest nearly horizontal line becomes horizontal.
        val aligned = alignHorizontalLine(image)

        val currentWidth = aligned.width()
        return if (currentWidth > 1200) {
            // Compute the scaling ratio
            val ratio = 1200.0 / currentWidth
            val newHeight = (image.height() * ratio).toInt()

            // Resize
            val resized = Mat()
            Imgproc.resize(aligned, resized, Size(1200.0, newHeight.toDouble()), 0.0, 0.0, Imgproc.INTER_AREA)

            // Release the old Mat if you’re done with it
            aligned.release()
            resized
        } else {
            aligned
        }
    }

    private fun alignHorizontalLine(src: Mat): Mat {
        // 1. Detect edges using Canny.
        val edges = Mat()
        Imgproc.Canny(src, edges, 50.0, 150.0)

        // 2. Detect lines using the Probabilistic Hough Transform.
        val lines = Mat()
        Imgproc.HoughLinesP(edges, lines, 1.0, Math.PI / 180, 50, 50.0, 10.0)

        // Data class to hold line information.
        data class LineInfo(val pt1: Point, val pt2: Point, val length: Double, val angle: Double)

        var longestLine: LineInfo? = null
        for (i in 0 until lines.rows()) {
            val line = lines.get(i, 0) // Each line is represented as [x1, y1, x2, y2].
            val x1 = line[0]
            val y1 = line[1]
            val x2 = line[2]
            val y2 = line[3]
            val dx = x2 - x1
            val dy = y2 - y1
            val length = sqrt(dx * dx + dy * dy)
            // Compute the angle in degrees.
            val angle = Math.toDegrees(atan2(dy, dx))
            // Consider only lines within ±10° of horizontal.
            if (abs(angle) <= 10.0) {
                if (longestLine == null || length > longestLine.length) {
                    longestLine = LineInfo(Point(x1, y1), Point(x2, y2), length, angle)
                }
            }
        }

        // If no appropriate line is found, return the original image.
        if (longestLine == null) {
            return src
        }

        // 3. Compute the rotation angle (negative of the detected line's angle).
        val rotationAngle = -longestLine.angle
        // 4. Get the center of the image.
        val center = Point((src.cols() / 2).toDouble(), (src.rows() / 2).toDouble())
        // 5. Compute the rotation matrix.
        val rotationMatrix = Imgproc.getRotationMatrix2D(center, rotationAngle, 1.0)
        // 6. Rotate the image using warpAffine.
        val rotated = Mat()
        Imgproc.warpAffine(src, rotated, rotationMatrix, src.size(), Imgproc.INTER_LINEAR)
        return rotated
    }

    private fun toGrayScale(src: Mat): Mat {
        val gray = Mat()
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGRA2GRAY)
        return gray
    }

    fun applyGaussianBlur(src: Mat): Mat {
        val blurred = Mat()
        Imgproc.GaussianBlur(src, blurred, Size(5.0, 5.0), 0.0)
        return blurred
    }

    fun applyThreshold(src: Mat): Mat {
        val thresholded = Mat()
        Imgproc.threshold(src, thresholded, 0.0, 255.0, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU)
        return thresholded
    }

    fun applyAdaptiveThreshold(src: Mat, blockSize: Int = 11, C: Double = 2.0): Mat {
        val thresholded = Mat()
        // Ensure blockSize is an odd number and greater than 1
        Imgproc.adaptiveThreshold(
            src,
            thresholded,
            255.0,
            Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
            Imgproc.THRESH_BINARY,
            blockSize,
            C
        )
        return thresholded
    }

}