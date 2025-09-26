/*
* File:     VisualizationScreen.kt
* Date:     2025-03-03
* Author:   Zai Yang Liu
* Description:
*   An extension to GrooveLabelsCanvasCanvas.kt, this file contains a composable that draws groove features
*   based on the parameter passed.
* Parameters include: Arrow Groove, Other Groove, depth, root opening, Arrow angle, Other Angle
*   Welding grooves include:
*       Bevel Groove, Double Bevel Groove, V-Groove, Double V-Groove, J-Groove,
*       U-Groove, Double J-Groove, Double U-Groove.
*   The groove drawings also have labels of depth of preparation, root opening, groove angle and other angle.
*   The grooves are also relatively dynamic to what groove is being used.
*   The labels still need to be passed as a parameter in the future (after detection by the app).
*   Reminder: This is for the mobile devices in landscape mode
*/
package com.example.weldvision.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.example.weldvision.ui.theme.WeldVisionTheme

const val BEVEL_GROOVE = "Bevel"
const val V_GROOVE = "V"
const val J_GROOVE = "J"
const val U_GROOVE = "U"
const val BEVEL_AND_J_ANGLE = 45
const val V_AND_U_ANGLE = 90

const val ARROW_SIDE_LABEL = "Arrow Side"
const val OTHER_SIDE_LABEL = "Other Side"

//@Preview(
//    showBackground = true,
//    device = "spec:width=800dp,height=400dp,orientation=landscape"
//)
//@Composable
//fun GreetingPreview() {
//    WeldVisionTheme {
//        VisualizationScreen(
//            arrowGroove = "",
//            otherGroove = "U",
//            arrowDepth = 3.0,
//            otherDepth = 4.0,
//            rootOpening = 4.0,
//            arrowAngle = 30,
//            otherAngle = 40,
//            modifier = Modifier.fillMaxSize(),
//            onBack = {} // Empty lambda for preview
//        )
//    }
//}

@Composable
fun VisualizationScreen(
    arrowGroove: String?, otherGroove: String?, arrowDepth: Double?, otherDepth: Double?,
    rootOpening: Double?, arrowAngle: Int?, otherAngle: Int?,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        val textMeasurer = rememberTextMeasurer()
        Canvas(modifier = modifier.fillMaxSize()) {
            // Screen Dimensions
            val width = size.width
            val height = size.height
            // Groove Height
            val topPointsPosition = 0.3f
            val bottomPointsPosition = 0.5f
            /// root opening
            var rootOpeningOffset1 = 0.005f
            var rootOpeningOffset2 = 0.005f
            // if no root opening
            if (rootOpening == null || rootOpening.toFloat() == 0.0f) {
                rootOpeningOffset1 = 0.0f
                rootOpeningOffset2 = 0.0f
            }
            // where grooves "meet", pointy middle if no depth
            var depthFlatOffset = 0.0f
            // if there is depth, make the meet point flat
            if ((arrowDepth != null && arrowDepth != 0.0) || (otherDepth != null && otherDepth != 0.0)) {
                depthFlatOffset = 0.03f
            }
            //// angle groove adjustments if user gives values for better visualization
            // minimum and maximum angle visualization for only left side (1) or both left and right (2)
            val minAngle1 = 30
            val minAngle2 = 30
            val maxAngle1 = 60
            val maxAngle2 = 120
            /// angles for otherGroove if not null
            var x1RightTopAngleOffset = 0.0f
            var x2LeftTopAngleOffset = 0.0f
            // if otherGroove is Bevel or J
            if (otherGroove == BEVEL_GROOVE || otherGroove == J_GROOVE) {
                if (otherAngle != null && otherAngle != 0) {
                    // if angle is exceeds range, set to min/max size
                    if (otherAngle < minAngle1){
                        x1RightTopAngleOffset = (minAngle1.toFloat() / BEVEL_AND_J_ANGLE)
                    }
                    else if (otherAngle > maxAngle1){
                        x1RightTopAngleOffset = (maxAngle1.toFloat() / BEVEL_AND_J_ANGLE)
                    }
                    else {
                        x1RightTopAngleOffset = (otherAngle.toFloat() / BEVEL_AND_J_ANGLE)
                    }
                }
                else if (otherAngle == null || otherAngle == 0) {
                    x1RightTopAngleOffset = (BEVEL_AND_J_ANGLE.toFloat() / BEVEL_AND_J_ANGLE)
                }
            }
            // if otherGroove is V or U
            if (otherGroove == V_GROOVE || otherGroove == U_GROOVE) {
                if (otherAngle != null && otherAngle != 0) {
                    // if angle is exceeds range, set to min/max size
                    if (otherAngle < minAngle2){
                        x1RightTopAngleOffset = (minAngle2.toFloat() / V_AND_U_ANGLE)
                        x2LeftTopAngleOffset = (minAngle2.toFloat() / V_AND_U_ANGLE)
                    }
                    else if (otherAngle > maxAngle2){
                        x1RightTopAngleOffset = (maxAngle2.toFloat() / V_AND_U_ANGLE)
                        x2LeftTopAngleOffset = (maxAngle2.toFloat() / V_AND_U_ANGLE)
                    }
                    else {
                        x1RightTopAngleOffset = (otherAngle.toFloat() / V_AND_U_ANGLE)
                        x2LeftTopAngleOffset = (otherAngle.toFloat() / V_AND_U_ANGLE)
                    }
                }
                else if (otherAngle == null || otherAngle == 0) {
                    x1RightTopAngleOffset = (V_AND_U_ANGLE.toFloat() / V_AND_U_ANGLE)
                    x2LeftTopAngleOffset = (V_AND_U_ANGLE.toFloat() / V_AND_U_ANGLE)
                }
            }
            /// angles for arrowGroove if not null
            var x1RightBottomAngleOffset = 0.0f
            var x2LeftBottomAngleOffset = 0.0f
            // if arrowGroove is Bevel or J
            if (arrowGroove == BEVEL_GROOVE || arrowGroove == J_GROOVE) {
                if (arrowAngle != null && arrowAngle != 0) {
                    // if angle is exceeds range, set to min/max size
                    if (arrowAngle < minAngle1){
                        x1RightBottomAngleOffset = (minAngle1.toFloat() / BEVEL_AND_J_ANGLE)
                    }
                    else if (arrowAngle > maxAngle1){
                        x1RightBottomAngleOffset = (maxAngle1.toFloat() / BEVEL_AND_J_ANGLE)
                    }
                    else {
                        x1RightBottomAngleOffset = (arrowAngle.toFloat() / BEVEL_AND_J_ANGLE)
                    }
                }
                else if (arrowAngle == null || arrowAngle == 0) {
                    x1RightBottomAngleOffset = (BEVEL_AND_J_ANGLE.toFloat() / BEVEL_AND_J_ANGLE)
                }
            }
            // if arrowGroove is V or U
            if (arrowGroove == V_GROOVE || arrowGroove == U_GROOVE) {
                if (arrowAngle != null && arrowAngle != 0) {
                    // if angle is exceeds range, set to min/max size
                    if (arrowAngle < minAngle2){
                        x1RightBottomAngleOffset = (minAngle2.toFloat() / V_AND_U_ANGLE)
                        x2LeftBottomAngleOffset = (minAngle2.toFloat() / V_AND_U_ANGLE)
                    }
                    else if (arrowAngle > maxAngle2){
                        x1RightBottomAngleOffset = (maxAngle2.toFloat() / V_AND_U_ANGLE)
                        x2LeftBottomAngleOffset = (maxAngle2.toFloat() / V_AND_U_ANGLE)
                    }
                    else {
                        x1RightBottomAngleOffset = (arrowAngle.toFloat() / V_AND_U_ANGLE)
                        x2LeftBottomAngleOffset = (arrowAngle.toFloat() / V_AND_U_ANGLE)
                    }
                }
                else if (arrowAngle == null || arrowAngle == 0) {
                    x1RightBottomAngleOffset = (V_AND_U_ANGLE.toFloat() / V_AND_U_ANGLE)
                    x2LeftBottomAngleOffset = (V_AND_U_ANGLE.toFloat() / V_AND_U_ANGLE)
                }
            }

            // Left Groove Width = x1 (x-Position)
            val x1LeftPos = 0.2f - rootOpeningOffset1
            var x1RightTopPos = 0.5f - rootOpeningOffset1
            var x1RightBottomPos = 0.5f - rootOpeningOffset1
            var x1RightCenterTopPos =
                0.5f - rootOpeningOffset1           // grooves with center-right points (pointy to the right)
            var x1RightCenterBottomPos = 0.5f - rootOpeningOffset1
            var x1ControlRightCenterTopPos =
                0.5f - rootOpeningOffset1    // control points for bezier curves
            var x1ControlRightCenterBottomPos = 0.5f - rootOpeningOffset1
            // Right Groove Width = x2 (x-Position)
            var x2LeftTopPos = 0.5f + rootOpeningOffset2
            var x2leftBottomPos = 0.5f + rootOpeningOffset2
            val x2RightPos = 0.8f + rootOpeningOffset2
            var x2LeftCenterTopPos =
                0.5f + rootOpeningOffset2           // grooves with center-left points (pointy to the left)
            var x2LeftCenterBottomPos = 0.5f + rootOpeningOffset2
            var x2ControlLeftCenterTopPos =
                0.5f + rootOpeningOffset2    // control points for bezier curves
            var x2ControlLeftCenterBottomPos = 0.5f + rootOpeningOffset2

            /// based on arrowGroove and otherGroove values passed, determine groove type needed to be drawn
            //double V
            when (otherGroove) {
                BEVEL_GROOVE -> {
                    x1RightTopPos -= 0.05f * x1RightTopAngleOffset
                }

                V_GROOVE -> {
                    x1RightTopPos -= 0.05f * x1RightTopAngleOffset
                    x2LeftTopPos += 0.05f * x2LeftTopAngleOffset
                }

                J_GROOVE -> {
                    x1RightTopPos -= 0.05f * x1RightTopAngleOffset
                    x1ControlRightCenterTopPos -= 0.05f * x1RightTopAngleOffset
                }

                U_GROOVE -> {
                    x1RightTopPos -= 0.05f * x1RightTopAngleOffset
                    x1ControlRightCenterTopPos -= 0.05f * x1RightTopAngleOffset
                    x2LeftTopPos += 0.05f * x2LeftTopAngleOffset
                    x2ControlLeftCenterTopPos += 0.05f * x2LeftTopAngleOffset
                }
            }
            when (arrowGroove) {
                BEVEL_GROOVE -> {
                    x1RightBottomPos -= 0.05f * x1RightBottomAngleOffset
                }

                V_GROOVE -> {
                    x1RightBottomPos -= 0.05f * x1RightBottomAngleOffset
                    x2leftBottomPos += 0.05f * x2LeftBottomAngleOffset
                }

                J_GROOVE -> {
                    x1RightBottomPos -= 0.05f * x1RightBottomAngleOffset
                    x1ControlRightCenterBottomPos -= 0.05f * x1RightBottomAngleOffset
                }

                U_GROOVE -> {
                    x1RightBottomPos -= 0.05f * x1RightBottomAngleOffset
                    x1ControlRightCenterBottomPos -= 0.05f * x1RightBottomAngleOffset
                    x2leftBottomPos += 0.05f * x2LeftBottomAngleOffset
                    x2ControlLeftCenterBottomPos += 0.05f * x2LeftBottomAngleOffset
                }
            }

            // Left Groove point positions
            val topLeft1 = Offset(width * x1LeftPos, height * topPointsPosition)
            val topRight1 = Offset(width * x1RightTopPos, height * topPointsPosition)
            val bottomLeft1 = Offset(width * x1LeftPos, height * bottomPointsPosition)
            val bottomRight1 = Offset(width * x1RightBottomPos, height * bottomPointsPosition)
            val middleTopRight1 = Offset(
                width * x1RightCenterTopPos,
                height * (topPointsPosition + bottomPointsPosition - depthFlatOffset) / 2
            )
            val middleBottomRight1 = Offset(
                width * x1RightCenterBottomPos,
                height * (topPointsPosition + bottomPointsPosition + depthFlatOffset) / 2
            )
            val controlTopRight1 = Offset(
                width * x1ControlRightCenterTopPos,
                height * (topPointsPosition + bottomPointsPosition - depthFlatOffset) / 2
            )
            val controlBottomRight1 = Offset(
                width * x1ControlRightCenterBottomPos,
                height * (topPointsPosition + bottomPointsPosition + depthFlatOffset) / 2
            )
            // Right Groove point positions
            val topLeft2 = Offset(width * x2LeftTopPos, height * topPointsPosition)
            val topRight2 = Offset(width * x2RightPos, height * topPointsPosition)
            val bottomLeft2 = Offset(width * x2leftBottomPos, height * bottomPointsPosition)
            val bottomRight2 = Offset(width * x2RightPos, height * bottomPointsPosition)
            val middleTopLeft2 = Offset(
                width * x2LeftCenterTopPos,
                height * (topPointsPosition + bottomPointsPosition - depthFlatOffset) / 2
            )
            val middleBottomLeft2 = Offset(
                width * x2LeftCenterBottomPos,
                height * (topPointsPosition + bottomPointsPosition + depthFlatOffset) / 2
            )
            val controlTopLeft2 = Offset(
                width * x2ControlLeftCenterTopPos,
                height * (topPointsPosition + bottomPointsPosition - depthFlatOffset) / 2
            )
            val controlBottomLeft2 = Offset(
                width * x2ControlLeftCenterBottomPos,
                height * (topPointsPosition + bottomPointsPosition + depthFlatOffset) / 2
            )
            // Groove Shape pathing
            val path = Path().apply {
                moveTo(topRight1.x, topRight1.y)
                lineTo(topLeft1.x, topLeft1.y)
                lineTo(bottomLeft1.x, bottomLeft1.y)
                lineTo(bottomRight1.x, bottomRight1.y)
                if (arrowGroove == BEVEL_GROOVE || arrowGroove == V_GROOVE) {
                    lineTo(middleBottomRight1.x, middleBottomRight1.y)
                } else if (arrowGroove == J_GROOVE || arrowGroove == U_GROOVE) {
                    quadraticBezierTo(
                        controlBottomRight1.x, controlBottomRight1.y,
                        middleBottomRight1.x, middleBottomRight1.y
                    )
                } else {
                    lineTo(middleBottomRight1.x, middleBottomRight1.y)
                }

                lineTo(middleTopRight1.x, middleTopRight1.y)

                if (otherGroove == BEVEL_GROOVE || otherGroove == V_GROOVE) {
                    lineTo(topRight1.x, topRight1.y)
                } else if (otherGroove == J_GROOVE || otherGroove == U_GROOVE) {
                    lineTo(middleTopRight1.x, middleTopRight1.y)
                    quadraticBezierTo(
                        controlTopRight1.x, controlTopRight1.y,
                        topRight1.x, topRight1.y
                    )
                } else {
                    lineTo(topRight1.x, topRight1.y)
                }
                close()

                moveTo(topLeft2.x, topLeft2.y)
                lineTo(topRight2.x, topRight2.y)
                lineTo(bottomRight2.x, bottomRight2.y)
                lineTo(bottomLeft2.x, bottomLeft2.y)
                if (arrowGroove == V_GROOVE) {
                    lineTo(middleBottomLeft2.x, middleBottomLeft2.y)
                } else if (arrowGroove == U_GROOVE) {
                    quadraticBezierTo(
                        controlBottomLeft2.x, controlBottomLeft2.y,
                        middleBottomLeft2.x, middleBottomLeft2.y
                    )
                } else {
                    lineTo(middleBottomLeft2.x, middleBottomLeft2.y)
                }

                lineTo(middleTopLeft2.x, middleTopLeft2.y)

                if (otherGroove == V_GROOVE) {
                    lineTo(topLeft2.x, topLeft2.y)
                } else if (otherGroove == U_GROOVE) {
                    lineTo(middleTopLeft2.x, middleTopLeft2.y)
                    quadraticBezierTo(
                        controlTopLeft2.x, controlTopLeft2.y,
                        topLeft2.x, topLeft2.y
                    )
                } else {
                    lineTo(topLeft2.x, topLeft2.y)
                }
                close()
            }

            drawPath(path, color = Color.LightGray, style = Fill)
            drawPath(path, color = Color.Black, style = Stroke(width = 10f))

            val dashEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f), 0f)

            /// arrow depth of preparation labels
            if (arrowDepth != null && arrowDepth != 0.0) {
                val depthPointPos1 = Offset(middleBottomRight1.x, middleBottomRight1.y)
                val depthOfPreparationPath = Path().apply {
                    moveTo(width * (x1LeftPos - 0.1f), bottomRight1.y)
                    lineTo(bottomRight1.x, bottomRight1.y)

                    moveTo(width * (x1LeftPos - 0.1f), depthPointPos1.y)
                    lineTo(depthPointPos1.x, depthPointPos1.y)
                }
                drawPath(
                    depthOfPreparationPath,
                    color = Color(0xFFfcab10),
                    style = Stroke(width = 5f, pathEffect = dashEffect)
                )
                val depthOfPrepPosText = arrowDepth.toString()
                val dopTextLayoutResult = textMeasurer.measure(
                    text = AnnotatedString(depthOfPrepPosText),
                    style = TextStyle(
                        fontSize = 25.sp
                    )
                )
                val dopTextSize = dopTextLayoutResult.size
                val depOfPrepLabelPos = Offset(
                    width * 0.02f,
                    ((depthPointPos1.y + bottomRight1.y) - dopTextSize.height.toFloat()) / 2
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
                    color = Color(0xFFfcab10)
                )
            }

            /// other depth of preparation labels
            if (otherDepth != null && otherDepth != 0.0) {
                val depthPointPos1 = Offset(middleTopRight1.x, middleTopRight1.y)
                val depthOfPreparationPath = Path().apply {
                    moveTo(width * (x1LeftPos - 0.1f), topRight1.y)
                    lineTo(topRight1.x, topRight1.y)

                    moveTo(width * (x1LeftPos - 0.1f), depthPointPos1.y)
                    lineTo(depthPointPos1.x, depthPointPos1.y)
                }
                drawPath(
                    depthOfPreparationPath,
                    color = Color(0xFF800080),
                    style = Stroke(width = 5f, pathEffect = dashEffect)
                )
                val depthOfPrepPosText = otherDepth.toString()
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
                    color = Color(0xFF800080)
                )
            }

            /// root opening labels
            if (rootOpening != null && rootOpening != 0.0) {
                val rootPointPos1 = Offset(middleTopRight1.x, middleTopRight1.y)
                val rootPointPos2 = Offset(middleTopLeft2.x, middleTopLeft2.y)
                val rootOpeningPath = Path().apply {
                    moveTo(rootPointPos1.x, height * (bottomPointsPosition + 0.25f))
                    lineTo(rootPointPos1.x, rootPointPos1.y)

                    moveTo(rootPointPos2.x, height * (bottomPointsPosition + 0.25f))
                    lineTo(rootPointPos2.x, rootPointPos2.y)
                }
                drawPath(
                    rootOpeningPath,
                    color = Color(0xFF0000FF),
                    style = Stroke(width = 5f, pathEffect = dashEffect)
                )
                val rootOpeningPosText = rootOpening.toString()
                val rootTextLayoutResult = textMeasurer.measure(
                    text = AnnotatedString(rootOpeningPosText),
                    style = TextStyle(
                        fontSize = 25.sp
                    )
                )
                val rootTextSize = rootTextLayoutResult.size
                val rootOpeningLabelPos = Offset(
                    (width - (rootTextSize.width.toFloat())) / 2,
                    (height * (bottomPointsPosition + 0.25f) - (rootTextSize.height.toFloat()) / 2)
                )
                // white background for text
                drawRect(
                    color = Color.White,                                                       // White color for the background
                    size = Size(
                        rootTextSize.width.toFloat(),
                        rootTextSize.height.toFloat()
                    ),  // Size of the rectangle based on text size
                    topLeft = Offset(
                        rootOpeningLabelPos.x,
                        rootOpeningLabelPos.y
                    )             // Position of the rectangle
                )
                // text
                drawText(
                    textLayoutResult = rootTextLayoutResult,
                    topLeft = rootOpeningLabelPos,
                    color = Color(0xFF0000FF)
                )
            }

            /// arrow angle labels
            if ((arrowAngle != null && arrowAngle != 0) && arrowGroove != null) {
                val groove1OuterLeftPointPos = Offset(
                    bottomRight1.x - (middleBottomRight1.x - bottomRight1.x),
                    bottomRight1.y - (middleBottomRight1.y - bottomRight1.y)
                )
                val groove1InnerLeftPointPos = Offset(middleBottomRight1.x, middleBottomRight1.y)

                val groove1OuterRightPointPos = Offset(
                    bottomLeft2.x - (middleBottomLeft2.x - bottomLeft2.x),
                    bottomLeft2.y - (middleBottomLeft2.y - bottomLeft2.y)
                )

                val groove1InnerRightPointPos = Offset(middleBottomLeft2.x, middleBottomLeft2.y)

                val groove1AnglePath = Path().apply {
                    moveTo(groove1InnerLeftPointPos.x, groove1InnerLeftPointPos.y)
                    lineTo(groove1OuterLeftPointPos.x, groove1OuterLeftPointPos.y)

                    moveTo(groove1InnerRightPointPos.x, groove1InnerRightPointPos.y)
                    lineTo(groove1OuterRightPointPos.x, groove1OuterRightPointPos.y)
                }
                drawPath(
                    groove1AnglePath,
                    color = Color(0xFFd62828),
                    style = Stroke(width = 5f, pathEffect = dashEffect)
                )
                val groove1AnglePosText = arrowAngle.toString() + "°"
                val groove1TextLayoutResult = textMeasurer.measure(
                    text = AnnotatedString(groove1AnglePosText),
                    style = TextStyle(
                        fontSize = 25.sp
                    )
                )
                val groove1TextSize = groove1TextLayoutResult.size
                val groove1AngleLabelPos = Offset(
                    groove1OuterLeftPointPos.x,
                    groove1OuterLeftPointPos.y
                )
                // white background for text
                drawRect(
                    color = Color.White,                                                       // White color for the background
                    size = Size(
                        groove1TextSize.width.toFloat(),
                        groove1TextSize.height.toFloat()
                    ),  // Size of the rectangle based on text size
                    topLeft = Offset(
                        groove1AngleLabelPos.x,
                        groove1AngleLabelPos.y
                    )    // Position of the rectangle
                )
                // text
                drawText(
                    textLayoutResult = groove1TextLayoutResult,
                    topLeft = groove1AngleLabelPos,
                    color = Color(0xFFd62828)
                )
            }

            /// other angle labels
            if ((otherAngle != null && otherAngle != 0) && otherGroove != null) {
                val groove2OuterLeftPointPos = Offset(
                    topRight1.x - (middleTopRight1.x - topRight1.x),
                    topRight1.y - (middleTopRight1.y - topRight1.y)
                )
                val groove2InnerLeftPointPos = Offset(middleTopRight1.x, middleTopRight1.y)

                val groove2OuterRightPointPos = Offset(
                    topLeft2.x - (middleTopLeft2.x - topLeft2.x),
                    topLeft2.y - (middleTopLeft2.y - topLeft2.y)
                )

                val groove2InnerRightPointPos = Offset(middleTopLeft2.x, middleTopLeft2.y)

                val groove2AnglePath = Path().apply {
                    moveTo(groove2InnerLeftPointPos.x, groove2InnerLeftPointPos.y)
                    lineTo(groove2OuterLeftPointPos.x, groove2OuterLeftPointPos.y)

                    moveTo(groove2InnerRightPointPos.x, groove2InnerRightPointPos.y)
                    lineTo(groove2OuterRightPointPos.x, groove2OuterRightPointPos.y)
                }
                drawPath(
                    groove2AnglePath,
                    color = Color(0xFF008000),
                    style = Stroke(width = 5f, pathEffect = dashEffect)
                )
                val groove2AnglePosText = otherAngle.toString() + "°"
                val groove2TextLayoutResult = textMeasurer.measure(
                    text = AnnotatedString(groove2AnglePosText),
                    style = TextStyle(
                        fontSize = 25.sp
                    )
                )
                val groove2TextSize = groove2TextLayoutResult.size
                val groove2AngleLabelPos = Offset(
                    groove2OuterLeftPointPos.x,
                    groove2OuterLeftPointPos.y - 50
                )
                // white background for text
                drawRect(
                    color = Color.White,                                                       // White color for the background
                    size = Size(
                        groove2TextSize.width.toFloat(),
                        groove2TextSize.height.toFloat()
                    ),  // Size of the rectangle based on text size
                    topLeft = Offset(
                        groove2AngleLabelPos.x,
                        groove2AngleLabelPos.y
                    )    // Position of the rectangle
                )
                // text
                drawText(
                    textLayoutResult = groove2TextLayoutResult,
                    topLeft = groove2AngleLabelPos,
                    color = Color(0xFF008000)
                )

            }

            /// labels for indicating arrow and groove sides
            val arrowSideLabelText = ARROW_SIDE_LABEL
            val arrowSideLabelResult = textMeasurer.measure(
                text = AnnotatedString(arrowSideLabelText),
                style = TextStyle(
                    fontSize = 35.sp
                )
            )
            val arrowSideLabelPos = Offset(
                width * 0.02f,
                height - height * 0.35f
            )
            val otherSideLabelText = OTHER_SIDE_LABEL
            val otherSideLabelResult = textMeasurer.measure(
                text = AnnotatedString(otherSideLabelText),
                style = TextStyle(
                    fontSize = 35.sp
                )
            )
            val otherSideLabelPos = Offset(
                width * 0.02f,
                height * 0.05f
            )
            drawText(
                textLayoutResult = arrowSideLabelResult,
                topLeft = Offset(arrowSideLabelPos.x, arrowSideLabelPos.y),
                color = Color(0xFFd62828)
            )
            drawText(
                textLayoutResult = otherSideLabelResult,
                topLeft = Offset(otherSideLabelPos.x, otherSideLabelPos.y),
                color = Color(0xFF008000)
            )

        }
        Button(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text(text = "Back")
        }
    }
}