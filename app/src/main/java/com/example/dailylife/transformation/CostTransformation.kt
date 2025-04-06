package com.example.dailylife.transformation

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CostTransformation: VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText = costFilter(text)

    private fun costFilter(text: AnnotatedString): TransformedText {
        val out = if(text.text.isNotEmpty()) "${text.text}원" else text.text

        val numberOffsetTranslator = object: OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = offset

            override fun transformedToOriginal(offset: Int): Int = when {
                offset < 1 -> offset
                else -> offset - 1
            }
        }

        return TransformedText(AnnotatedString(out), numberOffsetTranslator)
    }
}