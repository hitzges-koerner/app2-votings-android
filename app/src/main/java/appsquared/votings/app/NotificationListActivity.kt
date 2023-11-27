package appsquared.votings.app

import android.os.Bundle
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import app.votings.android.R
import app.votings.android.databinding.ActivityNotificationListBinding
import appsquared.votings.app.adapter.NotificationListAdapterFree
import appsquared.votings.app.rest.ApiService
import appsquared.votings.app.rest.Model
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class NotificationListActivity : BaseActivity() {

    val list = mutableListOf<Model.Notification>()

    var disposable: Disposable? = null

    val apiService by lazy {
        ApiService.create(Constant.BASE_API)
    }

    private lateinit var binding: ActivityNotificationListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun getColorTemp(color: Int) : Int {
        return ResourcesCompat.getColor(resources, color, null)
    }

    override fun childOnlyMethod() {

        setScreenTitle(getString(R.string.tile_notifications))

        val workspace = mWorkspace

        var spanCount = 1 // 1 columns for phone
        val tabletSize = resources.getBoolean(R.bool.isTablet)
        if (tabletSize) {
            spanCount = 2 // 2 columns for tablet
        }
        val includeEdge = false

        val spacing = dpToPx(16)

        binding.recyclerViewNotification.setPadding(0, getImageHeaderHeight(), 0, spacing)
        binding.recyclerViewNotification.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                spacing,
                includeEdge
            )
        )
        binding.recyclerViewNotification.layoutManager = GridLayoutManager(this, spanCount)
        binding.recyclerViewNotification.adapter = NotificationListAdapterFree(list)

        loadNotificationList()
    }

    private fun loadNotificationList() {

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val token = pref.getString(PreferenceNames.USER_TOKEN, "")
        val workspace = pref.getString(PreferenceNames.WORKSPACE_NAME, "")

        disposable = apiService.getNotificationList("Bearer $token", workspace!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    list.addAll(result)
                    binding.recyclerViewNotification.adapter!!.notifyDataSetChanged()
                }, { error ->
                    Log.d("LOGIN", error.message ?: "")

                    if(error is retrofit2.HttpException) {
                        if(error.code() == 401 || error.code() == 403) {
                            pref.edit().putString(PreferenceNames.USER_TOKEN, "").apply()
                            return@subscribe
                        }
                    }
                }
            )
    }
}
