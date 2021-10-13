package com.elizavetaartser.androidproject.ui.signin

import androidx.fragment.app.viewModels
import com.elizavetaartser.androidproject.ui.base.BaseFragment
import com.elizavetaartser.androidproject.R
import com.elizavetaartser.androidproject.databinding.FragmentSignInBinding

class SignInFragment : BaseFragment(R.layout.fragment_sign_in) {

    private val viewBinding by viewBinding(FragmentSignInBinding::bind)

    private val viewModel: SignInViewModel by viewModels()
}