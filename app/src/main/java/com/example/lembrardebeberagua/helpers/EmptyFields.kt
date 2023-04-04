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

    fun fieldAWeight(editText: EditText, context: Context): Boolean{
        return if(editText.text.length <= 3) {
            editText.error = context.getString(R.string.field_weight)
            editText.requestFocus()
            false
        }else{
            true
        }
    }
}