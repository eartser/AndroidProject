package com.elizavetaartser.androidproject.ui.onboarding

import com.elizavetaartser.androidproject.databinding.ItemOnboardingTextBinding
import com.elizavetaartser.androidproject.util.extensions.dpToPx
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun onboardingTextAdapterDelegate() =
    adapterDelegateViewBinding<String, CharSequence, ItemOnboardingTextBinding>(
        viewBinding = { layoutInflater, parent ->
            ItemOnboardingTextBinding.inflate(layoutInflater, parent, false)
        },
        block = {
            bind {
                binding.textView.apply {
                    text = item
                    val padding = dpToPx(48.0f).toInt()
                    setPadding(padding, 0, padding, 0)
                }
            }
        }
    )