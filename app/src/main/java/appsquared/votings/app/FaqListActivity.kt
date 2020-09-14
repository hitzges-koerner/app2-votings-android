package appsquared.votings.app

import android.os.Bundle
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import framework.base.constant.Constant
import framework.base.rest.ApiService
import framework.base.rest.Model
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_faq_list.*


class FaqListActivity : BaseActivity() {

    val list = mutableListOf<Model.Faq>()

    var disposable: Disposable? = null

    val apiService by lazy {
        ApiService.create(Constant.BASE_API)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq_list)
    }

    fun getColorTemp(color: Int) : Int {
        return ResourcesCompat.getColor(resources, color, null)
    }

    override fun childOnlyMethod() {

        setScreenTitle(getString(R.string.tile_faq))

        recyclerViewFaq.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerViewFaq.adapter = FaqListAdapter(list)
        recyclerViewFaq.addItemDecoration(
            HeaderItemDecoration(
                recyclerViewFaq,
                recyclerViewFaq.adapter as HeaderItemDecoration.StickyHeaderInterface
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
                    recyclerViewFaq.adapter!!.notifyDataSetChanged()
                }, { error ->
                    Log.d("LOGIN", error.message)

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
