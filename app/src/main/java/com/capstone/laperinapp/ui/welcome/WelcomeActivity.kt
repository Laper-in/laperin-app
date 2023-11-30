package com.capstone.laperinapp.ui.welcome

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.capstone.laperinapp.MainActivity
import com.capstone.laperinapp.R
import com.capstone.laperinapp.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var adapter: WelcomeAdapter
    private lateinit var dots: Array<TextView?>
    private lateinit var layouts: IntArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener { onClickNext() }
        binding.btnSkip.setOnClickListener { onClickSkip() }

        layouts = intArrayOf(
            R.layout.slide_1,
            R.layout.slide_2,
            R.layout.slide_3
        )
        adapter = WelcomeAdapter(layouts, this)
        binding.viewPager.adapter = adapter
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (position == layouts.size - 1){
                    binding.btnNext.text = getString(R.string.start)
                    binding.btnSkip.visibility = View.GONE
                } else {
                    binding.btnNext.text = getString(R.string.selanjutnya)
                    binding.btnSkip.visibility = View.VISIBLE
                }
                setDots(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })
        setDots(0)

    }

    private fun onClickSkip() {
        startActivity(Intent((this@WelcomeActivity), MainActivity::class.java))
        finish()
    }

    private fun onClickNext() {
        val currentPage: Int = binding.viewPager.currentItem + 1

        if (currentPage < layouts.size){
            binding.viewPager.currentItem = currentPage
        } else {
            startActivity(Intent((this@WelcomeActivity), MainActivity::class.java))
            finish()
        }
    }

    @Suppress("DEPRECATION")
    private fun setDots(page: Int) {
        binding.indicator.removeAllViews()
        dots = arrayOfNulls(layouts.size)

        for (i in dots.indices){
            dots[i] = TextView(this)
            dots[i]?.text = Html.fromHtml("&#8226")
            dots[i]?.textSize = 35f
            dots[i]?.setTextColor(getColor(R.color.light_gray))
            binding.indicator.addView(dots[i])
        }

        if (dots.isNotEmpty()){
            dots[page]?.setTextColor(getColor(R.color.primary))
        }
    }

}