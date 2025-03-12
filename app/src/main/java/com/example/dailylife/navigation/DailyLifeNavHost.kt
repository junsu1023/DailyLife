package com.example.dailylife.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dailylife.ui.screen.InitScreen

@Composable
fun DailyLifeNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = DailyLifeScreen.Init.name
    ) {
        val handleClickButton: (Int) -> Unit = {
            when(it) {
                0 -> navController.navigate(DailyLifeScreen.Init.name)
                1 -> navController.navigate(DailyLifeScreen.TodoList.name)
                2 -> navController.navigate(DailyLifeScreen.Calender.name)
                3 -> navController.navigate(DailyLifeScreen.AccountBook.name)
            }
        }

        composable(DailyLifeScreen.Init.name) {
            InitScreen(handleClickButton = handleClickButton)
        }
    }
}