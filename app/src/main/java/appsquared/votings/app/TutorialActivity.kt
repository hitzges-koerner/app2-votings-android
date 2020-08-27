package appsquared.votings.app

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment

class TutorialActivity : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make sure you don't call setContentView!

        setColorDoneText(R.color.black)
        setNextArrowColor(R.color.black)
        setColorSkipButton(R.color.black)

        // Call addSlide passing your Fragments.
        // You can use AppIntroFragment to use a pre-built fragment
        addSlide(
            AppIntroFragment.newInstance(
                title = getString(R.string.tutorial_title_1),
                description = getString(R.string.tutorial_description_1),
                backgroundColor = ContextCompat.getColor(this, R.color.white),
                titleColor = ContextCompat.getColor(this, R.color.black),
                descriptionColor = ContextCompat.getColor(this, R.color.black),
                imageDrawable = R.drawable.intro_1
        ))
        addSlide(AppIntroFragment.newInstance(
                title = getString(R.string.tutorial_title_2),
                description = getString(R.string.tutorial_description_2),
                backgroundColor = ContextCompat.getColor(this, R.color.white),
            titleColor = ContextCompat.getColor(this, R.color.black),
            descriptionColor = ContextCompat.getColor(this, R.color.black),
            imageDrawable = R.drawable.intro_2
        ))
        addSlide(AppIntroFragment.newInstance(
                title = getString(R.string.tutorial_title_3),
                description = getString(R.string.tutorial_description_3),
                backgroundColor = ContextCompat.getColor(this, R.color.white),
                titleColor = ContextCompat.getColor(this, R.color.black),
                descriptionColor = ContextCompat.getColor(this, R.color.black),
            imageDrawable = R.drawable.intro_3
        ))

        // Toggle Indicator Visibility
        isIndicatorEnabled = true

        // Change Indicator Color
        setIndicatorColor(
            selectedIndicatorColor = ContextCompat.getColor(this, R.color.colorAccent),
            unselectedIndicatorColor = ContextCompat.getColor(this, R.color.black)
        )
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        // Decide what to do when the user clicks on "Skip"
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        pref.edit().putBoolean(PreferenceNames.FIRST_START, false).apply()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Decide what to do when the user clicks on "Done"
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        pref.edit().putBoolean(PreferenceNames.FIRST_START, false).apply()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}