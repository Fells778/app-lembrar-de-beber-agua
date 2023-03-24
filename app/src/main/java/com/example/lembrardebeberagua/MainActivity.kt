package com.example.lembrardebeberagua

import android.app.AlarmManager
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.AlarmClock
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.lembrardebeberagua.databinding.ActivityMainBinding
import com.example.lembrardebeberagua.helpers.EmptyFields
import com.example.lembrardebeberagua.helpers.Mask
import com.example.lembrardebeberagua.model.ViewModelMain

class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding
    private lateinit var viewModelMain: ViewModelMain

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        viewModelMain = ViewModelProvider(this)[ViewModelMain::class.java]

        initViews()

        viewModelMain.alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        setContentView(_binding.root)
    }

    private fun initViews() {
        openAlarm()
        resetData()
        maskEditText()
        litersPerDayCalculation()
    }

    private fun openAlarm() {
        _binding.buttonAlarm.setOnClickListener {
            val intent = Intent(AlarmClock.ACTION_SET_ALARM)
            intent.putExtra(AlarmClock.EXTRA_LENGTH, "")
            startActivity(intent)
        }
    }


    private fun litersPerDayCalculation() {
        _binding.apply {
            buttonCalc.setOnClickListener {
                if (verificationFields()) {
                    dialogLoading()
                    val age = editTextAge.text.toString().toInt()
                    val weight = editTextWeight.text.toString().toDouble()
                    val result = textViewResult
                    viewModelMain.calcTotalMl(weight, age, result)
                    viewModelMain.setVisibleMessage(textViewLltResult, constraintLayoutResult)
                    viewModelMain.calcBottleAndGlass(textViewBottle, textViewGlass)
                    hiderKeyboard()
                } else {
                    Toast.makeText(
                        applicationContext,
                        R.string.fill_in_the_fields,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun verificationFields(): Boolean {
        _binding.apply {
            if (
                EmptyFields.fieldEmpty(editTextWeight, applicationContext) &&
                EmptyFields.fieldEmpty(editTextAge, applicationContext)
            ) return true
        }
        return false
    }

    private fun resetData() {
        _binding.apply {
            imageViewReset.setOnClickListener {
                val reset = ""
                editTextAge.text = reset.toEditable()
                editTextWeight.text = reset.toEditable()
                textViewResult.text = getString(R.string.text_lt)
                constraintLayoutResult.visibility = View.INVISIBLE
            }
        }
    }

    private fun hiderKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(_binding.buttonCalc.windowToken, 0)
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun dialogLoading() {
        val dialog = Dialog(this)
        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
        }, 1000)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun maskEditText() {
        _binding.apply {
            editTextWeight.addTextChangedListener(Mask.mask(editTextWeight, Mask.FORMAT_WEIGHT))
        }
    }
}