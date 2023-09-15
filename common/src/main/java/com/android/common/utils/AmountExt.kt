package com.android.common.utils

import kotlin.math.absoluteValue

// will make sure the float only has 2 digits (ex : 10.55e)
fun Float.twoDigits(): String {
    return if (this.absoluteValue % 1.0 >= 0.005) String.format("%.2f", this)
    else String.format("%d", this.toInt())
}