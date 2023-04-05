package com.example.lembrardebeberagua

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.AlarmClock
import android.text.Editable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import com.example.lembrardebeberagua.databinding.ActivityMainBinding
import com.example.lembrardebeberagua.datastore.DataStore
import com.example.lembrardebeberagua.helpers.EmptyFields
import com.example.lembrardebeberagua.helpers.Mask
import com.example.lembrardebeberagua.helpers.AlarmNotification
import com.example.lembrardebeberagua.helpers.AlarmNotification.Companion.NOTIFICATION_ID
import com.example.lembrardebeberagua.model.ViewModelMain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val MY_CHANNEL_ID = "myChannel"
    }
    private lateinit var _binding: ActivityMainBinding
    private lateinit var viewModelMain: ViewModelMain
    private lateinit var dataStore: DataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        viewModelMain = ViewModelProvider(this)[ViewModelMain::class.java]
        dataStore = DataStore(context = this.applicationContext)

        initViews()
        createChannel()

        viewModelMain.alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        setContentView(_binding.root)
    }

    private fun initViews() {
        readData()
        resetData()
        maskEditText()
        defineNotification()
        litersPerDayCalculation()
    }

    private fun defineNotification() {
        _binding.buttonAlarm.setOnClickListener {
            loopMessage()
            testCustomLayout()
        }
    }

    private fun scheduleNotification() {
        val intent = Intent(applicationContext, AlarmNotification::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, Calendar.getInstance().timeInMillis, pendingIntent)
    }

    private fun loopMessage() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                scheduleNotification()
                //Alterar para 3600000
                handler.postDelayed(this, 1 * 60 * 1000)
            }
        }
        handler.postDelayed(runnable, 1 * 60 * 1000)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MY_CHANNEL_ID,
                "MySuperChannel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notification"
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun rememberMe() {
        _binding.checkboxRememberMe.isChecked.apply {
            saveData()
        }
    }

    private fun readData() {
        lifecycle.coroutineScope.launchWhenCreated {
            dataStore.getUserAge().collect {
                _binding.editTextAge.text = it?.toEditable()
            }
            dataStore.getWeight().collect {
                _binding.editTextWeight.text = it?.toEditable()
            }
        }
    }

    private fun saveData() {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.storeAge(
                _binding.editTextAge.text.toString().trim()
            )
            dataStore.storeWeight(
                _binding.editTextWeight.text.toString().trim()
            )
        }
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
                    rememberMe()
                    dialogLoading()
                    val age = editTextAge.text.toString().toInt()
                    val weight = editTextWeight.text.toString().toDouble()
                    val result = textViewResult
                    viewModelMain.calcTotalMl(weight, age, result)
                    viewModelMain.setVisibleMessage(
                        textViewMessaeResult, textViewYouNeedToIngest, constraintLayoutResult
                    )
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
                EmptyFields.fieldAWeight(editTextWeight, applicationContext) &&
                EmptyFields.fieldEmpty(editTextAge, applicationContext)
            )
                return true
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
                textViewMessaeResult.visibility = View.VISIBLE
                constraintLayoutResult.visibility = View.INVISIBLE
                textViewYouNeedToIngest.visibility = View.INVISIBLE
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

    private fun testCustomLayout(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.custom_layout_notification_user)
        dialog.show()
    }

    private fun maskEditText() {
        _binding.apply {
            editTextWeight.addTextChangedListener(Mask.mask(editTextWeight, Mask.FORMAT_WEIGHT))
        }
    }
}