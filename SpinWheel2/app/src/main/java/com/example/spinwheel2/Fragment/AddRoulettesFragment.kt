package com.example.spinwheel2.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spinwheel2.Adapter.TagsAdapter
import com.example.spinwheel2.Model.Items
import com.example.spinwheel2.Model.Roulettes
import com.example.spinwheel2.ViewModel.RoulettesViewModel
import com.example.spinwheel2.databinding.FragmentAddRoulettesBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class AddRoulettesFragment : Fragment() {

    private lateinit var binding: FragmentAddRoulettesBinding

    private val items = mutableListOf<Items>()
    private lateinit var adapter: ItemAdapter
    private var rouletteId: Int? = null
    private lateinit var viewModel: RoulettesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddRoulettesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(RoulettesViewModel::class.java)


        val tags = listOf("+", "Tag 1", "Tag 2", "Tag 3", "Tag 4", "Tag 5", "Tag 6")
        val tagsLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.tagRecyclerView.layoutManager = tagsLayoutManager
        val tagsAdapter = TagsAdapter(tags)
        binding.tagRecyclerView.adapter = tagsAdapter

        // Setup RecyclerView cho items
        val itemsLayoutManager = LinearLayoutManager(requireContext())
        binding.listItem.layoutManager = itemsLayoutManager
        adapter = ItemAdapter(items, { item ->
            viewModel.deleteItem(item)
            items.remove(item)
            adapter.notifyDataSetChanged()
            Snackbar.make(binding.root, "Xóa thành công", Snackbar.LENGTH_SHORT).show()
        }, { item ->

        })
        binding.listItem.adapter = adapter

        binding.btnOpenLeftDrawer.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()

        }

        binding.btnAddItem.setOnClickListener {
            val dialog = ItemDialogFragment()
            dialog.onItemSaved = { itemName, itemColor ->
                val newItem = Items(name = itemName, color = String.format("#%06X", (0xFFFFFF and itemColor)), rouletteId = rouletteId ?: 0)
                items.add(newItem)
                adapter.notifyDataSetChanged()
            }
            dialog.show(childFragmentManager, "com.example.spinwheel2.Fragment.ItemDialogFragment")
        }

        binding.btnSaveRoulettes.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        val rouletteName = binding.rouletteNameEditText.text.toString()
        val tags = "Tag1"

        val newRoulette = Roulettes(name = rouletteName, tags = tags)
        viewModel.insertRoulette(newRoulette).observe(viewLifecycleOwner) { newRouletteId ->
            items.forEach { item ->
                viewModel.insertItems(listOf(item.copy(rouletteId = newRouletteId.toInt())))
            }

            Snackbar.make(binding.root, "Đã thêm thành công", Snackbar.LENGTH_SHORT).show()

            requireActivity().onBackPressed()
        }
    }


}


