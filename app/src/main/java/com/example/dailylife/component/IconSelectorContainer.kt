package com.example.dailylife.component

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailylife.R

@Composable
fun IconSelectorContainer(
    callBackContainerWidth: (Float) -> Unit
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
    ) // 테스트 위한 ImageVector, Icon으로 변경할 것.

    Column(
        modifier = Modifier
            .width(100.dp)
            .padding(horizontal = 5.dp)
            .background(
                color = colorResource(R.color.white),
                shape = RoundedCornerShape(20)
            )
            .onGloballyPositioned { layoutCoordinates ->
                val width = layoutCoordinates.size.width.toFloat()
                println("test-kjs: width = $width")
                callBackContainerWidth(width)
            }
    ) {
        Text(
            modifier = Modifier.padding(start = 10.dp),
            text = stringResource(R.string.select_icon),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(colorResource(R.color.black))
        )

        Spacer(modifier = Modifier.height(5.dp))

        Row {
            Spacer(modifier = Modifier.width(10.dp))

            LazyColumn {
                itemsIndexed(iconTint) { idx, tint ->
                    IconBox(
                        drawableResId = R.drawable.flag,
                        colorResId = tint
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            LazyColumn {
                itemsIndexed(iconTint) { idx, tint ->
                    IconBox(
                        drawableResId = iconList[idx],
                        colorResId = tint
                    )
                }
            }
        }
    }
}

@Composable
fun IconBox(
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
                .clickable(
                    enabled = true,
                    onClick = {
                        /*
                        Todo
                        선택한 아이콘으로 Todo data class의 icon을 변경해야함
                         */
                    }
                )
        )
    }
}