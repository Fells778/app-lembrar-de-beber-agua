package com.example.lembrardebeberagua.helpers

import android.content.Context
import android.widget.EditText
import com.example.lembrardebeberagua.R

object EmptyFields {

    fun fieldEmpty(editText: EditText, context: Context): Boolean{
        return if(editText.text!!.isEmpty()) {
            editText.error = context.getString(R.string.empty_field)
            editText.requestFocus()
            false
        } else {
            true
        }
    }

}