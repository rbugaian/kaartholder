package com.example.cardholder_android.Activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cardholder_android.CardDBHelper
import com.example.cardholder_android.Models.Card
import com.example.cardholder_android.R
import kotlinx.android.synthetic.main.card_info_activity.*



class CardInfoActivity : AppCompatActivity() {

    companion object {
        lateinit var dbHelper: CardDBHelper
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.card_info_activity)

        //Getting card object from DB
        val cardId = intent.getIntExtra("CARD ID", 0)
        val card = dbHelper.getCardById(cardId)

        //Set data to layout
        etChangeName.setText(card.cardName)
        etChangeBankAccount.setText(card.bankAccount)
        etChangeCardNumber.setText(card.cardNumber)
        etChangeExpDate.setText(card.expDate)

        //Editing card information
        btnEditCard.setOnClickListener {
            card.cardName = etChangeName.text.toString()
            card.cardNumber = etChangeCardNumber.text.toString()
            card.bankAccount = etChangeBankAccount.text.toString()
            card.expDate = etChangeExpDate.text.toString()
            card.id = cardId

            dbHelper.updateCard(card)
        }

        //Deleting card information
        btnDeleteCard.setOnClickListener {
            dbHelper.deleteCard(card)
        }
    }

}