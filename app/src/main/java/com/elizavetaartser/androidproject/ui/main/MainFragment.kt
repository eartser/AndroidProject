package com.elizavetaartser.androidproject.ui.main

import androidx.fragment.app.viewModels
import com.elizavetaartser.androidproject.ui.base.BaseFragment
import com.elizavetaartser.androidproject.R
import com.elizavetaartser.androidproject.databinding.FragmentMainBinding

class MainFragment : BaseFragment(R.layout.fragment_main) {

    private val viewBinding by viewBinding(FragmentMainBinding::bind)

    private val viewModel: MainFragmentViewModel by viewModels()
}