package com.personal.personalsafetyguard.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.personal.personalsafetyguard.model.ChildItem
import com.personal.personalsafetyguard.model.ParentItem

class FirebaseRepo(private val onRealtimeDbTaskComplete: OnRealtimeDbTaskComplete) {
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
        .child("Keamanan")

    fun getAllData() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val parentItemList: MutableList<ParentItem> = ArrayList()
                for (ds in snapshot.children) {
                    val parentItem = ParentItem()
                    parentItem.parentTitle = ds.child("parentTitle").getValue(String::class.java)
                    parentItem.parentImage = ds.child("parentImage").getValue(String::class.java)

                    val genericTypeIndicator = object : GenericTypeIndicator<List<ChildItem>>() {}

                    parentItem.childItemList = ds.child("childData").getValue(genericTypeIndicator) ?: emptyList()
                    parentItemList.add(parentItem)
                }
                Log.d("TAG", "onDataChange: $parentItemList")
                onRealtimeDbTaskComplete.onSuccess(parentItemList)
            }

            override fun onCancelled(error: DatabaseError) {
                onRealtimeDbTaskComplete.onFailure(error)
            }
        })
    }

    interface OnRealtimeDbTaskComplete {
        fun onSuccess(parentItemList: List<ParentItem>)
        fun onFailure(error: DatabaseError)
    }
}




