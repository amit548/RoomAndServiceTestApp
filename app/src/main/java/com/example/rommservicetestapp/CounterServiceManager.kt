package com.example.rommservicetestapp

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CounterServiceManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun startService() {
        val serviceIntent = Intent(context, MainService::class.java)
        ContextCompat.startForegroundService(context, serviceIntent)
    }

    fun stopService() {
        val serviceIntent = Intent(context, MainService::class.java)
        context.stopService(serviceIntent)
    }
}