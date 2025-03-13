package com.example.dailylife.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.dailylife.R

enum class DailyLifeScreen {
    TodoList,
    Calender,
    AccountBook
}

sealed class DailyLifeItem(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val screenRoute: String
) {
    data object TodoList: DailyLifeItem(
        title = R.string.todo_list,
        icon = R.drawable.todo_icon,
        screenRoute = DailyLifeScreen.TodoList.name
    )

    data object Calender: DailyLifeItem(
        title = R.string.calender,
        icon = R.drawable.calender_icon,
        screenRoute = DailyLifeScreen.Calender.name
    )

    data object AccountBook: DailyLifeItem(
        title = R.string.account_book,
        icon = R.drawable.account_book_icon,
        screenRoute = DailyLifeScreen.AccountBook.name
    )
}