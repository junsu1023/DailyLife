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
import com.example.dailylife.ui.screen.CalendarScreen
import com.example.dailylife.ui.screen.todo.TodoScreen
import com.example.dailylife.viewmodel.CalendarViewModel
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
        composable(DailyLifeScreen.TodoList.name) { backStackEntry ->
            val todoViewModel: TodoViewModel = hiltViewModel(backStackEntry)

            TodoScreen(
                todoViewModel = todoViewModel
            )
        }

        composable(DailyLifeScreen.Calendar.name) { backStackEntry ->
            val todoViewModel: TodoViewModel = if(navController.previousBackStackEntry != null) {
                hiltViewModel(navController.previousBackStackEntry!!)
            } else {
                hiltViewModel()
            }

            val calendarViewModel = hiltViewModel<CalendarViewModel>(backStackEntry)

            CalendarScreen(
                todoViewModel = todoViewModel,
                calendarViewModel = calendarViewModel
            )
        }

        composable(DailyLifeScreen.AccountBook.name) {
            AccountScreen()
        }
    }
}