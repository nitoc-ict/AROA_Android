package jp.ac.okinawa_ct.nitoc_ict.aroa.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat

class ConverterVectorToBitmap {
    fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId)
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}