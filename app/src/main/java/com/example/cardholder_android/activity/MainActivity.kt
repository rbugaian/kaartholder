package com.example.cardholder_android.activity

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cardholder_android.CardDBHelper
import com.example.cardholder_android.CardListAdapter
import com.example.cardholder_android.model.Card
import com.example.cardholder_android.R
import com.example.cardholder_android.util.FontLoader
import de.adorsys.android.securestoragelibrary.SecurePreferences
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_main.addCardButton

class MainActivity : AppCompatActivity() {

    private var regularTypeface: Typeface? = null

    companion object {
        lateinit var dbHelper: CardDBHelper
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val password = SecurePreferences.getStringValue(this,"authKey", null)
        if (password == null) {
            AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage("Authentication error.")
                .setPositiveButton(R.string.ok) { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                    this@MainActivity.finish()
                }.show()
        }

        dbHelper = CardDBHelper(this, password!!)
        renderCustomFont()

        if (!dbHelper.isEmpty()) {
            setContentView(R.layout.activity_home)

            renderCards()
        }

        addCardButton.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            startActivityForResult(intent, 558)
//            startActivity(intent)
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
            rvCardList.adapter?.notifyDataSetChanged()
        }
    }

    fun renderCards() {
        val cardList = dbHelper.allCards
        val adapter = CardListAdapter(cardList) { card: Card -> cardItemClicked(card) }

        rvCardList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvCardList.adapter = adapter

        adapter.notifyDataSetChanged()
    }

    private fun cardItemClicked(cardItem: Card) {
        cardItem.isHidden = !cardItem.isHidden
        rvCardList.adapter?.notifyDataSetChanged()
    }
}