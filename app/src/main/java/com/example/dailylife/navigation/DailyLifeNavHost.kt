package com.example.dailylife.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.dailylife.ui.screen.account.AccountEditListScreen
import com.example.dailylife.ui.screen.account.EditAddItemScreen
import com.example.dailylife.ui.screen.todo.TodoScreen
import com.example.dailylife.viewmodel.AccountViewModel
import com.example.dailylife.viewmodel.AccountSelectorViewModel
import com.example.dailylife.viewmodel.TodoViewModel
import com.example.data.entitiy.AccountEntity

@Composable
fun DailyLifeNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current)
    val onBackAction: () -> Unit = { navController.popBackStack() }
    var editListScreenTitle = ""
    var editKind: Any? = null
    var editAccountItem: AccountEntity? by remember { mutableStateOf(null) }

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
                todoViewModel = todoViewModel,
                snackbarHostState = snackbarHostState
            )
        }

        composable(DailyLifeScreen.Calendar.name) {
            val todoViewModel: TodoViewModel = hiltViewModel(viewModelStoreOwner)
            val accountViewModel: AccountViewModel = hiltViewModel(viewModelStoreOwner)

            CalendarScreen(
                todoViewModel = todoViewModel,
                accountViewModel = accountViewModel,
                snackbarHostState = snackbarHostState,
                onClickAccountItem = {
                    editAccountItem = it
                    navController.navigate(DailyLifeScreen.AddAccount.name) {
                        launchSingleTop = true
                    }
                },
                goAddAccountScreen = {
                    editAccountItem = null
                    navController.navigate(DailyLifeScreen.AddAccount.name) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(DailyLifeScreen.AccountBook.name) {
            val accountViewModel: AccountViewModel = hiltViewModel(viewModelStoreOwner)

            AccountScreen(
                accountViewModel = accountViewModel,
                onClickAddButton = {
                    editAccountItem = null
                    navController.navigate(DailyLifeScreen.AddAccount.name) {
                        launchSingleTop = true
                    }
                },
                onClickAccountItem = {
                    editAccountItem = it
                    navController.navigate(DailyLifeScreen.AddAccount.name) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(DailyLifeScreen.AddAccount.name) {
            val accountViewModel: AccountViewModel = hiltViewModel(viewModelStoreOwner)
            val accountSelectorViewModel: AccountSelectorViewModel = hiltViewModel(viewModelStoreOwner)

            AddAccountScreen(
                accountViewModel = accountViewModel,
                accountSelectorViewModel = accountSelectorViewModel,
                editAccountItem = editAccountItem,
                goListEditScreen = {
                    editListScreenTitle = it
                    navController.navigate(DailyLifeScreen.EditList.name) {
                        launchSingleTop = true
                    }
                },
                onBackAction = onBackAction
            )
        }

        composable(DailyLifeScreen.EditList.name) {
            val accountSelectorViewModel: AccountSelectorViewModel = hiltViewModel(viewModelStoreOwner)

            AccountEditListScreen(
                title = editListScreenTitle,
                accountSelectorViewModel = accountSelectorViewModel,
                onBackAction = onBackAction,
                goAddTitle = {
                    editKind = null
                    navController.navigate(DailyLifeScreen.EditAddList.name) {
                        launchSingleTop = true
                    }
                },
                onEditTitle = {
                    editKind = it
                    navController.navigate(DailyLifeScreen.EditAddList.name) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(DailyLifeScreen.EditAddList.name) {
            val accountSelectorViewModel: AccountSelectorViewModel = hiltViewModel(viewModelStoreOwner)

            EditAddItemScreen(
                title = editListScreenTitle,
                editKind = editKind,
                accountSelectorViewModel = accountSelectorViewModel,
                onBackAction = onBackAction
            )
        }
    }
}