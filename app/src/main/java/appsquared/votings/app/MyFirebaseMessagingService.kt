package appsquared.votings.app

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.preference.PreferenceManager
import appsquared.votings.app.PreferenceNames.Companion.NOTIFICATION_SHOW
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import framework.base.constant.Constant
import framework.base.rest.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

class MyFirebaseMessagingService : FirebaseMessagingService() {

    var disposable: Disposable? = null

    val apiService by lazy {
        ApiService.create(Constant.BASE_API)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i(TAG, "Refreshed token: $token")
        storeFirebaseToken(token)
    }

    private fun storeFirebaseToken(token: String) {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val appPrefEdit = pref.edit()
        appPrefEdit.putString(PreferenceNames.FIREBASE_TOKEN, token)
        appPrefEdit.apply()
        val showNotification = pref.getBoolean(PreferenceNames.NOTIFICATION_SHOW + "_" + PreferenceNames.WORKSPACE_NAME, true)
        if(showNotification) sendFirebaseTokenToServer()
    }

    private fun sendFirebaseTokenToServer() {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)

        val firebaseToken = pref.getString(PreferenceNames.FIREBASE_TOKEN, "")
        val userToken = pref.getString(PreferenceNames.USER_TOKEN, "")
        val workspace = pref.getString(PreferenceNames.WORKSPACE_NAME, "")

        val jsonObject = JSONObject()
        jsonObject.put(JsonParamNames.TOKEN, firebaseToken)
        jsonObject.put(JsonParamNames.PLATFORM, "fcm")

        if(userToken!!.isNotEmpty())  {
            disposable = apiService.sendFirebaseToken("Bearer $userToken", workspace!!, jsonObject.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        Log.d("FIREBASE", "Firebase token sent")
                    }, { error ->
                        Log.d("FIREBASE", "Firebase token NOT sent")
                    }
                )
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        // If the application is in the foreground handle both data ansd notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val showPushNotification = pref.getBoolean(PreferenceNames.NOTIFICATION_SHOW + "_" + PreferenceNames.WORKSPACE_NAME, true)

        if (showPushNotification) {

            val handler = Handler(Looper.getMainLooper())
            handler.post {
                if (remoteMessage.notification!!.body != null) {
                    val notificationHelper = NotificationHelper(this)
                    notificationHelper.showStatusNotification(remoteMessage.notification?.body!!)
                }
            }
        }
        Log.d(TAG, "From: " + remoteMessage.from!!)
        Log.d(TAG, "Notification Message Body: " + remoteMessage.notification!!.body!!)
    }

    companion object {
        private val TAG = "FCM Service"
    }
}