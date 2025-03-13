package com.example.dailylife.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dailylife.ui.screen.AccountScreen
import com.example.dailylife.ui.screen.CalenderScreen
import com.example.dailylife.ui.screen.TodoScreen

@Composable
fun DailyLifeNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = DailyLifeScreen.TodoList.name
    ) {
        composable(DailyLifeScreen.TodoList.name) {
            TodoScreen()
        }

        composable(DailyLifeScreen.Calender.name) {
            CalenderScreen()
        }

        composable(DailyLifeScreen.AccountBook.name) {
            AccountScreen()
        }
    }
}