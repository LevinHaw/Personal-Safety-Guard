package com.personal.personalsafetyguard.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseError
import com.personal.personalsafetyguard.model.ParentItem
import com.personal.personalsafetyguard.repository.FirebaseRepo

class FirebaseDataViewModel : ViewModel(), FirebaseRepo.OnRealtimeDbTaskComplete {
    private val parentItemMutableLiveData = MutableLiveData<List<ParentItem>>()
    private val databaseErrorMutableLiveData = MutableLiveData<DatabaseError>()
    private val firebaseRepo: FirebaseRepo

    fun getParentItemMutableLiveData(): MutableLiveData<List<ParentItem>> {
        return parentItemMutableLiveData
    }

    fun getDatabaseErrorMutableLiveData(): MutableLiveData<DatabaseError> {
        return databaseErrorMutableLiveData
    }

    init {
        firebaseRepo = FirebaseRepo(this)
    }

    fun getAllData() {
        firebaseRepo.getAllData()
    }

    override fun onSuccess(parentItemList: List<ParentItem>) {
        parentItemMutableLiveData.value = parentItemList
    }

    override fun onFailure(error: DatabaseError) {
        databaseErrorMutableLiveData.value = error
    }
}