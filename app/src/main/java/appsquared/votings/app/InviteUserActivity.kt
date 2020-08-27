package appsquared.votings.app

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import appsquared.votings.app.views.DecisionDialog
import framework.base.constant.Constant
import framework.base.rest.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_invite_user.*
import kotlinx.android.synthetic.main.button_card_view.view.*
import org.json.JSONObject


class InviteUserActivity : BaseActivity() {

    var disposable: Disposable? = null

    val apiService by lazy {
        ApiService.create(Constant.BASE_ADM)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_user)
    }

    fun getColorTemp(color: Int) : Int {
        return ResourcesCompat.getColor(resources, color, null)
    }

    override fun clickToolbarCancelButton() {
        super.clickToolbarCancelButton()

        if(editTextInviteUserNameFirst.text.isEmpty() && editTextInviteUserNameLast.text.isEmpty() && editTextInviteUserMail.text.isEmpty()) finish()
        else {
            DecisionDialog(this) {
                if (it == DecisionDialog.LEFT) return@DecisionDialog
                if (it == DecisionDialog.RIGHT) {
                    finish()
                }
            }.generate()
                .setButtonRightName(getString(R.string.yes))
                .setButtonLeftName(getString(R.string.no))
                .setMessage(getString(R.string.cancel_invite_user))
                .show()
        }
    }

    override fun childOnlyMethod() {

        setScreenTitle(getString(R.string.title_invite_user))
        setCancelButtonActive(true)
        removeImageHeader()

        editTextInviteUserNameFirst.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textViewInviteUserNameFirstError.visibility = View.INVISIBLE
            }
        })

        editTextInviteUserNameLast.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textViewInviteUserNameLastError.visibility = View.INVISIBLE
            }
        })

        editTextInviteUserMail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textViewInviteUserMailError.visibility = View.INVISIBLE
            }
        })

        buttonCardViewInviteUserSend.materialCardView.setOnClickListener {
            if(editTextInviteUserNameFirst.text.isEmpty()) {
                textViewInviteUserNameFirstError.visibility = View.VISIBLE
                return@setOnClickListener
            }
            if(editTextInviteUserNameLast.text.isEmpty()) {
                textViewInviteUserNameLastError.visibility = View.VISIBLE
                return@setOnClickListener
            }
            if(editTextInviteUserMail.text.isEmpty() || !isEmailValid(editTextInviteUserMail.text)) {
                textViewInviteUserMailError.visibility = View.VISIBLE
                return@setOnClickListener
            }
            sendInvite()
        }
        
    }
    
    fun sendInvite() {

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val token = pref.getString(PreferenceNames.USER_TOKEN, "")
        val workspace = pref.getString(PreferenceNames.WORKSPACE_NAME, "")

        val jsonData = JSONObject()
        jsonData.put("firstname", editTextInviteUserNameFirst.text.toString())
        jsonData.put("lastname",  editTextInviteUserNameLast.text.toString())
        jsonData.put("email",  editTextInviteUserMail.text.toString())
        jsonData.put("workspace", workspace!!)

        disposable = apiService.inviteUser("Bearer $token", workspace, jsonData.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    finish()
                }, { error ->
                    Log.d("LOGIN", error.message)

                    if(error is retrofit2.HttpException) {
                        if(error.code() == 409 ) {
                            return@subscribe
                        }
                    }
                }
            )
    }
}
