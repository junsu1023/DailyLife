package com.example.dailylife.component

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailylife.R
import com.example.dailylife.util.convertDrawableToBitMap
import com.example.dailylife.util.noRippleClick
import com.example.dailylife.util.roundRippleClickable
import com.example.dailylife.viewmodel.TodoViewModel
import com.example.data.entitiy.TodoEntity

@Composable
fun IconSelectorContainer(
    todoItem: TodoEntity,
    todoViewModel: TodoViewModel,
    selectorContainerOffset: Pair<Offset, Offset>,
    topBarHeight: Float,
    headerHeight: Float,
    iconSelectorContainerSize: IntSize,
    maxHeight: Float,
    callBackContainerSize: (IntSize) -> Unit,
    isShowSelectContainer: (Boolean) -> Unit
) {
    val iconTint = mutableListOf(
        R.color.dark_red,
        R.color.yellow,
        R.color.dark_green,
        R.color.steal_blue,
        R.color.blue_violet
    )
    val iconList = mutableListOf(
        R.drawable.number1,
        R.drawable.number2,
        R.drawable.number3,
        R.drawable.number4,
        R.drawable.number5
    )
    val context = LocalContext.current
    val density = LocalDensity.current

    val bottomLeftOffset = selectorContainerOffset.first
    val topLeftOffset = selectorContainerOffset.second
    val bottomEndOffsetY = (bottomLeftOffset.y - topBarHeight + headerHeight) + iconSelectorContainerSize.height.toFloat()

    val offsetX = with(density) { (bottomLeftOffset.x - iconSelectorContainerSize.width.toFloat() / 2).toDp() }
    val offsetY = with(density) {
        if(bottomEndOffsetY >= maxHeight) (topLeftOffset.y - iconSelectorContainerSize.height.toFloat() - headerHeight).toDp()
        else (bottomLeftOffset.y - topBarHeight + headerHeight).toDp()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .noRippleClick(
                onClick = { isShowSelectContainer(false) }
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = offsetX, y = offsetY)
        ) {
            Column(
                modifier = Modifier
                    .width(100.dp)
                    .padding(horizontal = 5.dp)
                    .background(
                        color = colorResource(R.color.white),
                        shape = RoundedCornerShape(20)
                    )
                    .onGloballyPositioned { layoutCoordinates ->
                        val size = layoutCoordinates.size
                        callBackContainerSize(size)
                    }
            ) {
                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = stringResource(R.string.select_icon),
                    style = TextStyle(
                        fontSize = 10.sp,
                        color = colorResource(R.color.black),
                        fontWeight = FontWeight.Bold
                    )
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(colorResource(R.color.platinum))
                )

                Spacer(modifier = Modifier.height(5.dp))

                Row {
                    Spacer(modifier = Modifier.width(10.dp))

                    LazyColumn {
                        itemsIndexed(iconTint) { idx, tint ->
                            IconBox(
                                context = context,
                                todoViewModel = todoViewModel,
                                todoItem = todoItem,
                                drawableResId = R.drawable.flag,
                                colorResId = tint
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    LazyColumn {
                        itemsIndexed(iconTint) { idx, tint ->
                            IconBox(
                                context = context,
                                todoViewModel = todoViewModel,
                                todoItem = todoItem,
                                drawableResId = iconList[idx],
                                colorResId = tint
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IconBox(
    context: Context,
    todoItem: TodoEntity,
    todoViewModel: TodoViewModel,
    @DrawableRes drawableResId: Int,
    @ColorRes colorResId: Int
) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .padding(5.dp)
    ) {
        Icon(
            painter = painterResource(drawableResId),
            contentDescription = null,
            tint = colorResource(colorResId),
            modifier = Modifier
                .size(25.dp)
                .roundRippleClickable(
                    rippleColor = colorResource(colorResId),
                    onClick = {
                        val convertBitmap = convertDrawableToBitMap(context, drawableResId)
                        val updateTodoItem = todoItem.copy(
                            icon = convertBitmap,
                            iconColor = colorResId
                        )
                        todoViewModel.updateTodoList(updateTodoItem)
                    }
                )
        )
    }
}