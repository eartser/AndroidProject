package com.elizavetaartser.androidproject.ui.news

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isInvisible
import com.elizavetaartser.androidproject.R
import com.elizavetaartser.androidproject.util.extensions.dpToPx
import com.elizavetaartser.androidproject.util.extensions.getCompatColor
import com.elizavetaartser.androidproject.util.extensions.inflate

class PostLikesView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleRes) {

    private var textView: TextView

    private var avatarRadius: Float = dpToPx(10f)

    var avatars: MutableList<Bitmap?> = mutableListOf()
        set(value) {
            field = value.map { bm ->
                bm?.let {
                    Bitmap.createScaledBitmap(
                        getCroppedBitmap(bm), (2 * avatarRadius).toInt(),
                        (2 * avatarRadius).toInt(), false
                    )
                }
            }.toMutableList()
            invalidate()
        }

    private var visibleColorCount: Int = 0
    private var collapsedColorCount: Int = 0

    private var circlePaint: Paint =
        Paint().apply {
            color = context.getCompatColor(R.color.color_post_like_background)
            strokeWidth = avatarRadius
        }
    private var avatarPaint: Paint = Paint()

    init {
        setWillNotDraw(false)
        textView = (inflate(R.layout.item_post_remaining_likes_count, false) as TextView)
            .also { textView ->
                addView(
                    textView,
                    LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT,
                        Gravity.END or Gravity.CENTER_VERTICAL
                    )
                )
            }
        if (isInEditMode) {
            avatars = MutableList(25) {
                AppCompatResources
                    .getDrawable(context, R.drawable.ic_mkn_logo)
                    ?.toBitmap()
            }
            setCollapsedColorCountText(avatars.size)
        }
    }

    fun updateAvatar(id: Int, bm: Bitmap) {
        avatars[id] = bm
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setCollapsedColorCountText(avatars.size)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (avatars.isNotEmpty()) {
            val widthOfOneColor = (avatarRadius * 3).toInt()
            val widthOfAllColors = avatars.size * widthOfOneColor
            val widthWithoutPadding = measuredWidth - paddingStart - paddingEnd
            var availableSpace = widthWithoutPadding
            if (availableSpace in 1..widthOfAllColors) {
                var visibleColorCount = availableSpace / widthOfOneColor + 1
                do {
                    visibleColorCount--
                    setCollapsedColorCountText(avatars.size - visibleColorCount)
                    measureChild(textView, widthMeasureSpec, heightMeasureSpec)
                    availableSpace = widthWithoutPadding - textView.measuredWidth
                } while (0 < visibleColorCount && availableSpace <= visibleColorCount * widthOfOneColor)
                textView.isInvisible = false
                this.visibleColorCount = visibleColorCount
                collapsedColorCount = avatars.size - visibleColorCount
            } else {
                textView.isInvisible = true
                visibleColorCount = avatars.size
                collapsedColorCount = 0
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val yPos = height / 2f
        var xPos = paddingStart + avatarRadius
        for (i in 0 until visibleColorCount) {
            canvas.drawCircle(
                xPos, yPos, avatarRadius + dpToPx(2f), circlePaint
            )
            avatars[i]?.let {
                canvas.drawBitmap(
                    it, xPos - avatarRadius,
                    yPos - avatarRadius, avatarPaint
                )
            }
            xPos += (avatarRadius * 3)
        }
    }

    private fun setCollapsedColorCountText(count: Int) {
        textView.text = resources.getString(R.string.common_counter_prefix, count)
    }

    private fun getCroppedBitmap(bm: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(
            bm.width, bm.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val rect = Rect(0, 0, bm.width, bm.height)
        val paint = Paint().apply {
            color = 0xff424242.toInt()
        }

        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(
            bm.width / 2f, bm.height / 2f,
            bm.width / 2f, paint
        )
        canvas.drawBitmap(bm, rect, rect, paint.apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        })
        return output
    }
}