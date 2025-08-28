package com.karuhun.launcher

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import java.util.concurrent.atomic.AtomicBoolean

class BootLaunchService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createChannel()
        val notification = buildNotification()
        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Acquire a short wake lock to ensure CPU is on during early boot
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "isolutions:bootlaunch").apply {
            setReferenceCounted(false)
            try { acquire(10_000) } catch (_: Throwable) {}
        }

        val mainHandler = Handler(Looper.getMainLooper())
        val launched = AtomicBoolean(false)
        fun launchOnce() {
            if (launched.get()) return
            try {
                val start = Intent(applicationContext, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                }
                applicationContext.startActivity(start)
                launched.set(true)
            } catch (_: Throwable) {
            }
        }

        // Try a short backoff series to beat varying OEM init timing
        val delays = longArrayOf(0, 500, 1000, 2000, 3500, 5000, 7000, 9000, 12000)
        for ((i, d) in delays.withIndex()) {
            mainHandler.postDelayed({
                try { launchOnce() } finally {
                    if (i == delays.lastIndex || launched.get()) {
                        try { wl.release() } catch (_: Throwable) {}
                        stopForeground(STOP_FOREGROUND_REMOVE)
                        stopSelf()
                    }
                }
            }, d)
        }

        return START_NOT_STICKY
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Boot Launch",
                NotificationManager.IMPORTANCE_MIN
            ).apply {
                description = "Starts launcher on boot"
                setSound(null, null)
                enableVibration(false)
                setShowBadge(false)
            }
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(): Notification {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Starting launcher")
            .setContentText("Preparing home screen…")
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_MIN)
        return builder.build()
    }

    companion object {
        private const val CHANNEL_ID = "boot_launch_channel"
        private const val NOTIFICATION_ID = 19001
    }
}
