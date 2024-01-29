package com.personal.personalsafetyguard.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.personal.personalsafetyguard.adapter.BankDetailsAdapter
import com.personal.personalsafetyguard.databinding.BankDetailsBinding
import com.personal.personalsafetyguard.model.BankDetailsItem
import com.personal.personalsafetyguard.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BankDetails : Fragment() {
    private lateinit var binding: BankDetailsBinding
    private lateinit var viewModel : DetailsViewModel
    private lateinit var adapter : BankDetailsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BankDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
        setUpItemTouchHelper()
        initRecyclerView()
        observeValue()

    }

    private fun observeValue() {
        viewModel.getAllBankDetails().observe(viewLifecycleOwner, Observer {
            adapter = BankDetailsAdapter(context,it,requireActivity().supportFragmentManager)
            binding.bankDetailsRecyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        })
    }

    private fun initRecyclerView() {
        binding.bankDetailsRecyclerView.layoutManager = LinearLayoutManager(context)

    }

    private fun setUpItemTouchHelper() {
        val simpleCallback: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    val swipedItemPosition = viewHolder.adapterPosition
                    val bankItem: BankDetailsItem = adapter.getItemAt(swipedItemPosition)
                    viewModel.deleteBankDetails(bankItem.bankAccNumber)
                    adapter.notifyDataSetChanged()
                    Toast.makeText(context, "Item dihapus", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.bankDetailsRecyclerView)
    }
}