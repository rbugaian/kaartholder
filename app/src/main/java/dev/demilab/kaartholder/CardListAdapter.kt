package dev.demilab.kaartholder

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.demilab.kaartholder.model.Card
import android.text.method.PasswordTransformationMethod
import android.util.Base64
import androidx.core.content.ContextCompat
import de.adorsys.android.securestoragelibrary.SecurePreferences
import dev.demilab.kaartholder.util.Encryption
import dev.demilab.kaartholder.util.FontLoader
import kotlinx.android.synthetic.main.card_info_activity.view.*
import kotlinx.android.synthetic.main.card_list_item.view.*
import kotlinx.android.synthetic.main.card_list_item.view.bankAccountView
import kotlinx.android.synthetic.main.card_list_item.view.cardNumberView
import kotlinx.android.synthetic.main.card_list_item.view.expirationDateView
import java.nio.charset.Charset

class CardListAdapter(
    private val cards: ArrayList<Card>,
    private val clickListener: (Card) -> Unit,
    private val deleteButtonClickListener: (Card) -> Unit
) : RecyclerView.Adapter<CardListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardListHolder {
        val inflatedView = parent.inflate(R.layout.card_list_item, false)
        val regularTypeFace = FontLoader.regular(inflatedView.context)

        return CardListHolder(inflatedView, regularTypeFace)
    }

    override fun getItemCount() = cards.size

    override fun onBindViewHolder(holder: CardListHolder, position: Int) {
        holder.bind(cards[position], clickListener, deleteButtonClickListener)
    }
}

class CardListHolder(
    v: View,
    private val regularTypeFace: Typeface
) : RecyclerView.ViewHolder(v) {

    fun bind(card: Card, clickListener: (Card) -> Unit, deleteButtonClickListener: (Card) -> Unit) {
        if (card.cardName == null) {
            //TODO Нужно убрать эту карточку если расшифровка неполучилась.
            itemView.visibility = View.GONE
        } else {
            itemView.cardNameView.text = card.cardName
            itemView.cardNameView.typeface = regularTypeFace

            itemView.cardNumberView?.text = card.cardNumber
            itemView.cardNumberView?.typeface = regularTypeFace

            itemView.bankAccountView?.text = card.bankAccount
            itemView.bankAccountView?.typeface = regularTypeFace

            itemView.expirationDateView?.text = card.expDate
            itemView.expirationDateView?.typeface = regularTypeFace

            itemView.cardPin?.setText(card.pinCode)
            itemView.cardCvv?.setText(card.cvvCode)

            itemView.btnHide.setOnClickListener { clickListener(card) }

            if (card.isHidden) {
                itemView.btnHide.background =
                    ContextCompat.getDrawable(itemView.context, R.drawable.ic_show)
                itemView.cardPin.transformationMethod = PasswordTransformationMethod()
                itemView.cardCvv.transformationMethod = PasswordTransformationMethod()
            } else {
                itemView.btnHide.background =
                    ContextCompat.getDrawable(itemView.context, R.drawable.ic_hide)
                itemView.cardPin.transformationMethod = null
                itemView.cardCvv.transformationMethod = null
            }

            itemView.deleteCardButton.setOnClickListener { deleteButtonClickListener(card) }
        }
    }
}