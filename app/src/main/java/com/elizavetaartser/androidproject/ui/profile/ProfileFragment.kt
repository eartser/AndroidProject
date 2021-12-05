package com.elizavetaartser.androidproject.ui.profile

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.elizavetaartser.androidproject.R
import com.elizavetaartser.androidproject.databinding.FragmentProfileBinding
import com.elizavetaartser.androidproject.ui.base.BaseFragment

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    private val viewBinding by viewBinding(FragmentProfileBinding::bind)

    private val viewModel: ProfileViewModel by viewModels()
}