package me.rerere.salary

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SalaryApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: SalaryApp
    }
}

fun sharedPreferenceOf(name: String): SharedPreferences =
    SalaryApp.instance.getSharedPreferences(name, Context.MODE_PRIVATE)