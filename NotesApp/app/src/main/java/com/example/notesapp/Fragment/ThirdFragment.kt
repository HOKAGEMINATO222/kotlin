package com.example.notesapp.Fragment

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.notesapp.Model.Notes
import com.example.notesapp.R
import com.example.notesapp.ViewModel.NotesViewModel
import com.example.notesapp.databinding.FragmentThirdBinding
import com.example.notesapp.databinding.FragmentTwoBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.Date


class ThirdFragment : Fragment() {

    val Oldnotes by navArgs<ThirdFragmentArgs>()
    val viewModel: NotesViewModel by viewModels()

    var priority: String = "1"

    lateinit var binding : FragmentThirdBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentThirdBinding.inflate(layoutInflater, container, false)

        setHasOptionsMenu(true)

        binding.editTitle.setText(Oldnotes.data.title)
        binding.editSub.setText(Oldnotes.data.subTitle)
        binding.editNotes.setText(Oldnotes.data.notes)

        when(Oldnotes.data.priority){
            "1" ->{
                priority = "1"
                binding.pGreen.setImageResource(R.drawable.baseline_done_24)
                binding.pRed.setImageResource(0)
                binding.pYellow.setImageResource(0)
            }
            "2" ->{
                priority = "2"
                binding.pYellow.setImageResource(R.drawable.baseline_done_24)
                binding.pGreen.setImageResource(0)
                binding.pRed.setImageResource(0)
            }
            "3" ->{
                priority = "3"
                binding.pRed.setImageResource(R.drawable.baseline_done_24)
                binding.pGreen.setImageResource(0)
                binding.pYellow.setImageResource(0)
            }
        }

        binding.pGreen.setOnClickListener {
            priority = "1"
            binding.pGreen.setImageResource(R.drawable.baseline_done_24)
            binding.pRed.setImageResource(0)
            binding.pYellow.setImageResource(0)
        }

        binding.pYellow.setOnClickListener {
            priority = "2"
            binding.pYellow.setImageResource(R.drawable.baseline_done_24)
            binding.pGreen.setImageResource(0)
            binding.pRed.setImageResource(0)
        }

        binding.pRed.setOnClickListener {
            priority = "3"
            binding.pRed.setImageResource(R.drawable.baseline_done_24)
            binding.pGreen.setImageResource(0)
            binding.pYellow.setImageResource(0)
        }
        
        binding.btnSaveNotes.setOnClickListener{
            updateNotes(it)
        }

        return binding.root
    }

    private fun updateNotes(it: View?) {
        val title = binding.editTitle.text.toString()
        val subTitle = binding.editSub.text.toString()
        val notes = binding.editNotes.text.toString()

        val d = Date()
        val notesDate: CharSequence = DateFormat.format("MMMM d, yyyy", d.time)
        val data = Notes(Oldnotes.data.id, title = title, subTitle = subTitle, notes = notes, date = notesDate.toString(), priority)
        viewModel.addNotes(data)

        Toast.makeText(requireContext(), "Notes updated successfully", Toast.LENGTH_SHORT).show()

        Navigation.findNavController(it!!).navigate(R.id.action_thirdFragment_to_firstFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> {
                val bottomSheet : BottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetStyle)
                bottomSheet.setContentView(R.layout.dialog_delete)

                val textviewYes = bottomSheet.findViewById<TextView>(R.id.yes)
                val textviewNo = bottomSheet.findViewById<TextView>(R.id.no)

                textviewYes?.setOnClickListener {
                    viewModel.deleteNotes(Oldnotes.data.id!!)
                    Navigation.findNavController(requireView()).popBackStack()
                    bottomSheet.dismiss()
                }

                textviewNo?.setOnClickListener {
                    bottomSheet.dismiss()
                }

                bottomSheet.show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}