/*
* File:     GroovesCanvas.kt
* Date:     2025-02-10
* Author:   Zai Yang Liu
* Description:
*   This file contains composable functions that draws simple groove shapes features
*   Welding grooves include:
*       Square Groove, Bevel Groove, Double Bevel Groove, V-Groove, Double V-Groove, J-Groove,
*       U-Groove, Double J-Groove, Double U-Groove, Flare Bevel Groove, Flare V-Groove.
*   Reminder: This is for the mobile devices in landscape mode
*/

package com.example.weldvision.ui.visualization

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun SquareGrooveCanvas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // left trapezium points
        val topLeft1 = Offset(width * 0.2f, height * 0.3f)
        val topRight1 = Offset(width * 0.5f, height * 0.3f)
        val bottomLeft1 = Offset(width * 0.2f, height * 0.5f)
        val bottomRight1 = Offset(width * 0.5f, height * 0.5f)

        // right trapezium points
        val topLeft2 = Offset(width * 0.5f, height * 0.3f)
        val topRight2 = Offset(width * 0.8f, height * 0.3f)
        val bottomLeft2 = Offset(width * 0.5f, height * 0.5f)
        val bottomRight2 = Offset(width * 0.8f, height * 0.5f)

        val path = Path().apply {
            // Left trapezium
            moveTo(topLeft1.x, topLeft1.y)
            lineTo(bottomLeft1.x, bottomLeft1.y)
            lineTo(bottomRight1.x, bottomRight1.y)
            lineTo(topRight1.x, topRight1.y)
            close()

            // Left trapezium
            moveTo(topLeft2.x, topLeft2.y)
            lineTo(bottomLeft2.x, bottomLeft2.y)
            lineTo(bottomRight2.x, bottomRight2.y)
            lineTo(topRight2.x, topRight2.y)
            close()

        }
        drawPath(
            path = path,
            color = Color.Gray,
            style = Fill
        )
        drawPath(
            path = path,
            color = Color.Black, // Simulating metal
            style = Stroke(width = 10f)
        )
    }
}

@Composable
fun BevelGrooveCanvas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // left trapezium points
        val topLeft1 = Offset(width * 0.2f, height * 0.3f)
        val topRight1 = Offset(width * 0.4f, height * 0.3f)
        val bottomLeft1 = Offset(width * 0.2f, height * 0.5f)
        val bottomRight1 = Offset(width * 0.5f, height * 0.5f)

        // right trapezium points
        val topLeft2 = Offset(width * 0.5f, height * 0.3f)
        val topRight2 = Offset(width * 0.8f, height * 0.3f)
        val bottomLeft2 = Offset(width * 0.5f, height * 0.5f)
        val bottomRight2 = Offset(width * 0.8f, height * 0.5f)

        val path = Path().apply {
            // Left trapezium
            moveTo(topLeft1.x, topLeft1.y)
            lineTo(bottomLeft1.x, bottomLeft1.y)
            lineTo(bottomRight1.x, bottomRight1.y)
            lineTo(topRight1.x, topRight1.y)
            close()

            // Left trapezium
            moveTo(topLeft2.x, topLeft2.y)
            lineTo(bottomLeft2.x, bottomLeft2.y)
            lineTo(bottomRight2.x, bottomRight2.y)
            lineTo(topRight2.x, topRight2.y)
            close()

        }
        drawPath(
            path = path,
            color = Color.Gray,
            style = Fill
        )
        drawPath(
            path = path,
            color = Color.Black, // Simulating metal
            style = Stroke(width = 10f)
        )
    }
}

@Composable
fun DoubleBevelGrooveCanvas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // left trapezium points
        val topLeft1 = Offset(width * 0.2f, height * 0.3f)
        val topRight1 = Offset(width * 0.45f, height * 0.3f)
        val bottomLeft1 = Offset(width * 0.2f, height * 0.5f)
        val bottomRight1 = Offset(width * 0.45f, height * 0.5f)
        val middleRight1 = Offset(width * 0.5f, height * 0.4f)

        // right trapezium points
        val topLeft2 = Offset(width * 0.5f, height * 0.3f)
        val topRight2 = Offset(width * 0.8f, height * 0.3f)
        val bottomLeft2 = Offset(width * 0.5f, height * 0.5f)
        val bottomRight2 = Offset(width * 0.8f, height * 0.5f)

        val path = Path().apply {
            // Left trapezium
            moveTo(topLeft1.x, topLeft1.y)
            lineTo(bottomLeft1.x, bottomLeft1.y)
            lineTo(bottomRight1.x, bottomRight1.y)
            lineTo(middleRight1.x, middleRight1.y)
            lineTo(topRight1.x, topRight1.y)
            close()

            // Left trapezium
            moveTo(topLeft2.x, topLeft2.y)
            lineTo(bottomLeft2.x, bottomLeft2.y)
            lineTo(bottomRight2.x, bottomRight2.y)
            lineTo(topRight2.x, topRight2.y)
            close()

        }
        drawPath(
            path = path,
            color = Color.Gray,
            style = Fill
        )
        drawPath(
            path = path,
            color = Color.Black, // Simulating metal
            style = Stroke(width = 10f)
        )
    }
}

@Composable
fun VGrooveCanvas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // left trapezium points
        val topLeft1 = Offset(width * 0.2f, height * 0.3f)
        val topRight1 = Offset(width * 0.4f, height * 0.3f)
        val bottomLeft1 = Offset(width * 0.2f, height * 0.5f)
        val bottomRight1 = Offset(width * 0.5f, height * 0.5f)

        // right trapezium points
        val topLeft2 = Offset(width * 0.6f, height * 0.3f)
        val topRight2 = Offset(width * 0.8f, height * 0.3f)
        val bottomLeft2 = Offset(width * 0.5f, height * 0.5f)
        val bottomRight2 = Offset(width * 0.8f, height * 0.5f)

        val path = Path().apply {
            // Left trapezium
            moveTo(topLeft1.x, topLeft1.y)
            lineTo(bottomLeft1.x, bottomLeft1.y)
            lineTo(bottomRight1.x, bottomRight1.y)
            lineTo(topRight1.x, topRight1.y)
            close()

            // Left trapezium
            moveTo(topLeft2.x, topLeft2.y)
            lineTo(bottomLeft2.x, bottomLeft2.y)
            lineTo(bottomRight2.x, bottomRight2.y)
            lineTo(topRight2.x, topRight2.y)
            close()

        }
        drawPath(
            path = path,
            color = Color.Gray,
            style = Fill
        )
        drawPath(
            path = path,
            color = Color.Black, // Simulating metal
            style = Stroke(width = 10f)
        )
    }
}

@Composable
fun DoubleVGrooveCanvas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // left trapezium points
        val topLeft1 = Offset(width * 0.2f, height * 0.3f)
        val topRight1 = Offset(width * 0.45f, height * 0.3f)
        val bottomLeft1 = Offset(width * 0.2f, height * 0.5f)
        val bottomRight1 = Offset(width * 0.45f, height * 0.5f)
        val middleRight1 = Offset(width * 0.5f, height * 0.4f)

        // right trapezium points
        val topLeft2 = Offset(width * 0.55f, height * 0.3f)
        val topRight2 = Offset(width * 0.8f, height * 0.3f)
        val bottomLeft2 = Offset(width * 0.55f, height * 0.5f)
        val bottomRight2 = Offset(width * 0.8f, height * 0.5f)
        val middleLeft2 = Offset(width * 0.5f, height * 0.4f)

        val path = Path().apply {
            // Left trapezium
            moveTo(topLeft1.x, topLeft1.y)
            lineTo(bottomLeft1.x, bottomLeft1.y)
            lineTo(bottomRight1.x, bottomRight1.y)
            lineTo(middleRight1.x, middleRight1.y)
            lineTo(topRight1.x, topRight1.y)
            close()

            // Left trapezium
            moveTo(topLeft2.x, topLeft2.y)
            lineTo(middleLeft2.x, middleLeft2.y)
            lineTo(bottomLeft2.x, bottomLeft2.y)
            lineTo(bottomRight2.x, bottomRight2.y)
            lineTo(topRight2.x, topRight2.y)
            close()

        }
        drawPath(
            path = path,
            color = Color.Gray,
            style = Fill
        )
        drawPath(
            path = path,
            color = Color.Black, // Simulating metal
            style = Stroke(width = 10f)
        )
    }
}

@Composable
fun JGrooveCanvas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // left trapezium points
        val topLeft1 = Offset(width * 0.2f, height * 0.3f)
        val topRight1 = Offset(width * 0.425f, height * 0.3f)
        val bottomLeft1 = Offset(width * 0.2f, height * 0.5f)
        val bottomRight1 = Offset(width * 0.5f, height * 0.5f)
        val centerRight1 = Offset(width * 0.5f, height * 0.45f)
        val controlRight1 = Offset(width * 0.425f, height * 0.45f)

        // right trapezium points
        val topLeft2 = Offset(width * 0.5f, height * 0.3f)
        val topRight2 = Offset(width * 0.8f, height * 0.3f)
        val bottomLeft2 = Offset(width * 0.5f, height * 0.5f)
        val bottomRight2 = Offset(width * 0.8f, height * 0.5f)

        val path = Path().apply {
            // Left trapezium
            moveTo(topLeft1.x, topLeft1.y)
            lineTo(bottomLeft1.x, bottomLeft1.y)
            lineTo(bottomRight1.x, bottomRight1.y)
            lineTo(centerRight1.x, centerRight1.y)
            quadraticBezierTo(controlRight1.x, controlRight1.y,
                topRight1.x, topRight1.y)
            close()

            // Left trapezium
            moveTo(topLeft2.x, topLeft2.y)
            lineTo(bottomLeft2.x, bottomLeft2.y)
            lineTo(bottomRight2.x, bottomRight2.y)
            lineTo(topRight2.x, topRight2.y)
            close()

        }
        drawPath(
            path = path,
            color = Color.Gray,
            style = Fill
        )
        drawPath(
            path = path,
            color = Color.Black, // Simulating metal
            style = Stroke(width = 10f)
        )
    }
}

@Composable
fun UGrooveCanvas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // left trapezium points
        val topLeft1 = Offset(width * 0.2f, height * 0.3f)
        val topRight1 = Offset(width * 0.425f, height * 0.3f)
        val bottomLeft1 = Offset(width * 0.2f, height * 0.5f)
        val bottomRight1 = Offset(width * 0.5f, height * 0.5f)
        val centerRight1 = Offset(width * 0.5f, height * 0.45f)
        val controlRight1 = Offset(width * 0.425f, height * 0.45f)

        // right trapezium points
        val topLeft2 = Offset(width * 0.575f, height * 0.3f)
        val topRight2 = Offset(width * 0.8f, height * 0.3f)
        val bottomLeft2 = Offset(width * 0.5f, height * 0.5f)
        val bottomRight2 = Offset(width * 0.8f, height * 0.5f)
        val centerLeft2 = Offset(width * 0.5f, height * 0.45f)
        val controlLeft2 = Offset(width * 0.575f, height * 0.45f)

        val path = Path().apply {
            // Left trapezium
            moveTo(topLeft1.x, topLeft1.y)
            lineTo(bottomLeft1.x, bottomLeft1.y)
            lineTo(bottomRight1.x, bottomRight1.y)
            lineTo(centerRight1.x, centerRight1.y)
            quadraticBezierTo(controlRight1.x, controlRight1.y,
                topRight1.x, topRight1.y)
            close()

            // Left trapezium
            moveTo(topLeft2.x, topLeft2.y)
            quadraticBezierTo(controlLeft2.x, controlLeft2.y,
                centerLeft2.x, centerLeft2.y)
            lineTo(bottomLeft2.x, bottomLeft2.y)
            lineTo(bottomRight2.x, bottomRight2.y)
            lineTo(topRight2.x, topRight2.y)
            close()

        }
        drawPath(
            path = path,
            color = Color.Gray,
            style = Fill
        )
        drawPath(
            path = path,
            color = Color.Black, // Simulating metal
            style = Stroke(width = 10f)
        )
    }
}

@Composable
fun DoubleJGrooveCanvas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // left trapezium points
        val topLeft1 = Offset(width * 0.2f, height * 0.3f)
        val topRight1 = Offset(width * 0.467f, height * 0.3f)
        val bottomLeft1 = Offset(width * 0.2f, height * 0.5f)
        val bottomRight1 = Offset(width * 0.467f, height * 0.5f)
        val centerTopRight1 = Offset(width * 0.5f, height * 0.367f)
        val centerBottomRight1 = Offset(width * 0.5f, height * 0.433f)
        val controlTopRight1 = Offset(width * 0.467f, height * 0.367f)
        val controlBottomRight1 = Offset(width * 0.467f, height * 0.433f)

        // right trapezium points
        val topLeft2 = Offset(width * 0.5f, height * 0.3f)
        val topRight2 = Offset(width * 0.8f, height * 0.3f)
        val bottomLeft2 = Offset(width * 0.5f, height * 0.5f)
        val bottomRight2 = Offset(width * 0.8f, height * 0.5f)

        val path = Path().apply {
            // Left trapezium
            moveTo(topLeft1.x, topLeft1.y)
            lineTo(bottomLeft1.x, bottomLeft1.y)
            lineTo(bottomRight1.x, bottomRight1.y)
            quadraticBezierTo(controlBottomRight1.x, controlBottomRight1.y,
                centerBottomRight1.x, centerBottomRight1.y)
            lineTo(centerTopRight1.x, centerTopRight1.y)
            quadraticBezierTo(controlTopRight1.x, controlTopRight1.y,
                topRight1.x, topRight1.y)
            close()

            // Left trapezium
            moveTo(topLeft2.x, topLeft2.y)
            lineTo(bottomLeft2.x, bottomLeft2.y)
            lineTo(bottomRight2.x, bottomRight2.y)
            lineTo(topRight2.x, topRight2.y)
            close()

        }
        drawPath(
            path = path,
            color = Color.Gray,
            style = Fill
        )
        drawPath(
            path = path,
            color = Color.Black, // Simulating metal
            style = Stroke(width = 10f)
        )
    }
}

@Composable
fun DoubleUGrooveCanvas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // left trapezium points
        val topLeft1 = Offset(width * 0.2f, height * 0.3f)
        val topRight1 = Offset(width * 0.467f, height * 0.3f)
        val bottomLeft1 = Offset(width * 0.2f, height * 0.5f)
        val bottomRight1 = Offset(width * 0.467f, height * 0.5f)
        val centerTopRight1 = Offset(width * 0.5f, height * 0.367f)
        val centerBottomRight1 = Offset(width * 0.5f, height * 0.433f)
        val controlTopRight1 = Offset(width * 0.467f, height * 0.367f)
        val controlBottomRight1 = Offset(width * 0.467f, height * 0.433f)

        // right trapezium points
        val topLeft2 = Offset(width * 0.533f, height * 0.3f)
        val topRight2 = Offset(width * 0.8f, height * 0.3f)
        val bottomLeft2 = Offset(width * 0.533f, height * 0.5f)
        val bottomRight2 = Offset(width * 0.8f, height * 0.5f)
        val centerTopLeft2 = Offset(width * 0.5f, height * 0.367f)
        val centerBottomLeft2 = Offset(width * 0.5f, height * 0.433f)
        val controlTopLeft2 = Offset(width * 0.533f, height * 0.367f)
        val controlBottomLeft2 = Offset(width * 0.533f, height * 0.433f)

        val path = Path().apply {
            // Left trapezium
            moveTo(topLeft1.x, topLeft1.y)
            lineTo(bottomLeft1.x, bottomLeft1.y)
            lineTo(bottomRight1.x, bottomRight1.y)
            quadraticBezierTo(controlBottomRight1.x, controlBottomRight1.y,
                centerBottomRight1.x, centerBottomRight1.y)
            lineTo(centerTopRight1.x, centerTopRight1.y)
            quadraticBezierTo(controlTopRight1.x, controlTopRight1.y,
                topRight1.x, topRight1.y)
            close()

            // Left trapezium
            moveTo(topLeft2.x, topLeft2.y)
            quadraticBezierTo(controlTopLeft2.x, controlTopLeft2.y,
                centerTopLeft2.x, centerTopLeft2.y)
            lineTo(centerBottomLeft2.x, centerBottomLeft2.y)
            quadraticBezierTo(controlBottomLeft2.x, controlBottomLeft2.y,
                bottomLeft2.x, bottomLeft2.y)
            lineTo(bottomRight2.x, bottomRight2.y)
            lineTo(topRight2.x, topRight2.y)
            close()

        }
        drawPath(
            path = path,
            color = Color.Gray,
            style = Fill
        )
        drawPath(
            path = path,
            color = Color.Black, // Simulating metal
            style = Stroke(width = 10f)
        )
    }
}

@Composable
fun FlareBevelGrooveCanvas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // left trapezium points
        val topLeft1 = Offset(width * 0.2f, height * 0.3f)
        val topRight1 = Offset(width * 0.4f, height * 0.3f)
        val bottomLeft1 = Offset(width * 0.2f, height * 0.5f)
        val bottomRight1 = Offset(width * 0.5f, height * 0.5f)
        val controlRight1 = Offset(width * 0.5f, height * 0.3f)

        // right trapezium points
        val topLeft2 = Offset(width * 0.5f, height * 0.3f)
        val topRight2 = Offset(width * 0.8f, height * 0.3f)
        val bottomLeft2 = Offset(width * 0.5f, height * 0.5f)
        val bottomRight2 = Offset(width * 0.8f, height * 0.5f)

        val path = Path().apply {
            // Left trapezium
            moveTo(topLeft1.x, topLeft1.y)
            lineTo(bottomLeft1.x, bottomLeft1.y)
            lineTo(bottomRight1.x, bottomRight1.y)
            quadraticBezierTo(controlRight1.x, controlRight1.y,
                topRight1.x, topRight1.y)
            close()

            // Left trapezium
            moveTo(topLeft2.x, topLeft2.y)
            lineTo(bottomLeft2.x, bottomLeft2.y)
            lineTo(bottomRight2.x, bottomRight2.y)
            lineTo(topRight2.x, topRight2.y)
            close()

        }
        drawPath(
            path = path,
            color = Color.Gray,
            style = Fill
        )
        drawPath(
            path = path,
            color = Color.Black, // Simulating metal
            style = Stroke(width = 10f)
        )
    }
}

@Composable
fun FlareVGrooveCanvas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // left trapezium points
        val topLeft1 = Offset(width * 0.2f, height * 0.3f)
        val topRight1 = Offset(width * 0.4f, height * 0.3f)
        val bottomLeft1 = Offset(width * 0.2f, height * 0.5f)
        val bottomRight1 = Offset(width * 0.5f, height * 0.5f)
        val controlRight1 = Offset(width * 0.5f, height * 0.3f)

        // right trapezium points
        val topLeft2 = Offset(width * 0.6f, height * 0.3f)
        val topRight2 = Offset(width * 0.8f, height * 0.3f)
        val bottomLeft2 = Offset(width * 0.5f, height * 0.5f)
        val bottomRight2 = Offset(width * 0.8f, height * 0.5f)
        val controlLeft2 = Offset(width * 0.5f, height * 0.3f)

        val path = Path().apply {
            // Left trapezium
            moveTo(topLeft1.x, topLeft1.y)
            lineTo(bottomLeft1.x, bottomLeft1.y)
            lineTo(bottomRight1.x, bottomRight1.y)
            quadraticBezierTo(controlRight1.x, controlRight1.y,
                topRight1.x, topRight1.y)
            close()

            // Left trapezium
            moveTo(topLeft2.x, topLeft2.y)
            quadraticBezierTo(controlLeft2.x, controlLeft2.y,
                bottomLeft2.x, bottomLeft2.y)
            lineTo(bottomRight2.x, bottomRight2.y)
            lineTo(topRight2.x, topRight2.y)
            close()

        }
        drawPath(
            path = path,
            color = Color.Gray,
            style = Fill
        )
        drawPath(
            path = path,
            color = Color.Black, // Simulating metal
            style = Stroke(width = 10f)
        )
    }
}