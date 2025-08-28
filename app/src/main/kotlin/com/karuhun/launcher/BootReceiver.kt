package com.karuhun.launcher

import android.Manifest
import android.app.AlarmManager
import android.app.admin.DevicePolicyManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission

class BootReceiver : BroadcastReceiver() {
    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Log.i("BootReceiver", "Received action: $action")

        // Start our launcher activity on boot or when app is updated
        if (
            Intent.ACTION_BOOT_COMPLETED == action ||
            Intent.ACTION_LOCKED_BOOT_COMPLETED == action ||
            Intent.ACTION_MY_PACKAGE_REPLACED == action ||
            (Build.VERSION.SDK_INT >= 24 && Intent.ACTION_USER_UNLOCKED == action) ||
            action == "android.intent.action.QUICKBOOT_POWERON" ||
            action == "com.htc.intent.action.QUICKBOOT_POWERON"
        ) {
            // Start a tiny foreground service to bring up MainActivity asap
            try {
                context.startForegroundService(Intent(context, BootLaunchService::class.java))
            } catch (_: Throwable) {
                try {
                    context.startService(Intent(context, BootLaunchService::class.java))
                } catch (_: Throwable) {}
            }

            // If we are device owner, set ourselves as the persistent HOME
            try {
                val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
                val admin = ComponentName(context, KioskDeviceAdminReceiver::class.java)
                if (dpm.isDeviceOwnerApp(context.packageName)) {
                    // Clear previous and set persistent preferred
                    dpm.clearPackagePersistentPreferredActivities(admin, context.packageName)
                    val intentFilter = IntentFilter(Intent.ACTION_MAIN).apply {
                        addCategory(Intent.CATEGORY_HOME)
                        addCategory(Intent.CATEGORY_DEFAULT)
                    }
                    val activity = ComponentName(context, MainActivity::class.java)
                    dpm.addPersistentPreferredActivity(admin, intentFilter, activity)
                    Log.i("BootReceiver", "Set persistent preferred HOME activity (device owner)")
                } else {
                    Log.i("BootReceiver", "Not device owner; skipping persistent home config")
                }
            } catch (t: Throwable) {
                Log.w("BootReceiver", "Device owner home config failed: ${t.message}")
            }

            // Schedule a delayed launch to let the system settle (some TVs need a home to initialize)
            try {
                val pending = PendingIntent.getActivity(
                    context,
                    1001,
                    Intent(context, MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    },
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val triggerAt = SystemClock.elapsedRealtime() + 3000 // 3s after boot
                try {
                    am.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAt, pending)
                } catch (_: Throwable) {
                    am.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAt, pending)
                }
                Log.i("BootReceiver", "Scheduled MainActivity start in 3s after boot")
            } catch (e: Exception) {
                Log.w("BootReceiver", "Alarm scheduling failed, trying immediate start: ${e.message}")
                // Immediate fallback
                try {
                    val startIntent = Intent(context, MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    }
                    context.startActivity(startIntent)
                } catch (e2: Exception) {
                    Log.e("BootReceiver", "Failed to start MainActivity immediately: ${e2.message}", e2)
                    // As a last resort, request HOME
                    try {
                        val home = Intent(Intent.ACTION_MAIN).apply {
                            addCategory(Intent.CATEGORY_HOME)
                            addCategory("android.intent.category.COMMON_HOME")
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(home)
                    } catch (_: Throwable) {
                        // Give up silently
                    }
                }
            }
        }
    }
}
