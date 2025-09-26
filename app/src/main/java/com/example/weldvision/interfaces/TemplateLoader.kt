package com.example.weldvision.interfaces

import android.content.Context
import com.example.weldvision.model.TemplateDefinition

interface TemplateLoader {
    fun loadTemplates(context: Context): List<TemplateDefinition>
}
