package com.example.dailylife.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dailylife.R
import com.example.dailylife.ui.screen.account.AccountScreen
import com.example.dailylife.ui.screen.account.AddAccountScreen
import com.example.dailylife.ui.screen.CalendarScreen
import com.example.dailylife.ui.screen.todo.TodoScreen
import com.example.dailylife.viewmodel.AccountViewModel
import com.example.dailylife.viewmodel.TodoViewModel

@Composable
fun DailyLifeNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)

    NavHost(
        navController = navController,
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.ivory)),
        startDestination = DailyLifeScreen.TodoList.name
    ) {
        composable(DailyLifeScreen.TodoList.name) {
            val todoViewModel: TodoViewModel = hiltViewModel(viewModelStoreOwner)

            TodoScreen(
                todoViewModel = todoViewModel
            )
        }

        composable(DailyLifeScreen.Calendar.name) {
            val todoViewModel: TodoViewModel = hiltViewModel(viewModelStoreOwner)

            CalendarScreen(
                todoViewModel = todoViewModel
            )
        }

        composable(DailyLifeScreen.AccountBook.name) {
            val accountViewModel: AccountViewModel = hiltViewModel(viewModelStoreOwner)

            AccountScreen(
                accountViewModel = accountViewModel,
                onClickAddButton = {
                    navController.navigate(DailyLifeScreen.AddAccount.name) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(DailyLifeScreen.AddAccount.name) {
            val accountViewModel: AccountViewModel = hiltViewModel(viewModelStoreOwner)

            AddAccountScreen(
                navController = navController,
                accountViewModel = accountViewModel
            )
        }
    }
}