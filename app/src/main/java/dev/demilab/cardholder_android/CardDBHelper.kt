package dev.demilab.cardholder_android

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import dev.demilab.cardholder_android.model.Card

class CardDBHelper(context: Context, userPassword: String) :
    SQLiteOpenHelper(context,
        DATABASE_NAME, null,
        DATABASE_VER
    ) {

    companion object {
        private val DATABASE_VER = 1
        private val DATABASE_NAME = "EDMTDB.db"

        //Table
        private val TABLE_NAME = "Card"
        private val COL_ID = "Id"
        private val COL_CARD_NAME = "Card_Name"
        private val COL_CARD_NUMBER = "Card_Number"
        private val COL_BANK_ACCOUNT = "Bank_account"
        private val COL_EXP_DATE = "Expiration_date"
        private val COL_PIN_CODE = "Pin_Code"
        private val COL_CVV_CODE = "CVV_Code"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY = ("CREATE TABLE $TABLE_NAME ( " +
                "$COL_ID INTEGER PRIMARY KEY, " +
                "$COL_CARD_NAME TEXT, " +
                "$COL_CARD_NUMBER TEXT, " +
                "$COL_BANK_ACCOUNT TEXT, " +
                "$COL_EXP_DATE TEXT," +
                "$COL_PIN_CODE TEXT," +
                "$COL_CVV_CODE TEXT)")

        db!!.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    //CRUD
    val allCards: ArrayList<Card>
        get() {
            val lstCard = ArrayList<Card>()
            val selectQuery = "SELECT * FROM $TABLE_NAME"
            val db: SQLiteDatabase = this.writableDatabase
            val cursor: Cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val card = Card()
                    card.id = cursor.getInt(cursor.getColumnIndex(COL_ID))
                    card.cardName = cursor.getString(cursor.getColumnIndex(COL_CARD_NAME))
                    card.cardNumber = cursor.getString(cursor.getColumnIndex(COL_CARD_NUMBER))
                    card.bankAccount = cursor.getString(cursor.getColumnIndex(COL_BANK_ACCOUNT))
                    card.expDate = cursor.getString(cursor.getColumnIndex(COL_EXP_DATE))
                    card.pinCode = cursor.getString(cursor.getColumnIndex(COL_PIN_CODE))
                    card.cvvCode = cursor.getString(cursor.getColumnIndex(COL_CVV_CODE))

                    lstCard.add(card)
                } while (cursor.moveToNext())
            }
            db.close()
            return lstCard
        }

    fun getCardById(cardId : Int): Card {
        val db: SQLiteDatabase = this.writableDatabase
        val cursor : Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COL_ID = " + cardId, null)

        if (cursor.moveToNext()) {
            val card = Card()
            card.id = cursor.getInt(cursor.getColumnIndex(COL_ID))
            card.cardName = cursor.getString(cursor.getColumnIndex(COL_CARD_NAME))
            card.cardNumber = cursor.getString(cursor.getColumnIndex(COL_CARD_NUMBER))
            card.bankAccount = cursor.getString(cursor.getColumnIndex(COL_BANK_ACCOUNT))
            card.expDate = cursor.getString(cursor.getColumnIndex(COL_EXP_DATE))
            card.pinCode = cursor.getString(cursor.getColumnIndex(COL_PIN_CODE))
            card.cvvCode = cursor.getString(cursor.getColumnIndex(COL_CVV_CODE))

            cursor.close()
            return card
        }
        return Card()
    }

    fun addCard(card: Card) {
        val db: SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        /*values.put(COL_ID, card.id)*/
        values.put(COL_CARD_NAME, card.cardName)
        values.put(COL_CARD_NUMBER, card.cardNumber)
        values.put(COL_BANK_ACCOUNT, card.bankAccount)
        values.put(COL_EXP_DATE, card.expDate)
        values.put(COL_PIN_CODE, card.pinCode)
        values.put(COL_CVV_CODE, card.cvvCode)

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateCard(card: Card): Int {
        val db: SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put(COL_ID, card.id)
        values.put(COL_CARD_NAME, card.cardName)
        values.put(COL_CARD_NUMBER, card.cardNumber)
        values.put(COL_BANK_ACCOUNT, card.bankAccount)
        values.put(COL_EXP_DATE, card.expDate)

        return db.update(TABLE_NAME, values, "$COL_ID = ?", arrayOf(card.id.toString()))
    }

    fun deleteCard(card: Card) {
        val db: SQLiteDatabase = this.writableDatabase

        db.delete(TABLE_NAME, "$COL_ID = ?", arrayOf(card.id.toString()))
        db.close()
    }

    fun isEmpty(): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor: Cursor = db.rawQuery(selectQuery, null)
        val count = cursor.count
        cursor.close()
        return (count == 0)
    }
}