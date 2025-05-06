package com.example.dailylife

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import android.widget.Toast
import com.example.dailylife.ui.screen.account.makeAccountItem
import com.example.dailylife.util.getToday

class AccountNotificationListenerService: NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        val packageName: String = sbn?.packageName ?: "null"
        val extras = sbn?.notification?.extras
        Log.d("MyNotificationService", "packageName = $packageName")
        val extraTitle: List<String> = extras?.getString(Notification.EXTRA_TITLE).toString().split(" ")
        val extraText: List<String> = extras?.getString(Notification.EXTRA_TEXT).toString().split(" ")

        val bank = packageName.isBankNotification()
        if(bank != "null") {
            if(extraText.size > 1 && extraText[1].isMoney()) {

                makeAccountItem(
                    kind = applicationContext.getString(R.string.expend),
                    date = getToday().split("-").joinToString(""),
                    cost = extraText[1].substring(0, extraText[1].length - 1).convertMoney(),
                    classification = applicationContext.getString(R.string.etc),
                    cardCompany = bank,
                    content = applicationContext.getString(R.string.expend)
                )
            }
            else if(extraTitle.size > 1 && extraTitle[1].isMoney()) {
                makeAccountItem(
                    kind = applicationContext.getString(R.string.expend),
                    date = getToday().split("-").joinToString(""),
                    cost = extraTitle[1].substring(0, extraTitle[1].length - 1).convertMoney(),
                    classification = applicationContext.getString(R.string.etc),
                    cardCompany = bank,
                    content = applicationContext.getString(R.string.expend)
                )
            }
        }

        Toast.makeText(applicationContext, "packageName = $packageName, title = $extraTitle, text = $extraText", Toast.LENGTH_LONG).show()
        Log.d("MyNotificationService", "extraTitle = $extraTitle")
        Log.d("MyNotificationService", "extraText = $extraText")
    }

    private fun String.isBankNotification(): String {
        return when(this) {
            "com.kebhana.hanapush" -> "하나은행"
            "com.kakaobank.channel" -> "카카오뱅크"
            else -> "Null"
        }
    }

    private fun String.isMoney(): Boolean = this.last() == '원'

    private fun String.convertMoney(): Long {
        var money = ""
        this.forEach {
            if(it in '0' .. '9') money += it
        }

        return money.toLong()
    }
}