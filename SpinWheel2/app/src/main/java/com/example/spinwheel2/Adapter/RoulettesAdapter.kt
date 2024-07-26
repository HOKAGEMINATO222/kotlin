package com.example.spinwheel2.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spinwheel2.Model.Roulettes
import com.example.spinwheel2.R
import com.example.spinwheel2.ViewModel.RoulettesViewModel

class RoulettesAdapter(
    private var roulettes: List<Roulettes>,
    private val viewModel: RoulettesViewModel,
    private val onItemClick: (Roulettes) -> Unit,
    private val onEditClick: (Roulettes) -> Unit
) : RecyclerView.Adapter<RoulettesAdapter.RoulettesViewHolder>() {

    inner class RoulettesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.roulette_name)
        private val moreVertButton: ImageButton = itemView.findViewById(R.id.more_vert)

        fun bind(roulette: Roulettes) {
            nameTextView.text = roulette.name
            itemView.setOnClickListener {
                // Gọi callback khi item được nhấp
                onItemClick(roulette)
            }

            moreVertButton.setOnClickListener { view ->
                showPopupMenu(view, roulette)
            }
        }

        private fun showPopupMenu(view: View, roulette: Roulettes) {
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.edit_item -> {
                        onEditClick(roulette) // Gọi callback edit
                        true
                    }
                    R.id.delete_item -> {
                        viewModel.deleteRoulette(roulette)
                        val updatedList = roulettes.toMutableList()
                        updatedList.remove(roulette)
                        updateRoulettes(updatedList)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoulettesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.roulettes_item, parent, false)
        return RoulettesViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoulettesViewHolder, position: Int) {
        holder.bind(roulettes[position])
    }

    override fun getItemCount(): Int = roulettes.size

    fun updateRoulettes(newRoulettes: List<Roulettes>) {
        roulettes = newRoulettes
        notifyDataSetChanged()
    }
}

