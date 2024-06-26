package dev.demilab.kaartholder.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import co.infinum.goldfinger.Goldfinger
import de.adorsys.android.securestoragelibrary.SecurePreferences
import dev.demilab.kaartholder.R
import dev.demilab.kaartholder.util.FontLoader
import kotlinx.android.synthetic.main.activity_authentication.*

class AuthenticationActivity : AppCompatActivity() {

    private var lightTypeface: Typeface? = null
    private var regularTypeface: Typeface? = null
    private val KEY_NAME = "key"
    private var encryptedValue: String? = null
    private var goldfinger: Goldfinger? = null

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        renderCustomFont()

        goldfinger = Goldfinger.Builder(this).logEnabled(true).build()

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

        val prefs = getSharedPreferences("auth", Context.MODE_PRIVATE)
        val didDeclineBiometricAuth = prefs.getBoolean("did_decline_biometric", false)

        if (!goldfinger!!.canAuthenticate() || encryptedValue == null) {
            try_fingerprint_button.visibility = View.GONE
        } else if (encryptedValue != null) {
            try_fingerprint_button.setOnClickListener {
                goldfinger!!.decrypt(
                    buildPromptParams(),
                    KEY_NAME,
                    encryptedValue,
                    object : Goldfinger.Callback {
                        override fun onError(e: Exception) {
                            Toast.makeText(
                                this@AuthenticationActivity,
                                "Fingerprint authentication error.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResult(result: Goldfinger.Result) {
                            onGoldfingerResult(result)
                        }
                    })
            }
        }

        auth_button.setOnClickListener {
            if (storedKey == passwordView.text.toString()) {
                if (encryptedValue == null && !didDeclineBiometricAuth) {
                    AlertDialog.Builder(this@AuthenticationActivity)
                        .setTitle("Hey")
                        .setMessage("Would you like to set up biometric authentication?")
                        .setNegativeButton(R.string.cancel) { dialogInterface: DialogInterface, _: Int ->
                            dialogInterface.dismiss()

                            Toast.makeText(this, "Authentication: Success!", Toast.LENGTH_SHORT)
                                .show()
                            startMainActivity()

                            prefs.edit().putBoolean("did_decline_biometric", true).apply();
                        }
                        .setPositiveButton(R.string.yes) { dialogInterface: DialogInterface, _: Int ->
                            dialogInterface.dismiss()
                            setUpBiometricAuthentication(storedKey)
                        }.show()
                } else {
                    Toast.makeText(this, "Authentication: Success!", Toast.LENGTH_SHORT)
                        .show()
                    startMainActivity()
                }
            } else {
                Toast.makeText(this, "Authentication: Failure!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpBiometricAuthentication(storedKey: String?) {
//        try_fingerprint_button.setOnClickListener {
        goldfinger!!.encrypt(
            buildPromptParams(),
            KEY_NAME,
            storedKey!!,
            object : Goldfinger.Callback {
                override fun onError(e: Exception) {
                    Toast.makeText(
                        this@AuthenticationActivity,
                        "Fingerprint authentication error.",
                        Toast.LENGTH_SHORT
                    ).show()
                    e.printStackTrace();
                }

                override fun onResult(result: Goldfinger.Result) {
                    onGoldfingerResult(result)
                    val resultValue = result.value()
                    SecurePreferences.setValue(
                        this@AuthenticationActivity, "encryptedValue",
                        resultValue.toString()
                    )
                }
            })
//        }
    }

    private fun handleNonExistingKey(storedKey: String?) {
        var storedKey1: String?
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

            startMainActivity()


//            goldfinger!!.encrypt(
//                buildPromptParams(),
//                KEY_NAME,
//                password.toString(),
//                object : Goldfinger.Callback {
//                    override fun onError(e: Exception) {
//                        Toast.makeText(
//                            this@AuthenticationActivity,
//                            "Fingerprint authentication error.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        e.printStackTrace();
//                    }
//
//                    override fun onResult(result: Goldfinger.Result) {
//                        onGoldfingerResult(result)
//                        encryptedValue = result.value()
//                        SecurePreferences.setValue(
//                            this@AuthenticationActivity, "encryptedValue",
//                            encryptedValue.toString()
//                        )
//                    }
//                })
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

    private fun onGoldfingerResult(result: Goldfinger.Result) {
        if (result.type() == Goldfinger.Type.SUCCESS) {
            Toast.makeText(this, "Authentication: Success!", Toast.LENGTH_SHORT).show()
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        startActivity(mainActivityIntent)
        this.finish()
    }
}
