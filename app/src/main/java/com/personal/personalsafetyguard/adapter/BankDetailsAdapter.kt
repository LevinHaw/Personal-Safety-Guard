package com.personal.personalsafetyguard.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.personal.personalsafetyguard.R
import com.personal.personalsafetyguard.databinding.BankListItemBinding

import com.personal.personalsafetyguard.model.BankDetailsItem
import com.personal.personalsafetyguard.model.LoginDetailsItem
import com.personal.personalsafetyguard.ui.dialog.BankDialog


class BankDetailsAdapter(private var mContext : Context?, private var mList : List<BankDetailsItem>, private var fragmentManager : FragmentManager) : RecyclerView.Adapter<BankDetailsAdapter.BankDetailsViewHolder>() {
    private  lateinit var binding : BankListItemBinding



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankDetailsViewHolder {
        binding = BankListItemBinding.inflate(LayoutInflater.from(mContext),parent,false)
        return BankDetailsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: BankDetailsViewHolder, position: Int) {
            binding.bankName.text = mList.get(position).bankName
            binding.bankAccountNumber.text = mList[position].bankAccNumber.toString()

            binding.bankItemCard.setOnClickListener {
                val dialog = BankDialog(mList[position])
                dialog.show(fragmentManager, "Bank Dialog")
            }

        setItemIcon(binding.itemIcon, mList.get(position).bankName)

    }

    private fun setItemIcon(itemIcon: ImageView, name: String) {
        when(name.lowercase().trim()){
            "mandiri" -> itemIcon.setImageResource(R.drawable.mandiri)
            "bca" -> itemIcon.setImageResource(R.drawable.bca)
            "bni" -> itemIcon.setImageResource(R.drawable.bni)
            "bri" -> itemIcon.setImageResource(R.drawable.bri)
            "cimb niaga" -> itemIcon.setImageResource(R.drawable.cimb_niaga)
            "mega" -> itemIcon.setImageResource(R.drawable.mega)
            "permata" -> itemIcon.setImageResource(R.drawable.permata)
            "btn" -> itemIcon.setImageResource(R.drawable.btn)
            "bukopin" -> itemIcon.setImageResource(R.drawable.bukopin)
            "danamon" -> itemIcon.setImageResource(R.drawable.danamon)
            "maybank" -> itemIcon.setImageResource(R.drawable.maybank)
            "sinarmas" -> itemIcon.setImageResource(R.drawable.sinarmas)
            "ocbc nisp" -> itemIcon.setImageResource(R.drawable.ocbc)
            "dki" -> itemIcon.setImageResource(R.drawable.dki)
            "jateng" -> itemIcon.setImageResource(R.drawable.jateng)
            "jatim" -> itemIcon.setImageResource(R.drawable.jatim)
            "bjb" -> itemIcon.setImageResource(R.drawable.bjb)

        }

    }

    class BankDetailsViewHolder(binding: BankListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }
    fun getItemAt(position : Int) : BankDetailsItem {
        return mList[position]
    }
}