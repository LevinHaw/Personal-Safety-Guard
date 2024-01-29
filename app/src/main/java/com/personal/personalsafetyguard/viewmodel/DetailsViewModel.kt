package com.personal.personalsafetyguard.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.personal.personalsafetyguard.model.BankDetailsItem
import com.personal.personalsafetyguard.model.CardDetailsItem
import com.personal.personalsafetyguard.model.LoginDetailsItem
import com.personal.personalsafetyguard.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private var bankDetailsList : LiveData<List<BankDetailsItem>>
    private var cardDetailsList : LiveData<List<CardDetailsItem>>
    private var loginDetaisList : LiveData<List<LoginDetailsItem>>

    init {
        bankDetailsList = repository.getAllBankDetails()
        cardDetailsList = repository.getAllCardDetails()
        loginDetaisList = repository.getAllLoginDetails()
    }

    fun getAllBankDetails() : LiveData<List<BankDetailsItem>>{
        return bankDetailsList
    }

    fun insertBankDetails(bankDetailsItem: BankDetailsItem){
        repository.insertBankDetails(bankDetailsItem)
    }

    fun deleteBankDetails(accNumber: Long){
        repository.deleteBankDetails(accNumber)
    }

    fun insertCardDetails(cardDetailsItem: CardDetailsItem){
        repository.insertCardDetails(cardDetailsItem)
    }

    fun deleteCardDetails(cardNumber: String){
        repository.deleteCardDetails(cardNumber)
    }

    fun getAllCardDetails() : LiveData<List<CardDetailsItem>>{
        return cardDetailsList
    }

    fun insertLoginDetails(loginDetailsItem: LoginDetailsItem){
        repository.insertLoginDetails(loginDetailsItem)
    }

    fun deleteLoginDetails(id: String){
        repository.deleteLoginDetails(id)
    }


    fun getAllLoginDetails() : LiveData<List<LoginDetailsItem>>{
        return loginDetaisList
    }
    fun getCategoryLoginDetails(category : String) : LiveData<List<LoginDetailsItem>>{
        return repository.getCategoryLoginDetails(category)
    }
}