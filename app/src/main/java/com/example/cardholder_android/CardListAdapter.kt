package com.example.cardholder_android

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cardholder_android.Models.Card
import kotlinx.android.synthetic.main.card_list_item.view.*
import android.graphics.BlurMaskFilter
import androidx.core.view.ViewCompat.setLayerType
import android.os.Build
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.add_card_activity.view.*
import kotlinx.android.synthetic.main.card_list_item.view.bankAccountView
import kotlinx.android.synthetic.main.card_list_item.view.cardNameView
import kotlinx.android.synthetic.main.card_list_item.view.cardNumberView
import kotlinx.android.synthetic.main.card_list_item.view.expirationDateView
import kotlinx.android.synthetic.main.card_list_item.view.btnHide
import kotlinx.android.synthetic.main.card_list_item.view.cardPin
import kotlinx.android.synthetic.main.card_list_item.view.cardCvv

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
        itemView.btnHide.setOnClickListener{clickListener(card)}

        if (card.isHidden) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                itemView.btnHide.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_show)
            } else {
                itemView.btnHide.setBackgroundDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_show))
            }
            itemView.cardPin.transformationMethod = PasswordTransformationMethod()
            itemView.cardCvv.transformationMethod = PasswordTransformationMethod()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                itemView.btnHide.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_hide)
            } else {
                itemView.btnHide.setBackgroundDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_hide))
            }
            itemView.cardPin.transformationMethod = null
            itemView.cardCvv.transformationMethod = null
        }
    }
}