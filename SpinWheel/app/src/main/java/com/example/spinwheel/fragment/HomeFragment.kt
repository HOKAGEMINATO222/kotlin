package com.example.spinwheel.fragment

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bluehomestudio.luckywheel.LuckyWheel
import com.bluehomestudio.luckywheel.WheelItem
import com.example.spinwheel.R
import com.example.spinwheel.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var lw: LuckyWheel
    private var wheelItems: MutableList<WheelItem> = mutableListOf()
    private var participants: MutableList<String> = mutableListOf()
    private var inter : Int = 0

    private val colors = listOf(
        Color.parseColor("#FF0000"),
        Color.parseColor("#FFA500"),
        Color.parseColor("#FFFF00"),
        Color.parseColor("#008000"),
        Color.parseColor("#0000FF"),
        Color.parseColor("#4B0082"),
        Color.parseColor("#8B00FF")
    )

    private val images = listOf(
        R.drawable.human1,
        R.drawable.human2,
        R.drawable.human3,
        R.drawable.human4,
        R.drawable.human5,
        R.drawable.human6,
        R.drawable.human7
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lw = binding.lwv

        initializeDefaultItems()

        binding.addParticipant.setOnClickListener {
            showDialog()
        }

        lw.setLuckyWheelReachTheTarget {
            val text = wheelItems[inter - 1].text
            Toast.makeText(requireContext(), text.toString(), Toast.LENGTH_LONG).show()
        }

        val start: Button = binding.start
        start.setOnClickListener {
            inter = (1..participants.size).random()
            lw.rotateWheelTo(inter)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeDefaultItems() {
        val size = 100

        val bitmap1 = BitmapFactory.decodeResource(resources, R.drawable.human1).rotate(30f)
        val scaledBitmap1 = Bitmap.createScaledBitmap(bitmap1, size, size, false)

        val bitmap2 = BitmapFactory.decodeResource(resources, R.drawable.human2).rotate(30f)
        val scaledBitmap2 = Bitmap.createScaledBitmap(bitmap2, size, size, false)

        wheelItems.apply {
            clear()
            add(WheelItem(colors[1], scaledBitmap1, "Participant 1"))
            add(WheelItem(colors[2], scaledBitmap2, "Participant 2"))
        }

        lw.addWheelItems(wheelItems)
    }

    private fun generateWheelItems() {
        val size = 100
        wheelItems.clear()

        participants.forEachIndexed { index, name ->
            val bitmap = BitmapFactory.decodeResource(resources, images[index % images.size]).rotate(30f)
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, size, size, false)
            val color = colors[index % colors.size]
            wheelItems.add(WheelItem(color, scaledBitmap, name))
        }

        lw.addWheelItems(wheelItems)
    }

    private fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
    }

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.add_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val numParticipantsEditText = dialog.findViewById<EditText>(R.id.numParticipants)
        val btnGenerateFields = dialog.findViewById<Button>(R.id.btnGenerateFields)
        val namesContainer = dialog.findViewById<LinearLayout>(R.id.namesContainer)
        val btnSubmit = dialog.findViewById<Button>(R.id.btnSubmit)

        btnGenerateFields.setOnClickListener {
            val numParticipants = numParticipantsEditText.text.toString().toIntOrNull()
            if (numParticipants != null) {
                namesContainer.removeAllViews()
                for (i in 1..numParticipants) {
                    val editText = EditText(requireContext()).apply {
                        hint = "Nhập tên người tham gia $i"
                    }
                    namesContainer.addView(editText)
                }
                btnSubmit.visibility = View.VISIBLE
            } else {
                Toast.makeText(requireContext(), "Vui lòng nhập số người tham gia hợp lệ", Toast.LENGTH_SHORT).show()
            }
        }

        btnSubmit.setOnClickListener {
            participants.clear()
            for (i in 0 until namesContainer.childCount) {
                val editText = namesContainer.getChildAt(i) as EditText
                val name = editText.text.toString()
                if (name.isNotEmpty()) {
                    participants.add(name)
                }
            }
            if (participants.isNotEmpty()) {
                generateWheelItems()
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Vui lòng nhập tên cho tất cả người tham gia", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}

