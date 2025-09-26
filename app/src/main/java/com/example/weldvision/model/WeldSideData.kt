package com.example.weldvision.model

/*
 * Combined OCR context for the entire weld symbol:
 * arrow side + other side.
 */
data class WeldSideData(
    val angle: String,
    val dimensions: List<String>,
)