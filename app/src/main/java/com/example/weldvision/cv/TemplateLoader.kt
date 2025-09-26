package com.example.weldvision.cv

import android.content.Context
import android.graphics.BitmapFactory
import com.example.weldvision.interfaces.TemplateLoader
import org.opencv.android.Utils
import org.opencv.core.Mat
import com.example.weldvision.model.TemplateDefinition

class TemplateLoaderImpl : TemplateLoader {
    override fun loadTemplates(context: Context): List<TemplateDefinition> {
        return listOf(
            TemplateDefinition("ArrowV", loadMat(context, "ArrowV.png")),
            TemplateDefinition("OtherV", loadMat(context, "OtherV.png")),
            TemplateDefinition("ArrowJ", loadMat(context, "ArrowJ.png")),
            TemplateDefinition("OtherJ", loadMat(context, "OtherJ.png")),
            TemplateDefinition("ArrowU", loadMat(context, "ArrowU.png")),
            TemplateDefinition("OtherU", loadMat(context, "OtherU.png")),
            TemplateDefinition("ArrowBevel", loadMat(context, "ArrowBevel.png")),
            TemplateDefinition("OtherBevel", loadMat(context, "OtherBevel.png"))
        )
    }

    fun loadMat(context: Context, filename: String): Mat {
        val assetManager = context.assets
        val path = "TestImages/CroppedTemplates/$filename"
        assetManager.open(path).use { inputStream ->
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val mat = Mat()
            // Convert the Bitmap to a Mat using OpenCV's built-in function.
            Utils.bitmapToMat(bitmap, mat)
            return mat
        }
    }
}