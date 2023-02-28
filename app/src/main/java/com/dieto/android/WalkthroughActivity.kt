package com.dieto.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager

class WalkthroughActivity : AppCompatActivity() {

    val TAG = "OnBoardingActivity"
    private var COMPLETED_ONBOARDING_PREF_NAME = "OnBoarding"

    lateinit var mCoordinator: RelativeLayout
    lateinit var indicators: Array<ImageView>
    lateinit var intro_indicator_0: ImageView
    lateinit var intro_indicator_1: ImageView
    lateinit var intro_indicator_2: ImageView
    lateinit var intro_btn_next: Button
    lateinit var intro_btn_finish: Button
    private lateinit var mSectionsPagerAdapter: SectionsPagerAdapter
    private lateinit var mViewPager: ViewPager
    var lastLeftValue = 0
    var page = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walkthrough)

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        mCoordinator = findViewById(R.id.intro_main_layout)
        intro_indicator_0 = findViewById(R.id.intro_indicator_0)
        intro_indicator_1 = findViewById(R.id.intro_indicator_1)
        intro_indicator_2 = findViewById(R.id.intro_indicator_2)
        indicators = arrayOf(intro_indicator_0, intro_indicator_1, intro_indicator_2)
        intro_btn_next = findViewById(R.id.intro_btn_next)
        intro_btn_finish = findViewById(R.id.intro_btn_finish)
        mViewPager = findViewById(R.id.intro_view_pager)
        mViewPager.adapter = mSectionsPagerAdapter
        mViewPager.currentItem = page
        updateIndicators(page)

        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                page = position
                updateIndicators(page)
                intro_btn_next.visibility = if (position == 2) View.GONE else View.VISIBLE
                intro_btn_finish.visibility = if (position == 2) View.VISIBLE else View.GONE
            }
        })

        intro_btn_next.setOnClickListener {
            page += 1
            mViewPager.setCurrentItem(page, true)
        }

        intro_btn_finish.setOnClickListener {
            val intent = Intent(this@WalkthroughActivity, MainActivity::class.java)
            startActivity(intent)
            finish()

            PreferenceManager.getDefaultSharedPreferences(this).edit().apply {
                putBoolean(COMPLETED_ONBOARDING_PREF_NAME, true)
                apply()
            }
        }

    }//onCreate()

    private fun updateIndicators(position:Int) {
        for (i in indicators.indices)
        {
            indicators[i].setBackgroundResource(
                if (i == position) R.drawable.indicator_selected else R.drawable.indicator_unselected)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu):Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_pager, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem):Boolean {
        val id = item.itemId
        if (id == R.id.action_settings)
        {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    class PlaceholderFragment: Fragment() {
        private lateinit var slider_section_image: ImageView
        private lateinit var slider_section_label: TextView
        private lateinit var slider_section_text: TextView
        private var image = intArrayOf(R.drawable.intro_image_one, R.drawable.intro_image_two, R.drawable.intro_image_three)
        private var label = intArrayOf(R.string.intro_label_one, R.string.intro_label_two, R.string.intro_label_three)
        private var text = intArrayOf(R.string.intro_text_one, R.string.intro_text_two, R.string.intro_text_three)

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.single_walkthrough_slider_layout, container, false)

            slider_section_label = view.findViewById(R.id.slider_section_label)
            slider_section_text = view.findViewById(R.id.slider_section_text)
            slider_section_image = view.findViewById(R.id.slider_section_image)

            slider_section_label.text = getString(label[requireArguments().getInt(ARG_SECTION_NUMBER) - 1])
            slider_section_text.text = getString(text[requireArguments().getInt(ARG_SECTION_NUMBER) - 1])
            slider_section_image.setImageResource(image[requireArguments().getInt(ARG_SECTION_NUMBER) - 1])

            return view
        }

        companion object {
            private val ARG_SECTION_NUMBER = "section_number"
            fun newInstance(sectionNumber:Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }

    class SectionsPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return PlaceholderFragment.newInstance(position + 1)
        }

        override fun getCount(): Int = 3

        override fun getPageTitle(position:Int):CharSequence {
            when (position) {
                0 -> return "SECTION 1"
                1 -> return "SECTION 2"
                2 -> return "SECTION 3"
            }
            return position.toString()
        }
    }

    override fun onBackPressed() {
        finish()
    }

}