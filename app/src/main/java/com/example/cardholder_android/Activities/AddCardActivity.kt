package com.example.cardholder_android.Activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.cardholder_android.Activities.MainActivity.Companion.dbHelper
import com.example.cardholder_android.CardDBHelper
import com.example.cardholder_android.Models.Card
import com.example.cardholder_android.R
import kotlinx.android.synthetic.main.add_card_activity.*

class AddCardActivity : AppCompatActivity() {

    companion object {
        lateinit var dbHelper: CardDBHelper
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_card_activity)

        cardNumberView.setInputType(InputType.TYPE_CLASS_NUMBER)
        bankAccountView.setInputType(InputType.TYPE_CLASS_NUMBER)
        expirationDateView.setInputType(InputType.TYPE_CLASS_NUMBER)
        cardNameView.setInputType(InputType.TYPE_CLASS_TEXT)

        //Card Number text formatting
        cardNumberView.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {
                var cardNumber = cardNumberView.text.toString()
                if (cardNumber.length == 4 || cardNumber.length == 9 || cardNumber.length == 14) {
                    cardNumber += " "
                    cardNumberView.setText(cardNumber)
                    cardNumberView.setSelection(cardNumber.length)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        //Expiration date number formatting
        expirationDateView.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {
                var expirationDate = expirationDateView.text.toString()
                if (expirationDate.length == 2) {
                    expirationDate += "/"
                    expirationDateView.setText(expirationDate)
                    expirationDateView.setSelection(expirationDate.length)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

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