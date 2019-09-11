package com.example.cardholder_android

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cardholder_android.Models.Card
import kotlinx.android.synthetic.main.card_list_item.view.*

class CardListAdapter(private val cards: ArrayList<Card>): RecyclerView.Adapter<CardListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardListHolder {
        val inflatedView = parent.inflate(R.layout.card_list_item, false)
        return CardListHolder(inflatedView)
    }

    override fun getItemCount() = cards.size

    override fun onBindViewHolder(holder: CardListHolder, position: Int) {
        val card: Card = cards[position]
        holder.cardNameView?.text = card.cardName
        holder.cardNumberView?.text = card.cardNumber
        holder.bankAccountView?.text = card.bankAccount
        holder.expirationDateView?.text = card.expDate
    }
}


class CardListHolder(v : View) : RecyclerView.ViewHolder(v) {

    val cardNameView = itemView.cardNameView
    val cardNumberView  = itemView.cardNumberView
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