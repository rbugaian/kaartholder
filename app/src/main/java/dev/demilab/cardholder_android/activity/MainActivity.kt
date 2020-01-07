package dev.demilab.cardholder_android.activity

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.demilab.cardholder_android.CardDBHelper
import dev.demilab.cardholder_android.CardListAdapter
import dev.demilab.cardholder_android.model.Card
import dev.demilab.cardholder_android.util.FontLoader
import de.adorsys.android.securestoragelibrary.SecurePreferences
import dev.demilab.cardholder_android.KaartholderApplication
import dev.demilab.cardholder_android.R
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_main.addCardButton

class MainActivity : AppCompatActivity() {

    private var currentApp: KaartholderApplication? = null

    private var regularTypeface: Typeface? = null

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
        if (cardList.count() > 0) {
            this.addCardHint.visibility = View.GONE
        }

        val adapter =
            CardListAdapter(cardList) { card: Card ->
                cardItemClicked(card)
            }

        rvCardList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvCardList.adapter = adapter

        adapter.notifyDataSetChanged()
    }

    private fun cardItemClicked(cardItem: Card) {
        cardItem.isHidden = !cardItem.isHidden
        rvCardList.adapter?.notifyDataSetChanged()
    }
}