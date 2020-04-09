package appsquared.votings.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import androidx.preference.PreferenceManager
import framework.base.constant.Constant
import framework.base.rest.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.button_card_view.view.*
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    var disposable: Disposable? = null

    val apiService by lazy {
        ApiService.create(Constant.BASE_API)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setLightStatusBar(window, true)

        constraintLayoutRoot.systemUiVisibility =
            SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        ViewCompat.setOnApplyWindowInsetsListener(buttonCardViewQR) { view, insets ->
            buttonCardViewQR.updatePadding(bottom = insets.systemWindowInsetBottom)
            insets
        }

        editTextCardViewMail.setText("jakob.koerner@app-squared.com")
        editTextCardViewPassword.setText("12345")
        editTextCardViewWorkspace.setText("app-squared")

        buttonCardViewLogin.materialCardView.setOnClickListener {

            linearLayoutStartIndicator.visibility = VISIBLE

            if(editTextCardViewMail.isEmpty() || editTextCardViewPassword.isEmpty() || editTextCardViewWorkspace.isEmpty()) {
                showErrorToast(getString(R.string.error_missing_input))
                return@setOnClickListener
            }

            editTextCardViewWorkspace.isEnabled(false)
            editTextCardViewMail.isEnabled(false)
            editTextCardViewPassword.isEnabled(false)

            apiLogin(editTextCardViewMail.getText(),
                editTextCardViewPassword.getText(),
                editTextCardViewWorkspace.getText())

        }
    }

    fun showErrorToast(error: String) {
        Toast.makeText(this, "Error: $error", Toast.LENGTH_LONG).show()
    }


    private fun apiLogin(email: String, password: String, workspace: String) {

        textViewProgress.text = getString(R.string.loading_login)

        val jsonData = JSONObject()
        jsonData.put("Email", email)
        jsonData.put("Password", password)
        jsonData.put("Workspace", workspace)

        disposable = apiService.login(jsonData.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->

                    val pref = PreferenceManager.getDefaultSharedPreferences(this)
                    pref.edit().putString(PreferenceNames.USER_TOKEN, result.userToken).apply()
                    pref.edit().putString(PreferenceNames.USERID, result.userId).apply()

                    loadWorkspace()

                }, { error ->

                    if(error is retrofit2.HttpException) {
                        if(error.code() == 401 || error.code() == 403) {
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

        disposable = apiService.loadWorkspace("Bearer $token")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->

                    startActivity(Intent(this, MainActivity::class.java))

                }, { error ->

                    if(error is retrofit2.HttpException) {
                        if(error.code() == 401 || error.code() == 403) {
                            showErrorToast(getString(R.string.error_login_again))
                            pref.edit().putString(PreferenceNames.USER_TOKEN, "").apply()
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
}
