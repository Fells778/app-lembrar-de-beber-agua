package com.example.lembrardebeberagua.model

import android.app.AlarmManager
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.util.*
import kotlin.math.ceil

class ViewModelMain : ViewModel() {
    private val youngML = 40.0
    private val adultML = 35.0
    private val agedML = 30.0
    private val moreThan60YearsML = 25.0
    private var resultMl = 0.0
    private var totalResultML = 0.0
    private val format = NumberFormat.getNumberInstance(Locale("pt", "BR"))
    var alarmManager: AlarmManager? = null

    fun calcTotalMl(weight: Double, age: Int, textView: TextView) {
        if (age <= 17) {
            resultMl = weight * youngML
            totalResultML = resultMl
        } else if (age <= 55) {
            resultMl = weight * adultML
            totalResultML = resultMl
        } else if (age <= 65) {
            resultMl = weight * agedML
            totalResultML = resultMl
        } else {
            resultMl = weight * moreThan60YearsML
            totalResultML = resultMl
        }
        format.isGroupingUsed = false
        textView.text = "${ceil(resultMl / 1000).toInt()} L de água /dia"
    }

    fun setVisibleMessage(
        textView: TextView,
        textResult: TextView,
        constraintLayout: ConstraintLayout
    ) {
        textView.visibility = View.INVISIBLE
        constraintLayout.visibility = View.VISIBLE
        textResult.text =
            "A quantidade sem arredondamento é de: " + format.format(resultMl / 1000) + "" + " L de água /dia"
        textResult.visibility = View.VISIBLE
    }

    fun calcBottleAndGlass(bottle: TextView, glass: TextView) {
        bottle.text = "${ceil(resultMl / 500).toInt()} garrafinhas de 500ml"
        glass.text = "${ceil(resultMl / 250).toInt()} copos de 250ml"
    }
}