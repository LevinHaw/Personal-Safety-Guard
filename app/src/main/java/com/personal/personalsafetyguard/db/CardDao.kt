package com.personal.personalsafetyguard.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.personal.personalsafetyguard.model.BankDetailsItem
import com.personal.personalsafetyguard.model.CardDetailsItem

@Dao
interface CardDao {

    @Insert
    fun insertCardDetails(cardDetails : CardDetailsItem)

    @Query("DELETE FROM CardDetailsTable WHERE cardNumber = :cardNumber")
    fun deleteCardDetails(cardNumber : String)

    @Query("SELECT * FROM CardDetailsTable")
    fun getAllCardDetails() : LiveData<List<CardDetailsItem>>

}