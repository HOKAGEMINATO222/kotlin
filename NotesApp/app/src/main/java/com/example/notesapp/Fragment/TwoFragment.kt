package com.example.notesapp.Fragment

import android.text.format.DateFormat
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.notesapp.Model.Notes
import com.example.notesapp.R
import com.example.notesapp.ViewModel.NotesViewModel
import com.example.notesapp.databinding.FragmentTwoBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*

class TwoFragment : Fragment() {
    private lateinit var binding: FragmentTwoBinding
    private var priority: String = "2"
    private val viewModel: NotesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTwoBinding.inflate(inflater, container, false)

        setupPriorityButtons()
        setupSaveButton()

        return binding.root
    }

    private fun setupPriorityButtons() {
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
    }



    private fun setupSaveButton() {
        binding.btnSaveNotes.setOnClickListener {
            createNotes()
        }
    }

    private fun createNotes() {
        val title = binding.editTitle.text.toString()
        val subTitle = binding.editSub.text.toString()
        val notes = binding.editNotes.text.toString()

        val d = Date()
        val notesDate: CharSequence = DateFormat.format("MMMM d, yyyy", d.time)
        val data = Notes(null, title, subTitle, notes, notesDate.toString(), priority)
        viewModel.addNotes(data)

        Toast.makeText(requireContext(), "Notes created successfully", Toast.LENGTH_SHORT).show()

        // Navigate back to the first fragment
        Navigation.findNavController(requireView()).navigate(R.id.action_twoFragment_to_firstFragment)
    }


}
