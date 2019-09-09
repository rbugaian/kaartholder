package com.example.cardholder_android.Models

class Card {

    var id:Int = 0
    var cardName: String? = null
    var cardNumber: String? = null
    var bankAccount: String? = null
    var expDate: String? = null

    constructor() {}

    constructor(id: Int, cardName: String, bankAccount: String, cardNumber: String, expDate: String) {
        this.id = id
        this.cardName = cardName
        this.cardNumber = cardNumber
        this.bankAccount = bankAccount
        this.expDate = expDate
    }

}
