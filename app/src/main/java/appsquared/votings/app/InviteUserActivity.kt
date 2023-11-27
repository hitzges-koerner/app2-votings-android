package appsquared.votings.app

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import app.votings.android.R
import app.votings.android.databinding.ActivityInviteUserBinding
import app.votings.android.databinding.DialogDecisionBinding
import appsquared.votings.app.rest.ApiService
import appsquared.votings.app.views.DecisionDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject


class InviteUserActivity : BaseActivity() {

    var disposable: Disposable? = null

    val apiService by lazy {
        ApiService.create(Constant.BASE_ADM)
    }

    private lateinit var binding: ActivityInviteUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInviteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun getColorTemp(color: Int) : Int {
        return ResourcesCompat.getColor(resources, color, null)
    }

    override fun clickToolbarCancelButton() {
        super.clickToolbarCancelButton()

        if(binding.editTextInviteUserNameFirst.text.isEmpty() && binding.editTextInviteUserNameLast.text.isEmpty() && binding.editTextInviteUserMail.text.isEmpty()) finish()
        else {
            DecisionDialog(this) {
                if (it == DecisionDialog.LEFT) return@DecisionDialog
                if (it == DecisionDialog.RIGHT) {
                    finish()
                }
            }.generate(DialogDecisionBinding.inflate(layoutInflater))
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

        binding.editTextInviteUserNameFirst.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.textViewInviteUserNameFirstError.visibility = View.INVISIBLE
            }
        })

        binding.editTextInviteUserNameLast.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.textViewInviteUserNameLastError.visibility = View.INVISIBLE
            }
        })

        binding.editTextInviteUserMail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.textViewInviteUserMailError.visibility = View.INVISIBLE
            }
        })

        binding.buttonCardViewInviteUserSend.setOnClickListener {
            if(binding.editTextInviteUserNameFirst.text.isEmpty()) {
                binding.textViewInviteUserNameFirstError.visibility = View.VISIBLE
                return@setOnClickListener
            }
            if(binding.editTextInviteUserNameLast.text.isEmpty()) {
                binding.textViewInviteUserNameLastError.visibility = View.VISIBLE
                return@setOnClickListener
            }
            if(binding.editTextInviteUserMail.text.isEmpty() || !isEmailValid(binding.editTextInviteUserMail.text)) {
                binding.textViewInviteUserMailError.visibility = View.VISIBLE
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
        jsonData.put("firstname", binding.editTextInviteUserNameFirst.text.toString())
        jsonData.put("lastname",  binding.editTextInviteUserNameLast.text.toString())
        jsonData.put("email",  binding.editTextInviteUserMail.text.toString())
        jsonData.put("workspace", workspace!!)

        disposable = apiService.inviteUser("Bearer $token", workspace, jsonData.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    finish()
                }, { error ->
                    Log.d("LOGIN", error.message ?: "")

                    if(error is retrofit2.HttpException) {
                        if(error.code() == 409 ) {
                            return@subscribe
                        }
                    }
                }
            )
    }
}
