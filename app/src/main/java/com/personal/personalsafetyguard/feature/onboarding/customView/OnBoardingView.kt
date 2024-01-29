package com.personal.personalsafetyguard.feature.onboarding.customView

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.viewpager2.widget.ViewPager2
import com.personal.personalsafetyguard.MainActivity
import com.personal.personalsafetyguard.animation.pageCompositePageTransformer
import com.personal.personalsafetyguard.animation.setParallaxTransformation
import com.personal.personalsafetyguard.databinding.OnboardingViewBinding
import com.personal.personalsafetyguard.domain.OnBoardingPrefManager
import com.personal.personalsafetyguard.feature.onboarding.OnBoardingActivity
import com.personal.personalsafetyguard.feature.onboarding.OnBoardingPagerAdapter
import com.personal.personalsafetyguard.feature.onboarding.entity.OnBoardingPage

class OnBoardingView @JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val numberOfPages by lazy { OnBoardingPage.values().size }
    private val prefManager: OnBoardingPrefManager

    init {
        val binding = OnboardingViewBinding.inflate(LayoutInflater.from(context), this, true)
        with(binding) {
            setUpSlider()
            addingButtonsClickListeners()
            prefManager = OnBoardingPrefManager(root.context)
        }
    }

    private fun OnboardingViewBinding.setUpSlider() {
        with(slider) {
            adapter = OnBoardingPagerAdapter()

            setPageTransformer { page, position ->
                setParallaxTransformation(page, position)
            }

            addSlideChangeListener()

            val wormDotsIndicator = pageIndicator
            wormDotsIndicator.attachTo(this)
        }
    }

    private fun OnboardingViewBinding.addSlideChangeListener() {

        slider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (numberOfPages > 1) {
                    val newProgress = (position + positionOffset) / (numberOfPages - 1)
                    onboardingRoot.progress = newProgress
                }
            }
        })
    }

    private fun OnboardingViewBinding.addingButtonsClickListeners() {
        nextBtn.setOnClickListener { navigateToNextSlide(slider) }
        skipBtn.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
        startBtn.setOnClickListener() {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    private fun navigateToNextSlide(slider: ViewPager2?) {
        val nextSlidePos: Int = slider?.currentItem?.plus(1) ?: 0
        slider?.setCurrentItem(nextSlidePos, true)
    }
}