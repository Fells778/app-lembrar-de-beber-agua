package com.example.lembrardebeberagua.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class DataStore(private val context: Context) {

    companion object {
        private val Context.DATA_STORE: DataStore<Preferences> by preferencesDataStore("remember_me")
        val USER_AGE = stringPreferencesKey("AGE")

        val USER_WEIGHT = stringPreferencesKey("WEIGHT")
    }


    suspend fun storeAge(age: String) {
        context.DATA_STORE.edit {
            it[USER_AGE] = age
        }
    }

    suspend fun storeWeight(weight: String) {
        context.DATA_STORE.edit {
            it[USER_WEIGHT] = weight
        }
    }

    fun getWeight() = context.DATA_STORE.data.map {
        it[USER_WEIGHT]
    }

    fun getUserAge() = context.DATA_STORE.data.map {
        it[USER_AGE]
    }
}