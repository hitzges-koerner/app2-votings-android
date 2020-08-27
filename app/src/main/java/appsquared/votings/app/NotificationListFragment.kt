package appsquared.votings.app

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import framework.base.constant.Constant
import framework.base.rest.ApiService
import framework.base.rest.Model
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_notificationlist.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NotificationListFragment : Fragment() {

    val list = mutableListOf<Model.Notification>()

    var disposable: Disposable? = null

    val apiService by lazy {
        ApiService.create(Constant.BASE_API)
    }

    private var param1: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
        }

        val workspace = (activity as NewsListActivity).mWorkspace
        val attributes = (activity as NewsListActivity).mAttributes

        var spanCount = 1 // 1 columns for phone
        val tabletSize = resources.getBoolean(R.bool.isTablet)
        if (tabletSize) {
            spanCount = 2 // 2 columns for tablet
        }
        val includeEdge = false

        val spacing = dpToPx(16)

        recyclerViewNotification.setPadding(0, (activity as NewsListActivity).getImageHeaderHeight() + param1, 0, spacing)
        recyclerViewNotification.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                spacing,
                includeEdge
            )
        )
        recyclerViewNotification.layoutManager = GridLayoutManager(context, spanCount)
        recyclerViewNotification.adapter = NotificationListAdapter(list, attributes)

        loadNotificationList()
    }

    private fun loadNotificationList() {

        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        val token = pref.getString(PreferenceNames.USER_TOKEN, "")
        val workspace = pref.getString(PreferenceNames.WORKSPACE_NAME, "")

        disposable = apiService.getNotificationList("Bearer $token", workspace!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    list.addAll(result)
                    recyclerViewNotification.adapter!!.notifyDataSetChanged()
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notificationlist, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(
            workspace: Model.WorkspaceResponse,
            attributes: Attributes,
            toolbarHeight: Int
        ) = NotificationListFragment()

        @JvmStatic
        fun newInstance(param1: Int) = NotificationListFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_PARAM1, param1)
            }
        }
    }
}
