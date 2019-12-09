package dev.demilab.cardholder_android.util

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Typeface
import java.util.*

class FontLoader {

    companion object {
        fun light(context: Context): Typeface {
            val am: AssetManager = context.assets
            return Typeface.createFromAsset(
                am, String.format(
                    Locale.US,
                    "fonts/%s", "RobotoMono-Light.ttf"
                )
            )
        }

        fun regular(context: Context): Typeface {
            val am: AssetManager = context.assets
            return Typeface.createFromAsset(
                am, String.format(
                    Locale.US,
                    "fonts/%s", "RobotoMono-Regular.ttf"
                )
            )
        }

        fun medium(context: Context): Typeface {
            val am: AssetManager = context.assets
            return Typeface.createFromAsset(
                am, String.format(
                    Locale.US,
                    "fonts/%s", "RobotoMono-Medium.ttf"
                )
            )
        }

        fun thin(context: Context): Typeface {
            val am: AssetManager = context.assets
            return Typeface.createFromAsset(
                am, String.format(
                    Locale.US,
                    "fonts/%s", "RobotoMono-Thin.ttf"
                )
            )
        }
    }
}
