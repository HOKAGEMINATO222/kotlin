package com.example.spinwheel2.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spinwheel2.Model.Items
import com.example.spinwheel2.Model.Roulettes
import com.example.spinwheel2.ViewModel.RoulettesViewModel
import com.example.spinwheel2.databinding.FragmentEditRoulettesBinding
import com.google.android.material.snackbar.Snackbar

class EditRoulettesFragment : Fragment() {

    private lateinit var binding: FragmentEditRoulettesBinding
    private val items = mutableListOf<Items>()
    private val deletedItems = mutableListOf<Items>()
    private lateinit var itemAdapter: ItemAdapter
    private var rouletteId: Int? = null
    private val args: EditRoulettesFragmentArgs by navArgs()
    private lateinit var viewModel: RoulettesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditRoulettesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(RoulettesViewModel::class.java)
        val roulette = args.roulette
        rouletteId = roulette.id
        binding.editRouletteName.setText(roulette.name)

        binding.close.setOnClickListener {
            requireActivity().onBackPressed()
        }

        // Setup RecyclerView cho items
        val itemsLayoutManager = LinearLayoutManager(requireContext())
        binding.itemRecyclerView.layoutManager = itemsLayoutManager
        itemAdapter = ItemAdapter(items, { item ->
            items.remove(item)
            deletedItems.add(item)
            itemAdapter.notifyDataSetChanged()
            Snackbar.make(binding.root, "Đã xóa tạm thời", Snackbar.LENGTH_SHORT).show()
        }, { item ->
            // Handle edit item here
        })
        binding.itemRecyclerView.adapter = itemAdapter

        // Load existing items
        viewModel.getItemsByRouletteId(roulette.id ?: 0).observe(viewLifecycleOwner) { loadedItems ->
            items.clear()
            items.addAll(loadedItems)
            itemAdapter.notifyDataSetChanged()
        }

        binding.btnAddItem.setOnClickListener {
            val dialog = ItemDialogFragment()
            dialog.onItemSaved = { itemName, itemColor ->
                val newItem = Items(name = itemName, color = String.format("#%06X", (0xFFFFFF and itemColor)), rouletteId = rouletteId ?: 0)
                items.add(newItem)
                itemAdapter.notifyDataSetChanged()
            }
            dialog.show(childFragmentManager, "com.example.spinwheel2.Fragment.ItemDialogFragment")
        }

        binding.saveButton.setOnClickListener {
            saveData(roulette)
        }
    }

    private fun saveData(roulette: Roulettes) {
        val updatedName = binding.editRouletteName.text.toString()
        val updatedRoulette = roulette.copy(name = updatedName)
        viewModel.updateRoulette(updatedRoulette)

        viewModel.deleteItemsByRouletteId(roulette.id ?: 0)
        viewModel.insertItems(items)

        deletedItems.forEach { item ->
            viewModel.deleteItem(item)
        }

        Snackbar.make(binding.root, "Cập nhật thành công", Snackbar.LENGTH_SHORT).show()

        requireActivity().onBackPressed()
    }
}
