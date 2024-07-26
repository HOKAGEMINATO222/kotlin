package com.example.navcomponment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import com.example.navcomponment.R

class FirstFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_first, container, false)

        // Find the textView1 and set OnClickListener
        val textView1: TextView = view.findViewById(R.id.textView1)
        textView1.setOnClickListener {
            val action = FirstFragmentDirections.action_firstFragment_to_secondFragment
            Navigation.findNavController(view).navigate(R.id.action_firstFragment_to_secondFragment)
        }

        return view
    }
}
