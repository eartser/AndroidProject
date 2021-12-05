package com.elizavetaartser.androidproject.ui.likes

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.elizavetaartser.androidproject.R
import com.elizavetaartser.androidproject.databinding.FragmentLikesBinding
import com.elizavetaartser.androidproject.ui.base.BaseFragment

class LikesFragment : BaseFragment(R.layout.fragment_likes) {

    private val viewBinding by viewBinding(FragmentLikesBinding::bind)

    private val viewModel: LikesViewModel by viewModels()
}