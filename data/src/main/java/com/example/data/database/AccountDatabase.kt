package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.AccountDao
import com.example.data.dao.CardCompanyDao
import com.example.data.dao.ClassificationDao
import com.example.data.entitiy.AccountEntity
import com.example.data.entitiy.CardCompanyEntity
import com.example.data.entitiy.ClassificationEntity
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

@Database(entities = [AccountEntity::class, ClassificationEntity::class, CardCompanyEntity::class], version = 1)
@TypeConverters(com.example.data.converters.TypeConverters::class)
abstract class AccountDatabase: RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun classificationDao(): ClassificationDao
    abstract fun cardCompanyDao(): CardCompanyDao

    companion object {
        fun getInstance(context: Context): AccountDatabase = Room
            .databaseBuilder(context, AccountDatabase:: class.java, "account_db")
            .addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    Executors.newSingleThreadExecutor().execute {
                        runBlocking {
                            getInstance(context).classificationDao().addClassification(ClassificationEntity(classification = "🧺생활용품"))
                            getInstance(context).classificationDao().addClassification(ClassificationEntity(classification = "🍕식비"))
                            getInstance(context).classificationDao().addClassification(ClassificationEntity(classification = "🚕교통/차량"))
                            getInstance(context).classificationDao().addClassification(ClassificationEntity(classification = "🥼패션/미용"))
                            getInstance(context).classificationDao().addClassification(ClassificationEntity(classification = "🏚️주거/통신"))
                            getInstance(context).classificationDao().addClassification(ClassificationEntity(classification = "💪건강"))
                            getInstance(context).classificationDao().addClassification(ClassificationEntity(classification = "📖교육"))
                            getInstance(context).classificationDao().addClassification(ClassificationEntity(classification = "기타"))

                            getInstance(context).cardCompanyDao().addCardCompany(CardCompanyEntity(cardCompany = "하나카드"))
                            getInstance(context).cardCompanyDao().addCardCompany(CardCompanyEntity(cardCompany = "카카오뱅크"))
                            getInstance(context).cardCompanyDao().addCardCompany(CardCompanyEntity(cardCompany = "토스"))
                            getInstance(context).cardCompanyDao().addCardCompany(CardCompanyEntity(cardCompany = "현대카드"))
                            getInstance(context).cardCompanyDao().addCardCompany(CardCompanyEntity(cardCompany = "국민카드"))
                            getInstance(context).cardCompanyDao().addCardCompany(CardCompanyEntity(cardCompany = "우리카드"))
                        }
                    }
                }
            })
            .build()
    }
}