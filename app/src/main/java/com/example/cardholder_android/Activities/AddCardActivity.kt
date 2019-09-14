package com.example.cardholder_android.Activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.cardholder_android.CardDBHelper
import com.example.cardholder_android.Models.Card
import com.example.cardholder_android.R
import kotlinx.android.synthetic.main.add_card_activity.*
import java.text.DecimalFormat


class AddCardActivity : AppCompatActivity() {

/*
    private val TOTAL_SYMBOLS = 19 // size of pattern 0000-0000-0000-0000
    private val TOTAL_DIGITS = 16 // max numbers of digits in pattern: 0000 x 4
    private val DIVIDER_MODULO = 5 // means divider position is every 5th symbol beginning with 1
    private val DIVIDER_POSITION = DIVIDER_MODULO - 1 // means divider position is every 4th symbol beginning with 0
    private val DIVIDER = '-'
*/

    companion object {
        lateinit var dbHelper: CardDBHelper
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_card_activity)

        //Input types for editViews
        cardNumberView.setInputType(InputType.TYPE_CLASS_NUMBER)
        bankAccountView.setInputType(InputType.TYPE_CLASS_NUMBER)
        expirationDateView.setInputType(InputType.TYPE_CLASS_NUMBER)
        cardNameView.setInputType(InputType.TYPE_CLASS_TEXT)

        //Card Number text formatting
        cardNumberView.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable) {
                var cardNumber = cardNumberView.text.toString()
                if (cardNumber.length == 4 || cardNumber.length == 9 || cardNumber.length == 14) {
                    cardNumber += " "
                    cardNumberView.setText(cardNumber)
                    cardNumberView.setSelection(cardNumber.length)
/*
                if (!isInputCorrect(p0, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                    p0.replace(0, p0.length,
                        buildCorrectString(getDigitArray(p0, TOTAL_DIGITS), DIVIDER_POSITION, DIVIDER))*/
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        //Expiration date number formatting
        expirationDateView.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable) {
                var expirationDate = expirationDateView.text.toString()
                if (expirationDate.length == 2) {
                    expirationDate += "/"
                    expirationDateView.setText(expirationDate)
                    expirationDateView.setSelection(expirationDate.length)
                }
/*
                val dec = DecimalFormat("##/##")
                val formattedText = dec.format(expirationDate)
                expirationDateView.setText(formattedText)
                }*/


            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

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
/*/

    private fun isInputCorrect(s: Editable, totalSymbols: Int, dividerModulo: Int, divider: Char): Boolean {
        var isCorrect = s.length <= totalSymbols // check size of entered string
        for (i in 0 until s.length) { // check that every element is right
            if (i > 0 && (i + 1) % dividerModulo == 0) {
                isCorrect = isCorrect and (divider == s[i])
            } else {
                isCorrect = isCorrect and Character.isDigit(s[i])
            }
        }
        return isCorrect
    }

    private fun buildCorrectString(digits: CharArray, dividerPosition: Int, divider: Char): String {
        val formatted = StringBuilder()

        for (i in digits.indices) {
            if (digits[i].toInt() != 0) {
                formatted.append(digits[i])
                if (i > 0 && i < digits.size - 1 && (i + 1) % dividerPosition == 0) {
                    formatted.append(divider)
                }
            }
        }

        return formatted.toString()
    }

    private fun getDigitArray(s: Editable, size: Int): CharArray {
        val digits = CharArray(size)
        var index = 0
        var i = 0
        while (i < s.length && index < size) {
            val current = s[i]
            if (Character.isDigit(current)) {
                digits[index] = current
                index++
            }
            i++
        }
        return digits
    }
}*/
