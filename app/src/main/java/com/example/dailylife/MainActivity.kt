package com.example.dailylife

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            DailyLifeApp()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if(ev?.action == MotionEvent.ACTION_DOWN) {
            val focusView = currentFocus

            if(focusView != null) {
                val outRect = Rect()
                focusView.getGlobalVisibleRect(outRect)

                val x = ev.rawX.toInt()
                val y = ev.rawY.toInt()

                if(outRect.contains(x, y)) {
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(focusView.windowToken, 0)
                    focusView.clearFocus()
                }
            }
        }

        return super.dispatchTouchEvent(ev)
    }
}