package com.elizavetaartser.androidproject.ui.likes

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.elizavetaartser.androidproject.R
import com.elizavetaartser.androidproject.databinding.FragmentLikesBinding
import com.elizavetaartser.androidproject.ui.base.BaseFragment

class LikesFragment : BaseFragment(R.layout.fragment_likes) {

    private val viewBinding by viewBinding(FragmentLikesBinding::bind)

    private val viewModel: LikesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cnt = 20
        viewBinding.postLikesView.avatars = MutableList(cnt) { null }
        repeat(cnt) { id ->
            Glide.with(viewBinding.postLikesView)
                .asBitmap()
                .load("https://d2ph5fj80uercy.cloudfront.net/05/cat${1000 + id}.jpg")
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        viewBinding.postLikesView.updateAvatar(id, resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })
        }
    }
}