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

    private lateinit var encryption: Encryption
    private var password: String? = null
    private lateinit var iv: ByteArray
    private lateinit var salt: ByteArray

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardListHolder {
        val inflatedView = parent.inflate(R.layout.card_list_item, false)
        val regularTypeFace = FontLoader.regular(inflatedView.context)

//        val saltSet = SecurePreferences.getStringSetValue(inflatedView.context, "salt", HashSet<String>());
//        val stringSalt = saltSet.joinToString()
//        salt = stringSalt.toByteArray()

//        val ivSet = SecurePreferences.getStringSetValue(inflatedView.context, "iv", HashSet<String>())
//        val stringIv = ivSet.joinToString()
//        iv = stringIv.toByteArray()

        val prefs = inflatedView.context.getSharedPreferences("crypto", Context.MODE_PRIVATE)
        val stringSalt = prefs.getString("salt", null)
        val salt = Base64.decode(stringSalt, Base64.NO_WRAP)

        val stringIv = prefs.getString("iv", null)
        val iv = Base64.decode(stringIv, Base64.NO_WRAP)

//        val salt =  prefs.getString("salt", null).toByteArray(Charset.defaultCharset())
//        val iv =  prefs.getStringSet("iv", HashSet<String>()).joinToString().toByteArray(Charset.defaultCharset())
//        salt = SecurePreferences.getStringValue(inflatedView.context, "salt", null)!!.toByteArray(Charset.defaultCharset())
//        iv = SecurePreferences.getStringValue(inflatedView.context, "iv", null)!!.toByteArray(Charset.defaultCharset())
        password = SecurePreferences.getStringValue(inflatedView.context, "authKey", null);
        encryption = Encryption()

        return CardListHolder(inflatedView, regularTypeFace, salt, iv, password, encryption)
    }

    override fun getItemCount() = cards.size

    override fun onBindViewHolder(holder: CardListHolder, position: Int) {
        holder.bind(cards[position], clickListener, deleteButtonClickListener)
    }
}

class CardListHolder(
    v: View,
    private val regularTypeFace: Typeface,
    private val salt: ByteArray,
    private val iv: ByteArray,
    private val password: String?,
    private val encryption: Encryption
) : RecyclerView.ViewHolder(v) {

    fun decryptField(field: String):String {
        return encryption.decrypt(encrypted = Base64.decode(field, Base64.NO_WRAP) , iv = iv, salt = salt, password = password!!.toCharArray())!!.toString(
            Charset.defaultCharset())
    }

    fun bind(card: Card, clickListener: (Card) -> Unit, deleteButtonClickListener: (Card) -> Unit) {
        itemView.cardNameView.text = decryptField(card.cardName!!)
        itemView.cardNameView.typeface = regularTypeFace

        itemView.cardNumberView?.text = decryptField(card.cardNumber!!)
        itemView.cardNumberView?.typeface = regularTypeFace

        itemView.bankAccountView?.text = decryptField(card.bankAccount!!)
        itemView.bankAccountView?.typeface = regularTypeFace

        itemView.expirationDateView?.text = decryptField(card.expDate!!)
        itemView.expirationDateView?.typeface = regularTypeFace

        itemView.cardPin?.setText(decryptField(card.pinCode!!))
        itemView.cardCvv?.setText(decryptField(card.cvvCode!!))

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

        itemView.btnDeleteCard.setOnClickListener { deleteButtonClickListener(card) }
    }
}