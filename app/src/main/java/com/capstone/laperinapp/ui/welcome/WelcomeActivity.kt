package com.capstone.laperinapp.ui.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.viewpager.widget.ViewPager
import com.capstone.laperinapp.ui.MainActivity
import com.capstone.laperinapp.R
import com.capstone.laperinapp.data.pref.UserModel
import com.capstone.laperinapp.databinding.ActivityWelcomeBinding
import com.capstone.laperinapp.helper.Result
import com.capstone.laperinapp.helper.ViewModelFactory

class WelcomeActivity : AppCompatActivity() {

    private val viewModel by viewModels<WelcomeViewModel> {
        ViewModelFactory.getInstance(this)
    }

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

        setupLayout()
        setupViewPager()
        setupLogin()

    }

    private fun setupLogin() {
        val email = intent.getStringExtra(EXTRA_EMAIL)
        val password = intent.getStringExtra(EXTRA_PASSWORD)

        if (email != null && password != null){
            viewModel.login(email, password).observe(this){ result ->
                when(result){
                    is Result.Loading -> {
                        Log.d(TAG, "setupLogin: Loading")
                    }
                    is Result.Success -> {
                        Log.d(TAG, "setupLogin: Success")
                        val token = result.data.token
                        val user = UserModel(email, token, true)
                        viewModel.saveSession(user)
                        ViewModelFactory.clearInstance()
                    }
                    is Result.Error -> {
                        Log.d(TAG, "setupLogin: Error")
                    }
                }
            }
        }
    }

    private fun setupLayout() {
        layouts = intArrayOf(
            R.layout.slide_1,
            R.layout.slide_2,
            R.layout.slide_3
        )
    }

    private fun setupViewPager() {
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

    companion object{
        const val TAG = "WelcomeActivity"
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_PASSWORD = "extra_password"
    }

}