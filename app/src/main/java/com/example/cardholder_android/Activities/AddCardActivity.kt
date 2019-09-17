package com.example.cardholder_android.Activities

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AppCompatActivity
import com.example.cardholder_android.CardDBHelper
import com.example.cardholder_android.Models.Card
import com.example.cardholder_android.R
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.add_card_activity.*


class AddCardActivity : AppCompatActivity() {

    companion object {
        lateinit var dbHelper: CardDBHelper
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_card_activity)

        //Input types for editViews
        bankAccountView.setInputType(InputType.TYPE_CLASS_NUMBER)
        cardNameView.setInputType(InputType.TYPE_CLASS_TEXT)

        //Card Number text formatting
        val cardNumberViewListener = MaskedTextChangedListener("[0000] [0000] [0000] [0000]", cardNumberView)
        cardNumberView.addTextChangedListener(cardNumberViewListener)
        cardNumberView.onFocusChangeListener = cardNumberViewListener

        //Expiration date text formatting
        val expDateViewListener = MaskedTextChangedListener("[00]{/}[00]", expirationDateView)
        expirationDateView.addTextChangedListener(expDateViewListener)
        expirationDateView.onFocusChangeListener = expDateViewListener

        //Adding card button
        dbHelper = CardDBHelper(this)

        btnAddCard.setOnClickListener {
            val card = Card()
            card.cardName = cardNameView.text.toString()
            card.cardNumber = cardNumberView.text.toString()
            card.bankAccount = bankAccountView.text.toString()
            card.expDate = expirationDateView.text.toString()

            dbHelper.addCard(card)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}