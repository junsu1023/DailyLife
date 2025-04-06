package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.AccountDao
import com.example.data.dao.ClassificationDao
import com.example.data.entitiy.AccountEntity
import com.example.data.entitiy.ClassificationEntity
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

@Database(entities = [AccountEntity::class, ClassificationEntity::class], version = 1)
@TypeConverters(com.example.data.converters.TypeConverters::class)
abstract class AccountDatabase: RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun classificationDao(): ClassificationDao

    companion object {
        fun getInstance(context: Context): AccountDatabase = Room
            .databaseBuilder(context, AccountDatabase:: class.java, "account_db")
            .addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    Executors.newSingleThreadExecutor().execute {
                        runBlocking {
                            getInstance(context).classificationDao().addClassification(ClassificationEntity("🧺생활용품"))
                            getInstance(context).classificationDao().addClassification(ClassificationEntity("🍕식비"))
                            getInstance(context).classificationDao().addClassification(ClassificationEntity("🚕교통/차량"))
                            getInstance(context).classificationDao().addClassification(ClassificationEntity("🥼패션/미용"))
                            getInstance(context).classificationDao().addClassification(ClassificationEntity("🏚️주거/통신"))
                            getInstance(context).classificationDao().addClassification(ClassificationEntity("💪건강"))
                            getInstance(context).classificationDao().addClassification(ClassificationEntity("📖교육"))
                            getInstance(context).classificationDao().addClassification(ClassificationEntity("기타"))
                        }
                    }
                }
            })
            .build()
    }
}