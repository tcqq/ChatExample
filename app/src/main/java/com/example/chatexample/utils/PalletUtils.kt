/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.chatexample.utils

import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.core.graphics.ColorUtils.HSLToColor
import androidx.core.graphics.ColorUtils.colorToHSL
import androidx.palette.graphics.Palette

/**
 * Utility methods for working with colors.
 */
object PalletUtils {

    private const val IS_LIGHT = 0
    private const val IS_DARK = 1
    private const val LIGHTNESS_UNKNOWN = 2

    /**
     * Set the alpha component of `color` to be `alpha`.
     */
    fun modifyAlpha(@ColorInt color: Int, @IntRange(from = 0, to = 255) alpha: Int): Int {
        return color and 0x00ffffff or (alpha shl 24)
    }

    /**
     * Set the alpha component of `color` to be `alpha`.
     */
    fun modifyAlpha(
        @ColorInt color: Int,
        @FloatRange(from = 0.0, to = 1.0) alpha: Float
    ): Int {
        return modifyAlpha(color, (255f * alpha).toInt())
    }

    /**
     * Blend `color1` and `color2` using the given ratio.
     *
     * @param ratio of which to blend. 0.0 will return `color1`, 0.5 will give an even blend,
     * 1.0 will return `color2`.
     */
    @ColorInt
    fun blendColors(
        @ColorInt color1: Int,
        @ColorInt color2: Int,
        @FloatRange(from = 0.0, to = 1.0) ratio: Float
    ): Int {
        val inverseRatio = 1f - ratio
        val a = Color.alpha(color1) * inverseRatio + Color.alpha(color2) * ratio
        val r = Color.red(color1) * inverseRatio + Color.red(color2) * ratio
        val g = Color.green(color1) * inverseRatio + Color.green(color2) * ratio
        val b = Color.blue(color1) * inverseRatio + Color.blue(color2) * ratio
        return Color.argb(a.toInt(), r.toInt(), g.toInt(), b.toInt())
    }

    /**
     * Checks if the most populous color in the given palette is dark
     *
     *
     * Annoyingly we have to return this Lightness 'enum' rather than a boolean as palette isn't
     * guaranteed to find the most populous color.
     */
    fun isDark(palette: Palette): Int {
        val mostPopulous = getMostPopulousSwatch(palette)
            ?: return LIGHTNESS_UNKNOWN
        return if (isDark(mostPopulous.hsl)) IS_DARK else IS_LIGHT
    }

    fun getMostPopulousSwatch(palette: Palette?): Palette.Swatch? {
        var mostPopulous: Palette.Swatch? = null
        if (palette != null) {
            for (swatch in palette.swatches) {
                if (mostPopulous == null || swatch.population > mostPopulous.population) {
                    mostPopulous = swatch
                }
            }
        }
        return mostPopulous
    }

    /**
     * Determines if a given bitmap is dark. This extracts a palette inline so should not be called
     * with a large image!! If palette fails then check the color of the specified pixel
     */
    @JvmOverloads
    fun isDark(bitmap: Bitmap, backupPixelX: Int = bitmap.width / 2, backupPixelY: Int = bitmap.height / 2): Boolean {
        // first try palette with a small color quant size
        val palette = Palette.from(bitmap).maximumColorCount(3).generate()
        return if (palette.swatches.size > 0) {
            isDark(palette) == IS_DARK
        } else {
            // if palette failed, then check the color of the specified pixel
            isDark(bitmap.getPixel(backupPixelX, backupPixelY))
        }
    }

    /**
     * Check that the lightness value (0–1)
     */
    fun isDark(hsl: FloatArray): Boolean { // @Size(3)
        return hsl[2] < 0.5f
    }

    /**
     * Convert to HSL & check that the lightness value
     */
    fun isDark(@ColorInt color: Int): Boolean {
        val hsl = FloatArray(3)
        colorToHSL(color, hsl)
        return isDark(hsl)
    }

    /**
     * Calculate a variant of the color to make it more suitable for overlaying information. Light
     * colors will be lightened and dark colors will be darkened
     *
     * @param color               the color to adjust
     * @param isDark              whether `color` is light or dark
     * @param lightnessMultiplier the amount to modify the color e.g. 0.1f will alter it by 10%
     * @return the adjusted color
     */
    @ColorInt
    fun scrimify(
        @ColorInt color: Int,
        isDark: Boolean,
        @FloatRange(from = 0.0, to = 1.0) lightnessMultiplier: Float
    ): Int {
        var lightness = lightnessMultiplier
        val hsl = FloatArray(3)
        colorToHSL(color, hsl)

        lightness += if (!isDark) {
            1f
        } else {
            1f - lightnessMultiplier
        }


        hsl[2] = Math.max(0f, Math.min(1f, hsl[2] * lightness))
        return HSLToColor(hsl)
    }

    @ColorInt
    fun scrimify(
        @ColorInt color: Int,
        @FloatRange(from = 0.0, to = 1.0) lightnessMultiplier: Float
    ): Int {
        return scrimify(
            color,
            isDark(color),
            lightnessMultiplier
        )
    }
}