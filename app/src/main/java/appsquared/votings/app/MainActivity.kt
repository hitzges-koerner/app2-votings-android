package appsquared.votings.app

import android.os.Bundle
import android.view.View.*
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var statusBarSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setLightStatusBar(window, true)

        constraintLayoutRoot.systemUiVisibility =
            SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        ViewCompat.setOnApplyWindowInsetsListener(toolbarCustom) { view, insets ->
            statusBarSize = insets.systemWindowInsetTop

            val params: ViewGroup.LayoutParams = toolbarCustom.layoutParams
            val temp = dpToPx(56)
            params.height = statusBarSize + temp
            toolbarCustom.requestLayout()

            insets
        }

        val list = mutableListOf<Item>()
        list.add(Item("Title 1", R.drawable.tile))
        list.add(Item("Title 2", R.drawable.tile))
        list.add(Item("Title 3", R.drawable.tile))
        list.add(Item("Title 4", R.drawable.tile))
        list.add(Item("Title 5", R.drawable.tile))
        list.add(Item("Title 1", R.drawable.tile))
        list.add(Item("Title 2", R.drawable.tile))
        list.add(Item("Title 3", R.drawable.tile))
        list.add(Item("Title 4", R.drawable.tile))
        list.add(Item("Title 5", R.drawable.tile))
        val attributes = Attributes()

        imageViewHeader.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                imageViewHeader.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                val imageViewHeaderHeight = imageViewHeader.getHeight() //height is ready

                var spanCount = 2 // 3 columns
                val includeEdge = false

                val spacing = dpToPx(16)

                recyclerView.setPadding(spacing, spacing + imageViewHeaderHeight, spacing, spacing)
                recyclerView.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge))

                recyclerView.layoutManager = GridLayoutManager(this@MainActivity, spanCount)

                recyclerView.adapter = TilesAdapter(list, attributes) { position: Int ->


                }




            }
        })


    }
}
