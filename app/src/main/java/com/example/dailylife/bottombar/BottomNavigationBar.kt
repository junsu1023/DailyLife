package com.example.dailylife.bottombar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.dailylife.R
import com.example.dailylife.navigation.DailyLifeItem

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    containerColor: Color,
    contentColor: Color,
    navController: NavHostController
) {
    val items = listOf(
        DailyLifeItem.TodoList,
        DailyLifeItem.Calender,
        DailyLifeItem.AccountBook
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val curRoute = navBackStackEntry?.destination?.route

    AnimatedVisibility(
        visible = items.map { it.screenRoute }.contains(curRoute)
    ) {
        NavigationBar(
            modifier =  modifier,
            containerColor = containerColor,
            contentColor = contentColor
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    selected = curRoute == item.screenRoute,
                    icon = {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(item.icon),
                                contentDescription = stringResource(item.title)
                            )

                            Text(
                                text = stringResource(item.title),
                                style = TextStyle(
                                    textAlign = TextAlign.Center,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorResource(R.color.gray_asparagus),
                        selectedTextColor = colorResource(R.color.gray_asparagus),
                        unselectedIconColor = colorResource(R.color.light_gray),
                        unselectedTextColor = colorResource(R.color.light_gray),
                        indicatorColor = Color.Transparent
                    ),
                    onClick = {
                        navController.navigate(item.screenRoute) {
                            navController.graph.startDestinationRoute?.let {
                                popUpTo(it) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}