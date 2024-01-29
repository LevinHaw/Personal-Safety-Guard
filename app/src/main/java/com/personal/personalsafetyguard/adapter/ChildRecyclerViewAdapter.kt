package com.personal.personalsafetyguard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.personal.personalsafetyguard.R
import com.personal.personalsafetyguard.model.ChildItem

class ChildRecyclerViewAdapter(private var childList: List<ChildItem>, val listener: (ChildItem) -> Unit) :
    RecyclerView.Adapter<ChildRecyclerViewAdapter.ChildViewHolder>() {

    private var childItemList: List<ChildItem> = childList

    inner class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.childLogoIv)
        val title: TextView = itemView.findViewById(R.id.childTitleTv)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val childItem = childItemList[position + 1]
                    listener(childItem)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.child_item, parent, false)
        return ChildViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        val childItem: ChildItem? = childItemList.getOrNull(position + 1)
        if (childItem != null) {
            holder.title.text = childItem.childTitle
            Glide.with(holder.itemView.context).load(childItem.childImage)
                .into(holder.imageView)
        }
    }

    override fun getItemCount(): Int {
        return childItemList.size - 1
    }
}