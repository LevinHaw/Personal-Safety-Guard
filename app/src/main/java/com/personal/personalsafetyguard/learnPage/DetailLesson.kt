package com.personal.personalsafetyguard.learnPage

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.personal.personalsafetyguard.R
import com.personal.personalsafetyguard.model.ChildItem

class DetailLesson : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_lesson)

        val childItem = intent.getParcelableExtra<ChildItem>(LearnPage.INTENT_PARCELABLE)

        val imageView: ImageView = findViewById(R.id.img_item)
        val titleTextView: TextView = findViewById(R.id.tv_item_title)
        val openTextView: TextView = findViewById(R.id.tv_item_open)
        val desc1TextView: TextView = findViewById(R.id.tv_item_desc1)
        val desc2TextView: TextView = findViewById(R.id.tv_item_desc2)
        val desc3TextView: TextView = findViewById(R.id.tv_item_desc3)
        val closeTextView: TextView = findViewById(R.id.tv_item_closing)
        val ref1TextView: TextView = findViewById(R.id.tv_item_reference1)
        val ref2TextView: TextView = findViewById(R.id.tv_item_reference2)
        val ref3TextView: TextView = findViewById(R.id.tv_item_reference3)
        val ref4TextView: TextView = findViewById(R.id.tv_item_reference4)

        val imageResId = childItem?.childBg
        val title = childItem?.childTitle
        val open = childItem?.childOpen
        val desc1 = childItem?.childDesc1
        val desc2 = childItem?.childDesc2
        val desc3 = childItem?.childDesc3
        val close = childItem?.childClose
        val ref1 = childItem?.childRef1
        val ref2 = childItem?.childRef2
        val ref3 = childItem?.childRef3
        val ref4 = childItem?.childRef4


        supportActionBar?.hide()

        // Load image using Glide
        if (imageResId != null) {
            Glide.with(this)
                .load(imageResId)
                .into(imageView)
        } else {

            imageView.setImageResource(R.drawable.top_background)
        }

        titleTextView.text = title
        openTextView.text = open
        desc1TextView.text = desc1
        desc2TextView.text = desc2
        desc3TextView.text = desc3
        closeTextView.text = close
        ref1TextView.text = ref1
        ref2TextView.text = ref2
        ref3TextView.text = ref3
        ref4TextView.text = ref4

        // Set OnClickListener for each reference TextView
        ref1TextView.setOnClickListener {
            openUrl(ref1)
        }

        ref2TextView.setOnClickListener {
            openUrl(ref2)
        }

        ref3TextView.setOnClickListener {
            openUrl(ref3)
        }

        ref4TextView.setOnClickListener {
            openUrl(ref4)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun openUrl(url: String?) {
        if (!url.isNullOrEmpty()) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }
}