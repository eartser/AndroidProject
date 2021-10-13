package com.elizavetaartser.androidproject.ui.emailconfirmation

import androidx.fragment.app.viewModels
import com.elizavetaartser.androidproject.ui.base.BaseFragment
import com.elizavetaartser.androidproject.R
import com.elizavetaartser.androidproject.databinding.FragmentEmailConfirmationBinding

class EmailConfirmationFragment : BaseFragment(R.layout.fragment_email_confirmation) {

    private val viewBinding by viewBinding(FragmentEmailConfirmationBinding::bind)

    private val viewModel: EmailConfirmationViewModel by viewModels()
}