package com.example.cardholder_android

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.cardholder_android.Activities.CardInfoActivity
import com.example.cardholder_android.Activities.MainActivity
import com.example.cardholder_android.Models.Card
import kotlinx.android.synthetic.main.card_list_item.view.*


class CardListAdapter(private val cards: ArrayList<Card>,
                      private val clickListener: (Card) -> Unit) : RecyclerView.Adapter<CardListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardListHolder {
        val inflatedView = parent.inflate(R.layout.card_list_item, false)
        return CardListHolder(inflatedView)
    }

    override fun getItemCount() = cards.size

    override fun onBindViewHolder(holder: CardListHolder, position: Int) {
        /*val card: Card = cards[position]*/
        /*holder.cardNameView?.text = card.cardName
        holder.cardNumberView?.text = card.cardNumber
        holder.bankAccountView?.text = card.bankAccount
        holder.expirationDateView?.text = card.expDate*/

        holder.bind(cards[position], clickListener)
    }
}

class CardListHolder(v: View) : RecyclerView.ViewHolder(v) {

    fun bind(card: Card, clickListener: (Card) -> Unit) {
        itemView.cardNameView.text = card.cardName
        itemView.cardNumberView?.text = card.cardNumber
        itemView.bankAccountView?.text = card.bankAccount
        itemView.expirationDateView?.text = card.expDate
        itemView.setOnClickListener{ clickListener(card)}
    }
}