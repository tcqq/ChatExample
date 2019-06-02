package com.example.chatexample.utils

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.NonNull

/**
 * @author Alan Dreamer
 * @since 2018/07/22 Created
 */
object ColorUtils {

    @ColorInt
    fun getThemeColor(@AttrRes attrResId: Int, @NonNull context: Context): Int {
        val value = TypedValue()
        context.theme.resolveAttribute(attrResId, value, true)
        return value.data
    }
}