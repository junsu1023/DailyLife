package com.example.dailylife.transformation

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class DateTransformation: VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText = dateFilter(text)

    private fun dateFilter(text: AnnotatedString): TransformedText {
        val trimmed = if(text.text.length >= 8) text.text.substring(0 until 8) else text.text

        var out = ""

        for(i in trimmed.indices) {
            out += trimmed[i]
            if(i == 3 || i == 5) out += '-'
        }

        val numberOffsetTranslator = object: OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = when {
                offset <= 3 -> offset
                offset <= 5 -> offset + 1
                offset <= 8 -> offset + 2
                else -> 10
            }

            override fun transformedToOriginal(offset: Int): Int = when {
                offset <= 2 -> offset
                offset <= 5 -> offset - 1
                offset <= 10 -> offset - 2
                else -> 8
            }
        }

        return TransformedText(AnnotatedString(out), numberOffsetTranslator)
    }
}