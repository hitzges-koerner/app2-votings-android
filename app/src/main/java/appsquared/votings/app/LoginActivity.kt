package appsquared.votings.app

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.View.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.preference.PreferenceManager
import appsquared.votings.app.views.InfoDialog
import framework.base.constant.Constant
import framework.base.rest.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.button_card_view.view.*
import org.json.JSONObject
import java.util.*
import java.util.concurrent.Executor


class LoginActivity : AppCompatActivity() {

    private var mLoginType: Int = 0
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    var disposable: Disposable? = null

    val apiService by lazy {
        ApiService.create(Constant.BASE_API)
    }

    override fun onStart() {
        super.onStart()

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val workspaceName = pref.getString(PreferenceNames.WORKSPACE_NAME, "") ?: ""
        val email = pref.getString(PreferenceNames.EMAIL, "") ?: ""
        val password = pref.getString(PreferenceNames.PASSWORD, "") ?: ""

        if(workspaceName.isEmpty() && email.isEmpty() && password.isEmpty()) {
            InfoDialog(this) {
            }.generate().setButtonName(R.string.how_it_works_button)
                .setTitle(R.string.how_it_works_title)
                .setMessage(R.string.how_it_works_message)
                .show()
        }
        if(workspaceName.isNotEmpty()) editTextCardViewWorkspace.setText(workspaceName)
        if(email.isNotEmpty()) editTextCardViewMail.setText(email)
        if(password.isNotEmpty()) editTextCardViewPassword.setText(password)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if(intent.hasExtra("login_type")) {
            mLoginType = intent.extras?.get("login_type") as Int
        }

        val isDebuggable = 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
        if(!isDebuggable) {
            Log.d(Constant.TAG, "telemetry sent")
            intent.extras?.let {
                if(it.containsKey("app_start")) {
                    if(it.get("app_start") as Boolean) sendTelemetry()
                }
            }
        }

        setLightStatusBar(window, true)
        constraintLayoutRoot.systemUiVisibility =
            SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        ViewCompat.setOnApplyWindowInsetsListener(buttonCardViewQR) { view, insets ->
            buttonCardViewQR.updatePadding(bottom = insets.systemWindowInsetBottom)
            insets
        }

        buttonCardViewQR.materialCardView.setOnClickListener {
            startActivity(Intent(this, AccountRegisterActivity::class.java))
        }

        // TODO DISABLED, TESTING ONLY
        /*
        if(pref.getString(PreferenceNames.WORKSPACE_NAME, "")!!.isNotEmpty() &&
            pref.getString(PreferenceNames.EMAIL, "")!!.isNotEmpty() &&
            pref.getString(PreferenceNames.PASSWORD, "")!!.isNotEmpty() &&
                BiometricManager.from(this).canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            biometric()
        }
         */

        buttonCardViewLogin.materialCardView.setOnClickListener {

            // check if inputs are not empty
            if(editTextCardViewMail.isEmpty() || editTextCardViewPassword.isEmpty() || editTextCardViewWorkspace.isEmpty()) {
                showErrorToast(getString(R.string.error_missing_input))
                return@setOnClickListener
            } else prepLogin()
        }

        textCardViewError.setOnClickListener {
            textCardViewError.visibility = GONE
        }
    }

    fun showErrorToast(error: String) {

        linearLayoutStartIndicator.visibility = GONE

        buttonCardViewLogin.isEnabled = false
        viewFadeIn(editTextCardViewWorkspace)
        viewFadeIn(editTextCardViewMail)
        viewFadeIn(editTextCardViewPassword)
        viewFadeIn(buttonCardViewLogin)
        viewFadeIn(buttonCardViewQR)

        //Toast.makeText(this, "Error: $error", Toast.LENGTH_LONG).show()

        textCardViewError.visibility = View.VISIBLE
        textCardViewError.setText(error)
    }

    private fun prepLogin() {
        // hide error view
        textCardViewError.visibility = GONE
        // show spinning progress indicator
        linearLayoutStartIndicator.visibility = VISIBLE

        // disable login button
        buttonCardViewLogin.isEnabled = false
        // fade out all views
        viewFadeOut(editTextCardViewWorkspace)
        viewFadeOut(editTextCardViewMail)
        viewFadeOut(editTextCardViewPassword)
        viewFadeOut(buttonCardViewLogin)
        viewFadeOut(buttonCardViewQR)

        apiLogin(
            editTextCardViewMail.getText(),
            editTextCardViewPassword.getText(),
            editTextCardViewWorkspace.getText()
        )
    }

    private fun apiLogin(email: String, password: String, workspace: String) {

        textViewProgress.text = getString(R.string.loading_login)

        val jsonData = JSONObject()
        jsonData.put("Email", email)
        jsonData.put("Password", password)
        jsonData.put("Workspace", workspace)

        disposable = apiService.login(workspace, jsonData.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->

                    val pref = PreferenceManager.getDefaultSharedPreferences(this)
                    pref.edit().putString(PreferenceNames.USER_TOKEN, result.userToken).apply()
                    pref.edit().putString(PreferenceNames.USERID, result.userId).apply()
                    pref.edit().putString(PreferenceNames.WORKSPACE_NAME, workspace).apply()
                    pref.edit().putString(PreferenceNames.PASSWORD, password).apply()
                    pref.edit().putString(PreferenceNames.EMAIL, email).apply()

                    AppData().saveObjectToSharedPreference(this, PreferenceNames.LOGIN_DATA, result)

                    loadWorkspace()

                }, { error ->

                    Log.d("LOGIN", error.message)

                    if (error is retrofit2.HttpException) {
                        if (error.code() == 401 || error.code() == 403) {
                            showErrorToast(getString(R.string.error_credentials_false))
                            return@subscribe
                        }
                    }

                    showErrorToast(getString(R.string.error_general))

                    editTextCardViewWorkspace.isEnabled(true)
                    editTextCardViewMail.isEnabled(true)
                    editTextCardViewPassword.isEnabled(true)
                }
            )
    }

    private fun loadWorkspace() {

        textViewProgress.text = getString(R.string.loading_workspace)

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val token = pref.getString(PreferenceNames.USER_TOKEN, "")
        val workspace = pref.getString(PreferenceNames.WORKSPACE_NAME, "")

        disposable = apiService.loadWorkspace(workspace!!, "Bearer $token")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->

                    val showNotification = pref.getBoolean(
                        PreferenceNames.NOTIFICATION_SHOW + "_" + PreferenceNames.WORKSPACE_NAME,
                        true
                    )
                    if (showNotification) sendFirebaseTokenToServer()
                    AppData().saveObjectToSharedPreference(this, PreferenceNames.WORKSPACE, result)

                    if(pref.getBoolean(PreferenceNames.FIRST_START, true) && result.planName.toUpperCase() == "FREE".toUpperCase()) {
                        startActivity(Intent(this, TutorialActivity::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }

                }, { error ->
                    Log.d("LOGIN", error.message)

                    if (error is retrofit2.HttpException) {
                        if (error.code() == 401 || error.code() == 403) {
                            showErrorToast(getString(R.string.error_login_again))
                            pref.edit().putString(PreferenceNames.USER_TOKEN, "").apply()
                            return@subscribe
                        }
                    }
                    showErrorToast(getString(R.string.error_general))
                }
            )
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
            disposable = apiService.sendFirebaseToken(
                "Bearer $userToken",
                workspace!!,
                jsonObject.toString()
            )
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

    fun getDeviceId(context: Context): String {
        return UUID.randomUUID().toString().toUpperCase()
    }

    private fun sendTelemetry() {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)

        var deviceID = pref.getString(PreferenceNames.DEVICE_ID, "") ?: ""
        if(deviceID.isEmpty()) deviceID = getDeviceId(this)

        val deviceName = Build.MODEL
        Log.d("MODEL", deviceName)
        val deviceMan = Build.MANUFACTURER
        Log.d("MANUFACTURER", deviceMan)
        val device = "$deviceMan $deviceName"

        val displayMetrics: DisplayMetrics = getResources().getDisplayMetrics()
        val dpHeight = displayMetrics.heightPixels / displayMetrics.density
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density

        var hardware = "[${dpHeight}x${dpWidth}]"
        if(resources.getBoolean(R.bool.isTablet)) hardware = "$device -TABLET- $hardware"
        else hardware = "$device -PHONE- $hardware"

        val jsonObject = JSONObject()
        jsonObject.put(JsonParamNames.ID, deviceID)
        jsonObject.put(JsonParamNames.OS, "ANDROID ${Build.VERSION.SDK_INT}")
        jsonObject.put(JsonParamNames.HARDWARE, hardware)
        jsonObject.put(JsonParamNames.APP_VERSION, BuildConfig.VERSION_NAME)
        jsonObject.put(JsonParamNames.LANGUAGE, Locale.getDefault().language)

        disposable = apiService.sendTelemetry(jsonObject.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Log.d("TELEMETRY", "telemetry data sent")
                }, { error ->
                    Log.d("TELEMETRY", "telemetry data NOT sent")
                }
            )
    }

    fun biometric() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        applicationContext,
                        "Authentication succeeded!", Toast.LENGTH_SHORT
                    )
                        .show()
                    prepLogin()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    companion object {
        val LOGIN_NEW = 0
        val LOGIN_SWITCH = 1
    }
}
