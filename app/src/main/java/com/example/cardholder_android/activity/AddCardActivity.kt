package com.example.cardholder_android.activity

import android.content.DialogInterface
import android.content.res.AssetManager
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.cardholder_android.CardDBHelper
import com.example.cardholder_android.R
import com.example.cardholder_android.model.Card
import com.example.cardholder_android.util.FontLoader
import com.redmadrobot.inputmask.MaskedTextChangedListener
import de.adorsys.android.securestoragelibrary.SecurePreferences
import kotlinx.android.synthetic.main.add_card_activity.*
import java.util.*


class AddCardActivity() : AppCompatActivity() {

    private var lightTypeface: Typeface? = null

    companion object {
        lateinit var dbHelper: CardDBHelper
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_card_activity)

        this.lightTypeface = FontLoader.light(this)

        //Input types for editViews
        bankAccountView.setInputType(InputType.TYPE_CLASS_NUMBER)
        passwordView.setInputType(InputType.TYPE_CLASS_TEXT)

        //Card Number text formatting
        val cardNumberViewListener = MaskedTextChangedListener("[0000] [0000] [0000] [0000]", cardNumberView)
        cardNumberView.addTextChangedListener(cardNumberViewListener)
        cardNumberView.onFocusChangeListener = cardNumberViewListener

        //Expiration date text formatting
        val expDateViewListener = MaskedTextChangedListener("[00]{/}[00]", expirationDateView)
        expirationDateView.addTextChangedListener(expDateViewListener)
        expirationDateView.onFocusChangeListener = expDateViewListener

        val password = SecurePreferences.getStringValue(this,"authKey", null)
        if (password == null) {
            AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage("Authentication error.")
                .setPositiveButton(R.string.ok) { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    this@AddCardActivity.finish()
                }.show()
        }

        //Adding card button
        dbHelper = CardDBHelper(this, password!!)

        btnAddCard.setOnClickListener {
            val card = Card()
            card.cardName = passwordView.text.toString()
            card.cardNumber = cardNumberView.text.toString()
            card.bankAccount = bankAccountView.text.toString()
            card.expDate = expirationDateView.text.toString()

            dbHelper.addCard(card)

            this.finish()
        }
    }
}