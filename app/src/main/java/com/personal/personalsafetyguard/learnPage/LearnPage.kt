package com.personal.personalsafetyguard.learnPage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.personal.personalsafetyguard.R
import com.personal.personalsafetyguard.adapter.ParentRecyclerViewAdapter
import com.personal.personalsafetyguard.model.ChildItem
import com.personal.personalsafetyguard.model.ParentItem
import com.personal.personalsafetyguard.viewmodel.FirebaseDataViewModel
import androidx.lifecycle.Observer
import java.util.Locale

class LearnPage : AppCompatActivity() {

    private lateinit var parentRecyclerView: RecyclerView
    private lateinit var firebaseViewModel: FirebaseDataViewModel
    private lateinit var parentAdapter: ParentRecyclerViewAdapter
    private lateinit var searchView: SearchView
    private var parentItemList: List<ParentItem> = emptyList()

    companion object {
        const val INTENT_PARCELABLE = "child_item"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn_page)

        parentRecyclerView = findViewById(R.id.parentRecyclerView)
        searchView = findViewById(R.id.searchView)

        parentRecyclerView.setHasFixedSize(true)
        parentRecyclerView.layoutManager = LinearLayoutManager(this)
        parentAdapter = ParentRecyclerViewAdapter()
        parentRecyclerView.adapter = parentAdapter

        firebaseViewModel = FirebaseDataViewModel()

        supportActionBar?.hide()

        parentAdapter.setOnChildItemClickListener { childItem ->
            if (childItem != null) {
                navigateToDetailLesson(childItem)
            }
        }

        firebaseViewModel.getAllData()
        firebaseViewModel.getParentItemMutableLiveData().observe(this, Observer { parentItemList ->
            this.parentItemList = parentItemList
            parentAdapter.setParentItemList(parentItemList)
            parentAdapter.notifyDataSetChanged()
        })
        firebaseViewModel.getDatabaseErrorMutableLiveData().observe(this, Observer { error ->
            Toast.makeText(this@LearnPage, error.toString(), Toast.LENGTH_SHORT).show()
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })


    }

    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList = parentItemList.filter { parentItem ->
                val matchingChildren = parentItem.childItemList.filter { childItem ->
                    childItem?.childTitle?.lowercase(Locale.ROOT)?.contains(query) == true
                }
                parentItem.parentTitle?.lowercase(Locale.ROOT)?.contains(query) == true || matchingChildren.isNotEmpty()
            }
            parentAdapter.setFilteredList(filteredList)
        } else {
            parentAdapter.setParentItemList(parentItemList)
        }
    }

    private fun navigateToDetailLesson(childItem: ChildItem) {
        val intent = Intent(this, DetailLesson::class.java)
        intent.putExtra(INTENT_PARCELABLE, childItem)
        startActivity(intent)
    }
}
