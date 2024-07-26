package com.example.spinwheel2.Fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluehomestudio.luckywheel.LuckyWheel
import com.example.spinwheel2.Adapter.RoulettesAdapter
import com.example.spinwheel2.R
import com.example.spinwheel2.ViewModel.RoulettesViewModel
import com.example.spinwheel2.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val roulettesViewModel: RoulettesViewModel by viewModels()
    private lateinit var lw: LuckyWheel
    private var inter: Int = 0
    private lateinit var adapter: RoulettesAdapter
    private var isDefaultItemsGenerated = false
    private var fireworkAnimationPlayed = false
    private var isWheelSpinning = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupNavigation()
        lw = binding.lwv

        adapter = RoulettesAdapter(emptyList(), roulettesViewModel, { roulette ->
            roulette.id?.let { rouletteId ->

                resetResult()
                roulettesViewModel.setSelectedRoulette(roulette)
                roulettesViewModel.generateWheelItemsFromRouletteId(rouletteId)
                binding.rouletteName.text = roulette.name

                binding.drawerLayout.closeDrawers()
            }
        }, { roulette ->
            val action = HomeFragmentDirections.actionHomeFragmentToEditRoulettesFragment(roulette)
            findNavController().navigate(action)
        })
        binding.roulettesRecyclerView.adapter = adapter
        binding.roulettesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        observeWheelItems()
        observeRoulettes()

        roulettesViewModel.selectedRoulette.observe(viewLifecycleOwner, Observer { roulette ->
            roulette?.let {
                resetResult()
                roulettesViewModel.generateWheelItemsFromRouletteId(it.id!!)
                binding.rouletteName.text = it.name
                isDefaultItemsGenerated = true
            }
        })

        if (!isDefaultItemsGenerated) {
            roulettesViewModel.generateDefaultWheelItems()
        }

        lw.setLuckyWheelReachTheTarget {
            if (!isWheelSpinning) return@setLuckyWheelReachTheTarget

            val wheelItems = roulettesViewModel.wheelItems.value
            if (wheelItems != null && inter > 0 && inter <= wheelItems.size) {
                val text = wheelItems[inter - 1].text
                val color = wheelItems[inter - 1].color

                binding.result.text = text
                setRoundedBackgroundColor(binding.result, color)
                binding.result.visibility = View.VISIBLE

                fadeIn(binding.result)
                slideUp(binding.linearLayout)

                if (!fireworkAnimationPlayed) {
                    playFireworkAnimation()
                }
            }

            isWheelSpinning = false
        }


        binding.btnPlay.setOnClickListener {
            if (isWheelSpinning) return@setOnClickListener

            fadeOut(binding.result)
            val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                it,
                PropertyValuesHolder.ofFloat("scaleX", 0.9f),
                PropertyValuesHolder.ofFloat("scaleY", 0.9f)
            )
            scaleDown.duration = 200

            scaleDown.repeatCount = 1
            scaleDown.repeatMode = ObjectAnimator.REVERSE

            scaleDown.start()

            val wheelItems = roulettesViewModel.wheelItems.value
            if (!wheelItems.isNullOrEmpty()) {
                inter = (1..wheelItems.size).random()
                lw.rotateWheelTo(inter)
                slideDown(binding.linearLayout)
                fireworkAnimationPlayed = false
                isWheelSpinning = true
            }
        }

        binding.result.setOnClickListener {
            fadeOut(binding.result)
        }

    }

    private fun resetResult() {
        fadeOut(binding.result)
    }

    private fun observeWheelItems() {
        roulettesViewModel.wheelItems.removeObservers(viewLifecycleOwner)
        roulettesViewModel.wheelItems.observe(viewLifecycleOwner, Observer { wheelItems ->
            Log.d("com.example.spinwheel2.Fragment.HomeFragment", "WheelItems size: ${wheelItems.size}")

            if (wheelItems.isNotEmpty()) {
                lw.addWheelItems(wheelItems)
            } else {
                Log.d("com.example.spinwheel2.Fragment.HomeFragment", "WheelItems is empty")
            }
        })
    }

    private fun observeRoulettes() {
        roulettesViewModel.getAllRoulettes().observe(viewLifecycleOwner, Observer { roulettes ->
            adapter.updateRoulettes(roulettes)
        })
    }

    private fun setupNavigation() {
        binding.btnOpenLeftDrawer.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.settingNav)
        }

        binding.btnOpenRightDrawer.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.roulettesNav)
        }

        binding.settingNav.getHeaderView(0).findViewById<ImageButton>(R.id.btn_exit).setOnClickListener {
            binding.drawerLayout.closeDrawers()
        }

        binding.roulettesNav.getHeaderView(0).findViewById<ImageButton>(R.id.btn_exit).setOnClickListener {
            binding.drawerLayout.closeDrawers()
        }

        binding.addBtn.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToAddRoulettesFragment()
            findNavController().navigate(action)
        }
    }

    private fun slideDown(view: View) {
        view.animate()
            .translationY(300f)
            .setDuration(300)
            .start()
    }

    private fun slideUp(view: View) {
        view.animate()
            .translationY(0f)
            .setDuration(300)
            .start()
    }

    private fun fadeIn(view: View) {
        view.animate()
            .translationY(40f)
            .alpha(1f)
            .setDuration(300)
            .start()
    }

    private fun fadeOut(view: View) {
        view.animate()
            .translationY(-200f)
            .alpha(0f)
            .setDuration(300)
            .withEndAction { view.visibility = View.GONE }
            .start()
    }

    private fun setRoundedBackgroundColor(view: View, color: Int) {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.cornerRadius = resources.getDimension(R.dimen.result_corner_radius)
        drawable.setColor(color)

        view.background = drawable
    }



    private fun playFireworkAnimation() {
        if (!fireworkAnimationPlayed) {
            binding.animationView1.visibility = View.VISIBLE
            binding.animationView1.playAnimation()
            binding.animationView1.addAnimatorListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    binding.animationView1.visibility = View.GONE

                }
            })

            binding.animationView2.visibility = View.VISIBLE
            binding.animationView2.playAnimation()
            binding.animationView2.addAnimatorListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    binding.animationView2.visibility = View.GONE

                }
            })

            fireworkAnimationPlayed = true
        }
    }




}
