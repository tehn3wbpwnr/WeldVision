/*
* File:     GrooveLabelsCanvas.kt
* Date:     2025-02-16
* Author:   Zai Yang Liu
* Description:
*   An extension to GroovesCanvas.kt, this file contains a composable that draws groove features
*   based on the parameter passed from (0 to 10)
*   Welding grooves include:
*       Square Groove, Bevel Groove, Double Bevel Groove, V-Groove, Double V-Groove, J-Groove,
*       U-Groove, Double J-Groove, Double U-Groove, Flare Bevel Groove, Flare V-Groove.
*   The groove drawings also have labels of depth of preparation, root opening, and groove angle.
*   The grooves are also relatively dynamic to what groove is being used.
*   The labels still need to be passed as a parameter in the future (after detection by the app).
*   Reminder: This is for the mobile devices in landscape mode
*/
package com.example.weldvision.ui.visualization

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp

@Composable
fun LabelGrooveCanvas(grooveType: Int, modifier: Modifier = Modifier) {
    val textMeasurer = rememberTextMeasurer()
    Canvas(modifier = modifier.fillMaxSize()) {
        // Screen Dimensions
        val width = size.width
        val height = size.height
        // Groove Height
        val topPointsPosition = 0.3f
        val bottomPointsPosition = 0.5f
        // Left Groove Width = x1 (x-Position)
        val rootOpeningOffset1 = 0.02f
        val x1LeftPos = 0.2f - rootOpeningOffset1
        var x1RightTopPos = 0.5f - rootOpeningOffset1
        var x1RightBottomPos = 0.5f - rootOpeningOffset1
        var x1RightCenterTopPos = 0.5f - rootOpeningOffset1           // grooves with center-right points (pointy to the right)
        var x1RightCenterBottomPos = 0.5f - rootOpeningOffset1
        var x1ControlRightCenterTopPos = 0.5f - rootOpeningOffset1    // control points for bezier curves
        var x1ControlRightCenterBottomPos = 0.5f - rootOpeningOffset1
        // conditional positions based on groove type
        when (grooveType) {
            0 -> Unit
            1 -> x1RightTopPos -= 0.1f
            2 -> {
                x1RightTopPos -= 0.05f
                x1RightBottomPos -= 0.05f
            }
            3 -> x1RightTopPos -= 0.1f
            4 -> {
                x1RightTopPos -= 0.05f
                x1RightBottomPos -= 0.05f
            }
            5 -> {
                x1RightTopPos -= 0.05f
                x1ControlRightCenterTopPos -= 0.05f
            }
            6 -> {
                x1RightTopPos -= 0.05f
                x1ControlRightCenterTopPos -= 0.05f
            }
            7 -> {
                x1RightTopPos -= 0.05f
                x1RightBottomPos -= 0.05f
                x1ControlRightCenterTopPos -= 0.05f
                x1ControlRightCenterBottomPos -= 0.05f
            }
            8 -> {
                x1RightTopPos -= 0.05f
                x1RightBottomPos -= 0.05f
                x1ControlRightCenterTopPos -= 0.05f
                x1ControlRightCenterBottomPos -= 0.05f
            }
            9 -> x1RightTopPos -= 0.1f
            10 -> x1RightTopPos -= 0.1f
        }
        // Right Groove Width = x2 (x-Position)
        val rootOpeningOffset2 = 0.02f
        var x2LeftTopPos = 0.5f + rootOpeningOffset2
        var x2leftBottomPos = 0.5f + rootOpeningOffset2
        val x2RightPos = 0.8f + rootOpeningOffset2
        var x2LeftCenterTopPos = 0.5f + rootOpeningOffset2           // grooves with center-left points (pointy to the left)
        var x2LeftCenterBottomPos = 0.5f + rootOpeningOffset2
        var x2ControlLeftCenterTopPos = 0.5f + rootOpeningOffset2    // control points for bezier curves
        var x2ControlLeftCenterBottomPos = 0.5f + rootOpeningOffset2
        // conditional positions based on groove type
        when (grooveType) {
            0 -> Unit
            1 -> Unit
            2 -> Unit
            3 -> x2LeftTopPos += 0.1f
            4 -> {
                x2LeftTopPos += 0.05f
                x2leftBottomPos += 0.05f
            }
            5 -> Unit
            6 -> {
                x2LeftTopPos += 0.05f
                x2ControlLeftCenterTopPos += 0.05f
            }
            7 -> Unit
            8 -> {
                x2LeftTopPos += 0.05f
                x2leftBottomPos += 0.05f
                x2ControlLeftCenterTopPos += 0.05f
                x2ControlLeftCenterBottomPos += 0.05f
            }
            9 -> Unit
            10 -> x2LeftTopPos += 0.1f
        }

        // Left Groove point positions
        val topLeft1 = Offset(width * x1LeftPos, height * topPointsPosition)
        val topRight1 = Offset(width * x1RightTopPos, height * topPointsPosition)
        val bottomLeft1 = Offset(width * x1LeftPos, height * bottomPointsPosition)
        val bottomRight1 = Offset(width * x1RightBottomPos, height * bottomPointsPosition)
        val middleTopRight1 = Offset(width * x1RightCenterTopPos, height * (topPointsPosition+bottomPointsPosition)/2)
        val middleBottomRight1 = Offset(width * x1RightCenterBottomPos, height * (topPointsPosition+bottomPointsPosition)/2)
        val controlTopRight1 = Offset(width * x1ControlRightCenterTopPos, height * (topPointsPosition+bottomPointsPosition)/2)
        val controlBottomRight1 = Offset(width * x1ControlRightCenterBottomPos, height * (topPointsPosition+bottomPointsPosition)/2)
        // Right Groove point positions
        val topLeft2 = Offset(width * x2LeftTopPos, height * topPointsPosition)
        val topRight2 = Offset(width * x2RightPos, height * topPointsPosition)
        val bottomLeft2 = Offset(width * x2leftBottomPos, height * bottomPointsPosition)
        val bottomRight2 = Offset(width * x2RightPos, height * bottomPointsPosition)
        val middleTopLeft2 = Offset(width * x2LeftCenterTopPos, height * (topPointsPosition+bottomPointsPosition)/2)
        val middleBottomLeft2 = Offset(width * x2LeftCenterBottomPos, height * (topPointsPosition+bottomPointsPosition)/2)
        val controlTopLeft2 = Offset(width * x2ControlLeftCenterTopPos, height * (topPointsPosition+bottomPointsPosition)/2)
        val controlBottomLeft2 = Offset(width * x2ControlLeftCenterBottomPos, height * (topPointsPosition+bottomPointsPosition)/2)
        // Groove Shape pathing
        val path = Path().apply {
            moveTo(topRight1.x, topRight1.y)
            lineTo(topLeft1.x, topLeft1.y)
            lineTo(bottomLeft1.x, bottomLeft1.y)
            lineTo(bottomRight1.x, bottomRight1.y)
            if (grooveType==2 || grooveType==4) {
                lineTo(middleTopRight1.x, middleTopRight1.y)
            }
            else if (grooveType==5 || grooveType==6) {
                lineTo(middleTopRight1.x, middleTopRight1.y)
                quadraticBezierTo(controlTopRight1.x, controlTopRight1.y,
                    topRight1.x, topRight1.y)
            }
            else if (grooveType==7 || grooveType==8) {
                quadraticBezierTo(controlBottomRight1.x, controlBottomRight1.y,
                    middleBottomRight1.x, middleBottomRight1.y)
                lineTo(middleTopRight1.x, middleTopRight1.y)
                quadraticBezierTo(controlTopRight1.x, controlTopRight1.y,
                    topRight1.x, topRight1.y)
            }
            else if (grooveType==9 || grooveType==10) {
                quadraticBezierTo(controlTopRight1.x, controlTopRight1.y,
                    topRight1.x, topRight1.y)
            }
            close()

            moveTo(topLeft2.x, topLeft2.y)
            lineTo(topRight2.x, topRight2.y)
            lineTo(bottomRight2.x, bottomRight2.y)
            lineTo(bottomLeft2.x, bottomLeft2.y)
            if ( grooveType==4) {
                lineTo(middleTopLeft2.x, middleTopLeft2.y)
            }
            else if (grooveType==6) {
                lineTo(middleTopLeft2.x, middleTopLeft2.y)
                quadraticBezierTo(controlTopLeft2.x, controlTopLeft2.y,
                    topLeft2.x, topLeft2.y)
            }
            else if (grooveType==8) {
                quadraticBezierTo(controlBottomLeft2.x, controlBottomLeft2.y,
                    middleBottomLeft2.x, middleBottomLeft2.y)
                lineTo(middleTopLeft2.x, middleTopLeft2.y)
                quadraticBezierTo(controlTopLeft2.x, controlTopLeft2.y,
                    topLeft2.x, topLeft2.y)
            }
            else if (grooveType==10) {
                quadraticBezierTo(controlTopLeft2.x, controlTopLeft2.y,
                    topLeft2.x, topLeft2.y)
            }
            close()
        }

        drawPath(path, color = Color.LightGray, style = Fill)
        drawPath(path, color = Color.Black, style = Stroke(width = 10f))

        val dashEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f), 0f)

        /// depth of preparation labels
        if (grooveType in 1..10) {
            val depthPointPos1 = when (grooveType) {
                0, 1, 3, 9, 10 -> Offset(bottomRight1.x, bottomRight1.y)
                2, 4, 5, 6, 7, 8 -> Offset(middleTopRight1.x, middleTopRight1.y)
                else -> Offset(bottomRight1.x, bottomRight1.y)
            }
            val depthOfPreparationPath = Path().apply {
                moveTo(width * (x1LeftPos - 0.1f), topRight1.y)
                lineTo(topRight1.x, topRight1.y)

                moveTo(width * (x1LeftPos - 0.1f), depthPointPos1.y)
                lineTo(depthPointPos1.x, depthPointPos1.y)
            }
            drawPath(
                depthOfPreparationPath,
                color = Color(0xFFFF5F1F),
                style = Stroke(width = 5f, pathEffect = dashEffect)
            )
            val depthOfPrepPosText = "5/8"
            val dopTextLayoutResult = textMeasurer.measure(
                text = AnnotatedString(depthOfPrepPosText),
                style = TextStyle(
                    fontSize = 25.sp
                )
            )
            val dopTextSize = dopTextLayoutResult.size
            val depOfPrepLabelPos = Offset(
                width * 0.02f,
                ((depthPointPos1.y + topRight1.y) - dopTextSize.height.toFloat()) / 2
            )
            // white background for text
            drawRect(
                color = Color.White,                                                   // White color for the background
                size = Size(
                    dopTextSize.width.toFloat(),
                    dopTextSize.height.toFloat()
                ),// Size of the rectangle based on text size
                topLeft = Offset(
                    depOfPrepLabelPos.x,
                    depOfPrepLabelPos.y + 10f
                )    // Position of the rectangle
            )
            // text
            drawText(
                textLayoutResult = dopTextLayoutResult,
                topLeft = Offset(depOfPrepLabelPos.x, depOfPrepLabelPos.y),
                color = Color(0xFFFF5F1F)
            )
        }

        /// root opening labels
        val rootPointPos1 = when (grooveType) {
            0,1,3,9,10 -> Offset(bottomRight1.x, bottomRight1.y)
            2,4,5,6,7,8 -> Offset(middleTopRight1.x, middleTopRight1.y)
            else -> Offset(bottomRight1.x, bottomRight1.y)
        }
        val rootPointPos2 = when (grooveType) {
            0, 1, 2, 3, 5, 7, 9, 10 -> Offset(bottomLeft2.x, bottomLeft2.y)
            4, 6, 8 -> Offset(middleTopLeft2.x, middleTopLeft2.y)
            else -> Offset(bottomLeft2.x, bottomLeft2.y)
        }
        val rootOpeningPath = Path().apply {
            moveTo(rootPointPos1.x, height * (bottomPointsPosition + 0.1f))
            lineTo(rootPointPos1.x, rootPointPos1.y)

            moveTo(rootPointPos2.x, height * (bottomPointsPosition + 0.1f))
            lineTo(rootPointPos2.x, rootPointPos2.y)
        }
        drawPath(
            rootOpeningPath,
            color = Color(0xFF0000FF),
            style = Stroke(width = 5f, pathEffect = dashEffect)
        )
        val rootOpeningPosText = "1/8"
        val rootTextLayoutResult  = textMeasurer.measure(
            text = AnnotatedString(rootOpeningPosText),
            style = TextStyle(
                fontSize = 25.sp
            )
        )
        val rootTextSize = rootTextLayoutResult.size
        val rootOpeningLabelPos = Offset (
            (width - (rootTextSize.width.toFloat()))/2,
            (height * (bottomPointsPosition + 0.2f) - (rootTextSize.height.toFloat())/2)
        )
        // white background for text
        drawRect(
            color = Color.White,                                                       // White color for the background
            size = Size(rootTextSize.width.toFloat(), rootTextSize.height.toFloat()),  // Size of the rectangle based on text size
            topLeft = Offset(rootOpeningLabelPos.x, rootOpeningLabelPos.y)             // Position of the rectangle
        )
        // text
        drawText(
            textLayoutResult = rootTextLayoutResult,
            topLeft = rootOpeningLabelPos,
            color = Color(0xFF0000FF)
        )

        /// groove angle labels
        if (grooveType in 1..10) {
            val grooveOuterLeftPointPos = when (grooveType) {
                1,3,9,10 -> Offset(topRight1.x - (bottomRight1.x - topRight1.x)/2, topRight1.y - (bottomRight1.y - topRight1.y)/2)
                2,4,5,6,7,8 -> Offset(topRight1.x - (middleTopRight1.x - topRight1.x), topRight1.y - (middleTopRight1.y - topRight1.y))
                else -> Offset(topRight1.x - (bottomRight1.x - topRight1.x), topRight1.y - (bottomRight1.y - topRight1.y))
            }
            val grooveInnerLeftPointPos = when (grooveType) {
                1,3,9,10 -> Offset(bottomRight1.x, bottomRight1.y)
                2,4,5,6,7,8 -> Offset(middleTopRight1.x, middleTopRight1.y)
                else -> Offset(bottomRight1.x, bottomRight1.y)
            }
            val grooveOuterRightPointPos = when (grooveType) {
                1,2,5,7,9 -> Offset(topLeft2.x - (bottomLeft2.x - topLeft2.x), topLeft2.y - (bottomLeft2.y - topLeft2.y))
                3 -> Offset(topLeft2.x - (bottomLeft2.x - topLeft2.x)/2, topLeft2.y - (bottomLeft2.y - topLeft2.y)/2)
                4,6,8 -> Offset(topLeft2.x - (middleTopLeft2.x - topLeft2.x), topLeft2.y - (middleTopLeft2.y - topLeft2.y))
                10 -> Offset(topLeft2.x - (bottomLeft2.x - topLeft2.x)/2, topLeft2.y - (bottomLeft2.y - topLeft2.y)/2)
                else -> Offset(topLeft2.x - (bottomLeft2.x - topLeft2.x), topLeft2.y - (bottomLeft2.y - topLeft2.y))
            }
            val grooveInnerRightPointPos = when (grooveType) {
                1,3,9,10 -> Offset(bottomLeft2.x, bottomLeft2.y)
                2,4,5,6,7,8 -> Offset(middleTopLeft2.x, middleTopLeft2.y)
                else -> Offset(bottomLeft2.x, bottomLeft2.y)
            }
            val grooveAnglePath = Path().apply {
                moveTo(grooveInnerLeftPointPos.x, grooveInnerLeftPointPos.y)
                lineTo(grooveOuterLeftPointPos.x, grooveOuterLeftPointPos.y)

                moveTo(grooveInnerRightPointPos.x, grooveInnerRightPointPos.y)
                lineTo(grooveOuterRightPointPos.x, grooveOuterRightPointPos.y)
            }
            drawPath(
                grooveAnglePath,
                color = Color(0xFF008000),
                style = Stroke(width = 5f, pathEffect = dashEffect)
            )
            val grooveAnglePosText = "60°"
            val grooveTextLayoutResult  = textMeasurer.measure(
                text = AnnotatedString(grooveAnglePosText),
                style = TextStyle(
                    fontSize = 25.sp
                )
            )
            val grooveTextSize = rootTextLayoutResult.size
            val grooveAngleLabelPos = when (grooveType) {
                3,4,6,8,10 -> Offset((width - (grooveTextSize.width.toFloat()))/2, height * topPointsPosition/2)
                1,2,5,7,9 -> Offset((width - (grooveTextSize.width.toFloat())/2 - (grooveOuterRightPointPos.x - grooveOuterLeftPointPos.x))/2,
                    height * topPointsPosition/2)
                else -> Offset((width - (grooveTextSize.width.toFloat()))/2, height * topPointsPosition/2)
            }
            // white background for text
            drawRect(
                color = Color.White,                                                       // White color for the background
                size = Size(grooveTextSize.width.toFloat(), grooveTextSize.height.toFloat()),  // Size of the rectangle based on text size
                topLeft = Offset(grooveAngleLabelPos.x, grooveAngleLabelPos.y)    // Position of the rectangle
            )
            // text
            drawText(
                textLayoutResult = grooveTextLayoutResult,
                topLeft = grooveAngleLabelPos,
                color = Color(0xFF008000)
            )
        }
    }

}