package com.personal.personalsafetyguard.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.crypto.SecretKey

@Entity(tableName = "LoginDetailsTable")
data class LoginDetailsItem(@PrimaryKey var id : String, var loginName:String, var loginEmail : String, var loginPassword : String, var loginWebsite : String, var loginNotes : String, var loginCategory : String,  @ColumnInfo(name = "encryptedPassword") val encryptedPassword: String  ) {

}