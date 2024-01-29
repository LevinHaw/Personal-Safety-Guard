package com.personal.personalsafetyguard.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "BankDetailsTable")
data class BankDetailsItem(var bankName:String, @PrimaryKey var bankAccNumber : Long, var bankPass : String, var bankAddress : String,  @ColumnInfo(name = "encryptedBankPassword") val encryptedBankPassword: String) {

}