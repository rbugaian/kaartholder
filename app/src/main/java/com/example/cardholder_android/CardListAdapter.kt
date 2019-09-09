package com.example.cardholder_android

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cardholder_android.Models.Card

class CardListAdapter(private val cards: ArrayList<Card>): RecyclerView.Adapter<CardListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardListHolder {
        val inflatedView = parent.inflate(R.layout.card_list_item, false)
        return CardListHolder(inflatedView)
    }

    override fun getItemCount() = cards.size

    override fun onBindViewHolder(holder: CardListHolder, position: Int) {
        val card: Card = cards[position]
        holder.cardNameView.text = card.cardName
        holder.cardNumberView.text = card.cardNumber
        holder.bankAccountView.text = card.bankAccount
        holder.expirationDateView.text = card.expDate
    }
}