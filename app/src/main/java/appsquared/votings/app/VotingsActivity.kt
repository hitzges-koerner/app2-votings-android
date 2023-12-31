package appsquared.votings.app

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import appsquared.votings.app.VotingCustomItem.Companion.BUTTON
import appsquared.votings.app.VotingCustomItem.Companion.CHOICE
import appsquared.votings.app.VotingCustomItem.Companion.DOCUMENT
import appsquared.votings.app.VotingCustomItem.Companion.INFO
import appsquared.votings.app.VotingCustomItem.Companion.RESULT
import appsquared.votings.app.VotingCustomItem.Companion.SECTION
import appsquared.votings.app.VotingCustomItem.Companion.STREAM
import appsquared.votings.app.VotingCustomItem.Companion.USER
import appsquared.votings.app.VotingsListActivity.Companion.CURRENT
import appsquared.votings.app.VotingsListActivity.Companion.FUTURE
import appsquared.votings.app.VotingsListActivity.Companion.PAST
import appsquared.votings.app.views.DecisionDialog
import framework.base.constant.Constant
import framework.base.rest.ApiService
import framework.base.rest.Model
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_votings.*
import org.json.JSONObject
import kotlin.math.roundToInt

class VotingsActivity : BaseActivity() {

    val ERROR_MAX_CHOICES = 0
    val ERROR_MIN_CHOICES = 1
    val ERROR_SELECT = 2

    private var mVotingId: String? = ""
    private var mVotingInRepresentationOfId: String? = ""
    private var mVotingInRepresentationOfName: String? = ""
    private var mStatus: Int? = 0
    private var mVoted: Boolean = false
    private val mVotings = mutableListOf<VotingCustomItem>()
    private val mChoices = mutableListOf<VotingCustomItem>()

    var mAttributes = Attributes()

    var disposable: Disposable? = null

    val apiService by lazy {
        ApiService.create(Constant.BASE_API)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_votings)
    }

    override fun childOnlyMethod() {

        removeToolbarShadow()

        mVotingId = intent.extras?.getString("voting_id", "")
        mVotingInRepresentationOfId = intent.extras?.getString("voting_representation_id", "")
        mVotingInRepresentationOfName = intent.extras?.getString("voting_representation_name", "")
        // exit activity when votingId is not set
        if(mVotingId!!.isEmpty()) finish()

        mStatus = intent.extras?.getInt(VotingsListActivity.STATUS)

        val workspace: Model.WorkspaceResponse = mWorkspace

        if (workspace.settings.style.isNotEmpty()) {
            when (workspace.settings.style.toLowerCase()) {
                "rich" -> {
                    mAttributes.contentBackgroundColor = getColorTemp(R.color.white_transparent_fill)
                    mAttributes.contentBorderColor = getColorTemp(R.color.white_transparent)
                    mAttributes.contentBorderWidth = 0
                    mAttributes.contentCornerRadius = 10

                    mAttributes.contentTextColor = getColorTemp(R.color.black)
                    mAttributes.contentAccentColor = getColorTemp(R.color.colorAccent)
                    mAttributes.headlinesBackgroundColor = getColorTemp(R.color.white_transparent)
                }

                "minimal" -> {
                    mAttributes.contentBackgroundColor = getColorTemp(R.color.transparent)
                    mAttributes.contentBorderColor = getColorTemp(R.color.transparent)
                    mAttributes.contentBorderWidth = 0
                    mAttributes.contentCornerRadius = 10

                    mAttributes.contentTextColor = getColorTemp(R.color.black)
                    mAttributes.contentAccentColor = getColorTemp(R.color.colorAccent)
                    mAttributes.headlinesBackgroundColor = getColorTemp(R.color.transparent)
                }

                "clean" -> {
                    mAttributes = setAttributesDefault()
                }
                else -> {
                    mAttributes = setAttributesDefault()
                }
            }
        }

        val workspaceSettings = workspace.settings
        if (workspaceSettings.contentBackgroundColor.isNotEmpty()) mAttributes.contentBackgroundColor =
            convertStringToColor(workspaceSettings.contentBackgroundColor)
        if (workspaceSettings.contentBorderColor.isNotEmpty()) mAttributes.contentBorderColor =
            convertStringToColor(workspaceSettings.contentBorderColor)
        if (workspaceSettings.contentBorderWidth.isNotEmpty()) mAttributes.contentBorderWidth =
            workspaceSettings.contentBorderWidth.toDouble().roundToInt()
        if (workspaceSettings.contentCornerRadius.isNotEmpty()) mAttributes.contentCornerRadius =
            workspaceSettings.contentCornerRadius.toDouble().roundToInt()

        if (workspaceSettings.contentTextColor.isNotEmpty()) mAttributes.contentTextColor =
            convertStringToColor(workspaceSettings.contentTextColor)
        if (workspaceSettings.contentAccentColor.isNotEmpty()) mAttributes.contentAccentColor =
            convertStringToColor(workspaceSettings.contentAccentColor)

        loadVoting()

        val spacing = dpToPx(16)
        linearLayoutVotingsTitle.setPadding(spacing, spacing + getImageHeaderHeight(), spacing, spacing)
    }

    private fun loadVoting() {
        setLoadingIndicatorVisibility(View.VISIBLE)

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val token = pref.getString(PreferenceNames.USER_TOKEN, "")
        val userId = pref.getString(PreferenceNames.USERID, "")
        val workspace = pref.getString(PreferenceNames.WORKSPACE_NAME, "")

        disposable = apiService.getVoting("Bearer $token", workspace!!, mVotingId!!, mVotingInRepresentationOfId!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->

                    textViewVotingsTitle.text = result.votingTitle
                    if(result.inRepresentationOfId.isNotEmpty()) {
                        textViewVotingsInRepresentationOf.visibility = VISIBLE
                        textViewVotingsInRepresentationOf.text = "In Vertretung für: ${mVotingInRepresentationOfName}"
                        textViewVotingsInRepresentationOf.setTextColor(mAttributes.contentBackgroundColor)
                    }

                    val ownVotes = mutableListOf<String>()
                    result.users.forEach {
                        if(result.inRepresentationOfId.isNotEmpty()) {
                            if(it.userId == result.inRepresentationOfId) ownVotes.addAll(it.votedChoiceIds.replace(" ", "").split(","))
                        } else if(it.userId == userId) ownVotes.addAll(it.votedChoiceIds.replace(" ", "").split(","))
                    }
                    if(result.votingDescription.isNotEmpty()) {
                        mVotings.add(VotingCustomItem(INFO, INFO, result.votingDescription))
                    }

                    if(result.liveStreamUrl.isNotEmpty()) {
                        mVotings.add(VotingCustomItem(SECTION, STREAM, "Livestream"))
                        mVotings.add(VotingCustomItem(STREAM, STREAM, result.liveStreamUrl))
                    }
                    if(result.documents.isNotEmpty()) {
                        mVotings.add(VotingCustomItem(SECTION, DOCUMENT, "Zugehörige Unterlagen"))
                        for(document in result.documents) {
                            mVotings.add(VotingCustomItem(DOCUMENT, DOCUMENT, document.title, document.fileExtension, document.url))
                        }
                    }
                    if(result.choices.isNotEmpty()) {
                        for(choice in result.choices) {
                            val selected = ownVotes.contains(choice.choiceId)
                            if(selected) mVoted = true
                        }
                        if(!mVoted) {
                            when(mStatus) {
                                FUTURE -> {
                                    mVotings.add(VotingCustomItem(SECTION, CHOICE, "Optionen"))
                                }
                                CURRENT -> {
                                    mVotings.add(VotingCustomItem(SECTION, CHOICE, "Ich stimme mit"))
                                }
                            }
                            if(mStatus != PAST) {
                                var total = 0
                                result.choices.forEach {
                                    total += it.votesCnt.toInt()
                                }
                                for(choice in result.choices) {
                                    val selected = ownVotes.contains(choice.choiceId)
                                    if(selected) mVoted = true
                                    mVotings.add(VotingCustomItem(CHOICE, CHOICE, choice.choiceId, choice.choiceTitle, choice.votesCnt.toInt(), total, selected))
                                }
                            }
                            if(mStatus == CURRENT && !mVoted) {
                                mVotings.add(VotingCustomItem(BUTTON, BUTTON, ""))
                            }
                        }
                    }
                    when(result.votingResultsAvailableFrom.toLowerCase()) {
                        // before voting
                        "BEFORE".toLowerCase() -> {
                            if(result.votingTill.isEmpty()) {
                                mVotings.add(VotingCustomItem(SECTION, RESULT, "Vorläufiges Ergebnis"))
                            } else if(getTimeDifference(result.votingTill) < 0) mVotings.add(VotingCustomItem(SECTION, RESULT, "Ergebnis"))
                            else mVotings.add(VotingCustomItem(SECTION, RESULT, "Vorläufiges Ergebnis"))
                            mVotings.addAll(buildResultList(result.choices.sortedByDescending { it.votesCnt } as MutableList<Model.Choice>))
                        }
                        // after voting
                        "AFTER".toLowerCase() -> {
                            if(mVoted) {
                                if(result.votingTill.isEmpty()) {
                                    mVotings.add(VotingCustomItem(SECTION, RESULT, "Vorläufiges Ergebnis"))
                                } else if(getTimeDifference(result.votingTill) < 0) mVotings.add(VotingCustomItem(SECTION, RESULT, "Ergebnis"))
                                else mVotings.add(VotingCustomItem(SECTION, RESULT, "Vorläufiges Ergebnis"))
                                mVotings.addAll(buildResultList(result.choices.sortedByDescending { it.votesCnt } as MutableList<Model.Choice>))
                            }
                        }
                        // after voting ended
                        "AT-END" -> {
                            if(result.votingTill.isNotEmpty()) {
                                if(getTimeDifference(result.votingTill) < 0) {
                                    mVotings.add(VotingCustomItem(SECTION, RESULT, "Ergebnis"))
                                    mVotings.addAll(buildResultList(result.choices.sortedByDescending { it.votesCnt } as MutableList<Model.Choice>))
                                }
                            }
                        }
                    }
                    result.votingType
                    if(result.users.isNotEmpty() && mStatus != FUTURE && result.votingType.equals("OPEN", true)) {
                        mVotings.add(VotingCustomItem(SECTION, USER, "Teilnehmer"))
                        if(result.users.size == 1) {
                            mVotings.add(VotingCustomItem(USER, USER, 2, result.users[0].userId, result.users[0].firstName, result.users[0].lastName, result.users[0].votedChoiceIds, getChoiceNames(result.users[0].votedChoiceIds, result.choices)))
                        } else {
                            for((index, user) in result.users.withIndex()) {
                                if(index == 0) mVotings.add(VotingCustomItem(USER, USER, 0, user.userId, user.firstName, user.lastName, user.votedChoiceIds, getChoiceNames(user.votedChoiceIds, result.choices)))
                                else if(index == result.users.size-1) mVotings.add(VotingCustomItem(USER, USER, 1, user.userId, user.firstName, user.lastName, user.votedChoiceIds, getChoiceNames(user.votedChoiceIds, result.choices)))
                                else mVotings.add(VotingCustomItem(USER, USER, -1, user.userId, user.firstName, user.lastName, user.votedChoiceIds, getChoiceNames(user.votedChoiceIds, result.choices)))
                            }
                        }
                    }

                    var spanCount = 1 // 1 columns for phone
                    val tabletSize = resources.getBoolean(R.bool.isTablet)
                    if (tabletSize) {
                        spanCount = 1 // 1 columns for tablet
                    }
                    val includeEdge = false

                    val spacing = dpToPx(16)

                    //recyclerView.setPadding(0, getImageHeaderHeight(), 0, spacing)
                    recyclerView.addItemDecoration(
                        GridSpacingItemDecoration(
                            spanCount,
                            0,
                            includeEdge
                        )
                    )
                    recyclerView.layoutManager = GridLayoutManager(this@VotingsActivity, spanCount)

                    recyclerView.adapter = VotingsAdapter(mVotings, mAttributes, mStatus!!, mVoted) { position: Int ->
                        val votingCustomItem = mVotings[position]
                        when(votingCustomItem.type) {
                            CHOICE -> {
                                if(result.choicesMax == "1") {
                                    if(mVotings[position].selected) {
                                        mVotings[position].selected = false
                                        recyclerView.adapter!!.notifyItemChanged(position, "lol")
                                    } else {
                                        val itemPosition = mVotings.indexOfFirst { it.selected }
                                        if(itemPosition != -1) {
                                            mVotings[itemPosition].selected = false
                                            recyclerView.adapter!!.notifyItemChanged(itemPosition, "lol")
                                        }
                                        mVotings[position].selected = true
                                        recyclerView.adapter!!.notifyItemChanged(position, "lol")
                                    }
                                } else {
                                    if(mVotings[position].selected) {
                                        mVotings[position].selected = false
                                        recyclerView.adapter!!.notifyItemChanged(position, "lol")
                                    } else {
                                        val votes = mVotings.filter { it.selected }
                                        if(votes.size >= result.choicesMax.toInt() && result.choicesMax != "0") {
                                            //show error message when votes count is bigger than MAX choices
                                            showErrorDialog(ERROR_MAX_CHOICES, result.choicesMax.toInt())
                                        } else {
                                            mVotings[position].selected = true
                                            recyclerView.adapter!!.notifyItemChanged(position, "lol")
                                        }
                                    }
                                }
                            }

                            BUTTON -> {
                                if(result.choicesMax == "1") {
                                    val votingItem = mVotings.find { it.selected }
                                    if(votingItem != null) {
                                        showDialogConfirmationSendVoting(votingItem.id)
                                    } else {
                                        // show error message to select
                                        showErrorDialog(
                                            ERROR_MIN_CHOICES, 0
                                        )
                                    }
                                } else {
                                    val items = mVotings.filter { it.selected }
                                    val count = items.size
                                    if(count >= result.choicesMin.toInt()) {
                                        if(count <= result.choicesMax.toInt() || result.choicesMax == "0") {
                                            var choices = HashSet<String>()
                                            for (item in items) {
                                                choices.add(item.id)
                                            }
                                            showDialogConfirmationSendVoting(choices.joinToString())
                                        } else {
                                            // show error when count votes > MAX choices
                                            showErrorDialog(
                                                ERROR_MAX_CHOICES,
                                                result.choicesMax.toInt()
                                            )
                                        }
                                    } else {
                                        // show error when count votes < MIN choices
                                        showErrorDialog(
                                            ERROR_MIN_CHOICES,
                                            result.choicesMin.toInt()
                                        )
                                    }
                                }
                            }

                            SECTION -> {
                                val visibility = mVotings[position].visible
                                Log.d("visibility: ", visibility.toString())
                                val type = mVotings[position].type
                                Log.d("type: ", type.toString())
                                val subType = mVotings[position].subType
                                Log.d("subType: ", subType.toString())

                                mVotings[position].visible = !visibility
                                mVotings.forEachIndexed { index, it ->
                                    if(it.type == subType && it.subType == subType) {
                                        mVotings[index].visible = !visibility
                                    }
                                }
                                recyclerView.adapter?.notifyDataSetChanged()
                            }

                            DOCUMENT -> {
                                // static let GetDocuments: ApiEndpoint = ApiEndpoint(Method: .Get, Url: "https://api.votings.app/v1.0/{WORKSPACE}/documents/{url}")

                                /*
                                private func openWebView(urlString: String, authKey: String, authHeader: String) {
                                    let viewController = WebViewController(nibName: "WebViewController", bundle: nil)
                                    viewController.setUrl(urlString: urlString, authKey: authKey, authHeader: authHeader)
                                    self.navigationController?.pushViewController(viewController, animated: true)
                                }
                                 */
                            }

                            STREAM -> {
                                val url = mVotings[position].title
                                val builder = CustomTabsIntent.Builder();
                                val customTabsIntent = builder.build();
                                customTabsIntent.launchUrl(this, Uri.parse(url));
                            }
                        }
                    }
                    setLoadingIndicatorVisibility(View.GONE)
                }, { error ->
                    Log.d("LOGIN", error.message)
                    setErrorView(error.message) {
                        loadVoting()
                    }

                    if(error is retrofit2.HttpException) {
                        if(error.code() == 401 || error.code() == 403) {
                            pref.edit().putString(PreferenceNames.USER_TOKEN, "").apply()
                            return@subscribe
                        }
                    }
                }
            )
    }

    private fun buildResultList(choices: MutableList<Model.Choice>): MutableList<VotingCustomItem> {
        var total = 0
        choices.forEach {
            total += it.votesCnt.toInt()
        }
        val resultList = mutableListOf<VotingCustomItem>()
        for(choice in choices) {
            resultList.add(VotingCustomItem(RESULT, RESULT, choice.choiceId, choice.choiceTitle, choice.votesCnt.toInt(), total))
        }
        return resultList
    }

    private fun getChoiceName(
        votedChoiceId: String,
        choices: MutableList<Model.Choice>
    ): String {
        val choice = choices.find { it.choiceId == votedChoiceId }
        if(choice != null) return choice.choiceTitle
        return ""
    }

    private fun getChoiceNames(
        votedChoiceIds: String,
        choices: MutableList<Model.Choice>
    ): String {
        if(votedChoiceIds.isEmpty()) return ""
        var choiceNames = ""
        for (votedChoiceId in votedChoiceIds.split(",")) {
            val choice = choices.find { it.choiceId == votedChoiceId.replace(" ", "") }
            choice?.let {
                if(choiceNames == "") choiceNames = it.choiceTitle
                else choiceNames = choiceNames + "\n" + it.choiceTitle
            }
        }
        return choiceNames
    }

    private fun showDialogConfirmationSendVoting(choiceIds: String) {
        DecisionDialog(this) {
            if (it == DecisionDialog.LEFT) return@DecisionDialog
            if (it == DecisionDialog.RIGHT) {
                sendVoting(choiceIds)
            }
        }.generate()
            .setButtonRightName(getString(R.string.yes))
            .setButtonLeftName(getString(R.string.no))
            .setMessage(getString(R.string.check_send_choices))
            .show()
    }

    private fun showErrorDialog(errorType: Int, count: Int) {
        var errorMessage = ""
        when(errorType) {
            ERROR_MAX_CHOICES -> {
                errorMessage = "Sie dürfen nur maximal ${count} Möglichkeit(en) wählen."
            }
            ERROR_MIN_CHOICES -> {
                errorMessage = "Sie müssen mindestens ${count} Möglichkeit(en) wählen."
            }
            ERROR_SELECT -> {
                errorMessage = "Sie müssen eine Auswahl treffen."
            }
        }
        DecisionDialog(this) {
            if (it == DecisionDialog.RIGHT) return@DecisionDialog
        }.generate()
            .setTitle("Achtung")
            .setButtonRightName(getString(R.string.ok))
            .setMessage(errorMessage)
            .show()
    }

    private fun sendVoting(choiceIds: String) {

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val token = pref.getString(PreferenceNames.USER_TOKEN, "")
        val workspace = pref.getString(PreferenceNames.WORKSPACE_NAME, "")

        val jsonData = JSONObject()
        jsonData.put("choiceIds", choiceIds)
        jsonData.put("InRepresentationOfId", mVotingInRepresentationOfId)

        disposable = apiService.sendVoting("Bearer $token", workspace!!, mVotingId!!, jsonData.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    mVotings.clear()
                    recyclerView.adapter!!.notifyDataSetChanged()

                    loadVoting()

                }, { error ->
                    Log.d("LOGIN", error.message)

                    if(error is retrofit2.HttpException) {
                        if(error.code() == 401 || error.code() == 403) {
                            pref.edit().putString(PreferenceNames.USER_TOKEN, "").apply()
                            return@subscribe
                        }
                    }
                }
            )
    }

    fun getColorTemp(color: Int) : Int {
        return ResourcesCompat.getColor(resources, color, null)
    }

    fun setAttributesDefault() : Attributes {
        val attributes = Attributes()
        attributes.contentBackgroundColor = getColorTemp(R.color.colorAccent)
        attributes.contentBorderColor = getColorTemp(R.color.transparent)
        attributes.contentBorderWidth = 0
        attributes.contentCornerRadius = 10

        attributes.contentTextColor = getColorTemp(R.color.white)
        attributes.contentAccentColor = getColorTemp(R.color.white)
        attributes.headlinesBackgroundColor = getColorTemp(R.color.transparent)
        return attributes
    }
}
