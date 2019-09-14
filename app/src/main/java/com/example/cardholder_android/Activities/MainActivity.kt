package com.example.cardholder_android.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cardholder_android.CardDBHelper
import com.example.cardholder_android.CardListAdapter
import com.example.cardholder_android.Models.Card
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

        //If database is empty, set empty layout
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

    //Function displaying all cards
    private fun renderCards() {
        val cardList = dbHelper.allCards
        val adapter = CardListAdapter(cardList, {card: Card -> cardItemClicked(card)})

        rvCardList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvCardList.adapter = adapter

        adapter.notifyDataSetChanged()
    }

    private fun cardItemClicked(cardItem : Card) {
        Toast.makeText(this, "Clicked: ${cardItem.cardName}", Toast.LENGTH_LONG).show()

        val intent = Intent(this, CardInfoActivity::class.java)
        intent.putExtra("card_id", cardItem.id)
        startActivity(intent)
    }
}