package com.example.cardholder_android.Activities

import android.os.Bundle
import android.os.Handler
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cardholder_android.Activities.MainActivity.Companion.dbHelper
import com.example.cardholder_android.CardDBHelper
import com.example.cardholder_android.CardListAdapter
import com.example.cardholder_android.R
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.card_list_item.*

class HomeActivity : AppCompatActivity() {

    /*private lateinit var linearLayoutManager: LinearLayoutManager*/

/*
    companion object {
        lateinit var dbHelper: CardDBHelper
    }
*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        /*linearLayoutManager = LinearLayoutManager(this)
        cardList.layoutManager = linearLayoutManager*/

        dbHelper = CardDBHelper(this)

        viewCards()
    }

    private fun viewCards() {
        val cardList = dbHelper.allCards
        val adapter = CardListAdapter(cardList)
        rvCardList.layoutManager = LinearLayoutManager(this, LinearLayout.HORIZONTAL, false) as RecyclerView.LayoutManager
        rvCardList.adapter = adapter
    }
}