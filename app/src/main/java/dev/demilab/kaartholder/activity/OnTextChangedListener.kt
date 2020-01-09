package dev.demilab.kaartholder.activity

import android.text.Editable
import android.text.TextWatcher

abstract class OnTextChangedListener : TextWatcher {

    internal abstract fun onTextChanged(text: String)

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable) {
        onTextChanged(s.toString())
    }
}