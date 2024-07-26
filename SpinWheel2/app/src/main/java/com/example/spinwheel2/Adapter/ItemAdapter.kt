package com.example.spinwheel2.Fragment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.spinwheel2.Model.Items
import com.example.spinwheel2.databinding.ItemItemBinding

class ItemAdapter(
    private val items: MutableList<Items>,
    private val onDeleteItem: (Items) -> Unit,
    private val onEditItem: (Items) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: ItemItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Items) {
            binding.apply {
                nameItem.text = item.name
                colorItem.setCardBackgroundColor(Color.parseColor(item.color))

                deleteItem.setOnClickListener {
                    onDeleteItem(item)
                }
                eidtItem.setOnClickListener {

                    val dialog = EditDialogFragment.newInstance(item.name, Color.parseColor(item.color))
                    dialog.onItemSaved = { newName, newColor ->
                        // Update the item and notify adapter
                        item.name = newName
                        item.color = String.format("#%06X", (0xFFFFFF and newColor))
                        notifyDataSetChanged()
                    }
                    dialog.show((binding.root.context as FragmentActivity).supportFragmentManager, "EditDialogFragment")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size
}
