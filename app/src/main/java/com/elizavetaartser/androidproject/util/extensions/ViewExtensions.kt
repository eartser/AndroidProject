package com.elizavetaartser.androidproject.util.extensions

import android.view.View
import androidx.annotation.Dimension
import com.elizavetaartser.androidproject.util.dpToPx

fun View.dpToPx(@Dimension(unit = Dimension.DP) dp: Float): Float = dpToPx(resources.displayMetrics, dp)