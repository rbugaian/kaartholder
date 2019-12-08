package com.example.cardholder_android.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cardholder_android.CardDBHelper
import com.example.cardholder_android.CardListAdapter
import com.example.cardholder_android.model.Card
import com.example.cardholder_android.R
import kotlinx.android.synthetic.main.activity_main.addCardButton
import kotlinx.android.synthetic.main.activity_home.rvCardList

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var dbHelper: CardDBHelper
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = CardDBHelper(this)

        if (!dbHelper.isEmpty()) {
            setContentView(R.layout.activity_home)

            dbHelper = CardDBHelper(this)

            renderCards()
        }

        addCardButton.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        rvCardList.adapter?.notifyDataSetChanged()
    }

    private fun renderCards() {
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