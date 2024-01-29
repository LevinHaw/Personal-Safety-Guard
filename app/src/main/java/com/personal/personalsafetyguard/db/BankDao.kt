package com.personal.personalsafetyguard.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.personal.personalsafetyguard.model.BankDetailsItem

@Dao
interface BankDao {

    @Insert
    fun insertBankDetails(bankDetails : BankDetailsItem)

    @Query("DELETE FROM BankDetailsTable WHERE bankAccNumber = :accNumber")
    fun deleteBankDetails(accNumber : Long)

    @Query("SELECT * FROM BankDetailsTable")
    fun getAllBankDetails() : LiveData<List<BankDetailsItem>>

}