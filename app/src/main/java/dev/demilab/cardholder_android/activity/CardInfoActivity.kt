package dev.demilab.cardholder_android.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import dev.demilab.cardholder_android.CardDBHelper
import com.redmadrobot.inputmask.MaskedTextChangedListener
import de.adorsys.android.securestoragelibrary.SecurePreferences
import dev.demilab.cardholder_android.R
import kotlinx.android.synthetic.main.card_info_activity.*

class CardInfoActivity : AppCompatActivity() {

    companion object {
        lateinit var dbHelper: CardDBHelper;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card_info_activity)

        val password = SecurePreferences.getStringValue(this,"authKey", null)
        if (password == null) {
            AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage("Authentication error.")
                .setPositiveButton(R.string.ok) { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    this@CardInfoActivity.finish()
                }.show()
        }

        dbHelper =
            CardDBHelper(this, password!!)

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