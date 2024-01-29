package com.personal.personalsafetyguard.feature.onboarding.entity

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.personal.personalsafetyguard.R

enum class OnBoardingPage(
    @StringRes val titleResource: Int,
    @StringRes val subTitleResource: Int,
    @StringRes val descriptionResource: Int,
    @RawRes val logoResource: Int
) {
    ONE(R.string.onboarding_slide1_title, R.string.onboarding_slide1_subtitle,R.string.onboarding_slide1_desc, R.raw.forgot_pasword),
    TWO(R.string.onboarding_slide1_title, R.string.onboarding_slide2_subtitle,R.string.onboarding_slide2_desc, R.raw.vibrate_phone),
    THREE(R.string.onboarding_slide1_title, R.string.onboarding_slide3_subtitle,R.string.onboarding_slide3_desc, R.raw.phone_message),
    FOUR(R.string.onboarding_slide1_title, R.string.onboarding_slide4_subtitle,R.string.onboarding_slide4_desc, R.raw.learn)
}