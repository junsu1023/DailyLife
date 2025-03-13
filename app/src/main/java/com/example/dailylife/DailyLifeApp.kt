package com.example.dailylife

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.compose.rememberNavController
import com.example.dailylife.bottombar.BottomNavigationBar
import com.example.dailylife.navigation.DailyLifeNavHost
import com.example.dailylife.ui.theme.DailyLifeTheme

@Composable
fun DailyLifeApp() {
    val navController = rememberNavController()

    DailyLifeTheme {
        Scaffold(
            bottomBar = { BottomNavigationBar(
                modifier = Modifier,
                containerColor = colorResource(R.color.alabaster),
                contentColor = colorResource(R.color.alabaster),
                navController = navController
            ) }
        ) {
            DailyLifeNavHost(
                modifier = Modifier.padding(it),
                navController = navController
            )
        }
    }
}