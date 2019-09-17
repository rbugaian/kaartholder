package com.example.cardholder_android

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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