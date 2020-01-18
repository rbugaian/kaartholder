package dev.demilab.kaartholder.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.demilab.kaartholder.util.CardDBHelper
import dev.demilab.kaartholder.CardListAdapter
import dev.demilab.kaartholder.model.Card
import dev.demilab.kaartholder.util.FontLoader
import de.adorsys.android.securestoragelibrary.SecurePreferences
import dev.demilab.kaartholder.KaartholderApplication
import dev.demilab.kaartholder.R
import dev.demilab.kaartholder.util.Encryption
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_main.addCardButton
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    private var currentApp: KaartholderApplication? = null

    private var regularTypeface: Typeface? = null

    private lateinit var encryption: Encryption
    private var password: String? = null
    private lateinit var iv: ByteArray
    private lateinit var salt: ByteArray

    companion object {
        lateinit var dbHelper: CardDBHelper
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        this.currentApp = this.application as KaartholderApplication;
        this.currentApp?.setCurrentActivity(this)

        val password = SecurePreferences.getStringValue(this, "authKey", null)
        if (password == null) {
            AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage("Authentication error.")
                .setPositiveButton(R.string.ok) { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    this@MainActivity.finish()
                }.show()
        }

        renderCustomFont()
        dbHelper =
            CardDBHelper(this, password!!)
        if (!dbHelper.isEmpty()) {
//            setContentView(R.layout.activity_home)
            this.addCardHint.visibility = View.GONE
            renderCards()
        }

        addCardButton.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            startActivityForResult(intent, 558)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 558) {
            this.renderCards()
        }
    }

    fun renderCustomFont() {
        this.regularTypeface = FontLoader.regular(this)
    }

    override fun onResume() {
        super.onResume()
        if (rvCardList != null) {
            this.renderCards()
        }
    }

    private fun renderCards() {
        val cardList = dbHelper.allCards
        //val decryptedCardList = ArrayList<Card>()
        if (cardList.count() > 0) {
            this.addCardHint.visibility = View.GONE

            val prefs = this@MainActivity.getSharedPreferences("crypto", Context.MODE_PRIVATE)
            val stringSalt = prefs.getString("salt", null)
            salt = Base64.decode(stringSalt, Base64.NO_WRAP)

            val stringIv = prefs.getString("iv", null)
            iv = Base64.decode(stringIv, Base64.NO_WRAP)

            password = SecurePreferences.getStringValue(this@MainActivity, "authKey", null)
            encryption = Encryption()

            cardList.forEach {
                it.cardName = decryptField(it.cardName!!)
                if (it.cardName == null) {
                    dbHelper.deleteCard(it)
                    cardList.remove(it)
                } else {
                    it.cardNumber = decryptField(it.cardNumber!!)
                    it.bankAccount = decryptField(it.bankAccount!!)
                    it.expDate = decryptField(it.expDate!!)
                    it.cvvCode = decryptField(it.cvvCode!!)
                    it.pinCode = decryptField(it.pinCode!!)
                }
            }
        }

        val adapter =
            CardListAdapter(cardList, {card: Card -> cardItemClicked(card)}, {card: Card -> deleteButtonClicked(card)})

        rvCardList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvCardList.adapter = adapter

        adapter.notifyDataSetChanged()
    }

    private fun cardItemClicked(cardItem: Card) {
        cardItem.isHidden = !cardItem.isHidden
        rvCardList.adapter?.notifyDataSetChanged()
    }

    private fun deleteButtonClicked(card: Card) {
        AlertDialog.Builder(this)
            .setTitle("Warning!")
            .setMessage("Are you sure you want to delete this card?")
            .setNegativeButton("CANCEL"){dialog, which ->
                Log.d("LOG", "Canceled")
            }
            .setPositiveButton("YES"){dialog, which ->
                dbHelper.deleteCard(card)
                if(dbHelper.isEmpty()) {
                    this.addCardHint.visibility = View.VISIBLE
                    renderCards()
                } else {
                    renderCards()
                }
            }.show()
    }

    private fun decryptField(field: String):String? {
        return encryption.decrypt(encrypted = Base64.decode(field, Base64.NO_WRAP) , iv = iv, salt = salt, password = password!!.toCharArray())
            ?.toString(
                Charset.defaultCharset())
    }
}