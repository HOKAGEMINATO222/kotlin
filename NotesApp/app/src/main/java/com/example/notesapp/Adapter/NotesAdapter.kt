package com.example.notesapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.Fragment.FirstFragmentDirections
import com.example.notesapp.Model.Notes
import com.example.notesapp.R
import com.example.notesapp.databinding.ItemNotesBinding

class NotesAdapter(val requireContext: Context, var notesList: List<Notes>) : RecyclerView.Adapter<NotesAdapter.notesViewHolder>(){

    fun filtering(newFilteredList: ArrayList<Notes>) {
        notesList = newFilteredList
        notifyDataSetChanged()
    }

    class notesViewHolder( val binding: ItemNotesBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotesAdapter.notesViewHolder {
        return notesViewHolder(ItemNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: NotesAdapter.notesViewHolder, position: Int) {
        val data = notesList[position]
        holder.binding.notesTitle.text = data.title
        holder.binding.notesSubTitle.text = data.subTitle
        holder.binding.notesDate.text = data.date

        when(data.priority){
            "1" ->{
                holder.binding.viewPrioty.setBackgroundResource(R.drawable.green_dot)
            }
            "2" ->{
                holder.binding.viewPrioty.setBackgroundResource(R.drawable.yellow_dot)
            }
            "3" ->{
                holder.binding.viewPrioty.setBackgroundResource(R.drawable.red_dot)
            }
        }

        holder.binding.root.setOnClickListener{
            val action = FirstFragmentDirections.actionFirstFragmentToThirdFragment(data)
            Navigation.findNavController(it).navigate(action)

        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }


}