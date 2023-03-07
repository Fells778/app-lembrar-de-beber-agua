package com.example.lembrardebeberagua

import android.app.AlarmManager
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.lembrardebeberagua.databinding.ActivityMainBinding
import com.example.lembrardebeberagua.helpers.EmptyFields
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

    private fun initViews(){
        testView()
        resetData()
        litersPerDayCalculation()
    }

    private fun testView() {
        _binding.buttonAlarm.setOnClickListener {
            val intent = Intent(AlarmClock.ACTION_SET_ALARM)
            intent.putExtra(AlarmClock.EXTRA_LENGTH, "")
            startActivity(intent)

        }
    }


    private fun litersPerDayCalculation() {
        _binding.apply {
            buttonCalc.setOnClickListener {
                if (verificationFields()){
                    val age = editTextAge.text.toString().toInt()
                    val weight = editTextWeight.text.toString().toDouble()
                    val result = textViewResult
                    viewModelMain.calcTotalMl(weight, age, result)
                }else{
                    Toast.makeText(applicationContext, R.string.fill_in_the_fields, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun verificationFields(): Boolean{
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
                editTextName.text = reset.toEditable()
                editTextWeight.text = reset.toEditable()
                textViewResult.text = reset.toEditable()
            }
        }
    }

    private fun String.toEditable():Editable= Editable.Factory.getInstance().newEditable(this)
}