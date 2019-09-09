package com.example.cardholder_android

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.cardholder_android.Models.Card
import kotlinx.android.synthetic.main.card_list_item.view.*

class CardListHolder(v : View) : RecyclerView.ViewHolder(v) {

    val cardNameView = itemView.cardNameView
    val cardNumberView = itemView.cardNumberView
    val bankAccountView = itemView.bankAccountView
    val expirationDateView = itemView.expirationDateView

    /*private var view: View = v
    private var card: Card? = null

    init {
        v.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        Log.d("RecyclerView", "Click")
    }

    companion object {
        private val CARD_KEY = "CARD"
    }*/


}