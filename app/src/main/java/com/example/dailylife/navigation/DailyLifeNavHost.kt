package com.example.dailylife.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dailylife.R
import com.example.dailylife.ui.screen.AccountScreen
import com.example.dailylife.ui.screen.CalenderScreen
import com.example.dailylife.ui.screen.todo.TodoScreen
import com.example.dailylife.viewmodel.TodoViewModel

@Composable
fun DailyLifeNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.ivory)),
        startDestination = DailyLifeScreen.TodoList.name
    ) {
        composable(DailyLifeScreen.TodoList.name) {
            val todoViewModel = hiltViewModel<TodoViewModel>(
                navController.getBackStackEntry(DailyLifeScreen.TodoList.name)
            )

            TodoScreen(
                todoViewModel = todoViewModel
            )
        }

        composable(DailyLifeScreen.Calendar.name) {
            CalenderScreen()
        }

        composable(DailyLifeScreen.AccountBook.name) {
            AccountScreen()
        }
    }
}