package com.personal.personalsafetyguard.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.personal.personalsafetyguard.model.BankDetailsItem
import com.personal.personalsafetyguard.model.CardDetailsItem
import com.personal.personalsafetyguard.model.LoginDetailsItem
import com.personal.personalsafetyguard.ui.fragments.BankDetails

@Database(entities = [BankDetailsItem::class, LoginDetailsItem::class, CardDetailsItem::class],version = 9,exportSchema = false)
abstract class PasswordDatabase : RoomDatabase() {

    abstract fun bankDao() : BankDao

    abstract fun cardDao() : CardDao

    abstract fun loginDao() : LoginDao

}