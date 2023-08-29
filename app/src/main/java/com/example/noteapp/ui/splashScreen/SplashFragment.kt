package com.example.noteapp.ui.splashScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentSplashBinding


class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val slideUpAnimation = AnimationUtils.loadAnimation(requireContext(),R.anim.slide_up)
        slideUpAnimation.duration = 3000

        slideUpAnimation.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationStart(animation: android.view.animation.Animation) {
            }

            override fun onAnimationEnd(animation: android.view.animation.Animation) {
                val action = SplashFragmentDirections.actionSplashFragmentToMainScreen()
                Navigation.findNavController(requireView()).navigate(action, NavOptions.Builder()
                    .setPopUpTo(R.id.splashFragment,true)
                    .build())
            }

            override fun onAnimationRepeat(animation: android.view.animation.Animation) {
            }
        })
        binding.slideUpImage.startAnimation(slideUpAnimation)
    }
}