package com.example.dailylife

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.dailylife.bottombar.BottomNavigationBar
import com.example.dailylife.navigation.DailyLifeNavHost
import com.example.dailylife.ui.theme.DailyLifeTheme

@Composable
fun DailyLifeApp() {
    val navController = rememberNavController()
    val snackbarState = remember { SnackbarHostState() }

    DailyLifeTheme {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    modifier = Modifier.height(100.dp),
                    containerColor = colorResource(R.color.alabaster),
                    contentColor = colorResource(R.color.alabaster),
                    navController = navController
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarState) }
        ) {
            DailyLifeNavHost(
                modifier = Modifier.padding(it),
                navController = navController,
                snackbarHostState = snackbarState
            )
        }
    }
}