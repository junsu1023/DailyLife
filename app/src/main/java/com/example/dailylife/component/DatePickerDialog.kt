package com.example.dailylife.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.dailylife.R
import com.example.dailylife.util.roundRippleClickable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDatePickerDialog(
    selectedDate: String?,
    onClickCancel: () -> Unit,
    onClickConfirm: (String) -> Unit
) {
    DatePickerDialog(
        onDismissRequest = { onClickCancel() },
        confirmButton = { },
        colors = DatePickerDefaults.colors(
            containerColor = colorResource(R.color.white)
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        val datePickerState = rememberDatePickerState(
            yearRange = 1800 .. 3000,
            initialDisplayMode = DisplayMode.Picker,
            initialSelectedDateMillis = selectedDate?.let {
                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }
                formatter.parse(it)?.time ?: System.currentTimeMillis()
            } ?: System.currentTimeMillis(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return true
                }
            }
        )

        DatePicker(state = datePickerState)

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.cancel),
                modifier = Modifier.roundRippleClickable(
                    rippleColor = colorResource(R.color.gray_asparagus),
                    onClick = onClickCancel
                )
            )

            Text(
                text = stringResource(R.string.ok),
                modifier = Modifier.roundRippleClickable(
                    rippleColor = colorResource(R.color.gray_asparagus),
                    onClick = {
                        datePickerState.selectedDateMillis?.let { selectedDateMillis ->
                            val date = SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.getDefault()
                            ).format(Date(selectedDateMillis))

                            onClickConfirm(date)
                        }
                    }
                )
            )

            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}