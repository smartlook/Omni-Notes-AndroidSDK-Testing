package it.feio.android.omninotes.smartlook

import android.graphics.Rect
import android.view.View

private const val LOCATION_ARRAY_SIZE = 2
private const val X_INDEX = 0
private const val Y_INDEX = 1

fun View.getRectOnScreen(): Rect {
    val location = getLocationOnScreen()

    return Rect(
            location[X_INDEX],
            location[Y_INDEX],
            location[X_INDEX] + width,
            location[Y_INDEX] + height
    )
}

fun View.getLocationOnScreen(): IntArray {
    return IntArray(LOCATION_ARRAY_SIZE).apply {
        getLocationOnScreen(this)
    }
}