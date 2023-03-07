package com.example.lembrardebeberagua.model

import android.app.AlarmManager
import android.app.PendingIntent
import android.widget.TextView
import android.widget.TimePicker
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.util.*

class ViewModelMain: ViewModel() {
    private val youngML = 40.0
    private val adultML = 35.0
    private val agedML = 30.0
    private val moreThan60YearsML = 25.0
    private var resultMl = 0.0
    private var totalResultML = 0.0
    private val format = NumberFormat.getNumberInstance(Locale("pt", "BR"))
    var alarmTimePicker: TimePicker? = null
    var pendingIntent: PendingIntent? = null
    var alarmManager: AlarmManager? = null

    fun calcTotalMl(weight: Double, age: Int, textView: TextView) {
        if (age <= 17){
            resultMl = weight * youngML
            totalResultML = resultMl
        }else if(age <= 55){
            resultMl = weight * adultML
            totalResultML = resultMl
        } else if(age <= 65){
            resultMl = weight * agedML
            totalResultML = resultMl
        }else{
            resultMl = weight * moreThan60YearsML
            totalResultML = resultMl
        }
        format.isGroupingUsed = false
        textView.text = format.format(resultMl/1000) + "" + " L "
    }


}