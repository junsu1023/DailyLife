package com.example.dailylife

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.res.stringResource
import androidx.core.app.NotificationManagerCompat
import com.example.dailylife.component.CheckDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var isShowPermissionDialog = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            if(!isNotificationPermissionGranted()) {
                isShowPermissionDialog = true
            }
            else {
                DailyLifeApp()
            }

            if(isShowPermissionDialog) {
                CheckDialog(
                    title = stringResource(R.string.permission_dialog_title),
                    description = stringResource(R.string.permission_dialog_description),
                    leftButton = stringResource(R.string.move_settings_screen),
                    onClickCancel = {
                        isShowPermissionDialog = false
                        finish()
                    },
                    onClickConfirm = {
                        startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                    }
                )
            }
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

    override fun onRestart() {
        super.onRestart()

        if(!isNotificationPermissionGranted()) {
            isShowPermissionDialog = true
        }
        else {
            setContent {
                DailyLifeApp()
            }
        }
    }

    private fun isNotificationPermissionGranted(): Boolean {
        /*
        현재 프로젝트의 minSDK는 30이기에 if문을 무조건 타게 됨.
        차후 타 프로젝트에서 minSDK가 26 이하라면 else문의 동작도 필요하기에 공부용으로 작성
         */
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            return notificationManager.isNotificationListenerAccessGranted(ComponentName(application, AccountNotificationListenerService::class.java))
        } else {
            return NotificationManagerCompat.getEnabledListenerPackages(applicationContext).contains(applicationContext.packageName)
        }
    }
}