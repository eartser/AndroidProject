package com.elizavetaartser.androidproject.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.elizavetaartser.androidproject.R
import com.elizavetaartser.androidproject.databinding.FragmentProfileBinding
import com.elizavetaartser.androidproject.entity.User
import com.elizavetaartser.androidproject.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    private val viewBinding by viewBinding(FragmentProfileBinding::bind)

    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToEvents()
        viewBinding.logoutButton.applyInsetter {
            type(statusBars = true) { margin() }
        }
        viewBinding.userLoginTextView.applyInsetter {
            type(statusBars = true) { margin() }
        }
        viewBinding.logoutButton.setOnClickListener {
            viewModel.logout()
        }
        fillPersonalInfo()
    }

    private fun subscribeToEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventsFlow().collect { event ->
                    when (event) {
                        is ProfileViewModel.Event.LogoutError -> {
                            Toast
                                .makeText(
                                    requireContext(),
                                    R.string.common_general_error_text,
                                    Toast.LENGTH_LONG
                                )
                                .show()
                        }
                        is ProfileViewModel.Event.ProfileLoaded -> {
                            fillPersonalInfo()
                        }
                    }
                }
            }
        }
    }

    private fun fillPersonalInfo() {
        viewBinding.apply {
            viewModel.user?.apply {
                userLoginTextView.text = login
                userNameTextView.text = "${first_name} ${last_name}"
                Glide.with(avatarImageView)
                    .load(avatar)
                    .into(avatarImageView)
                groupNameTextView.text = group_name
            }
        }
    }
}