package com.elizavetaartser.androidproject.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.elizavetaartser.androidproject.R
import com.elizavetaartser.androidproject.databinding.FragmentOnboardingBinding
import com.elizavetaartser.androidproject.ui.base.BaseFragment
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import dev.chrisbanes.insetter.applyInsetter
import java.util.*
import kotlin.math.abs
import com.elizavetaartser.androidproject.util.extensions.dpToPx

class OnboardingFragment : BaseFragment(R.layout.fragment_onboarding) {

    private val viewBinding by viewBinding(FragmentOnboardingBinding::bind)

    private val viewModel: OnboardingViewModel by viewModels()

    private var player: ExoPlayer? = null
    private var scrollTimer: Timer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        player = SimpleExoPlayer.Builder(requireContext()).build().apply {
            addMediaItem(MediaItem.fromUri("asset:///onboarding.mp4"))
            repeatMode = Player.REPEAT_MODE_ALL
            prepare()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.playerView.player = player
        viewBinding.viewPager.setTextPages()
        viewBinding.viewPager.attachDots(viewBinding.onboardingTextTabLayout)
        viewBinding.viewPager.offscreenPageLimit = 1
        viewBinding.viewPager.setPageTransformer { page, position ->
            val distance = abs(position)
            page.scaleX = 1 - distance / 2
            page.scaleY = 1 - distance / 2
            page.alpha = 1 - distance * 0.3f
            page.translationX = -view.dpToPx(200.0f) * position
        }
        viewBinding.signInButton.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_signInFragment)
        }
        viewBinding.signUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_signUpFragment)
        }
        viewBinding.volumeControlButton.applyInsetter {
            type(statusBars = true) { margin() }
        }

        viewBinding.signUpButton.applyInsetter {
            type(navigationBars = true) { margin() }
        }
        setVolume()
        viewBinding.volumeControlButton.setOnClickListener {
            viewBinding.playerView.player?.let {
                viewModel.isMuted = !viewModel.isMuted
                setVolume()
            }
        }
        startScrollTimer()
    }

    override fun onResume() {
        super.onResume()
        player?.play()
        startScrollTimer()
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
        stopScrollTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        stopScrollTimer()
    }

    private fun ViewPager2.setTextPages() {
        adapter =
            ListDelegationAdapter(onboardingTextAdapterDelegate()).apply {
                items =
                    listOf(
                        getString(R.string.onboarding_view_pager_text_1),
                        getString(R.string.onboarding_view_pager_text_2),
                        getString(R.string.onboarding_view_pager_text_3)
                    )
            }
    }

    private fun ViewPager2.attachDots(tabLayout: TabLayout) {
        TabLayoutMediator(tabLayout, this) { _, _ -> }.attach()
    }

    private fun setVolume() {
        if (viewModel.isMuted) {
            viewBinding.playerView.player?.volume = 0F
            viewBinding.volumeControlButton.setImageResource(R.drawable.ic_volume_off_white_24dp)
        }
        else {
            viewBinding.playerView.player?.volume = 1F
            viewBinding.volumeControlButton.setImageResource(R.drawable.ic_volume_up_white_24dp)
        }
    }

    private fun startScrollTimer() {
        scrollTimer?.cancel()
        scrollTimer = Timer().apply {
            scheduleAtFixedRate(scrollTask(), 4000, 4000)
        }
    }

    private fun stopScrollTimer() {
        scrollTimer?.cancel()
        scrollTimer = null
    }

    private fun scrollTask() = object : TimerTask() {
        override fun run() {
            activity?.runOnUiThread {
                viewBinding.viewPager.apply {
                    setCurrentItem((currentItem + 1) % (adapter?.itemCount ?: 1), true)
                }
            }
        }
    }

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            when (state) {
                ViewPager2.SCROLL_STATE_IDLE -> {
                    startScrollTimer()
                }
                ViewPager2.SCROLL_STATE_DRAGGING -> {
                    stopScrollTimer()
                }
                ViewPager2.SCROLL_STATE_SETTLING -> {
                    stopScrollTimer()
                }
            }
        }
    }

}