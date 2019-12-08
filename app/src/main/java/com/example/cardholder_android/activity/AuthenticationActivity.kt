package com.example.cardholder_android.activity

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import co.infinum.goldfinger.Goldfinger
import com.example.cardholder_android.BuildConfig
import com.example.cardholder_android.R
import com.example.cardholder_android.util.FontLoader
import de.adorsys.android.securestoragelibrary.SecurePreferences
import kotlinx.android.synthetic.main.activity_authentication.*

class AuthenticationActivity : AppCompatActivity() {

    private var lightTypeface: Typeface? = null
    private var regularTypeface: Typeface? = null
    private val KEY_NAME = "key"

    private var cancelButton: View? = null

    private var encryptedValue: String? = null
    private var goldfinger: Goldfinger? = null
    private var statusView: TextView? = null

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        renderCustomFont()

        goldfinger = Goldfinger.Builder(this).logEnabled(BuildConfig.DEBUG).build()

        val storedKey = SecurePreferences.getStringValue(this, "authKey", null)
        if (storedKey == null) {
            handleNonExistingKey(storedKey)
        } else {
            handleExistingKey(storedKey)
        }
    }

    private fun handleExistingKey(storedKey: String?) {
        auth_button.setText(R.string.authenticate)
        passwordConfirmView.visibility = View.GONE

        val encryptedValue = SecurePreferences.getStringValue(
            this@AuthenticationActivity, "encryptedValue",
            null
        )

        if (!goldfinger!!.canAuthenticate() && encryptedValue == null) {
            try_fingerprint_button.visibility = View.GONE
        } else if (encryptedValue != null) {
            try_fingerprint_button.setOnClickListener {
                goldfinger!!.decrypt(
                    buildPromptParams(),
                    KEY_NAME,
                    encryptedValue,
                    object : Goldfinger.Callback {
                        override fun onError(e: Exception) {
                            onGoldfingerError()
                        }

                        override fun onResult(result: Goldfinger.Result) {
                            onGoldfingerResult(result)
                        }
                    })
            }
        }

        auth_button.setOnClickListener {
            if (storedKey == passwordView.text.toString()) {
                Toast.makeText(this, "Authentication: Success!", Toast.LENGTH_SHORT).show()
                startMainActivity()
            } else {
                Toast.makeText(this, "Authentication: Failure!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleNonExistingKey(storedKey: String?) {
        var storedKey1 = storedKey
        auth_button.setText(R.string.register)
        try_fingerprint_button.visibility = View.GONE

        auth_button.setOnClickListener {
            val password = passwordView.text
            val passwordConfirm = passwordConfirmView.text
            if (password == null ||
                password.isEmpty() ||
                passwordConfirm == null ||
                passwordConfirm.isEmpty()
            ) {
                AlertDialog.Builder(this@AuthenticationActivity)
                    .setTitle("Oops")
                    .setMessage("One of the fields empty. Please fill both fields.")
                    .setPositiveButton(R.string.ok) { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                    }.show()
                return@setOnClickListener
            }

            if (password.toString() != passwordConfirm.toString()) {
                AlertDialog.Builder(this@AuthenticationActivity)
                    .setTitle("Oops")
                    .setMessage("Password and confirmation do not match.")
                    .setPositiveButton(R.string.ok) { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                    }.show()
                return@setOnClickListener
            }

            storedKey1 = password.toString()
            SecurePreferences.setValue(
                this@AuthenticationActivity,
                "authKey",
                storedKey1.toString()
            )

            goldfinger!!.encrypt(
                buildPromptParams(),
                KEY_NAME,
                password.toString(),
                object : Goldfinger.Callback {
                    override fun onError(e: Exception) {
                        onGoldfingerError()
                    }

                    override fun onResult(result: Goldfinger.Result) {
                        onGoldfingerResult(result)
                        encryptedValue = result.value()
                        SecurePreferences.setValue(
                            this@AuthenticationActivity, "encryptedValue",
                            encryptedValue.toString()
                        )
                    }
                })
        }
    }

    private fun renderCustomFont() {
        this.lightTypeface = FontLoader.light(this)
        this.regularTypeface = FontLoader.regular(this)
        this.mainTitle.typeface = lightTypeface
        this.passwordViewLayout.typeface = regularTypeface
        this.passwordConfirmViewLayout.typeface = regularTypeface
        this.passwordView.typeface = regularTypeface
        this.passwordConfirmView.typeface = regularTypeface
        this.auth_button.typeface = regularTypeface
        this.try_fingerprint_button.typeface = regularTypeface
    }

    private fun buildPromptParams(): Goldfinger.PromptParams {
        return Goldfinger.PromptParams.Builder(this)
            .description("")
            .subtitle("")
            .title("Authenticate")
            .negativeButtonText("Cancel")
            .build()
    }

    private fun onGoldfingerError() {
        cancelButton!!.setEnabled(false)
        statusView!!.setTextColor(ContextCompat.getColor(this, R.color.error))
        statusView!!.text = getString(R.string.error)
    }

    private fun onGoldfingerResult(result: Goldfinger.Result) {
        if (result.type() == Goldfinger.Type.SUCCESS) {
            Toast.makeText(this, "Authentication: Success!", Toast.LENGTH_SHORT).show()
            startMainActivity()
        } else {
            Toast.makeText(this, "Authentication: Failure.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startMainActivity() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        startActivity(mainActivityIntent)
        this.finish()
    }
}
