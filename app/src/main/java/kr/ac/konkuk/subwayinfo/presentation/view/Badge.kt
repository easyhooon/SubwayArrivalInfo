package kr.ac.konkuk.subwayinfo.presentation.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import kr.ac.konkuk.subwayinfo.extension.dip

class Badge constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    var badgeColor = Color.LTGRAY
        set(value) {
            field = value
            invalidate()
        }

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        val verticalPadding = dip(4f)
        val horizontalPadding = dip(8f)
        setPadding(
            horizontalPadding,
            verticalPadding,
            horizontalPadding,
            verticalPadding
        )
        setTextColor(Color.WHITE)
        textSize = 12f
        typeface = Typeface.DEFAULT_BOLD
        background = null
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        canvas.drawRoundRect(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            height / 2f, height / 2f,
            paint.apply { color = badgeColor }
        )

        //super.onDraw()가 호출되기 전에 무슨 로직을 수행하면 바탕에 뭔가 그려지게 되고 그위에 super.onDraw()가 텍스트를 그리게 됨
        super.onDraw(canvas)
    }
}