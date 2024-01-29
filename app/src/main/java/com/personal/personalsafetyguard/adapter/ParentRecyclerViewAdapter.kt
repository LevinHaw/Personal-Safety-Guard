package com.personal.personalsafetyguard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.personal.personalsafetyguard.R
import com.personal.personalsafetyguard.model.ChildItem
import com.personal.personalsafetyguard.model.ParentItem

class ParentRecyclerViewAdapter :
    RecyclerView.Adapter<ParentRecyclerViewAdapter.ParentRecyclerViewHolder>() {

    private var parentItemList: List<ParentItem>? = null
    private var onChildItemClickListener: ((ChildItem?) -> Unit)? = null

    fun setOnChildItemClickListener(listener: ((ChildItem?) -> Unit)?) {
        onChildItemClickListener = listener
    }

    fun setParentItemList(parentItemList: List<ParentItem>?) {
        this.parentItemList = parentItemList ?: emptyList()
    }

    fun setFilteredList(parentItemList: List<ParentItem>) {
        this.parentItemList = parentItemList
        notifyDataSetChanged()
    }

    inner class ParentRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val parentImageView: ImageView
        val parentTitle: TextView
        val childRecyclerView: RecyclerView
        val constraintLayout: ConstraintLayout

        init {
            parentImageView = itemView.findViewById(R.id.parentLogoIv)
            parentTitle = itemView.findViewById(R.id.parentTitleTv)
            childRecyclerView = itemView.findViewById(R.id.childRecyclerView)
            constraintLayout = itemView.findViewById(R.id.constraintLayout)

            itemView.setOnClickListener {
                val childItemPosition = adapterPosition
                if (childItemPosition != RecyclerView.NO_POSITION) {
                    val parentItem = parentItemList?.get(childItemPosition)
                    val childItemList = parentItem?.childItemList
                    val childItem = childItemList?.getOrNull(0) // Get the first childItem or null
                    onChildItemClickListener?.invoke(childItem)
                } else {
                    onChildItemClickListener?.invoke(null)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.parent_item, parent, false)
        return ParentRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParentRecyclerViewHolder, position: Int) {
        val parentItem: ParentItem = parentItemList!![position]

        holder.parentTitle.text = parentItem.parentTitle
        Glide.with(holder.itemView.context).load(parentItem.parentImage)
            .into(holder.parentImageView)

        holder.childRecyclerView.setHasFixedSize(true)
        holder.childRecyclerView.layoutManager = GridLayoutManager(holder.itemView.context, 3)

        val childItemList = parentItem.childItemList

        if (childItemList != null) {
            val modifiedChildItemList = childItemList.subList(0, childItemList.size)
            val adapter = ChildRecyclerViewAdapter(modifiedChildItemList) { childItem ->
                onChildItemClickListener?.invoke(childItem)
            }
            holder.childRecyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }

        //expandable functionality
        val isExpandable = parentItem.isExpandable
        holder.childRecyclerView.visibility = if (isExpandable) View.VISIBLE else View.GONE

        holder.constraintLayout.setOnClickListener {
            isAnyItemExpanded(position)
            parentItem.isExpandable = !parentItem.isExpandable
            notifyItemChanged(position)
        }
    }

    private fun isAnyItemExpanded(position: Int) {
        val temp = parentItemList?.indexOfFirst {
            it.isExpandable
        } ?: -1

        if (temp >= 0 && temp != position) {
            parentItemList?.get(temp)?.isExpandable = false
            notifyItemChanged(temp)
        }
    }

    override fun getItemCount(): Int {
        return parentItemList?.size ?: 0
    }

    interface OnChildItemClickListener {
        fun onChildItemClick(childItem: ChildItem)
    }
}