package com.elizavetaartser.androidproject.ui.emailconfirmation

import android.os.Bundle
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.elizavetaartser.androidproject.R
import com.elizavetaartser.androidproject.databinding.FragmentEmailConfirmationBinding
import com.elizavetaartser.androidproject.ui.base.BaseFragment
import com.elizavetaartser.androidproject.util.extensions.getSpannedString
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timerTask

@AndroidEntryPoint
class EmailConfirmationFragment : BaseFragment(R.layout.fragment_email_confirmation) {

    private val viewBinding by viewBinding(FragmentEmailConfirmationBinding::bind)

    private val viewModel: EmailConfirmationViewModel by viewModels()

    private val email: String = "random_email@random.rand"
    private val password: String = "random_password"

    private val defaultSecsToWaitToResend: Int = 10
    private var secsToWaitToResend: Int = defaultSecsToWaitToResend

    private var refreshTimer: Timer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        viewBinding.confirmButton.setOnClickListener {
            viewModel.verifyCode(viewBinding.verificationCodeView.getCode(), email)
        }
        viewBinding.confirmButton.applyInsetter {
            type(navigationBars = true) { margin() }
        }
        viewBinding.backButton.applyInsetter {
            type(statusBars = true) { margin() }
        }
        subscribeToRefresh()
        subscribeToCodeView()
        subscribeToEvents()
    }

    private fun subscribeToEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventsFlow().collect { event ->
                    when (event) {
                        EmailConfirmationViewModel.Event.VerificationCodeSent -> {
                            Toast
                                .makeText(
                                    requireContext(),
                                    "Verification code sent",
                                    Toast.LENGTH_LONG
                                )
                                .show()
                        }
                        is EmailConfirmationViewModel.Event.VerificationSuccess -> {
                            viewModel.signIn(email, password)
                        }
                        is EmailConfirmationViewModel.Event.VerificationCodeSendError -> {
                            Toast
                                .makeText(
                                    requireContext(),
                                    "Verification code sending failed: ${
                                        event.e.body?.nonFieldErrors?.elementAtOrNull(0)?.message
                                    }",
                                    Toast.LENGTH_LONG
                                )
                                .show()
                        }
                        is EmailConfirmationViewModel.Event.VerificationFailed -> {
                            Toast
                                .makeText(
                                    requireContext(),
                                    "Verification failed: ${
                                        event.e.body?.nonFieldErrors?.elementAtOrNull(0)?.message
                                    }",
                                    Toast.LENGTH_LONG
                                )
                                .show()
                        }
                        else -> {
                            // Nothing
                        }
                    }
                }
            }
        }
    }

    private fun subscribeToCodeView() {
        decideFromCodeView(viewBinding.verificationCodeView.isFilled())
        viewBinding.verificationCodeView.onVerificationCodeFilledChangeListener = {
            decideFromCodeView(it)
        }
        viewBinding.verificationCodeView.onVerificationCodeFilledListener = { code ->
            viewModel.verifyCode(code, email)
        }
    }

    private fun decideFromCodeView(isFilled: Boolean) {
        viewBinding.confirmButton.isEnabled = isFilled
    }

    private fun stopTimer() {
        refreshTimer?.cancel()
        refreshTimer = null
    }

    private fun subscribeToRefresh() {
        viewModel.sendVerificationCode(email)
        secsToWaitToResend = defaultSecsToWaitToResend
        val timerTask = timerTask {
            activity?.runOnUiThread {
                decideRefreshText(secsToWaitToResend--)
            }
        }
        refreshTimer?.cancel()
        refreshTimer = Timer().apply {
            scheduleAtFixedRate(timerTask, 0, 1000)
        }
    }

    private fun decideRefreshText(sec: Int) {
        if (sec > 0) {
            viewBinding.refreshTextView.text = resources.getString(
                R.string.email_confirmation_refresh_wait_template, sec
            )
        } else {
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    subscribeToRefresh()
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = resources.getColor(R.color.purple_200, null)
                }
            }

            viewBinding.refreshTextView.movementMethod = LinkMovementMethod.getInstance()
            viewBinding.refreshTextView.text = buildSpannedString {
                inSpans(clickableSpan) {
                    append(resources.getSpannedString(R.string.email_confirmation_refresh_text))
                }
            }

            stopTimer()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopTimer()
    }
}