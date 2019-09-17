package com.example.cardholder_android.Activities

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AppCompatActivity
import com.example.cardholder_android.CardDBHelper
import com.example.cardholder_android.R
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.card_info_activity.*

class CardInfoActivity : AppCompatActivity() {

    companion object {
        lateinit var dbHelper: CardDBHelper;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card_info_activity)

        dbHelper = CardDBHelper(this)

        //Getting card object from DB
        val cardId = intent.getIntExtra("card_id", 0)
        val card = dbHelper.getCardById(cardId)

        //Set data to layout
        etChangeName.setText(card.cardName)
        etChangeBankAccount.setText(card.bankAccount)
        etChangeCardNumber.setText(card.cardNumber)
        etChangeExpDate.setText(card.expDate)

        //Input types for editViews
        etChangeBankAccount.setInputType(InputType.TYPE_CLASS_NUMBER)
        etChangeName.setInputType(InputType.TYPE_CLASS_TEXT)

        //Card Number text formatting
        val cardNumberViewListener = MaskedTextChangedListener("[0000] [0000] [0000] [0000]", etChangeCardNumber)
        etChangeCardNumber.addTextChangedListener(cardNumberViewListener)
        etChangeCardNumber.onFocusChangeListener = cardNumberViewListener

        //Expiration date text formatting
        val expDateViewListener = MaskedTextChangedListener("[00]{/}[00]", etChangeExpDate)
        etChangeExpDate.addTextChangedListener(expDateViewListener)
        etChangeExpDate.onFocusChangeListener = expDateViewListener

        //Editing card information
        btnEditCard.setOnClickListener {
            card.cardName = etChangeName.text.toString()
            card.cardNumber = etChangeCardNumber.text.toString()
            card.bankAccount = etChangeBankAccount.text.toString()
            card.expDate = etChangeExpDate.text.toString()
            card.id = cardId

            dbHelper.updateCard(card)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //Deleting card information
        btnDeleteCard.setOnClickListener {
            dbHelper.deleteCard(card)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}