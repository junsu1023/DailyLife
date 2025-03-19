package com.example.dailylife.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailylife.R

@Composable
fun IconSelectorContainer() {
    val iconList = mutableListOf<ImageVector>(
        Icons.Filled.Clear,
        Icons.Filled.ExpandLess,
        Icons.Filled.ExpandMore,
        Icons.Filled.Cancel,
        Icons.Filled.Brush
    ) // 테스트 위한 ImageVector, Icon으로 변경할 것.

    Column(
        modifier = Modifier
            .width(180.dp)
            .wrapContentHeight()
            .background(
                color = colorResource(R.color.white),
                shape = RoundedCornerShape(20)
            )
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

        LazyRow {
            item {
                Spacer(modifier = Modifier.width(5.dp))
            }

            itemsIndexed(iconList) { idx, icon ->
                IconBox(icon)

                if(idx != iconList.size) {
                    Spacer(modifier = Modifier.width(5.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.width(5.dp))
            }
        }
    }
}

@Composable
fun IconBox(
    icon: ImageVector // 테스트 위한 ImageVector, Icon으로 변경할 것.
) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .padding(5.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
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