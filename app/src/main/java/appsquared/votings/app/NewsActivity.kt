package appsquared.votings.app

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.content.res.ResourcesCompat
import app.votings.android.R
import app.votings.android.databinding.ActivityNewsBinding
import com.squareup.picasso.Picasso
import appsquared.votings.app.rest.Model


class NewsActivity : BaseActivity() {

    private lateinit var mNews: Model.News
    private var mImageViewHeaderHeight: Int = 0

    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setScreenTitle(R.string.tile_news)

        intent.extras?.let {

            mNews = it["news_item"] as Model.News

            binding.imageViewHeader.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding.imageViewHeader.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    if (mNews.headerImageUrl.isNotEmpty()) {
                        binding.imageViewHeader.visibility = View.VISIBLE
                        Picasso.get()
                            .load(mNews.headerImageUrl)
                            .into(binding.imageViewHeader)
                    } else binding.imageViewHeader.visibility = View.GONE

                    mImageViewHeaderHeight = binding.imageViewHeader.height //height is ready
                    if (binding.imageViewHeader.visibility == View.GONE) mImageViewHeaderHeight = 0
                }
            })
        }


    }

    fun getColorTemp(color: Int): Int {
        return ResourcesCompat.getColor(resources, color, null)
    }

    override fun childOnlyMethod() {
        val workspace = mWorkspace

        val spacing = dpToPx(16)
        binding.scrollView.setPadding(spacing, spacing + mImageViewHeaderHeight, spacing, spacing)


        //textViewNewsTitle.setTextColor(contentAccentColor)
        binding.textViewNewsTitle.text = mNews.title

        //textViewNewsSubTitle.setTextColor(contentAccentColor)
        binding.textViewNewsSubTitle.text = mNews.subTitle

        //textViewNewsContent.setTextColor(contentTextColor)
        binding.textViewNewsContent.text = mNews.content

    }
}
