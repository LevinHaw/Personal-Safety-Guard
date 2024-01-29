package com.personal.personalsafetyguard.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.personal.personalsafetyguard.model.BankDetailsItem
import com.personal.personalsafetyguard.model.LoginDetailsItem

@Dao
interface LoginDao {

    @Insert
    fun insertLoginDetails(loginDetails : LoginDetailsItem)

    @Query("DELETE FROM LoginDetailsTable WHERE id = :id")
    fun deleteLoginDetails(id : String)

    @Query("SELECT * FROM LoginDetailsTable")
    fun getAllLoginDetails() : LiveData<List<LoginDetailsItem>>

    @Query("SELECT * FROM LoginDetailsTable WHERE loginCategory = :category")
    fun getCategoryLoginDetails(category : String) : LiveData<List<LoginDetailsItem>>

}