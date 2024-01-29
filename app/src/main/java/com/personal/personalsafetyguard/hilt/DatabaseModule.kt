package com.personal.personalsafetyguard.hilt

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.personal.personalsafetyguard.db.BankDao
import com.personal.personalsafetyguard.db.CardDao
import com.personal.personalsafetyguard.db.LoginDao
import com.personal.personalsafetyguard.db.PasswordDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(application : Application) : PasswordDatabase{
        return Room.databaseBuilder(application,PasswordDatabase::class.java,"MyDatabase")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    fun provideBankDao(passwordDatabase: PasswordDatabase) : BankDao{
        return passwordDatabase.bankDao()
    }

    @Provides
    fun provideLoginDao(passwordDatabase: PasswordDatabase) : LoginDao{
        return passwordDatabase.loginDao()
    }

    @Provides
    fun provideCardDao(passwordDatabase: PasswordDatabase) : CardDao{
        return passwordDatabase.cardDao()
    }
}