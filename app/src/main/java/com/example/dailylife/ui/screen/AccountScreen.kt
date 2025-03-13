package com.example.dailylife.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.dailylife.R

@Composable
fun AccountScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = stringResource(R.string.account_book))
    }
}