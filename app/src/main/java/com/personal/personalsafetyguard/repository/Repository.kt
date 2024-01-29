package com.personal.personalsafetyguard.repository

import androidx.lifecycle.LiveData
import com.personal.personalsafetyguard.db.BankDao
import com.personal.personalsafetyguard.db.CardDao
import com.personal.personalsafetyguard.db.LoginDao
import com.personal.personalsafetyguard.model.BankDetailsItem
import com.personal.personalsafetyguard.model.CardDetailsItem
import com.personal.personalsafetyguard.model.LoginDetailsItem
import javax.inject.Inject



class Repository @Inject constructor(private val bankDao: BankDao, private val loginDao: LoginDao, private val cardDao: CardDao) {

    fun insertBankDetails(bankDetailsItem: BankDetailsItem){
      bankDao.insertBankDetails(bankDetailsItem)
    }

    fun deleteBankDetails(accountNumber: Long){
        bankDao.deleteBankDetails(accountNumber)
    }

    fun getAllBankDetails() : LiveData<List<BankDetailsItem>>{
        return bankDao.getAllBankDetails()
    }

    fun insertCardDetails(cardDetailsItem: CardDetailsItem){
        cardDao.insertCardDetails(cardDetailsItem)
    }

    fun deleteCardDetails(cardNumber: String){
        cardDao.deleteCardDetails(cardNumber)
    }

    fun getAllCardDetails() : LiveData<List<CardDetailsItem>>{
        return cardDao.getAllCardDetails()
    }

    fun insertLoginDetails(loginDetailsItem: LoginDetailsItem){
        loginDao.insertLoginDetails(loginDetailsItem)
    }

    fun deleteLoginDetails(id: String){
        loginDao.deleteLoginDetails(id)
    }

    fun getAllLoginDetails() : LiveData<List<LoginDetailsItem>>{
        return loginDao.getAllLoginDetails()
    }

    fun getCategoryLoginDetails(category : String) : LiveData<List<LoginDetailsItem>>{
        return loginDao.getCategoryLoginDetails(category)
    }


}