package appsquared.votings.app

import android.os.Bundle
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.votings.android.R
import app.votings.android.databinding.ActivityFaqListBinding
import appsquared.votings.app.adapter.FaqListAdapter
import appsquared.votings.app.rest.ApiService
import appsquared.votings.app.rest.Model
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class FaqListActivity : BaseActivity() {

    val list = mutableListOf<Model.Faq>()

    var disposable: Disposable? = null

    val apiService by lazy {
        ApiService.create(Constant.BASE_API)
    }

    private lateinit var binding: ActivityFaqListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun getColorTemp(color: Int) : Int {
        return ResourcesCompat.getColor(resources, color, null)
    }

    override fun childOnlyMethod() {

        setScreenTitle(getString(R.string.tile_faq))

        binding.recyclerViewFaq.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerViewFaq.adapter = FaqListAdapter(list)
        binding.recyclerViewFaq.addItemDecoration(
            HeaderItemDecoration(
                binding.recyclerViewFaq,
                binding.recyclerViewFaq.adapter as HeaderItemDecoration.StickyHeaderInterface
            )
        )
        /*
        recyclerViewFaq.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
         */

        loadFaq()
    }

    private fun loadFaq() {

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val token = pref.getString(PreferenceNames.USER_TOKEN, "")

        disposable = apiService.getFaq("Bearer $token")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    //list.addAll(result)
                    var i = 0
                    for (item in result) {
                        if (item.topicPos > i) {
                            val section = Model.Faq(true, item.topic, item.topicPos)
                            list.add(section)
                            i++
                        }
                        list.add(item)
                    }
                    binding.recyclerViewFaq.adapter!!.notifyDataSetChanged()
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
}
