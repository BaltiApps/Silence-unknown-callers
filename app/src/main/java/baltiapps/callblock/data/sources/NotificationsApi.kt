package baltiapps.callblock.data.sources

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import baltiapps.callblock.R
import baltiapps.callblock.domain.models.CallInformation
import baltiapps.callblock.domain.sources.PlatformNotificationsApi
import baltiapps.callblock.utils.convertLongToTimestamp

class NotificationsApi (
    private val context: Context,
): PlatformNotificationsApi {

    companion object {
        private const val NOTIFICATIONS_CHANNEL = "filter_channel"
        private const val NOTIFICATIONS_CHANNEL_DESC = "Call filter notifications"
        private var notificationCount = 0
    }

    private val notificationService by lazy {
        context.getSystemService(NotificationManager::class.java)
    }

    override fun checkNotificationsPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return true
        }
        return context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    }

    override fun initNotifications() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return
        }

        notificationService.createNotificationChannel(
            NotificationChannel(
                NOTIFICATIONS_CHANNEL,
                NOTIFICATIONS_CHANNEL_DESC,
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )
    }

    private fun showNotification(
        callInformation: CallInformation,
        titleStringRes: Int,
    ) {
        val title = context.getString(titleStringRes, callInformation.displayName)
        val timeStamp = convertLongToTimestamp(callInformation.callTimeStamp)

        val notification = Notification.Builder(context, NOTIFICATIONS_CHANNEL)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(timeStamp)
            .build()

        notificationService.notify(
            ++notificationCount,
            notification
        )
    }

    override fun showSilencedNotifications(callInformation: CallInformation) {
        showNotification(callInformation, R.string.call_silenced)
    }

    override fun showBlockedNotifications(callInformation: CallInformation) {
        showNotification(callInformation, R.string.call_blocked)
    }
}