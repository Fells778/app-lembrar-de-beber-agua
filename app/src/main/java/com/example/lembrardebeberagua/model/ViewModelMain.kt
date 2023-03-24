package com.example.lembrardebeberagua.model

import android.app.AlarmManager
import android.view.View
import android.widget.LinearLayout
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
        textView.text = format.format(resultMl / 1000) + "" + " L "
    }

    fun setVisibleMessage(
        textResult: TextView,
        constraintLayout: ConstraintLayout
    ) {
        constraintLayout.visibility = View.VISIBLE
        textResult.text = format.format(resultMl / 1000) + "" + " L de água /dia"
    }

    fun calcBottleAndGlass(bottle: TextView, glass: TextView) {
        bottle.text = "${ceil(resultMl / 500).toInt()} garrafinhas de 500ml"
        glass.text = "${ceil(resultMl / 250).toInt()} copos de 250ml"
    }
}