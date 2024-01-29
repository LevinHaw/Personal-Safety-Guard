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
import com.personal.personalsafetyguard.adapter.CardDetailsAdapter
import com.personal.personalsafetyguard.databinding.CardsDetailsBinding
import com.personal.personalsafetyguard.model.CardDetailsItem
import com.personal.personalsafetyguard.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardDetails : Fragment() {

    private lateinit var binding: CardsDetailsBinding
    private lateinit var viewModel : DetailsViewModel
    private lateinit var adapter : CardDetailsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CardsDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)

        initRecyclerView()
        setUpItemTouchHelper()
        observeValue()
    }

    private fun observeValue() {
        viewModel.getAllCardDetails().observe(viewLifecycleOwner, Observer {
            adapter = CardDetailsAdapter(context,it)
            binding.cardDetailsRecyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        })
    }

    private fun initRecyclerView() {
        binding.cardDetailsRecyclerView.layoutManager = LinearLayoutManager(context)

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
                    val cardItem: CardDetailsItem = adapter.getItemAt(swipedItemPosition)
                    viewModel.deleteCardDetails(cardItem.cardNumber)
                    adapter.notifyDataSetChanged()
                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.cardDetailsRecyclerView)
    }
}