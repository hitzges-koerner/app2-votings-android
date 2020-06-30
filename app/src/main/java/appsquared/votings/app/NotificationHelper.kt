package appsquared.votings.app

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.provider.Settings.Global.getString
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import java.util.*

class NotificationHelper(val context: Context) {

    private lateinit var mNotification: Notification
    private var mNotificationId: Int = 1000

    fun showStatusNotification(message: String) {
        try {

            //Create Channel
            createChannel()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotification = Notification.Builder(context, CHANNEL_ID)
                    // Set the intent that will fire when the user taps the notification
                    //.setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_notification_voting)
                    .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                    .setAutoCancel(false)
                    .setContentTitle(context.resources.getString(R.string.app_name))
                    .setStyle(
                        Notification.BigTextStyle()
                            .bigText(message))
                    .setContentText(message).build()
            } else {
                mNotification = Notification.Builder(context)
                    // Set the intent that will fire when the user taps the notification
                    //.setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_notification_voting)
                    .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                    .setAutoCancel(false)
                    .setContentTitle(context.resources.getString(R.string.app_name))
                    .setStyle(
                        Notification.BigTextStyle()
                            .bigText(message))
                    .setContentText(message).build()
            }

            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            // mNotificationId is a unique int for each notification that you must define
            notificationManager.notify(mNotificationId, mNotification)

        } catch(e:Exception) {}
    }



    @SuppressLint("NewApi")
    private fun createChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = ContextCompat.getColor(context, R.color.colorAccent)
            notificationChannel.description = context.getString(R.string.notification_channel_description)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }

    companion object {
        const val CHANNEL_ID = "app.votings.android.PUSH_NOTIFICATION"
        const val CHANNEL_NAME = "Push Notification"
    }

}
