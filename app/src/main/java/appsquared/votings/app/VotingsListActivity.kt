package appsquared.votings.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import app.votings.android.R
import app.votings.android.databinding.ActivityVotingsListBinding
import app.votings.android.databinding.DialogDecisionBinding
import app.votings.android.databinding.DialogListBinding
import app.votings.android.databinding.DialogVotingSelectBinding
import appsquared.votings.app.adapter.VotingsListAdapter
import appsquared.votings.app.views.DecisionDialog
import appsquared.votings.app.views.ListDialog
import appsquared.votings.app.views.VotingSelectDialog
import appsquared.votings.app.rest.ApiService
import appsquared.votings.app.rest.Model
import appsquared.votings.app.tag.enums.Style
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.Calendar
import java.util.Date
import kotlin.math.roundToInt

class VotingsListActivity : BaseActivity() {

    private lateinit var mVotingsListAdapter: VotingsListAdapter
    private var mStatus: Int? = 0
    private var mEditMode: Boolean = false
    private val mVotings = mutableListOf<Model.VotingShort>()
    private val mVotingsAll = mutableListOf<Model.VotingShort>()

    var disposable: Disposable? = null

    val apiService by lazy {
        ApiService.create(Constant.BASE_API)
    }

    private lateinit var binding: ActivityVotingsListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVotingsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        if (binding.recyclerView.adapter != null) loadVotingsList()
    }

    override fun clickToolbarMenuButton() {
        super.clickToolbarMenuButton()
        toggleEditMode()
    }

    private fun toggleEditMode() {
        mEditMode = !mEditMode
        refreshEditMode()
    }

    private fun setEditMode(enable: Boolean) {
        mEditMode = enable
        refreshEditMode()
    }

    private fun refreshEditMode() {
        if (mEditMode) {
            setMenuButton(R.string.done, ContextCompat.getColor(this, R.color.colorAccent))
        } else {
            setMenuButton(R.string.edit, ContextCompat.getColor(this, R.color.colorAccent))
        }
        mVotingsListAdapter.setEditMode(mEditMode)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun childOnlyMethod() {
        setLoadingIndicatorVisibility(View.VISIBLE)
        mStatus = intent.extras?.getInt(STATUS)
        // exit activity when status is not set
        if (mStatus == 0) finish()

        when (mStatus) {
            FUTURE -> {
                setScreenTitle(getString(R.string.tile_upcoming_votings))
            }

            CURRENT -> {
                setScreenTitle(getString(R.string.tile_current_votings))
            }

            PAST -> {
                setScreenTitle(getString(R.string.tile_archive))
            }
        }

        val workspace: Model.WorkspaceResponse = mWorkspace

        var attributes = Attributes()


        when (workspace.settings.style) {
            Style.RICH -> {
                attributes.contentBackgroundColor = getColorTemp(R.color.white_transparent_fill)
                attributes.contentBorderColor = getColorTemp(R.color.white_transparent)
                attributes.contentBorderWidth = 0
                attributes.contentCornerRadius = 10

                attributes.contentTextColor = getColorTemp(R.color.black)
                attributes.contentAccentColor = getColorTemp(R.color.colorAccent)
                attributes.headlinesBackgroundColor = getColorTemp(R.color.white_transparent)
            }

            Style.MINIMAL -> {
                attributes.contentBackgroundColor = getColorTemp(R.color.transparent)
                attributes.contentBorderColor = getColorTemp(R.color.transparent)
                attributes.contentBorderWidth = 0
                attributes.contentCornerRadius = 10

                attributes.contentTextColor = getColorTemp(R.color.black)
                attributes.contentAccentColor = getColorTemp(R.color.colorAccent)
                attributes.headlinesBackgroundColor = getColorTemp(R.color.transparent)
            }

            Style.CLEAN -> {
                attributes = setAttributesDefault()
            }

            else -> {
                attributes = setAttributesDefault()
            }
        }

        val workspaceSettings = workspace.settings
        if (workspaceSettings.contentBackgroundColor.isNotEmpty()) attributes.contentBackgroundColor =
            convertStringToColor(workspaceSettings.contentBackgroundColor)
        if (workspaceSettings.contentBorderColor.isNotEmpty()) attributes.contentBorderColor =
            convertStringToColor(workspaceSettings.contentBorderColor)
        if (workspaceSettings.contentBorderWidth.isNotEmpty()) attributes.contentBorderWidth =
            workspaceSettings.contentBorderWidth.toDouble().roundToInt()
        if (workspaceSettings.contentCornerRadius.isNotEmpty()) attributes.contentCornerRadius =
            workspaceSettings.contentCornerRadius.toDouble().roundToInt()

        if (workspaceSettings.contentTextColor.isNotEmpty()) attributes.contentTextColor =
            convertStringToColor(workspaceSettings.contentTextColor)
        if (workspaceSettings.contentAccentColor.isNotEmpty()) attributes.contentAccentColor =
            convertStringToColor(workspaceSettings.contentAccentColor)

        var spanCount = 1 // 1 columns for phone
        val tabletSize = resources.getBoolean(R.bool.isTablet)
        if (tabletSize) {
            spanCount = 2 // 2 columns for tablet
        }
        val includeEdge = false

        val spacing = dpToPx(16)

        binding.recyclerView.setPadding(0, spacing + getImageHeaderHeight(), 0, spacing)
        binding.recyclerView.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                spacing,
                includeEdge
            )
        )
        binding.recyclerView.layoutManager = GridLayoutManager(this@VotingsListActivity, spanCount)

        mVotingsListAdapter = VotingsListAdapter(mVotings, attributes) { type: Int, position: Int ->

            when (type) {
                VotingsListAdapter.EDIT_BUTTON -> {
                    var counter = 0
                    val pref = PreferenceManager.getDefaultSharedPreferences(this)
                    val listDialog = ListDialog(this)
                    listDialog.generate(DialogListBinding.inflate(layoutInflater))
                    if (mVotings[position].isQuickVoting == "1" && mVotings[position].ownerId == pref.getString(
                            PreferenceNames.USERID,
                            ""
                        )
                    ) listDialog.addButton("delete", R.string.voting_delete)
                    else counter++
                    if (mVotings[position].isQuickVoting == "1" && mVotings[position].ownerId == pref.getString(
                            PreferenceNames.USERID,
                            ""
                        ) && mVotings[position].votingTill.isEmpty()
                    ) listDialog.addButton("close", R.string.voting_close)
                    else counter++
                    if (mStatus == PAST) listDialog.addButton("hide", R.string.voting_hide)
                    else counter++
                    listDialog.callBack { tag: String ->
                        when (tag) {
                            "delete" -> {
                                DecisionDialog(this) {
                                    if (it == DecisionDialog.LEFT) return@DecisionDialog
                                    if (it == DecisionDialog.RIGHT) {
                                        deleteVoting(mVotings[position].votingId, position)
                                    }
                                }.generate(DialogDecisionBinding.inflate(layoutInflater)).setMessage(R.string.dialog_voting_delete_title)
                                    .setButtonRightName(R.string.yes).setButtonLeftName(R.string.no)
                                    .show()
                            }

                            "close" -> {
                                DecisionDialog(this) {
                                    if (it == DecisionDialog.LEFT) return@DecisionDialog
                                    if (it == DecisionDialog.RIGHT) {
                                        closeVoting(mVotings[position].votingId, position)
                                    }
                                }.generate(DialogDecisionBinding.inflate(layoutInflater)).setMessage(R.string.dialog_voting_close_title)
                                    .setButtonRightName(R.string.yes).setButtonLeftName(R.string.no)
                                    .show()

                            }

                            "hide" -> {
                                val votingsHidden = pref.getStringSet(
                                    PreferenceNames.VOTINGS_HIDDEN_BY_USER,
                                    mutableSetOf()
                                ) ?: mutableSetOf()
                                votingsHidden.add(mVotings[position].votingId)
                                pref.edit().putStringSet(
                                    PreferenceNames.VOTINGS_HIDDEN_BY_USER,
                                    votingsHidden
                                ).apply()
                                toggleEditMode()
                            }
                        }
                    }
                    listDialog.addCancelButton() {
                        toggleEditMode()
                    }
                    listDialog.show()

                    /*
                    ListDialog(this)
                        .generate()
                        .addButton("delete", R.string.voting_delete)
                        .addButton("close", R.string.voting_close)
                        .addButton("hide", R.string.voting_hide)
                        .addCancelButton()
                        .callBack { tag: String ->
                            when (tag) {
                                "delete" -> {
                                    toast("callback: $tag")
                                }
                                "close" -> {
                                    toast("callback: $tag")
                                }
                                "hide" -> {
                                    toast("callback: $tag")
                                }
                            }
                        }
                        .show()
                     */
                }

                VotingsListAdapter.VOTING_BUTTON -> {
                    val votingSelectList = mVotingsAll.filter {
                        it.isVoted == mVotings[position].isVoted && it.votingId == mVotings[position].votingId
                    }

                    if (votingSelectList.size > 1) {
                        VotingSelectDialog(this, attributes) {
                            startActivity(
                                Intent(this@VotingsListActivity, VotingsActivity::class.java)
                                    .putExtra("voting_id", it.votingId)
                                    .putExtra(STATUS, mStatus)
                                    .putExtra("voting_representation_id", it.inRepresentationOfId)
                                    .putExtra(
                                        "voting_representation_name",
                                        it.inRepresentationOfName
                                    )
                            )
                        }
                            .generate(DialogVotingSelectBinding.inflate(layoutInflater))
                            .setItems(votingSelectList.toMutableList())
                            .show()
                    } else {
                        startActivity(
                            Intent(this@VotingsListActivity, VotingsActivity::class.java)
                                .putExtra("voting_id", mVotings[position].votingId)
                                .putExtra(STATUS, mStatus)
                                .putExtra(
                                    "voting_representation_id",
                                    mVotings[position].inRepresentationOfId
                                )
                                .putExtra(
                                    "voting_representation_name",
                                    mVotings[position].inRepresentationOfName
                                )
                        )
                    }
                }
            }
        }
        binding.recyclerView.adapter = mVotingsListAdapter
        loadVotingsList()
    }

    private fun closeVoting(votingId: String, position: Int) {

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val token = pref.getString(PreferenceNames.USER_TOKEN, "")
        val workspace = pref.getString(PreferenceNames.WORKSPACE_NAME, "")

        disposable = apiService.closeQuickVoting("Bearer $token", workspace!!, votingId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    mVotings.removeAt(position)
                    binding.recyclerView.adapter?.notifyItemRemoved(position)
                }, { error ->
                    Log.d("LOGIN", error.message ?: "")

                    if (error is retrofit2.HttpException) {
                        if (error.code() == 401 || error.code() == 403) {
                            pref.edit().putString(PreferenceNames.USER_TOKEN, "").apply()
                            return@subscribe
                        }
                    }
                }
            )
    }

    private fun deleteVoting(votingId: String, position: Int) {

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val token = pref.getString(PreferenceNames.USER_TOKEN, "")
        val workspace = pref.getString(PreferenceNames.WORKSPACE_NAME, "")

        disposable = apiService.deleteQuickVoting("Bearer $token", workspace!!, votingId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    mVotings.removeAt(position)
                    binding.recyclerView.adapter?.notifyItemRemoved(position)
                }, { error ->
                    Log.d("LOGIN", error.message ?: "")

                    if (error is retrofit2.HttpException) {
                        if (error.code() == 401 || error.code() == 403) {
                            pref.edit().putString(PreferenceNames.USER_TOKEN, "").apply()
                            return@subscribe
                        }
                    }
                }
            )
    }

    private fun loadVotingsList() {

        setLoadingIndicatorVisibility(View.VISIBLE)
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val token = pref.getString(PreferenceNames.USER_TOKEN, "")
        val workspace = pref.getString(PreferenceNames.WORKSPACE_NAME, "")

        disposable = apiService.getVotingList("Bearer $token", workspace!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    mVotings.clear()
                    mVotingsAll.clear()
                    when (mStatus) {
                        FUTURE -> {
                            mVotingsAll.addAll(getVotingsByStatus(result).sortedBy { it.votingTill })
                        }

                        CURRENT -> {
                            mVotingsAll.addAll(getVotingsByStatus(result).sortedBy { it.votingTill })
                        }

                        PAST -> {
                            mVotingsAll.addAll(getVotingsByStatus(result).sortedByDescending { it.votingTill })
                        }
                    }

                    if (mStatus == CURRENT) {
                        val listVoted = mVotingsAll.filter {
                            it.isVoted == "1"
                        }
                        val listVotedGroupedMap = listVoted.groupBy { it.votingId }
                        val listNotVoted = mVotingsAll.filter {
                            it.isVoted == "0"
                        }
                        val listNotVotedGroupedMap = listNotVoted.groupBy { it.votingId }

                        val listNotVotedGrouped: MutableList<Model.VotingShort> = mutableListOf()
                        for (listNotVotedGroupedMapItem in listNotVotedGroupedMap) {
                            if (listNotVotedGroupedMapItem.value.size > 1) {
                                val item = listNotVotedGroupedMapItem.value[0]
                                item.inRepresentationOfId = ""
                                item.inRepresentationOfName = ""
                                listNotVotedGrouped.add(item)
                            } else listNotVotedGrouped.add(listNotVotedGroupedMapItem.value[0])
                        }

                        val listVotedGrouped: MutableList<Model.VotingShort> = mutableListOf()
                        for (listVotedGroupedMapItem in listVotedGroupedMap) {
                            listVotedGrouped.add(listVotedGroupedMapItem.value[0])
                        }

                        if (listNotVoted.isNotEmpty()) {
                            mVotings.add(Model.VotingShort(getString(R.string.not_voted)))
                            mVotings.addAll(listNotVotedGrouped)
                        }
                        if (listVoted.isNotEmpty()) {
                            mVotings.add(Model.VotingShort(getString(R.string.already_voted)))
                            mVotings.addAll(listVotedGrouped)
                        }
                    } else {

                        val listGroupedMap = mVotingsAll.groupBy {
                            it.votingId
                        }

                        val listGrouped: MutableList<Model.VotingShort> = mutableListOf()
                        for (listGroupedMapItem in listGroupedMap) {
                            listGrouped.add(listGroupedMapItem.value[0])
                        }
                        mVotings.addAll(listGrouped)
                    }
                    binding.recyclerView.adapter?.notifyDataSetChanged()
                    isEditModeButtonVisible()

                    if (mVotings.isEmpty()) setErrorView(getString(R.string.error_no_voting_available))
                    setLoadingIndicatorVisibility(View.GONE)
                }, { error ->
                    Log.d("LOGIN", error.message ?: "")
                    setErrorView(error.message ?: "") {
                        loadVotingsList()
                    }
                    if (error is retrofit2.HttpException) {
                        if (error.code() == 401 || error.code() == 403) {
                            pref.edit().putString(PreferenceNames.USER_TOKEN, "").apply()
                            return@subscribe
                        }
                    }
                }
            )
    }

    private fun isEditModeButtonVisible() {
        if (mVotings.isEmpty()) removeMenuButton()
        else if (mStatus == PAST || isQuickVotingFromOwserInList()) setMenuButton(
            R.string.edit,
            ContextCompat.getColor(this, R.color.colorAccent)
        )
    }

    fun isQuickVotingFromOwserInList(): Boolean {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val userId = pref.getString(PreferenceNames.USERID, "") ?: ""
        //return mVotings.find { it.isQuickVoting == "1" && it.ownerId == userId}
        return mVotings.any { it.isQuickVoting == "1" && it.ownerId == userId }
    }

    fun getVotingsByStatus(votings: MutableList<Model.VotingShort>): MutableList<Model.VotingShort> {

        val votingsTemp: MutableList<Model.VotingShort> = mutableListOf()
        val currentTime: Date = Calendar.getInstance().time

        for (voting in votings) {
            if (parseStringToDate(voting.votingFrom)!! > currentTime) {
                Log.i("app", "votingFrom is after currentTime -> voting not started")
                if (mStatus == FUTURE) {
                    voting.votingStatus = FUTURE
                    votingsTemp.add(voting)
                }
                continue
            }
            if (voting.votingTill.isEmpty()) {
                Log.i("app", "votingTill is empty -> free voting -> ends when all user voted")
                if (mStatus == CURRENT) {
                    voting.votingStatus = CURRENT
                    votingsTemp.add(voting)
                }
                continue
            }
            if (parseStringToDate(voting.votingTill)!! < currentTime) {
                Log.i("app", "votingTill is before currentTime -> voting ended")
                if (mStatus == PAST) {
                    voting.votingStatus = PAST
                    votingsTemp.add(voting)
                }
                continue
            }
            if (mStatus == CURRENT) {
                voting.votingStatus = CURRENT
                votingsTemp.add(voting)
            }
            continue
        }
        return votingsTemp
    }

    fun getColorTemp(color: Int): Int {
        return ResourcesCompat.getColor(resources, color, null)
    }

    fun setAttributesDefault(): Attributes {
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

    companion object {
        val UNDEFINED = 0
        val FUTURE = 1
        val CURRENT = 2
        val PAST = 3

        val STATUS = "status"
    }
}
