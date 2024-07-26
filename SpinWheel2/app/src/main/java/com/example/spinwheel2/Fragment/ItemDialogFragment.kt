package com.example.spinwheel2.Fragment

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.spinwheel2.R
import com.example.spinwheel2.databinding.FragmentItemDialogBinding
import com.flask.colorpicker.OnColorSelectedListener
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ItemDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentItemDialogBinding
    var onItemSaved: ((String, Int) -> Unit)? = null
    private var selectedColor: Int = Color.WHITE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return inflater.inflate(R.layout.fragment_item_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentItemDialogBinding.bind(view)

        binding.btnSave.setOnClickListener {
            val itemName = binding.etItemName.text.toString()
            onItemSaved?.invoke(itemName, selectedColor)
            dismiss()
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.btnColorItem.setOnClickListener {
            showColorPickerDialog()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
            bottomSheet.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            bottomSheet.requestLayout()
        }
        return dialog
    }

    private fun showColorPickerDialog() {
        ColorPickerDialogBuilder
            .with(requireContext())
            .setTitle("Choose Color")
            .initialColor(selectedColor)
            .density(12)
            .setOnColorSelectedListener(object : OnColorSelectedListener {
                override fun onColorSelected(color: Int) {
                    Toast.makeText(
                        requireContext(),
                        "onColorSelected: 0x${Integer.toHexString(color)}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
            .setPositiveButton("ok") { dialog, color, allColors ->
                changeBackgroundColor(color)
            }
            .setNegativeButton("cancel") { dialog, which ->

            }
            .build()
            .show()
    }

    private fun changeBackgroundColor(color: Int) {
        selectedColor = color
        binding.btnColorItem.setBackgroundColor(color)
    }
}
