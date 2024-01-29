package com.personal.personalsafetyguard.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CardDetailsTable")
data class CardDetailsItem(var cardHolder : String , var cardIssuer :String, @PrimaryKey var cardNumber :String, var cardExpiryMonth : String, var cardExpiryYear : String, var cardCvv : String) {

}