package appsquared.votings.app

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import framework.base.rest.Model
import kotlinx.android.synthetic.main.fragment_newslist.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NewsListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: Int = 0
    private var param2: String? = null

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

        recyclerViewNews.setPadding(0, (activity as NewsListActivity).getImageHeaderHeight() + param1, 0, spacing)
        recyclerViewNews.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                spacing,
                includeEdge
            )
        )
        recyclerViewNews.layoutManager = GridLayoutManager(context, spanCount)

        recyclerViewNews.adapter = NewsListAdapter(workspace.news, attributes) { position: Int ->
            startActivity(Intent(context, NewsActivity::class.java).putExtra("news_item", workspace.news[position]))
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_newslist, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(
            workspace: Model.WorkspaceResponse,
            attributes: Attributes,
            toolbarHeight: Int
        ) = NewsListFragment()

        @JvmStatic
        fun newInstance(param1: Int) = NewsListFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_PARAM1, param1)
            }
        }
    }
}
