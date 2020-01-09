package dev.demilab.kaartholder.activity

import android.content.Context
import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import dev.demilab.kaartholder.CardDBHelper
import dev.demilab.kaartholder.model.Card
import dev.demilab.kaartholder.util.FontLoader
import com.redmadrobot.inputmask.MaskedTextChangedListener
import de.adorsys.android.securestoragelibrary.SecurePreferences
import dev.demilab.kaartholder.R
import dev.demilab.kaartholder.util.Encryption
import kotlinx.android.synthetic.main.add_card_activity.*
import java.security.SecureRandom
import android.util.Base64


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
        val cardNumberViewListener =
            MaskedTextChangedListener("[0000] [0000] [0000] [0000]", cardNumberView)
        cardNumberView.addTextChangedListener(cardNumberViewListener)
        cardNumberView.onFocusChangeListener = cardNumberViewListener

        //Expiration date text formatting
        val expDateViewListener = MaskedTextChangedListener("[00]{/}[00]", expirationDateView)
        expirationDateView.addTextChangedListener(expDateViewListener)
        expirationDateView.onFocusChangeListener = expDateViewListener

        val password = SecurePreferences.getStringValue(this, "authKey", null)
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
        dbHelper =
            CardDBHelper(this, password!!)

        //Generate iv and salt, then store it in SecureStorage
        var salt = ByteArray(256)
        var iv = ByteArray(16)

        if (SecurePreferences.contains(this, "salt") && SecurePreferences.contains(this, "iv")) {
//            val saltSet = SecurePreferences.getStringSetValue(this, "salt", HashSet<String>());
//            val stringSalt = saltSet.joinToString()
//            salt = stringSalt.toByteArray()
////            salt = SecurePreferences.getStringValue(this, "salt", null)!!.toByteArray(Charset.defaultCharset())
//
//            val ivSet = SecurePreferences.getStringSetValue(this, "iv", HashSet<String>())
//            val stringIv = ivSet.joinToString()
//            iv = stringIv.toByteArray()
        } else {
            val random = SecureRandom()
            //Generating salt
            random.nextBytes(salt)

            //Generating IV
            val ivRandom = SecureRandom()
            ivRandom.nextBytes(iv)


            val prefs = getSharedPreferences("crypto", Context.MODE_PRIVATE)
            val editor = prefs.edit();

            val stringSalt = Base64.encodeToString(salt, Base64.NO_WRAP)
            editor.putString("salt", stringSalt)

            val stringIv = Base64.encodeToString(iv, Base64.NO_WRAP)
            editor.putString("iv", stringIv)

            editor.apply()
        }

        btnAddCard.setOnClickListener {
            val card = Card()
            val encryption = Encryption()

            val cardName = passwordView.text.toString()
            card.cardName = encryptedField(encryption, password, salt, iv, cardName)

            val cardNumber = cardNumberView.text.toString()
            card.cardNumber = encryptedField(encryption, password, salt, iv, cardNumber)

            val bankAccount = bankAccountView.text.toString()
            card.bankAccount = encryptedField(encryption, password, salt, iv, bankAccount)

            val expDate = expirationDateView.text.toString()
            card.expDate = encryptedField(encryption, password, salt, iv, expDate)

            val pinCode = pinCodeView.text.toString()
            card.pinCode = encryptedField(encryption, password, salt, iv, pinCode)

            val cvvCode = cvvCodeView.text.toString()
            card.cvvCode = encryptedField(encryption, password, salt, iv, cvvCode)

            dbHelper.addCard(card)

            this.finish()
        }
    }

    private fun encryptedField(
        encryption: Encryption,
        password: String,
        salt: ByteArray,
        iv: ByteArray,
        cardNumber: String
    ): String {
        return Base64.encodeToString(encryption.encrypt(
            password = password.toCharArray(), salt = salt, iv = iv,
            dataToEncrypt = cardNumber.toByteArray()
        )["encrypted"]!!, Base64.NO_WRAP)
    }
}