package com.bmit.lib_core.view
import android.content.Context
import android.graphics.Outline
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.withStyledAttributes
import com.bmit.lib_core.R

class RoundedImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var cornerRadius = 0f

    init {
        // 读取自定义属性
        context.withStyledAttributes(attrs, R.styleable.RoundedImageView) {
            cornerRadius = getDimension(R.styleable.RoundedImageView_cornerRadius, 0f)
        }

        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, cornerRadius)
            }
        }
        clipToOutline = true
    }

    fun setCornerRadius(radius: Float) {
        cornerRadius = radius
        invalidateOutline()
    }
}