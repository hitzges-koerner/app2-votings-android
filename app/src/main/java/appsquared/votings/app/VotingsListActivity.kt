package appsquared.votings.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import appsquared.votings.app.views.VotingSelectDialog
import framework.base.constant.Constant
import framework.base.rest.ApiService
import framework.base.rest.Model
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_votings_list.recyclerView
import java.util.*
import kotlin.math.roundToInt

class VotingsListActivity : BaseActivity() {

    private var mStatus: Int? = 0
    private val mVotings = mutableListOf<Model.VotingShort>()
    private val mVotingsAll = mutableListOf<Model.VotingShort>()

    var disposable: Disposable? = null

    val apiService by lazy {
        ApiService.create(Constant.BASE_API)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_votings_list)
    }

    override fun onResume() {
        super.onResume()
        if(recyclerView.adapter != null) loadVotingsList()
    }

    override fun childOnlyMethod() {

        mStatus = intent.extras?.getInt(STATUS)
        // exit activity when status is not set
        if(mStatus == 0) finish()

        when(mStatus) {
            FUTURE -> {
                setScreenTitle("Kommende Abstimmungen")
            }
            CURRENT -> {
                setScreenTitle("Aktuelle Abstimmungen")
            }
            PAST -> {
                setScreenTitle("Archiv")
            }
        }

        val workspace: Model.WorkspaceResponse = mWorkspace

        var attributes = Attributes()

        if (workspace.settings.style.isNotEmpty()) {
            when (workspace.settings.style.toLowerCase()) {
                "rich" -> {
                    attributes.contentBackgroundColor = getColorTemp(R.color.white_transparent_fill)
                    attributes.contentBorderColor = getColorTemp(R.color.white_transparent)
                    attributes.contentBorderWidth = 0
                    attributes.contentCornerRadius = 10

                    attributes.contentTextColor = getColorTemp(R.color.black)
                    attributes.contentAccentColor = getColorTemp(R.color.colorAccent)
                    attributes.headlinesBackgroundColor = getColorTemp(R.color.white_transparent)
                }

                "minimal" -> {
                    attributes.contentBackgroundColor = getColorTemp(R.color.transparent)
                    attributes.contentBorderColor = getColorTemp(R.color.transparent)
                    attributes.contentBorderWidth = 0
                    attributes.contentCornerRadius = 10

                    attributes.contentTextColor = getColorTemp(R.color.black)
                    attributes.contentAccentColor = getColorTemp(R.color.colorAccent)
                    attributes.headlinesBackgroundColor = getColorTemp(R.color.transparent)
                }

                "clean" -> {
                    attributes = setAttributesDefault()
                }
                else -> {
                    attributes = setAttributesDefault()
                }
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

        recyclerView.setPadding(0, spacing + getImageHeaderHeight(), 0, spacing)
        recyclerView.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                spacing,
                includeEdge
            )
        )
        recyclerView.layoutManager = GridLayoutManager(this@VotingsListActivity, spanCount)

        recyclerView.adapter = VotingsListAdapter(mVotings, attributes) { position: Int ->

            val votingSelectList = mVotingsAll.filter {
                it.isVoted == mVotings[position].isVoted && it.votingId == mVotings[position].votingId
            }

            if(votingSelectList.size > 1) {
                VotingSelectDialog(this, attributes) {
                    startActivity(
                        Intent(this@VotingsListActivity, VotingsActivity::class.java)
                            .putExtra("voting_id", it.votingId)
                            .putExtra(STATUS, mStatus)
                            .putExtra("voting_representation_id", it.inRepresentationOfId)
                            .putExtra("voting_representation_name", it.inRepresentationOfName))
                }
                    .generate()
                    .setItems(votingSelectList.toMutableList())
                    .show()
            } else {
                startActivity(Intent(this@VotingsListActivity, VotingsActivity::class.java)
                    .putExtra("voting_id", mVotings[position].votingId)
                    .putExtra(STATUS, mStatus)
                    .putExtra("voting_representation_id", mVotings[position].inRepresentationOfId)
                    .putExtra("voting_representation_name", mVotings[position].inRepresentationOfName))
            }
        }

        loadVotingsList()
    }

    private fun loadVotingsList() {

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
                    mVotingsAll.addAll(getVotingsByStatus(result))

                    if(mStatus == CURRENT) {
                        val listVoted = mVotingsAll.filter {
                            it.isVoted == "1"
                        }
                        val listVotedGroupedMap = listVoted.groupBy { it.votingId }
                        val listNotVoted = mVotingsAll.filter {
                            it.isVoted == "0"
                        }
                        val listNotVotedGroupedMap = listNotVoted.groupBy { it.votingId }

                        val listNotVotedGrouped : MutableList<Model.VotingShort> = mutableListOf()
                        for(listNotVotedGroupedMapItem in listNotVotedGroupedMap) {
                            if(listNotVotedGroupedMapItem.value.size > 1) {
                                val item = listNotVotedGroupedMapItem.value[0]
                                item.inRepresentationOfId = ""
                                item.inRepresentationOfName = ""
                                listNotVotedGrouped.add(item)
                            } else listNotVotedGrouped.add(listNotVotedGroupedMapItem.value[0])
                        }

                        val listVotedGrouped : MutableList<Model.VotingShort> = mutableListOf()
                        for(listVotedGroupedMapItem in listVotedGroupedMap) {
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

                        val listGrouped : MutableList<Model.VotingShort> = mutableListOf()
                        for(listGroupedMapItem in listGroupedMap) {
                            listGrouped.add(listGroupedMapItem.value[0])
                        }
                        mVotings.addAll(listGrouped)
                    }
                    recyclerView.adapter?.notifyDataSetChanged()
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

    fun getVotingsByStatus(votings: MutableList<Model.VotingShort>) : MutableList<Model.VotingShort> {

        val votingsTemp : MutableList<Model.VotingShort> = mutableListOf()
        val currentTime: Date = Calendar.getInstance().time

        for(voting in votings) {
            if (parseStringToDate(voting.votingFrom)!! > currentTime) {
                Log.i("app", "votingFrom is after currentTime -> voting not started")
                if(mStatus == FUTURE) {
                    voting.votingStatus = FUTURE
                    votingsTemp.add(voting)
                }
                continue
            }
            if (parseStringToDate(voting.votingTill)!! < currentTime) {
                Log.i("app", "votingTill is before currentTime -> voting ended")
                if(mStatus == PAST) {
                    voting.votingStatus = PAST
                    votingsTemp.add(voting)
                }
                continue
            }
            if(mStatus == CURRENT) {
                voting.votingStatus = CURRENT
                votingsTemp.add(voting)
            }
            continue
        }
        return votingsTemp
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

    companion object {
        val UNDEFINED = 0
        val FUTURE = 1
        val CURRENT = 2
        val PAST = 3

        val STATUS = "status"
    }
}
