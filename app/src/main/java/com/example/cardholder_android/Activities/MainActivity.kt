package com.example.cardholder_android.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cardholder_android.CardDBHelper
import com.example.cardholder_android.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var dbHelper: CardDBHelper
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = CardDBHelper(this)

        if (dbHelper.isEmpty()) {
            addCardButton.setOnClickListener() {
                val intent = Intent(this, AddCardActivity::class.java)
                startActivity(intent)
            }
        } else {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

        }
    }
}
