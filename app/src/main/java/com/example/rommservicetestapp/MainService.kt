package com.example.rommservicetestapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainService : Service() {

    private lateinit var notificationManager: NotificationManagerCompat

    private val serviceScope = CoroutineScope(SupervisorJob())

    @Inject
    lateinit var counterManager: CounterManager

    @Inject
    lateinit var dao: CounterDao

    private lateinit var counterNotification: NotificationCompat.Builder


    private val countInit = object : CountDownTimer(10000 * 60 * 60, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            serviceScope.launch {
                dao.insertCounter(Counter(10000, millisUntilFinished))
            }
        }

        override fun onFinish() {
            serviceScope.launch {
                dao.insertCounter(Counter(10000, 0))
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = NotificationManagerCompat.from(this)
        createNotificationChannel()
        counterNotification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setSilent(true)
            .setOnlyAlertOnce(true)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = counterNotification
            .setContentTitle("Foreground Service")
            .setContentText("Foreground Service")
            .build()
        startForeground(10458, notification)

        countInit.start()

        serviceScope.launch {
            counterManager.counter.collect {
                updateNotification(it)
            }
        }

        return START_STICKY
    }

    private fun updateNotification(counter: Counter?) {
        val notification = counterNotification.setContentText("Count timer ${counter?.count ?: 0}")
            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(10458, notification)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        countInit.cancel()
        serviceScope.cancel()
    }
}

private const val CHANNEL_ID = "ForegroundServiceChannel"