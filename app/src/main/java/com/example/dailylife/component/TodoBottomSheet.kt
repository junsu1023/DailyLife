package com.example.dailylife.component

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.dailylife.R
import com.example.dailylife.util.convertDrawableToBitMap
import com.example.data.entitiy.TodoEntity
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoBottomSheet(
    modifier: Modifier = Modifier,
    closeSheet: () -> Unit,
    onSaveTodo: (TodoEntity) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        delay(200)
        focusRequester.requestFocus()
    }

    ModalBottomSheet(
        modifier = Modifier,
        onDismissRequest = closeSheet,
        dragHandle = null,
        sheetState = sheetState,
        tonalElevation = 0.dp,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        containerColor = colorResource(R.color.alabaster)
    ) {
        Column(
            modifier = modifier
                .focusRequester(focusRequester)
                .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp)
        ) {
            var text by remember { mutableStateOf("") }

            TextField(
                text = text,
                onValueChanged = { text = it },
                placeholderText = stringResource(R.string.input_new_job),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            Icon(
                painter = painterResource(R.drawable.check_icon),
                contentDescription = null,
                tint = colorResource(R.color.forest_green),
                modifier = Modifier
                    .align(Alignment.End)
                    .size(40.dp)
                    .padding(end = 10.dp)
                    .clickable(enabled = true, onClick = {
                        closeSheet()
                        onSaveTodo(
                            makeTodoItem(context, R.drawable.default_icon, text)
                        )
                    })
            )
        }
    }
}

private fun makeTodoItem(
    context: Context,
    @DrawableRes resId: Int,
    title: String
): TodoEntity {
    val curTime = System.currentTimeMillis()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
    val dueDate = dateFormat.format(curTime)

    return TodoEntity(
        dueDate = dueDate,
        isComplete = false,
        icon = convertDrawableToBitMap(context, resId),
        title = title
    )
}